/**
 * @file    main.c
 * @brief   嵌入式实验7：基于uC/OS-II的多任务协作系统
 * @author  周俊哲
 * @date    2025-05-18
 * @version 2.0
 *
 * @details
 * 本程序实现基于uC/OS-II实时操作系统的多任务协作系统，包含：
 * - LED控制任务
 * - 数码管显示任务
 * - 按键处理任务
 * - 系统监控任务
 * 使用消息队列和信号量实现任务间通信
 */

#include "bsp.h" // 硬件抽象层
#include "includes.h"

/**
 * @defgroup 系统配置
 * @brief 系统全局配置参数
 * @{
 */

#define TASK_STK_SIZE 512 ///< 任务栈大小
#define MSG_QUEUE_SIZE 10 ///< 消息队列容量
#define MEM_POOL_SIZE 20  ///< 内存分区大小

/** @} */ // end of 系统配置

/**
 * @defgroup 全局变量
 * @brief 系统全局资源定义
 * @{
 */

OS_STK LED_TaskStk[TASK_STK_SIZE];     ///< LED任务堆栈
OS_STK Display_TaskStk[TASK_STK_SIZE]; ///< 显示任务堆栈
OS_STK Input_TaskStk[TASK_STK_SIZE];   ///< 输入任务堆栈
OS_STK Monitor_TaskStk[TASK_STK_SIZE]; ///< 监控任务堆栈

OS_EVENT *MsgQueue; ///< 消息队列指针
OS_MEM *MsgPool;    ///< 内存分区指针
OS_EVENT *LedSem;   ///< LED控制信号量

/** @} */ // end of 全局变量

/**
 * @struct MSG
 * @brief 任务间通信消息结构体
 * @ingroup 数据结构
 *
 * @details 用于在LED任务和显示任务之间传递控制参数
 */
typedef struct {
  INT8U display_value; ///< 显示数值 取值范围0-9
  INT8U led_pattern;   /**< LED显示模式
                           - 0x01: 单闪模式
                           - 0x02: 双闪模式
                           - 0x03: 流水灯模式 */
} MSG;

/* 函数前置声明 */
static void App_CreateResources(void);
void LED_Task(void *pdata);
void Display_Task(void *pdata);
void Input_Task(void *pdata);
void Monitor_Task(void *pdata);

/********************** 主函数 **********************/
/**
 * @brief 系统主函数
 * @details 程序入口，完成系统初始化和任务创建
 *
 * @return int 程序返回值（实际不会返回）
 *
 * @note 本函数不会正常返回，将一直运行在操作系统的任务调度中
 */
int main(void) {
  /* 硬件初始化 */
  BSP_Init();

  /* 初始化uC/OS-II */
  OSInit();

  /* 创建系统资源 */
  App_CreateResources();

  /* 创建应用任务 */
  OSTaskCreate(LED_Task, NULL, &LED_TaskStk[TASK_STK_SIZE - 1], 3);
  OSTaskCreate(Display_Task, NULL, &Display_TaskStk[TASK_STK_SIZE - 1], 4);
  OSTaskCreate(Input_Task, NULL, &Input_TaskStk[TASK_STK_SIZE - 1], 5);
  OSTaskCreate(Monitor_Task, NULL, &Monitor_TaskStk[TASK_STK_SIZE - 1], 2);

  /* 启动多任务调度 */
  OSStart();

  return 0;
}

/********************** 系统资源初始化 **********************/
/**
 * @brief 创建系统资源
 * @ingroup 系统初始化
 *
 * @details 初始化系统运行所需的通信资源和内存管理：
 * - 创建消息内存池
 * - 创建消息队列
 * - 创建信号量
 *
 * @warning 必须在任务创建前调用
 */
static void App_CreateResources(void) {
  INT8U err;
  void *MemPartition[MEM_POOL_SIZE];

  /* 创建内存分区 */
  MsgPool = OSMemCreate(MemPartition, MEM_POOL_SIZE, sizeof(MSG), &err);

  /* 创建消息队列 */
  MsgQueue = OSQCreate(&MemPartition[0], MSG_QUEUE_SIZE);

  /* 创建信号量 */
  LedSem = OSSemCreate(0);
}

/********************** LED控制任务 **********************/
/**
 * @brief LED控制任务函数
 * @param pdata 任务参数（未使用）
 * @ingroup 应用任务
 *
 * @details 任务工作流程：
 * 1. 等待信号量触发
 * 2. 从消息队列获取控制参数
 * 3. 根据模式更新LED显示
 * 4. 延时500ms
 *
 * @note LED模式由消息中的led_pattern字段决定
 */
void LED_Task(void *pdata) {
  INT8U err;
  MSG *pmsg;

  (void)pdata; // 消除未使用参数警告

  while (1) {
    /* 等待信号量 */
    OSSemPend(LedSem, 0, &err);

    /* 从消息队列获取消息 */
    pmsg = (MSG *)OSQPend(MsgQueue, 0, &err);

    /* 执行LED控制 */
    switch (pmsg->led_pattern) {
    case 0x01: // 单闪模式
      BSP_LED_Set(0x01);
      OSTimeDlyHMSM(0, 0, 0, 500);
      BSP_LED_Set(0x00);
      break;

    case 0x02: // 双闪模式
      BSP_LED_Set(0x03);
      OSTimeDlyHMSM(0, 0, 0, 250);
      BSP_LED_Set(0x00);
      OSTimeDlyHMSM(0, 0, 0, 250);
      break;

    case 0x03: // 流水灯模式
      for (INT8U i = 0; i < 4; i++) {
        BSP_LED_Set(0x01 << i);
        OSTimeDlyHMSM(0, 0, 0, 200);
      }
      break;

    default:
      break;
    }

    /* 释放消息内存 */
    OSMemPut(MsgPool, pmsg);
  }
}

/********************** 数码管显示任务 **********************/
/**
 * @brief 数码管显示任务函数
 * @param pdata 任务参数（未使用）
 * @ingroup 应用任务
 *
 * @details 任务工作流程：
 * 1. 从消息队列获取显示数值
 * 2. 更新数码管显示
 * 3. 延时1秒
 *
 * @warning 显示值超过9时将不更新显示
 */
void Display_Task(void *pdata) {
  INT8U err;
  MSG *pmsg;

  (void)pdata;

  while (1) {
    pmsg = (MSG *)OSQPend(MsgQueue, 0, &err);

    if (pmsg->display_value <= 9) {
      BSP_7Seg_Display(pmsg->display_value);
    }

    OSMemPut(MsgPool, pmsg);
    OSTimeDlyHMSM(0, 0, 1, 0);
  }
}

/********************** 按键处理任务 **********************/
/**
 * @brief 按键处理任务函数
 * @param pdata 任务参数（未使用）
 * @ingroup 应用任务
 *
 * @details 任务工作流程：
 * 1. 扫描按键状态
 * 2. 检测到有效按键时创建消息
 * 3. 发送消息到队列
 * 4. 触发信号量
 * 5. 延时100ms防抖
 *
 * @note 按键值直接作为显示数值，LED模式循环切换
 */
void Input_Task(void *pdata) {
  INT8U key_val;
  INT8U err;
  MSG *pmsg;

  (void)pdata;

  while (1) {
    key_val = BSP_Key_Scan();

    if (key_val != 0xFF) {
      pmsg = (MSG *)OSMemGet(MsgPool, &err);

      if (err == OS_NO_ERR) {
        pmsg->display_value = key_val;
        pmsg->led_pattern = 0x01 + (key_val % 3);

        OSQPost(MsgQueue, pmsg);
        OSSemPost(LedSem);
      }
    }

    OSTimeDlyHMSM(0, 0, 0, 100);
  }
}

/********************** 系统监控任务 **********************/
/**
 * @brief 系统监控任务函数
 * @param pdata 任务参数（未使用）
 * @ingroup 系统任务
 *
 * @details 任务工作流程：
 * 1. 执行系统状态检查
 * 2. 喂看门狗
 * 3. 延时5秒
 *
 * @note 需要启用OS_TASK_STAT_EN宏定义
 */
void Monitor_Task(void *pdata) {
  (void)pdata;

  while (1) {
#if OS_TASK_STAT_EN > 0
    OS_TaskStat();
#endif

    BSP_WDT_Feed();
    OSTimeDlyHMSM(0, 0, 5, 0);
  }
}

/********************** 硬件抽象层 **********************/
/**
 * @defgroup BSP 硬件抽象层
 * @brief 硬件平台相关函数实现
 * @{
 */

/**
 * @brief 初始化硬件平台
 * @details 初始化系统时钟、GPIO、外设等
 * @note 必须在所有任务创建前调用
 */
void BSP_Init(void) {
  // 具体实现依赖于目标硬件
}

/**
 * @brief 设置LED显示模式
 * @param pattern LED模式位图
 *
 * @details 根据输入位图控制LED状态：
 * - bit0: LED1
 * - bit1: LED2
 * - bit2: LED3
 * - bit3: LED4
 */
void BSP_LED_Set(INT8U pattern) {
  // 具体实现依赖于目标硬件
}

/**
 * @brief 数码管显示数字
 * @param num 要显示的数字（0-9）
 *
 * @warning 输入超过9时将不更新显示
 */
void BSP_7Seg_Display(INT8U num) {
  // 具体实现依赖于目标硬件
}

/**
 * @brief 扫描按键状态
 * @return INT8U 按键值（0-9）或0xFF表示无按键
 *
 * @details 返回当前按下的按键编号
 */
INT8U BSP_Key_Scan(void) {
  // 具体实现依赖于目标硬件
  return 0xFF;
}

/**
 * @brief 喂看门狗
 * @details 重置看门狗定时器防止系统复位
 */
void BSP_WDT_Feed(void) {
  // 具体实现依赖于目标硬件
}

/** @} */ // end of BSP
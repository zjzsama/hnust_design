#include <math.h>
#include <stdio.h>

#define MAX 10000

#define MAX_LEN 100 // 最大请求数
int io[MAX_LEN], io_len = 0, now; // io_len表示io请求数；now表示当前磁头所在磁道

void m_printf();
void init();   // 初始化
void FIFO();   // 先进先出算法
void SSTF();   // 最短服务时间优先算法
void SCAN();   // 电梯算法
void C_SCAN(); // 循环扫描算法

int main() {
  int choice, flag = 0, flag0; // flag0 = 0时表示选择成功；

  do {
    flag0 = 1;
    init();
    while (flag0) {
      printf("请选择使用的磁盘调度算法：1_FIFO  |  2_SSTF  |  3_SCAN  |  "
             "4_C-SCAN\n");
      scanf("%d", &choice);
      switch (choice) {
      case 1:
        FIFO();
        flag0 = 0;
        break;
      case 2:
        SSTF();
        flag0 = 0;
        break;
      case 3:
        SCAN();
        flag0 = 0;
        break;
      case 4:
        C_SCAN();
        flag0 = 0;
        break;
      default:
        printf("没有此选项\n");
      }
    }
    printf("是否继续？（是：1；否：0)：");
    scanf("%d", &flag);
  } while (flag);
}

void init() {
  printf("请输入io请求：(输入-1终止)\n");
  int i = 0;
  while (i < MAX_LEN) {
    scanf("%d", &io[i]);
    if (io[i] == -1)
      break;
    i++;
  }
  io_len = i;
  printf("请输入当前磁头位置：");
  scanf("%d", &now);
}

void m_printf() {
  int i;
  for (i = 0; i < MAX_LEN; i++) {
    printf("%d, ", io[i]);
  }
}

void FIFO() {
  int i, len; // len表示横跨的磁道数
  double sum = 0;

  printf("下一个被访问的磁道\t横跨的磁道数\n");

  for (i = 0; i < io_len; i++) {
    len = abs(now - io[i]);
    now = io[i];
    sum += len;
    printf("%d\t\t\t%d\n", io[i], len);
  }

  printf("平均寻道长度：%.1f\n", sum / io_len);
}

void SSTF() {
  int i, min = MAX, min_num,
         num = 0; // min表示最短服务时间；num表示已服务的io操作数量
  double sum = 0;

  printf("下一个被访问的磁道\t横跨的磁道数\n");
  while (num < io_len) {
    for (i = 0; i < io_len; i++) {
      if (min > abs(now - io[i])) {
        min = abs(now - io[i]);
        min_num = i;
      }
    }
    sum += min;
    printf("%d\t\t\t%d\n", io[min_num], min);
    now = io[min_num];
    num++;
    io[min_num] = min = MAX;
  }

  printf("平均寻道长度：%.1f\n", sum / io_len);
}

void SCAN() {
  int i, j, len, temp,
      now_index; // now_index表示大于且离now最近的磁道所在数组下标
  double sum = 0;

  printf("下一个被访问的磁道\t横跨的磁道数\n");

  // 将io[]排序
  for (i = 0; i < io_len - 1; i++) {
    for (j = i + 1; j < io_len; j++) {
      if (io[i] > io[j]) {
        temp = io[i];
        io[i] = io[j];
        io[j] = temp;
      }
    }
  }

  // 找now_index
  for (i = 0; i < io_len; i++) {
    if (io[i] >= now) {
      now_index = i;
      break;
    }
  }

  // 主程序
  for (i = now_index; i < io_len; i++) {
    len = io[i] - now;
    now = io[i];
    sum += len;
    printf("%d\t\t\t%d\n", io[i], len);
  }
  for (i = now_index - 1; i >= 0; i--) {
    len = now - io[i];
    now = io[i];
    sum += len;
    printf("%d\t\t\t%d\n", io[i], len);
  }

  printf("平均寻道长度：%.1f\n", sum / io_len);
}

void C_SCAN() {
  int i, j, len, temp,
      now_index; // now_index表示大于且离now最近的磁道所在数组下标
  double sum = 0;

  printf("下一个被访问的磁道\t横跨的磁道数\n");

  // 将io[]排序
  for (i = 0; i < io_len - 1; i++) {
    for (j = i + 1; j < io_len; j++) {
      if (io[i] > io[j]) {
        temp = io[i];
        io[i] = io[j];
        io[j] = temp;
      }
    }
  }

  // 找now_index
  for (i = 0; i < io_len; i++) {
    if (io[i] >= now) {
      now_index = i;
      break;
    }
  }

  // 主程序
  for (i = now_index; i < io_len; i++) {
    len = abs(io[i] - now);
    now = io[i];
    sum += len;
    printf("%d\t\t\t%d\n", io[i], len);
  }
  for (i = 0; i < now_index; i++) {
    len = abs(io[i] - now);
    now = io[i];
    sum += len;
    printf("%d\t\t\t%d\n", io[i], len);
  }

  printf("平均寻道长度：%.1f\n", sum / io_len);
}

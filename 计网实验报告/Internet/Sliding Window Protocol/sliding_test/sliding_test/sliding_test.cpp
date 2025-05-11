#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <windows.h>
#include <time.h>
#include <iostream>
using namespace std;

#define WINDOW_SIZE 4 // 窗口大小
#define TOTAL_FRAMES 10 // 总帧数
#define TIMEOUT 2 // 超时时间（秒）

double send_time = 0;
double receive_time;
int framesSent = 0;  // 发送的帧数
int framesReceived = 0; // 接收的帧数

// 帧结构体，包含帧序号和是否被确认的状态
typedef struct {
    int seq_num;  // 帧的序号 
    bool isAck;   // 是否是确认帧
} Frame;

// 发送方结构体
typedef struct {
    int windowStart;  // 滑动窗口的起始位置
    Frame frames[TOTAL_FRAMES]; // 所有帧 
    bool ackReceived[TOTAL_FRAMES]; // 存储ACK确认状态
} Sender;

// 接收方结构体
typedef struct {
    bool receivedFrames[TOTAL_FRAMES]; // 标记是否收到该帧
} Receiver;

// 发送方初始化
void initSender(Sender* sender) {
    sender->windowStart = 0;
    for (int i = 0; i < TOTAL_FRAMES; i++) {
        sender->frames[i].seq_num = i;
        sender->frames[i].isAck = false;
        sender->ackReceived[i] = false;
    }
}

// 接收方初始化
void initReceiver(Receiver* receiver) {
    for (int i = 0; i < TOTAL_FRAMES; i++) {
        receiver->receivedFrames[i] = false;
    }
}

// 更新窗口，滑动窗口的起始位置移动
void updateWindow(Sender* sender) {
    while (sender->ackReceived[sender->windowStart]) {
        sender->ackReceived[sender->windowStart] = false; // 重置确认状态 
        sender->windowStart++;
        if (sender->windowStart >= TOTAL_FRAMES) break;
    }
}

// 发送ACK
void sendAck(int seq_num) {
    printf("接收方:\t发送帧 %d 的ACT\n", seq_num);
    framesReceived++;
}

// 接收帧
void receiveFrame(Receiver* receiver, Frame frame) {
    if (frame.seq_num < TOTAL_FRAMES) {
        printf("接收方:\t接收方收到帧: %d\n", frame.seq_num);
        receiver->receivedFrames[frame.seq_num] = true;

        // 模拟随机丢包，70%概率发送ACK
        if (rand() % 10 < 7) {
            sendAck(frame.seq_num);
        }
    }
}

void receiveAck(Sender* sender);
// 发送帧
void sendFrames(Sender* sender, double& sendTime) {
    cout << endl;
    clock_t startSend = clock(); // 记录发送开始时间
    while (framesSent < TOTAL_FRAMES) {
        // 填充当前窗口 
        printf("发送方:\t发送窗口起始帧 %d\n", sender->windowStart);
        for (int i = sender->windowStart; i < sender->windowStart + WINDOW_SIZE && i < TOTAL_FRAMES; i++) {
            printf("发送方:\t发送帧: %d\n", sender->frames[i].seq_num);
            framesSent++;
        }
        cout << endl;
        clock_t endSend = clock(); // 记录发送结束时间
        sendTime = double(endSend - startSend) / CLOCKS_PER_SEC; // 计算发送所用时间
        // 模拟发送完所有帧后等待确认 
        Sleep(1000);

        // 模拟接收ACK 
        receiveAck(sender);
    }
}
// 发送单个帧
void sendFrame(Sender* sender, Frame frame) {
    // 模拟发送帧的逻辑
    printf("发送方:\t发送帧: %d\n", frame.seq_num);
    
    Sleep(500); // 模拟发送延迟
    // 发送后，增加发送计数 
    framesSent++;
}

// 接收ACK确认
void receiveAck(Sender* sender) {
    double sendTime;
    for (int i = sender->windowStart; i < sender->windowStart + WINDOW_SIZE && i < TOTAL_FRAMES; i++) {
        // 随机模拟丢包，70%概率确认收到
        if (rand() % 10 < 7) {
            printf("发送方:\t收到帧 %d 的ACT\n", sender->frames[i].seq_num);
            sender->ackReceived[i] = true;
            framesReceived++;
        }
        else {
            printf("发送方:\t未收到帧 %d 的ACT,准备重传\n", sender->frames[i].seq_num);
            sendFrame(sender, sender->frames[i]);
        }
    }
    cout << endl;
    // 更新窗口 
    updateWindow(sender);
}


// 线程函数：接收方接收数据
DWORD WINAPI receiverThread(LPVOID arg) {
    Sender* sender = (Sender*)arg;
    Receiver receiver;
    clock_t startReceive = clock(); // 记录接收开始时间
    initReceiver(&receiver);

    while (framesReceived < TOTAL_FRAMES) {
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            if (!sender->ackReceived[i]) {
                receiveFrame(&receiver, sender->frames[i]);
            }
        }
    }
    clock_t endReceive = clock(); // 记录接收结束时间
    double receiveTime = double(endReceive - startReceive) / CLOCKS_PER_SEC; // 计算接收所用时间 
    receive_time = receiveTime;
    return 0;
}

// 线程函数：发送方发送数据
DWORD WINAPI senderThread(LPVOID arg) {
    Sender* sender = (Sender*)arg;
    double sendTime;
    sendFrames(sender, sendTime);
    send_time = sendTime;
    
    
    return 0;
}

int main() {
    srand(time(NULL)); // 用于生成随机数，模拟丢包和ACK确认

    Sender sender;
    initSender(&sender);

    HANDLE senderT, receiverT;

    // 启动发送和接收线程
    senderT = CreateThread(NULL, 0, senderThread, &sender, 0, NULL);
    receiverT = CreateThread(NULL, 0, receiverThread, &sender, 0, NULL);

    // 等待线程完成 
    WaitForSingleObject(senderT, INFINITE);
    WaitForSingleObject(receiverT, INFINITE);

    printf("发送速度: %.2f 帧/秒\n", framesSent / send_time);// 输出发送速度 
    printf("接收速度: %.2f 帧/秒\n", framesReceived / receive_time); //输出接收速度
    printf("总共发送的帧数: %d\n", framesSent);
    printf("总共接收的帧数: %d\n", framesReceived);

    // 关闭线程句柄
    CloseHandle(senderT);
    CloseHandle(receiverT);

    return 0;
}

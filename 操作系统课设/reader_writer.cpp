#include <iostream>
#include <stdlib.h>
#include <time.h>
#include <windows.h>
using namespace std;

const int READER_COUNT = 3;                            // 读者个数
const int WRITER_COUNT = 1;                            // 写者个数
const int THREADS_COUNT = READER_COUNT + WRITER_COUNT; // 总的线程数
int num = 0; // 记录当前有几个读线程在访问文件
int flag = 1,
    flag0[THREADS_COUNT]; // flag标识子线程运行中；flag0标识子线程完成一次操作
HANDLE Mutex; // 用于保证对count变量的互斥访问
HANDLE rw;    // 用于实现对共享文件的互斥访问
typedef struct {
  int id;
} threadP;

DWORD WINAPI reader(LPVOID); // 读者线程
DWORD WINAPI writer(LPVOID); // 写者线程

int main() {
  int i;
  // 创建各个互斥信号
  Mutex = CreateMutex(NULL, FALSE, NULL);
  rw = CreateSemaphore(NULL, 1, INT_MAX, NULL);

  HANDLE hThreads[THREADS_COUNT]; // 各线程的handle
  DWORD readerID[READER_COUNT];   // 生产者线程的标识符
  DWORD writerID[WRITER_COUNT];   // 消费者线程的标识符

  threadP tp[THREADS_COUNT];
  // 创建读者线程
  for (i = 0; i < READER_COUNT; i++) {
    tp[i].id = i + 1;
    hThreads[i] = CreateThread(NULL, 0, reader, &tp[i], 0, &readerID[i]);
    if (hThreads[i] == NULL)
      return -1;
  }
  // 创建写者线程
  for (i = 0; i < WRITER_COUNT; i++) {
    tp[READER_COUNT + i].id = i + 1;
    hThreads[READER_COUNT + i] =
        CreateThread(NULL, 0, writer, &tp[READER_COUNT + i], 0, &writerID[i]);
    if (hThreads[READER_COUNT + i] == NULL)
      return -1;
  }

  while (flag) {
    if (getchar()) { // 键盘输入后终止程序运行
      flag = 0;
    }
  }
  for (i = 0; i < THREADS_COUNT; i++) {
    while (flag0[i]) {
    }
  }
  printf("主程序结束。\n");

  return 0;
}

DWORD WINAPI reader(LPVOID lpPara) {
  threadP *tp = (threadP *)lpPara;
  int id = tp->id;
  srand((unsigned)time(NULL)); // 放在循环外部
  while (flag) {
    flag0[id - 1] = 1;
    WaitForSingleObject(Mutex, INFINITE); // P(Mutex)
    num++;
    if (num == 1) // 第一个读线程给共享文件加锁
      WaitForSingleObject(rw, INFINITE); // P(rw)
    if (!flag) { // 当主程序终止时，还没开始操作子线程终止
      flag0[id - 1] = 0;
      ReleaseMutex(Mutex); // V(Mutex) - 确保释放Mutex
      return 0;
    }
    printf("读线程%d读文件中...\n", id);
    ReleaseMutex(Mutex); // V(Mutex)

    // 模拟读文件操作
    Sleep(rand() % 3000);

    WaitForSingleObject(Mutex, INFINITE); // P(Mutex)
    printf("读线程%d读文件结束\n", id);
    num--;
    if (num == 0) // 当没有读线程时给共享文件开锁
      ReleaseSemaphore(rw, 1, NULL); // V(rw)
    ReleaseMutex(Mutex);             // V(Mutex)
    flag0[id - 1] = 0;
  }
  return 0;
}

DWORD WINAPI writer(LPVOID lpPara) {
  threadP *tp = (threadP *)lpPara;
  int id = tp->id;
  srand((unsigned)time(NULL)); // 放在循环外部
  while (flag) {
    flag0[id + READER_COUNT - 1] = 1;
    WaitForSingleObject(rw, INFINITE); // P(rw)
    if (!flag) { // 当主程序终止时，还没开始操作子线程终止
      flag0[id + READER_COUNT - 1] = 0;
      ReleaseSemaphore(rw, 1, NULL); // 确保释放Semaphore
      return 0;
    }
    // 模拟写文件
    printf("写线程%d写文件中...\n", id);
    Sleep(rand() % 5000);
    printf("写线程%d写文件结束\n", id);

    ReleaseSemaphore(rw, 1, NULL); // V(rw)
    flag0[id + READER_COUNT - 1] = 0;
  }
  return 0;
}

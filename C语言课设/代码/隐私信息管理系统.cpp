#include <assert.h> //判断指针是否为空
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct mess {
  int num;           // 编号
  char place[50];    // 账号位置
  char describe[50]; // 账号描述
  char name[20];     // 账号名字
  char code[20];     // 账号密码
};
struct Node {
  struct mess data;
  struct Node *next;
};
struct Node *Creathead();                         // 创造表头
struct Node *Creatnode();                         // 创造结点
int Verification();                               // 判断密码是否正确
void Insert(struct Node *head, struct mess data); // 录入数据
void Modify();                                    // 修改数据
struct Node *Search(struct Node *head, int n);    // 查找数据
void Delete(struct Node *head, int n);            // 删除数据
void Print(struct Node *head);                    // 打印数据
void Makemenu();                                  // 创建菜单
void Keydown();                                   // 读入客户输入的数字
void Savefile(struct Node *head, const char *fileURL); // 写文件
void Readfile(struct Node *head, const char *fileURL); // 读文件

struct Node *list = NULL; // 创造一个全局变量
int main() {
  if (Verification() == 0)
    return 0;
  list = Creathead();            // 创造头结点
  Readfile(list, "message.txt"); // 程序运行时读文件
  while (1) {
    Makemenu();
    Keydown();
    system("pause");
    system("cls"); // 清屏处理
  }
}
void Keydown() {
  int x, n = 0, i;
  struct Node data;
  struct mess data1;
  struct Node *result = NULL;
  struct Node *result1 = NULL;
  char s[20];
  scanf("%d", &x);
  switch (x) {
  case 0:
    printf("退出成功!\n");
    system("pause");
    exit(0);
    break;
  case 1:
    printf("请输入数据编号:");
    scanf("%d", &data1.num);
    printf("请输入数据位置:");
    scanf("%s", data1.place);
    printf("请输入数据描述:");
    scanf("%s", data1.describe);
    printf("请输入数据名字:");
    scanf("%s", data1.name);
    printf("请输入数据密码:");
    scanf("%s", s);
    for (i = 0; i < strlen(s); i++) {
      data1.code[i] = s[i] + 64;
    }
    data1.code[i] = '\0';
    Insert(list, data1);
    printf("录入成功!\n");
    // 保存
    Savefile(list, "message.txt");
    break;
  case 2:
    Print(list);
    break;
  case 3:
    printf("请输入要删除的数据的编号:");
    scanf("%d", &n);
    Delete(list, n);
    // 保存
    Savefile(list, "message.txt");
    break;
  case 4:
    printf("请输入要查询的数据的编号:");
    scanf("%d", &n);
    result1 = Search(list, n);
    if (result1 != NULL) {
      printf("编号:%d 位置:%s 描述:%s 名字:%s 密码:%s\n", result1->data.num,
             result1->data.place, result1->data.describe, result1->data.name,
             result1->data.code);
    } else
      printf("未找到指定数据");
    break;
  case 5:
    printf("请输入需要修改的数据的编号:");
    scanf("%d", &n);
    result1 = Search(list, n);
    if (result1 != NULL) {
      printf("请输入新的数据编号:");
      scanf("%d", &result1->data.num);
      printf("请输入新的数据位置:");
      scanf("%s", result1->data.place);
      printf("请输入新的数据描述:");
      scanf("%s", result1->data.describe);
      printf("请输入新的数据名字:");
      scanf("%s", result1->data.name);
      printf("请输入新的数据密码:");
      scanf("%s", s);
      for (i = 0; i < strlen(s); i++) {
        result1->data.code[i] = s[i] + 64;
      }
      result1->data.code[i] = '\0';
    } else
      printf("未找到指定数据,无法修改");
    // 保存
    Savefile(list, "message.txt");
    break;
  default:
    printf("输入错误,请重新输入");
    break;
  }
}
void Makemenu() {
  printf("---------隐私管理系统----------\n");
  printf("\t0.退出系统\n");
  printf("\t1.录入信息\n");
  printf("\t2.浏览信息\n");
  printf("\t3.删除信息\n");
  printf("\t4.查询信息\n");
  printf("\t5.修改信息\n");
  printf("-------------------------------\n");
  printf("输入你的选择0-5:");
}
int Verification() {
  int i;
  char key[50];
  printf("------程序密码验证------\n请输入密码:");
  scanf("%s", key);
  for (i = 0; i < 3; i++) { // 判断密码是否正确,共有三次机会
    if (strcmp(key, "114514") == 0) {
      printf("口令正确!\n");
      return 1;
    } else if (i < 2 && strcmp(key, "114514") != 0) {
      printf("密码错误,您还有%d次机会\n", 2 - i);
    }
    if (i < 2)
      scanf("%s", key);
  }
  if (i == 3) { // 密码错误则退出
    printf("您是非法用户！");
    return 0;
  }
}
struct Node *Creathead() {
  struct Node *head = (struct Node *)malloc(sizeof(struct Node)); // 创建表头
  assert(head);
  head->next = NULL;
  return head;
}
struct Node *Creatnode(struct mess data) {
  struct Node *p = (struct Node *)malloc(sizeof(struct Node));
  p->data = data;
  p->next = NULL;
}
void Insert(struct Node *head, struct mess data) {
  struct Node *p = Creatnode(data);
  p->next = head->next;
  head->next = p;
}
void Print(struct Node *head) {
  struct Node *pMove = head->next;
  while (pMove != NULL) {
    printf("编号:%d 位置:%s 描述:%s 名字:%s 密码:%s\n", pMove->data.num,
           pMove->data.place, pMove->data.describe, pMove->data.name,
           pMove->data.code);
    pMove = pMove->next;
  }
}
void Delete(struct Node *head, int n) {
  struct Node *ptr = head, *ptr1 = head->next;
  while (ptr1 != NULL && ptr1->data.num != n) {
    ptr = ptr1;
    ptr1 = ptr->next;
  }
  if (ptr1 != NULL) {
    ptr->next = ptr1->next;
    free(ptr1);
    printf("删除成功\n");
  }
}
struct Node *Search(struct Node *head, int n) {
  struct Node *p = head->next;
  while (p->next != NULL && p->data.num != n) {
    p = p->next;
  }
  return p;
}
void Readfile(struct Node *head, const char *fileURL) {
  FILE *fp = fopen(fileURL, "r");
  char s[20];
  int i;
  if (fp == NULL) {
    fp = fopen(fileURL, "w+"); // 如果没有文件则创建
    fclose(fp);
    return;
  }
  struct mess p;
  while (fscanf(fp, "%d\t%s\t%s\t%s\t%s\n", &p.num, p.place, p.describe, p.name,
                p.code) != EOF) { // 读入数据
    Insert(list, p);
  }
  fclose(fp);
}
void Savefile(struct Node *head, const char *fileURL) {
  FILE *fp = fopen(fileURL, "w");
  int i;
  struct Node *p = head->next;
  while (p != NULL) { // 写入数据
    fprintf(fp, "%d\t%s\t%s\t%s\t%s\n", p->data.num, p->data.place,
            p->data.describe, p->data.name, p->data.code);
    p = p->next;
  }
  fclose(fp);
}

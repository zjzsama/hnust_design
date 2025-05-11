#include<iostream>
using namespace std;

//定义单链表,参考教材P30
typedef struct LNode
{
	int data;
	struct LNode *next;
}LNode,*LinkList;

void MergeList_L(LinkList &LA,LinkList &LB); //函数声明

//单链表的初始化，参考教材P32
void InitList_L(LinkList &L)
{
	L=new LNode;
	L->next=NULL;
}

//尾插法创建单链表，参考教材P37--P38
void CreateList_L(LinkList L,int n)
{
	int i;
	LinkList p,r;
	r=L;
	//cout<<"请依次输入该表的各个元素：";
	for(i=0;i<n;i++)
	{
		p=new LNode;
		cin>>p->data;
		p->next=NULL;
		r->next=p;
		r=p;
	}
}

//依次输出单链表中的各个元素（逗号隔开）
void output(LinkList L)
{
	int i=0;
	LNode *p;
	p=L->next;
	while(p)
	{
		if(i)
			cout<<",";
		cout<<p->data;
		p=p->next;
		i=1;
	}
}

//完成MergeList_L()函数的定义，函数首部见前面的“函数声明”
/**********************   begin   *********************/
void MergeList_L(LinkList &LA,LinkList &LB){
/*	LinkList pa=LA->next,pb=LB->next,pc=LA;
	
	while(pa&&pb){
	if(pa->data<=pb->data){
		pc->next=pa;
		pc=pa;
		pa=pa->next;
	}
	else{
		pc->next=pb;
		pc=pb;
		pb=pb->next;
	}
}
	pc->next=pa?pa:pb;
	delete LB; 
	 
}*/ 
			//没有考虑两者相等时许删去的情况
	LinkList pa = LA->next;
	LinkList pb = LB->next;
	LinkList pc = LA;
	while (pa && pb)
	{
		if (pa->data < pb->data)
		{
			pc->next = pa;
			pc = pa;
			pa = pa->next;
		}
		else if (pb->data < pa->data)
		{
			pc->next = pb;
			pc = pb;
			pb = pb->next;
		}
		else
		{
			pc->next = pa;
			pc = pa;
			pa = pa->next;
			LinkList p = pb;
			pb = pb->next;
			delete p;
		}
	}
	if (pa)
	{
		pc->next = pa;
	}
	else
	{
		pc->next = pb;
	}
	delete LB;
}
/**********************    end    *********************/

int main(void)
{
	LinkList La,Lb,Lc;
	int num_a,num_b;

	//cout<<"请输入非递减单链表La的个数num_a：";
	cin>>num_a;
	InitList_L(La);			//La表的创建
	CreateList_L(La,num_a);	//依次往单链表La里输入数据

	//cout<<"请输入非递减单链表Lb的个数num_b：";
	cin>>num_b;
	InitList_L(Lb);			//Lb表的创建
	CreateList_L(Lb,num_b);	//依次往单链表La里输入数据

	MergeList_L(La,Lb);	//将单链表La和Lb 合并到La中

	//cout<<"非递减单链表La，Lb合并后的元素依次为：\n";
	output(La);             //输出合并后的单链表La
	cout<<endl;
	return 0;
}


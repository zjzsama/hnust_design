#include <stdio.h>
#include <stdlib.h>

struct ListNode {
    int data;
    struct ListNode *next;
};

struct ListNode *createlist();

int main()
{
    struct ListNode *p, *head = NULL;

    head = createlist();
    for ( p = head; p != NULL; p = p->next )
        printf("%d ", p->data);
    printf("\n");

    return 0;
}

/* 你的代码将被嵌在这里 */
struct ListNode *createlist(){
	struct ListNode *head=(struct ListNode*)malloc(sizeof(struct ListNode));
	head->next=NULL;
	while(1){
		struct ListNode *p=(struct ListNode*)malloc(sizeof(struct ListNode));
		scanf("%d",&p->data);
		if(p->data==-1) break;
		if(head->next==NULL){
			head->next=p;
			p->next=NULL;
		} else{
			p->next=head->next;
			head->next=p;
		}
	}
	return head->next; 
}


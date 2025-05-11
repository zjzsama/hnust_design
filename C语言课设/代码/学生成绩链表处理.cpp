#include <stdio.h>
#include <stdlib.h>

struct stud_node {
     int    num;
     char   name[20];
     int    score;
     struct stud_node *next;
};

struct stud_node *createlist();
struct stud_node *deletelist( struct stud_node *head, int min_score );

int main()
{
    int min_score;
    struct stud_node *p, *head = NULL;

    head = createlist();
    scanf("%d", &min_score);
    head = deletelist(head, min_score);
    for ( p = head; p != NULL; p = p->next )
        printf("%d %s %d\n", p->num, p->name, p->score);

    return 0;
}

/* 你的代码将被嵌在这里 */
typedef struct stud_node stu;
struct stud_node *deletelist( struct stud_node *head, int min_score )
{
    while(head&&head->score<min_score)
    {
        head=head->next;
    }
    if(!head)return NULL;
    stu *pt=head,*pu=head->next;
    while(pu)
    {
        if(pu->score<min_score)
        {
            pt->next=pu->next;
        }
        else pt=pu;
            pu=pt->next;
    }
    return head;



}

struct stud_node *createlist()
{
    stu* head=NULL,*tail=NULL;
    stu *p=(stu*)malloc(sizeof(stu));
    while(scanf("%d",&p->num)&&p->num!=0)
    {
        scanf("%s%d",p->name,&p->score);
        p->next=NULL;
        if(!head)head=p;
        else tail->next=p;
        tail=p;
        p=(stu*)malloc(sizeof(stu));

    }
    return head;


};


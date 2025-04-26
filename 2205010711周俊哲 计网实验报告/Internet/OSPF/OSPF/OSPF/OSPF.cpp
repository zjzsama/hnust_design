#include <iostream>
#include <iomanip>
#include <stdio.h>
#include <vector>
using namespace std;


const int Max = 1e5;		//最大权重
const int MVNum = 1e2;		//最大顶点数
const int OK = 1;

const int mm = 1e2;

typedef char VerTexType;	//顶点信息
typedef int OtherInfo;    	//和边相关的信息
typedef int ArcType;		//边的类型

typedef struct {
	int info;
	VerTexType vexs[MVNum];			//顶点表
	ArcType arcs[MVNum][MVNum];		//邻接矩阵
	int vexnum, arcnum;        		//图的当前点数和边数
} Graph;

//确定顶点v在G中的位置
int LocateVex(const Graph& g, VerTexType v) {
	for (int i = 0; i < g.vexnum; ++i)
		if (g.vexs[i] == v)
			return i;
	return -1;
}//LocateVex


void CreateGraph(Graph& g) {
	//采用邻接矩阵表示法，创建无向图G
	int i, j, k, w;
	char v1, v2;
	cout << "请输入顶点数：";
	cin >> g.vexnum;
	while (g.vexnum == 0) {
		cin.clear();
		cin.sync(); //清空缓冲区
		cout << "输入错误请重新输入：";
		cin >> g.vexnum;
	}
	cout << "请输入边数：";
	cin >> g.arcnum;
	while (g.arcnum == 0) {
		cin.clear();
		cin.sync(); //清空缓冲区
		cout << "输入错误请重新输入：";
		cin >> g.arcnum;
	}

	cout << "请输入" << g.vexnum << "个顶点的名称：";
	for (i = 0; i < g.vexnum; i++) {
		cin >> g.vexs[i];
	}
	for (i = 0; i < g.vexnum; i++) {
		for (j = 0; j < g.vexnum; j++) {
			if (i == j)g.arcs[i][j] = 0;
			else g.arcs[i][j] = Max;
		}
	}
	for (k = 0; k < g.arcnum; k++) {
		cout << "请输入第" << k + 1 << "条边的起点、终点和权重：";
		cin >> v1 >> v2 >> w;
		i = LocateVex(g, v1);
		j = LocateVex(g, v2);
		while (i == -1 || j == -1 || w == 0) {
			cin.clear();
			cin.sync(); //清空缓冲区
			cout << "输入错误请重新输入：";
			cin >> v1 >> v2 >> w;
			i = LocateVex(g, v1);
			j = LocateVex(g, v2);
		}
		g.arcs[i][j] = g.arcs[j][i] = w;
	}
}//CreateGraph

void shortDIJ(Graph g, int v0) {
	int i = 0, v, w, Min, n = g.vexnum;
	vector<int> s(g.vexnum, 0);    // 初始化为 0
	vector<int> d(g.vexnum, Max);  // 初始化为最大值
	vector<int> p(g.vexnum, -1);   // 初始化为 -1
	vector<char> temp(g.vexnum);
	for (v = 0; v < g.vexnum; v++) {
		s[v] = 0;
		d[v] = g.arcs[v0][v];
		if (d[v] < Max)p[v] = v0;
		else p[v] = -1;
	}
	s[v0] = 1;
	d[v0] = 0;
	for (i = 1; i < n; i++) {
		Min = Max;
		for (w = 0; w < n; w++) {
			if (!s[w] && d[w] < Min) {
				v = w;
				Min = d[w];
			}
		}
		s[v] = 1;
		for (w = 0; w < n; w++) {
			if (!s[w] && d[v] + g.arcs[v][w] < d[w]) {
				d[w] = d[v] + g.arcs[v][w];
				p[w] = LocateVex(g, g.vexs[v]);
			}
		}
	}
	for (i = 0; i < n; i++) {
		if (i != v0 && d[i] < Max) {
			cout << "从 " << g.vexs[v0] << " 到 " << g.vexs[i] << " 的最短路由为：";
			Min = 0;
			for (w = p[i]; w != v0; w = p[w]) {
				temp[Min++] = g.vexs[w];
			}
			cout << g.vexs[v0];
			for (w = Min - 1; w >= 0; w--) {
				cout << "->" << temp[w];
			}
			cout << "->" << g.vexs[i];
			cout << "\t，代价为：" << d[i];
			cout << "\n";
		}
		else if (d[i] == Max) {
			cout << -1 << " ";
		}
	}
	cout << "节点 " << g.vexs[v0] << " 的路由表如下：\n";
	cout << "---------\n";
	cout << "|   " << g.vexs[v0] << "\t|\n";
	cout << "---------\n";
	for (i = 0; i < n; i++) {
		if (g.vexs[i] != g.vexs[v0]) {
			cout << "| " << g.vexs[i] << "->" << d[i] << "\t|\n";
		}
	}
	cout << "---------\n";
}

void showGraph(Graph g) {
	int i;
	printf("\t%c", ' ');
	for (i = 0; i < g.vexnum; i++) {
		printf("\t%c", g.vexs[i]);
	}
	cout << "\n";
	for (i = 0; i < g.vexnum; i++) {
		printf("\t%c", g.vexs[i]);
		for (int j = 0; j < g.vexnum; j++) {
			if (g.arcs[i][j] == Max) {
				printf("\t%s", "∞");
			}
			else printf("\t%d", g.arcs[i][j]);
		}
		cout << "\n";
	}
}

int main() {
	Graph g;
	int a = mm;
	char v0;
	CreateGraph(g);
	showGraph(g);
	for (int i = 0; i < g.vexnum; i++) {
		a = LocateVex(g, 'a' + i);
		shortDIJ(g, a);
	}

	return 0;
}
//main
/*
6
8
a b c d e f
a b 4
a e 5
b f 6
b c 2
c d 3
c e 1
d f 7
e f 8
*/
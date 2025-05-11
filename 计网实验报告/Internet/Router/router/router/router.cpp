#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <iostream>
#include <limits.h>
#include <vector>
using namespace std;
#define MAX_ROUTERS 10
#define MAX_DESTINATIONS 10
#define PACKET_SIZE 1024
#define NETWORK_SIZE 5
const int Max = 1e5;		//最大权重
const int MVNum = 1e2;		//最大顶点数
const int OK = 1;
const int mm = 1e2;
typedef string VerTexType;	//顶点信息
typedef int OtherInfo;    	//和边相关的信息
typedef int ArcType;		//边的类型

//图的邻接表存储表示- - - - -
typedef struct {
	int info;
	VerTexType vexs[MVNum];			//顶点表
	ArcType arcs[MVNum][MVNum];		//邻接矩阵
	int vexnum, arcnum;        		//图的当前点数和边数
} Graph;

// 路由表条目
typedef struct {
	string destination;
	int distance;
	string nextHop;
} Route;
// 路由器
typedef struct {
	string name;
	Route table[MAX_DESTINATIONS];
	int tableSize;
} Router;
// 模拟网络中的路由器
Router network[NETWORK_SIZE] = {
 {"R1", {{"R2", 1, ""}, {"R3", 2, ""}}, 2},
 {"R2", {{"R1", 2, ""}, {"R3", 1, ""},{"R4",1,"R4"}}, 3},
 {"R3", {{"R5", 3, ""}, {"R2", 1, ""}}, 2},
 {"R4", {{"R5", 1, ""}, {"R2", 2, ""}}, 2},
 {"R5", {{"R3", 3, ""}, {"R4", 1, ""}}, 2}
};

//确定顶点v在邻接图中的位置
int LocateVex(const Graph& g, VerTexType v) {
	for (int i = 0; i < g.vexnum; ++i)
		if (g.vexs[i] == v)
			return i;
	return -1;
}//LocateVex

//创建邻接图
void CreateGraph(Graph& g) {
	//采用邻接矩阵表示法，创建无向图G
	int i, j, k, w;
	string v1, v2;
	g.vexnum = NETWORK_SIZE;
	
	for (i = 0;i < g.vexnum;i++) {
		g.vexs[i] = network[i].name;
	}

	for (i = 0; i < g.vexnum; i++) {
		for (j = 0; j < g.vexnum; j++) {
			if (i == j)g.arcs[i][j] = 0;
			else g.arcs[i][j] = Max;
		}
	}
	for (k = 0; k < g.vexnum; k++) {
		v1 = network[k].name;
		for (int m = 0;m < network[k].tableSize;m++) {
			v2 = network[k].table[m].destination;
			w = network[k].table[m].distance;
			i = LocateVex(g, v1);
			j = LocateVex(g, v2);
			g.arcs[i][j] = g.arcs[j][i] = w;
		}
		//		g.arcs[i][j]=w;
	}
}//CreateGraph

//显示邻接图
static void showGraph(Graph g) {
	int i;
	printf("\t%c", ' ');
	for (i = 0; i < g.vexnum; i++) {
		cout << "\t" << g.vexs[i];
	}
	cout << "\n";
	for (i = 0; i < g.vexnum; i++) {
		cout << "\t" << g.vexs[i];
		for (int j = 0; j < g.vexnum; j++) {
			if (g.arcs[i][j] == Max) {
				printf("\t%s", "∞");
			}
			else printf("\t%d", g.arcs[i][j]);
		}
		cout << "\n";
	}
}

// 查找路由器
Router * find_router(string name) {
	for (int i = 0; i < NETWORK_SIZE; i++) {
		if (network[i].name == name) {
			return &network[i];
		}
	}
	return NULL;
}

//查找路由器位置
int LocalRouter(string name) {
	for (int i = 0; i < NETWORK_SIZE; i++)
	{
		if (network[i].name == name) {
			return i;
		}
	}
	return -1;
}
 
//查找最佳路径 
static string find_best_path(Router* router, string destination) {
	for (int i = 0; i < router->tableSize; i++) {
		if (router->table[i].destination == destination) {
			return router->table[i].nextHop;
		}
	}
	return "Destination Unreachable";
}

//填充network中的table
static void shortDIJ(Graph g) {
	int i = 0, v, w,v0,router, Min, n = g.vexnum;
	vector<int> s(g.vexnum, 0);
	vector<int> d(g.vexnum, Max);
	vector<int> p(g.vexnum, -1);
	vector<string> temp(g.vexnum);
	for (v0 = 0;v0 < g.vexnum;v0++) {
		router = LocalRouter(g.vexs[v0]);
		for (v = 0; v < g.vexnum; v++) {
			s[v] = 0;
			d[v] = g.arcs[router][v];
			if (d[v] < Max)p[v] = router;
			else p[v] = -1;
		}
		s[router] = 1;
		d[router] = 0;
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
			if (i != router && d[i] < Max) {
				Min = 0;
				for (w = p[i]; w != router; w = p[w]) {
					temp[Min++] = g.vexs[w];
				}
				w = Min - 1;
				if (w >= 0) {
					//cout << "增加" << g.vexs[v0] << "的table" << endl;
					network[router].table[network[router].tableSize] = { g.vexs[i],d[i],temp[w] };
					network[router].tableSize++;
				}
			}
			else if (d[i] == Max) {
				cout << -1 << " ";
			}
		}
	}
}

// 模拟数据包发送
int send_packet(Router* current_router, string source, string destination, string packet) {
	cout << "Packet from " << source << " to " << destination << ":" << packet <<endl;
	string nextHop = find_best_path(current_router, destination);
	if (nextHop == "Destination Unreachable") {
		printf("Packet cannot be delivered.\n");
	}
	else {
		Router* next_router = find_router(nextHop);
		if (nextHop == destination) {
			return 0;
		}else if (next_router) {
			cout << "Packet from " << source << " to " << nextHop << ":" << packet << endl;
			send_packet(next_router, nextHop, destination, packet); // 递归转发
		}
		else {
			printf("Next hop router not found.\n");
		}
	}
}

int main() {
	Graph g;
	int a = mm;
	// 初始化路由表
	network[0].table[0].nextHop = "R2"; // R1 -> R2
	network[0].table[1].nextHop = "R3"; // R1 -> R3
	network[1].table[0].nextHop = "R1"; // R2 -> R1
	network[1].table[1].nextHop = "R3"; // R2 -> R3
	network[1].table[2].nextHop = "R4"; // R2 -> R4
	network[2].table[0].nextHop = "R5"; // R3 -> R5
	network[2].table[1].nextHop = "R2"; // R3 -> R2
	network[3].table[0].nextHop = "R5"; // R4 -> R5
	network[3].table[1].nextHop = "R2"; // R4 -> R2
	network[4].table[0].nextHop = "R3"; // R5 -> R3
	network[4].table[1].nextHop = "R4"; // R5 -> R4


	CreateGraph(g);
	//showGraph(g);
	shortDIJ(g);
	// 发送数据包
	string packet = "Hello from R1 to R4";
	send_packet(find_router("R1"), "R1", "R4", packet);
	return 0;
}
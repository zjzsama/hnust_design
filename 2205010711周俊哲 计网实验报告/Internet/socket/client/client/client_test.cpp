#include <stdio.h>
#include <Winsock2.h>
#include <Ws2tcpip.h>
#include <iostream>
#include <string>
#pragma comment(lib,"ws2_32.lib")
using namespace std;

int client_test() {
	WORD wVersionRequested;
	WSADATA wsaData;
	SOCKET sockClient;
	SOCKADDR_IN addrSrv;
	int err;
	char recvBuf[1024];

	// 初始化Winsock
	wVersionRequested = MAKEWORD(1, 1);
	err = WSAStartup(wVersionRequested, &wsaData);
	if (err != 0) {
		printf("WSAStartup failed\n");
		return 0;
	}
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1) {
		WSACleanup();
		return 0;
	}

	// 创建套接字
	sockClient = socket(AF_INET, SOCK_STREAM, 0);
	if (sockClient == INVALID_SOCKET) {
		printf("Socket creation failed\n");
		WSACleanup();
		return 0;
	}

	// 设置服务器地址
	addrSrv.sin_family = AF_INET;
	addrSrv.sin_port = htons(6000);
	if (inet_pton(AF_INET, "127.0.0.4", &addrSrv.sin_addr) <= 0) {
		printf("Invalid address\n");
		closesocket(sockClient);
		WSACleanup();
		return 0;
	}

	// 连接到服务器
	if (connect(sockClient, (SOCKADDR*)&addrSrv, sizeof(SOCKADDR)) == SOCKET_ERROR) {
		printf("Connection failed\n");
		closesocket(sockClient);
		WSACleanup();
		return 0;
	}

	printf("Connected to server\n");

	// 聊天循环
	while (true) {
		// 从用户获取输入并发送
		string message;
		printf("You: ");
		getline(cin, message);
		send(sockClient, message.c_str(), message.length() + 1, 0);

		// 接收服务器消息
		int recvSize = recv(sockClient, recvBuf, sizeof(recvBuf), 0);
		if (recvSize == SOCKET_ERROR || recvSize == 0) {
			printf("Server disconnected\n");
			break;
		}
		recvBuf[recvSize] = '\0';  // 添加字符串结束符
		printf("Server: %s\n", recvBuf);
	}

	closesocket(sockClient);
	WSACleanup();
	return 0;
}
int main() {
	client_test();
}

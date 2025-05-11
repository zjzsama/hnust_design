#include <stdio.h>
#include <Winsock2.h>
#include <ws2tcpip.h>
#include <iostream>
#include <thread>
#include <string>
#pragma comment(lib,"ws2_32")
using namespace std;


int server() {
	WORD wVersionRequested;
	WSADATA wsaData;
	int err;

	//初始化Winsock
	wVersionRequested = MAKEWORD(1, 1);
	err = WSAStartup(wVersionRequested, &wsaData);
	if (err != 0) {
		return 0;
	}
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1) {
		WSACleanup();
		return 0;
	}
	//创建套接字
	SOCKET sockServer = socket(AF_INET, SOCK_STREAM, 0);
	SOCKADDR_IN addrSrv;
	//设置服务器地址
	addrSrv.sin_addr.S_un.S_addr = htonl(INADDR_ANY);
	addrSrv.sin_family = AF_INET;
	addrSrv.sin_port = htons(6000);


	//绑定套接字
	bind(sockServer, (SOCKADDR*)&addrSrv, sizeof(SOCKADDR));
	if (err == SOCKET_ERROR) {
		printf("Bind failed\n");
		closesocket(sockServer);
		WSACleanup();
		return 0;
	}
	//监听客户端连接
	listen(sockServer, 5);
	if (err == SOCKET_ERROR) {
		printf("Listen failed\n");
		closesocket(sockServer);
		WSACleanup();
		return 0;
	}

	printf("Server is listening on port 6000...\n");
	// 等待客户端连接
	int addrSize, recvSize;
	SOCKET sockClient;
	SOCKADDR_IN addrClient;
	char recvBuf[1024];
	addrSize = sizeof(SOCKADDR_IN);
	sockClient = accept(sockServer, (SOCKADDR*)&addrClient, &addrSize);
	if (sockClient == INVALID_SOCKET) {
		printf("Accept failed\n");
		closesocket(sockServer);
		WSACleanup();
		return 0;
	}
	char ipStr[INET_ADDRSTRLEN];  // 用于存储 IP 地址字符串
	if (inet_ntop(AF_INET, &addrClient.sin_addr, ipStr, sizeof(ipStr)) != nullptr) {
		printf("Client connected: %s\n", ipStr);
	}
	else {
		perror("inet_ntop failed");
	}
	// 处理客户端消息
	while (true) {
		recvSize = recv(sockClient, recvBuf, sizeof(recvBuf), 0);
		if (recvSize == SOCKET_ERROR || recvSize == 0) {
			printf("Client disconnected\n");
			break;
		}
		recvBuf[recvSize] = '\0';  // 添加字符串结束符
		printf("Client: %s\n", recvBuf);

		// 回应客户端
		printf("Server: ");
		string response;
		getline(cin, response);
		send(sockClient, response.c_str(), response.length() + 1, 0);
	}

	closesocket(sockClient);
	closesocket(sockServer);
	WSACleanup();
	return 0;
}
int main()
{
	server();
}



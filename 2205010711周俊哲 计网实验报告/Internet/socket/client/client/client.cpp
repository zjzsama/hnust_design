#include <stdio.h>
#include <Winsock2.h>
#include <Ws2tcpip.h>
#include <iostream>
#pragma comment(lib,"ws2_32.lib")
using namespace std;

int client() {
	WORD wVersionRequested;
	WSADATA wsaData;
	char* sendMessage;
	int err;
	wVersionRequested = MAKEWORD(1, 1);
	err = WSAStartup(wVersionRequested, &wsaData);
	if (err != 0) {
		return 0;
	}
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1) {
		WSACleanup();
		return 0;
	}
	SOCKET sockClient = socket(AF_INET, SOCK_STREAM, 0);
	SOCKADDR_IN addrSrv;

	addrSrv.sin_family = AF_INET;
	addrSrv.sin_port = htons(6000);

	if (inet_pton(AF_INET, "127.0.0.1", &addrSrv.sin_addr) <= 0) {
		printf("Invalid address\n");
		return 0;
	}

	connect(sockClient, (SOCKADDR*)&addrSrv, sizeof(SOCKADDR));
	send(sockClient, "hello", strlen("hello") + 1, 0);

	char recvBuf[50];
	recv(sockClient, recvBuf, 50, 0);
	printf("%s\n", recvBuf);
	closesocket(sockClient);
	WSACleanup();
}

//int main() {
//	client();
//}

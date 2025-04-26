#include <iostream>
#include <map>
using namespace std;



map<string, string> Maps_In;       //内部地址
map<string, string> Maps_Out;    //外部地址

//初始化函数
void Init_data() {
	Maps_In["172.38.1.5:40001"] = "192.168.0.3:30000";
	Maps_In["172.38.1.5:40002"] = "192.168.0.4:30000";
	Maps_Out["192.168.0.3:30000"] = "172.38.1.5:40001";
	Maps_Out["192.168.0.4:30000"] = "172.38.1.5:40002";
}

//显示路由表
void show_data() {
	cout << "外转内映射表" << endl;
	cout << "\t\t外部地址" << "\t\t内部地址" << endl;
	for (const auto& pair : Maps_In) {
		cout << "\t" << pair.first << "\t" << pair.second << endl;
	}
	cout << endl;
	cout << "内转外映射表" << endl;
	cout << "\t\t内部地址" << "\t\t外部地址" << endl;
	for (const auto& pair : Maps_Out) {
		cout << "\t" << pair.first << "\t" << pair.second << endl;
	}
	system("pause");
	cout << endl;
}

// 清屏函数
void clear_screen() {
#ifdef _WIN32
	system("cls"); // Windows 系统
#else
	system("clear"); // Linux/Mac 系统
#endif
}

//转换地址
void Net_address_translation() {
	string Search_addrs;
	cout << "请输入IP与端口(例如:172.38.1.5:40001):";
	cin >> Search_addrs;
	cout << endl;
	if (Maps_In.find(Search_addrs) == Maps_In.end() && Maps_Out.find(Search_addrs) == Maps_Out.end()) {
		cout << "***系统没有此地址IP***" << endl;
	}
	else if (Maps_In.find(Search_addrs) != Maps_In.end()) {
		cout << "***内部地址:\t" << Maps_In[Search_addrs] << endl;
	}
	else if (Maps_Out.find(Search_addrs) != Maps_Out.end()) {
		cout << "***外部地址:\t" << Maps_Out[Search_addrs] << endl;
	}
	system("pause");
	cout << endl;
}

//新增地址
void Net_address_add() {
	string Input_interior_addrs;
	string Input_exterior_addrs;
	cout << "请输入内部地址(例如:192.168.0.4:30001):";
	cin >> Input_interior_addrs;
	cout << endl;
	cout << "请输入外部地址(例如:172.38.1.6:40007):";
	cin >> Input_exterior_addrs;
	cout << endl;
	cout << " 保存结果为:" << endl;
	cout << "***内部地址:\t" << Input_interior_addrs << endl;
	cout << "***外部地址:\t" << Input_exterior_addrs << endl;
	Maps_In[Input_exterior_addrs] = Input_interior_addrs;
	Maps_Out[Input_interior_addrs] = Input_exterior_addrs;
	system("pause");
	cout << endl;
}

//删除地址
void Net_address_delect() {
	string Search_addrs;
	cout << "请输入需要删除的IP与端口(例如:172.38.1.5:40001):";
	cin >> Search_addrs;
	cout << endl;
	if (Maps_In.find(Search_addrs) == Maps_In.end() && Maps_Out.find(Search_addrs) == Maps_Out.end()) {
		cout << "***系统没有此地址IP***" << endl;
		system("pause");
		cout << endl;
	}
	else if (Maps_In.find(Search_addrs) != Maps_In.end()) {
		cout << "***内部地址:\t" << Maps_In[Search_addrs] << endl;
		cout << "***外部地址:\t" << Search_addrs << endl;
		Maps_Out.erase(Maps_In[Search_addrs]);
		Maps_In.erase(Search_addrs);
	}
	else if (Maps_Out.find(Search_addrs) != Maps_Out.end()) {
		cout << "***内部地址:\t" << Search_addrs << endl;
		cout << "***外部地址:\t" << Maps_Out[Search_addrs] << endl;
		Maps_In.erase(Maps_Out[Search_addrs]);
		Maps_Out.erase(Search_addrs);
	}
	system("pause");
	cout << endl;
}

//修改地址
void Net_address_revise() {
	string Search_addrs;
	string Input__addrs;
	cout << "请输入需要修改的IP与端口(例如:172.38.1.5:40001):";
	cin >> Search_addrs;
	cout << endl;
	if (Maps_In.find(Search_addrs) == Maps_In.end() && Maps_Out.find(Search_addrs) == Maps_Out.end()) {
		cout << "***系统没有此地址IP***" << endl;
		system("pause");
		cout << endl;
		return;
	}
	else if (Maps_In.find(Search_addrs) != Maps_In.end()) {
		cout << "***内部地址:\t" << Maps_In[Search_addrs] << endl;
		cout << "***外部地址:\t" << Search_addrs << endl;
		cout << endl;
		cout << "请修改内部的IP与端口(例如:192.168.0.4:30005):";
		cin >> Input__addrs;
		Maps_In[Search_addrs] = Input__addrs;
	}
	else if (Maps_Out.find(Search_addrs) != Maps_Out.end()) {
		cout << "***内部地址:\t" << Search_addrs << endl;
		cout << "***外部地址:\t" << Maps_Out[Search_addrs] << endl;
		cout << endl;
		cout << "请修改外部的IP与端口(例如:172.38.1.5:40007):" << endl, cin >> Input__addrs;;
		Maps_Out[Search_addrs] = Input__addrs;
	}
	system("pause");
	cout << endl;
}


int main() {
	int choice;
	Init_data();
	do {
		clear_screen(); // 每次显示菜单前清屏
		cout << "1. 转换网络地址" << endl;
		cout << "2. 新增网络地址" << endl;
		cout << "3. 修改网络地址" << endl;
		cout << "4. 删除网络地址" << endl;
		cout << "5. 显示映射表" << endl;
		cout << "输入选择操作(输入0退出应用): ";
		cin >> choice;

		switch (choice) {
		case 0:
			break;
		case 1:
			Net_address_translation();
			break;
		case 2:
			Net_address_add();
			break;
		case 3:
			Net_address_revise();
			break;
		case 4:
			Net_address_delect();
			break;
		case 5:
			show_data();
			break;
		default:
			cout << "\n请输出正确选项\n" << endl;
			system("pause");
			break;
		}
	} while (choice);
}
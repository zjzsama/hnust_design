var username;  // 当前用户名
var toname;  // 聊天对象
var broadcastListStr="";  // 系统广播
var Connection=false;  // 当前用户状态
var ws; // webSocket对象
// 页面一加载完成就执行该函数
$(function() {
    $.ajax({
        url: "/get_userinfo",
        method: 'GET',
        dataType: 'json',
        success: function (result) {
            username = result.data;
        },
        error: function () {
            console.log("请求失败！");
            // 在这里进行错误处理，例如提示用户重试、跳转至错误页面等
        },
        async: false
    });
    const ip = window.location.hostname; // 获取当前页面的 IP 地址
    const port = window.location.port; // 获取当前页面的端口号
    console.log(`IP地址：${ip}，端口号：${port}`); // 输出 IP 地址和端口号
    //创建webSocket对象
    ws = new WebSocket("ws://"+ip+":"+port+"/chat");
    //给ws绑定事件
    ws.onopen = function() {
        // 构造要插入样式表的 CSS 字符串
        const styleString = "#username { color: green; }";
        // 使用 jQuery 动态创建一个 style 元素并插入样式表
        $("<style>").text(styleString).appendTo("head");
        // 修改 friendsname 的文本内容，此时文本颜色为绿色
        $("#username").html(username + "（在线）" + "<span id='user-link'>账号管理</span>");
        //管理账号页面跳转
        $("#user-link").click(function() {
            // 在此处添加账号管理链接的处理代码
            location.href="/chat/account_page";
        });
        Connection=true;
        getFriendsList();
    }
    //接收到服务端推送的消息后触发
    ws.onmessage = function(evt) {
        //获取服务端推送过来的消息
        let dataStr = evt.data;
        //将dataStr 转换为json对象
        let res = JSON.parse(dataStr);
        //判断是否是系统消息
        if(res.checkSystem) {
            //系统消息
            let onlinename = res.message[0];
            let status = res.message[1];
            if(onlinename!==username)
            {
                broadcastListStr += "<li>您的好友 "+onlinename+" "+status+"</li>";
            } else {
                if(status === "上线啦~") {
                    broadcastListStr += "<li>亲爱的 "+onlinename+" 欢迎来到聊天室！"+"</li>";
                }
            }
            $("#broadcast-ul").html(broadcastListStr);
            getFriendsList();
            showChat(toname);
        } else {
            alert("你的好友"+res.senderName+"发来消息啦————————")
            if(res.senderName===toname&&res.receiverName===username)
            {
                showChat(toname);
            }
        }
    }

    ws.onclose = function() {
        // 构造要插入样式表的 CSS 字符串
        const styleString = "#username { color: red; }";
        // 使用 jQuery 动态创建一个 style 元素并插入样式表
        $("<style>").text(styleString).appendTo("head");
        // 修改 friendsname 的文本内容，此时文本颜色为红色
        $("#username").text(username+"（离线）");
        Connection=false;
        broadcastListStr += "<li>亲爱的 "+username+" 欢迎下次再来~"+"</li>";
        $("#broadcast-ul").html(broadcastListStr);
    }
})
// 获取好友列表
function getFriendsList() {
    $.ajax({
        url: "/get_userlist",
        method: 'GET',
        dataType: 'json',
        success: function (result) {
            if(toname===undefined)
            {
                toname=result.data[0];
            }
            showChat(toname);
            const list = $('#friends');
            list.html("");
            $.each(result.data, function(index, item) {
                const friend = $('<div>').text(item).addClass('friend-wrapper').attr('onclick', `showChat('${item}')`); // 创建链接并添加 onclick 事件并添加类名
                friend.appendTo(list);
            });
        },
        error: function () {
            console.log("请求失败！");
            // 在这里进行错误处理，例如提示用户重试、跳转至错误页面等
        },
        async: false
    });
}

// 获取聊天对象状态
function getStatus(name) {
    let status;
    // 发起 Ajax 请求
    $.ajax({
        url: '/get_status', // 后端接口地址
        type: 'GET', // 请求类型
        data: {username: name}, // 请求参数
        success: function(result) { // 成功回调函数
            if (result.msg === 'online') {
                status=true;
            } else {
                status=false;
            }
        },
        error: function() { // 失败回调函数
            console.error('Failed to check status');
        },
        async: false
    });
    if(status===true)
    {
        return "（在线）";
    }else{
        return "（离线）";
    }
}

// 显示聊天框
function showChat(name){
    toname = name;
    const friendsname = $("#friendsname");
    let status=getStatus(name);
    if(status==="（离线）")
    {
    // 构造要插入样式表的 CSS 字符串
        const styleString = "#friendsname { color: red; }";
    // 使用 jQuery 动态创建一个 style 元素并插入样式表
        $("<style>").text(styleString).appendTo("head");
    // 修改 friendsname 的文本内容，此时文本颜色为红色
    }else{
        // 构造要插入样式表的 CSS 字符串
        const styleString = "#friendsname { color: green; }";
        // 使用 jQuery 动态创建一个 style 元素并插入样式表
        $("<style>").text(styleString).appendTo("head");
        // 修改 friendsname 的文本内容，此时文本颜色为绿色
    }
    friendsname.text(name+status); // 显示好友姓名和状态

    if (Connection) { // 如果已连接 WebSocket
        $.ajax({
            url: "/getChatMessages",
            type: "GET",
            dataType: "json",
            data: {
                username: username,
                toname: toname
            },
            success: function(result) {
                if (result.msg === "success") {
                    const chat_message = $('.chat-message');
                    chat_message.empty(); // 更新聊天记录区域的内容

                    let chatMessage="";
                    for (let i = 0; i < result.data.length; i++) {
                        const message = result.data[i];
                        let html = '<div class="message ';
                        if (message.senderName === username) {
                            html += 'right';
                            html += '">' +
                                '<p class="sender">' + message.senderName + '</p>'
                        } else {
                            html += 'left';
                            html += '">' +
                                '<p class="sender">' + message.senderName + '</p>'
                        }
                        if (message.message || message.messageImage) {
                            html += '<div class="content">';
                            if (message.message) {
                                html += '<p>' + message.message + '</p>';
                            }
                            if (message.messageImage) {
                                html += '<img src="data:image/png;base64,' + message.messageImage + '" />';
                            }
                            html += '</div>';
                        }

                        html += '<p class="time">' + message.sendTime + '</p>' +
                            '</div>';
                        chatMessage+=html;
                        chat_message.append(html);
                    }
                    // 计算要存储的数据所需的空间大小，并与 sessionStorage 的限制进行比较
                    if (JSON.stringify(chatMessage).length <= 4 * 1024 * 1024 - unescape(encodeURIComponent(JSON.stringify(chatMessage))).length) {
                        // 当要存储的数据量小于 5MB 时，将数据存储到 sessionStorage 中
                        sessionStorage.setItem(toname, chatMessage);
                    } else {
                        // 当要存储的数据量大于等于 5MB 时，提示用户无法继续存储数据
                        console.log("无法存储更多的数据，因为超出了 sessionStorage 的限制！");
                    }
                }
            },
            error: function() {
                alert("获取聊天记录失败");
            },
            async:false
        });
    } else {
        const savedMessages = sessionStorage.getItem(toname); // 从 sessionStorage 中获取已保存的聊天记录
        if (savedMessages) { // 如果已存在聊天记录，则直接展示在聊天区域
            $('.chat-message').html(savedMessages);
        }
    }
}



// 获取当前时间
function getDate(){
    let date = new Date();
    let year = date.getFullYear();
    let month = ('0' + (date.getMonth() + 1)).slice(-2);
    let day = ('0' + date.getDate()).slice(-2);
    let hour = ('0' + date.getHours()).slice(-2);
    let minute = ('0' + date.getMinutes()).slice(-2);
    let second = ('0' + date.getSeconds()).slice(-2);

    let dateString = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
    return dateString;
}

// 发送图片
$("#imageFile").click(function () {
    $("#imageFile").off('change').on('change', function () {
        var file = this.files[0];
        // 判断是否为图片类型
        if (!/image\/\w+/.test(file.type)) {
            alert("请上传图片文件！");
            return;
        }
        var reader = new FileReader();
        reader.onload = function () {
            // 将图片转换为 base64 编码字符串
            var base64Image = reader.result.split(',')[1];

            // 构造要提交的数据
            var formData = {
                senderName: username,
                receiverName: toname,
                messageImage: base64Image,
                sendTime: getDate(),
            };
            // 发送消息到后端保存
            $.ajax({
                url: "/save_solve",
                type: "POST",
                dataType: 'json',
                contentType: 'application/json;charset=utf-8', // 设置请求头 Content-Type
                data: JSON.stringify(formData),
                success: function (response) {
                    console.log("保存成功");
                    ws.send(JSON.stringify(formData));
                    showChat(toname);
                },
                error: function (error) {
                    console.log("保存失败：" + error);
                }
            });
        }
        reader.readAsDataURL(file);
    });
});
//弹出提示框
function showAlert(message, timeout = 1500) {
    var alertBox = document.createElement("div");
    alertBox.className = "alert-box";
    alertBox.innerText = message;
    document.body.appendChild(alertBox);
    setTimeout(function() {
        alertBox.style.opacity = 0;
        setTimeout(function() {
            document.body.removeChild(alertBox);
        }, 1000);
    }, timeout);
}
// 发送消息事件
function sendMsg() {
    var inputBox = document.getElementById("input");
    var message = inputBox.value;
    if (message.trim().length === 0) {
        showAlert("消息不能为空！");
        return;
    }
    inputBox.value = "";
    // 构造消息对象
    let msg = {
        "senderName": username,
        "receiverName": toname,
        "message": message,
        "messageImage": null,
        "sendTime": getDate()
    };
    // 发送消息到后端保存
    $.ajax({
        url: "/save_solve",
        type: "POST",
        dataType: 'json',
        contentType: 'application/json;charset=utf-8', // 设置请求头 Content-Type
        data: JSON.stringify(msg),
        success: function(response) {
            console.log("保存成功");
            ws.send(JSON.stringify(msg));
            showChat(toname);
        },
        error: function(error) {
            console.log("保存失败：" + error);
        }
    });
}

// 发送按钮
$("#send").click(sendMsg);

//回车事件
$("#input").keydown(function (y) {
    if (y.keyCode == 13) {
        y.preventDefault();
        sendMsg();
    }
});

// 显示emoji表格
function showEmoji() {
    var table = document.querySelector("#emoji");
    if (table.style.display === "none") {
        table.style.display = "table";
    }
}

// 监听事件，当鼠标点击页面其他地方时，隐藏emoji表
document.addEventListener("click", function(event) {
    var emoji = document.querySelector("#emoji");
    if (event.target !== emoji || emoji.contains(event.target)) {
        emoji.style.display = "none";
    }
},true);

// 将选中的emoji加入输入框中
function insertEmoji(emoji) {
    var input = document.querySelector("textarea");
    input.value += emoji;
}

// 关闭当前窗口
$("#close").click(function () {
    ws.close();
    location.href = '/login_page';
});
// 获取登录链接元素
const loginLink = document.getElementById('enroll-link');

// 添加点击事件监听器
loginLink.addEventListener('click', function(event) {
    // 阻止默认跳转行为
    event.preventDefault();

    // 重定向到登录页面
    location.href = '/login_page';
});
$(function () {
    $("#enroll-form").submit(function (event) {
        // 阻止默认的表单提交事件
        event.preventDefault();

        // 获取用户名和密码
        var username = $("#username").val();
        var password = $("#password").val();

        // 使用 AJAX 发送注册请求
        $.ajax({
            url: "/enroll_solve", // 替换为实际的后端注册接口地址
            method: "POST",
            dataType:'json',//表示返回的数据必须为json，否则：会走下面error对应的方法。
            contentType:'application/json;charset=utf-8',//如果要发送json字符串，此属性不能少；否则，会出现后台会报异常
            data:JSON.stringify({
                username: username, password: password
            }),//将json对象转换为json字符串
            success: function(result){
                if (result.msg==="success") {
                    alert("注册成功————让我们愉快的一起聊天吧！"+ result.data.username);
                    location.href = "/chat/main_page";
                    return false;
                } else {
                    alert("注册失败: 该用户名已经有人使用，请更换用户名！");
                }
            },
            //请求失败，包含具体的错误信息
            error : function(e){
                console.log(e.status);
                console.log(e.responseText);
            }
        });
    });
});
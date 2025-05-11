// 获取注册链接元素
const loginLink = document.getElementById('login-link');

// 添加点击事件监听器
loginLink.addEventListener('click', function(event) {
    // 阻止默认跳转行为
    event.preventDefault();
    // 重定向到注册页面
    location.href = '/enroll_page';
});
$(function () {
    $("#login-form").submit(function (event) {
        // 阻止默认的表单提交事件
        event.preventDefault();

        // 获取id和密码
        var username = $("#username").val();
        var password = $("#password").val();

        // 使用 AJAX 发送登录请求
        $.ajax({
            url: "/login_solve", // 替换为实际的后端登录接口地址
            method: "POST",
            dataType:'json',//表示返回的数据必须为json，否则：会走下面error对应的方法。
            contentType:'application/json;charset=utf-8',//如果要发送json字符串，此属性不能少；否则，会出现后台会报异常
            data:JSON.stringify({
                username: username, password: password
            }),//将json对象转换为json字符串
            success: function(result){
                if (result.msg==="success") {
                    alert("登录成功————欢迎回来！"+ result.data.username);
                    location.href = "/chat/main_page";
                    return false;
                } else if(result.msg==="wrong"){
                    alert("登录失败: 用户名不存在或者密码错误！");
                } else{
                    alert("登录失败: 该用户已经登录！");
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
// account.js
function changeAccountInfo() {
    var newPassword = $('#newPassword').val();
    var confirmPassword = $('#confirmPassword').val();

    // 检查密码是否为空
    if (newPassword === '' || confirmPassword === '') {
        alert('密码不能为空！');
        return false;
    }
    // 检查密码是否一致
    if (newPassword !== confirmPassword) {
        alert('两次输入的密码不一致！');
        return false;
    }
    // 发起 AJAX 请求
    $.ajax({
        type: 'POST',
        url: '/password_solve',
        dataType:'json',//表示返回的数据必须为json，否则：会走下面error对应的方法。
        data: {
            password: newPassword
        },
        success: function(result) {
            // 请求成功后的回调函数
            console.log('修改成功！');
            alert('修改成功！');
            return true;
        },
        error: function() {
            // 请求失败后的回调函数
            console.log('修改失败：');
            alert('修改失败！');
        }
    });
}
function back() {
    location.href="/chat/main_page";
}
function logout() {
    // 显示确定对话框
    var confirmLogout = confirm('您确定要注销账号吗？三思而后行啊！！！');
    if (confirmLogout) {
        $.ajax({
            type: 'POST',
            url: '/delete_solve',
            dataType:'json',//表示返回的数据必须为json，否则：会走下面error对应的方法。
            success: function(result) {
                alert("注销成功！再见啦————")
                // 请求成功后的回调函数
                console.log('注销成功！');
                location.href="/login_page";
                return true;
            },
            error: function() {
                // 请求失败后的回调函数
                console.log('注销失败：');
            }
        });
    }
}


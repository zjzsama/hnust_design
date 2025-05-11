package com.hnust.controller;

import com.hnust.domian.User;
import com.hnust.model.Result;
import com.hnust.service.ChatService;
import com.hnust.service.UserService;
import com.hnust.config.ChatEndpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @Autowired
    ChatService chatService;
    @Autowired
    Result result;
    //登录逻辑处理
    @PostMapping("/login_solve")
    public Result login_solve(@RequestBody User user, HttpServletRequest request) {
        User cur=userService.selectByUsername(user.getUsername());
        // 判断账号和密码是否正确
        if (cur!=null&&user.getPassword().equals(cur.getPassword())) {
            if(ChatEndpoint.checkSessionMap(cur.getUsername()))
            {
                logger.info(user+"：登录失败--------用户名已经登录");
                result.setMsg("failure");
                result.setData(null);
            }else{
                HttpSession session = request.getSession();
                ChatEndpoint.closeBefore(session);
                session.setAttribute("user", cur.getUsername());
                logger.info(cur+"：登录成功--------");
                result.setMsg("success");
                result.setData(cur);
            }
        } else {
            logger.info(user+"：登录失败--------用户名或者密码错误");
            result.setMsg("wrong");
            result.setData(null);
        }
        return result;
    }
    //注册逻辑处理
    @PostMapping("/enroll_solve")
    public Result enroll_solve(@RequestBody User user, HttpServletRequest request) {
        User cur=userService.selectByUsername(user.getUsername());
        if(cur!=null)
        {
            user=null;
            logger.info(user+"：注册失败");
            result.setMsg("failure");
            result.setData(null);
        }else{
            userService.insertUser(user);
            HttpSession session = request.getSession();
            ChatEndpoint.closeBefore(session);
            session.setAttribute("user", user.getUsername());
            logger.info(user+"：注册成功");
            result.setMsg("success");
            result.setData(user);
        }
        return result;
    }
    //获取用户信息
    @GetMapping("/get_userinfo")
    public Result get_userinfo(HttpSession session)
    {
        String username = (String) session.getAttribute("user");
        logger.info("获取当前用户："+username);
        result.setData(username);
        return result;
    }
    //获取所有用户信息,不包括本身
    @GetMapping("/get_userlist")
    public Result get_userlist(HttpSession session)
    {
        List<User> user=userService.selectAll();
        List<String> users=new ArrayList<>();
        String curname=(String)session.getAttribute("user");
        for(User it :user)
        {
            if(!it.getUsername().equals(curname))
            {
                users.add(it.getUsername());
            }
        }
        result.setData(users);
        return result;
    }
    //获取用户状态
    @GetMapping("/get_status")
    public Result get_status(String username)
    {
        if(ChatEndpoint.checkSessionMap(username))
        {
            result.setMsg("online");
            logger.info(username+"-----在线");
        }else{
            result.setMsg("offline");
            logger.info(username+"-----离线");
        }
        return result;
    }
    //修改用户密码
    @PostMapping("/password_solve")
    public Result password_solve(String password,HttpSession session) {
        String username = (String) session.getAttribute("user");
        User user=new User(username,password);
        Boolean check =userService.updatePassword(user);
        if(check)
        {
            logger.info(username+"---------修改密码");
            result.setMsg("success");
        }else{
            result.setMsg("failure");
        }
        return result;
    }
    //注销用户
    @PostMapping("/delete_solve")
    public Result logout_solve(HttpSession session) {
        String username = (String) session.getAttribute("user");
        Boolean check1 =userService.deleteUser(username);
        Boolean check2 =chatService.deleteBySenderName(username,username);
        if(check1&&check2)
        {
            session.invalidate(); // 删除用户的 session
            logger.info(username+"---------注销");
            result.setMsg("success");
        }else{
            result.setMsg("failure");
        }
        return result;
    }
}

package com.hnust.config;

import com.hnust.controller.UserController;
import com.hnust.domian.User;
import com.hnust.model.ChatMessage;
import com.hnust.service.UserService;
import com.hnust.utils.ChatMessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    // 注入 UserService
    @Autowired
    private UserService userService;
    //用来存储每一个客户端对象对应的ChatEndpoint对象
    private static  Map<String,ChatEndpoint> allUsers = new ConcurrentHashMap<>();
    // 在程序启动时执行该静态方法，将所有用户信息添加到 onlineUsers 集合中
    @PostConstruct
    public void init() {
        List<User> users = userService.selectAll();
        for (User user : users) {
            allUsers.put(user.getUsername(), new ChatEndpoint());
        }
        logger.info(allUsers.toString());
    }
    public static Boolean checkSessionMap(String username) {
        if(username==null)
        {
            return false;
        }
        Session cur=allUsers.get(username).session;
        return cur != null && cur.isOpen();
    }
    public static void closeBefore(HttpSession httpSession)
    {
        for (String name : allUsers.keySet()) {
            ChatEndpoint chatEndpoint = allUsers.get(name);
            if(chatEndpoint.httpSession!=null&&chatEndpoint.httpSession==httpSession)
            {
                try {
                    chatEndpoint.session.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    //声明session对象，通过该对象可以发送消息给指定的用户
    private Session session;

    //声明一个HttpSession对象，我们之前在HttpSession对象中存储了用户对象
    private HttpSession httpSession;

    @OnOpen
    //连接建立时被调用
    public void onOpen(Session session, EndpointConfig config) {
        //将局部的session对象赋值给成员session
        this.session = session;
        //获取httpSession对象
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        //从httpSession对象中获取用户名
        String username = (String) httpSession.getAttribute("user");
        logger.info(username+"已经上线-----------");
        allUsers.put(username,this);
        //创建系统消息
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setCheckSystem(true);
        String[] systemMessage = {username,"刚刚上线啦~"};
        chatMessage.setMessage(systemMessage);
        //对象转JSON
        String message=ChatMessageUtil.getChatMessageJson(chatMessage);
        //发送系统消息
        broadcastAllUsers(message);
    }

    @OnMessage(maxMessageSize=104857600)
    //接收到客户端发送的数据时被调用
    public void onMessage(String message) {
        try {
            ChatMessage chatMessage =ChatMessageUtil.getChatMessageObject(message);
            //获取要将数据发送给的用户
            String toName = chatMessage.getReceiverName();
            if(checkSessionMap(toName))
            {
                allUsers.get(toName).session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    //连接关闭时被调用
    public void onClose() {
        //从httpSession对象中获取用户名
        String username = (String) httpSession.getAttribute("user");
        logger.info(username+"已经下线-----------");
        //创建系统消息
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setCheckSystem(true);
        String[] systemMessage = {username,"刚刚离线啦~"};
        chatMessage.setMessage(systemMessage);
        //对象转JSON
        String message=ChatMessageUtil.getChatMessageJson(chatMessage);
        //发送系统消息
        broadcastAllUsers(message);
    }
    // 系统推送消息
    private void broadcastAllUsers(String message) {
        try {
            //要将该消息推送给所有的客户端
            for (String name : allUsers.keySet()) {
                ChatEndpoint chatEndpoint = allUsers.get(name);
                if(chatEndpoint.session!=null&&chatEndpoint.session.isOpen())
                {
                    chatEndpoint.session.getBasicRemote().sendText(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

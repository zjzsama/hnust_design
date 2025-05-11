package com.hnust.controller;

import com.hnust.domian.ChatRecord;
import com.hnust.model.ChatMessage;
import com.hnust.model.Result;
import com.hnust.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    Result result;
    @PostMapping("/save_solve")
    public Result save_solve(@RequestBody ChatMessage chatMessage) {
        String base64Image = chatMessage.getMessageImage();
        byte[] messageImage=null;
        if(base64Image!=null)
        {
            messageImage = Base64.getDecoder().decode(base64Image);
        }
        ChatRecord chatRecord=new ChatRecord(chatMessage.getSenderName(),chatMessage.getReceiverName(), (String)chatMessage.getMessage(),  messageImage, chatMessage.getSendTime());
        System.out.println(chatRecord);
        if(!chatService.selectChat(chatRecord.getSenderName(),chatRecord.getReceiverName()).equals(chatRecord))
        {
            chatService.insertChat(chatRecord);
        }
        return result;
    }
    @GetMapping("/getChatMessages")
    public Result getChatMessages(@RequestParam("username") String username, @RequestParam("toname") String toname){
        List<ChatRecord> chatRecords=chatService.selectChat(username,toname);
        List<ChatMessage> chatMessages=new ArrayList<>();
        for (ChatRecord chatRecord : chatRecords) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setCheckSystem(false);
            chatMessage.setSenderName(chatRecord.getSenderName());
            chatMessage.setReceiverName(chatRecord.getReceiverName());
            chatMessage.setMessage(chatRecord.getMessage());
            chatMessage.setMessageImage(chatRecord.getMessageImage() != null ? Base64.getEncoder().encodeToString(chatRecord.getMessageImage()) : null);
            chatMessage.setSendTime(chatRecord.getSendTime());
            chatMessages.add(chatMessage);
        }
        result.setData(chatMessages);
        result.setMsg("success");
        return result;
    }
}

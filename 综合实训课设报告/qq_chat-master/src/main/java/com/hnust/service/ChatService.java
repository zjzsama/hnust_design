package com.hnust.service;

import com.hnust.domian.ChatRecord;
import com.hnust.model.ChatMessage;

import java.util.List;


public interface ChatService {
    Boolean insertChat(ChatRecord chatRecord);
    List<ChatRecord> selectChat(String senderName, String receiverName);
    Boolean deleteBySenderName(String senderName,String receiverName);
}

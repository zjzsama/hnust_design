package com.hnust.service.impl;

import com.hnust.dao.ChatDao;
import com.hnust.domian.ChatRecord;
import com.hnust.model.ChatMessage;
import com.hnust.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatDao chatDao;

    @Override
    public Boolean insertChat(ChatRecord chatRecord) {
        return chatDao.insertChat(chatRecord);
    }

    @Override
    public List<ChatRecord> selectChat(String senderName, String receiverName) {
        return chatDao.selectChat(senderName,receiverName);
    }

    @Override
    public Boolean deleteBySenderName(String senderName, String receiverName) {
        return chatDao.deleteBySenderName(senderName,receiverName);
    }
}

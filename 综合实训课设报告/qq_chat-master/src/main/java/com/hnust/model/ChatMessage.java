package com.hnust.model;

import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.util.Date;

@Component
public class ChatMessage {
    private Boolean checkSystem;
    private String senderName;
    private String receiverName;
    private Object message;
    private String messageImage;
    private String sendTime;


    public ChatMessage() {
    }

    public ChatMessage(Boolean checkSystem, String senderName, String receiverName, Object message, String messageImage, String sendTime) {
        this.checkSystem = checkSystem;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.messageImage = messageImage;
        this.sendTime = sendTime;
    }

    /**
     * 获取
     * @return checkSystem
     */
    public Boolean getCheckSystem() {
        return checkSystem;
    }

    /**
     * 设置
     * @param checkSystem
     */
    public void setCheckSystem(Boolean checkSystem) {
        this.checkSystem = checkSystem;
    }

    /**
     * 获取
     * @return senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * 设置
     * @param senderName
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * 获取
     * @return receiverName
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 设置
     * @param receiverName
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 获取
     * @return message
     */
    public Object getMessage() {
        return message;
    }

    /**
     * 设置
     * @param message
     */
    public void setMessage(Object message) {
        this.message = message;
    }

    /**
     * 获取
     * @return messageImage
     */
    public String getMessageImage() {
        return messageImage;
    }

    /**
     * 设置
     * @param messageImage
     */
    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    /**
     * 获取
     * @return sendTime
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * 设置
     * @param sendTime
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String toString() {
        return "ChatMessage{checkSystem = " + checkSystem + ", senderName = " + senderName + ", receiverName = " + receiverName + ", message = " + message + ", messageImage = " + messageImage + ", sendTime = " + sendTime + "}";
    }
}

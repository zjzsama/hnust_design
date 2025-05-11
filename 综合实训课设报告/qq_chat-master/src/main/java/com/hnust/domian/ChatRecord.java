package com.hnust.domian;

import java.security.Timestamp;


public class ChatRecord {
    private String senderName;
    private String receiverName;
    private String message;
    private byte[] messageImage;
    private String sendTime;


    public ChatRecord() {
    }

    public ChatRecord(String senderName, String receiverName, String message, byte[] messageImage, String sendTime) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.messageImage = messageImage;
        this.sendTime = sendTime;
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
    public String getMessage() {
        return message;
    }

    /**
     * 设置
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取
     * @return messageImage
     */
    public byte[] getMessageImage() {
        return messageImage;
    }

    /**
     * 设置
     * @param messageImage
     */
    public void setMessageImage(byte[] messageImage) {
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
        return "ChatRecord{senderName = " + senderName + ", receiverName = " + receiverName + ", message = " + message + ", messageImage = " + messageImage + ", sendTime = " + sendTime + "}";
    }
}

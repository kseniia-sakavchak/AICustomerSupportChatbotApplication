package com.mycompany.chatbot.chat_service.domain;

import java.util.Date;

public class ChatResponseDto {
    private Long id;
    private String sender;
    private String content;
    private String chatId;
    private Date timestamp;

    public ChatResponseDto() {
    }

    public ChatResponseDto(Long id, String sender, String content, String chatId, Date timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.chatId = chatId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

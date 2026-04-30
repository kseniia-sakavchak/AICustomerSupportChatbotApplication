package com.mycompany.chatbot.chat_service.domain;

public class MessageCreateDto {
    private String sender;
    private String content;
    private String chatId;
    private ResponseMode mode;

    public MessageCreateDto() {
    }

    public MessageCreateDto(String sender, String content, String chatId, ResponseMode mode) {
        this.sender = sender;
        this.content = content;
        this.chatId = chatId;
        this.mode = mode;
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

    public ResponseMode getMode() {
        return mode;
    }

    public void setMode(ResponseMode mode) {
        this.mode = mode;
    }
}

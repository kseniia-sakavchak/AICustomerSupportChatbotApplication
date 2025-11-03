package com.mycompany.chatbot.chat_service.domain;

public class MessageCreateDto {
    private String sender;
    private String content;
    private String chatId;

    public MessageCreateDto() {
    }

    public MessageCreateDto(String sender, String content, String chatId) {
        this.sender = sender;
        this.content = content;
        this.chatId = chatId;
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
}

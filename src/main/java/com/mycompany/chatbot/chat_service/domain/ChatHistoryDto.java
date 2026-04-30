package com.mycompany.chatbot.chat_service.domain;

import java.util.List;

public class ChatHistoryDto {

    private String chatId;
    private List<ChatResponseDto> messages;

    public ChatHistoryDto(String chatId, List<ChatResponseDto> messages) {
        this.chatId = chatId;
        this.messages = messages;
    }

    public ChatHistoryDto() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<ChatResponseDto> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatResponseDto> messages) {
        this.messages = messages;
    }
}

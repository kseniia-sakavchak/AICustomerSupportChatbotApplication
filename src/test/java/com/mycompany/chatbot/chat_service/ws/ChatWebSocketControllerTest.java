package com.mycompany.chatbot.chat_service.ws;


import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import com.mycompany.chatbot.chat_service.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatWebSocketControllerTest {

    private ChatService chatService;
    private SimpMessagingTemplate messagingTemplate;
    private ChatWebSocketController controller;

    @BeforeEach
    void setUp() {
        chatService = mock(ChatService.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        controller = new ChatWebSocketController(chatService, messagingTemplate);
    }

    @Test
    void getMessage_shouldSetChatIdCreateMessageAndSendResponse() {
        MessageCreateDto request = new MessageCreateDto();
        request.setContent("hello");

        ChatResponseDto response = new ChatResponseDto();
        response.setId(1L);
        response.setChatId("chat-1");
        response.setSender("bot");
        response.setContent("Hello! How can I help?");
        response.setTimestamp(new Date());

        when(chatService.createMessage(request)).thenReturn(response);

        controller.getMessage(request, "chat-1");

        assertEquals("chat-1", request.getChatId());

        verify(chatService).createMessage(request);
        verify(messagingTemplate).convertAndSend("/topic/chat/chat-1", response);
    }

}
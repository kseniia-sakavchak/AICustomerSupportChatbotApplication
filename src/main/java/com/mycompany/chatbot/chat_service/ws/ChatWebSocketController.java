package com.mycompany.chatbot.chat_service.ws;

import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import com.mycompany.chatbot.chat_service.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{chatId}")
    public void getMessage(MessageCreateDto dto, @DestinationVariable String chatId) {
        dto.setChatId(chatId);
        ChatResponseDto response = chatService.createMessage(dto);
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, response);
    }
}

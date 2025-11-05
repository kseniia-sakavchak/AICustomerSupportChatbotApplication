package com.mycompany.chatbot.chat_service.mapper;

import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MessageMapper {

    public Message toEntity(MessageCreateDto dto) {
         Message message = new Message();
         message.setSender(dto.getSender());
         message.setContent(dto.getContent());
         message.setChatId(dto.getChatId());
         message.setTimestamp(new Date());
         return message;
    }

    public ChatResponseDto toDto(Message message) {
        ChatResponseDto dto = new ChatResponseDto();
        dto.setId(message.getId());
        dto.setSender(message.getSender());
        dto.setContent(message.getContent());
        dto.setChatId(message.getChatId());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}

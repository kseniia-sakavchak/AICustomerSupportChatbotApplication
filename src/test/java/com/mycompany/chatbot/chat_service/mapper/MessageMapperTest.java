package com.mycompany.chatbot.chat_service.mapper;

import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageMapper = new MessageMapper();
    }

    @Test
    void toEntity_shouldMapMessageCreateDtoToMessage() {

        MessageCreateDto dto = new MessageCreateDto();
        dto.setSender("user");
        dto.setContent("hello");
        dto.setChatId("chat-1");

        Message result = messageMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals("user", result.getSender());
        assertEquals("hello", result.getContent());
        assertEquals("chat-1", result.getChatId());
        assertNotNull(result.getTimestamp());
    }

    @Test
    void toDto_shouldMapMessageToChatResponseDto() {

        Message message = new Message();
        message.setId(1L);
        message.setSender("bot");
        message.setContent("Hello!");
        message.setChatId("chat-1");

        ChatResponseDto result = messageMapper.toDto(message);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("bot", result.getSender());
        assertEquals("Hello!", result.getContent());
        assertEquals("chat-1", result.getChatId());
    }

}
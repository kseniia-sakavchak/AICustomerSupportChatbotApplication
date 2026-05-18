package com.mycompany.chatbot.chat_service.service;

import com.mycompany.chatbot.ai_service.AiService;
import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import com.mycompany.chatbot.chat_service.domain.ResponseMode;
import com.mycompany.chatbot.chat_service.mapper.MessageMapper;
import com.mycompany.chatbot.chat_service.repo.MessageRepository;
import com.mycompany.chatbot.faq_service.service.FaqService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatServiceTest {

    private MessageRepository messageRepository;
    private MessageMapper messageMapper;
    private FaqService faqService;
    private AiService aiService;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        messageMapper = mock(MessageMapper.class);
        faqService = mock(FaqService.class);
        aiService = mock(AiService.class);

        chatService = new ChatService(messageRepository, messageMapper, faqService, aiService);
    }

    @Test
    void createMessage_shouldReturnFaqResponse_whenModeIsFAQ() {
        MessageCreateDto dto = new MessageCreateDto();
        dto.setChatId("1");
        dto.setContent("reset password");
        dto.setMode(ResponseMode.FAQ);

        Message userMessage = new Message();
        userMessage.setChatId("1");
        userMessage.setContent("reset password");

        when(messageMapper.toEntity(dto)).thenReturn(userMessage);
        when(messageRepository.save(any())).thenReturn(userMessage);
        when(faqService.getAnswerForQuestion("reset password"))
                .thenReturn("Reset in settings");

        Message botMessage = new Message();
        botMessage.setChatId("1");
        botMessage.setContent("Reset in settings");
        botMessage.setSender("bot");
        botMessage.setTimestamp(new Date());

        when(messageRepository.save(any())).thenReturn(botMessage);

        ChatResponseDto response = chatService.createMessage(dto);

        assertEquals("Reset in settings", response.getContent());
    }

    @Test
    void createMessage_shouldReturnAiResponse_whenModeIsAI() {
        MessageCreateDto dto = new MessageCreateDto();
        dto.setChatId("1");
        dto.setContent("help");
        dto.setMode(ResponseMode.AI);

        Message userMessage = new Message();
        userMessage.setChatId("1");
        userMessage.setContent("help");

        when(messageMapper.toEntity(dto)).thenReturn(userMessage);
        when(messageRepository.save(any())).thenReturn(userMessage);

        when(aiService.getAnswer(eq("help"), any()))
                .thenReturn("AI response");

        Message botMessage = new Message();
        botMessage.setChatId("1");
        botMessage.setContent("AI response");
        botMessage.setSender("bot");
        botMessage.setTimestamp(new Date());

        when(messageRepository.save(any())).thenReturn(botMessage);

        ChatResponseDto response = chatService.createMessage(dto);

        assertEquals("AI response", response.getContent());
    }

    @Test
    void createMessage_shouldReturnHumanResponse_whenModeIsHUMAN() {
        MessageCreateDto dto = new MessageCreateDto();
        dto.setChatId("1");
        dto.setContent("help");
        dto.setMode(ResponseMode.HUMAN);

        Message userMessage = new Message();
        userMessage.setChatId("1");
        userMessage.setContent("help");

        when(messageMapper.toEntity(dto)).thenReturn(userMessage);
        when(messageRepository.save(any())).thenReturn(userMessage);

        Message botMessage = new Message();
        botMessage.setChatId("1");
        botMessage.setContent("Our support agent will reply to you shortly.");
        botMessage.setSender("bot");
        botMessage.setTimestamp(new Date());

        when(messageRepository.save(any())).thenReturn(botMessage);

        ChatResponseDto response = chatService.createMessage(dto);

        assertTrue(response.getContent().toLowerCase().contains("support"));
    }

}
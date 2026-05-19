package com.mycompany.chatbot.chat_service.service;

import com.mycompany.chatbot.ai_service.AiService;
import com.mycompany.chatbot.chat_service.domain.*;
import com.mycompany.chatbot.chat_service.mapper.MessageMapper;
import com.mycompany.chatbot.chat_service.repo.MessageRepository;
import com.mycompany.chatbot.faq_service.service.FaqService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        userMessage.setSender("user");

        Message botMessage = new Message();
        botMessage.setChatId("1");
        botMessage.setContent("Reset in settings");
        botMessage.setSender("bot");
        botMessage.setTimestamp(new Date());

        when(messageMapper.toEntity(dto)).thenReturn(userMessage);
        when(messageRepository.save(any())).thenReturn(userMessage, botMessage);
        when(faqService.getAnswerForQuestion("reset password"))
                .thenReturn("Reset in settings");

        ChatResponseDto response = chatService.createMessage(dto);

        assertEquals("Reset in settings", response.getContent());
        assertEquals("bot", response.getSender());
        assertEquals("1", response.getChatId());
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

    @Test
    void createMessage_shouldUseFaqMode_whenModeIsNull() {
        MessageCreateDto dto = new MessageCreateDto();
        dto.setChatId("1");
        dto.setContent("reset password");
        dto.setMode(null);

        Message userMessage = new Message();
        userMessage.setChatId("1");
        userMessage.setContent("reset password");
        userMessage.setSender("user");

        Message botMessage = new Message();
        botMessage.setChatId("1");
        botMessage.setContent("Reset in settings");
        botMessage.setSender("bot");
        botMessage.setTimestamp(new Date());

        when(messageMapper.toEntity(dto)).thenReturn(userMessage);
        when(messageRepository.save(any(Message.class)))
                .thenReturn(userMessage, botMessage);
        when(faqService.getAnswerForQuestion("reset password"))
                .thenReturn("Reset in settings");

        ChatResponseDto response = chatService.createMessage(dto);

        assertEquals("Reset in settings", response.getContent());
    }

    @Test
    void processUserMessage_shouldReturnFallback_whenAiFails() {
        when(messageRepository.findTop50ByChatIdOrderByTimestampDesc("1"))
                .thenReturn(List.of());

        when(aiService.getAnswer(eq("help"), any()))
                .thenThrow(new RuntimeException("AI error"));

        String result = chatService.processUserMessage("1", "help", ResponseMode.AI);

        assertTrue(result.contains("technical issue"));
        assertTrue(result.contains("HUMAN support mode"));
    }

    @Test
    void getChatHistory_shouldReturnMessagesInAscendingOrder() {
        Date olderDate = new Date(System.currentTimeMillis() - 10000);
        Date newerDate = new Date();

        Message newerMessage = new Message();
        newerMessage.setId(2L);
        newerMessage.setChatId("1");
        newerMessage.setSender("bot");
        newerMessage.setContent("New message");
        newerMessage.setTimestamp(newerDate);

        Message olderMessage = new Message();
        olderMessage.setId(1L);
        olderMessage.setChatId("1");
        olderMessage.setSender("user");
        olderMessage.setContent("Old message");
        olderMessage.setTimestamp(olderDate);

        ChatResponseDto olderDto = new ChatResponseDto();
        olderDto.setId(1L);
        olderDto.setChatId("1");
        olderDto.setSender("user");
        olderDto.setContent("Old message");
        olderDto.setTimestamp(olderDate);

        ChatResponseDto newerDto = new ChatResponseDto();
        newerDto.setId(2L);
        newerDto.setChatId("1");
        newerDto.setSender("bot");
        newerDto.setContent("New message");
        newerDto.setTimestamp(newerDate);

        when(messageRepository.findTop50ByChatIdOrderByTimestampDesc("1"))
                .thenReturn(new ArrayList<>(List.of(newerMessage, olderMessage)));

        when(messageMapper.toDto(olderMessage)).thenReturn(olderDto);
        when(messageMapper.toDto(newerMessage)).thenReturn(newerDto);

        ChatHistoryDto result = chatService.getChatHistory("1");

        assertEquals("1", result.getChatId());
        assertEquals(2, result.getMessages().size());
        assertEquals("Old message", result.getMessages().get(0).getContent());
        assertEquals("New message", result.getMessages().get(1).getContent());
    }

}
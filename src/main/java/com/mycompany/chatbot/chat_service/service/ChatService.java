package com.mycompany.chatbot.chat_service.service;

import com.mycompany.chatbot.ai_service.AiService;
import com.mycompany.chatbot.ai_service.core.AiMessage;
import com.mycompany.chatbot.chat_service.domain.*;
import com.mycompany.chatbot.chat_service.mapper.MessageMapper;
import com.mycompany.chatbot.chat_service.repo.MessageRepository;
import com.mycompany.chatbot.faq_service.service.FaqService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final FaqService faqService;
    private final AiService aiService;

    public ChatService(MessageRepository messageRepository, MessageMapper messageMapper, FaqService faqService, AiService aiService) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.faqService = faqService;
        this.aiService = aiService;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public ChatResponseDto createMessage(MessageCreateDto dto) {
        Message savedMessage = saveUserMessage(dto);
        ResponseMode mode = dto.getMode() != null ? dto.getMode() : ResponseMode.FAQ;
        String botResponse = processUserMessage(savedMessage.getContent(), savedMessage.getContent(), mode);

        Message botMessage = saveBotMessage(savedMessage.getChatId(), botResponse);
        return buildBotResponseMessage(botMessage);
    }

    public Message saveUserMessage(MessageCreateDto dto) {
        Message message = messageMapper.toEntity(dto);
        message = messageRepository.save(message);
        return message;
    }

    public Message saveBotMessage(String chatId, String botResponse) {
        Message botMessage = new Message();
        botMessage.setChatId(chatId);
        botMessage.setSender("bot");
        botMessage.setContent(botResponse);
        botMessage.setTimestamp(new Date());
        botMessage = messageRepository.save(botMessage);

        return botMessage;
    }

    public ChatResponseDto buildBotResponseMessage(Message botMessage) {
        ChatResponseDto response = new ChatResponseDto();
        response.setId(botMessage.getId());
        response.setSender(botMessage.getSender());
        response.setContent(botMessage.getContent());
        response.setChatId(botMessage.getChatId());
        response.setTimestamp(botMessage.getTimestamp());

        return response;
    }

    public String processUserMessage(String chatId, String messageText, ResponseMode mode) {
        return switch (mode) {
            case FAQ -> faqService.getAnswerForQuestion(messageText);
            case AI -> aiService.getAnswer(messageText, buildAiHistory(chatId, 20));
            case HUMAN -> "Our support agent will reply to you shortly.";
        };
    }

    public ChatHistoryDto getChatHistory(String chatId) {
        List<Message> messages = messageRepository.findTop50ByChatIdOrderByTimestampDesc(chatId);
        Collections.reverse(messages);
        List<ChatResponseDto> messageDto = messages.stream().map(messageMapper::toDto).toList();
        return new ChatHistoryDto(chatId, messageDto);
    }

    private List<AiMessage> buildAiHistory(String chatId, int limit) {
        List<Message> messages = messageRepository.findTop50ByChatIdOrderByTimestampDesc(chatId);
        Collections.reverse(messages);

        int fromIndex = Math.max(0, messages.size() - limit);
        return messages.subList(fromIndex, messages.size()).stream()
                .map(m -> {
                    String role = "bot".equalsIgnoreCase(m.getSender()) ? "assistant" : "user";
                    return new AiMessage(role, m.getContent());
                })
                .toList();
    }
}

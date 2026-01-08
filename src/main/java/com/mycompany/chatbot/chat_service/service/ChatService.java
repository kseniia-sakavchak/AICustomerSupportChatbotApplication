package com.mycompany.chatbot.chat_service.service;

import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import com.mycompany.chatbot.chat_service.mapper.MessageMapper;
import com.mycompany.chatbot.chat_service.repo.MessageRepository;
import com.mycompany.chatbot.faq_service.service.FaqService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final FaqService faqService;

    public ChatService(MessageRepository messageRepository, MessageMapper messageMapper, FaqService faqService) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.faqService = faqService;
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
        String botResponse = processUserMessage(savedMessage.getContent());

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

    public String processUserMessage(String messageText) {
        if (messageText.startsWith("/faq ")) {
            String question = messageText.substring(5);
            return faqService.getAnswerForQuestion(question);
        }
        return "Echo: " + messageText;
    }

    public
}

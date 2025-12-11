package com.mycompany.chatbot.chat_service.service;

import com.mycompany.chatbot.chat_service.client.FaqClient;
import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import com.mycompany.chatbot.chat_service.mapper.MessageMapper;
import com.mycompany.chatbot.chat_service.repo.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final FaqClient faqClient;

    public ChatService(MessageRepository messageRepository, MessageMapper messageMapper, FaqClient faqClient) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.faqClient = faqClient;
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
        Message message = messageMapper.toEntity(dto);
        Message savedMessage = messageRepository.save(message);
        String botResponse = processUserMessage(savedMessage.getContent());

        Message botMessage = new Message();
        botMessage.setChatId(message.getChatId());
        botMessage.setSender("bot");
        botMessage.setContent(botResponse);
        botMessage.setTimestamp(new Date());
        botMessage = messageRepository.save(botMessage);

        ChatResponseDto response = new ChatResponseDto();
        response.setId(botMessage.getId());
        response.setSender("bot");
        response.setContent(botResponse);
        response.setChatId(savedMessage.getChatId());
        response.setTimestamp(new Date());

        return response;
    }

    public String processUserMessage(String messageText) {
        if (messageText.startsWith("/faq ")) {
            String question = messageText.substring(5);
            return faqClient.getAnswer(question);
        }
        return "Echo: " + messageText;
    }
}

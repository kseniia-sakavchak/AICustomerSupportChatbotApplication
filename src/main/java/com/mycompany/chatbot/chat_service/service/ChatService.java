package com.mycompany.chatbot.chat_service.service;

import com.mycompany.chatbot.chat_service.domain.ChatResponseDto;
import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.domain.MessageCreateDto;
import com.mycompany.chatbot.chat_service.mapper.MessageMapper;
import com.mycompany.chatbot.chat_service.repo.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public ChatService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
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
        return messageMapper.toDto(savedMessage);
    }
}

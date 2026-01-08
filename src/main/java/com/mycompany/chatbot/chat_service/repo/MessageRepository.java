package com.mycompany.chatbot.chat_service.repo;

import com.mycompany.chatbot.chat_service.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    public List<Message> findByChatIdOrderByTimestampAsc(String chatId);

}

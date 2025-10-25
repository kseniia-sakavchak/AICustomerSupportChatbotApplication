package com.mycompany.chatbot.chat_service.repo;

import com.mycompany.chatbot.chat_service.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

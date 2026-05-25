package com.mycompany.chatbot.chat_service.repo;

import com.mycompany.chatbot.chat_service.domain.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void findTop50ByChatIdOrderByTimestampDesc_shouldReturnMessagesForChat() {

        Message message1 = new Message();
        message1.setChatId("chat-1");
        message1.setSender("user");
        message1.setContent("Hello");
        message1.setTimestamp(new Date(System.currentTimeMillis() - 10000));

        Message message2 = new Message();
        message2.setChatId("chat-1");
        message2.setSender("bot");
        message2.setContent("Hi");
        message2.setTimestamp(new Date());

        messageRepository.save(message1);
        messageRepository.save(message2);

        List<Message> result =
                messageRepository.findTop50ByChatIdOrderByTimestampDesc("chat-1");

        assertEquals(2, result.size());
    }

    @Test
    void deleteByChatId_shouldDeleteAllMessagesForChat() {

        Message message = new Message();
        message.setChatId("chat-delete");
        message.setSender("user");
        message.setContent("delete me");
        message.setTimestamp(new Date());

        messageRepository.save(message);

        messageRepository.deleteByChatId("chat-delete");

        List<Message> result =
                messageRepository.findTop50ByChatIdOrderByTimestampDesc("chat-delete");

        assertTrue(result.isEmpty());
    }

}
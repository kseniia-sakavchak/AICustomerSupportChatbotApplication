package com.mycompany.chatbot.chat_service.web;

import com.mycompany.chatbot.chat_service.domain.Message;
import com.mycompany.chatbot.chat_service.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {
    private final ChatService chatService;

    public ChatHistoryController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return chatService.getAllMessages();
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable Long id) {
        return chatService.getMessageById(id).orElse(null);
    }

    @PostMapping
    public Message saveMessage(@RequestBody Message message) {
        return chatService.saveMessage(message);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        chatService.deleteMessage(id);
    }
}

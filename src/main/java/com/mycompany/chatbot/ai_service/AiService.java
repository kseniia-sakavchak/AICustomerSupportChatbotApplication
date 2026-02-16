package com.mycompany.chatbot.ai_service;

import com.mycompany.chatbot.ai_service.core.AiClient;
import com.mycompany.chatbot.ai_service.core.AiMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {
    private final AiClient aiClient;

    public AiService(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    public String getAnswer(String question, List<AiMessage> history) {
        return aiClient.generateAnswer(question, history);
    }
}

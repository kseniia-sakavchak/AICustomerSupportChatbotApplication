package com.mycompany.chatbot.ai_service;

import com.mycompany.chatbot.ai_service.core.AiClientRouter;
import com.mycompany.chatbot.ai_service.core.AiMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {

    private final AiClientRouter aiClient;

    public AiService(AiClientRouter aiClient) {
        this.aiClient = aiClient;
    }

    public String getAnswer(String question, List<AiMessage> history) {
        try {
            String answer = aiClient.generateAnswer(question, history);

            if (answer == null || answer.isBlank()) {
                return "I’m sorry — I couldn’t understand your issue. Please describe what happened.";
            }

            return answer;
        } catch (Exception e) {
            return "I’m sorry — something went wrong while processing your request. Please try again.";
        }
    }
}

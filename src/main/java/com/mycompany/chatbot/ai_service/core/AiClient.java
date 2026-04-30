package com.mycompany.chatbot.ai_service.core;

import java.util.List;

public interface AiClient {
    String generateAnswer(String question, List<AiMessage> history);
}

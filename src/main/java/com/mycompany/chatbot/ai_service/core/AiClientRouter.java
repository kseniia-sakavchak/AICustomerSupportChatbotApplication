package com.mycompany.chatbot.ai_service.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AiClientRouter implements AiClient {

    private final Map<String, AiClient> clients;
    private final String provider;

    public AiClientRouter(Map<String, AiClient> clients, @Value("${ai.provider:smart}") String provider) {
        this.clients = clients;
        this.provider = provider.trim().toLowerCase();
    }

    @Override
    public String generateAnswer(String question, List<AiMessage> history) {
        AiClient client = clients.get(provider);
        if (client == null) {
            throw new IllegalStateException(
                    "Unknown ai.provider=" + provider + ", available: " + clients.keySet()
            );
        }

        return client.generateAnswer(question, history);
    }
}

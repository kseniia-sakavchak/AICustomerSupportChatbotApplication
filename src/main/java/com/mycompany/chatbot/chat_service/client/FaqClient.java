package com.mycompany.chatbot.chat_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FaqClient {
    private final WebClient webClient;

    public FaqClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getAnswer(String question) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/faqs/answer")
                        .queryParam("question", question)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

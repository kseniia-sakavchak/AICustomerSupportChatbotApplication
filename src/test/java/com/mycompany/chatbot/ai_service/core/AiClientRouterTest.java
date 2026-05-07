package com.mycompany.chatbot.ai_service.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AiClientRouterTest {

    @Test
    void shouldCallCorrectClient_whenProviderExists() {
        AiClient smartClient = mock(AiClient.class);

        when(smartClient.generateAnswer("question", List.of()))
                .thenReturn("smart response");

        Map<String, AiClient> clients = Map.of(
                "smart", smartClient
        );

        AiClientRouter router = new AiClientRouter(clients, "smart");

        String result = router.generateAnswer("question", List.of());

        assertEquals("smart response", result);
    }

    @Test
    void shouldThrowException_whenProviderNotFound() {
        Map<String, AiClient> clients = Map.of();

        AiClientRouter router = new AiClientRouter(clients, "unknown");

        assertThrows(IllegalStateException.class,
                () -> router.generateAnswer("question", List.of()));
    }

}
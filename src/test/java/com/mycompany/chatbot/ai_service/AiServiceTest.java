package com.mycompany.chatbot.ai_service;

import com.mycompany.chatbot.ai_service.core.AiClientRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AiServiceTest {

    private AiClientRouter aiClientRouter;
    private AiService aiService;

    @BeforeEach
    void setUp() {
        aiClientRouter = mock (AiClientRouter.class);
        aiService = new AiService(aiClientRouter);
    }

    @Test
    void getAnswer_shouldReturnAnswerValidText() {
        when(aiClientRouter.generateAnswer("question", List.of()))
                .thenReturn("Some valid answer");

        String result = aiService.getAnswer("question", List.of());

        assertEquals("Some valid answer", result);
    }

    @Test
    void getAnswer_shouldReturnFallback_whenClientReturnsNull() {
        when(aiClientRouter.generateAnswer("question", List.of()))
                .thenReturn(null);

        String result = aiService.getAnswer("question", List.of());

        assertTrue(result.toLowerCase().contains("couldn’t")
                || result.toLowerCase().contains("could not"));
    }

    @Test
    void getAnswer_shouldReturnFallback_whenClientReturnsBlank() {
        when(aiClientRouter.generateAnswer("question", List.of()))
                .thenReturn("   ");

        String result = aiService.getAnswer("question", List.of());

        assertTrue(result.toLowerCase().contains("couldn’t")
                || result.toLowerCase().contains("could not"));
    }

    @Test
    void getAnswer_shouldReturnErrorMessage_whenExceptionThrown() {
        when(aiClientRouter.generateAnswer("question", List.of()))
                .thenThrow(new RuntimeException("error"));

        String result = aiService.getAnswer("question", List.of());

        assertTrue(result.toLowerCase().contains("something went wrong"));
    }
}
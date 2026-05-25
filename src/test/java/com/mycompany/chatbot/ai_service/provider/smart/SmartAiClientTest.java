package com.mycompany.chatbot.ai_service.provider.smart;

import com.mycompany.chatbot.ai_service.core.AiMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmartAiClientTest {

    private SmartAiClient smartAiClient;

    @BeforeEach
    void setUp() {
        smartAiClient = new SmartAiClient();
    }

    @Test
    void generateAnswer_shouldReturnPasswordResetResponse() {
        String result = smartAiClient.generateAnswer("I want to reset my password", List.of());

        assertTrue(result.toLowerCase().contains("password"));
        assertTrue(result.toLowerCase().contains("reset"));
    }

    @Test
    void generateAnswer_shouldReturnGreetingResponse() {
        String result = smartAiClient.generateAnswer("Hello", List.of());

        assertTrue(result.toLowerCase().contains("login"));
        assertTrue(result.toLowerCase().contains("help"));
    }

    @Test
    void generateAnswer_shouldReturnTwoFactorResponse() {
        String result = smartAiClient.generateAnswer("I use verification code and it does not work", List.of());

        assertTrue(result.toLowerCase().contains("receive")
                || result.toLowerCase().contains("error")
                || result.toLowerCase().contains("code")
                || result.toLowerCase().contains("authenticator")
                || result.toLowerCase().contains("sms")
        );
    }

    @Test
    void generateAnswer_shouldReturnGenericResponseForUnknownQuestion() {
        String result = smartAiClient.generateAnswer("asdasdasd", List.of());

        assertTrue(result.toLowerCase().contains("please tell me"));
    }

    @Test
    void generateAnswer_shouldReturnFollowUpWhenPreviousIssueDidNotWork() {
        List<AiMessage> history = List.of(
                new AiMessage("user", "I use verification code"),
                new AiMessage("assistant", "Got it — let’s check the 2FA step.")
        );

        String result = smartAiClient.generateAnswer("That did not work", history);

        assertTrue(result.toLowerCase().contains("happens")
                || result.toLowerCase().contains("code")
                || result.toLowerCase().contains("error")
                || result.toLowerCase().contains("receive"));
    }

    @Test
    void generateAnswer_shouldAskForClarificationWhenMessageIsUnclear() {
        List<AiMessage> history = List.of(
                new AiMessage("user", "I cannot login"),
                new AiMessage("assistant", "You can reset your password in settings.")
        );

        String result = smartAiClient.generateAnswer("it", history);

        assertTrue(result.toLowerCase().contains("clarify"));
    }

    @Test
    void generateAnswer_shouldDetectGreetingIntent() {
        String result = smartAiClient.generateAnswer("hi help", List.of());

        assertTrue(result.toLowerCase().contains("help"));
    }

    @Test
    void generateAnswer_shouldFallbackWhenFailureWithoutHistory() {
        String result = smartAiClient.generateAnswer("still not working", List.of());

        assertTrue(result.toLowerCase().contains("happens")
                || result.toLowerCase().contains("error")
                || result.toLowerCase().contains("describe"));
    }

    @Test
    void generateAnswer_shouldWork_whenHistoryIsNull() {
        String result = smartAiClient.generateAnswer("reset password", null);

        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("password"));
    }
}
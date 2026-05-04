package com.mycompany.chatbot.faq_service.service;

import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.repo.FaqRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FaqSearchServiceTest {

    private FaqRepository faqRepository;
    private FaqSearchService faqSearchService;

    @BeforeEach
    void setUp() {
        faqRepository = mock(FaqRepository.class);
        faqSearchService = new FaqSearchService(faqRepository);
    }

    @Test
    void shouldReturnBestMatch_whenRelevantFaqExists() {
        Faq faq1 = new Faq();
        faq1.setQuestion("How to reset password?");
        faq1.setAnswer("Resent in settings.");

        Faq faq2 = new Faq();
        faq2.setQuestion("How to change email?");
        faq2.setAnswer("Change in profile.");

        when(faqRepository.findAll()).thenReturn(List.of(faq1, faq2));

        Faq result = faqSearchService.findBestMatch("reset password");

        assertNotNull(result);
        assertEquals("How to reset password?", result.getQuestion());
    }

    @Test
    void shouldReturnNull_whenNoMatchFound() {
        Faq faq = new Faq();
        faq.setQuestion("How to reset password?");

        when(faqRepository.findAll()).thenReturn(List.of(faq));

        Faq result = faqSearchService.findBestMatch("change email");

        assertNull(result);
    }

    @Test
    void shouldReturnNull_whenQuestionIsBlank() {
        Faq result = faqSearchService.findBestMatch("   ");
        assertNull(result);
    }

    @Test
    void shouldPickFaqWithHigherScore() {
        Faq faq1 = new Faq();
        faq1.setQuestion("reset password");

        Faq faq2 = new Faq();
        faq2.setQuestion("reset password email login");

        when(faqRepository.findAll()).thenReturn(List.of(faq1, faq2));

        Faq result = faqSearchService.findBestMatch("reset password login");

        assertEquals("reset password email login", result.getQuestion());
    }

    @Test
    void shouldSkipFaqWithNullQuestion() {
        Faq faq1 = new Faq();
        faq1.setQuestion(null);

        Faq faq2 = new Faq();
        faq2.setQuestion("reset password");

        when(faqRepository.findAll()).thenReturn(List.of(faq1, faq2));

        Faq result = faqSearchService.findBestMatch("reset password");

        assertNotNull(result);
        assertEquals("reset password", result.getQuestion());
    }
}
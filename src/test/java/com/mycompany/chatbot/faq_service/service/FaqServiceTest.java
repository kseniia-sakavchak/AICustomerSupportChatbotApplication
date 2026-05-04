package com.mycompany.chatbot.faq_service.service;

import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.repo.FaqRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FaqServiceTest {

    private FaqRepository faqRepository;
    private FaqSearchService faqSearchService;
    private FaqService faqService;

    @BeforeEach
    void setUp() {
        faqRepository = mock(FaqRepository.class);
        faqSearchService = mock(FaqSearchService.class);
        faqService = new FaqService(faqRepository, faqSearchService);
    }

    @Test
    void getAnswerForQuestion_shouldReturnAnswer_whenFaqFound() {
        Faq faq = new Faq();
        faq.setQuestion("reset password");
        faq.setAnswer("Go to settings");

        when(faqSearchService.findBestMatch("password")).thenReturn(faq);

        String result = faqService.getAnswerForQuestion("password");

        assertEquals("Go to settings", result);
    }

    @Test
    void getAnswerForQuestion_shouldReturnFallback_whenFaqNotFound() {
        when(faqSearchService.findBestMatch("unknown")).thenReturn(null);

        String result = faqService.getAnswerForQuestion("unknown");

        assertEquals("Sorry, I don't have an answer for that question.", result);
    }

    @Test
    void updateAnswer_shouldUpdateFaqAnswer_whenFaqExists() {
        Faq faq = new Faq();
        faq.setAnswer("Old answer");

        when(faqRepository.findById(1L)).thenReturn(Optional.of(faq));
        when(faqRepository.save(faq)).thenReturn(faq);

        Faq result = faqService.updateAnswer(1L, "New answer");

        assertEquals("New answer", result.getAnswer());
        verify(faqRepository).save(faq);
    }

    @Test
    void updateAnswer_shouldThrow_whenFaqNotFound() {
        when(faqRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> faqService.updateAnswer(99L, "New answer"));
    }

    @Test
    void updateAnswer_shouldThrow_whenAnswerIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> faqService.updateAnswer(1L, "   "));
    }



}
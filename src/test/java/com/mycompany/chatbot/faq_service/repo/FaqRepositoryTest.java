package com.mycompany.chatbot.faq_service.repo;

import com.mycompany.chatbot.faq_service.domain.Category;
import com.mycompany.chatbot.faq_service.domain.Faq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FaqRepositoryTest {

    @Autowired
    private FaqRepository faqRepository;

    @Test
    void findByQuestionContainingIgnoreCase_shouldReturnMatchingFaqs() {
        Faq faq = new Faq();
        faq.setQuestion("How to reset password?");
        faq.setAnswer("Go to settings");
        faq.setCategory(Category.ACCOUNT);

        faqRepository.save(faq);

        List<Faq> result = faqRepository.findByQuestionContainingIgnoreCase("RESET");

        assertEquals(1, result.size());
        assertEquals("How to reset password?", result.get(0).getQuestion());
    }

    @Test
    void findByCategory_shouldReturnFaqsByCategory() {
        Faq faq = new Faq();
        faq.setQuestion("How to change email?");
        faq.setAnswer("Go to profile");
        faq.setCategory(Category.ACCOUNT);

        faqRepository.save(faq);

        List<Faq> result = faqRepository.findByCategory(Category.ACCOUNT);

        assertEquals(1, result.size());
        assertEquals(Category.ACCOUNT, result.get(0).getCategory());
    }

}
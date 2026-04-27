package com.mycompany.chatbot.faq_service.service;

import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.repo.FaqRepository;
import org.springframework.stereotype.Service;

@Service
public class FaqService {
    private final FaqRepository faqRepository;
    private final FaqSearchService faqSearchService;

    public FaqService(FaqRepository faqRepository, FaqSearchService faqSearchService) {
        this.faqRepository = faqRepository;
        this.faqSearchService = faqSearchService;
    }

    public Faq saveFaq(Faq faq) {
        return faqRepository.save(faq);
    }

    public Faq findFaqById(Long id) {
        return faqRepository.findById(id).orElse(null);
    }

    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }

    public Faq findFaqByQuestion(String question) {
        return faqSearchService.findBestMatch(question);
    }

    public String getAnswerForQuestion(String question) {
        Faq faq = faqSearchService.findBestMatch(question);
        if (faq != null) {
            return faq.getAnswer();
        } else {
            return "Sorry, I don't have an answer for that question.";
        }
    }

    public Faq updateAnswer(Long id, String newAnswer) {
        Faq faq = findFaqById(id);

        if (faq == null) {
            throw new IllegalArgumentException("FAQ not found with id: " + id);
        }

        if (newAnswer == null || newAnswer.isBlank()) {
            throw new IllegalArgumentException("FAQ answer must not be empty");
        }

        faq.setAnswer(newAnswer);
        return faqRepository.save(faq);
    }
}

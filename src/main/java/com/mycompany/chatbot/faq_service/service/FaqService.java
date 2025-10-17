package com.mycompany.chatbot.faq_service.service;

import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.repo.FaqRepository;
import org.springframework.stereotype.Service;

@Service
public class FaqService {
    private FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
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
        return faqRepository.findByQuestionContainingIgnoreCase(question)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public String getAnswerForQuestion(String question) {
        Faq faq = findFaqByQuestion(question);
        if (faq != null) {
            return faq.getAnswer();
        } else {
            return "Sorry, I don't have an answer for that question.";
        }
    }

    public Faq updateAnswer(Long id, String newAnswer) {
        Faq faq = findFaqById(id);
        faq.setAnswer(newAnswer);
        return faqRepository.save(faq);
    }
}

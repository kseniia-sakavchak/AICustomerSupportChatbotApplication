package com.mycompany.chatbot.faq_service.service;

import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.repo.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FaqSearchService {
    private final FaqRepository faqRepository;

    public FaqSearchService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public Faq findBestMatch(String question) {
        if(question == null || question.isBlank()) {
            return null;
        }

        List<Faq> faqs = faqRepository.findAll();

        Set<String> userWords = tokenize(question);

        Faq bestMatch = null;
        int bestScore = 0;

        for(Faq faq : faqs) {
            if (faq.getQuestion() == null) continue;

            Set<String> faqWords = tokenize(faq.getQuestion());
            int score = calculateScore(userWords, faqWords);

            if(score > bestScore) {
                bestScore = score;
                bestMatch = faq;
            }
        }

        return bestScore > 0 ? bestMatch : null;
    }

    private Set<String> tokenize(String text) {
        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toSet());
    }

    private int calculateScore(Set<String> userWords, Set<String> faqWords) {
        int matches = 0;

        for(String word : userWords) {
            if(faqWords.contains(word)) {
                matches+= 2;
            }
        }
        return matches;
    }
}

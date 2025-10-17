package com.mycompany.chatbot.faq_service.repo;

import com.mycompany.chatbot.faq_service.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findByQuestionContainingIgnoreCase(String text);

    List<Faq> findByCategory(String category);
}

package com.mycompany.chatbot.config;

import com.mycompany.chatbot.faq_service.domain.Category;
import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.repo.FaqRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FaqDataInitializer implements CommandLineRunner {

    private final FaqRepository faqRepository;

    public FaqDataInitializer(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (faqRepository.count() > 0) {
            return;
        }

        Date now = new Date();
        List<Faq> faqs = List.of(
                new Faq(null, "How to reset my password?", "To reset your password, click on 'Forgot Password' at the login page and follow the instructions.", Category.ACCOUNT, "en", now, now),
                new Faq(null, "How can I contact customer support?", "You can contact customer support via chat or email support@company.com.", Category.GENERAL, "en", now, now),
                new Faq(null, "I can't log into my account. What should I do?", "Please ensure your username and password are correct. If you still can't log in, try resetting your password.", Category.ACCOUNT, "en", now, now),
                new Faq(null, "How do I change my email?", "To change your email, go to account settings and update your email address.", Category.ACCOUNT, "en", now, now),
                new Faq(null, "How do I delete my account?", "To delete your account, please contact customer support for assistance.", Category.ACCOUNT, "en", now, now)
        );
        faqRepository.saveAll(faqs);
    }
}

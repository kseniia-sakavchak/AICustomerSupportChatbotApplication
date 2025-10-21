package com.mycompany.chatbot.faq_service.web;

import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.service.FaqService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faqs")
public class FaqController {
    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @PostMapping
    public Faq createFaq(@RequestBody Faq faq) {
        return faqService.saveFaq(faq);
    }

    @GetMapping("/{id}")
    public Faq getFaqById(@PathVariable Long id) {
        return faqService.findFaqById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
    }

    @GetMapping("/search")
    public Faq findByQuestion(@RequestParam String question) {
        return faqService.findFaqByQuestion(question);
    }

    @PutMapping("/{id}")
    public Faq updateAnswer(@PathVariable Long id, @RequestParam String newAnswer) {
        return faqService.updateAnswer(id, newAnswer);
    }

    @GetMapping("/answer")
    public String getAnswer(@RequestParam String question) {
        return faqService.getAnswerForQuestion(question);
    }
}

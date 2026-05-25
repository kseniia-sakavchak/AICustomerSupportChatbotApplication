package com.mycompany.chatbot.faq_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.chatbot.faq_service.domain.Category;
import com.mycompany.chatbot.faq_service.domain.Faq;
import com.mycompany.chatbot.faq_service.service.FaqService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FaqControllerTest {

    private FaqService faqService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        faqService = mock(FaqService.class);
        FaqController faqController = new FaqController(faqService);
        mockMvc = MockMvcBuilders.standaloneSetup(faqController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createFaq_shouldReturnCreatedFaq() throws Exception {
    Faq request = new Faq();
        request.setQuestion("How to reset password?");
        request.setAnswer("Go to settings");
        request.setCategory(Category.ACCOUNT);

    Faq response = new Faq();
        response.setId(1L);
        response.setQuestion("How to reset password?");
        response.setAnswer("Go to settings");
        response.setCategory(Category.ACCOUNT);

    when(faqService.saveFaq(any(Faq.class))).thenReturn(response);

        mockMvc.perform(post("/api/faqs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.question").value("How to reset password?"))
            .andExpect(jsonPath("$.answer").value("Go to settings"));
    }

    @Test
    void getFaqById_shouldReturnFaq() throws Exception {
        Faq faq = new Faq();
        faq.setId(1L);
        faq.setQuestion("How to reset password?");
        faq.setAnswer("Go to settings");

        when(faqService.findFaqById(1L)).thenReturn(faq);

        mockMvc.perform(get("/api/faqs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("How to reset password?"));
    }

    @Test
    void deleteFaq_shouldDeleteFaq() throws Exception {
        mockMvc.perform(delete("/api/faqs/1"))
                .andExpect(status().isOk());

        verify(faqService).deleteFaq(1L);
    }

    @Test
    void findByQuestion_shouldReturnFaq() throws Exception {
        Faq faq = new Faq();
        faq.setId(1L);
        faq.setQuestion("How to reset password?");
        faq.setAnswer("Go to settings");

        when(faqService.findFaqByQuestion("password")).thenReturn(faq);

        mockMvc.perform(get("/api/faqs/search")
                        .param("question", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("How to reset password?"));
    }

    @Test
    void updateAnswer_shouldReturnUpdatedFaq() throws Exception {
        Faq faq = new Faq();
        faq.setId(1L);
        faq.setQuestion("How to reset password?");
        faq.setAnswer("New answer");

        when(faqService.updateAnswer(1L, "New answer")).thenReturn(faq);

        mockMvc.perform(put("/api/faqs/1")
                        .param("newAnswer", "New answer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("New answer"));
    }

    @Test
    void getAnswer_shouldReturnAnswerText() throws Exception {
        when(faqService.getAnswerForQuestion("password"))
                .thenReturn("Go to settings");

        mockMvc.perform(get("/api/faqs/answer")
                        .param("question", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string("Go to settings"));
    }

}
package com.mycompany.chatbot.chat_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.chatbot.chat_service.domain.*;
import com.mycompany.chatbot.chat_service.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ChatHistoryControllerTest {

    private ChatService chatService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        chatService = mock(ChatService.class);
        ChatHistoryController controller = new ChatHistoryController(chatService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createMessage_shouldReturnChatResponse() throws Exception {
        MessageCreateDto request = new MessageCreateDto();
        request.setChatId("chat-1");
        request.setContent("hello");
        request.setMode(ResponseMode.FAQ);

        ChatResponseDto response = new ChatResponseDto();
        response.setId(1L);
        response.setChatId("chat-1");
        response.setSender("bot");
        response.setContent("Hello! How can I help?");
        response.setTimestamp(new Date());

        when(chatService.createMessage(any(MessageCreateDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.chatId").value("chat-1"))
                .andExpect(jsonPath("$.sender").value("bot"))
                .andExpect(jsonPath("$.content").value("Hello! How can I help?"));
    }

    @Test
    void getAllMessages_shouldReturnMessages() throws Exception {
        Message message1 = new Message();
        message1.setId(1L);
        message1.setChatId("chat-1");
        message1.setSender("user");
        message1.setContent("hello");
        message1.setTimestamp(new Date());

        Message message2 = new Message();
        message2.setId(2L);
        message2.setChatId("chat-1");
        message2.setSender("bot");
        message2.setContent("hi");
        message2.setTimestamp(new Date());

        when(chatService.getAllMessages()).thenReturn(List.of(message1, message2));

        mockMvc.perform(get("/api/chat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].sender").value("user"))
                .andExpect(jsonPath("$[1].sender").value("bot"));
    }

    @Test
    void getChatHistoryDto_shouldReturnChatHistory() throws Exception {
        ChatResponseDto message = new ChatResponseDto();
        message.setId(1L);
        message.setChatId("chat-1");
        message.setSender("user");
        message.setContent("hello");
        message.setTimestamp(new Date());

        ChatHistoryDto history = new ChatHistoryDto("chat-1", List.of(message));

        when(chatService.getChatHistory("chat-1")).thenReturn(history);

        mockMvc.perform(get("/api/chat/chat-1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value("chat-1"))
                .andExpect(jsonPath("$.messages.size()").value(1))
                .andExpect(jsonPath("$.messages[0].content").value("hello"));
    }

    @Test
    void getMessageById_shouldReturnMessage_whenMessageExists() throws Exception {
        Message message = new Message();
        message.setId(1L);
        message.setChatId("chat-1");
        message.setSender("user");
        message.setContent("hello");
        message.setTimestamp(new Date());

        when(chatService.getMessageById(1L)).thenReturn(Optional.of(message));

        mockMvc.perform(get("/api/chat/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("hello"));
    }

    @Test
    void getMessageById_shouldReturnNotFound_whenMessageDoesNotExist() throws Exception {
        when(chatService.getMessageById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/chat/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMessage_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/chat/message/1"))
                .andExpect(status().isNoContent());

        verify(chatService).deleteMessage(1L);
    }

    @Test
    void deleteChat_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/chat/chat-1"))
                .andExpect(status().isNoContent());

        verify(chatService).deleteChat("chat-1");
    }

}
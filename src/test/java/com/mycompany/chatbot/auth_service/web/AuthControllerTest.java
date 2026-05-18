package com.mycompany.chatbot.auth_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.chatbot.auth_service.dto.AuthRequest;
import com.mycompany.chatbot.auth_service.dto.AuthResponse;
import com.mycompany.chatbot.auth_service.dto.UserRegisterDTO;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;
import com.mycompany.chatbot.auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class AuthControllerTest {

    private AuthService authService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        AuthController authController = new AuthController(authService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll_shouldReturnAllUsers() throws Exception {
        UserResponseDTO user1 = new UserResponseDTO(1L, "user1", "user1@test.com", "USER");
        UserResponseDTO user2 = new UserResponseDTO(2L, "admin", "admin@test.com", "ADMIN");

        when(authService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("admin"));
    }

    @Test
    void findById_shouldReturnUserById() throws Exception {
        UserResponseDTO user = new UserResponseDTO(1L, "user1", "user1@test.com", "USER");

        when(authService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/auth/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@test.com"));
    }

    @Test
    void registerUser_shouldRegisterUser() throws Exception {
        UserRegisterDTO request = new UserRegisterDTO("user1", "password", "user1@test.com");

        UserResponseDTO response = new UserResponseDTO(1L, "user1", "user1@test.com", "USER");

        when(authService.registerUser(any(UserRegisterDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@test.com"));
    }

    @Test
    void login_shouldReturnToken() throws Exception {
        AuthRequest request = new AuthRequest("user1", "password");
        AuthResponse response = new AuthResponse("fake-jwt-token");

        when(authService.authenticate(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void adminOnlyEndpoint_shouldReturnMessage() throws Exception {
        mockMvc.perform(get("/auth/admin/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Hello Admin!")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("You have access.")));
    }

    @Test
    void userAccess_shouldReturnMessage() throws Exception {
        mockMvc.perform(get("/auth/user/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Hello User!")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("You have access.")));
    }

}
package com.mycompany.chatbot.auth_service.web;

import com.mycompany.chatbot.auth_service.domain.Role;
import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.repo.UserRepository;
import com.mycompany.chatbot.auth_service.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setRole(Role.USER);
        user.setEmail("user@test.com");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("12345"));
        admin.setRole(Role.ADMIN);
        admin.setEmail("admin@test.com");

        userRepository.save(user);
        userRepository.save(admin);

        userToken = jwtService.generateToken("user", "USER");
        adminToken = jwtService.generateToken("admin", "ADMIN");
    }

    @Test
    void shouldReturnUnauthorized_whenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/auth/user/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowUserAccess_whenUserTokenIsValid() throws Exception {
        mockMvc.perform(get("/auth/user/test")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDenyUserAccessToAdminEndpoint() throws Exception {
        mockMvc.perform(get("/auth/admin/test")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminAccessToAdminEndpoint() throws Exception {
        mockMvc.perform(get("/auth/admin/test")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}

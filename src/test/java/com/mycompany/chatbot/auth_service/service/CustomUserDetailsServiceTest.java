package com.mycompany.chatbot.auth_service.service;

import com.mycompany.chatbot.auth_service.domain.Role;
import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReurnUserDetails_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("user@test.com");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")
                || a.getAuthority().equals("USER")));
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonExistentUser");
        });

        assertEquals("User not found : nonExistentUser", exception.getMessage());

    }
}
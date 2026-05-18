package com.mycompany.chatbot.auth_service.repo;

import com.mycompany.chatbot.auth_service.domain.Role;
import com.mycompany.chatbot.auth_service.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("user1@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("user1");

        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
        assertEquals("user1@test.com", result.get().getEmail());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<User> result = userRepository.findByUsername("unknown");

        assertTrue(result.isEmpty());
    }

}
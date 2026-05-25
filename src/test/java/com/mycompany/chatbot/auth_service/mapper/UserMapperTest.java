package com.mycompany.chatbot.auth_service.mapper;

import com.mycompany.chatbot.auth_service.domain.Role;
import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDto_shouldMapUserToUserResponseDTO() {

        User user = new User(1L, "user1", "user1@test.com", "password", Role.USER);

        UserResponseDTO dto = UserMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("user1", dto.getUsername());
        assertEquals("user1@test.com", dto.getEmail());
        assertEquals("USER", dto.getRole());
    }

    @Test
    void toDto_shouldReturnNullWhenUserIsNull() {
        UserResponseDTO dto = UserMapper.toDto(null);
        assertNull(dto);
    }

}
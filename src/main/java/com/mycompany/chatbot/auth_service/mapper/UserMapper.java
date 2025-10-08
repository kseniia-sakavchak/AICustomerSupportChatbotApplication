package com.mycompany.chatbot.auth_service.mapper;

import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;

public class UserMapper {

    public static UserResponseDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}

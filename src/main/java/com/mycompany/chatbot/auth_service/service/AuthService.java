package com.mycompany.chatbot.auth_service.service;

import com.mycompany.chatbot.auth_service.domain.Role;
import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.dto.AuthRequest;
import com.mycompany.chatbot.auth_service.dto.AuthResponse;
import com.mycompany.chatbot.auth_service.dto.UserRegisterDTO;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;
import com.mycompany.chatbot.auth_service.mapper.UserMapper;
import com.mycompany.chatbot.auth_service.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) {
        userRepository.findByUsername(userRegisterDTO.getUsername())
                .ifPresent(u -> { throw new RuntimeException("Username already exists"); });

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token);
    }
}

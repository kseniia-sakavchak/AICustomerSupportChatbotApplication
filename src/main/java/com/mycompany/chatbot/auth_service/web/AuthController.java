package com.mycompany.chatbot.auth_service.web;

import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.dto.AuthRequest;
import com.mycompany.chatbot.auth_service.dto.AuthResponse;
import com.mycompany.chatbot.auth_service.dto.UserRegisterDTO;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;
import com.mycompany.chatbot.auth_service.mapper.UserMapper;
import com.mycompany.chatbot.auth_service.repo.UserRepository;
import com.mycompany.chatbot.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/users")
    public List<UserResponseDTO> findAll() {
        return authService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserResponseDTO findById(@PathVariable Long id) {
        return authService.getUserById(id);
    }

    @PostMapping("/register")
    public UserResponseDTO registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return authService.registerUser(userRegisterDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}

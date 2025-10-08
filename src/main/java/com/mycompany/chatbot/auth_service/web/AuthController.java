package com.mycompany.chatbot.auth_service.web;

import com.mycompany.chatbot.auth_service.dto.AuthRequest;
import com.mycompany.chatbot.auth_service.dto.AuthResponse;
import com.mycompany.chatbot.auth_service.dto.UserRegisterDTO;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;
import com.mycompany.chatbot.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnlyEndpoint() {
        return "Hello Admin! 🔒 You have access.";
    }

    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Hello User! 👋 You have access.");
    }
}

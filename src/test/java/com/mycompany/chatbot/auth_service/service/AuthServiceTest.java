package com.mycompany.chatbot.auth_service.service;

import com.mycompany.chatbot.auth_service.domain.Role;
import com.mycompany.chatbot.auth_service.domain.User;
import com.mycompany.chatbot.auth_service.dto.AuthRequest;
import com.mycompany.chatbot.auth_service.dto.AuthResponse;
import com.mycompany.chatbot.auth_service.dto.UserRegisterDTO;
import com.mycompany.chatbot.auth_service.dto.UserResponseDTO;
import com.mycompany.chatbot.auth_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);

        authService = new AuthService(passwordEncoder, userRepository, jwtService);
    }

    @Test
    void registerUser_shouldCreateUser_whenUsernameIsUnique() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("ksenia");
        dto.setEmail("ksenia@test.com");
        dto.setPassword("12345");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("ksenia");
        savedUser.setEmail("ksenia@test.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);

        when(userRepository.findByUsername("ksenia")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = authService.registerUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("ksenia");
        assertThat(result.getEmail()).isEqualTo("ksenia@test.com");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameAlreadyExists() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("ksenia");
        dto.setEmail("ksenia@test.com");
        dto.setPassword("12345");

        User existingUser = new User();
        existingUser.setUsername("ksenia");

        when(userRepository.findByUsername("ksenia"))
                .thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authService.registerUser(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnUserList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("ksenia");
        user1.setEmail("ksenia@test.com");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("admin");
        user2.setEmail("admin@test.com");
        user2.setRole(Role.ADMIN);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = authService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("ksenia");
        assertThat(result.get(1).getUsername()).isEqualTo("admin");
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ksenia");
        user.setEmail("ksenia@test.com");
        user.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = authService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("ksenia");
        assertThat(result.getEmail()).isEqualTo("ksenia@test.com");
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found with id: 99");
    }

    @Test
    void authenticate_shouldReturnToken_whenCredentialsAreValid() {
        AuthRequest request = new AuthRequest("ksenia", "12345");

        User user = new User();
        user.setUsername("ksenia");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("ksenia"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345", "encodedPassword"))
                .thenReturn(true);
        when(jwtService.generateToken("ksenia", "USER"))
                .thenReturn("jwt-token");

        AuthResponse result = authService.authenticate(request);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo("jwt-token");
    }

    @Test
    void authenticate_shouldThrowException_whenUsernameIsInvalid() {
        AuthRequest request = new AuthRequest("unknown", "12345");

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid credentials");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

    @Test
    void authenticate_shouldThrowException_whenPasswordIsInvalid() {
        AuthRequest request = new AuthRequest("ksenia", "wrong-password");

        User user = new User();
        user.setUsername("ksenia");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("ksenia"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encodedPassword"))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid credentials");

        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

}
package com.mycompany.chatbot.auth_service.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secretKey = "my-super-secret-key-for-jwt-testing-123456789";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 1000 * 60 * 60L);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtService.generateToken("ksenia", "USER");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_shouldReturnUsernameFromToken() {
        String token = jwtService.generateToken("ksenia", "USER");

        String username = jwtService.extractUsername(token);

        assertEquals("ksenia", username);
    }

    @Test
    void extractRole_shouldReturnRoleFromToken() {
        String token = jwtService.generateToken("ksenia", "ADMIN");

        String role = jwtService.extractRole(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenIsValidAndUsernameMatches() {
        String token = jwtService.generateToken("ksenia", "USER");

        boolean result = jwtService.validateToken(token, "ksenia");

        assertTrue(result);
    }

    @Test
    void validateToken_shouldReturnFalse_whenUsernameDoesNotMatch() {
        String token = jwtService.generateToken("ksenia", "USER");

        boolean result = jwtService.validateToken(token, "otherUser");

        assertFalse(result);
    }

    @Test
    void validateToken_shouldThrowException_whenTokenIsExpired() {
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", -1000L);

        String expiredToken = jwtService.generateToken("ksenia", "USER");

        assertThrows(ExpiredJwtException.class,
                () -> jwtService.validateToken(expiredToken, "ksenia"));
    }

}
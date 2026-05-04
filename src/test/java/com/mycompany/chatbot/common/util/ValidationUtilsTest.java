package com.mycompany.chatbot.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void requireId_shouldPass_whenValidId() {
        assertDoesNotThrow(() -> ValidationUtils.requireId(1L, "id"));
    }

    @Test
    void requireId_shouldThrow_whenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireId(null, "id"));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireId(-1L, "id"));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireId(0L, "id"));
    }

    @Test
    void requireText_shouldPass_whenValidText() {
        assertDoesNotThrow(() -> ValidationUtils.requireText("Hello", "text"));
    }

    @Test
    void requireText_shouldThrow_whenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireText(null, "text"));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireText(" ", "text"));
    }

    @Test
    void requireEmail_shouldPass_whenValidEmail() {
        assertDoesNotThrow(() -> ValidationUtils.requireEmail("test@email.com", "email"));
    }

    @Test
    void requireEmail_shouldThrow_whenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireEmail(null, "email"));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireEmail(" ", "email"));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.requireEmail("invalid-email", "email"));
    }
}
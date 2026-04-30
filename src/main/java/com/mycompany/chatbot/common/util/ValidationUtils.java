package com.mycompany.chatbot.common.util;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void requireId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number");
        }
    }

    public static void requireText(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
    }

    public static void requireEmail(String email, String fieldName) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException(fieldName + " must be a valid email");
        }
    }
}

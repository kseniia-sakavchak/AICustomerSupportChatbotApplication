package com.mycompany.chatbot.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextUtilsTest {

    @Test
    void normalize_shouldReturnNormalizedText() {
        String input = "  Hello World!  ";
        String expected = TextUtils.normalize(input);

        assertEquals("hello world!", expected);
    }

    @Test
    void normalize_shouldHandleNullInput() {
        String expected = TextUtils.normalize(null);

        assertEquals("", expected);
    }

    @Test
    void normalize_shouldReplaceSpecialCharacters() {
        String input = "It’s a test.";
        String expected = TextUtils.normalize(input);

        assertEquals("it's a test.", expected);
    }
}
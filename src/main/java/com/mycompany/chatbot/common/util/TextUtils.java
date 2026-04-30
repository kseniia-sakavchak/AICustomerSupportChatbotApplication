package com.mycompany.chatbot.common.util;

import java.util.Locale;

public final class TextUtils {

    private TextUtils() {
    }

    public static String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.trim()
                .toLowerCase(Locale.ROOT)
                .replace("’", "'")
                .replace("`", "'")
                .replaceAll("\\s+", " ");
    }

    public static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    public static String safeTrim(String text) {
        return text == null ? "" : text.trim();
    }
}

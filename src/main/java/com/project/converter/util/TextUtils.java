package com.project.converter.util;

// This class contains utility methods for text processing
public class TextUtils {

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // converts null to empty string
    public static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    public static String toTrimmedAndLowercase(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}

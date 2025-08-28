package com.project.converter.util;

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

    // normalizes whitespace, trims leading/trailing and replaces multiple spaces with a single space
    public static String normalizeSpace(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("\\s+", " ");
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }
}

package com.project.converter.util;

public class Validation {

    public static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public static <T> T requireNonNull(T value, String message) {
        if (value == null) throw new IllegalArgumentException(message);
        return value;
    }

}

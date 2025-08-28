package com.project.converter.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Specifiers {

    public Map<String, String> specifiers = new LinkedHashMap<>();

    public static Specifiers of(String key, String value) {
        Specifiers spec = new Specifiers();
        spec.specifiers.put(key, value);
        return spec;
    }

    public static Specifiers empty() {
        return new Specifiers();
    }

}

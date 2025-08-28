package com.project.converter.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

// This class represents specifiers as a map of key-value pairs.
public class Specifiers {

    private Map<String, String> specifiers = new LinkedHashMap<>();

    @JsonValue // ensures that during serialization only the map is the output
    public Map<String, String> getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(Map<String, String> specifiers) {
        this.specifiers = specifiers;
    }

    public static Specifiers of(String key, String value) {
        Specifiers specifier = new Specifiers();
        specifier.specifiers.put(key, value);
        return specifier;
    }

    public static Specifiers empty() {
        return new Specifiers();
    }

}

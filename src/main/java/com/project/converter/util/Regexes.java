package com.project.converter.util;

import java.util.regex.Pattern;

// This class contains regex patterns for matching numbers and signed numbers
public class Regexes {

    // regex patterns to match numbers
    public static final Pattern DECIMAL = Pattern.compile("(\\d+(?:\\.\\d+)?)");

    // regex pattern to match signed numbers (positive or negative)
    public static final Pattern SIGNED_DECIMAL = Pattern.compile("([+-]?\\d+(?:\\.\\d+)?)");

}

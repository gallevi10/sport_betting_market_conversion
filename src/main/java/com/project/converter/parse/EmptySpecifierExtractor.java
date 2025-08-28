package com.project.converter.parse;

import com.project.converter.model.InMarket;
import com.project.converter.model.Specifiers;

// This class always returns empty specifiers.
// Meant for markets without specifiers.
public class EmptySpecifierExtractor implements SpecifierExtractor {
    @Override
    public Specifiers extract(InMarket market) {
        return Specifiers.empty();
    }
}

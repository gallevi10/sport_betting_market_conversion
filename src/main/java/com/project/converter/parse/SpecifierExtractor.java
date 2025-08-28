package com.project.converter.parse;

import com.project.converter.model.InMarket;
import com.project.converter.model.Specifiers;

// This interface defines a method for extracting specifiers from an InMarket object.
public interface SpecifierExtractor {

    Specifiers extract(InMarket market);

}

package com.project.converter.parse;

import com.project.converter.model.InMarket;
import com.project.converter.model.Specifiers;

public interface SpecifierExtractor {

    Specifiers extract(InMarket market);

}

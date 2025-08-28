package com.project.converter.parse;

import com.project.converter.model.InMarket;
import com.project.converter.model.InSelection;
import com.project.converter.model.Specifiers;
import com.project.converter.util.Regexes;
import com.project.converter.util.TextUtils;

import java.util.regex.Matcher;

// This class extracts handicap specifiers from market selections.
public class HandicapSpecifierExtractor implements SpecifierExtractor {

    @Override
    public Specifiers extract(InMarket market) {
        if (market == null || market.getSelections() == null) return Specifiers.empty();

        for (InSelection selection : market.getSelections()) {
            String name = TextUtils.nullToEmpty(selection.getName()).toLowerCase();
            Matcher matcher = Regexes.SIGNED_DECIMAL.matcher(name);
            if (matcher.find()) {
                return Specifiers.of(
                    "hcp", matcher.group(1).substring(1)
                ); // return "hcp" -> value without sign
            }
        }

        return Specifiers.empty();
    }
}

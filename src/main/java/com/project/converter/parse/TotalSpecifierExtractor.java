package com.project.converter.parse;

import com.project.converter.model.InMarket;
import com.project.converter.model.InSelection;
import com.project.converter.model.Specifiers;
import com.project.converter.util.Regexes;
import com.project.converter.util.TextUtils;

import java.util.regex.Matcher;


public class TotalSpecifierExtractor implements SpecifierExtractor{

    @Override
    public Specifiers extract(InMarket market) {
        if (market == null || market.getSelections() == null) return Specifiers.empty();

        for (InSelection selection : market.getSelections()) { // for each selection in the market
            String name = TextUtils.nullToEmpty(selection.getName()).toLowerCase(); // gets the selection name
            if (name.startsWith("over") || name.startsWith("under")) {
                Matcher matcher = Regexes.DECIMAL.matcher(name);
                if (matcher.find()) {
                    return Specifiers.of("total", matcher.group(1)); // returns "total" -> matched number
                }
            }
        }

        return Specifiers.empty();
    }
}

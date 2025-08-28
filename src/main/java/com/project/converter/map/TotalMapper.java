package com.project.converter.map;

import com.project.converter.enums.SelectionType;
import com.project.converter.model.*;
import com.project.converter.parse.TotalSpecifierExtractor;
import com.project.converter.util.TextUtils;
import com.project.converter.util.UidBuilder;

import java.util.ArrayList;
import java.util.List;

// This class maps total markets from input to output format
public class TotalMapper implements MarketMapper {

    private final String marketTypeId;
    private final TotalSpecifierExtractor totalSpecifierExtractor = new TotalSpecifierExtractor();

    public TotalMapper(String marketTypeId) {
        this.marketTypeId = marketTypeId;
    }

    @Override
    public OutMarket map(InMarket inMarket) {
        Specifiers specifiers = totalSpecifierExtractor.extract(inMarket);

        OutMarket outMarket = new OutMarket();
        outMarket.setMarketTypeId(marketTypeId);
        outMarket.setSpecifiers(specifiers);

        String totalSpecifier = specifiers.getSpecifiers().getOrDefault("total", "");
        String marketUid = UidBuilder.buildMarketUid(inMarket.getEventId().trim(), marketTypeId, totalSpecifier);
        outMarket.setMarketUid(marketUid);

        List<OutSelection> outSelections = new ArrayList<>();
        for (InSelection inSelection : inMarket.getSelections()) {
            String selectionName = TextUtils.toTrimmedAndLowercase(inSelection.getName());
            OutSelection outSelection = getTotalOutSelection(inSelection, selectionName, marketUid);
            outSelections.add(outSelection);
        }
        outMarket.setSelections(outSelections);
        return outMarket;
    }

    private static OutSelection getTotalOutSelection(InSelection inSelection, String selectionName, String marketUid) {
        int id;
        if (selectionName.startsWith("over")) {
            id = SelectionType.OVER.id;
        } else if (selectionName.startsWith("under")) {
            id = SelectionType.UNDER.id;
        } else {
            throw new IllegalArgumentException("Unknown total selection name: " + selectionName);
        }

        OutSelection outSelection = new OutSelection();
        outSelection.setSelectionTypeId(id);
        outSelection.setDecimalOdds(Double.parseDouble(inSelection.getOdds()));
        outSelection.setSelectionUid(UidBuilder.buildSelectionUid(marketUid, id));
        return outSelection;
    }
}

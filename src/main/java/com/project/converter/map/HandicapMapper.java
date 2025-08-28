package com.project.converter.map;

import com.project.converter.enums.SelectionType;
import com.project.converter.model.*;
import com.project.converter.parse.HandicapSpecifierExtractor;
import com.project.converter.util.TextUtils;
import com.project.converter.util.UidBuilder;

import java.util.ArrayList;
import java.util.List;

// This class maps handicap markets from input to output format
public class HandicapMapper implements MarketMapper{

    private final String marketTypeId;
    private final HandicapSpecifierExtractor handicapSpecifierExtractor =
        new HandicapSpecifierExtractor();

    public HandicapMapper(String marketTypeId) {
        this.marketTypeId = marketTypeId;
    }

    @Override
    public OutMarket map(InMarket inMarket) {
        Specifiers specifiers = handicapSpecifierExtractor.extract(inMarket); // extracts specifiers

        OutMarket outMarket = new OutMarket();
        outMarket.setMarketTypeId(marketTypeId);
        outMarket.setSpecifiers(specifiers);

        String hcpSpecifier = specifiers.getSpecifiers().getOrDefault("hcp", "");
        String marketUid = UidBuilder.buildMarketUid(
            inMarket.getEventId().trim(), marketTypeId, hcpSpecifier
        );
        outMarket.setMarketUid(marketUid);

        List<OutSelection> outSelections = new ArrayList<>();
        for (InSelection inSelection : inMarket.getSelections()) {
            String selectionName = TextUtils.toTrimmedAndLowercase(inSelection.getName());
            OutSelection outSelection = getHandicapOutSelection(inSelection, selectionName, marketUid);
            outSelections.add(outSelection);
        }
        outMarket.setSelections(outSelections);
        return outMarket;
    }

    private static OutSelection getHandicapOutSelection(InSelection inSelection,
                                                        String selectionName,
                                                        String marketUid) {
        int id;
        if (selectionName.startsWith("team a")) {
            id = SelectionType.HCP_TEAM_A.id;
        } else if (selectionName.startsWith("team b")) {
            id = SelectionType.HCP_TEAM_B.id;
        } else {
            throw new IllegalArgumentException("Unknown handicap selection name: " + selectionName);
        }

        OutSelection outSelection = new OutSelection();
        outSelection.setSelectionTypeId(id);
        outSelection.setDecimalOdds(Double.parseDouble(inSelection.getOdds()));
        outSelection.setSelectionUid(UidBuilder.buildSelectionUid(marketUid, id));
        return outSelection;
    }
}

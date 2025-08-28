package com.project.converter.map;

import com.project.converter.enums.SelectionType;
import com.project.converter.model.*;
import com.project.converter.parse.HandicapSpecifierExtractor;
import com.project.converter.util.TextUtils;
import com.project.converter.util.UidBuilder;

import java.util.ArrayList;
import java.util.List;

public class HandicapMapper implements MarketMapper{

    private final String marketTypeId;
    private final HandicapSpecifierExtractor extractor = new HandicapSpecifierExtractor();

    public HandicapMapper(String marketTypeId) {
        this.marketTypeId = marketTypeId;
    }

    @Override
    public OutMarket map(InMarket inMarket) {
        Specifiers specifiers = extractor.extract(inMarket);

        OutMarket outMarket = new OutMarket();
        outMarket.setMarketTypeId(marketTypeId);
        outMarket.setSpecifiers(specifiers);

        String hcp = specifiers.getSpecifiers().getOrDefault("hcp", "");
        String marketUid = UidBuilder.buildMarketUid(inMarket.getEventId(), marketTypeId, hcp);
        outMarket.setMarketUid(marketUid);

        List<OutSelection> outSelections = new ArrayList<>();
        for (InSelection inSelection : inMarket.getSelections()) {
            String selectionName = TextUtils.toTrimmedAndLowercase(inSelection.getName());
            OutSelection o = getHandicapOutSelection(inSelection, selectionName, marketUid);
            outSelections.add(o);
        }
        outMarket.setSelections(outSelections);
        return outMarket;
    }

    private static OutSelection getHandicapOutSelection(InSelection inSelection, String selectionName, String marketUid) {
        int id;
        if (selectionName.startsWith("team a")) {
            id = SelectionType.HCP_TEAM_A.id;
        } else if (selectionName.startsWith("team b")) {
            id = SelectionType.HCP_TEAM_B.id;
        } else {
            throw new IllegalArgumentException("Unknown handicap selection name: " + selectionName);
        }

        OutSelection o = new OutSelection();
        o.setSelectionTypeId(id);
        o.setDecimalOdds(Double.parseDouble(inSelection.getOdds()));
        o.setSelectionUid(UidBuilder.buildSelectionUid(marketUid, id));
        return o;
    }
}

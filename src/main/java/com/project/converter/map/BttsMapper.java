package com.project.converter.map;

import com.project.converter.enums.MarketType;
import com.project.converter.enums.SelectionType;
import com.project.converter.model.*;
import com.project.converter.util.TextUtils;
import com.project.converter.util.UidBuilder;

import java.util.ArrayList;
import java.util.List;

// This class maps "Both Teams To Score" (BTTS) markets from input to output format
public class BttsMapper implements MarketMapper {
    @Override
    public OutMarket map(InMarket inMarket) {

        OutMarket outMarket = new OutMarket();
        outMarket.setMarketTypeId(MarketType.BTTS.id);
        outMarket.setSpecifiers(Specifiers.empty());

        String marketUid = UidBuilder.buildMarketUid(
            inMarket.getEventId().trim(), MarketType.BTTS.id, ""
        );
        outMarket.setMarketUid(marketUid);

        List<OutSelection> outSelections = new ArrayList<>();
        for (InSelection inSelection : inMarket.getSelections()) {
            String selectionName = TextUtils.toTrimmedAndLowercase(inSelection.getName());
            OutSelection outSelection = getBttsOutSelection(inSelection, selectionName, marketUid);
            outSelections.add(outSelection);
        }
        outMarket.setSelections(outSelections);
        return outMarket;
    }

    private static OutSelection getBttsOutSelection(InSelection inSelection, String selectionName, String marketUid) {
        int id;
        if (selectionName.equals("yes")) {
            id = SelectionType.YES.id;
        } else if (selectionName.equals("no")) {
            id = SelectionType.NO.id;
        } else {
            throw new IllegalArgumentException("Unknown BTTS selection name: " + selectionName);
        }

        OutSelection outSelection = new OutSelection();
        outSelection.setSelectionTypeId(id);
        outSelection.setDecimalOdds(Double.parseDouble(inSelection.getOdds()));
        outSelection.setSelectionUid(UidBuilder.buildSelectionUid(marketUid, id));
        return outSelection;
    }
}

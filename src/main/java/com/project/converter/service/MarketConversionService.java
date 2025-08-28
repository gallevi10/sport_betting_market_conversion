package com.project.converter.service;

import com.project.converter.enums.MarketType;
import com.project.converter.map.BttsMapper;
import com.project.converter.map.HandicapMapper;
import com.project.converter.map.OneXTwoMapper;
import com.project.converter.map.TotalMapper;
import com.project.converter.model.InMarket;
import com.project.converter.model.OutMarket;
import com.project.converter.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static com.project.converter.util.Validation.require;
import static com.project.converter.util.Validation.requireNonNull;

// This service handles conversion of input market data to output market data using the appropriate mappers
public class MarketConversionService {

    // converts single input market to output market
    public OutMarket convert(InMarket inMarket) {
        validateInMarket(inMarket);

        MarketType marketType = MarketType.fromName(inMarket.getName());

        return switch (marketType) {
            case ONE_X_TWO -> new OneXTwoMapper().map(inMarket);
            case TOTAL, FIRST_HALF_TOTAL -> new TotalMapper(marketType.id).map(inMarket);
            case HANDICAP, FIRST_HALF_HANDICAP, SECOND_HALF_HANDICAP ->
                new HandicapMapper(marketType.id).map(inMarket);
            case BTTS -> new BttsMapper().map(inMarket);
        };
    }

    // converts list of input markets to list of output markets
    public List<OutMarket> convertAll(List<InMarket> inMarkets) {
        if (inMarkets == null) return List.of();
        List<OutMarket> outMarkets = new ArrayList<>(inMarkets.size());
        for (InMarket inMarket : inMarkets) {
            outMarkets.add(convert(inMarket));
        }
        return outMarkets;
    }

    // validates the input market data
    private static void validateInMarket(InMarket inMarket) {
        requireNonNull(inMarket, "inMarket must not be null");
        require(!TextUtils.isBlank(inMarket.getEventId()), "eventId must not be blank");
        require(!TextUtils.isBlank(inMarket.getName()), "Market name must not be blank");
        require(inMarket.getSelections() != null && !inMarket.getSelections().isEmpty(),
            "Selections must not be null or empty");
    }
}

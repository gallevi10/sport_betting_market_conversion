package com.project.converter.map;

import com.project.converter.model.InMarket;
import com.project.converter.model.OutMarket;

// This interface defines a mapper for converting InMarket objects to OutMarket objects
public interface MarketMapper {

    OutMarket map(InMarket inMarket);

}

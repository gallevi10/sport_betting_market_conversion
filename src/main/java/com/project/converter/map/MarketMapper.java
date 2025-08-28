package com.project.converter.map;

import com.project.converter.model.InMarket;
import com.project.converter.model.OutMarket;

public interface MarketMapper {

    OutMarket map(InMarket inMarket);

}

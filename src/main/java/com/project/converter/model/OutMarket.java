package com.project.converter.model;

import java.util.List;
import java.util.Objects;

public class OutMarket {

    String marketUid;

    String marketTypeId;

    Specifiers specifiers;

    List<OutSelection> selections;

    public OutMarket(String marketUid, String marketTypeId,
                     Specifiers specifiers,
                     List<OutSelection> selections) {
        this.marketUid = marketUid;
        this.marketTypeId = marketTypeId;
        this.specifiers = specifiers;
        this.selections = selections;
    }

    public OutMarket() {
    }

    public String getMarketUid() {
        return marketUid;
    }

    public void setMarketUid(String marketUid) {
        this.marketUid = marketUid;
    }

    public String getMarketTypeId() {
        return marketTypeId;
    }

    public void setMarketTypeId(String marketTypeId) {
        this.marketTypeId = marketTypeId;
    }

    public Specifiers getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(Specifiers specifiers) {
        this.specifiers = specifiers;
    }

    public List<OutSelection> getSelections() {
        return selections;
    }

    public void setSelections(List<OutSelection> selections) {
        this.selections = selections;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OutMarket outMarket = (OutMarket) o;
        return Objects.equals(marketUid, outMarket.marketUid) &&
            Objects.equals(marketTypeId, outMarket.marketTypeId) &&
            Objects.equals(specifiers, outMarket.specifiers) &&
            Objects.equals(selections, outMarket.selections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marketUid, marketTypeId, specifiers, selections);
    }
}

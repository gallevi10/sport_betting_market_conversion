package com.project.converter.model;

import java.util.Objects;

public class InSelection {

    String name;

    String odds;

    public InSelection(String name, String odds) {
        this.name = name;
        this.odds = odds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InSelection that = (InSelection) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(odds, that.odds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, odds);
    }
}

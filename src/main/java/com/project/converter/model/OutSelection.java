package com.project.converter.model;

import java.util.Objects;

public class OutSelection {

    String selectionUid;

    int selectionTypeId;

    double decimalOdds;

    public OutSelection() {
    }

    public OutSelection(String selectionUid, int selectionTypeId, double decimalOdds) {
        this.selectionUid = selectionUid;
        this.selectionTypeId = selectionTypeId;
        this.decimalOdds = decimalOdds;
    }

    public String getSelectionUid() {
        return selectionUid;
    }

    public void setSelectionUid(String selectionUid) {
        this.selectionUid = selectionUid;
    }

    public int getSelectionTypeId() {
        return selectionTypeId;
    }

    public void setSelectionTypeId(int selectionTypeId) {
        this.selectionTypeId = selectionTypeId;
    }

    public double getDecimalOdds() {
        return decimalOdds;
    }

    public void setDecimalOdds(double decimalOdds) {
        this.decimalOdds = decimalOdds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OutSelection that = (OutSelection) o;
        return selectionTypeId == that.selectionTypeId &&
            Double.compare(decimalOdds, that.decimalOdds) == 0 &&
            Objects.equals(selectionUid, that.selectionUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectionUid, selectionTypeId, decimalOdds);
    }
}

package com.project.converter.model;

import java.util.List;
import java.util.Objects;

// This class represents an input market object.
public class InMarket {

    String name;

    String eventId;

    List<InSelection> selections;

    public InMarket() {
    }

    public InMarket(String name, String eventId, List<InSelection> selections) {
        this.name = name;
        this.eventId = eventId;
        this.selections = selections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<InSelection> getSelections() {
        return selections;
    }

    public void setSelections(List<InSelection> selections) {
        this.selections = selections;
    }

    @Override
    public String toString() {
        return "InMarket{" +
            "name='" + name + '\'' +
            ", eventId='" + eventId + '\'' +
            ", selections=" + selections +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InMarket inMarket = (InMarket) o;
        return Objects.equals(name, inMarket.name) &&
            Objects.equals(eventId, inMarket.eventId) &&
            Objects.equals(selections, inMarket.selections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventId, selections);
    }
}

package com.project.converter.enums;

public enum MarketType {

    ONE_X_TWO("1", "1x2"),
    TOTAL("18", "Total"),
    FIRST_HALF_TOTAL("68", "1st half - total"),
    HANDICAP("16", "Handicap"),
    FIRST_HALF_HANDICAP("66", "1st half - handicap"),
    SECOND_HALF_HANDICAP("88", "2nd half - handicap"),
    BTTS("50", "Both teams to score");

    public final String id;
    public final String name;

    MarketType(String id, String name){
        this.id = id;
        this.name = name;
    }

    // gets id by name
    public static MarketType fromName(String name){
        String n = name.trim().toLowerCase();
        for (MarketType id : values())
            if (id.name.toLowerCase().equals(n))
                return id;
        throw new IllegalArgumentException("Unknown market name: " + name);
    }
}

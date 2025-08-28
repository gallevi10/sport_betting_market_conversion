package com.project.converter.enums;

public enum SelectionType {

    // Market Type 1
    TEAM_A(1), DRAW(2), TEAM_B(3),

    // Market Types 18/68
    OVER(12), UNDER(13),

    // Market Types 16/66/88
    HCP_TEAM_A(1714), HCP_TEAM_B(1715),

    // Market Type 50
    YES(10), NO(11);

    public final int id;
    SelectionType(int id) {
        this.id = id;
    }
}

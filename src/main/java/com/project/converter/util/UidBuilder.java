package com.project.converter.util;

// This class builds unique identifiers for markets and selections
public class UidBuilder {

    public static String buildMarketUid(String eventId,
                                        String marketTypeId,
                                        String specifier) {
        String spec = TextUtils.isBlank(specifier) ? "" : specifier.trim();
        return eventId + "_" + marketTypeId + (!spec.isEmpty() ? "_" : "") + spec;
    }

    public static String buildSelectionUid(String marketUid, int selectionTypeId) {
        return marketUid + "_" + selectionTypeId;
    }
}

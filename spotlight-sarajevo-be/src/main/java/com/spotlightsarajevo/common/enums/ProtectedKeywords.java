package com.spotlightsarajevo.common.enums;

import lombok.Getter;

@Getter
public enum ProtectedKeywords {
    ALL_SPOTS("ALL-SPOTS"),
    ALL_EVENTS("ALL-EVENTS"),
    SYSTEM("SYSTEM");

    private final String value;

    ProtectedKeywords(String value) {
        this.value = value;
    }
}

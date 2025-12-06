package com.iftekhar.ai_paradox.model;

public enum GroupType {
    GROUP_A("Group A - AI Enabled"),
    GROUP_B("Group B - No AI");

    private final String displayName;

    GroupType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

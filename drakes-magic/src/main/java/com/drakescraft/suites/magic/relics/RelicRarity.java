package com.drakescraft.suites.magic.relics;

/**
 * Niveles de rareza canónicos para las reliquias de Cthonia.
 */
public enum RelicRarity {
    COMMON("Común", "<gray>", 50.0),
    UNCOMMON("Poco Común", "<green>", 25.0),
    RARE("Rara", "<blue>", 15.0),
    EPIC("Épica", "<dark_purple>", 8.0),
    LEGENDARY("Legendaria", "<gold>", 2.0);

    private final String displayName;
    private final String colorTag;
    private final double dropWeight;

    RelicRarity(String displayName, String colorTag, double dropWeight) {
        this.displayName = displayName;
        this.colorTag = colorTag;
        this.dropWeight = dropWeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorTag() {
        return colorTag;
    }

    public double getDropWeight() {
        return dropWeight;
    }
}

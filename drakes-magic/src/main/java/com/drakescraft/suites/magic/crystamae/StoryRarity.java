package com.drakescraft.suites.magic.crystamae;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;

/**
 * Grados de rareza para cristales e historias de Crystamae.
 */
public enum StoryRarity {
    COMMON(1, "Común", ChatColor.WHITE, 1.0),
    UNCOMMON(2, "Poco Común", ChatColor.GREEN, 1.25),
    RARE(3, "Raro", ChatColor.BLUE, 1.6),
    EPIC(4, "Épico", ChatColor.DARK_PURPLE, 2.2),
    MYTHICAL(5, "Mítico", ChatColor.GOLD, 3.0),
    UNIQUE(6, "Único", ChatColor.DARK_RED, 4.5);

    private final int id;
    private final String displayName;
    private final ChatColor color;
    private final double powerMultiplier;

    StoryRarity(int id, String displayName, ChatColor color, double powerMultiplier) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.powerMultiplier = powerMultiplier;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public double getPowerMultiplier() {
        return powerMultiplier;
    }

    @Nullable
    public static StoryRarity getById(int id) {
        for (StoryRarity rarity : values()) {
            if (rarity.id == id) {
                return rarity;
            }
        }
        return null;
    }

    @Nullable
    public static StoryRarity getByName(String name) {
        for (StoryRarity rarity : values()) {
            if (rarity.name().equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }
}

package com.drakescraft.suites.magic.crystamae;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;

/**
 * Los 9 Dominios Conceptuales de Historias en Crystamae.
 */
public enum StoryType {
    ELEMENTAL(1, "Elemental", ChatColor.RED),
    MECHANICAL(2, "Mecánico", ChatColor.GRAY),
    ALCHEMICAL(3, "Alquímico", ChatColor.DARK_PURPLE),
    HISTORICAL(4, "Histórico", ChatColor.GOLD),
    HUMAN(5, "Humano", ChatColor.BLUE),
    ANIMAL(6, "Animal", ChatColor.DARK_GREEN),
    CELESTIAL(7, "Celestial", ChatColor.AQUA),
    VOID(8, "Vacío", ChatColor.DARK_GRAY),
    PHILOSOPHICAL(9, "Filosófico", ChatColor.LIGHT_PURPLE);

    private final int id;
    private final String displayName;
    private final ChatColor color;

    StoryType(int id, String displayName, ChatColor color) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
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

    @Nullable
    public static StoryType getById(int id) {
        for (StoryType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static StoryType getByName(String name) {
        for (StoryType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}

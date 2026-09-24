package com.drakescraft.suites.magic.crystamae;

import org.bukkit.ChatColor;

import java.util.Objects;

/**
 * Representa una narrativa conceptual o memoria mística cristalizada en Crystamae.
 */
public class CrystamaeStory {

    private final String id;
    private final String title;
    private final StoryType type;
    private final StoryRarity rarity;
    private final String description;
    private final boolean gilded;

    public CrystamaeStory(String id, String title, StoryType type, StoryRarity rarity, String description, boolean gilded) {
        this.id = Objects.requireNonNull(id, "Story ID cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.rarity = Objects.requireNonNull(rarity, "Rarity cannot be null");
        this.description = description != null ? description : "";
        this.gilded = gilded;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public StoryType getType() {
        return type;
    }

    public StoryRarity getRarity() {
        return rarity;
    }

    public String getDescription() {
        return description;
    }

    public boolean isGilded() {
        return gilded;
    }

    public String getFormattedTitle() {
        ChatColor color = rarity.getColor();
        String prefix = gilded ? ChatColor.GOLD + "✦ " : "";
        return prefix + color + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrystamaeStory that = (CrystamaeStory) o;
        return gilded == that.gilded && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gilded);
    }

    @Override
    public String toString() {
        return "CrystamaeStory{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", rarity=" + rarity +
                ", gilded=" + gilded +
                '}';
    }
}

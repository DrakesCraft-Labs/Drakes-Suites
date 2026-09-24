package com.drakescraft.suites.utility.backpacks;

/**
 * Representa los 6 niveles o categorías de mochilas en Slimefun/DrakesCraft.
 * Preserva exactamente los IDs de ítem canónicos (BACKPACK_SMALL, BACKPACK_MEDIUM, etc.).
 */
public enum BackpackTier {
    SMALL("BACKPACK_SMALL", "<gold>Mochila Pequeña</gold>", 9),
    MEDIUM("BACKPACK_MEDIUM", "<gold>Mochila Mediana</gold>", 18),
    LARGE("BACKPACK_LARGE", "<gold>Mochila Grande</gold>", 27),
    WOVEN("WOVEN_BACKPACK", "<aqua>Mochila Tejida</aqua>", 36),
    GILDED("GILDED_BACKPACK", "<yellow>Mochila Dorada</yellow>", 45),
    RADIANT("RADIANT_BACKPACK", "<light_purple>Mochila Radiante</light_purple>", 54);

    private final String id;
    private final String displayName;
    private final int slots;

    BackpackTier(String id, String displayName, int slots) {
        this.id = id;
        this.displayName = displayName;
        this.slots = slots;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSlots() {
        return slots;
    }

    public static BackpackTier fromId(String id) {
        if (id == null) return null;
        for (BackpackTier tier : values()) {
            if (tier.id.equalsIgnoreCase(id)) {
                return tier;
            }
        }
        return null;
    }
}

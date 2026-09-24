package com.drakescraft.suites.bio.flowerpower;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nullable;

/**
 * Tipos de amuletos místicos de FlowerPower y sus atributos correspondientes.
 */
public enum CharmType {
    MOVEMENT_SPEED("MOVEMENT_SPEED_CHARM", "Amuleto de Celeridad", Attribute.MOVEMENT_SPEED, 0.20, ChatColor.AQUA),
    ATTACK_SPEED("ATTACK_SPEED_CHARM", "Amuleto de Furia Voraz", Attribute.ATTACK_SPEED, 0.30, ChatColor.RED),
    FLY_SPEED("FLY_SPEED_CHARM", "Amuleto de Vuelo Etéreo", Attribute.FLYING_SPEED, 0.25, ChatColor.LIGHT_PURPLE),
    DAMAGE("DAMAGE_CHARM", "Amuleto de Poder Bélico", Attribute.ATTACK_DAMAGE, 4.0, ChatColor.DARK_RED),
    HEALTH("HEALTH_CHARM", "Amuleto de Vitalidad Arbórea", Attribute.MAX_HEALTH, 6.0, ChatColor.GREEN),
    KNOCKBACK_RESISTANCE("KNOCKBACK_RESISTANCE_CHARM", "Amuleto de Firmeza Telúrica", Attribute.KNOCKBACK_RESISTANCE, 0.50, ChatColor.GOLD);

    private final String slimefunId;
    private final String displayName;
    private final Attribute attribute;
    private final double value;
    private final ChatColor color;

    CharmType(String slimefunId, String displayName, Attribute attribute, double value, ChatColor color) {
        this.slimefunId = slimefunId;
        this.displayName = displayName;
        this.attribute = attribute;
        this.value = value;
        this.color = color;
    }

    public String getSlimefunId() {
        return slimefunId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getValue() {
        return value;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getFormattedName() {
        return color + "" + ChatColor.BOLD + displayName;
    }

    @Nullable
    public static CharmType getBySlimefunId(String id) {
        if (id == null) return null;
        for (CharmType type : values()) {
            if (type.slimefunId.equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }
}

package com.drakescraft.suites.magic.relics;

import org.bukkit.Material;

/**
 * Definición inmutable de cada reliquia de Cthonia.
 */
public record RelicDefinition(
        String id,
        String displayName,
        Material material,
        RelicRarity rarity,
        String description,
        int xpReward
) {
    public String getPdcKey() {
        return "CTHONIAN_RELIC_" + id;
    }
}

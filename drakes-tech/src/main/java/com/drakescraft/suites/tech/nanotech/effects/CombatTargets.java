package com.drakescraft.suites.tech.nanotech.effects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

/** Central immutable PvE boundary shared by every cinematic weapon. */
public final class CombatTargets {
    private CombatTargets() {}

    /** Accepts actual mobs only and rejects common NPC, pet and grave markers. */
    public static boolean isHostileEffectTarget(Entity entity) {
        if (!(entity instanceof Mob)) return false;
        if (entity.hasMetadata("NPC") || entity.hasMetadata("CitizensNPC") || entity.hasMetadata("grave")) return false;
        return entity.getScoreboardTags().stream().noneMatch(tag -> {
            String normalized = tag.toLowerCase(java.util.Locale.ROOT);
            return normalized.contains("npc") || normalized.contains("grave") || normalized.contains("protected_pet");
        });
    }
}

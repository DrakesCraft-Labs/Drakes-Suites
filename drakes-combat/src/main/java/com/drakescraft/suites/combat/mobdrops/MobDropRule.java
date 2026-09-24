package com.drakescraft.suites.combat.mobdrops;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Representa una regla de drop para una entidad en combate.
 */
public class MobDropRule {

    private final EntityType entityType;
    private final String slimefunId;
    private final Material fallbackMaterial;
    private final double chancePercent;
    private final int minAmount;
    private final int maxAmount;

    public MobDropRule(EntityType entityType, String slimefunId, Material fallbackMaterial, double chancePercent, int minAmount, int maxAmount) {
        this.entityType = entityType;
        this.slimefunId = slimefunId;
        this.fallbackMaterial = fallbackMaterial;
        this.chancePercent = chancePercent;
        this.minAmount = Math.max(1, minAmount);
        this.maxAmount = Math.max(this.minAmount, maxAmount);
    }

    public EntityType getEntityType() { return entityType; }
    public String getSlimefunId() { return slimefunId; }
    public Material getFallbackMaterial() { return fallbackMaterial; }
    public double getChancePercent() { return chancePercent; }
    public int getMinAmount() { return minAmount; }
    public int getMaxAmount() { return maxAmount; }
}

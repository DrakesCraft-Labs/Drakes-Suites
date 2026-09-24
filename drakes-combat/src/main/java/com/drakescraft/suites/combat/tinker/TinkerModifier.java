package com.drakescraft.suites.combat.tinker;

import java.util.Objects;

/**
 * Modificador aplicado a una herramienta forjada de SlimeTinker.
 */
public class TinkerModifier {

    private final TinkerTrait trait;
    private final int level;
    private final double activationChance;

    public TinkerModifier(TinkerTrait trait, int level, double activationChance) {
        this.trait = Objects.requireNonNull(trait, "trait cannot be null");
        this.level = Math.max(1, Math.min(10, level));
        this.activationChance = Math.max(0.0, Math.min(1.0, activationChance));
    }

    public TinkerTrait getTrait() {
        return trait;
    }

    public int getLevel() {
        return level;
    }

    public double getActivationChance() {
        return activationChance;
    }

    public double calculateEffectiveFactor() {
        return trait.getBaseFactor() * level * activationChance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TinkerModifier that)) return false;
        return level == that.level && trait == that.trait;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trait, level);
    }
}

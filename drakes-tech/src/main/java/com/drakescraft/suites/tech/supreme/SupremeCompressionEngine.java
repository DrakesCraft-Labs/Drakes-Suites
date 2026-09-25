package com.drakescraft.suites.tech.supreme;

/**
 * Motor de compresión cuántica, cálculo de multiplicadores energéticos y validación de tiers Supreme.
 */
public final class SupremeCompressionEngine {

    private final int tierLimit;
    private final double quantumReactorMultiplier;
    private final boolean strictCraftingCheck;

    public SupremeCompressionEngine(int tierLimit, double quantumReactorMultiplier, boolean strictCraftingCheck) {
        this.tierLimit = Math.clamp(tierLimit, 1, 10);
        this.quantumReactorMultiplier = Math.max(1.0, quantumReactorMultiplier);
        this.strictCraftingCheck = strictCraftingCheck;
    }

    /**
     * Calcula la energía generada o almacenada por un componente cuántico según su tier (1 a 10).
     */
    public long calculateQuantumEnergyOutput(int tier, long baseJoules) {
        if (tier < 1 || tier > tierLimit) {
            return 0L;
        }
        // Progresión exponencial acotada por tier
        double multiplier = Math.pow(quantumReactorMultiplier, tier - 1);
        return (long) Math.min((double) Long.MAX_VALUE, baseJoules * multiplier);
    }

    /**
     * Valida si un ítem de crafteo cumple con los requisitos del tier y anti-dupe estricto.
     */
    public boolean isValidCraftingOperation(int inputAmount, int requiredAmount, boolean matchesPdcSignature) {
        if (strictCraftingCheck && !matchesPdcSignature) {
            return false;
        }
        return inputAmount >= requiredAmount && requiredAmount > 0;
    }

    /**
     * Calcula la densidad de compresión necesaria para matrices Supreme de alto nivel (Tiers 1 a 10).
     */
    public long calculateCompressionDensity(int tier) {
        int boundedTier = Math.clamp(tier, 1, tierLimit);
        return (long) Math.pow(4, boundedTier + 3); // Base 4^4 = 256 hasta 4^13 para Tier 10
    }

    /**
     * Determina el número de singularidades base requeridas para forjar una Matriz de Singularidad Supreme.
     */
    public int calculateSingularityMatrixCost(int tier) {
        int boundedTier = Math.clamp(tier, 1, tierLimit);
        return Math.max(1, boundedTier * 2);
    }

    public int getTierLimit() {
        return tierLimit;
    }

    public double getQuantumReactorMultiplier() {
        return quantumReactorMultiplier;
    }

    public boolean isStrictCraftingCheck() {
        return strictCraftingCheck;
    }
}

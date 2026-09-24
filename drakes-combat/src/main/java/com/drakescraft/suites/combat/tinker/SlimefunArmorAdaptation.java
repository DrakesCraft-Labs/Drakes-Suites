package com.drakescraft.suites.combat.tinker;

/**
 * Adaptador Canónico de Armaduras Slimefun & SlimeTinker para DrakesBosses (AGENTS.md Secc. 6.A).
 * Mapea los tiers de SlimeTinker e Infinity aplicando Daño Verdadero (True Damage)
 * proporcional a la absorción pasiva del jugador, erradicando el juego AFK ante los Dioses.
 */
public class SlimefunArmorAdaptation {

    private final double antiAfkDecayRate;
    private final boolean trueDamageEnabled;

    public SlimefunArmorAdaptation(double antiAfkDecayRate, boolean trueDamageEnabled) {
        this.antiAfkDecayRate = Math.max(0.0, antiAfkDecayRate);
        this.trueDamageEnabled = trueDamageEnabled;
    }

    /**
     * Evalúa el daño de penetración contra un set de armadura SlimeTinker o Infinity.
     *
     * @param armorTier Nivel de la armadura (1-5 SlimeTinker, 10=Infinity Singularity)
     * @param consecutiveAfkHits Golpes consecutivos infligidos/recibidos sin movimiento
     * @param baseBossDamage Daño base de ataque del Dios o Boss
     * @return Daño Verdadero (True Damage) que penetra la resistencia pasiva
     */
    public double calculateTrueDamagePenetration(int armorTier, int consecutiveAfkHits, double baseBossDamage) {
        if (!trueDamageEnabled || baseBossDamage <= 0.0) {
            return 0.0;
        }

        // Tiers superiores sufren un factor de adaptación divino mayor para evitar trivializar el combate
        double tierFactor;
        if (armorTier >= 10) {
            // Full Infinity Singularity Set
            tierFactor = 0.45;
        } else if (armorTier >= 5) {
            // SlimeTinker Masterwork / Adamantite
            tierFactor = 0.30;
        } else if (armorTier >= 3) {
            // SlimeTinker Reinforced
            tierFactor = 0.15;
        } else {
            tierFactor = 0.05;
        }

        // Penalización por combate estático/AFK acumulada
        double afkPenalty = Math.min(2.0, 1.0 + (consecutiveAfkHits * antiAfkDecayRate));

        return baseBossDamage * tierFactor * afkPenalty;
    }

    public double getAntiAfkDecayRate() {
        return antiAfkDecayRate;
    }

    public boolean isTrueDamageEnabled() {
        return trueDamageEnabled;
    }
}

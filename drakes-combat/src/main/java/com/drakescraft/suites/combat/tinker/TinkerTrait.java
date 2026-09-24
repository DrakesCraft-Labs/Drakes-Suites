package com.drakescraft.suites.combat.tinker;

/**
 * Rasgos y afinidades elementales aplicables a armas y herramientas de SlimeTinker.
 */
public enum TinkerTrait {
    LIFESTEAL("Robo de Vida", 0.15, "Restaura un porcentaje del daño infligido"),
    LIGHTNING_STRIKE("Descarga Celestial", 0.25, "Invoca un rayo al golpear"),
    VOID_EDGE("Filo del Vacío", 0.20, "Ignora armaduras físicas convencionales"),
    DIVINE_SLAYER("Asesino de Dioses", 0.35, "Incrementa daño contra entidades divinas de DrakesBosses"),
    MOMENTUM("Inercia Cósmica", 0.10, "Aumenta la velocidad de ataque por cada golpe consecutivo"),
    REINFORCED("Reforzado Primordial", 0.30, "Probabilidad de no consumir durabilidad"),
    COSMIC_DECAY("Desintegración Cósmica", 0.40, "Aplica daño sostenido irreducible");

    private final String displayName;
    private final double baseFactor;
    private final String description;

    TinkerTrait(String displayName, double baseFactor, String description) {
        this.displayName = displayName;
        this.baseFactor = baseFactor;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getBaseFactor() {
        return baseFactor;
    }

    public String getDescription() {
        return description;
    }
}

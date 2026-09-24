package com.drakescraft.suites.tech.infinity;

/**
 * Motor de mitigación y evaluación de armadura de Infinity para DrakesTech.
 * Implementa las reglas del canon de DrakesCraft (AGENTS.md):
 * - Reducción de daño convencional hasta el límite configurado (ej. 95%).
 * - Salvamento de caída al vacío (Anti-Void).
 * - Código Anti-Infinity activo: El daño verdadero (True Damage) procedente de jefes celestiales
 *   (DrakesBosses / Mahoraga / Zeus / Hades) quebranta la invulnerabilidad absoluta.
 */
public class InfinityArmorEngine {

    private final double damageReduction;
    private final boolean antiVoid;
    private final boolean godTrueDamageBypass;

    public InfinityArmorEngine(double damageReduction, boolean antiVoid, boolean godTrueDamageBypass) {
        this.damageReduction = Math.max(0.0, Math.min(0.99, damageReduction));
        this.antiVoid = antiVoid;
        this.godTrueDamageBypass = godTrueDamageBypass;
    }

    /**
     * Calcula el daño resultante tras la aplicación de la protección de la armadura Infinity.
     *
     * @param rawDamage Daño base recibido
     * @param isVoid Si el daño proviene del vacío
     * @param isTrueDamage Si el daño es de tipo Daño Verdadero (jefes divinos)
     * @return Daño neto a aplicar al jugador
     */
    public double calculateDamage(double rawDamage, boolean isVoid, boolean isTrueDamage) {
        if (rawDamage <= 0) {
            return 0.0;
        }

        // Si es daño de vacío y anti-void está activado, se absorbe el daño para permitir el rescate
        if (isVoid && antiVoid) {
            return 0.0;
        }

        // Canon DrakesCraft: Jefes divinos con True Damage ignoran la reducción Infinity (FullInfinityArmorCounter)
        if (isTrueDamage && godTrueDamageBypass) {
            return rawDamage;
        }

        // Mitigación convencional
        return rawDamage * (1.0 - damageReduction);
    }

    public double getDamageReduction() {
        return damageReduction;
    }

    public boolean isAntiVoid() {
        return antiVoid;
    }

    public boolean isGodTrueDamageBypass() {
        return godTrueDamageBypass;
    }
}

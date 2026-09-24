package com.drakescraft.suites.generators.ecopower;

/**
 * Motor puro de cálculo ambiental y energético para generadores ecológicos.
 * Desacoplado de NMS y testeable sin servidor Minecraft activo.
 */
public final class EcoPowerEngine {

    private EcoPowerEngine() {}

    /**
     * Determina si el mundo se encuentra en ciclo diurno (sol visible).
     * En Minecraft vanilla el día transcurre entre los ticks 0 y 12300 (aprox).
     */
    public static boolean isDay(long worldTime) {
        long time = worldTime % 24000L;
        return time < 12300L || time > 23850L;
    }

    /**
     * Determina si el mundo se encuentra en ciclo nocturno (luna visible).
     */
    public static boolean isNight(long worldTime) {
        return !isDay(worldTime);
    }

    /**
     * Calcula la generación efectiva de una turbina eólica en base a la altura Y y el clima.
     * Mayor altitud = mayor viento (escala hasta un 150% del base).
     * En tormenta el viento aumenta un 50% extra.
     */
    public static int calculateWindPower(int basePower, int blockY, boolean hasStorm) {
        if (blockY < 64) {
            return Math.max(1, basePower / 2);
        }
        // Factor de altura: 64 a 256 -> 1.0x a 1.5x
        double altitudeFactor = 1.0D + Math.min(0.5D, (blockY - 64) / 384.0D);
        double weatherFactor = hasStorm ? 1.5D : 1.0D;

        return (int) Math.round(basePower * altitudeFactor * weatherFactor);
    }

    /**
     * Calcula la generación solar. En lluvia/tormenta se reduce al 20%. De noche es 0.
     */
    public static int calculateSolarPower(int basePower, long worldTime, boolean hasStorm) {
        if (!isDay(worldTime)) {
            return 0;
        }
        return hasStorm ? Math.max(1, basePower / 5) : basePower;
    }

    /**
     * Calcula la generación lunar. Solo activa de noche. En tormenta densa se reduce a la mitad.
     */
    public static int calculateLunarPower(int basePower, long worldTime, boolean hasStorm) {
        if (!isNight(worldTime)) {
            return 0;
        }
        return hasStorm ? Math.max(1, basePower / 2) : basePower;
    }

    /**
     * Calcula la potencia captada por un receptor de tormentas.
     * Genera solo durante tormentas eléctricas o lluvias intensas.
     */
    public static int calculateLightningReceptorPower(int basePower, boolean isThundering, boolean hasStorm) {
        if (isThundering) {
            return basePower * 2; // Rayos directos
        } else if (hasStorm) {
            return basePower / 2; // Carga estática de lluvia
        }
        return 0; // Día despejado
    }

    /**
     * Calcula la generación de una turbina de vapor según las fuentes de calor adyacentes.
     */
    public static int calculateSteamPower(int basePower, int heatSources) {
        if (heatSources <= 0) return 0;
        // Cada fuente de calor (magma/lava) aporta un factor multiplicador
        double multiplier = Math.min(2.0D, 0.5D + (heatSources * 0.25D));
        return (int) Math.round(basePower * multiplier);
    }
}

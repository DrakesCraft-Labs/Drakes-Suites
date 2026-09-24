package com.drakescraft.suites.generators.smg;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Motor puro de generación y acumulación de ticks para generadores SMG.
 */
public class SMGGeneratorEngine {

    private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();
    private final double overclockMultiplier;

    public SMGGeneratorEngine(double overclockMultiplier) {
        this.overclockMultiplier = Math.max(0.1, overclockMultiplier);
    }

    /**
     * Avanza un tick para una posición determinada de generador.
     * Retorna true si se alcanzó la cuota de ticks para producir un material.
     */
    public boolean stepTick(String blockKey, SMGMaterialType type) {
        if (blockKey == null || type == null) return false;

        int baseRate = type.getDefaultRateTicks();
        int effectiveRate = Math.max(1, (int) Math.round(baseRate / overclockMultiplier));

        int current = progressMap.getOrDefault(blockKey, 0) + 1;
        if (current >= effectiveRate) {
            progressMap.put(blockKey, 0);
            return true;
        } else {
            progressMap.put(blockKey, current);
            return false;
        }
    }

    /**
     * Produce el item generado correspondiente.
     */
    public ItemStack produceOutput(SMGMaterialType type, int amount) {
        if (type == null) return null;
        return new ItemStack(type.getProducedMaterial(), Math.max(1, amount));
    }

    public int getProgress(String blockKey) {
        return progressMap.getOrDefault(blockKey, 0);
    }

    public void reset(String blockKey) {
        progressMap.remove(blockKey);
    }

    public void clear() {
        progressMap.clear();
    }
}

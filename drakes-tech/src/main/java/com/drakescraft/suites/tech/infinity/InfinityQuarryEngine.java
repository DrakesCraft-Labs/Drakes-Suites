package com.drakescraft.suites.tech.infinity;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Motor de canteras cuánticas y extracción del vacío para Infinity Expansion en DrakesTech.
 * Incorpora reguladores de frecuencia por chunk (Chunk Rate Throttling) para prevenir caídas de TPS en Dallas.
 */
public class InfinityQuarryEngine {

    private final int maxBlocksPerTick;
    private final long baseEnergyPerBlock;
    private final boolean chunkProtection;
    private final ConcurrentHashMap<String, AtomicInteger> chunkQuarryCount = new ConcurrentHashMap<>();

    public InfinityQuarryEngine(int maxBlocksPerTick, long baseEnergyPerBlock, boolean chunkProtection) {
        this.maxBlocksPerTick = Math.max(1, maxBlocksPerTick);
        this.baseEnergyPerBlock = Math.max(1L, baseEnergyPerBlock);
        this.chunkProtection = chunkProtection;
    }

    /**
     * Calcula la energía necesaria para la operación de la cantera según el tier y profundidad.
     *
     * @param tier Nivel de la cantera (1=Basic, 2=Advanced, 3=Void, 4=Infinity)
     * @param depthFactor Factor de profundidad (ej. minado bajo Y=0 en 1.21)
     * @return Consumo de energía en J
     */
    public long calculateEnergyRequired(int tier, double depthFactor) {
        int safeTier = Math.max(1, Math.min(5, tier));
        double multiplier = Math.pow(1.5, safeTier - 1) * Math.max(1.0, depthFactor);
        return (long) (baseEnergyPerBlock * multiplier);
    }

    /**
     * Registra una cantera activa en un chunk y verifica si no excede el límite permitido.
     *
     * @param chunkKey Clave del chunk ("world:x:z")
     * @param maxPerChunk Límite máximo de canteras por chunk
     * @return true si se permite la operación
     */
    public boolean registerQuarryInChunk(String chunkKey, int maxPerChunk) {
        Objects.requireNonNull(chunkKey, "chunkKey cannot be null");
        if (!chunkProtection) return true;

        AtomicInteger count = chunkQuarryCount.computeIfAbsent(chunkKey, k -> new AtomicInteger(0));
        int current = count.incrementAndGet();
        if (current > maxPerChunk) {
            count.decrementAndGet();
            return false;
        }
        return true;
    }

    /**
     * Libera una cantera activa de un chunk.
     */
    public void unregisterQuarryInChunk(String chunkKey) {
        if (chunkKey == null || !chunkProtection) return;
        AtomicInteger count = chunkQuarryCount.get(chunkKey);
        if (count != null) {
            count.updateAndGet(c -> Math.max(0, c - 1));
        }
    }

    public int getMaxBlocksPerTick() {
        return maxBlocksPerTick;
    }

    public long getBaseEnergyPerBlock() {
        return baseEnergyPerBlock;
    }

    public boolean isChunkProtection() {
        return chunkProtection;
    }

    public void clear() {
        chunkQuarryCount.clear();
    }
}

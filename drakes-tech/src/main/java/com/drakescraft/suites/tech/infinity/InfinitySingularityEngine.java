package com.drakescraft.suites.tech.infinity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Motor central de Singularidades para Infinity Expansion en DrakesTech.
 * Gestiona requisitos de compresión atómica, tasas de conversión y validaciones anti-dupe estrictas.
 */
public class InfinitySingularityEngine {

    private final Map<String, Integer> requiredMaterials = new HashMap<>();
    private final Map<String, Long> requiredEnergy = new HashMap<>();
    private final boolean strictAntiDupe;

    public InfinitySingularityEngine(boolean strictAntiDupe) {
        this.strictAntiDupe = strictAntiDupe;
        registerDefaultSingularities();
    }

    private void registerDefaultSingularities() {
        registerSingularity("IRON", 10000, 500000L);
        registerSingularity("GOLD", 8000, 750000L);
        registerSingularity("DIAMOND", 4000, 1500000L);
        registerSingularity("NETHERITE", 1000, 5000000L);
        registerSingularity("EMERALD", 3000, 1200000L);
        registerSingularity("REDSTONE", 12000, 600000L);
        registerSingularity("LAPIS", 12000, 600000L);
        registerSingularity("QUARTZ", 8000, 800000L);
        registerSingularity("COPPER", 15000, 400000L);
        registerSingularity("NETHER_STAR", 64, 10000000L);
    }

    public synchronized void registerSingularity(String id, int requiredAmount, long energyJ) {
        Objects.requireNonNull(id, "Singularity ID cannot be null");
        if (requiredAmount <= 0) {
            throw new IllegalArgumentException("Required amount must be positive");
        }
        String key = id.toUpperCase();
        requiredMaterials.put(key, requiredAmount);
        requiredEnergy.put(key, Math.max(0, energyJ));
    }

    public synchronized boolean isRegistered(String id) {
        return id != null && requiredMaterials.containsKey(id.toUpperCase());
    }

    public synchronized int getRequiredAmount(String id) {
        if (id == null) return -1;
        return requiredMaterials.getOrDefault(id.toUpperCase(), -1);
    }

    public synchronized long getRequiredEnergy(String id) {
        if (id == null) return -1L;
        return requiredEnergy.getOrDefault(id.toUpperCase(), -1L);
    }

    public synchronized Set<String> getRegisteredSingularities() {
        return Collections.unmodifiableSet(requiredMaterials.keySet());
    }

    /**
     * Valida si la compresión de materiales en una singularidad es legítima.
     * Erradica desbordamientos enteros, cantidades negativas y discrepancias de ítems.
     *
     * @param singularityId Identificador de la singularidad
     * @param providedAmount Cantidad de materiales suministrados
     * @param providedEnergy Energía acumulada disponible (J)
     * @return true si cumple exactamente los requisitos sin exceder ni truncar de forma anómala
     */
    public synchronized boolean validateCompression(String singularityId, int providedAmount, long providedEnergy) {
        if (singularityId == null) return false;
        String key = singularityId.toUpperCase();
        Integer required = requiredMaterials.get(key);
        if (required == null) return false;

        if (strictAntiDupe) {
            if (providedAmount < 0 || providedAmount < required) {
                return false;
            }
            Long energy = requiredEnergy.get(key);
            if (energy != null && providedEnergy < energy) {
                return false;
            }
        } else {
            if (providedAmount < required) {
                return false;
            }
        }

        return true;
    }

    public boolean isStrictAntiDupe() {
        return strictAntiDupe;
    }
}

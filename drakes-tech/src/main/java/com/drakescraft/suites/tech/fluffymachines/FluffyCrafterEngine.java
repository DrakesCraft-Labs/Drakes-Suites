package com.drakescraft.suites.tech.fluffymachines;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Motor de sincronización y validación de snapshots para auto-crafteadores de FluffyMachines.
 */
public class FluffyCrafterEngine {

    private final Map<String, Integer> tickDelays = new ConcurrentHashMap<>();
    private final int configuredDelay;
    private final boolean snapshotValidation;

    public FluffyCrafterEngine(int configuredDelay, boolean snapshotValidation) {
        this.configuredDelay = Math.max(1, configuredDelay);
        this.snapshotValidation = snapshotValidation;
    }

    /**
     * Avanza el contador de delay para una máquina específica.
     * Retorna true si es el tick en el cual debe ejecutarse la receta.
     */
    public boolean shouldCraft(String machinePos) {
        if (machinePos == null) return false;

        int current = tickDelays.getOrDefault(machinePos, 0) + 1;
        if (current >= configuredDelay) {
            tickDelays.put(machinePos, 0);
            return true;
        } else {
            tickDelays.put(machinePos, current);
            return false;
        }
    }

    /**
     * Valida un snapshot atómico de inventario antes de permitir la deducción.
     */
    public boolean validateSnapshot(ItemStack[] inputs, ItemStack[] expectedRecipe) {
        if (!snapshotValidation) return true;
        if (inputs == null || expectedRecipe == null) return false;

        for (int i = 0; i < expectedRecipe.length; i++) {
            ItemStack required = expectedRecipe[i];
            if (required == null || required.getType().isAir()) continue;

            if (i >= inputs.length || inputs[i] == null || inputs[i].getType() != required.getType()) {
                return false;
            }
            if (inputs[i].getAmount() < required.getAmount()) {
                return false;
            }
        }
        return true;
    }

    public void reset(String machinePos) {
        tickDelays.remove(machinePos);
    }

    public void clear() {
        tickDelays.clear();
    }
}

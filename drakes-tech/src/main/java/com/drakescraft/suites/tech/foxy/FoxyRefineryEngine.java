package com.drakescraft.suites.tech.foxy;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Motor de refinamiento y procesamiento metalúrgico industrial FoxyMachines.
 * Gestiona conversiones atómicas de minerales, forjas de mejora y buffers de energía.
 */
public class FoxyRefineryEngine {

    private final boolean chunkGuard;
    private final int maxBuffer;
    private final Map<String, Integer> machineEnergyStorage = new ConcurrentHashMap<>();

    public FoxyRefineryEngine(boolean chunkGuard, int maxBuffer) {
        this.chunkGuard = chunkGuard;
        this.maxBuffer = Math.max(64, maxBuffer);
    }

    /**
     * Procesa un lingote o pepita en la refinería eléctrica dorada.
     * @param input Ítem a procesar
     * @param availableEnergy Energía disponible en Joules
     * @return Ítem refinado o null si no califica o no hay energía suficiente
     */
    public ItemStack processRefinery(ItemStack input, int availableEnergy) {
        if (input == null || input.getType().isAir() || availableEnergy < 15) {
            return null;
        }

        if (input.getType() == Material.RAW_GOLD || input.getType() == Material.GOLD_ORE) {
            ItemStack result = new ItemStack(Material.GOLD_INGOT, input.getAmount());
            SuiteItemPdcBridge.setCustomString(result, "drakestech", "foxy_refined", "true");
            return result;
        }

        if (input.getType() == Material.RAW_IRON || input.getType() == Material.IRON_ORE) {
            ItemStack result = new ItemStack(Material.IRON_INGOT, input.getAmount());
            SuiteItemPdcBridge.setCustomString(result, "drakestech", "foxy_refined", "true");
            return result;
        }

        return null;
    }

    public void storeEnergy(String machineLocationKey, int amount) {
        machineEnergyStorage.merge(machineLocationKey, amount, (curr, add) -> Math.min(maxBuffer, curr + add));
    }

    public int getEnergy(String machineLocationKey) {
        return machineEnergyStorage.getOrDefault(machineLocationKey, 0);
    }

    public boolean consumeEnergy(String machineLocationKey, int amount) {
        int current = getEnergy(machineLocationKey);
        if (current >= amount) {
            machineEnergyStorage.put(machineLocationKey, current - amount);
            return true;
        }
        return false;
    }

    public void clear() {
        machineEnergyStorage.clear();
    }

    public boolean isChunkGuard() {
        return chunkGuard;
    }

    public int getMaxBuffer() {
        return maxBuffer;
    }
}

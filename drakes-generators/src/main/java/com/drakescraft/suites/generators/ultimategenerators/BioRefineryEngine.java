package com.drakescraft.suites.generators.ultimategenerators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de destilación y síntesis de biocombustibles e hidrocarburos refinados.
 */
public final class BioRefineryEngine {

    private BioRefineryEngine() {}

    /**
     * Evalúa si una materia prima orgánica es apta para extracción de biomasa.
     */
    public static boolean isBiomassRawMaterial(Material mat) {
        if (mat == null) return false;
        String name = mat.name();
        return name.endsWith("_SAPLING") || name.endsWith("_LEAVES") ||
               name.endsWith("_SEEDS") || mat == Material.WHEAT || mat == Material.CARROT ||
               mat == Material.POTATO || mat == Material.SUGAR_CANE || mat == Material.KELP;
    }

    /**
     * Procesa la extracción de biomasa si se suministra suficiente materia orgánica y un cubo vacío.
     */
    public static ItemStack processBiomassExtraction(List<ItemStack> inputs) {
        int organicCount = 0;
        boolean hasBucket = false;

        for (ItemStack item : inputs) {
            if (item == null) continue;
            if (item.getType() == Material.BUCKET) {
                hasBucket = true;
            } else if (isBiomassRawMaterial(item.getType())) {
                organicCount += item.getAmount();
            }
        }

        if (hasBucket && organicCount >= 8) {
            return UltimateGeneratorsItemsRegistry.getItem("BIOMASS_BUCKET");
        }
        return null;
    }

    /**
     * Procesa la refinación de biomasa a biocombustible.
     */
    public static ItemStack processBioFuelRefining(ItemStack input) {
        if (input == null) return null;
        if ("BIOMASS_BUCKET".equalsIgnoreCase(com.drakescraft.suites.core.pdc.SuiteItemPdcBridge.getSlimefunId(input))) {
            return UltimateGeneratorsItemsRegistry.getItem("BIOFUEL_BUCKET");
        }
        return null;
    }

    /**
     * Procesa la refinación de hidrocarburos a diésel de alto octanaje.
     */
    public static ItemStack processDieselRefining(ItemStack input) {
        if (input == null) return null;
        if (input.getType() == Material.LAVA_BUCKET ||
            "OIL_BUCKET".equalsIgnoreCase(com.drakescraft.suites.core.pdc.SuiteItemPdcBridge.getSlimefunId(input))) {
            return UltimateGeneratorsItemsRegistry.getItem("DIESEL_BUCKET");
        }
        return null;
    }
}

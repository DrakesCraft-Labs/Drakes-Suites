package com.drakescraft.suites.tech.dynatech;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Motor de aceleración biológica para las Cámaras de Cultivo de DynaTech.
 */
public final class GrowthChamberEngine {

    public enum ChamberEnvironment {
        STANDARD, NETHER, END, OCEAN
    }

    private static final Map<Material, Material> CROP_MAPPINGS = new HashMap<>();

    static {
        // Estándar
        CROP_MAPPINGS.put(Material.WHEAT_SEEDS, Material.WHEAT);
        CROP_MAPPINGS.put(Material.CARROT, Material.CARROT);
        CROP_MAPPINGS.put(Material.POTATO, Material.POTATO);
        CROP_MAPPINGS.put(Material.BEETROOT_SEEDS, Material.BEETROOT);
        CROP_MAPPINGS.put(Material.PUMPKIN_SEEDS, Material.PUMPKIN);
        CROP_MAPPINGS.put(Material.MELON_SEEDS, Material.MELON_SLICE);
        CROP_MAPPINGS.put(Material.SUGAR_CANE, Material.SUGAR_CANE);
        CROP_MAPPINGS.put(Material.CACTUS, Material.CACTUS);

        // Nether
        CROP_MAPPINGS.put(Material.NETHER_WART, Material.NETHER_WART);
        CROP_MAPPINGS.put(Material.CRIMSON_FUNGUS, Material.CRIMSON_FUNGUS);
        CROP_MAPPINGS.put(Material.WARPED_FUNGUS, Material.WARPED_FUNGUS);

        // End
        CROP_MAPPINGS.put(Material.CHORUS_FLOWER, Material.CHORUS_FRUIT);

        // Océano
        CROP_MAPPINGS.put(Material.KELP, Material.KELP);
        CROP_MAPPINGS.put(Material.SEA_PICKLE, Material.SEA_PICKLE);
    }

    private GrowthChamberEngine() {}

    /**
     * Determina el multiplicador de producción de la cámara.
     */
    public static int getMultiplier(String chamberType) {
        if (chamberType == null) return 2;
        String upper = chamberType.toUpperCase();
        if (upper.contains("MK2")) {
            return 3;
        }
        return 2; // MK1 default
    }

    /**
     * Procesa un ciclo de crecimiento para una semilla o insumo botánico dado.
     */
    public static List<ItemStack> process(String chamberType, Material input) {
        if (input == null) return Collections.emptyList();

        Material outputMat = CROP_MAPPINGS.get(input);
        if (outputMat == null) return Collections.emptyList();

        int multiplier = getMultiplier(chamberType);
        List<ItemStack> result = new ArrayList<>();
        result.add(new ItemStack(outputMat, multiplier));

        // Reintegrar una semilla para ciclo continuo
        result.add(new ItemStack(input, 1));
        return result;
    }
}

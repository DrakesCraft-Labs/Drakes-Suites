package com.drakescraft.suites.bio.flowerpower;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

/**
 * Motor de aceleración y clonación vegetal con Semillas de Sobrecrecimiento (Overgrowth Seed).
 */
public class FlowerDuplicationEngine {

    private static final Set<Material> COMPATIBLE_FLOWERS = EnumSet.of(
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.PINK_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE,
            Material.SUNFLOWER,
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY
    );

    /**
     * Determina si el material especificado es una flor válida para multiplicación.
     */
    public static boolean isCompatibleFlower(@Nonnull Material material) {
        return COMPATIBLE_FLOWERS.contains(material);
    }

    /**
     * Calcula la cantidad de flores clonadas resultantes del sobrecrecimiento.
     * Retorna entre minYield y maxYield.
     */
    public static int calculateYield(int minYield, int maxYield) {
        int min = Math.max(1, minYield);
        int max = Math.max(min, maxYield);
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static Set<Material> getCompatibleFlowers() {
        return COMPATIBLE_FLOWERS;
    }
}

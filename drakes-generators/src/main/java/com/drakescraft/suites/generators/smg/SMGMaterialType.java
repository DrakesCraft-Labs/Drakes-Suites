package com.drakescraft.suites.generators.smg;

import org.bukkit.Material;

/**
 * Tipos de generadores de materiales en SMG y sus especificaciones canónicas.
 */
public enum SMGMaterialType {
    COBBLESTONE("Cobblestone", Material.COBBLESTONE, 4, Material.COBBLESTONE),
    STONE("Stone", Material.STONE, 8, Material.STONE),
    SMOOTH_STONE("Smooth Stone", Material.SMOOTH_STONE, 12, Material.SMOOTH_STONE),
    GRAVEL("Gravel", Material.ANDESITE, 6, Material.GRAVEL),
    SAND("Sand", Material.SANDSTONE, 8, Material.SAND),
    GLASS("Glass", Material.GLASS, 12, Material.GLASS),
    NETHERRACK("Netherrack", Material.NETHERRACK, 6, Material.NETHERRACK),
    SOUL_SAND("Soul Sand", Material.SOUL_SAND, 8, Material.SOUL_SAND),
    OBSIDIAN("Obsidian", Material.OBSIDIAN, 24, Material.OBSIDIAN);

    private final String displayName;
    private final Material blockIcon;
    private final int defaultRateTicks;
    private final Material producedMaterial;

    SMGMaterialType(String displayName, Material blockIcon, int defaultRateTicks, Material producedMaterial) {
        this.displayName = displayName;
        this.blockIcon = blockIcon;
        this.defaultRateTicks = defaultRateTicks;
        this.producedMaterial = producedMaterial;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getBlockIcon() {
        return blockIcon;
    }

    public int getDefaultRateTicks() {
        return defaultRateTicks;
    }

    public Material getProducedMaterial() {
        return producedMaterial;
    }

    public String getCanonicalId() {
        return "SMG_GENERATOR_" + name();
    }

    public String getBrokenId() {
        return "SMG_GENERATOR_" + name() + "_BROKEN";
    }
}

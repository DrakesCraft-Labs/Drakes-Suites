package com.drakescraft.suites.tech.fluffymachines;

import org.bukkit.Material;

/**
 * Catálogo de máquinas automatizadas de FluffyMachines para DrakesTech.
 */
public enum FluffyMachineType {
    AUTO_CRAFTING_TABLE("Auto Crafting Table", Material.CRAFTING_TABLE, 16),
    AUTO_ENHANCED_CRAFTING_TABLE("Auto Enhanced Crafting Table", Material.SMITHING_TABLE, 24),
    AUTO_ARMOR_FORGE("Auto Armor Forge", Material.ANVIL, 32),
    AUTO_MAGIC_WORKBENCH("Auto Magic Workbench", Material.BOOKSHELF, 24),
    AUTO_ANCIENT_ALTAR("Auto Ancient Altar", Material.ENCHANTING_TABLE, 48),
    AUTO_TABLE_SAW("Auto Table Saw", Material.STONECUTTER, 16),
    ELECTRIC_DUST_FABRICATOR("Electric Dust Fabricator", Material.CAULDRON, 40),
    ELECTRIC_DUST_RECYCLER("Electric Dust Recycler", Material.BLAST_FURNACE, 30),
    SMART_FACTORY("Smart Factory", Material.DISPENSER, 64),
    WATER_SPRINKLER("Water Sprinkler", Material.IRON_BARS, 10),
    BACKPACK_LOADER("Backpack Loader", Material.HOPPER, 12),
    BACKPACK_UNLOADER("Backpack Unloader", Material.DROPPER, 12);

    private final String displayName;
    private final Material material;
    private final int energyPerTickJ;

    FluffyMachineType(String displayName, Material material, int energyPerTickJ) {
        this.displayName = displayName;
        this.material = material;
        this.energyPerTickJ = energyPerTickJ;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getEnergyPerTickJ() {
        return energyPerTickJ;
    }

    public String getCanonicalId() {
        return name();
    }
}

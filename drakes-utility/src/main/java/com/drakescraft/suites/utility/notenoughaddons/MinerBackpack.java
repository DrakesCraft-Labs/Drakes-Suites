package com.drakescraft.suites.utility.notenoughaddons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MinerBackpack specification and material filter for DrakesUtility.
 */
public class MinerBackpack {

    public static final int DEFAULT_SIZE = 54;
    private static final List<Material> WHITELISTED_ORES = new ArrayList<>();

    static {
        Material[] ores = {
                Material.COAL, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON,
                Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.RAW_COPPER,
                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD, Material.GOLD_NUGGET,
                Material.REDSTONE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
                Material.LAPIS_LAZULI, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
                Material.DIAMOND, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
                Material.EMERALD, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
                Material.NETHER_QUARTZ_ORE, Material.QUARTZ, Material.NETHER_GOLD_ORE,
                Material.ANCIENT_DEBRIS, Material.NETHERITE_SCRAP
        };
        Collections.addAll(WHITELISTED_ORES, ores);
    }

    public static boolean isItemAllowed(@Nullable ItemStack item) {
        if (item == null) return false;
        return WHITELISTED_ORES.contains(item.getType());
    }

    @Nonnull
    public static List<Material> getWhitelistedOres() {
        return Collections.unmodifiableList(WHITELISTED_ORES);
    }

    public static int getDefaultSize() {
        return DEFAULT_SIZE;
    }
}

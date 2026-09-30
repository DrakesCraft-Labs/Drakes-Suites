package com.drakescraft.suites.utility.notenoughaddons;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * NotEnoughAddons item registry for DrakesUtility.
 * Preserves canonical Slimefun PDC IDs with 100% Universal English canon.
 */
public final class NEAItems {

    public static final String BUDGET_DUST_FABRICATOR = "BUDGET_DUST_FABRICATOR";
    public static final String FLYING_BUBBLE = "FLYING_BUBBLE";
    public static final String ANGEL_BLOCK = "ANGEL_BLOCK";
    public static final String MINER_BACKPACK = "MINER_BACKPACK";

    public static final String SHORTSWORD_COPPER = "SHORTSWORD_COPPER";
    public static final String SHORTSWORD_TIN = "SHORTSWORD_TIN";
    public static final String SHORTSWORD_IRON = "SHORTSWORD_IRON";
    public static final String SHORTSWORD_LEAD = "SHORTSWORD_LEAD";
    public static final String SHORTSWORD_SILVER = "SHORTSWORD_SILVER";
    public static final String SHORTSWORD_TUNGSTEN = "SHORTSWORD_TUNGSTEN";
    public static final String SHORTSWORD_GOLD = "SHORTSWORD_GOLD";
    public static final String SHORTSWORD_PLATINUM = "SHORTSWORD_PLATINUM";

    private static final Map<String, ItemStack> ITEMS = new LinkedHashMap<>();

    static {
        registerItem(BUDGET_DUST_FABRICATOR, Material.CRACKED_STONE_BRICKS,
                "<gradient:#ffaa00:#ff5500><bold>Budget Dust Fabricator</bold></gradient>",
                Arrays.asList(
                        "&7An all-in-one and cheap machine,",
                        "&7extracts dust directly from cobblestone or its variants.",
                        "",
                        "&8» &eMachine Tier: &aGood",
                        "&8» &eSpeed: &b1x",
                        "&8» &eEnergy Consumption: &615 J/s",
                        "&8» &eEnergy Capacity: &645 J",
                        "",
                        "&8» &fID: &e" + BUDGET_DUST_FABRICATOR
                ));

        registerItem(FLYING_BUBBLE, Material.CRYING_OBSIDIAN,
                "<gradient:#aa00ff:#00aaff><bold>Flying Bubble</bold></gradient>",
                Arrays.asList(
                        "&7Allows creative flight within a 25 block radius.",
                        "",
                        "&8» &eMachine Tier: &5End-Game",
                        "&8» &eEnergy Consumption: &6128 J/s",
                        "&8» &eEnergy Capacity: &61024 J",
                        "",
                        "&8» &fID: &e" + FLYING_BUBBLE
                ));

        registerItem(ANGEL_BLOCK, Material.FEATHER,
                "<gradient:#ffffff:#ffff55><bold>Angel Block</bold></gradient>",
                Arrays.asList(
                        "&7Places a temporary block beneath your feet.",
                        "&7Very useful while in mid-air or building bridges.",
                        "",
                        "&eRight-click to place block",
                        "",
                        "&8» &fID: &e" + ANGEL_BLOCK
                ));

        registerItem(MINER_BACKPACK, Material.CHEST,
                "<gradient:#ffaa00:#aa5500><bold>Miner's Backpack</bold></gradient>",
                Arrays.asList(
                        "&7Stores mined ores automatically on pickup.",
                        "&7Just keep it in your active inventory.",
                        "",
                        "&8» &eSize: &f54 Slots (Double Chest)",
                        "&eRight-click to open backpack",
                        "",
                        "&8» &fID: &e" + MINER_BACKPACK
                ));

        // Shortswords
        registerShortsword(SHORTSWORD_COPPER, Material.WOODEN_SWORD, "Copper Shortsword", 5, 0.04, 13, 4.0);
        registerShortsword(SHORTSWORD_TIN, Material.WOODEN_SWORD, "Tin Shortsword", 7, 0.04, 12, 4.5);
        registerShortsword(SHORTSWORD_IRON, Material.WOODEN_SWORD, "Iron Shortsword", 8, 0.04, 11, 4.5);
        registerShortsword(SHORTSWORD_LEAD, Material.WOODEN_SWORD, "Lead Shortsword", 9, 0.04, 11, 5.0);
        registerShortsword(SHORTSWORD_SILVER, Material.WOODEN_SWORD, "Silver Shortsword", 9, 0.04, 11, 5.0);
        registerShortsword(SHORTSWORD_TUNGSTEN, Material.WOODEN_SWORD, "Tungsten Shortsword", 10, 0.04, 10, 5.0);
        registerShortsword(SHORTSWORD_GOLD, Material.WOODEN_SWORD, "Gold Shortsword", 11, 0.04, 11, 5.0);
        registerShortsword(SHORTSWORD_PLATINUM, Material.WOODEN_SWORD, "Platinum Shortsword", 13, 0.04, 11, 5.5);
    }

    private static void registerShortsword(String sfId, Material mat, String name, double dmg, double cc, int useTime, double kb) {
        registerItem(sfId, mat,
                "<gradient:#ffffff:#aaaaaa><bold>" + name + "</bold></gradient>",
                Arrays.asList(
                        TerrariaUtils.getDMG(dmg),
                        TerrariaUtils.getCC(cc),
                        TerrariaUtils.useTimeConv(useTime),
                        TerrariaUtils.kbConv(kb),
                        "",
                        "&8» &fID: &e" + sfId
                ));
    }

    private static void registerItem(String sfId, Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, displayName);
            CrossVersionAdapter.setLore(meta, lore);
            SuiteItemPdcBridge.setSlimefunId(meta, sfId);
            item.setItemMeta(meta);
        }
        ITEMS.put(sfId, item);
    }

    public static ItemStack getItem(String id) {
        ItemStack item = ITEMS.get(id);
        return item != null ? item.clone() : null;
    }

    public static Map<String, ItemStack> getAllItems() {
        return Collections.unmodifiableMap(ITEMS);
    }

    private NEAItems() {
    }
}

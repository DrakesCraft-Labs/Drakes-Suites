package com.drakescraft.suites.combat.obsidian;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Obsidian Armor and ObsidianExpansion items registry.
 * Consolidates Obsidian-Armor and ObsidianExpansion addons into DrakesCombat.
 * Strict English naming with full PDC compatibility.
 */
public final class ObsidianItems {

    // Obsidian-Armor and ObsidianExpansion shared canonical IDs
    public static final String OBSIDIAN_ALLOY = "OBSIDIAN_ALLOY";
    public static final String OBSIDIAN_HELMET = "OBSIDIAN_HELMET";
    public static final String OBSIDIAN_CHESTPLATE = "OBSIDIAN_CHESTPLATE";
    public static final String OBSIDIAN_LEGGINGS = "OBSIDIAN_LEGGINGS";
    public static final String OBSIDIAN_BOOTS = "OBSIDIAN_BOOTS";

    // Expansion items (OMC prefix)
    public static final String OBSIDIAN_FORGE = "OMC_OBSIDIAN_FORGE";
    public static final String CONTAINMENT_PICK = "OMC_CONTAINMENT_PICK";
    public static final String NETHERITE_GEN = "OMC_NETHERITE_GEN";
    public static final String OBSIDIAN_REACTOR = "OMC_OBSIDIAN_REACTOR";
    public static final String OBSIDIAN_PLATE = "OMC_OBSIDIAN_PLATE";
    public static final String VOID_CORE = "OMC_VOID_CORE";
    public static final String ADVANCED_VOID_CORE = "OMC_ADVANCED_VOID_CORE";
    public static final String OBSIDIAN_GEAR = "OMC_OBSIDIAN_GEAR";
    public static final String DRAGON_SCALE = "OMC_DRAGON_SCALE";
    public static final String PHANTOM_SCALE = "OMC_PHANTOM_SCALE";
    public static final String ANGEL_GEM = "OMC_ANGEL_GEM";

    // Compressed Obsidian tiers
    public static final String SINGLE_COMPRESSED = "OMC_SINGLE_COMPRESSED_OBSIDIAN";
    public static final String DOUBLE_COMPRESSED = "OMC_DOUBLE_COMPRESSED_OBSIDIAN";
    public static final String TRIPLE_COMPRESSED = "OMC_TRIPLE_COMPRESSED_OBSIDIAN";
    public static final String QUADRUPLE_COMPRESSED = "OMC_QUADRUPLE_COMPRESSED_OBSIDIAN";
    public static final String QUINTUPLE_COMPRESSED = "OMC_QUINTUPLE_COMPRESSED_OBSIDIAN";

    private static final Map<String, ItemStack> ITEMS = new LinkedHashMap<>();

    static {
        // Obsidian Alloy
        registerItem(OBSIDIAN_ALLOY, Material.DIAMOND,
                "<gradient:#220033:#550088><bold>Obsidian Alloy</bold></gradient>",
                Arrays.asList(
                        "&7High-density composite of volcanic obsidian and diamond lattice.",
                        "&7Essential material for heavy reinforced combat plating.",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_ALLOY
                ));

        // Obsidian Armor pieces with enchants
        ItemStack helm = createArmorPiece(OBSIDIAN_HELMET, Material.DIAMOND_HELMET,
                "<gradient:#330044:#7700aa><bold>Obsidian Helmet</bold></gradient>",
                Arrays.asList(
                        "&7Forged with resilient obsidian alloy.",
                        "&8» &bBlast Absorption VI",
                        "&8» &6Fire Resistance VI",
                        "&8» &9Protection IV",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_HELMET
                ));
        helm.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
        helm.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 6);
        helm.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 6);
        helm.addUnsafeEnchantment(Enchantment.PROTECTION, 4);
        ITEMS.put(OBSIDIAN_HELMET, helm);

        ItemStack chest = createArmorPiece(OBSIDIAN_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
                "<gradient:#330044:#7700aa><bold>Obsidian Chestplate</bold></gradient>",
                Arrays.asList(
                        "&7Reinforced volcanic breastplate.",
                        "&8» &bBlast Absorption VI",
                        "&8» &6Fire Resistance VI",
                        "&8» &9Protection IV",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_CHESTPLATE
                ));
        chest.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
        chest.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 6);
        chest.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 6);
        chest.addUnsafeEnchantment(Enchantment.PROTECTION, 4);
        ITEMS.put(OBSIDIAN_CHESTPLATE, chest);

        ItemStack legs = createArmorPiece(OBSIDIAN_LEGGINGS, Material.DIAMOND_LEGGINGS,
                "<gradient:#330044:#7700aa><bold>Obsidian Leggings</bold></gradient>",
                Arrays.asList(
                        "&7Heavy volcanic greaves.",
                        "&8» &bBlast Absorption VI",
                        "&8» &6Fire Resistance VI",
                        "&8» &9Protection IV",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_LEGGINGS
                ));
        legs.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
        legs.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 6);
        legs.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 6);
        legs.addUnsafeEnchantment(Enchantment.PROTECTION, 4);
        ITEMS.put(OBSIDIAN_LEGGINGS, legs);

        ItemStack boots = createArmorPiece(OBSIDIAN_BOOTS, Material.DIAMOND_BOOTS,
                "<gradient:#330044:#7700aa><bold>Obsidian Boots</bold></gradient>",
                Arrays.asList(
                        "&7Volcanic shock-absorbing sabatons.",
                        "&8» &bBlast Absorption VI",
                        "&8» &6Fire Resistance VI",
                        "&8» &9Protection IV",
                        "&8» &aFeather Falling IV",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_BOOTS
                ));
        boots.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
        boots.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 6);
        boots.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 6);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION, 4);
        boots.addUnsafeEnchantment(Enchantment.FEATHER_FALLING, 4);
        ITEMS.put(OBSIDIAN_BOOTS, boots);

        // Obsidian Forge Table
        registerItem(OBSIDIAN_FORGE, Material.SMITHING_TABLE,
                "<gradient:#5500aa:#9900ff><bold>Obsidian Forge Table</bold></gradient>",
                Arrays.asList(
                        "&7Specialized crafting station for volcanic weaponry and machines.",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_FORGE
                ));

        // Containment Pick
        registerItem(CONTAINMENT_PICK, Material.NETHERITE_PICKAXE,
                "<gradient:#aa0000:#ff3333><bold>Spawner Containment Pickaxe</bold></gradient>",
                Arrays.asList(
                        "&7Reinforced seismic pickaxe capable of safely harvesting mob spawners.",
                        "",
                        "&8» &fID: &e" + CONTAINMENT_PICK
                ));

        // Netherite Converter
        registerItem(NETHERITE_GEN, Material.NETHERITE_BLOCK,
                "<gradient:#440000:#881111><bold>Netherite Synthesis Chamber</bold></gradient>",
                Arrays.asList(
                        "&7Converts compressed basalt and volcanic matter into netherite ingots.",
                        "&8» &eEnergy: &61,600 J/t",
                        "",
                        "&8» &fID: &e" + NETHERITE_GEN
                ));

        // Obsidian Reactor
        registerItem(OBSIDIAN_REACTOR, Material.OBSIDIAN,
                "<gradient:#330055:#8800ff><bold>Obsidian Fusion Reactor</bold></gradient>",
                Arrays.asList(
                        "&7High-temperature geothermal fission reactor.",
                        "&8» &eGenerates: &64,096 J/t",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_REACTOR
                ));

        // Plating & Components
        registerItem(OBSIDIAN_PLATE, Material.NETHERITE_INGOT,
                "&5Reinforced Obsidian Plate",
                Arrays.asList(
                        "&7Dense compressed volcanic sheet for heavy armor framing.",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_PLATE
                ));

        registerItem(VOID_CORE, Material.ENDER_EYE,
                "<gradient:#000044:#440088><bold>Void Core</bold></gradient>",
                Arrays.asList(
                        "&7Condensation of dimensional void energy.",
                        "",
                        "&8» &fID: &e" + VOID_CORE
                ));

        registerItem(ADVANCED_VOID_CORE, Material.HEART_OF_THE_SEA,
                "<gradient:#220055:#8800cc><bold>Advanced Void Core</bold></gradient>",
                Arrays.asList(
                        "&7Hyper-stabilized vacuum core for Tier 10 machinery.",
                        "",
                        "&8» &fID: &e" + ADVANCED_VOID_CORE
                ));

        registerItem(OBSIDIAN_GEAR, Material.IRON_BLOCK,
                "&7Obsidian Heavy Gear",
                Arrays.asList(
                        "&7Hardened gear mechanism resistant to extreme friction.",
                        "",
                        "&8» &fID: &e" + OBSIDIAN_GEAR
                ));

        registerItem(DRAGON_SCALE, Material.PHANTOM_MEMBRANE,
                "<gradient:#5500aa:#aa00ff><bold>Ender Dragon Scale</bold></gradient>",
                Arrays.asList(
                        "&7Shed scale from the Ender Dragon.",
                        "",
                        "&8» &fID: &e" + DRAGON_SCALE
                ));

        registerItem(PHANTOM_SCALE, Material.FEATHER,
                "<gradient:#00aaaa:#00ffff><bold>Phantom Membrane Scale</bold></gradient>",
                Arrays.asList(
                        "&7Lightweight nocturnal membrane.",
                        "",
                        "&8» &fID: &e" + PHANTOM_SCALE
                ));

        registerItem(ANGEL_GEM, Material.EMERALD,
                "<gradient:#ffff55:#ffffff><bold>Angel Flight Gem</bold></gradient>",
                Arrays.asList(
                        "&7Mystical relic that grants temporary creative flight.",
                        "",
                        "&8» &fID: &e" + ANGEL_GEM
                ));

        // Compressed Obsidian
        registerItem(SINGLE_COMPRESSED, Material.OBSIDIAN,
                "&8Single Compressed Obsidian &7(9x)",
                Arrays.asList("&7Dense block formed by 9 compressed obsidian blocks.", "", "&8» &fID: &e" + SINGLE_COMPRESSED));
        registerItem(DOUBLE_COMPRESSED, Material.OBSIDIAN,
                "&8Double Compressed Obsidian &7(81x)",
                Arrays.asList("&7Dense block formed by 81 compressed obsidian blocks.", "", "&8» &fID: &e" + DOUBLE_COMPRESSED));
        registerItem(TRIPLE_COMPRESSED, Material.OBSIDIAN,
                "&8Triple Compressed Obsidian &7(729x)",
                Arrays.asList("&7Dense block formed by 729 compressed obsidian blocks.", "", "&8» &fID: &e" + TRIPLE_COMPRESSED));
        registerItem(QUADRUPLE_COMPRESSED, Material.CRYING_OBSIDIAN,
                "<gradient:#440055:#8800aa><bold>Quadruple Compressed Obsidian</bold></gradient> &7(6,561x)",
                Arrays.asList("&7Volcanic crystal with immense structural mass.", "", "&8» &fID: &e" + QUADRUPLE_COMPRESSED));
        registerItem(QUINTUPLE_COMPRESSED, Material.RESPAWN_ANCHOR,
                "<gradient:#660088:#aa00ff><bold>Quintuple Compressed Obsidian</bold></gradient> &7(59,049x)",
                Arrays.asList("&7Near-singularity mass density block.", "", "&8» &fID: &e" + QUINTUPLE_COMPRESSED));
    }

    private static ItemStack createArmorPiece(String sfId, Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, name);
            CrossVersionAdapter.setLore(meta, lore);
            SuiteItemPdcBridge.setSlimefunId(meta, sfId);
            item.setItemMeta(meta);
        }
        return item;
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

    private ObsidianItems() {}
}

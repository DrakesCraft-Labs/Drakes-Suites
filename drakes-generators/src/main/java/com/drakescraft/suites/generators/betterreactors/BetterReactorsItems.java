package com.drakescraft.suites.generators.betterreactors;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Canonical Slimefun items for BetterReactors in DrakesGenerators.
 * Preserves Slimefun item IDs for 100% production parity.
 */
public final class BetterReactorsItems {

    public static final String REACTOR_CORE = "REACTOR_CORE";
    public static final String HEAT_SENSOR = "HEAT_SENSOR";
    public static final String REACTOR_STOP = "REACTOR_STOP";
    public static final String SUPER_FREEZER = "SUPER_FREEZER";
    public static final String HEATED_COOLANT = "HEATED_COOLANT";
    public static final String LEAD_BLOCK = "LEAD_BLOCK";
    public static final String LEAD_GLASS = "LEAD_GLASS";
    public static final String BORIUM_ROD = "BORIUM_ROD";
    public static final String BORIUM = "BORIUM";
    public static final String GRAPHITE = "GRAPHITE";
    public static final String REACTOR_INPUT = "REACTOR_INPUT";
    public static final String REACTOR_OUTPUT = "REACTOR_OUTPUT";
    public static final String REACTOR_HATCH = "REACTOR_HATCH";

    private static final Map<String, ItemStack> ITEMS = new LinkedHashMap<>();

    static {
        registerItem(REACTOR_CORE, Material.MAGENTA_GLAZED_TERRACOTTA,
                "<gradient:#ff0055:#aa00ff><bold>Nuclear Reactor Core</bold></gradient>",
                Arrays.asList(
                        "&7End-Game Fission Generator Core",
                        "&8» &ePower Output: &65,000 J/s per Uranium",
                        "&8» &bRequires Coolant System",
                        "",
                        "&8» &fID: &e" + REACTOR_CORE
                ));

        registerItem(HEAT_SENSOR, Material.POLISHED_DEEPSLATE,
                "<gradient:#ffaa00:#ff5500><bold>Reactor Heat Sensor</bold></gradient>",
                Arrays.asList(
                        "&7Detects thermal levels within the reactor core.",
                        "&7Emits redstone pulse when temperature exceeds safety limits.",
                        "",
                        "&8» &fID: &e" + HEAT_SENSOR
                ));

        registerItem(REACTOR_STOP, Material.REDSTONE_LAMP,
                "<gradient:#ff0000:#aa0000><bold>Reactor Emergency Stop</bold></gradient>",
                Arrays.asList(
                        "&7Immediately halts reactor fission cycle when powered.",
                        "&eConsumes Graphite control rods during scram.",
                        "",
                        "&8» &fID: &e" + REACTOR_STOP
                ));

        registerItem(SUPER_FREEZER, Material.QUARTZ_BLOCK,
                "<gradient:#00ffff:#0088ff><bold>Super Cryo-Freezer</bold></gradient>",
                Arrays.asList(
                        "&7Rapid cryogenic coolant converter.",
                        "&7Instantly condenses water into cryogenic coolant.",
                        "&8» &ePower Consumption: &650 J/s",
                        "",
                        "&8» &fID: &e" + SUPER_FREEZER
                ));

        registerItem(HEATED_COOLANT, Material.BLUE_DYE,
                "<gradient:#ff5500:#00aaff><bold>Heated Coolant Cell</bold></gradient>",
                Arrays.asList(
                        "&7Spent coolant containing absorbed fission heat.",
                        "&7Can be recycled or cooled in heat exchangers.",
                        "",
                        "&8» &fID: &e" + HEATED_COOLANT
                ));

        registerItem(LEAD_BLOCK, Material.IRON_BLOCK,
                "&fLead Plating Block",
                Arrays.asList(
                        "&7Dense radiation-shielding block.",
                        "&7Structural element for reactor chamber.",
                        "",
                        "&8» &fID: &e" + LEAD_BLOCK
                ));

        registerItem(LEAD_GLASS, Material.GRAY_STAINED_GLASS,
                "&7Heavy Lead Glass",
                Arrays.asList(
                        "&7Reinforced glass containing lead isotopes.",
                        "&7Allows reactor observation without radiation leak.",
                        "",
                        "&8» &fID: &e" + LEAD_GLASS
                ));

        registerItem(BORIUM_ROD, Material.ANCIENT_DEBRIS,
                "<gradient:#ffaa00:#ffff55><bold>Borium Control Rod</bold></gradient>",
                Arrays.asList(
                        "&7Moderator rod made of Borium alloy.",
                        "&7Controls neutron absorption during reaction.",
                        "",
                        "&8» &fID: &e" + BORIUM_ROD
                ));

        registerItem(BORIUM, Material.GUNPOWDER,
                "&6Borium Isotope",
                Arrays.asList(
                        "&7Raw enriched boron isotope.",
                        "&7Used to manufacture reactor control components.",
                        "",
                        "&8» &fID: &e" + BORIUM
                ));

        registerItem(GRAPHITE, Material.CHARCOAL,
                "&8Graphite Moderator",
                Arrays.asList(
                        "&7High-purity carbon moderator block.",
                        "&7Used in emergency scram procedures.",
                        "",
                        "&8» &fID: &e" + GRAPHITE
                ));

        registerItem(REACTOR_INPUT, Material.LIGHT_BLUE_WOOL,
                "<gradient:#00aaff:#ffffff><bold>Reactor Coolant & Fuel Input</bold></gradient>",
                Arrays.asList(
                        "&7Automated intake port for coolant cells and uranium rods.",
                        "",
                        "&8» &fID: &e" + REACTOR_INPUT
                ));

        registerItem(REACTOR_OUTPUT, Material.RED_WOOL,
                "<gradient:#ff0000:#ff8800><bold>Reactor Byproduct Output</bold></gradient>",
                Arrays.asList(
                        "&7Automated extraction port for spent heated coolant cells.",
                        "",
                        "&8» &fID: &e" + REACTOR_OUTPUT
                ));

        registerItem(REACTOR_HATCH, Material.IRON_DOOR,
                "<gradient:#00ff88:#00aa55><bold>Reactor Maintenance Hatch</bold></gradient>",
                Arrays.asList(
                        "&7Reinforced airtight maintenance hatch.",
                        "",
                        "&8» &fID: &e" + REACTOR_HATCH
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

    private BetterReactorsItems() {}
}

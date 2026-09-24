package com.drakescraft.suites.utility.extratools;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Catálogo canónico de ítems y maquinaria eléctrica de ExtraTools.
 */
public final class ExtraToolsItemsRegistry {

    private static final Map<String, ItemStack> ITEMS = new HashMap<>();

    static {
        // Herramientas
        register("HAMMER", createGlowingItem(Material.IRON_PICKAXE, "HAMMER",
                ChatColor.RED + "Martillo Pulverizador",
                "Pulveriza bloques y extrae polvos minerales al minar."));

        // Máquinas Eléctricas
        register("GOLD_TRANSMUTER", createItem(Material.YELLOW_TERRACOTTA, "GOLD_TRANSMUTER",
                ChatColor.GOLD + "" + ChatColor.BOLD + "Transmutador de Oro",
                "§7Máquina de alta energía para transmutar lingotes nobles en oro.",
                "§8⇨ §7Capacidad: 256 J",
                "§8⇨ §7Consumo: 18 J/s"));

        register("ELECTRIC_COMPOSTER", createItem(Material.MAGENTA_TERRACOTTA, "ELECTRIC_COMPOSTER",
                ChatColor.RED + "Compostador Eléctrico (MK1)",
                "§7Convierte residuos vegetales y trigo en tierra fértil a velocidad 1x.",
                "§8⇨ §7Capacidad: 256 J",
                "§8⇨ §7Consumo: 18 J/s"));

        register("ELECTRIC_COMPOSTER_2", createGlowingItem(Material.MAGENTA_TERRACOTTA, "ELECTRIC_COMPOSTER_2",
                ChatColor.RED + "" + ChatColor.BOLD + "Compostador Eléctrico (MK2)",
                "§7Acelera el compostaje a velocidad 4x.",
                "§8⇨ §7Capacidad: 256 J",
                "§8⇨ §7Consumo: 50 J/s"));

        register("COBBLESTONE_GENERATOR", createGlowingItem(Material.POLISHED_ANDESITE, "COBBLESTONE_GENERATOR",
                ChatColor.RED + "" + ChatColor.BOLD + "Generador de Adoquín Eléctrico",
                "§7Genera adoquín de forma continua sin requerir agua ni lava estática.",
                "§8⇨ §7Capacidad: 512 J",
                "§8⇨ §7Consumo: 36 J/s"));

        register("VAPORIZER", createItem(Material.RED_STAINED_GLASS, "VAPORIZER",
                ChatColor.RED + "Vaporizador de Fluidos",
                "§7Evapora cubos de agua para condensar sales y sedimentos minerales.",
                "§8⇨ §7Capacidad: 256 J",
                "§8⇨ §7Consumo: 32 J/s"));

        register("CONCRETE_FACTORY", createItem(Material.BLACK_CONCRETE, "CONCRETE_FACTORY",
                ChatColor.DARK_RED + "" + ChatColor.BOLD + "Fábrica de Concreto",
                "§7Hidrata polvos de concreto de los 16 colores instantáneamente.",
                "§8⇨ §7Capacidad: 256 J",
                "§8⇨ §7Consumo: 16 J/s"));

        register("PULVERIZER", createItem(Material.ORANGE_TERRACOTTA, "PULVERIZER",
                ChatColor.GOLD + "Pulverizador Industrial",
                "§7Tritura adoquín en grava y grava en arena de forma automatizada.",
                "§8⇨ §7Capacidad: 256 J",
                "§8⇨ §7Consumo: 18 J/s"));
    }

    private static void register(String id, ItemStack item) {
        ITEMS.put(id, item);
    }

    @Nullable
    public static ItemStack getItem(String id) {
        ItemStack item = ITEMS.get(id);
        return item != null ? item.clone() : null;
    }

    public static Map<String, ItemStack> getAllItems() {
        return Collections.unmodifiableMap(ITEMS);
    }

    private static ItemStack createItem(Material material, String slimefunId, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            if (lore.length > 0) {
                List<String> loreList = new ArrayList<>();
                for (String line : lore) {
                    loreList.add(line);
                }
                meta.setLore(loreList);
            }
            SuiteItemPdcBridge.setSlimefunId(meta, slimefunId);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createGlowingItem(Material material, String slimefunId, String displayName, String... lore) {
        ItemStack item = createItem(material, slimefunId, displayName, lore);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ExtraToolsItemsRegistry() {}
}

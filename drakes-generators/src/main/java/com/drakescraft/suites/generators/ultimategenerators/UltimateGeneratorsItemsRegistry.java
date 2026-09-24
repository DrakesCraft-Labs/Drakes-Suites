package com.drakescraft.suites.generators.ultimategenerators;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Catálogo maestro de generadores pesados, refinerías de biomasa e hidrocarburos de UltimateGenerators2.
 */
public final class UltimateGeneratorsItemsRegistry {

    private static final Map<String, ItemStack> ITEMS = new HashMap<>();

    static {
        // Generadores Autónomos
        register("ENDLESS_GENERATOR", createGlowingItem(Material.RESPAWN_ANCHOR, "ENDLESS_GENERATOR",
                ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Generador Infinito",
                "Produce un flujo inagotable de energía del vacío.",
                "Tasa constante: 512 J/s sin combustible."));

        register("DIESEL_GENERATOR", createItem(Material.BLAST_FURNACE, "DIESEL_GENERATOR",
                ChatColor.GOLD + "" + ChatColor.BOLD + "Generador Diésel",
                "Motor de combustión interna de alta cilindrada.",
                "Consume Diésel Refinado a 128 J/s."));

        register("BIOFUEL_GENERATOR", createItem(Material.YELLOW_STAINED_GLASS, "BIOFUEL_GENERATOR",
                ChatColor.YELLOW + "" + ChatColor.BOLD + "Generador de Biocombustible",
                "Generador ecológico de combustión limpia.",
                "Consume Biocombustible a 64 J/s."));

        register("DRAGON_BREATH_GENERATOR", createItem(Material.MAGENTA_STAINED_GLASS, "DRAGON_BREATH_GENERATOR",
                ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Generador de Aliento de Dragón",
                "Canaliza el plasma dracónico en energía pura.",
                "Rendimiento extremo: 256 J/s."));

        register("REACTION_GENERATOR", createItem(Material.GREEN_TERRACOTTA, "REACTION_GENERATOR",
                ChatColor.DARK_GREEN + "Generador de Reacción Química",
                "Fusión de reactivos orgánicos y minerales a 96 J/s."));

        // Red Modular de Cristales del End
        register("ENDER_CRYSTAL_GENERATOR", createItem(Material.PURPLE_STAINED_GLASS, "ENDER_CRYSTAL_GENERATOR",
                ChatColor.DARK_PURPLE + "Generador de Cristal del End",
                "Aprovecha el rayo resonante de un Cristal del End estabilizado."));

        register("ENDER_CRYSTAL_GENERATOR_BASE", createItem(Material.END_STONE_BRICKS, "ENDER_CRYSTAL_GENERATOR_BASE",
                ChatColor.GRAY + "Base del Generador de Cristal",
                "Pedestal estructural de absorción energética."));

        register("ENDER_CRYSTAL_GENERATOR_STABILIZER", createItem(Material.OBSIDIAN, "ENDER_CRYSTAL_GENERATOR_STABILIZER",
                ChatColor.BLACK + "Estabilizador de Cristal del End",
                "Previene sobrecargas destructivas durante la extracción."));

        // Refinerías Industriales
        register("DIESEL_REFINERY", createItem(Material.PISTON, "DIESEL_REFINERY",
                ChatColor.GOLD + "Refinería de Diésel",
                "Fracciona petróleo e hidrocarburos pesados en combustible de alto octanaje."));

        register("BIOFUEL_REFINERY", createItem(Material.FURNACE, "BIOFUEL_REFINERY",
                ChatColor.YELLOW + "Refinería de Biocombustible",
                "Sintetiza biomasa destilada en bioetanol enriquecido."));

        register("BIOMASS_EXTRACTION_MACHINE", createItem(Material.LIME_STAINED_GLASS, "BIOMASS_EXTRACTION_MACHINE",
                ChatColor.GREEN + "Extractor de Biomasa",
                "Tritura follaje, brotes y materia orgánica en pulpa densa."));

        register("HEAVY_WATER_REFINING_MACHINE", createItem(Material.CYAN_STAINED_GLASS, "HEAVY_WATER_REFINING_MACHINE",
                ChatColor.DARK_AQUA + "Refinador de Agua Pesada",
                "Centrifuga deuterio a partir de corrientes acuíferas."));

        // Combustibles Líquidos
        register("DIESEL_BUCKET", createItem(Material.LAVA_BUCKET, "DIESEL_BUCKET",
                ChatColor.GOLD + "Cubo de Diésel Refinado", "Combustible de alta densidad energética."));

        register("BIOFUEL_BUCKET", createItem(Material.MILK_BUCKET, "BIOFUEL_BUCKET",
                ChatColor.YELLOW + "Cubo de Biocombustible", "Combustible limpio derivado de biomasa vegetal."));

        register("BIOMASS_BUCKET", createItem(Material.WATER_BUCKET, "BIOMASS_BUCKET",
                ChatColor.GREEN + "Cubo de Biomasa Licuada", "Solución orgánica precursora de biocombustible."));

        register("HEAVY_WATER_BUCKET", createItem(Material.WATER_BUCKET, "HEAVY_WATER_BUCKET",
                ChatColor.AQUA + "Cubo de Agua Pesada (Deuterio)", "Refrigerante y catalizador nuclear enriquecido."));

        // Capacitores
        register("UG_CAPACITOR_1", createItem(Material.REDSTONE_BLOCK, "UG_CAPACITOR_1",
                ChatColor.RED + "Capacitor UG Nivel I", "Capacidad: 10,000 J."));
        register("UG_CAPACITOR_2", createItem(Material.IRON_BLOCK, "UG_CAPACITOR_2",
                ChatColor.RED + "Capacitor UG Nivel II", "Capacidad: 50,000 J."));
        register("UG_CAPACITOR_3", createItem(Material.DIAMOND_BLOCK, "UG_CAPACITOR_3",
                ChatColor.RED + "Capacitor UG Nivel III", "Capacidad: 250,000 J."));
    }

    private UltimateGeneratorsItemsRegistry() {}

    private static void register(String sfId, ItemStack item) {
        ITEMS.put(sfId.toUpperCase(), item);
    }

    private static ItemStack createItem(Material mat, String sfId, String name, String... loreLines) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            for (String line : loreLines) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            lore.add("");
            lore.add(ChatColor.DARK_GRAY + "✦ StarSuites Generators - UG2 ✦");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, sfId);
        return item;
    }

    private static ItemStack createGlowingItem(Material mat, String sfId, String name, String... loreLines) {
        ItemStack item = createItem(mat, sfId, name, loreLines);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        item.addUnsafeEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);
        return item;
    }

    public static ItemStack getItem(String sfId) {
        if (sfId == null) return null;
        ItemStack item = ITEMS.get(sfId.toUpperCase());
        return item != null ? item.clone() : null;
    }

    public static Map<String, ItemStack> getAllItems() {
        return Collections.unmodifiableMap(ITEMS);
    }
}

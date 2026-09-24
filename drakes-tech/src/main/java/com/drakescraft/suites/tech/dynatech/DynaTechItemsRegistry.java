package com.drakescraft.suites.tech.dynatech;

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
 * Catálogo maestro de ítems, máquinas y componentes tecnológicos de DynaTech.
 */
public final class DynaTechItemsRegistry {

    private static final Map<String, ItemStack> ITEMS = new HashMap<>();

    static {
        // Herramientas y Misceláneos
        register("DYNATECH_ANGEL_GEM", createGlowingItem(Material.DIAMOND, "DYNATECH_ANGEL_GEM",
                ChatColor.AQUA + "" + ChatColor.BOLD + "Gema de los Ángeles",
                "Otorga la bendición del vuelo perpetuo.",
                "Consume energía dinámica por segundo.",
                "&eClic derecho &7para activar o desactivar vuelo."));

        register("DYNATECH_TESSERACT_BINDER", createItem(Material.COMPASS, "DYNATECH_TESSERACT_BINDER",
                ChatColor.GOLD + "" + ChatColor.BOLD + "Vinculador de Teseractos",
                "Permite sintonizar la frecuencia de dos teseractos.",
                "&eClic derecho &7para registrar la ubicación.",
                "&eShift + Clic &7para vincular el destino."));

        register("DYNATECH_TESSERACTING_OBJECT", createGlowingItem(Material.MUSHROOM_STEM, "DYNATECH_TESSERACTING_OBJECT",
                ChatColor.LIGHT_PURPLE + "Objeto Teseráctico",
                "Vibra y distorsiona el espacio-tiempo entre dimensiones."));

        // Máquinas de Red y Teseractos
        register("DYNATECH_TESSERACT", createItem(Material.ENDER_CHEST, "DYNATECH_TESSERACT",
                ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Teseracto Hiperdimensional",
                "Transfiere ítems, fluidos y energía sin límites de distancia.",
                "Capacidad cuántica interdimensional."));

        register("DYNATECH_WIRELESS_ENERGY_BANK", createItem(Material.RESPAWN_ANCHOR, "DYNATECH_WIRELESS_ENERGY_BANK",
                ChatColor.RED + "Banco de Energía Inalámbrico",
                "Acumula y retransmite Joules a través de la red local."));

        register("DYNATECH_WIRELESS_ENERGY_POINT", createItem(Material.REDSTONE_LAMP, "DYNATECH_WIRELESS_ENERGY_POINT",
                ChatColor.RED + "Punto de Carga Inalámbrico",
                "Abastece de electricidad a los nodos cercanos."));

        register("DYNATECH_WIRELESS_ITEM_INPUT", createItem(Material.HOPPER, "DYNATECH_WIRELESS_ITEM_INPUT",
                ChatColor.BLUE + "Entrada de Ítems Inalámbrica",
                "Absorbe cargamentos hacia la red virtual."));

        register("DYNATECH_WIRELESS_ITEM_OUTPUT", createItem(Material.DISPENSER, "DYNATECH_WIRELESS_ITEM_OUTPUT",
                ChatColor.BLUE + "Salida de Ítems Inalámbrica",
                "Descarga recursos desde la red cuántica."));

        // Cámaras de Cultivo (Growth Chambers)
        register("DYNATECH_GROWTH_CHAMBER", createItem(Material.LIME_STAINED_GLASS, "DYNATECH_GROWTH_CHAMBER",
                ChatColor.GREEN + "Cámara de Cultivo Hidropónica MK1",
                "Acelera x2 la reproducción de flora y cultivos terrestres."));

        register("DYNATECH_GROWTH_CHAMBER_MK2", createItem(Material.LIME_CONCRETE, "DYNATECH_GROWTH_CHAMBER_MK2",
                ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cámara de Cultivo Cuántica MK2",
                "Acelera x3 el rendimiento con estimulación fotónica avanzada."));

        register("DYNATECH_GROWTH_CHAMBER_NETHER", createItem(Material.RED_STAINED_GLASS, "DYNATECH_GROWTH_CHAMBER_NETHER",
                ChatColor.DARK_RED + "Cámara de Cultivo Infernal",
                "Optimizado para verrugas del Nether y hongos carmesí."));

        register("DYNATECH_GROWTH_CHAMBER_END", createItem(Material.PURPLE_STAINED_GLASS, "DYNATECH_GROWTH_CHAMBER_END",
                ChatColor.DARK_PURPLE + "Cámara de Cultivo del Vacío",
                "Ambiente controlado para la propagación de flores del Chorus."));

        register("DYNATECH_GROWTH_CHAMBER_OCEAN", createItem(Material.CYAN_STAINED_GLASS, "DYNATECH_GROWTH_CHAMBER_OCEAN",
                ChatColor.DARK_AQUA + "Cámara de Cultivo Marina",
                "Presión hiperbárica para kelp, pepinos de mar y corales."));

        // Generadores Ecológicos
        register("DYNATECH_WIND_MILL", createItem(Material.WHITE_GLAZED_TERRACOTTA, "DYNATECH_WIND_MILL",
                ChatColor.WHITE + "Molino Eólico",
                "Aprovecha las corrientes de aire a gran altitud para producir energía."));

        register("DYNATECH_WATER_MILL", createItem(Material.PRISMARINE_WALL, "DYNATECH_WATER_MILL",
                ChatColor.AQUA + "Turbina Hidráulica",
                "Convierte el flujo laminar de corrientes de agua en electricidad."));

        register("DYNATECH_CULINARY_GENERATOR", createItem(Material.SMOKER, "DYNATECH_CULINARY_GENERATOR",
                ChatColor.GOLD + "Generador Culinario",
                "Incinera alimentos calóricos para producir Joules de alta eficiencia."));

        register("DYNATECH_STARDUST_REACTOR", createItem(Material.BEACON, "DYNATECH_STARDUST_REACTOR",
                ChatColor.YELLOW + "" + ChatColor.BOLD + "Reactor de Polvo Estelar",
                "Fusión de plasma cósmico con rendimiento energético masivo."));

        // Núcleos Mecánicos
        register("DYNATECH_WOOD_MACHINE_CORE", createItem(Material.MANGROVE_WOOD, "DYNATECH_WOOD_MACHINE_CORE",
                ChatColor.WHITE + "Núcleo de Madera", "Componente mecánico estructural básico."));
        register("DYNATECH_STONE_MACHINE_CORE", createItem(Material.SMOOTH_STONE, "DYNATECH_STONE_MACHINE_CORE",
                ChatColor.WHITE + "Núcleo de Piedra", "Chasis resistente de compresión mineral."));
        register("DYNATECH_IRON_MACHINE_CORE", createItem(Material.GRAY_CONCRETE, "DYNATECH_IRON_MACHINE_CORE",
                ChatColor.WHITE + "Núcleo de Hierro", "Armazón industrial de precisión."));
        register("DYNATECH_DIAMOND_MACHINE_CORE", createItem(Material.LIGHT_BLUE_CONCRETE, "DYNATECH_DIAMOND_MACHINE_CORE",
                ChatColor.AQUA + "Núcleo de Diamante", "Estructura de diamante reforzado para alto estrés."));
        register("DYNATECH_ANCIENT_MACHINE_CORE", createGlowingItem(Material.LAPIS_BLOCK, "DYNATECH_ANCIENT_MACHINE_CORE",
                ChatColor.GOLD + "Núcleo Arcaico", "Tecnología perdida potenciada con resonancia mística."));
    }

    private DynaTechItemsRegistry() {}

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
            lore.add(ChatColor.DARK_GRAY + "✦ StarSuites Tech - DynaTech ✦");
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

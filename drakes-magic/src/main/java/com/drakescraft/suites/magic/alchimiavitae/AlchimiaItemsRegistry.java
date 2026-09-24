package com.drakescraft.suites.magic.alchimiavitae;

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
 * Catálogo canónico de ítems y materiales místicos de AlchimiaVitae.
 */
public final class AlchimiaItemsRegistry {

    private static final Map<String, ItemStack> ITEMS = new HashMap<>();

    static {
        register("AV_SOUL_COLLECTOR", SoulHarvestEngine.createSoulCollector());
        register("AV_CONDENSED_SOUL", SoulHarvestEngine.createCondensedSoul(1));

        register("AV_GOOD_MAGIC_PLANT", createGlowingItem(Material.OAK_SAPLING, "AV_GOOD_MAGIC_PLANT",
                ChatColor.GREEN + "Planta de Magia Luminosa", "Irradia un fulgor empíreo celestial."));
        register("AV_EVIL_MAGIC_PLANT", createGlowingItem(Material.OAK_SAPLING, "AV_EVIL_MAGIC_PLANT",
                ChatColor.RED + "Planta de Magia Oscura", "Absorbe la luz ambiental para nutrirse de sombras."));

        register("AV_EXP_CRYSTAL", createGlowingItem(Material.EMERALD, "AV_EXP_CRYSTAL",
                ChatColor.AQUA + "Cristal de Experiencia", "Experiencia condensada en estado sólido y estable."));
        register("AV_GOOD_ESSENCE", createItem(Material.SUGAR, "AV_GOOD_ESSENCE",
                ChatColor.AQUA + "Esencia Luminosa", "Pulsa con luminiscencia sagrada."));
        register("AV_EVIL_ESSENCE", createItem(Material.GUNPOWDER, "AV_EVIL_ESSENCE",
                ChatColor.DARK_RED + "Esencia Oscura", "Absorbe la luz del entorno como una esponja."));

        register("AV_ILLUMIUM", createGlowingItem(Material.IRON_INGOT, "AV_ILLUMIUM",
                ChatColor.GREEN + "Lingote de Illumium", "Emite calor y luminosidad pura al tacto."));
        register("AV_DARKSTEEL", createGlowingItem(Material.NETHERITE_INGOT, "AV_DARKSTEEL",
                ChatColor.DARK_GRAY + "Lingote de Acero Oscuro", "Radiante con densas tinieblas forjadas."));

        register("AV_MOLTEN_MYSTERY_METAL", createItem(Material.LAVA_BUCKET, "AV_MOLTEN_MYSTERY_METAL",
                ChatColor.GOLD + "Metal Misterioso Fundido", "Conglomerado incandescente de múltiples aleaciones."));
        register("AV_MYSTERY_METAL", createItem(Material.IRON_INGOT, "AV_MYSTERY_METAL",
                ChatColor.GOLD + "Lingote de Metal Misterioso", "Contiene trazas de metales nobles desconocidos."));

        register("AV_DIVINE_ALTAR", createItem(Material.ENCHANTING_TABLE, "AV_DIVINE_ALTAR",
                ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Altar Divino", "Aparato sagrado para transmutaciones milenarias."));
        register("AV_ALTAR_OF_INFUSION", createItem(Material.LODESTONE, "AV_ALTAR_OF_INFUSION",
                ChatColor.RED + "" + ChatColor.BOLD + "Altar de Infusión", "Canaliza poderes eternos en armas y armaduras."));
        register("AV_ORNATE_CAULDRON", createItem(Material.CAULDRON, "AV_ORNATE_CAULDRON",
                ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Caldero Cósmico", "Permite elaborar brebajes y pócimas avanzadas."));

        register("AV_PLANT_INFUSION_CHAMBER", createItem(Material.LIME_STAINED_GLASS, "AV_PLANT_INFUSION_CHAMBER",
                ChatColor.GREEN + "Cámara de Infusión Botánica", "Imbuye flora con energía de luz u oscuridad."));
        register("AV_EXP_CRYSTALLIZER", createItem(Material.CYAN_STAINED_GLASS, "AV_EXP_CRYSTALLIZER",
                ChatColor.AQUA + "Cristalizador de Experiencia", "Sintetiza esferas de experiencia en gemas sólidas."));
    }

    private AlchimiaItemsRegistry() {}

    private static void register(String sfId, ItemStack item) {
        ITEMS.put(sfId.toUpperCase(), item);
    }

    private static ItemStack createItem(Material mat, String sfId, String name, String loreDesc) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + loreDesc);
            lore.add("");
            lore.add(ChatColor.DARK_GRAY + "✦ StarSuites Magic ✦");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, sfId);
        return item;
    }

    private static ItemStack createGlowingItem(Material mat, String sfId, String name, String loreDesc) {
        ItemStack item = createItem(mat, sfId, name, loreDesc);
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

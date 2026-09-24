package com.drakescraft.suites.bio.slimybees;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utilidades para la creación, serialización y extracción de genomas en ItemStacks de abejas.
 */
public final class BeeItemHelper {

    public static final NamespacedKey KEY_BEE_GENOME = new NamespacedKey("drakes", "bee_genome");
    public static final NamespacedKey KEY_LEGACY_BEE_TYPE = new NamespacedKey("slimybees", "bee_type");

    private BeeItemHelper() {}

    /**
     * Crea un ítem de Princesa con el genoma codificado en PDC.
     */
    public static ItemStack createPrincess(BeeGenome genome) {
        BeeSpecies species = genome.getActiveSpecies();
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String color = species.getColorCode();
            meta.setDisplayName(color + "Abeja Reina " + species.getDisplayName());

            List<String> lore = buildBeeLore(genome, "Princesa / Reina");
            meta.setLore(lore);

            applyGenomePdc(meta, genome, species.getPrincessSfId());
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Crea un ítem de Zángano con el genoma codificado en PDC.
     */
    public static ItemStack createDrone(BeeGenome genome) {
        BeeSpecies species = genome.getActiveSpecies();
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String color = species.getColorCode();
            meta.setDisplayName(color + "Zángano " + species.getDisplayName());

            List<String> lore = buildBeeLore(genome, "Zángano");
            meta.setLore(lore);

            applyGenomePdc(meta, genome, species.getDroneSfId());
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Extrae el genoma de un ItemStack de abeja.
     * Soporta clave nativa de Drakes, clave legada de SlimyBees o fallback por Slimefun ID.
     */
    public static BeeGenome extractGenome(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        // 1. Clave nativa StarSuites
        if (pdc.has(KEY_BEE_GENOME, PersistentDataType.STRING)) {
            String str = pdc.get(KEY_BEE_GENOME, PersistentDataType.STRING);
            if (str != null && !str.isEmpty()) {
                return BeeGenome.deserialize(str);
            }
        }

        // 2. Clave legada SlimyBees
        if (pdc.has(KEY_LEGACY_BEE_TYPE, PersistentDataType.STRING)) {
            String str = pdc.get(KEY_LEGACY_BEE_TYPE, PersistentDataType.STRING);
            if (str != null && !str.isEmpty()) {
                return BeeGenome.deserialize(str);
            }
        }

        // 3. Fallback por ID de Slimefun
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId != null) {
            String clean = sfId.replace("_BEE_PRINCESS", "").replace("_BEE_DRONE", "").replace("_BEE_QUEEN", "");
            BeeSpecies species = BeeSpecies.fromId(clean);
            if (species != null) {
                return BeeGenome.createPure(species);
            }
        }

        return null;
    }

    private static void applyGenomePdc(ItemMeta meta, BeeGenome genome, String sfId) {
        String serialized = genome.serialize();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_BEE_GENOME, PersistentDataType.STRING, serialized);
        // Compatibilidad binaria con servidores legacy
        pdc.set(KEY_LEGACY_BEE_TYPE, PersistentDataType.STRING, serialized);

        // Clave Slimefun
        pdc.set(SuiteItemPdcBridge.SLIMEFUN_ITEM_KEY, PersistentDataType.STRING, sfId);
    }

    private static List<String> buildBeeLore(BeeGenome genome, String caste) {
        List<String> lore = new ArrayList<>();
        BeeSpecies primary = genome.getPrimarySpecies();
        BeeSpecies secondary = genome.getSecondarySpecies();

        lore.add(ChatColor.GRAY + "Casta: " + ChatColor.YELLOW + caste);
        if (primary == secondary) {
            lore.add(ChatColor.GRAY + "Especie: " + primary.getColorCode() + primary.getDisplayName() + ChatColor.DARK_GRAY + " (Pura)");
        } else {
            lore.add(ChatColor.GRAY + "Especie: " + primary.getColorCode() + primary.getDisplayName()
                    + ChatColor.GRAY + " / " + secondary.getColorCode() + secondary.getDisplayName() + ChatColor.DARK_GRAY + " (Híbrida)");
        }

        BeeChromosome fert = genome.getChromosome(BeeChromosomeType.FERTILITY);
        if (fert != null) {
            lore.add(ChatColor.GRAY + "Fertilidad: " + ChatColor.WHITE + fert.getPrimaryAllele().replace("fertility:", ""));
        }
        BeeChromosome prod = genome.getChromosome(BeeChromosomeType.PRODUCTIVITY);
        if (prod != null) {
            lore.add(ChatColor.GRAY + "Producción: " + ChatColor.WHITE + prod.getPrimaryAllele().replace("productivity:", ""));
        }

        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "✦ StarSuites Bio-Genetics ✦");
        return lore;
    }
}

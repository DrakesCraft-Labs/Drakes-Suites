package com.drakescraft.suites.bio.geneticchickens;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Generador y validador de huevos genéticos de GeneticChickengineering.
 * Preserva información cromosómica, tier biológico y claves PDC.
 */
public final class ChickenEggItem {

    public static final String SLIMEFUN_ID = "GCE_RESOURCE_EGG";
    private static NamespacedKey KEY_TYPING;
    private static NamespacedKey KEY_DNA;
    private static NamespacedKey KEY_TIER;

    public static void initializeKeys(Plugin plugin) {
        KEY_TYPING = new NamespacedKey(plugin, "chicken_typing");
        KEY_DNA = new NamespacedKey(plugin, "chicken_dna");
        KEY_TIER = new NamespacedKey(plugin, "chicken_tier");
    }

    @Nonnull
    public static ItemStack createEgg(@Nonnull ChickenDNA dna, @Nonnull Plugin plugin) {
        if (KEY_TYPING == null) {
            initializeKeys(plugin);
        }

        int typing = dna.getTyping();
        ChickenSpeciesRegistry.ChickenSpec spec = ChickenSpeciesRegistry.get(typing);

        ItemStack item = new ItemStack(Material.EGG);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "<gradient:#ffe259:#ffa751><bold>Huevo Genético: " + spec.name() + "</bold></gradient>");
            List<String> lore = Arrays.asList(
                    "&7Huevo fértil con genoma secuenciado.",
                    "&8» &fEspecie: &e" + spec.name() + " &7(Tier " + spec.tier() + ")",
                    "&8» &fGenoma: &b" + dna.toNotation(),
                    "&8» &fGenes Activos: &a" + dna.getActiveGeneCount() + "/6",
                    "",
                    "&eIncúbalo en una incubadora eléctrica para eclosionar.",
                    "&8» &fID: &e" + SLIMEFUN_ID
            );
            CrossVersionAdapter.setLore(meta, lore);

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(KEY_TYPING, PersistentDataType.INTEGER, typing);
            pdc.set(KEY_DNA, PersistentDataType.STRING, dna.toNotation());
            pdc.set(KEY_TIER, PersistentDataType.INTEGER, spec.tier());

            SuiteItemPdcBridge.setSlimefunId(meta, SLIMEFUN_ID);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Nullable
    public static ChickenDNA extractDna(@Nullable ItemStack item) {
        if (item == null || item.getType() != Material.EGG || KEY_DNA == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(KEY_DNA, PersistentDataType.STRING)) {
            return null;
        }
        String dnaStr = pdc.get(KEY_DNA, PersistentDataType.STRING);
        return dnaStr != null ? new ChickenDNA(dnaStr) : null;
    }

    public static int extractTyping(@Nullable ItemStack item) {
        if (item == null || KEY_TYPING == null) return -1;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return -1;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.getOrDefault(KEY_TYPING, PersistentDataType.INTEGER, -1);
    }
}

package com.drakescraft.suites.bio.cultivation;

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
 * Representación y serialización de semillas de Cultivation.
 * Preserva metadatos botánicos y claves PDC para integración con Paper y Purpur 26.X.
 */
public final class CultivationSeedItem {

    public static final String SLIMEFUN_ID_PREFIX = "CULTIVATION_SEED_";
    private static NamespacedKey KEY_PLANT_NAME;

    public static void initializeKeys(Plugin plugin) {
        KEY_PLANT_NAME = new NamespacedKey(plugin, "cultivation_plant_name");
    }

    @Nonnull
    public static ItemStack createSeed(@Nonnull String plantName, @Nonnull Plugin plugin) {
        if (KEY_PLANT_NAME == null) {
            initializeKeys(plugin);
        }

        String sfId = SLIMEFUN_ID_PREFIX + plantName.toUpperCase().replace(" ", "_");
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "<gradient:#56ab2f:#a8e063><bold>Semilla: " + plantName + "</bold></gradient>");
            List<String> lore = Arrays.asList(
                    "&7Semilla híbrida de alta pureza botánica.",
                    "&8» &fPlanta: &a" + plantName,
                    "",
                    "&7Plántala en tierra arada hidratada junto a otra",
                    "&7variedad en diagonal para inducir polinización cruzada.",
                    "&8» &fID: &e" + sfId
            );
            CrossVersionAdapter.setLore(meta, lore);

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(KEY_PLANT_NAME, PersistentDataType.STRING, plantName);

            SuiteItemPdcBridge.setSlimefunId(meta, sfId);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Nullable
    public static String extractPlantName(@Nullable ItemStack item) {
        if (item == null || item.getType() != Material.WHEAT_SEEDS || KEY_PLANT_NAME == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(KEY_PLANT_NAME, PersistentDataType.STRING);
    }
}

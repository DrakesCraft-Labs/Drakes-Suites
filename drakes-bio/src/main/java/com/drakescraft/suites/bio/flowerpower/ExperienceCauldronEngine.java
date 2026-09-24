package com.drakescraft.suites.bio.flowerpower;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Motor de almacenamiento y transferencia de experiencia en calderos y tomos arcanos.
 * Mantiene compatibilidad dual con el formato original de FlowerPower.
 */
public class ExperienceCauldronEngine {

    public static final NamespacedKey KEY_EXP_STORED = new NamespacedKey("drakes", "exp_stored");
    public static final NamespacedKey KEY_EXP_MAX = new NamespacedKey("drakes", "exp_max");
    public static final NamespacedKey LEGACY_KEY_EXP_STORED = new NamespacedKey("flowerpower", "exp_stored");

    public static final int DEFAULT_MAX_EXP = 1_000_000;

    /**
     * Obtiene la experiencia acumulada en un ítem (ej. Tome o Cauldron).
     */
    public static int getStoredExp(@Nullable ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pdc.has(KEY_EXP_STORED, PersistentDataType.INTEGER)) {
            Integer val = pdc.get(KEY_EXP_STORED, PersistentDataType.INTEGER);
            return val != null ? val : 0;
        }
        if (pdc.has(LEGACY_KEY_EXP_STORED, PersistentDataType.INTEGER)) {
            Integer val = pdc.get(LEGACY_KEY_EXP_STORED, PersistentDataType.INTEGER);
            return val != null ? val : 0;
        }
        return 0;
    }

    /**
     * Deposita experiencia en el contenedor hasta su capacidad máxima.
     * Retorna la cantidad real de experiencia depositada.
     */
    public static int depositExp(@Nonnull ItemStack item, int amount, int maxCapacity) {
        if (amount <= 0) return 0;
        int current = getStoredExp(item);
        int space = Math.max(0, maxCapacity - current);
        int toAdd = Math.min(amount, space);
        if (toAdd <= 0) return 0;

        int newTotal = current + toAdd;
        setStoredExp(item, newTotal, maxCapacity);
        return toAdd;
    }

    /**
     * Retira experiencia del contenedor. Retorna la cantidad retirada.
     */
    public static int withdrawExp(@Nonnull ItemStack item, int amount) {
        if (amount <= 0) return 0;
        int current = getStoredExp(item);
        int toRemove = Math.min(amount, current);
        if (toRemove <= 0) return 0;

        int newTotal = current - toRemove;
        setStoredExp(item, newTotal, DEFAULT_MAX_EXP);
        return toRemove;
    }

    /**
     * Guarda la experiencia en el contenedor con PDC dual y actualiza el lore.
     */
    public static void setStoredExp(@Nonnull ItemStack item, int amount, int maxCapacity) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_EXP_STORED, PersistentDataType.INTEGER, amount);
        pdc.set(KEY_EXP_MAX, PersistentDataType.INTEGER, maxCapacity);
        pdc.set(LEGACY_KEY_EXP_STORED, PersistentDataType.INTEGER, amount);

        // Actualizar visualización del ítem
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if ("EXPERIENCE_TOME".equalsIgnoreCase(sfId)) {
            meta.setDisplayName(ChatColor.GOLD + "Tomo de Experiencia " + ChatColor.GREEN + "(" + amount + " / " + maxCapacity + ")");
        }

        item.setItemMeta(meta);
    }
}

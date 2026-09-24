package com.drakescraft.suites.magic.alchimiavitae;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Catálogo canónico de infusiones místicas de AlchimiaVitae.
 */
@Getter
public enum InfusionType {

    DESTRUCTIVE_CRITS("infusion_destructivecrits", "Golpes Críticos Destructivos", ChatColor.RED + "✦ Críticos Destructivos ✦", TargetType.MELEE),
    PHANTOM_CRITS("infusion_phantomcrits", "Golpes Críticos Fantasmales", ChatColor.AQUA + "✦ Críticos Fantasmales ✦", TargetType.MELEE),
    FORCEFUL("infusion_forceful", "Disparo Contundente", ChatColor.DARK_GREEN + "✦ Disparo Contundente ✦", TargetType.RANGED),
    HEALING("infusion_healing", "Disparo Curativo", ChatColor.GREEN + "✦ Disparo Curativo ✦", TargetType.RANGED),
    TRUE_AIM("infusion_trueaim", "Puntería Verdadera", ChatColor.LIGHT_PURPLE + "✦ Puntería Verdadera ✦", TargetType.RANGED),
    VOLATILITY("infusion_volatile", "Proyectil Volátil", ChatColor.GOLD + "✦ Proyectil Volátil ✦", TargetType.RANGED),
    TOTEM_BATTERY("infusion_totemstorage", "Batería de Tótems", ChatColor.YELLOW + "✦ Batería de Tótems ✦", TargetType.CHESTPLATE),
    AUTO_REPLANT("infusion_autoreplant", "Replantado Automático", ChatColor.DARK_AQUA + "✦ Replantado Automático ✦", TargetType.HOE),
    KNOCKBACK("infusion_knockback", "Resistencia de Empuje", ChatColor.DARK_GRAY + "✦ Inercia Firme ✦", TargetType.FISHING_ROD);

    public enum TargetType {
        MELEE, RANGED, CHESTPLATE, HOE, FISHING_ROD
    }

    private final String keyName;
    private final String displayName;
    private final String loreText;
    private final TargetType targetType;

    private final NamespacedKey nativeKey;
    private final NamespacedKey legacyKey;

    private static final Map<String, InfusionType> BY_KEY;

    static {
        Map<String, InfusionType> map = new HashMap<>();
        for (InfusionType type : values()) {
            map.put(type.keyName.toLowerCase(Locale.ROOT), type);
        }
        BY_KEY = Collections.unmodifiableMap(map);
    }

    InfusionType(String keyName, String displayName, String loreText, TargetType targetType) {
        this.keyName = keyName;
        this.displayName = displayName;
        this.loreText = loreText;
        this.targetType = targetType;
        this.nativeKey = new NamespacedKey("drakes", keyName);
        this.legacyKey = new NamespacedKey("alchimiavitae", keyName);
    }

    /**
     * Comprueba si el ítem es apto para recibir esta infusión.
     */
    public boolean canApply(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        Material mat = item.getType();

        return switch (targetType) {
            case MELEE -> mat == Material.GOLDEN_SWORD || mat == Material.IRON_SWORD ||
                          mat == Material.DIAMOND_SWORD || mat == Material.NETHERITE_SWORD ||
                          mat == Material.GOLDEN_AXE || mat == Material.IRON_AXE ||
                          mat == Material.DIAMOND_AXE || mat == Material.NETHERITE_AXE;
            case RANGED -> mat == Material.BOW || mat == Material.CROSSBOW;
            case CHESTPLATE -> mat == Material.GOLDEN_CHESTPLATE || mat == Material.IRON_CHESTPLATE ||
                               mat == Material.DIAMOND_CHESTPLATE || mat == Material.NETHERITE_CHESTPLATE;
            case HOE -> mat == Material.GOLDEN_HOE || mat == Material.IRON_HOE ||
                        mat == Material.DIAMOND_HOE || mat == Material.NETHERITE_HOE;
            case FISHING_ROD -> mat == Material.FISHING_ROD;
        };
    }

    /**
     * Comprueba si el ítem ya posee esta infusión activa en su PDC.
     */
    public boolean has(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (this == TOTEM_BATTERY) {
            return pdc.has(nativeKey, PersistentDataType.INTEGER) || pdc.has(legacyKey, PersistentDataType.INTEGER);
        } else {
            return pdc.has(nativeKey, PersistentDataType.BYTE) || pdc.has(legacyKey, PersistentDataType.BYTE);
        }
    }

    /**
     * Aplica la infusión al ItemStack preservando tanto la clave moderna como la legada.
     */
    public boolean apply(ItemStack item) {
        if (!canApply(item) || has(item)) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (this == TOTEM_BATTERY) {
            pdc.set(nativeKey, PersistentDataType.INTEGER, 0);
            pdc.set(legacyKey, PersistentDataType.INTEGER, 0);
        } else {
            pdc.set(nativeKey, PersistentDataType.BYTE, (byte) 1);
            pdc.set(legacyKey, PersistentDataType.BYTE, (byte) 1);
        }

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.add(loreText);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return true;
    }

    /**
     * Obtiene el número de tótems almacenados en una coraza con infusión de Batería de Tótems.
     */
    public static int getTotemCount(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(TOTEM_BATTERY.nativeKey, PersistentDataType.INTEGER)) {
            Integer v = pdc.get(TOTEM_BATTERY.nativeKey, PersistentDataType.INTEGER);
            return v != null ? Math.max(0, v) : 0;
        }
        if (pdc.has(TOTEM_BATTERY.legacyKey, PersistentDataType.INTEGER)) {
            Integer v = pdc.get(TOTEM_BATTERY.legacyKey, PersistentDataType.INTEGER);
            return v != null ? Math.max(0, v) : 0;
        }
        return 0;
    }

    /**
     * Actualiza el número de tótems almacenados en la coraza.
     */
    public static void setTotemCount(ItemStack item, int count) {
        if (item == null || !item.hasItemMeta() || !TOTEM_BATTERY.has(item)) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(TOTEM_BATTERY.nativeKey, PersistentDataType.INTEGER, Math.max(0, count));
        pdc.set(TOTEM_BATTERY.legacyKey, PersistentDataType.INTEGER, Math.max(0, count));

        // Actualizar línea de lore
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        String countLine = ChatColor.GRAY + "Tótems Almacenados: " + ChatColor.GOLD + count;
        boolean replaced = false;
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("Tótems Almacenados:")) {
                lore.set(i, countLine);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            lore.add(countLine);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static InfusionType fromKey(String key) {
        if (key == null) return null;
        return BY_KEY.get(key.toLowerCase(Locale.ROOT));
    }
}

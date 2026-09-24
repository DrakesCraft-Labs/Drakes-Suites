package com.drakescraft.suites.magic.souljars;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Registro de entidades, requisitos de almas y fábrica de items para el módulo SoulJars.
 * Mantiene 100% de compatibilidad con los IDs canónicos de Slimefun:
 * - SOUL_JAR
 * - <ENTITY>_SOUL_JAR
 * - FILLED_<ENTITY>_SOUL_JAR
 * - <ENTITY>_BROKEN_SPAWNER
 */
public class SoulJarRegistry {

    public static final String EMPTY_JAR_ID = "SOUL_JAR";
    public static final String PDC_NAMESPACE = "drakesmagic";
    public static final String PDC_KEY_SOULS = "souls";
    public static final String PDC_KEY_ENTITY = "entity_type";

    private final Map<EntityType, Integer> requiredSouls = new EnumMap<>(EntityType.class);

    public SoulJarRegistry() {
        // Valores por defecto canónicos
        registerDefault(EntityType.ZOMBIE, 128);
        registerDefault(EntityType.SKELETON, 128);
        registerDefault(EntityType.CREEPER, 128);
        registerDefault(EntityType.SPIDER, 128);
        registerDefault(EntityType.CAVE_SPIDER, 128);
        registerDefault(EntityType.BLAZE, 128);
        registerDefault(EntityType.ENDERMAN, 128);
        registerDefault(EntityType.WITHER_SKELETON, 128);
        registerDefault(EntityType.SLIME, 128);
        registerDefault(EntityType.MAGMA_CUBE, 128);
        registerDefault(EntityType.GHAST, 64);
        registerDefault(EntityType.PIGLIN, 128);
        registerDefault(EntityType.PIG, 64);
        registerDefault(EntityType.COW, 64);
        registerDefault(EntityType.SHEEP, 64);
        registerDefault(EntityType.CHICKEN, 64);
    }

    public void registerDefault(EntityType type, int souls) {
        requiredSouls.put(type, souls);
    }

    public void setRequiredSouls(EntityType type, int souls) {
        requiredSouls.put(type, souls);
    }

    public boolean isSupportedEntity(EntityType type) {
        return type != null && requiredSouls.containsKey(type);
    }

    public int getRequiredSouls(EntityType type) {
        return requiredSouls.getOrDefault(type, 128);
    }

    public Set<EntityType> getSupportedEntities() {
        return Collections.unmodifiableSet(requiredSouls.keySet());
    }

    /**
     * Crea un frasco de almas vacío.
     */
    public ItemStack createEmptyJar(int amount) {
        ItemStack item = new ItemStack(Material.GLASS_BOTTLE, Math.max(1, amount));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&bSoul Jar &7(Empty)");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesMagic",
                    "&7Mata un mob compatible teniendo este",
                    "&7frasco en tu inventario para comenzar",
                    "&7a absorber su esencia de alma.",
                    "",
                    "&eKill compatible mobs to harvest souls."
            ));
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, EMPTY_JAR_ID);
        return item;
    }

    /**
     * Crea un frasco de almas en progreso.
     */
    public ItemStack createSoulJar(EntityType type, int currentSouls, int maxSouls) {
        ItemStack item = new ItemStack(Material.GLASS_BOTTLE);
        String name = humanize(type.name());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&cSoul Jar &7(" + name + ")");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesMagic",
                    "&7Infused Souls: &e" + currentSouls + " &7/ &a" + maxSouls,
                    "&7Mob: &f" + name,
                    "",
                    "&7Continúa cosechando este mob para",
                    "&7completar la saturación de almas.",
                    "&eHarvest more to fill this jar completely."
            ));
            item.setItemMeta(meta);
        }
        String id = type.name() + "_SOUL_JAR";
        SuiteItemPdcBridge.setSlimefunId(item, id);
        SuiteItemPdcBridge.setCustomInt(item, PDC_NAMESPACE, PDC_KEY_SOULS, currentSouls);
        SuiteItemPdcBridge.setCustomString(item, PDC_NAMESPACE, PDC_KEY_ENTITY, type.name());
        return item;
    }

    /**
     * Crea un frasco de almas completamente lleno.
     */
    public ItemStack createFilledJar(EntityType type, int maxSouls) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        String name = humanize(type.name());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&cFilled Soul Jar &7(" + name + ")");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesMagic",
                    "&7Infused Souls: &e" + maxSouls + " &a(COMPLETO / COMPLETE)",
                    "&7Mob: &f" + name,
                    "",
                    "&6¡Saturación mística alcanzada!",
                    "&7Úsalo en el Ancient Altar para restaurar",
                    "&7spawners rotos de esta criatura."
            ));
            item.setItemMeta(meta);
        }
        String id = "FILLED_" + type.name() + "_SOUL_JAR";
        SuiteItemPdcBridge.setSlimefunId(item, id);
        SuiteItemPdcBridge.setCustomInt(item, PDC_NAMESPACE, PDC_KEY_SOULS, maxSouls);
        SuiteItemPdcBridge.setCustomString(item, PDC_NAMESPACE, PDC_KEY_ENTITY, type.name());
        return item;
    }

    /**
     * Crea un spawner roto asociado a un tipo de entidad.
     */
    public ItemStack createBrokenSpawner(EntityType type) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        String name = humanize(type.name());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&cBroken Spawner &7(" + name + ")");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesMagic",
                    "&7Tipo de criatura: &e" + name,
                    "",
                    "&7Restaura este spawner combinándolo",
                    "&7con un Filled Soul Jar en un Ancient Altar."
            ));
            item.setItemMeta(meta);
        }
        String id = type.name() + "_BROKEN_SPAWNER";
        SuiteItemPdcBridge.setSlimefunId(item, id);
        SuiteItemPdcBridge.setCustomString(item, PDC_NAMESPACE, PDC_KEY_ENTITY, type.name());
        return item;
    }

    public boolean isEmptyJar(ItemStack item) {
        return SuiteItemPdcBridge.hasSlimefunId(item, EMPTY_JAR_ID);
    }

    public boolean isSoulJar(ItemStack item) {
        String id = SuiteItemPdcBridge.getSlimefunId(item);
        return id != null && id.endsWith("_SOUL_JAR") && !id.startsWith("FILLED_") && !id.equals(EMPTY_JAR_ID);
    }

    public boolean isFilledJar(ItemStack item) {
        String id = SuiteItemPdcBridge.getSlimefunId(item);
        return id != null && id.startsWith("FILLED_") && id.endsWith("_SOUL_JAR");
    }

    public EntityType getJarEntityType(ItemStack item) {
        String typeStr = SuiteItemPdcBridge.getCustomString(item, PDC_NAMESPACE, PDC_KEY_ENTITY);
        if (typeStr != null) {
            try {
                return EntityType.valueOf(typeStr);
            } catch (IllegalArgumentException ignored) {}
        }
        String id = SuiteItemPdcBridge.getSlimefunId(item);
        if (id == null) return null;
        if (id.startsWith("FILLED_")) {
            id = id.substring("FILLED_".length());
        }
        if (id.endsWith("_SOUL_JAR")) {
            id = id.substring(0, id.length() - "_SOUL_JAR".length());
            try {
                return EntityType.valueOf(id);
            } catch (IllegalArgumentException ignored) {}
        }
        return null;
    }

    public int getStoredSouls(ItemStack item) {
        Integer val = SuiteItemPdcBridge.getCustomInt(item, PDC_NAMESPACE, PDC_KEY_SOULS);
        if (val != null) {
            return val;
        }
        // Fallback: leer de lore
        ItemMeta meta = item != null ? item.getItemMeta() : null;
        if (meta != null && meta.hasLore() && meta.getLore() != null) {
            for (String line : meta.getLore()) {
                String stripped = ChatColor.stripColor(line);
                if (stripped != null && stripped.startsWith("Infused Souls:")) {
                    try {
                        String[] parts = stripped.split(":");
                        if (parts.length > 1) {
                            String numPart = parts[1].trim().split("/")[0].trim().split(" ")[0].trim();
                            return Integer.parseInt(numPart);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        return 0;
    }

    public static String humanize(String input) {
        if (input == null || input.isEmpty()) return "";
        String[] words = input.toLowerCase().replace('_', ' ').split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}

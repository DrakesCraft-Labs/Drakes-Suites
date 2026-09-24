package com.drakescraft.suites.bio.mobcapturer;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Registro de items y reglas de captura para el módulo MobCapturer en DrakesBio.
 * Preserva 100% de compatibilidad con los IDs canónicos de Slimefun:
 * - MOB_CANNON
 * - MOB_CAPTURING_PELLET
 * - <ENTITY>_MOB_EGG
 */
public class MobCapturerRegistry {

    public static final String MOB_CANNON_ID = "MOB_CANNON";
    public static final String MOB_PELLET_ID = "MOB_CAPTURING_PELLET";
    public static final String PDC_NAMESPACE = "drakesbio";
    public static final String PDC_KEY_ENTITY = "captured_entity";

    private final Set<EntityType> blacklist = EnumSet.of(
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
            EntityType.WARDEN,
            EntityType.ELDER_GUARDIAN,
            EntityType.GIANT,
            EntityType.ARMOR_STAND,
            EntityType.PLAYER
    );

    private boolean preventBossCapture = true;
    private boolean preventNamedNpcCapture = true;

    public void configure(boolean preventBoss, boolean preventNpc) {
        this.preventBossCapture = preventBoss;
        this.preventNamedNpcCapture = preventNpc;
    }

    /**
     * Comprueba si una criatura específica puede ser capturada.
     */
    public boolean canCapture(LivingEntity entity) {
        if (entity == null || !entity.isValid() || entity.isDead()) {
            return false;
        }

        EntityType type = entity.getType();
        if (blacklist.contains(type)) {
            return false;
        }

        if (preventNamedNpcCapture) {
            // Protección contra captura de NPCs de Citizens u otros plugins
            if (entity.hasMetadata("NPC") || entity.hasMetadata("shopkeeper")) {
                return false;
            }
        }

        if (preventBossCapture) {
            // Protección contra bosses personalizados (DrakesBosses / MultiverseCreatures)
            if (entity.hasMetadata("drakes_boss") || entity.hasMetadata("boss_id")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Crea un Mob Cannon.
     */
    public ItemStack createMobCannon() {
        ItemStack item = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&6Mob Cannon");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesBio",
                    "&7Dispara perdigones de captura biológica.",
                    "&7Permite almacenar criaturas vivas en huevos.",
                    "",
                    "&eMunición requerida: &fMob Capturing Pellet",
                    "&eFire at compatible mobs to capture them."
            ));
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, MOB_CANNON_ID);
        return item;
    }

    /**
     * Crea munición Mob Capturing Pellet.
     */
    public ItemStack createMobPellet(int amount) {
        ItemStack item = new ItemStack(Material.SNOWBALL, Math.max(1, amount));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&bMob Capturing Pellet");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesBio",
                    "&7Perdigón de contención criogénica.",
                    "&7Se carga automáticamente en el Mob Cannon.",
                    "",
                    "&eCryogenic containment ammunition."
            ));
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, MOB_PELLET_ID);
        return item;
    }

    /**
     * Crea un huevo de mob capturado.
     */
    public ItemStack createMobEgg(EntityType type) {
        Material eggMaterial = getSpawnEggMaterial(type);
        ItemStack item = new ItemStack(eggMaterial);
        String name = humanize(type.name());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&aMob Egg &7(" + name + ")");
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesBio",
                    "&7Criatura capturada: &e" + name,
                    "",
                    "&7Haz clic derecho en el suelo para liberar",
                    "&7a la criatura en su estado original.",
                    "&eRight click on ground to release."
            ));
            item.setItemMeta(meta);
        }
        String id = type.name() + "_MOB_EGG";
        SuiteItemPdcBridge.setSlimefunId(item, id);
        SuiteItemPdcBridge.setCustomString(item, PDC_NAMESPACE, PDC_KEY_ENTITY, type.name());
        return item;
    }

    public boolean isMobCannon(ItemStack item) {
        return SuiteItemPdcBridge.hasSlimefunId(item, MOB_CANNON_ID);
    }

    public boolean isMobPellet(ItemStack item) {
        return SuiteItemPdcBridge.hasSlimefunId(item, MOB_PELLET_ID);
    }

    public boolean isMobEgg(ItemStack item) {
        String id = SuiteItemPdcBridge.getSlimefunId(item);
        return id != null && id.endsWith("_MOB_EGG");
    }

    public EntityType getEggEntityType(ItemStack item) {
        String typeStr = SuiteItemPdcBridge.getCustomString(item, PDC_NAMESPACE, PDC_KEY_ENTITY);
        if (typeStr != null) {
            try {
                return EntityType.valueOf(typeStr);
            } catch (IllegalArgumentException ignored) {}
        }
        String id = SuiteItemPdcBridge.getSlimefunId(item);
        if (id != null && id.endsWith("_MOB_EGG")) {
            String mobName = id.substring(0, id.length() - "_MOB_EGG".length());
            try {
                return EntityType.valueOf(mobName);
            } catch (IllegalArgumentException ignored) {}
        }
        return null;
    }

    public Material getSpawnEggMaterial(EntityType type) {
        try {
            Material mat = Material.getMaterial(type.name() + "_SPAWN_EGG");
            if (mat != null) return mat;
        } catch (Exception ignored) {}
        return Material.EGG;
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

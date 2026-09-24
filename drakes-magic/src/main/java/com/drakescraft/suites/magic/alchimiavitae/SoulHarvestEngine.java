package com.drakescraft.suites.magic.alchimiavitae;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Motor de recolección de almas condensadas y multiplicación de experiencia para AlchimiaVitae.
 */
public final class SoulHarvestEngine {

    public static final String SOUL_COLLECTOR_ID = "AV_SOUL_COLLECTOR";
    public static final String CONDENSED_SOUL_ID = "AV_CONDENSED_SOUL";

    @Getter
    public static final class HarvestResult {
        private final int soulCount;
        private final int expMultiplier;
        private final List<ItemStack> droppedSouls;

        public HarvestResult(int soulCount, int expMultiplier, List<ItemStack> droppedSouls) {
            this.soulCount = soulCount;
            this.expMultiplier = expMultiplier;
            this.droppedSouls = droppedSouls;
        }
    }

    private SoulHarvestEngine() {}

    /**
     * Crea un ItemStack del Coleccionista de Almas (AV_SOUL_COLLECTOR).
     */
    public static ItemStack createSoulCollector() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Coleccionista de Almas");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Extrae almas condensadas de monstruos caídos.");
            lore.add(ChatColor.YELLOW + "✦ Triplica la experiencia obtenida de mobs.");
            lore.add(ChatColor.RED + "✦ Entidades del Wither otorgan almas adicionales.");
            lore.add("");
            lore.add(ChatColor.DARK_GRAY + "✦ StarSuites Magic - AlchimiaVitae ✦");
            meta.setLore(lore);
            meta.setUnbreakable(true);
            sword.setItemMeta(meta);
        }
        sword.addUnsafeEnchantment(Enchantment.SHARPNESS, 3);
        SuiteItemPdcBridge.setSlimefunId(sword, SOUL_COLLECTOR_ID);
        return sword;
    }

    /**
     * Crea un orbe de Alma Condensada (AV_CONDENSED_SOUL).
     */
    public static ItemStack createCondensedSoul(int amount) {
        ItemStack item = new ItemStack(Material.LIGHT_BLUE_DYE, Math.max(1, amount));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Alma Condensada");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Un fragmento de alma condensada en un orbe etéreo.");
            lore.add(ChatColor.DARK_AQUA + "Utilizada en rituales del Altar Divino e infusiones.");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        item.addUnsafeEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);
        SuiteItemPdcBridge.setSlimefunId(item, CONDENSED_SOUL_ID);
        return item;
    }

    /**
     * Evalúa si un mob muerto con el Coleccionista de Almas debe otorgar almas condensadas.
     *
     * @param entityType Tipo de entidad asesinada
     * @param baseDropChance Probabilidad base de obtención de almas (default ~0.33)
     * @return Resultado de la recolección
     */
    public static HarvestResult evaluateHarvest(EntityType entityType, double baseDropChance) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        if (rand.nextDouble() > baseDropChance) {
            return new HarvestResult(0, 3, List.of());
        }

        int souls = 1;
        int expMultiplier = 3;

        if (entityType == EntityType.WITHER) {
            souls = 1 + rand.nextInt(15);
        } else if (entityType == EntityType.WITHER_SKELETON) {
            souls = 1 + rand.nextInt(5);
        } else if (entityType == EntityType.ENDER_DRAGON) {
            expMultiplier = 1;
        }

        List<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < souls; i++) {
            drops.add(createCondensedSoul(1));
        }

        return new HarvestResult(souls, expMultiplier, drops);
    }
}

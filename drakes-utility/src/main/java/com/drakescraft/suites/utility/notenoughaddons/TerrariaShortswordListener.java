package com.drakescraft.suites.utility.notenoughaddons;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * TerrariaShortswordListener - Handles combat damage, critical strikes, and custom knockback.
 */
public class TerrariaShortswordListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir() || !item.hasItemMeta()) {
            return;
        }

        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId == null || !sfId.startsWith("SHORTSWORD_")) {
            return;
        }

        TerrariaShortsword sword = TerrariaShortsword.getById(sfId);
        if (sword != null) {
            TerrariaUtils.castDamage(event, player, item, sword.getCritChance(), sword.getDamage(), sword.getKnockback(), sword.getUseTime());
        }
    }
}

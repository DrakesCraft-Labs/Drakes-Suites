package com.drakescraft.suites.combat.warfare;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener de balística, exoesqueletos y combate táctico para SlimefunWarfare.
 */
public class WarfareListener implements Listener {

    private final WarfareModule module;
    private final WarfareRegistry registry;

    public WarfareListener(WarfareModule module, WarfareRegistry registry) {
        this.module = module;
        this.registry = registry;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        ItemStack weapon = player.getInventory().getItemInMainHand();
        String sfId = SuiteItemPdcBridge.getSlimefunId(weapon);

        if (sfId != null && registry.isWarfareWeapon(sfId)) {
            double customDamage = registry.getWeaponDamage(sfId);
            event.setDamage(customDamage);

            if ("ENERGY_BLADE".equalsIgnoreCase(sfId) && event.getEntity() instanceof LivingEntity target) {
                target.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, target.getLocation().add(0, 1, 0), 15, 0.4, 0.4, 0.4);
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.8f, 1.8f);
            }
        }
    }
}

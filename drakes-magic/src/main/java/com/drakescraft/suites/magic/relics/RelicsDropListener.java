package com.drakescraft.suites.magic.relics;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Listener que gestiona la obtención de reliquias de Cthonia mediante minería y combate.
 */
public class RelicsDropListener implements Listener {

    private final RelicsCthoniaModule module;
    private final RelicsRegistry registry;

    public RelicsDropListener(RelicsCthoniaModule module, RelicsRegistry registry) {
        this.module = module;
        this.registry = registry;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (!registry.getMiningSources().contains(event.getBlock().getType())) return;

        double chance = module.getMiningDropChance(); // e.g. 0.005 (0.5%)
        if (ThreadLocalRandom.current().nextDouble() < chance) {
            RelicDefinition relic = registry.rollRandomRelic();
            if (relic != null) {
                ItemStack item = registry.createRelic(relic.id(), 1);
                if (item != null) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null && event.getDamageSource() != null && event.getDamageSource().getCausingEntity() instanceof Player p) {
            killer = p;
        }
        if (killer == null) return;
        if (killer.getGameMode() != GameMode.SURVIVAL) return;

        if (!registry.getMobSources().contains(event.getEntityType())) return;

        double chance = module.getMobDropChance(); // e.g. 0.025 (2.5%)
        if (ThreadLocalRandom.current().nextDouble() < chance) {
            RelicDefinition relic = registry.rollRandomRelic();
            if (relic != null) {
                ItemStack item = registry.createRelic(relic.id(), 1);
                if (item != null) {
                    event.getDrops().add(item);
                }
            }
        }
    }
}

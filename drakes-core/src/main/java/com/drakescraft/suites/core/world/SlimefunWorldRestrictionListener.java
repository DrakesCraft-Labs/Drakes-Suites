package com.drakescraft.suites.core.world;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener que intercepta eventos de colocación e interacción con ítems de Slimefun
 * en mundos donde Slimefun está estrictamente deshabilitado (ej: Survival Clásico).
 */
public class SlimefunWorldRestrictionListener implements Listener {

    private final SlimefunWorldFilter filter;

    public SlimefunWorldRestrictionListener(SlimefunWorldFilter filter) {
        this.filter = filter;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (filter.isSlimefunAllowed(event.getBlock().getWorld())) {
            return;
        }

        ItemStack item = event.getItemInHand();
        if (SuiteItemPdcBridge.isSlimefunItem(item)) {
            event.setCancelled(true);
            notifyPlayer(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (filter.isSlimefunAllowed(player.getWorld())) {
            return;
        }

        ItemStack item = event.getItem();
        if (SuiteItemPdcBridge.isSlimefunItem(item)) {
            event.setCancelled(true);
            notifyPlayer(player);
        }
    }

    private void notifyPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        player.sendMessage(MiniMessage.miniMessage().deserialize(filter.getRestrictionMessage()));
    }
}

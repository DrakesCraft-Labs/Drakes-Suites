package com.drakescraft.suites.utility.notenoughaddons;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * MinerBackpackListener - Handles automatic vacuuming of mined ores directly into Miner's Backpack.
 */
public class MinerBackpackListener implements Listener {

    private final Plugin plugin;

    public MinerBackpackListener(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) {
            return;
        }

        Item pickedItem = e.getItem();
        ItemStack pickedItemStack = pickedItem.getItemStack();
        if (!MinerBackpack.isItemAllowed(pickedItemStack)) {
            return;
        }

        // Look for Miner's Backpack in player's inventory
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType() == Material.CHEST && item.hasItemMeta()) {
                String sfId = SuiteItemPdcBridge.getSlimefunId(item);
                if (NEAItems.MINER_BACKPACK.equals(sfId)) {
                    // Item matches Miner Backpack
                    return;
                }
            }
        }
    }
}

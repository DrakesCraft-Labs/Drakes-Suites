package com.drakescraft.suites.utility.notenoughaddons;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * AngelBlock - Places a temporary safe block beneath the player's feet.
 */
public class AngelBlock {

    private static final List<Block> angelBlockBufferList = new ArrayList<>();
    private final Plugin plugin;

    public AngelBlock(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean use(@Nonnull Player p) {
        final Location playerLocation = p.getLocation();
        final Location blockLocation = playerLocation.clone();
        blockLocation.setY(blockLocation.getY() - 1);
        Block targetBlock = p.getWorld().getBlockAt(blockLocation);
        if (targetBlock.getType() != Material.AIR) {
            return false;
        }

        targetBlock.setType(Material.COBBLESTONE);
        angelBlockBufferList.add(targetBlock);

        ItemStack angelItem = NEAItems.getItem(NEAItems.ANGEL_BLOCK);
        if (angelItem != null && p.getInventory().containsAtLeast(angelItem, 1)) {
            p.getInventory().removeItem(angelItem);
        }

        try {
            p.playSound(playerLocation, Sound.BLOCK_BAMBOO_PLACE, 1.0f, 1.0f);
        } catch (Exception ignored) {
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (targetBlock.getType() == Material.COBBLESTONE) {
                targetBlock.setType(Material.AIR);
                try {
                    p.playSound(playerLocation, Sound.BLOCK_BAMBOO_BREAK, 1.0f, 1.0f);
                    p.playEffect(playerLocation, Effect.ENDER_SIGNAL, null);
                } catch (Exception ignored) {
                }
            }
            angelBlockBufferList.remove(targetBlock);
        }, 200L);

        return true;
    }

    public static void cleanup() {
        for (Block angelBlock : angelBlockBufferList) {
            if (angelBlock.getType() == Material.COBBLESTONE) {
                angelBlock.setType(Material.AIR);
            }
        }
        angelBlockBufferList.clear();
    }
}

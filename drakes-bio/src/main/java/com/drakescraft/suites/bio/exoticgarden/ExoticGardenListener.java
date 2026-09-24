package com.drakescraft.suites.bio.exoticgarden;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Listener de interacción, cosecha y anti-dupe para Exotic Garden.
 */
public class ExoticGardenListener implements Listener {

    private final ExoticGardenModule module;
    private final ExoticGardenRegistry registry;
    private final int crookLeafDropChance;
    private final boolean preventUnnaturalLeafDrops;

    public ExoticGardenListener(ExoticGardenModule module, ExoticGardenRegistry registry, int crookLeafDropChance, boolean preventUnnaturalLeafDrops) {
        this.module = module;
        this.registry = registry;
        this.crookLeafDropChance = crookLeafDropChance;
        this.preventUnnaturalLeafDrops = preventUnnaturalLeafDrops;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = tool.getItemMeta();
        if (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("Crook")) {
            if (Tag.LEAVES.isTagged(block.getType())) {
                if (ThreadLocalRandom.current().nextInt(100) < crookLeafDropChance) {
                    Material saplingMaterial = getCorrespondingSapling(block.getType());
                    if (saplingMaterial != null) {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(saplingMaterial, 1));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        // Si se usa polvo de hueso en plantas exóticas, aplicar aceleración de crecimiento
        if (handItem.getType() == Material.BONE_MEAL && module.isBonemealEnabled()) {
            if (Tag.CROPS.isTagged(block.getType()) || Tag.SAPLINGS.isTagged(block.getType())) {
                block.getWorld().spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, block.getLocation().add(0.5, 0.5, 0.5), 8, 0.3, 0.3, 0.3);
            }
        }
    }

    private Material getCorrespondingSapling(Material leaf) {
        return switch (leaf) {
            case OAK_LEAVES -> Material.OAK_SAPLING;
            case SPRUCE_LEAVES -> Material.SPRUCE_SAPLING;
            case BIRCH_LEAVES -> Material.BIRCH_SAPLING;
            case JUNGLE_LEAVES -> Material.JUNGLE_SAPLING;
            case ACACIA_LEAVES -> Material.ACACIA_SAPLING;
            case DARK_OAK_LEAVES -> Material.DARK_OAK_SAPLING;
            case CHERRY_LEAVES -> Material.CHERRY_SAPLING;
            case MANGROVE_LEAVES -> Material.MANGROVE_PROPAGULE;
            default -> Material.OAK_SAPLING;
        };
    }
}

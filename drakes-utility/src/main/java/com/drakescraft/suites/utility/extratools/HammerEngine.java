package com.drakescraft.suites.utility.extratools;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Motor de pulverización y excavación de área 3x3 del Martillo de ExtraTools.
 */
public class HammerEngine {

    /**
     * Obtiene el ítem pulverizado resultante al picar un bloque con el Martillo.
     */
    @Nullable
    public static ItemStack getPulverizedDrop(@Nonnull Material material) {
        return switch (material) {
            case STONE, GRANITE, DIORITE, ANDESITE, COBBLESTONE -> new ItemStack(Material.GRAVEL);
            case GRAVEL, GRASS_BLOCK, DIRT, COARSE_DIRT, PODZOL -> new ItemStack(Material.SAND);
            case IRON_ORE, DEEPSLATE_IRON_ORE -> createSlimefunDrop(Material.IRON_NUGGET, "IRON_DUST", "Polvo de Hierro");
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> createSlimefunDrop(Material.GOLD_NUGGET, "GOLD_DUST", "Polvo de Oro");
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> createSlimefunDrop(Material.COPPER_INGOT, "COPPER_DUST", "Polvo de Cobre");
            case NETHERRACK -> new ItemStack(Material.SOUL_SAND);
            default -> null;
        };
    }

    /**
     * Calcula los 9 bloques del área 3x3 ortogonal a la cara golpeada.
     */
    @Nonnull
    public static List<Block> calculate3x3Grid(@Nonnull Block centerBlock, @Nonnull BlockFace hitFace) {
        List<Block> blocks = new ArrayList<>(9);
        int cx = centerBlock.getX();
        int cy = centerBlock.getY();
        int cz = centerBlock.getZ();
        var world = centerBlock.getWorld();

        if (hitFace == BlockFace.UP || hitFace == BlockFace.DOWN) {
            // Plano horizontal (X-Z)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    blocks.add(world.getBlockAt(cx + dx, cy, cz + dz));
                }
            }
        } else if (hitFace == BlockFace.NORTH || hitFace == BlockFace.SOUTH) {
            // Plano vertical transversal (X-Y)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    blocks.add(world.getBlockAt(cx + dx, cy + dy, cz));
                }
            }
        } else {
            // Plano vertical longitudinal (Z-Y)
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    blocks.add(world.getBlockAt(cx, cy + dy, cz + dz));
                }
            }
        }

        return blocks;
    }

    private static ItemStack createSlimefunDrop(Material mat, String sfId, String name) {
        ItemStack item = new ItemStack(mat);
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e" + name);
            SuiteItemPdcBridge.setSlimefunId(meta, sfId);
            item.setItemMeta(meta);
        }
        return item;
    }
}

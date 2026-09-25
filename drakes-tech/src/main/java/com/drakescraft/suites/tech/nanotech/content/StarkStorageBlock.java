package com.drakescraft.suites.tech.nanotech.content;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.List;

/** Standard Slimefun inventory contract consumed by Cargo and adjacent Networks accessors. */
public final class StarkStorageBlock extends SlimefunItem {
    private static final int[] STORAGE_SLOTS = IntStream.range(0, 54).toArray();

    public StarkStorageBlock(ItemGroup group, SlimefunItemStack item, ItemStack[] recipe) {
        super(group, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent event, ItemStack brokenItem, List<ItemStack> drops) {
                dropContents(event.getBlock(), drops);
            }

            @Override
            public void onExplode(Block block, List<ItemStack> drops) {
                dropContents(block, drops);
            }
        });
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(getId(), getItemName()) {
            @Override public void init() { setSize(54); }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return StarkStorageBlock.this.canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return STORAGE_SLOTS;
            }
        };
    }

    private static void dropContents(Block block, List<ItemStack> drops) {
        BlockMenu menu = BlockStorage.getInventory(block);
        if (menu == null) return;
        for (int slot : STORAGE_SLOTS) {
            ItemStack stack = menu.getItemInSlot(slot);
            if (stack != null && !stack.getType().isAir()) drops.add(stack.clone());
        }
    }
}

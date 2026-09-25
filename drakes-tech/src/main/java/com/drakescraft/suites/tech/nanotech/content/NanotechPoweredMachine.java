package com.drakescraft.suites.tech.nanotech.content;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/** A real energy-consuming batch processor shared by every catalog machine. */
public final class NanotechPoweredMachine extends SlimefunItem implements EnergyNetComponent {
    private static final int INPUT = 20;
    private static final int CONTROL = 22;
    private static final int OUTPUT = 24;
    private final MachineDefinition definition;
    private final String inputId;
    private final ItemStack output;

    public NanotechPoweredMachine(ItemGroup group, SlimefunItemStack item, ItemStack[] recipe,
                                  MachineDefinition definition, String inputId, ItemStack output) {
        super(group, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.definition = definition;
        this.inputId = inputId;
        this.output = output;
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent event, ItemStack broken, List<ItemStack> drops) {
                BlockMenu menu = BlockStorage.getInventory(event.getBlock());
                if (menu != null) menu.dropItems(event.getBlock().getLocation(), INPUT, OUTPUT);
            }
        });
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                setSize(45);
                drawBackground(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,23,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44});
                addItem(CONTROL, statusItem(), (player, slot, item, action) -> false);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block block) {
                menu.addMenuClickHandler(CONTROL, (player, slot, item, action) -> {
                    process(menu, player);
                    return false;
                });
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return NanotechPoweredMachine.this.canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow == ItemTransportFlow.INSERT ? new int[]{INPUT} : new int[]{OUTPUT};
            }
        };
    }

    private void process(BlockMenu menu, Player player) {
        ItemStack input = menu.getItemInSlot(INPUT);
        if (!matches(input, inputId)) {
            player.sendMessage("§6DrakesNanotech §8· §cRequired input: §f" + inputId);
            return;
        }
        ItemStack result = output.clone();
        if (!menu.fits(result, OUTPUT)) {
            player.sendMessage("§6DrakesNanotech §8· §cOutput slot is full or contains another item.");
            return;
        }
        long calculated = (long) definition.energyPerTick() * definition.seconds() * 20L;
        int energy = (int) Math.min(definition.buffer(), Math.min(Integer.MAX_VALUE, calculated));
        if (getCharge(menu.getLocation()) < energy) {
            player.sendMessage("§6DrakesNanotech §8· §cInsufficient energy. Batch requires §f" + energy + " J§c.");
            return;
        }
        removeCharge(menu.getLocation(), energy);
        input.setAmount(input.getAmount() - 1);
        menu.pushItem(result, OUTPUT);
        player.getWorld().playSound(player, org.bukkit.Sound.BLOCK_BEACON_POWER_SELECT, 0.9F, 1.35F);
        player.sendMessage("§6DrakesNanotech §8· §aBatch complete: §f" + definition.output());
    }

    private ItemStack statusItem() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        var meta = item.getItemMeta();
        meta.setDisplayName("§aRun secured batch");
        meta.setLore(List.of("§7Input: §f" + definition.input(), "§7Output: §f" + definition.output(),
                "§7Draw contract: §b" + definition.energyPerTick() + " J/t for " + definition.seconds() + "s",
                "§7Buffer: §b" + definition.buffer() + " J", "", "§eClick to process"));
        item.setItemMeta(meta);
        return item;
    }

    private static boolean matches(ItemStack stack, String id) {
        if (stack == null || stack.getType().isAir()) return false;
        SlimefunItem sf = SlimefunItem.getByItem(stack);
        return sf != null ? sf.getId().equals(id) : stack.getType().name().equals(id);
    }

    @Nonnull @Override public EnergyNetComponentType getEnergyComponentType() { return EnergyNetComponentType.CONSUMER; }
    @Override public int getCapacity() { return definition.buffer(); }
}

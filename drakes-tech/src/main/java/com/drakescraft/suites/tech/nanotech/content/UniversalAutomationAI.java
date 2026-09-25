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
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Recipe-aware, powered and restart-resumable automation for registered Slimefun items. */
public final class UniversalAutomationAI extends SlimefunItem implements EnergyNetComponent {
    private static final int TARGET = 13;
    private static final int STATUS = 22;
    private static final int OUTPUT = 31;
    private static final int[] INPUTS = {36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};
    private static final String TARGET_KEY = "dn_ai_target";
    private static final String PROGRESS_KEY = "dn_ai_progress";
    private static final String REQUIRED_KEY = "dn_ai_required";
    private static final String PHASE_KEY = "dn_ai_phase";
    private final org.bukkit.plugin.java.JavaPlugin plugin;

    public UniversalAutomationAI(org.bukkit.plugin.java.JavaPlugin plugin, ItemGroup group, SlimefunItemStack item, ItemStack[] recipe) {
        super(group, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.plugin = plugin;
        addItemHandler(new BlockTicker() {
            @Override public void tick(@Nonnull Block block, @Nonnull SlimefunItem item, @Nonnull Config data) { tickJob(block); }
            @Override public boolean isSynchronized() { return true; }
        }, new BlockBreakHandler(false, false) {
            @Override public void onPlayerBreak(BlockBreakEvent event, ItemStack broken, List<ItemStack> drops) {
                BlockMenu menu = BlockStorage.getInventory(event.getBlock());
                if (menu != null) {
                    menu.dropItems(event.getBlock().getLocation(), TARGET, OUTPUT);
                    menu.dropItems(event.getBlock().getLocation(), INPUTS);
                }
            }
        });
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(getId(), getItemName()) {
            @Override public void init() {
                setSize(54);
                drawBackground(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29,30,32,33,34,35});
                addItem(STATUS, status("§7Awaiting target template", List.of("§8The target is analyzed and never consumed.")),
                        (player, slot, item, action) -> false);
            }
            @Override public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return UniversalAutomationAI.this.canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }
            @Override public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow == ItemTransportFlow.INSERT ? INPUTS : new int[]{OUTPUT};
            }
        };
    }

    private void tickJob(Block block) {
        BlockMenu menu = BlockStorage.getInventory(block);
        if (menu == null) return;
        SlimefunItem target = SlimefunItem.getByItem(menu.getItemInSlot(TARGET));
        if (target == null || target == this || target.getRecipe().length == 0) {
            clearJob(block);
            menu.replaceExistingItem(STATUS, status("§7Awaiting analyzable target", List.of("§8Place a registered Slimefun item in the template slot.")));
            return;
        }
        Map<ItemKey, Requirement> requirements = analyze(target.getRecipe());
        if (requirements.isEmpty()) {
            menu.replaceExistingItem(STATUS, status("§cUnsupported recipe", List.of("§8The target exposes no concrete ingredients.")));
            return;
        }
        if (!target.getId().equals(info(block, TARGET_KEY))) initialize(block, target, requirements);
        long required = number(info(block, REQUIRED_KEY), requiredTicks(target, requirements));
        long progress = number(info(block, PROGRESS_KEY), 0L);
        int draw = energyPerTick(target, requirements);
        int tickRate = Math.max(1, Slimefun.getTickerTask().getTickRate());
        if (!hasMaterials(menu, requirements)) {
            menu.replaceExistingItem(STATUS, jobStatus(target, progress, required, draw, "§cWAITING FOR MATERIALS"));
            return;
        }
        if (!menu.fits(target.getRecipeOutput(), OUTPUT)) {
            menu.replaceExistingItem(STATUS, jobStatus(target, progress, required, draw, "§cOUTPUT BLOCKED"));
            return;
        }
        int charge = draw * tickRate;
        if (getCharge(block.getLocation()) < charge) {
            menu.replaceExistingItem(STATUS, jobStatus(target, progress, required, draw, "§cINSUFFICIENT ENERGY"));
            return;
        }
        removeCharge(block.getLocation(), charge);
        progress = Math.min(required, progress + tickRate);
        BlockStorage.addBlockInfo(block, PROGRESS_KEY, Long.toString(progress));
        BlockStorage.addBlockInfo(block, PHASE_KEY, "RUNNING");
        if (progress >= required) commitNoLoss(block, menu, target, requirements);
        else menu.replaceExistingItem(STATUS, jobStatus(target, progress, required, draw, "§aPROCESSING"));
    }

    /** Output-first main-thread commit prevents a crash from consuming materials without an item. */
    private void commitNoLoss(Block block, BlockMenu menu, SlimefunItem target, Map<ItemKey, Requirement> requirements) {
        BlockStorage.addBlockInfo(block, PHASE_KEY, "COMMITTING_OUTPUT_FIRST");
        menu.pushItem(target.getRecipeOutput(), OUTPUT);
        consume(menu, requirements);
        BlockStorage.addBlockInfo(block, PROGRESS_KEY, "0");
        BlockStorage.addBlockInfo(block, PHASE_KEY, "IDLE");
        menu.replaceExistingItem(STATUS, status("§aAutomation completed", List.of("§7Output committed before material debit.", "§8Template preserved for the next batch.")));
        block.getWorld().playSound(block.getLocation(), org.bukkit.Sound.BLOCK_BEACON_ACTIVATE, 1.1F, 1.45F);
    }

    private void initialize(Block block, SlimefunItem target, Map<ItemKey, Requirement> requirements) {
        BlockStorage.addBlockInfo(block, TARGET_KEY, target.getId());
        BlockStorage.addBlockInfo(block, PROGRESS_KEY, "0");
        BlockStorage.addBlockInfo(block, REQUIRED_KEY, Long.toString(requiredTicks(target, requirements)));
        BlockStorage.addBlockInfo(block, PHASE_KEY, "ANALYZED");
    }

    private void clearJob(Block block) {
        BlockStorage.addBlockInfo(block, TARGET_KEY, null);
        BlockStorage.addBlockInfo(block, PROGRESS_KEY, "0");
        BlockStorage.addBlockInfo(block, REQUIRED_KEY, "0");
        BlockStorage.addBlockInfo(block, PHASE_KEY, "IDLE");
    }

    private long requiredTicks(SlimefunItem target, Map<ItemKey, Requirement> requirements) {
        String id = target.getId().toUpperCase(Locale.ROOT);
        long seconds;
        if (id.contains("INFINITY") || id.contains("SINGULARITY") || id.contains("VOID_SWORD")) {
            seconds = Math.max(86_400L, plugin.getConfig().getLong("automation-ai.infinity-minimum-seconds", 259_200L));
        } else {
            long weight = requirements.values().stream().mapToLong(r -> materialWeight(r.sample()) * r.amount()).sum();
            seconds = Math.max(30L, Math.min(plugin.getConfig().getLong("automation-ai.maximum-seconds", 604_800L), weight * 30L));
        }
        return seconds * 20L;
    }

    private static int energyPerTick(SlimefunItem target, Map<ItemKey, Requirement> requirements) {
        long weight = requirements.values().stream().mapToLong(r -> materialWeight(r.sample()) * r.amount()).sum();
        if (target.getId().toUpperCase(Locale.ROOT).contains("INFINITY")) weight *= 64L;
        return (int) Math.max(256L, Math.min(131_072L, weight * 128L));
    }

    private static int materialWeight(ItemStack item) {
        SlimefunItem sf = SlimefunItem.getByItem(item);
        if (sf != null) {
            String id = sf.getId().toUpperCase(Locale.ROOT);
            if (id.contains("INFINITY") || id.contains("SINGULARITY")) return 1024;
            if (id.contains("COSMIC") || id.contains("URU") || id.contains("VOID")) return 256;
            return 16;
        }
        return switch (item.getType()) {
            case NETHER_STAR, DRAGON_EGG -> 512;
            case NETHERITE_BLOCK, NETHERITE_INGOT -> 128;
            case DIAMOND_BLOCK, EMERALD_BLOCK -> 48;
            case DIAMOND, EMERALD -> 12;
            default -> 1;
        };
    }

    static Map<ItemKey, Requirement> analyze(ItemStack[] recipe) {
        Map<ItemKey, Requirement> result = new LinkedHashMap<>();
        for (ItemStack ingredient : recipe) {
            if (ingredient == null || ingredient.getType().isAir()) continue;
            ItemKey key = ItemKey.of(ingredient);
            result.compute(key, (ignored, old) -> old == null ? new Requirement(ingredient.clone(), ingredient.getAmount())
                    : new Requirement(old.sample(), old.amount() + ingredient.getAmount()));
        }
        return result;
    }

    private static boolean hasMaterials(BlockMenu menu, Map<ItemKey, Requirement> requirements) {
        for (Map.Entry<ItemKey, Requirement> requirement : requirements.entrySet()) {
            int found = 0;
            for (int slot : INPUTS) {
                ItemStack stack = menu.getItemInSlot(slot);
                if (requirement.getKey().matches(stack)) found += stack.getAmount();
            }
            if (found < requirement.getValue().amount()) return false;
        }
        return true;
    }

    private static void consume(BlockMenu menu, Map<ItemKey, Requirement> requirements) {
        for (Map.Entry<ItemKey, Requirement> requirement : requirements.entrySet()) {
            int remaining = requirement.getValue().amount();
            for (int slot : INPUTS) {
                ItemStack stack = menu.getItemInSlot(slot);
                if (!requirement.getKey().matches(stack)) continue;
                int taken = Math.min(stack.getAmount(), remaining);
                stack.setAmount(stack.getAmount() - taken);
                if ((remaining -= taken) == 0) break;
            }
        }
    }

    private ItemStack jobStatus(SlimefunItem target, long progress, long required, int draw, String state) {
        double percent = required == 0 ? 0D : progress * 100D / required;
        long remaining = Math.max(0L, (required - progress) / 20L);
        return status("§bUniversal Automation AI", List.of("§7State: " + state, "§7Target: §f" + target.getId(),
                String.format(Locale.ROOT, "§7Progress: §b%.3f%%", percent), "§7Powered time left: §f" + duration(remaining),
                "§7Draw: §e" + draw + " J/t", "§8Progress persists across restarts."));
    }

    private static ItemStack status(String name, List<String> lore) {
        ItemStack item = new ItemStack(Material.CALIBRATED_SCULK_SENSOR);
        var meta = item.getItemMeta(); meta.setDisplayName(name); meta.setLore(new ArrayList<>(lore)); item.setItemMeta(meta); return item;
    }
    private static String duration(long seconds) { return seconds / 86_400L + "d " + seconds % 86_400L / 3_600L + "h " + seconds % 3_600L / 60L + "m"; }
    private static String info(Block block, String key) { return BlockStorage.getLocationInfo(block.getLocation(), key); }
    private static long number(String text, long fallback) { try { return text == null ? fallback : Long.parseLong(text); } catch (NumberFormatException ignored) { return fallback; } }

    record Requirement(ItemStack sample, int amount) {}
    record ItemKey(String slimefunId, Material material) {
        static ItemKey of(ItemStack item) { SlimefunItem sf = SlimefunItem.getByItem(item); return new ItemKey(sf == null ? "" : sf.getId(), item.getType()); }
        boolean matches(ItemStack item) { if (item == null || item.getType().isAir()) return false; SlimefunItem sf = SlimefunItem.getByItem(item); return slimefunId.isEmpty() ? sf == null && item.getType() == material : sf != null && sf.getId().equals(slimefunId); }
    }

    @Nonnull @Override public EnergyNetComponentType getEnergyComponentType() { return EnergyNetComponentType.CONSUMER; }
    @Override public int getCapacity() { return 2_000_000_000; }
}

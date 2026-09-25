package com.drakescraft.suites.server.vaults;

import org.bukkit.ChatColor;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;

/** Detects Slimefun backpacks, including backpacks hidden inside item containers. */
public final class BackpackDetector {
    private static final String LORE_PREFIX = ChatColor.GRAY + "ID: ";
    private static boolean reflectionInitialized;
    private static Method getByItem;
    private static Class<?> backpackClass;

    private final boolean inspectContainers;
    private final int maximumDepth;

    public BackpackDetector(boolean inspectContainers, int maximumDepth) {
        this.inspectContainers = inspectContainers;
        this.maximumDepth = Math.max(1, maximumDepth);
    }

    public boolean containsBackpack(ItemStack item) {
        return find(item, 0);
    }

    private boolean find(ItemStack item, int depth) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        if (isBackpack(item)) {
            return true;
        }
        if (!inspectContainers || depth >= maximumDepth || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof BlockStateMeta stateMeta && stateMeta.hasBlockState()
                && stateMeta.getBlockState() instanceof ShulkerBox shulkerBox) {
            for (ItemStack nested : shulkerBox.getInventory().getContents()) {
                if (find(nested, depth + 1)) {
                    return true;
                }
            }
        }
        if (meta instanceof BundleMeta bundleMeta) {
            for (ItemStack nested : bundleMeta.getItems()) {
                if (find(nested, depth + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBackpack(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        Boolean fromSlimefun = lookupSlimefun(item);
        return fromSlimefun != null ? fromSlimefun : hasBackpackLore(item);
    }

    private static Boolean lookupSlimefun(ItemStack item) {
        initializeReflection();
        if (getByItem == null || backpackClass == null) {
            return null;
        }
        try {
            Object slimefunItem = getByItem.invoke(null, item);
            return slimefunItem != null && backpackClass.isInstance(slimefunItem);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return null;
        }
    }

    static boolean hasBackpackLore(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        List<String> lore = item.getItemMeta().getLore();
        return lore != null && lore.stream().anyMatch(line -> line != null && line.startsWith(LORE_PREFIX));
    }

    private static synchronized void initializeReflection() {
        if (reflectionInitialized) {
            return;
        }
        reflectionInitialized = true;
        for (String base : List.of("com.github.drakescraft_labs.slimefun4", "io.github.thebusybiscuit.slimefun4")) {
            try {
                Class<?> slimefunItem = Class.forName(base + ".api.items.SlimefunItem");
                backpackClass = Class.forName(base + ".implementation.items.backpacks.SlimefunBackpack");
                getByItem = slimefunItem.getMethod("getByItem", ItemStack.class);
                return;
            } catch (ReflectiveOperationException ignored) {
                getByItem = null;
                backpackClass = null;
            }
        }
    }
}

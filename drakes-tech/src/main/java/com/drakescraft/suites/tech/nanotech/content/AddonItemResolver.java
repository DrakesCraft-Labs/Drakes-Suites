package com.drakescraft.suites.tech.nanotech.content;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** Resolves real registered Slimefun components without replacing them with vanilla lookalikes. */
final class AddonItemResolver {
    private final org.bukkit.plugin.Plugin plugin;
    private final Map<String, Integer> resolvedByAddon = new LinkedHashMap<>();

    AddonItemResolver(org.bukkit.plugin.Plugin plugin) {
        this.plugin = plugin;
    }

    /** Returns the first installed candidate, allowing recipes to degrade only to another SF component. */
    ItemStack require(String purpose, String... candidateIds) {
        for (String id : candidateIds) {
            SlimefunItem item = SlimefunItem.getById(id);
            if (item != null) {
                String addon = item.getAddon() == null ? "Slimefun" : item.getAddon().getName();
                resolvedByAddon.merge(addon, 1, Integer::sum);
                return item.getItem().clone();
            }
        }
        throw new IllegalStateException("Missing Slimefun ingredient for " + purpose + ": "
                + Arrays.toString(candidateIds));
    }

    /** Emits an auditable startup summary so cross-addon progression can never be merely descriptive. */
    void logSummary() {
        plugin.getLogger().info("Cross-addon recipe ingredients resolved: " + resolvedByAddon);
    }
}

package com.drakescraft.suites.utility.chestterminal;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Motor de indexación, ordenamiento y protección anti-duplicación para ChestTerminal.
 */
public class ChestTerminalIndex {

    public enum SortType {
        NAME,
        AMOUNT,
        ID
    }

    private final Map<UUID, Long> lastClickMap = new ConcurrentHashMap<>();
    private final int throttleMs;
    private final boolean ghostClearing;

    public ChestTerminalIndex(int throttleMs, boolean ghostClearing) {
        this.throttleMs = Math.max(50, throttleMs);
        this.ghostClearing = ghostClearing;
    }

    /**
     * Comprueba si el jugador está haciendo clics demasiado rápido (anti-dupe).
     */
    public boolean isThrottled(UUID playerUuid) {
        long now = System.currentTimeMillis();
        Long last = lastClickMap.get(playerUuid);
        if (last != null && (now - last) < throttleMs) {
            return true;
        }
        lastClickMap.put(playerUuid, now);
        return false;
    }

    /**
     * Limpia y unifica una lista de items conectados a los cofres.
     */
    public List<ItemStack> cleanAndFilter(List<ItemStack> rawItems, String searchQuery, SortType sortType) {
        if (rawItems == null) return Collections.emptyList();

        List<ItemStack> result = new ArrayList<>();
        String query = (searchQuery != null) ? searchQuery.toLowerCase().trim() : "";

        for (ItemStack item : rawItems) {
            if (item == null || item.getType().isAir()) continue;
            if (ghostClearing && item.getAmount() <= 0) continue;

            if (!query.isEmpty()) {
                String name = getItemSearchName(item).toLowerCase();
                String sfId = SuiteItemPdcBridge.getSlimefunId(item);
                boolean matchesName = name.contains(query);
                boolean matchesId = sfId != null && sfId.toLowerCase().contains(query);
                boolean matchesMat = item.getType().name().toLowerCase().contains(query);

                if (!matchesName && !matchesId && !matchesMat) {
                    continue;
                }
            }

            result.add(item.clone());
        }

        // Ordenamiento
        Comparator<ItemStack> comparator;
        switch (sortType != null ? sortType : SortType.NAME) {
            case AMOUNT -> comparator = Comparator.comparingInt(ItemStack::getAmount).reversed();
            case ID -> comparator = Comparator.comparing(this::getItemSortId);
            default -> comparator = Comparator.comparing(this::getItemSearchName);
        }
        result.sort(comparator);

        return result;
    }

    private String getItemSearchName(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                return ChatColor.stripColor(meta.getDisplayName());
            }
        }
        return item.getType().name();
    }

    private String getItemSortId(ItemStack item) {
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId != null) return sfId;
        return item.getType().name();
    }

    public void clearPlayerThrottle(UUID playerUuid) {
        lastClickMap.remove(playerUuid);
    }
}

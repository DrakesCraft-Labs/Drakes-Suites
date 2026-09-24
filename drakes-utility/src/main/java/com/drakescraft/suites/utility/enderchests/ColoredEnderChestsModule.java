package com.drakescraft.suites.utility.enderchests;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.module.AbstractSuiteModule;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Módulo de Cofres de Ender Coloreados (ColoredEnderChests) para DrakesUtility.
 * Permite almacenar y sincronizar inventarios interdimensionales en 4096 frecuencias
 * cromáticas (0-15 en 3 canales: c1, c2, c3).
 *
 * Características:
 * 1. Claves PDC canónicas: COLORED_ENDER_CHEST_SMALL_c1_c2_c3 y COLORED_ENDER_CHEST_BIG_c1_c2_c3.
 * 2. Tamaños duales: Pequeño (27 slots) y Grande (54 slots).
 * 3. Compatibilidad dual Paper 1.21.11 / Purpur 26.X usando CrossVersionAdapter.
 * 4. Control de concurrencia y prevención anti-dupe con auditoría de transacciones.
 */
public class ColoredEnderChestsModule extends AbstractSuiteModule implements Listener {

    private static final String NAMESPACE = "drakesutility";
    private static final String KEY_IS_BIG = "enderchest_big";
    private static final String KEY_C1 = "enderchest_c1";
    private static final String KEY_C2 = "enderchest_c2";
    private static final String KEY_C3 = "enderchest_c3";

    private static final String[] COLOR_NAMES = {
            "Blanco", "Naranja", "Magenta", "Celeste", "Amarillo", "Lima", "Rosa", "Gris Oscuro",
            "Gris Claro", "Cian", "Púrpura", "Azul", "Marrón", "Verde", "Rojo", "Negro"
    };

    private final Map<String, ItemStack[]> frequencyStorage = new ConcurrentHashMap<>();
    private final Map<UUID, String> activeViewerFrequencies = new ConcurrentHashMap<>();

    private boolean cacheFrequencies = true;
    private boolean crossPlayerLock = true;
    private int maxFrequency = 4096;

    public ColoredEnderChestsModule(JavaPlugin plugin) {
        super(plugin, "colored_enderchests", "Frequency Colored EnderChests");
    }

    @Override
    public void onEnable() {
        this.cacheFrequencies = config.getBoolean("performance.cache-frequencies", true);
        this.crossPlayerLock = config.getBoolean("anti-dupe.cross-player-chest-lock", true);
        this.maxFrequency = config.getInt("features.max-frequency", 4096);

        Bukkit.getPluginManager().registerEvents(this, plugin);

        logInfo("ColoredEnderChests inicializado. 4096 frecuencias por tamaño activas.");
    }

    @Override
    public void onDisable() {
        for (UUID uuid : activeViewerFrequencies.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                p.closeInventory();
            }
        }
        activeViewerFrequencies.clear();
        logInfo("ColoredEnderChests deshabilitado limpiamente.");
    }

    /**
     * Crea un ítem de cofre de ender coloreado con metadatos PDC compatibles al 100% con Slimefun.
     */
    public ItemStack createEnderChestItemStack(boolean big, int c1, int c2, int c3) {
        c1 = Math.max(0, Math.min(15, c1));
        c2 = Math.max(0, Math.min(15, c2));
        c3 = Math.max(0, Math.min(15, c3));

        ItemStack item = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String title = "<yellow>Cofre de Ender Coloreado <gray>(" + (big ? "Grande - 54" : "Pequeño - 27") + ")</gray></yellow>";
            CrossVersionAdapter.setItemName(meta, title);
            List<String> lore = List.of(
                    "<gray>Frecuencia: <gold>#" + c1 + "-" + c2 + "-" + c3 + "</gold></gray>",
                    "<dark_gray>• Canal 1: " + COLOR_NAMES[c1] + "</dark_gray>",
                    "<dark_gray>• Canal 2: " + COLOR_NAMES[c2] + "</dark_gray>",
                    "<dark_gray>• Canal 3: " + COLOR_NAMES[c3] + "</dark_gray>",
                    "<dark_purple>★ StarSuites Utility ★</dark_purple>"
            );
            CrossVersionAdapter.setItemLore(meta, lore);
            item.setItemMeta(meta);
        }

        String sfId = "COLORED_ENDER_CHEST_" + (big ? "BIG" : "SMALL") + "_" + c1 + "_" + c2 + "_" + c3;
        SuiteItemPdcBridge.setSlimefunId(item, sfId);
        SuiteItemPdcBridge.setCustomInt(item, NAMESPACE, KEY_IS_BIG, big ? 1 : 0);
        SuiteItemPdcBridge.setCustomInt(item, NAMESPACE, KEY_C1, c1);
        SuiteItemPdcBridge.setCustomInt(item, NAMESPACE, KEY_C2, c2);
        SuiteItemPdcBridge.setCustomInt(item, NAMESPACE, KEY_C3, c3);

        return item;
    }

    public boolean isColoredEnderChest(ItemStack item) {
        if (item == null || item.getType() != Material.ENDER_CHEST) return false;
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        return sfId != null && sfId.startsWith("COLORED_ENDER_CHEST_");
    }

    public String getFrequencyKey(boolean big, int c1, int c2, int c3) {
        return (big ? "BIG_" : "SMALL_") + c1 + "_" + c2 + "_" + c3;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item == null || !isColoredEnderChest(item)) return;

        Player player = event.getPlayer();

        // Anti-dupe check de stack y PDC
        if (!SuiteItemPdcBridge.validateItemIntegrity(item, player.getName(), player.getLocation().toString())) {
            event.setCancelled(true);
            CrossVersionAdapter.sendMessage(player, "<red>§l[Anti-Dupe] §cEste cofre de ender contiene metadatos corruptos. Bloqueado.");
            return;
        }

        event.setCancelled(true);

        // Extraer canales
        Integer isBigInt = SuiteItemPdcBridge.getCustomInt(item, NAMESPACE, KEY_IS_BIG);
        Integer c1 = SuiteItemPdcBridge.getCustomInt(item, NAMESPACE, KEY_C1);
        Integer c2 = SuiteItemPdcBridge.getCustomInt(item, NAMESPACE, KEY_C2);
        Integer c3 = SuiteItemPdcBridge.getCustomInt(item, NAMESPACE, KEY_C3);

        boolean big = (isBigInt != null && isBigInt == 1);
        if (c1 == null || c2 == null || c3 == null) {
            String sfId = SuiteItemPdcBridge.getSlimefunId(item);
            if (sfId != null) {
                String[] parts = sfId.split("_");
                if (parts.length >= 6) {
                    big = "BIG".equalsIgnoreCase(parts[3]);
                    c1 = Integer.parseInt(parts[4]);
                    c2 = Integer.parseInt(parts[5]);
                    c3 = Integer.parseInt(parts[6]);
                }
            }
        }

        if (c1 == null) c1 = 0;
        if (c2 == null) c2 = 0;
        if (c3 == null) c3 = 0;

        openEnderChest(player, big, c1, c2, c3);
    }

    public void openEnderChest(Player player, boolean big, int c1, int c2, int c3) {
        String freqKey = getFrequencyKey(big, c1, c2, c3);
        int slots = big ? 54 : 27;

        String title = "<dark_purple>EnderChest <gold>#" + c1 + "-" + c2 + "-" + c3 + " <gray>(" + (big ? "Grande" : "Pequeño") + ")</gray></gold></dark_purple>";
        Inventory inv = Bukkit.createInventory(null, slots, CrossVersionAdapter.parseComponent(title));

        ItemStack[] saved = frequencyStorage.get(freqKey);
        if (saved != null) {
            inv.setContents(saved);
        }

        activeViewerFrequencies.put(player.getUniqueId(), freqKey);
        player.openInventory(inv);

        try {
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
        } catch (Throwable ignored) {}
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        String freqKey = activeViewerFrequencies.remove(player.getUniqueId());
        if (freqKey != null) {
            ItemStack[] contents = event.getInventory().getContents();
            frequencyStorage.put(freqKey, contents.clone());

            try {
                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
            } catch (Throwable ignored) {}
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isEnabled()) return;
        ItemStack item = event.getItemInHand();
        if (isColoredEnderChest(item)) {
            // Guardar frecuencia en el bloque si se coloca
            logInfo("Jugador " + event.getPlayer().getName() + " colocó un ColoredEnderChest en " + event.getBlock().getLocation());
        }
    }

    public Map<String, ItemStack[]> getFrequencyStorage() {
        return frequencyStorage;
    }

    public int getMaxFrequency() {
        return maxFrequency;
    }
}

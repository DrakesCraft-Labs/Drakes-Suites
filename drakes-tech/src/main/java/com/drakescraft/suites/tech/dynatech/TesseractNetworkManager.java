package com.drakescraft.suites.tech.dynatech;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor de canales y frecuencias de transmisión cuántica para Teseractos de DynaTech.
 */
public final class TesseractNetworkManager {

    public static final NamespacedKey KEY_TESSERACT_CHANNEL = new NamespacedKey("drakes", "tesseract_channel");
    public static final NamespacedKey KEY_LEGACY_CHANNEL = new NamespacedKey("dynatech", "tesseract_channel");

    // Registro en memoria de buffers de transporte por canal
    private final Map<String, Double> energyBuffers = new ConcurrentHashMap<>();

    public TesseractNetworkManager() {}

    /**
     * Vincula un canal de frecuencia a un vinculador o teseracto.
     */
    public boolean bindChannel(ItemStack item, String channelName) {
        if (item == null || !item.hasItemMeta() || channelName == null || channelName.trim().isEmpty()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_TESSERACT_CHANNEL, PersistentDataType.STRING, channelName.trim());
        pdc.set(KEY_LEGACY_CHANNEL, PersistentDataType.STRING, channelName.trim());

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        String channelLine = ChatColor.LIGHT_PURPLE + "Frecuencia Cuántica: " + ChatColor.WHITE + channelName.trim();
        boolean replaced = false;
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("Frecuencia Cuántica:")) {
                lore.set(i, channelLine);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            lore.add(channelLine);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return true;
    }

    /**
     * Extrae el canal cuántico configurado en un ítem.
     */
    public String getChannel(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(KEY_TESSERACT_CHANNEL, PersistentDataType.STRING)) {
            return pdc.get(KEY_TESSERACT_CHANNEL, PersistentDataType.STRING);
        }
        if (pdc.has(KEY_LEGACY_CHANNEL, PersistentDataType.STRING)) {
            return pdc.get(KEY_LEGACY_CHANNEL, PersistentDataType.STRING);
        }
        return null;
    }

    /**
     * Inyecta energía al buffer virtual del canal cuántico.
     */
    public double pushEnergy(String channel, double amount) {
        if (channel == null || amount <= 0) return 0;
        return energyBuffers.compute(channel, (k, current) -> (current == null ? 0 : current) + amount);
    }

    /**
     * Extrae energía del buffer virtual del canal cuántico.
     */
    public double pullEnergy(String channel, double maxAmount) {
        if (channel == null || maxAmount <= 0) return 0;
        Double current = energyBuffers.get(channel);
        if (current == null || current <= 0) return 0;

        double drawn = Math.min(current, maxAmount);
        energyBuffers.put(channel, current - drawn);
        return drawn;
    }

    public double getStoredEnergy(String channel) {
        if (channel == null) return 0;
        return energyBuffers.getOrDefault(channel, 0.0);
    }
}

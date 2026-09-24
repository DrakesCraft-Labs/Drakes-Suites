package com.drakescraft.suites.tech.dynatech;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Motor de control de vuelo sagrado, carga y descarga energética de la Gema de los Ángeles.
 */
public final class AngelGemEngine {

    public static final NamespacedKey KEY_CHARGE = new NamespacedKey("drakes", "angel_gem_charge");
    public static final NamespacedKey KEY_LEGACY_CHARGE = new NamespacedKey("slimefun", "charge");

    private final double maxCharge;
    private final double drainPerSecond;
    private final Set<UUID> activeFlyingPlayers = Collections.synchronizedSet(new HashSet<>());

    public AngelGemEngine(double maxCharge, double drainPerSecond) {
        this.maxCharge = maxCharge;
        this.drainPerSecond = drainPerSecond;
    }

    public double getMaxCharge() {
        return maxCharge;
    }

    public double getDrainPerSecond() {
        return drainPerSecond;
    }

    public double getCharge(ItemStack gem) {
        if (gem == null || !gem.hasItemMeta()) return 0;
        ItemMeta meta = gem.getItemMeta();
        if (meta == null) return 0;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (pdc.has(KEY_CHARGE, PersistentDataType.DOUBLE)) {
            Double val = pdc.get(KEY_CHARGE, PersistentDataType.DOUBLE);
            return val != null ? val : 0;
        }
        if (pdc.has(KEY_LEGACY_CHARGE, PersistentDataType.DOUBLE)) {
            Double val = pdc.get(KEY_LEGACY_CHARGE, PersistentDataType.DOUBLE);
            return val != null ? val : 0;
        }
        return 0;
    }

    public void setCharge(ItemStack gem, double charge) {
        if (gem == null || !gem.hasItemMeta()) return;
        ItemMeta meta = gem.getItemMeta();
        if (meta == null) return;

        double clamped = Math.max(0, Math.min(maxCharge, charge));
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_CHARGE, PersistentDataType.DOUBLE, clamped);
        pdc.set(KEY_LEGACY_CHARGE, PersistentDataType.DOUBLE, clamped);

        // Actualizar barra o lore de carga
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        String chargeLine = ChatColor.YELLOW + "Carga: " + ChatColor.WHITE + String.format("%.0f", clamped) + " / " + String.format("%.0f", maxCharge) + " J";
        boolean replaced = false;
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("Carga:")) {
                lore.set(i, chargeLine);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            lore.add(0, chargeLine);
        }
        meta.setLore(lore);
        gem.setItemMeta(meta);
    }

    public boolean drain(ItemStack gem, double amount) {
        double current = getCharge(gem);
        if (current < amount) return false;
        setCharge(gem, current - amount);
        return true;
    }

    public void recharge(ItemStack gem, double amount) {
        double current = getCharge(gem);
        setCharge(gem, current + amount);
    }

    /**
     * Alterna el estado de vuelo del jugador.
     */
    public boolean toggleFlight(Player player, ItemStack gem) {
        if (player == null || gem == null) return false;

        UUID uuid = player.getUniqueId();
        if (activeFlyingPlayers.contains(uuid)) {
            // Desactivar vuelo
            activeFlyingPlayers.remove(uuid);
            if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            player.sendMessage(ChatColor.YELLOW + "[DynaTech] Vuelo sagrado desactivado.");
            return false;
        } else {
            // Activar vuelo si tiene carga
            if (getCharge(gem) <= 0) {
                player.sendMessage(ChatColor.RED + "[DynaTech] La Gema de los Ángeles no tiene energía acumulada.");
                return false;
            }
            activeFlyingPlayers.add(uuid);
            player.setAllowFlight(true);
            player.sendMessage(ChatColor.AQUA + "[DynaTech] Vuelo sagrado activado. La gravedad se rinde ante ti.");
            return true;
        }
    }

    public boolean isFlying(UUID uuid) {
        return activeFlyingPlayers.contains(uuid);
    }

    public void disableFlight(Player player) {
        if (player == null) return;
        activeFlyingPlayers.remove(player.getUniqueId());
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}

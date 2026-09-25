package com.drakescraft.suites.tech.nanotech.gameplay;

import com.drakescraft.suites.tech.nanotech.content.NanotechContent;
import com.drakescraft.suites.tech.nanotech.protection.ProtectionGate;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/** Owner-bound, metered survival flight inside a ProtectionStones cuboid. */
public final class GodPrisonFieldService extends BukkitRunnable implements Listener {
    private record Field(ProtectionGate.OwnedRegion region, double originY, int cellsPerCycle) {}

    private final org.bukkit.plugin.Plugin plugin;
    private final NanotechContent content;
    private final ProtectionGate protections;
    private final Map<UUID, Field> active = new HashMap<>();
    private int ticks;

    public GodPrisonFieldService(org.bukkit.plugin.Plugin plugin, NanotechContent content, ProtectionGate protections) {
        this.plugin = plugin;
        this.content = content;
        this.protections = protections;
    }

    /** Toggles a field only for the owner of the ProtectionStone under their feet. */
    public void toggle(Player player) {
        if (active.remove(player.getUniqueId()) != null) {
            revoke(player, "§7God-Prison field disengaged.");
            return;
        }
        ProtectionGate.OwnedRegion region = protections.ownedProtection(player.getLocation(), player.getUniqueId());
        if (region == null) {
            player.sendMessage("§6DrakesNanotech §8· §cStand inside a ProtectionStone region that you own.");
            return;
        }
        long blocksPerCell = Math.max(512L, plugin.getConfig().getLong("god-prison.region-blocks-per-cell", 4096L));
        int tariff = (int) Math.max(1L, Math.min(64L, (region.volume() + blocksPerCell - 1L) / blocksPerCell));
        if (!consumeCells(player, tariff)) {
            player.sendMessage("§6DrakesNanotech §8· §cRequires " + tariff + " God-Prison Power Cell(s) to ignite this volume.");
            return;
        }
        active.put(player.getUniqueId(), new Field(region, player.getLocation().getY(), tariff));
        player.setAllowFlight(true);
        player.getWorld().playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 1.2F, 0.7F);
        player.sendMessage("§6DrakesNanotech §8· §dGraviton cage online. §7Altitude envelope: §b+20 / -10 blocks§7.");
    }

    @Override
    public void run() {
        try {
            ticks++;
            for (Map.Entry<UUID, Field> entry : new HashMap<>(active).entrySet()) {
                Player player = plugin.getServer().getPlayer(entry.getKey());
                Field field = entry.getValue();
                if (player == null || !player.isOnline() || !field.region().contains(player.getLocation())
                        || player.getLocation().getY() > field.originY() + 20D
                        || player.getLocation().getY() < field.originY() - 10D) {
                    active.remove(entry.getKey());
                    if (player != null) revoke(player, "§cThe God-Prison revoked flight: boundary exceeded.");
                    continue;
                }
                if (ticks % Math.max(20, plugin.getConfig().getInt("god-prison.cell-cycle-seconds", 15) * 20) == 0
                        && !consumeCells(player, field.cellsPerCycle())) {
                    active.remove(entry.getKey());
                    revoke(player, "§cThe God-Prison exhausted its power cells.");
                    continue;
                }
                render(player);
            }
        } catch (RuntimeException error) {
            plugin.getLogger().log(Level.WARNING, "God-Prison field tick aborted safely", error);
        }
    }

    public void shutdown() {
        for (UUID uuid : active.keySet()) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) revoke(player, "§7God-Prison field offline.");
        }
        active.clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (active.remove(event.getPlayer().getUniqueId()) != null) {
            revoke(event.getPlayer(), "§7God-Prison field offline.");
        }
    }

    private void render(Player player) {
        if (!player.isFlying()) return;
        var base = player.getLocation().add(0, 0.12, 0);
        Particle.DustOptions cyan = new Particle.DustOptions(Color.fromRGB(70, 225, 255), 1.15F);
        for (int point = 0; point < 12; point++) {
            double angle = (ticks * 0.25D) + Math.PI * 2D * point / 12D;
            player.getWorld().spawnParticle(Particle.DUST,
                    base.clone().add(Math.cos(angle) * 0.65D, 0.08D, Math.sin(angle) * 0.65D), 1, 0, 0, 0, cyan);
        }
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, base, 3, 0.28, 0.04, 0.28, 0.01);
    }

    private boolean consumeCells(Player player, int amount) {
        int available = 0;
        for (ItemStack stack : player.getInventory().getStorageContents()) {
            if (content.is(stack, "GOD_PRISON_POWER_CELL")) available += stack.getAmount();
        }
        if (available < amount) return false;
        int remaining = amount;
        for (ItemStack stack : player.getInventory().getStorageContents()) {
            if (!content.is(stack, "GOD_PRISON_POWER_CELL")) continue;
            int taken = Math.min(stack.getAmount(), remaining);
            stack.setAmount(stack.getAmount() - taken);
            if ((remaining -= taken) == 0) break;
        }
        return true;
    }

    private void revoke(Player player, String message) {
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
        player.sendActionBar(message);
        player.getWorld().playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 0.8F, 0.65F);
    }
}

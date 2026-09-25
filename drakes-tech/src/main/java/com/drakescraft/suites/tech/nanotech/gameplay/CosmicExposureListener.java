package com.drakescraft.suites.tech.nanotech.gameplay;

import com.drakescraft.suites.tech.nanotech.content.NanotechContent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Escalating bare-stone exposure with a carrier-only, block-safe terminal event. */
public final class CosmicExposureListener implements Listener {
    private final org.bukkit.plugin.Plugin plugin;
    private final NanotechContent content;
    private final Map<UUID, Integer> exposure = new HashMap<>();
    private final BukkitTask task;

    public CosmicExposureListener(org.bukkit.plugin.Plugin plugin, NanotechContent content) {
        this.plugin = plugin;
        this.content = content;
        this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, 20L, 20L);
    }

    /** Updates only online carriers; no entity scans or chunk loads occur. */
    private void tick() {
        if (!plugin.getConfig().getBoolean("cosmic-exposure.enabled", true)) return;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!content.isBareStone(player.getInventory().getItemInMainHand())) {
                exposure.remove(player.getUniqueId());
                continue;
            }
            int seconds = exposure.merge(player.getUniqueId(), 1, Integer::sum);
            String stone = content.idOf(player.getInventory().getItemInMainHand()).replace('_', ' ');
            player.sendActionBar("§5COSMIC EXPOSURE §8· §f" + stone + " §8· §c" + seconds + "s");
            player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0, 1, 0), Math.min(30, 4 + seconds), 0.35, 0.7, 0.35, 0.02);
            if (seconds >= 4) player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 45, Math.min(3, seconds / 5), false, false));
            if (seconds == 12) player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1F, 0.5F);
            if (seconds >= 20) disintegrateCarrier(player);
        }
    }

    private void disintegrateCarrier(Player player) {
        exposure.remove(player.getUniqueId());
        player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1, 0), 160, 0.55, 1.0, 0.55, 0.035);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.7F, 0.45F);
        // Damage is deliberately carrier-only and cannot create a block explosion or chain kill.
        player.damage(Math.max(1D, player.getHealth() - 1D));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2, false, true));
        player.sendMessage("§6DrakesNanotech §8· §cThe exposed artifact fractured your body. Seal it in a Cosmic Capsule.");
    }

    @EventHandler public void onQuit(PlayerQuitEvent event) { exposure.remove(event.getPlayer().getUniqueId()); }
}

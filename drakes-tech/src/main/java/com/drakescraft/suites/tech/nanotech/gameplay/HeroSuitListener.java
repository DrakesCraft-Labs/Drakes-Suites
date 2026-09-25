package com.drakescraft.suites.tech.nanotech.gameplay;

import com.drakescraft.suites.tech.nanotech.content.NanotechContent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/** Lightweight hero-suit abilities with no persistent entities or block modification. */
public final class HeroSuitListener implements Listener {
    private final org.bukkit.plugin.Plugin plugin;
    private final NanotechContent content;
    private final Map<UUID, Long> wallLeapCooldown = new HashMap<>();
    private double animation;

    public HeroSuitListener(org.bukkit.plugin.Plugin plugin, NanotechContent content) {
        this.plugin = plugin;
        this.content = content;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWallLeap(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!event.isSneaking() || !wearsSpiderSuit(player) || !touchingWall(player)) return;
        long now = System.currentTimeMillis();
        if (wallLeapCooldown.getOrDefault(player.getUniqueId(), 0L) > now) return;
        wallLeapCooldown.put(player.getUniqueId(), now + 1200L);
        try {
            Vector launch = player.getLocation().getDirection().multiply(-0.45D).setY(0.82D);
            player.setVelocity(player.getVelocity().add(launch));
            player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0, 0.7, 0), 18, 0.3, 0.45, 0.3, 0.02);
            player.getWorld().playSound(player, Sound.ENTITY_SPIDER_STEP, 0.8F, 1.6F);
        } catch (RuntimeException error) {
            plugin.getLogger().log(Level.WARNING, "Spider wall leap aborted safely", error);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHeroFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (wearsSpiderSuit(player) || content.is(player.getInventory().getChestplate(), "MYSTIC_CLOAK")) {
            event.setDamage(event.getDamage() * 0.15D);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 14, 0.3, 0.05, 0.3, 0.02);
        }
    }

    private boolean wearsSpiderSuit(Player player) {
        String id = content.idOf(player.getInventory().getChestplate());
        return id.equals("CLASSIC_SPIDER_SUIT") || id.equals("STEALTH_SPIDER_SUIT") || id.equals("IRON_SPIDER_NANOCORE");
    }

    private boolean touchingWall(Player player) {
        var block = player.getLocation().getBlock();
        return !block.getRelative(BlockFace.NORTH).isPassable() || !block.getRelative(BlockFace.SOUTH).isPassable()
                || !block.getRelative(BlockFace.EAST).isPassable() || !block.getRelative(BlockFace.WEST).isPassable();
    }

    /** Renders an animated six-domain synthezoid shell without spawning persistent entities. */
    public void renderAdvancedArmor() {
        animation += 0.18D;
        try {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!content.is(player.getInventory().getChestplate(), "ULTRON_INFINITY_ARMOR")) continue;
                var center = player.getLocation().add(0, 1.05, 0);
                Color[] colors = {Color.PURPLE, Color.BLUE, Color.RED, Color.YELLOW, Color.LIME, Color.ORANGE};
                for (int domain = 0; domain < colors.length; domain++) {
                    double angle = animation + Math.PI * 2D * domain / colors.length;
                    double height = 0.45D * Math.sin(animation * 0.7D + domain);
                    var point = center.clone().add(Math.cos(angle) * 0.78D, height, Math.sin(angle) * 0.78D);
                    player.getWorld().spawnParticle(Particle.DUST, point, 2, 0.025, 0.025, 0.025,
                            new Particle.DustOptions(colors[domain], 1.45F));
                }
                renderNaniteWing(player, center, -1D);
                renderNaniteWing(player, center, 1D);
                player.getWorld().spawnParticle(Particle.END_ROD, center.clone().add(0, 0.75, 0), 1, 0.12, 0.06, 0.12, 0);
            }
        } catch (RuntimeException error) {
            plugin.getLogger().log(Level.WARNING, "Advanced armor animation aborted safely", error);
        }
    }

    private void renderNaniteWing(Player player, org.bukkit.Location center, double side) {
        Vector right = new Vector(-player.getLocation().getDirection().getZ(), 0, player.getLocation().getDirection().getX()).normalize();
        Vector back = player.getLocation().getDirection().setY(0).normalize().multiply(-1D);
        Particle.DustOptions silver = new Particle.DustOptions(Color.fromRGB(205, 215, 230), 1.0F);
        for (int segment = 1; segment <= 7; segment++) {
            Vector offset = right.clone().multiply(side * segment * 0.18D)
                    .add(back.clone().multiply(0.18D + segment * 0.11D))
                    .setY(0.36D - segment * 0.035D + Math.sin(animation + segment) * 0.04D);
            player.getWorld().spawnParticle(Particle.DUST, center.clone().add(offset), 2, 0.02, 0.02, 0.02, silver);
            if (segment % 2 == 0) player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, center.clone().add(offset), 1);
        }
    }
}

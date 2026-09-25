package com.drakescraft.suites.tech.nanotech.effects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Bounded six-stage Snap cinematic. Its target collection cannot contain a player. */
public final class SnapEffectService {
    private static final Color[] STONES = {
        Color.fromRGB(153, 60, 255), Color.fromRGB(40, 125, 255), Color.fromRGB(255, 45, 55),
        Color.fromRGB(255, 220, 45), Color.fromRGB(65, 255, 95), Color.fromRGB(255, 135, 30)
    };
    private final org.bukkit.plugin.Plugin plugin;

    public SnapEffectService(org.bukkit.plugin.Plugin plugin) { this.plugin = plugin; }

    /** Charges every domain, releases a celestial wave, then disintegrates a bounded fraction of mobs. */
    public void snap(Player actor) {
        Location origin = actor.getLocation().add(0, 1.1, 0);
        double radius = Math.max(8D, plugin.getConfig().getDouble("snap.radius", 42D));
        List<Mob> targets = collectTargets(actor, radius);
        new BukkitRunnable() {
            private int tick;

            @Override public void run() {
                if (!actor.isOnline() || tick > 84) { cancel(); return; }
                if (tick <= 36) renderStoneCharge(actor, tick);
                if (tick == 40) {
                    actor.getWorld().playSound(origin, Sound.BLOCK_END_PORTAL_SPAWN, 2F, 0.55F);
                    actor.getWorld().spawnParticle(Particle.FLASH, origin, 3, 0.1, 0.1, 0.1, 0.0, Color.WHITE);
                }
                if (tick >= 40 && tick <= 58 && tick % 3 == 0) renderWave(origin, radius * (tick - 38D) / 20D);
                if (tick == 62) targets.forEach(target -> beginDisintegration(actor, target));
                tick += 2;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    private List<Mob> collectTargets(Player actor, double radius) {
        int cap = Math.max(1, plugin.getConfig().getInt("snap.max-targets", 48));
        double fraction = Math.max(0.05D, Math.min(1D, plugin.getConfig().getDouble("snap.fraction", 0.5D)));
        List<Mob> eligible = actor.getWorld().getNearbyEntities(actor.getLocation(), radius, radius, radius,
                CombatTargets::isHostileEffectTarget).stream().map(Mob.class::cast)
                .sorted(Comparator.comparing(entity -> entity.getUniqueId().toString())).toList();
        int count = Math.min(cap, (int) Math.ceil(eligible.size() * fraction));
        return new ArrayList<>(eligible.subList(0, count));
    }

    private void renderStoneCharge(Player actor, int tick) {
        Location center = actor.getLocation().add(0, 1.25, 0);
        for (int index = 0; index < STONES.length; index++) {
            double angle = tick * 0.17D + index * Math.PI / 3D;
            Location point = center.clone().add(Math.cos(angle) * 1.35D, 0.3D * Math.sin(angle * 2D), Math.sin(angle) * 1.35D);
            actor.getWorld().spawnParticle(Particle.DUST, point, 5, 0.04, 0.04, 0.04,
                    new Particle.DustOptions(STONES[index], 1.65F));
            actor.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, point, 1, 0.03, 0.03, 0.03, 0);
        }
        if (tick % 8 == 0) actor.getWorld().playSound(center, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1F, 0.65F + tick / 70F);
    }

    private void renderWave(Location center, double radius) {
        for (int point = 0; point < 72; point++) {
            double angle = Math.PI * 2D * point / 72D;
            Location edge = center.clone().add(Math.cos(angle) * radius, 0.15D, Math.sin(angle) * radius);
            center.getWorld().spawnParticle(Particle.DUST, edge, 1, 0, 0, 0,
                    new Particle.DustOptions(STONES[point % STONES.length], 1.35F));
            if (point % 6 == 0) center.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, edge, 1, 0, 0, 0, 0);
        }
    }

    private void beginDisintegration(Player actor, Mob target) {
        new BukkitRunnable() {
            private int layer;
            @Override public void run() {
                if (!target.isValid() || layer >= 8) {
                    if (target.isValid()) target.damage(Math.max(2048D, target.getHealth() + 1D), actor);
                    cancel();
                    return;
                }
                double y = Math.max(0.15D, target.getHeight() * layer / 8D);
                Location ash = target.getLocation().add(0, y, 0);
                target.getWorld().spawnParticle(Particle.ASH, ash, 18, 0.32, 0.12, 0.32, 0.025);
                target.getWorld().spawnParticle(Particle.SOUL, ash, 5, 0.22, 0.14, 0.22, 0.01);
                target.setVelocity(new Vector(0, 0.015D, 0));
                layer++;
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
}

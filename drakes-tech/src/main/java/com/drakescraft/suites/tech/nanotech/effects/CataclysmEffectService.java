package com.drakescraft.suites.tech.nanotech.effects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Comparator;

/** Bounded cinematic WMD renderer. It never mutates a block or targets a player. */
public final class CataclysmEffectService {
    private final org.bukkit.plugin.Plugin plugin;

    public CataclysmEffectService(org.bukkit.plugin.Plugin plugin) { this.plugin = plugin; }

    /** Renders a sky-to-ground lance and applies capped PvE damage at the terminal point. */
    public void skyLance(Player actor, Location target, Color color, double radius, double damage) {
        Location top = target.clone();
        top.setY(Math.min(target.getWorld().getMaxHeight() - 2, target.getY() + 90));
        Particle.DustOptions dust = new Particle.DustOptions(color, 2.2F);
        for (double y = top.getY(); y >= target.getY(); y -= 1.25D) {
            Location point = new Location(target.getWorld(), target.getX(), y, target.getZ());
            target.getWorld().spawnParticle(Particle.DUST, point, 3, 0.12, 0.12, 0.12, dust);
            if (((int) y) % 4 == 0) target.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, point, 2, 0.2, 0.2, 0.2, 0.02);
        }
        rings(target, color, radius);
        target.getWorld().playSound(target, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2F, 0.55F);
        damageCreatures(actor, target, radius, damage);
    }

    /** Creates a reversible-looking fracture made exclusively from particles and sounds. */
    public void realityFracture(Player actor, Location center, double radius) {
        Particle.DustOptions violet = new Particle.DustOptions(Color.fromRGB(185, 55, 255), 1.4F);
        for (int shard = 0; shard < 11; shard++) {
            Vector direction = new Vector(Math.sin(shard * 1.7), 0.25 + (shard % 4) * 0.18, Math.cos(shard * 1.7)).normalize();
            for (double step = 0; step < radius; step += 0.45D) {
                Location point = center.clone().add(direction.clone().multiply(step));
                center.getWorld().spawnParticle(Particle.DUST, point, 1, 0, 0, 0, violet);
                if (((int) (step * 10)) % 14 == 0) center.getWorld().spawnParticle(Particle.REVERSE_PORTAL, point, 2, 0.08, 0.08, 0.08, 0);
            }
        }
        center.getWorld().playSound(center, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.4F, 0.4F);
        damageCreatures(actor, center, radius, 14D);
    }

    private void rings(Location center, Color color, double radius) {
        Particle.DustOptions dust = new Particle.DustOptions(color, 1.5F);
        for (double ring = 2D; ring <= radius; ring += 2D) {
            for (int point = 0; point < 32; point++) {
                double angle = Math.PI * 2D * point / 32D;
                center.getWorld().spawnParticle(Particle.DUST, center.clone().add(Math.cos(angle) * ring, 0.2, Math.sin(angle) * ring), 1, 0, 0, 0, dust);
            }
        }
    }

    private void damageCreatures(Player actor, Location center, double radius, double damage) {
        int cap = Math.max(1, plugin.getConfig().getInt("safety.max-entities-per-effect", 48));
        center.getWorld().getNearbyEntities(center, radius, radius, radius).stream()
                .filter(CombatTargets::isHostileEffectTarget)
                .map(Mob.class::cast)
                .sorted(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(center)))
                .limit(cap)
                .forEach(entity -> {
                    entity.damage(damage, actor);
                    Vector away = entity.getLocation().toVector().subtract(center.toVector()).normalize().multiply(0.8D);
                    entity.setVelocity(entity.getVelocity().add(away).setY(0.35D));
                });
    }
}

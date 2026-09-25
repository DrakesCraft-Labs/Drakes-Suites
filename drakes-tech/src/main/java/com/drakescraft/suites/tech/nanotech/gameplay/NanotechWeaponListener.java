package com.drakescraft.suites.tech.nanotech.gameplay;

import com.drakescraft.suites.tech.nanotech.content.NanotechContent;
import com.drakescraft.suites.tech.nanotech.effects.CataclysmEffectService;
import com.drakescraft.suites.tech.nanotech.effects.CombatTargets;
import com.drakescraft.suites.tech.nanotech.effects.SnapEffectService;
import com.drakescraft.suites.tech.nanotech.protection.ProtectionGate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Particle-first ARC weapons; never damages players and never creates block explosions. */
public final class NanotechWeaponListener implements Listener {
    private final org.bukkit.plugin.Plugin plugin;
    private final NanotechContent content;
    private final ProtectionGate protectionGate;
    private final CataclysmEffectService cataclysms;
    private final SnapEffectService snaps;
    private final GodPrisonFieldService godPrisonFields;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Long> snapCooldowns = new HashMap<>();

    public NanotechWeaponListener(org.bukkit.plugin.Plugin plugin, NanotechContent content,
                                  ProtectionGate protectionGate, GodPrisonFieldService godPrisonFields) {
        this.plugin = plugin;
        this.content = content;
        this.protectionGate = protectionGate;
        this.cataclysms = new CataclysmEffectService(plugin);
        this.snaps = new SnapEffectService(plugin);
        this.godPrisonFields = godPrisonFields;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onRepulsor(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        Player player = event.getPlayer();
        String id = content.idOf(event.getItem());
        if (!isAbilityItem(id)) return;
        if (id.equals("INFINITY_GAUNTLET") || id.equals("NANO_INFINITY_GAUNTLET")) {
            event.setCancelled(true);
            snap(player, id.equals("NANO_INFINITY_GAUNTLET"));
            return;
        }
        if (id.equals("GOD_PRISON_FIELD_CORE")) {
            event.setCancelled(true);
            godPrisonFields.toggle(player);
            return;
        }
        long now = System.currentTimeMillis();
        if (cooldowns.getOrDefault(player.getUniqueId(), 0L) > now) {
            player.sendActionBar("§cRepulsor cooling down");
            return;
        }
        cooldowns.put(player.getUniqueId(), now + 1800L);
        event.setCancelled(true);
        try {
            switch (id) {
                case "REPULSOR_EMITTER" -> fireRepulsor(player);
                case "WEB_SHOOTER" -> fireWeb(player);
                case "HAWKEYE_COMPOUND_BOW", "SONIC_ARROW" -> sonicArrow(player);
                case "WIDOW_BITE_GAUNTLET" -> widowBite(player);
                case "CAPTAIN_SHIELD", "STEALTH_CAPTAIN_SHIELD" -> throwShield(player, id.equals("STEALTH_CAPTAIN_SHIELD"));
                case "UNIBEAM_LENS", "STARK_UNIBEAM_ASSEMBLY" -> fireUnibeam(player);
                case "ORBITAL_SKY_LANCE", "ULTRON_INFINITY_BEAM", "CELESTIAL_NULLIFIER" -> fireSkyWeapon(player, id);
                case "REALITY_FRACTURE_DEVICE", "DIMENSIONAL_BREACH_CHARGE", "SINGULARITY_WARHEAD" -> fractureReality(player);
                default -> cooldowns.remove(player.getUniqueId());
            }
        } catch (RuntimeException error) {
            plugin.getLogger().log(java.util.logging.Level.WARNING, "Ability execution failed safely for " + id, error);
            player.sendMessage("§6DrakesNanotech §8· §cThe ability aborted safely.");
        }
    }

    /**
     * Keeps Nanotech items out of vanilla vaults. UNIVERSAL_FORGE_KEY and ULTRON_AI_CORE are built
     * on TRIAL_KEY and OMINOUS_TRIAL_KEY, so a right click on a vault either burns a tier 5/6 item
     * for vanilla loot or silently does nothing, which players report as a broken key. Cancelling
     * the interaction keeps the item in the inventory and says why.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onVaultInsert(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.VAULT) return;
        if (event.getItem() == null) return;
        String id = content.idOf(event.getItem());
        if (id.isEmpty()) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(vaultRejectionMessage(id));
    }

    /**
     * Explains the cancelled vault click in chat instead of the action bar. The rejected items look
     * exactly like a vanilla trial key, so an action bar line that scrolls away leaves the player
     * believing the key itself is broken.
     */
    static String vaultRejectionMessage(String id) {
        return "§6DrakesNanotech §8· §c" + id
                + " is a Nanotech item, not a trial key. §7The vault ignores it and the item stays in your inventory.";
    }

    /**
     * Limits this listener to the explicit Nanotech weapons it owns. {@link NanotechContent#idOf}
     * returns IDs for every Slimefun addon, so treating any non-empty ID as a weapon cancelled
     * placement of all Slimefun machines before Bukkit could emit BlockPlaceEvent.
     */
    static boolean isAbilityItem(String id) {
        return switch (id) {
            case "INFINITY_GAUNTLET", "NANO_INFINITY_GAUNTLET", "GOD_PRISON_FIELD_CORE",
                 "REPULSOR_EMITTER", "WEB_SHOOTER", "HAWKEYE_COMPOUND_BOW", "SONIC_ARROW",
                 "WIDOW_BITE_GAUNTLET", "CAPTAIN_SHIELD", "STEALTH_CAPTAIN_SHIELD",
                 "UNIBEAM_LENS", "STARK_UNIBEAM_ASSEMBLY", "ORBITAL_SKY_LANCE",
                 "ULTRON_INFINITY_BEAM", "CELESTIAL_NULLIFIER", "REALITY_FRACTURE_DEVICE",
                 "DIMENSIONAL_BREACH_CHARGE", "SINGULARITY_WARHEAD" -> true;
            default -> false;
        };
    }

    /** Executes a long-cooldown Snap only after a buffered protection scan succeeds. */
    private void snap(Player player, boolean sacrificialStarkGauntlet) {
        double radius = Math.max(8D, plugin.getConfig().getDouble("snap.radius", 42D));
        if (!protectionGate.allowLargeAbility(player, player.getLocation(), radius)) return;
        long now = System.currentTimeMillis();
        long until = snapCooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (until > now) {
            player.sendActionBar("§cUniversal discharge is still cooling down");
            return;
        }
        snapCooldowns.put(player.getUniqueId(), now + Math.max(60, plugin.getConfig().getInt("snap.cooldown-seconds", 600)) * 1000L);
        snaps.snap(player);
        if (sacrificialStarkGauntlet) {
            player.sendTitle("§c§lUNIVERSAL DISCHARGE", "§7The nanogauntlet cannot contain it", 5, 45, 20);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline() || player.isDead()) return;
                player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1, 0), 220, 0.55, 0.9, 0.55, 0.04);
                player.setHealth(0D);
            }, 55L);
        }
    }

    /** Draws an outbound and returning kinetic path through at most six mobs. */
    private void throwShield(Player player, boolean stealth) {
        org.bukkit.Location cursor = player.getEyeLocation();
        Vector direction = cursor.getDirection().normalize();
        Particle.DustOptions trail = new Particle.DustOptions(stealth ? Color.fromRGB(55, 75, 95) : Color.fromRGB(225, 35, 45), 1.45F);
        java.util.List<Mob> hits = new java.util.ArrayList<>();
        for (int step = 0; step < 34; step++) {
            cursor = cursor.clone().add(direction.clone().multiply(0.7D));
            cursor.getWorld().spawnParticle(Particle.DUST, cursor, 2, 0.04, 0.04, 0.04, trail);
            for (Entity entity : cursor.getWorld().getNearbyEntities(cursor, 1D, 1D, 1D, CombatTargets::isHostileEffectTarget)) {
                Mob mob = (Mob) entity;
                if (hits.contains(mob) || hits.size() >= 6) continue;
                hits.add(mob);
                mob.damage(11D, player);
                mob.setVelocity(direction.clone().multiply(0.55D).setY(0.22D));
                cursor.getWorld().playSound(cursor, Sound.ITEM_SHIELD_BLOCK, 0.8F, 1.45F);
                direction.rotateAroundY(Math.PI / 5D);
            }
        }
        drawArc(cursor, player.getEyeLocation());
        player.getWorld().playSound(player, Sound.ITEM_TRIDENT_RETURN, 1F, 1.35F);
    }

    /** Charges and emits a wide visible ARC beam whose ray predicate accepts mobs only. */
    private void fireUnibeam(Player player) {
        Vector direction = player.getEyeLocation().getDirection().normalize();
        org.bukkit.Location start = player.getEyeLocation().add(direction.clone().multiply(0.8D));
        Particle.DustOptions white = new Particle.DustOptions(Color.fromRGB(220, 255, 255), 2.2F);
        for (double distance = 0; distance <= 42D; distance += 0.28D) {
            org.bukkit.Location point = start.clone().add(direction.clone().multiply(distance));
            player.getWorld().spawnParticle(Particle.DUST, point, 3, 0.09, 0.09, 0.09, white);
            if (((int) (distance * 10)) % 12 == 0) player.getWorld().spawnParticle(Particle.END_ROD, point, 2, 0.12, 0.12, 0.12, 0.01);
        }
        player.getWorld().playSound(player, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.2F, 1.25F);
        RayTraceResult hit = player.getWorld().rayTraceEntities(start, direction, 42D, 1.2D, CombatTargets::isHostileEffectTarget);
        if (hit != null && hit.getHitEntity() instanceof Mob mob) mob.damage(28D, player);
    }

    private void fireWeb(Player player) {
        Vector direction = player.getEyeLocation().getDirection().normalize();
        var start = player.getEyeLocation().add(direction.clone().multiply(0.6D));
        for (double step = 0; step < 22D; step += 0.35D) {
            player.getWorld().spawnParticle(Particle.END_ROD, start.clone().add(direction.clone().multiply(step)), 1, 0, 0, 0, 0);
        }
        RayTraceResult hit = player.getWorld().rayTraceEntities(start, direction, 22D, 0.5D,
                entity -> eligibleTarget(player, entity));
        if (hit != null && hit.getHitEntity() instanceof LivingEntity target) {
            target.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 5, false, true));
            target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation().add(0, 1, 0), 35, 0.45, 0.8, 0.45, 0.01);
        }
    }

    private void widowBite(Player player) {
        LivingEntity previous = player;
        int affected = 0;
        for (LivingEntity target : player.getWorld().getNearbyLivingEntities(player.getLocation(), 12D,
                CombatTargets::isHostileEffectTarget)) {
            if (affected++ >= 6) break;
            drawArc(previous.getLocation().add(0, 1, 0), target.getLocation().add(0, 1, 0));
            target.damage(7D, player);
            target.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 50, 3, false, true));
            previous = target;
        }
        player.getWorld().playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 1F, 1.8F);
    }

    private void sonicArrow(Player player) {
        org.bukkit.Location target = aimedLocation(player, 34D);
        Particle.DustOptions purple = new Particle.DustOptions(Color.fromRGB(185, 90, 255), 1.3F);
        for (double radius = 1D; radius <= 7D; radius += 1D) {
            for (int point = 0; point < 28; point++) {
                double angle = Math.PI * 2D * point / 28D;
                target.getWorld().spawnParticle(Particle.DUST,
                        target.clone().add(Math.cos(angle) * radius, 0.25, Math.sin(angle) * radius), 1, 0, 0, 0, purple);
            }
        }
        target.getWorld().playSound(target, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.9F, 1.7F);
        target.getWorld().getNearbyLivingEntities(target, 7D, CombatTargets::isHostileEffectTarget).stream()
                .limit(12)
                .forEach(entity -> {
                    entity.damage(8D, player);
                    entity.setVelocity(entity.getVelocity().add(entity.getLocation().toVector()
                            .subtract(target.toVector()).normalize().multiply(0.65D).setY(0.25D)));
                });
    }

    private void drawArc(org.bukkit.Location from, org.bukkit.Location to) {
        Vector delta = to.toVector().subtract(from.toVector());
        for (double step = 0; step <= 1D; step += 0.08D) {
            org.bukkit.Location point = from.clone().add(delta.clone().multiply(step));
            point.add((Math.random() - 0.5D) * 0.2D, (Math.random() - 0.5D) * 0.2D, (Math.random() - 0.5D) * 0.2D);
            from.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, point, 2, 0.02, 0.02, 0.02, 0);
        }
    }

    private void fireSkyWeapon(Player player, String id) {
        org.bukkit.Location target = aimedLocation(player, 52D);
        double radius = id.equals("CELESTIAL_NULLIFIER") ? 18D : 11D;
        if (!protectionGate.allowLargeAbility(player, target, radius)) return;
        Color color = id.equals("ULTRON_INFINITY_BEAM") ? Color.fromRGB(210, 70, 255) : Color.fromRGB(100, 225, 255);
        cataclysms.skyLance(player, target, color, radius, id.equals("CELESTIAL_NULLIFIER") ? 28D : 20D);
    }

    private void fractureReality(Player player) {
        org.bukkit.Location target = aimedLocation(player, 36D);
        if (!protectionGate.allowLargeAbility(player, target, 14D)) return;
        cataclysms.realityFracture(player, target, 14D);
    }

    private org.bukkit.Location aimedLocation(Player player, double range) {
        RayTraceResult result = player.rayTraceBlocks(range);
        return result != null && result.getHitPosition() != null
                ? result.getHitPosition().toLocation(player.getWorld())
                : player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(range));
    }

    /** Draws a bounded ray and applies one protected PvE impact. */
    private void fireRepulsor(Player player) {
        double range = plugin.getConfig().getDouble("effects.repulsor-range", 28D);
        Vector direction = player.getEyeLocation().getDirection().normalize();
        var start = player.getEyeLocation().add(direction.clone().multiply(0.7));
        Particle.DustOptions core = new Particle.DustOptions(Color.fromRGB(120, 235, 255), 1.15F);
        for (double distance = 0; distance <= range; distance += 0.45D) {
            var point = start.clone().add(direction.clone().multiply(distance));
            player.getWorld().spawnParticle(Particle.DUST, point, 1, 0, 0, 0, core);
            if (((int) (distance * 10)) % 18 == 0) player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, point, 2, 0.04, 0.04, 0.04, 0);
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_ATTACK, 0.8F, 1.8F);
        RayTraceResult hit = player.getWorld().rayTraceEntities(start, direction, range, 0.45,
                entity -> eligibleTarget(player, entity));
        if (hit == null || !(hit.getHitEntity() instanceof LivingEntity target)) return;
        target.damage(plugin.getConfig().getDouble("effects.repulsor-damage", 10D), player);
        target.setVelocity(target.getVelocity().add(direction.multiply(0.75)));
        target.getWorld().spawnParticle(Particle.FLASH, target.getLocation().add(0, 1, 0), 1, 0.0, 0.0, 0.0, 0.0, Color.WHITE);
        target.getWorld().spawnParticle(Particle.END_ROD, target.getLocation().add(0, 1, 0), 24, 0.35, 0.55, 0.35, 0.05);
    }

    private static boolean eligibleTarget(Player source, Entity entity) {
        return CombatTargets.isHostileEffectTarget(entity) && !entity.getUniqueId().equals(source.getUniqueId());
    }
}

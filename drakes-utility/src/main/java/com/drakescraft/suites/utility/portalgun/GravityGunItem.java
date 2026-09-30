package com.drakescraft.suites.utility.portalgun;

import org.bukkit.Sound;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GravityGunItem logic decoupled from Slimefun Item classes.
 */
public class GravityGunItem {

    public static final Map<String, Entity> holding = new ConcurrentHashMap<>();
    private final Plugin plugin;

    public GravityGunItem(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean releaseHeld(Player p) {
        if (holding.containsKey(p.getName())) {
            Entity entity = holding.get(p.getName());
            if (entity instanceof BlockDisplay) {
                entity.remove();
            }
            holding.remove(p.getName());
            try {
                p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
            } catch (Exception ignored) {
            }
            return true;
        }
        return false;
    }

    public void toggleGrab(Player p, Entity target) {
        if (holding.containsKey(p.getName())) {
            holding.remove(p.getName());
        } else {
            holding.put(p.getName(), target);
            try {
                p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0f, 1.0f);
            } catch (Exception ignored) {
            }
        }
    }

    public boolean launchHeld(Player p) {
        if (holding.containsKey(p.getName())) {
            Entity ent = holding.get(p.getName());
            if (ent != null && ent.isValid()) {
                ent.setVelocity(p.getLocation().getDirection().multiply(1.8));
                try {
                    p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
                } catch (Exception ignored) {
                }
            }
            holding.remove(p.getName());
            return true;
        }
        return false;
    }

    public static void cleanup() {
        for (Entity ent : holding.values()) {
            if (ent instanceof BlockDisplay) {
                ent.remove();
            }
        }
        holding.clear();
    }
}

package com.drakescraft.suites.utility.notenoughaddons;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FlyingBubble - Quantum anti-gravity generator allowing creative flight in a 25-block radius.
 */
public class FlyingBubble {

    public static final int DEFAULT_CAPACITY = 1024;
    public static final int DEFAULT_CONSUMPTION = 128;
    public static final double RADIUS = 25.0;

    private static final Map<Location, Set<UUID>> allEnabledPlayers = new ConcurrentHashMap<>();
    private static final Set<UUID> allUuids = Collections.synchronizedSet(new HashSet<>());

    private final int energyCapacity;
    private final int energyConsumption;

    public FlyingBubble() {
        this(DEFAULT_CAPACITY, DEFAULT_CONSUMPTION);
    }

    public FlyingBubble(int energyCapacity, int energyConsumption) {
        this.energyCapacity = energyCapacity;
        this.energyConsumption = energyConsumption;
    }

    public void tick(@Nonnull Block b) {
        Location loc = b.getLocation();
        Set<UUID> playersInBubble = allEnabledPlayers.computeIfAbsent(loc, k -> ConcurrentHashMap.newKeySet());
        Collection<Entity> bubbledEntities = b.getWorld().getNearbyEntities(loc, RADIUS, RADIUS, RADIUS);

        for (Entity entity : bubbledEntities) {
            if (entity instanceof Player p) {
                playersInBubble.add(p.getUniqueId());
                if (!p.getAllowFlight()) {
                    p.setAllowFlight(true);
                }
            }
        }

        Iterator<UUID> it = playersInBubble.iterator();
        while (it.hasNext()) {
            UUID uuid = it.next();
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) {
                it.remove();
            } else if (!bubbledEntities.contains(p)) {
                it.remove();
                checkPlayer(p.getUniqueId());
            }
        }
    }

    public void onBlockBreak(@Nonnull Location location) {
        Set<UUID> uuids = allEnabledPlayers.remove(location);
        if (uuids != null) {
            for (UUID uuid : uuids) {
                checkPlayer(uuid);
            }
        }
    }

    private void checkPlayer(@Nonnull UUID u) {
        allUuids.clear();
        for (Set<UUID> uuidSet : allEnabledPlayers.values()) {
            allUuids.addAll(uuidSet);
        }

        if (!allUuids.contains(u)) {
            Player p = Bukkit.getPlayer(u);
            if (p != null) {
                p.setAllowFlight(false);
                p.setFlying(false);
                p.setFallDistance(0.0f);
            }
        }
    }

    public static void cleanup() {
        for (UUID u : allUuids) {
            Player p = Bukkit.getPlayer(u);
            if (p != null) {
                p.setAllowFlight(false);
                p.setFlying(false);
            }
        }
        allEnabledPlayers.clear();
        allUuids.clear();
    }

    public int getCapacity() {
        return energyCapacity;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }
}

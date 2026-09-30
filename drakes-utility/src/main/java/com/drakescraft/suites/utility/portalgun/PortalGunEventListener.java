package com.drakescraft.suites.utility.portalgun;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PortalGunEventListener implements Listener {

    public static final Map<String, List<PortalData>> portals = new ConcurrentHashMap<>();
    private final Set<UUID> doNotTeleport = Collections.synchronizedSet(new HashSet<>());
    private final Plugin plugin;

    public PortalGunEventListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(ProjectileHitEvent e) {
        Entity projectile = e.getEntity();
        if (projectile instanceof Snowball ball && ball.hasMetadata("portalgunball")) {
            if (e.getHitBlock() == null || e.getHitBlockFace() == null || e.getHitBlock().getType() == Material.AIR) {
                return;
            }

            String playerName = ball.getMetadata("portalgunball").get(0).asString();
            String pgId = ball.getMetadata("portalgunid").get(0).asString();
            boolean isSecond = ball.getMetadata("is_second").get(0).asBoolean();
            Player p = Bukkit.getPlayer(playerName);
            if (p == null) {
                return;
            }

            List<PortalData> portalList = portals.computeIfAbsent(pgId, k -> new ArrayList<>());
            if (portalList.size() >= 2) {
                portalList.remove(0);
            }

            String colorStr = ball.getMetadata(isSecond ? "color_2" : "color_1").get(0).asString();
            String[] rgb = colorStr.split(";");
            Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

            PortalData pd = new PortalData();
            pd.blockFace = e.getHitBlockFace();
            pd.location = e.getHitBlock().getLocation();
            pd.color = color;
            pd.second = isSecond;

            portalList.add(pd);
            try {
                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0f, 1.0f);
            } catch (Exception ignored) {
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location loc = e.getTo();
        if (loc == null) return;

        handleGravityGun(p);

        if (doNotTeleport.contains(p.getUniqueId())) {
            return;
        }

        for (List<PortalData> pair : portals.values()) {
            if (pair.size() == 2) {
                PortalData p1 = pair.get(0);
                PortalData p2 = pair.get(1);

                if (p1.intersects(loc)) {
                    teleportPlayerThroughPortal(p, loc, p1, p2);
                    return;
                } else if (p2.intersects(loc)) {
                    teleportPlayerThroughPortal(p, loc, p2, p1);
                    return;
                }
            }
        }
    }

    private void handleGravityGun(Player p) {
        if (!GravityGunItem.holding.containsKey(p.getName())) {
            return;
        }
        Entity entity = GravityGunItem.holding.get(p.getName());
        if (entity == null || !entity.isValid()) {
            GravityGunItem.holding.remove(p.getName());
            return;
        }

        RayTraceResult rtr = p.rayTraceBlocks(6.0, FluidCollisionMode.NEVER);
        Location targetLoc;
        if (rtr != null && rtr.getHitPosition() != null) {
            targetLoc = rtr.getHitPosition().toLocation(p.getWorld());
        } else {
            targetLoc = p.getLocation().clone().add(p.getLocation().getDirection().multiply(4.0));
        }

        if (entity instanceof BlockDisplay) {
            targetLoc = targetLoc.setDirection(new Vector(0, 0, 0));
        }
        entity.teleport(targetLoc.add(0, 0.8, 0));
        entity.setFallDistance(0);
    }

    private void teleportPlayerThroughPortal(Player p, Location currentLoc, PortalData origin, PortalData destination) {
        doNotTeleport.add(p.getUniqueId());
        try {
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        } catch (Exception ignored) {
        }

        Location targetLoc = destination.location.clone();
        targetLoc.setYaw(currentLoc.getYaw());
        targetLoc.setPitch(currentLoc.getPitch());

        if (destination.blockFace == BlockFace.UP) {
            targetLoc.add(0.5, 1.0, 0.5);
        } else if (destination.blockFace == BlockFace.DOWN) {
            targetLoc.add(0.5, -2.0, 0.5);
        } else if (destination.blockFace == BlockFace.NORTH) {
            targetLoc.add(0.5, 0.0, -0.2);
        } else if (destination.blockFace == BlockFace.SOUTH) {
            targetLoc.add(0.5, 0.0, 1.2);
        } else if (destination.blockFace == BlockFace.EAST) {
            targetLoc.add(1.2, 0.0, 0.5);
        } else if (destination.blockFace == BlockFace.WEST) {
            targetLoc.add(-0.2, 0.0, 0.5);
        }

        p.teleport(targetLoc);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            p.setVelocity(new Vector(0, 0.3, 0));
        }, 1L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            doNotTeleport.remove(p.getUniqueId());
        }, 10L);
    }

    public static void cleanup() {
        portals.clear();
    }
}

package com.drakescraft.suites.utility.portalgun;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * PortalGunModule - Portal Gun & Gravity Gun quantum mechanics.
 */
public class PortalGunModule extends AbstractSuiteModule implements Listener {

    private PortalGunItem portalGunItem;
    private GravityGunItem gravityGunItem;
    private PortalGunEventListener listener;
    private BukkitTask particleTask;

    public PortalGunModule(@Nonnull JavaPlugin plugin) {
        super(plugin, "portalgun", "SFPortalGun Quantum Entanglement Teleporters");
    }

    @Override
    public void onEnable() {
        this.portalGunItem = new PortalGunItem(getPlugin());
        this.gravityGunItem = new GravityGunItem(getPlugin());

        this.listener = new PortalGunEventListener(getPlugin());
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());

        // Particle rendering task for open portals
        this.particleTask = Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
            for (List<PortalData> pair : PortalGunEventListener.portals.values()) {
                for (PortalData pd : pair) {
                    if (pd == null || pd.location == null || pd.location.getWorld() == null) continue;
                    try {
                        spawnPortalParticles(pd);
                    } catch (Exception ignored) {
                    }
                }
            }
        }, 10L, 10L);

        logInfo("Module enabled with Portal Gun and Gravity Gun (" + PortalGunItems.getAllItems().size() + " items).");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return;

        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId == null) return;

        Player player = event.getPlayer();

        if (PortalGunItems.PORTAL_GUN.equals(sfId)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                portalGunItem.firePortal(player, item);
            }
        } else if (PortalGunItems.GRAVITY_GUN.equals(sfId)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (gravityGunItem.launchHeld(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!isEnabled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir() || !item.hasItemMeta()) return;

        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (PortalGunItems.GRAVITY_GUN.equals(sfId)) {
            event.setCancelled(true);
            Entity clicked = event.getRightClicked();
            gravityGunItem.toggleGrab(player, clicked);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isEnabled()) return;

        ItemStack dropped = event.getItemDrop().getItemStack();
        if (dropped.getType().isAir() || !dropped.hasItemMeta()) return;

        String sfId = SuiteItemPdcBridge.getSlimefunId(dropped);
        if (sfId == null) return;

        Player player = event.getPlayer();
        if (PortalGunItems.PORTAL_GUN.equals(sfId)) {
            if (portalGunItem.randomizeColors(player, dropped)) {
                event.setCancelled(true);
            }
        } else if (PortalGunItems.GRAVITY_GUN.equals(sfId)) {
            if (gravityGunItem.releaseHeld(player)) {
                event.setCancelled(true);
            }
        }
    }

    private void spawnPortalParticles(PortalData pd) {
        double x = pd.location.getBlockX() + 0.5;
        double y = pd.location.getBlockY() + 0.5;
        double z = pd.location.getBlockZ() + 0.5;
        if (pd.blockFace == BlockFace.UP) y += 0.5;
        else if (pd.blockFace == BlockFace.DOWN) y -= 0.5;
        else if (pd.blockFace == BlockFace.NORTH) z -= 0.5;
        else if (pd.blockFace == BlockFace.SOUTH) z += 0.5;
        else if (pd.blockFace == BlockFace.EAST) x += 0.5;
        else if (pd.blockFace == BlockFace.WEST) x -= 0.5;

        pd.location.getWorld().spawnParticle(
            Particle.DUST_COLOR_TRANSITION,
            x, y, z, 15, 0.2, 0.2, 0.2, 0.0,
            new Particle.DustTransition(pd.color, pd.color, 1.0f)
        );
    }

    @Override
    public void onDisable() {
        if (particleTask != null) {
            particleTask.cancel();
            particleTask = null;
        }
        if (listener != null) {
            HandlerList.unregisterAll(listener);
            listener = null;
        }
        HandlerList.unregisterAll(this);
        GravityGunItem.cleanup();
        PortalGunEventListener.cleanup();
        logInfo("Module disabled cleanly.");
    }

    public PortalGunItem getPortalGunItem() {
        return portalGunItem;
    }

    public GravityGunItem getGravityGunItem() {
        return gravityGunItem;
    }
}

package com.drakescraft.suites.utility.portalgun;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * PortalGunItem logic decoupled from Slimefun Item classes.
 */
public class PortalGunItem {

    private final Plugin plugin;
    private final NamespacedKey keyId;
    private final NamespacedKey keyColor1;
    private final NamespacedKey keyColor2;

    public PortalGunItem(@Nonnull Plugin plugin) {
        this.plugin = plugin;
        this.keyId = new NamespacedKey(plugin, "unique_portals_id");
        this.keyColor1 = new NamespacedKey(plugin, "pg_1color");
        this.keyColor2 = new NamespacedKey(plugin, "pg_2color");
    }

    public String getPortalGunId(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        if (im == null) return UUID.randomUUID().toString();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        String id;
        if (!pdc.has(keyId, PersistentDataType.STRING)) {
            id = UUID.randomUUID().toString();
            pdc.set(keyId, PersistentDataType.STRING, id);
            itemStack.setItemMeta(im);
        } else {
            id = pdc.get(keyId, PersistentDataType.STRING);
        }
        return id;
    }

    public String getPortal1Color(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        if (im == null) return "0;150;255";
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        String color;
        if (!pdc.has(keyColor1, PersistentDataType.STRING)) {
            color = ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255));
            pdc.set(keyColor1, PersistentDataType.STRING, color);
            itemStack.setItemMeta(im);
        } else {
            color = pdc.get(keyColor1, PersistentDataType.STRING);
        }
        return color;
    }

    public String getPortal2Color(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        if (im == null) return "255;150;0";
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        String color;
        if (!pdc.has(keyColor2, PersistentDataType.STRING)) {
            color = ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255));
            pdc.set(keyColor2, PersistentDataType.STRING, color);
            itemStack.setItemMeta(im);
        } else {
            color = pdc.get(keyColor2, PersistentDataType.STRING);
        }
        return color;
    }

    public boolean randomizeColors(Player p, ItemStack itemStack) {
        if (p.isSneaking() && itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta im = itemStack.getItemMeta();
            PersistentDataContainer pdc = im.getPersistentDataContainer();

            String color1 = ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255));
            String color2 = ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255)) + ";" + ((int) (Math.random() * 255));

            pdc.set(keyColor1, PersistentDataType.STRING, color1);
            pdc.set(keyColor2, PersistentDataType.STRING, color2);
            itemStack.setItemMeta(im);

            p.sendMessage(Component.text("Portal colors randomized!").color(NamedTextColor.AQUA));
            try {
                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 1.0f);
            } catch (Exception ignored) {
            }
            return true;
        }
        return false;
    }

    public void firePortal(Player p, ItemStack item) {
        String id = getPortalGunId(item);
        String color1 = getPortal1Color(item);
        String color2 = getPortal2Color(item);

        Location loc = p.getEyeLocation();
        Snowball snowball = (Snowball) p.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
        snowball.setVelocity(loc.getDirection().multiply(2.0));
        snowball.setShooter(p);
        snowball.setMetadata("portalgunball", new FixedMetadataValue(plugin, p.getName()));
        snowball.setMetadata("portalgunid", new FixedMetadataValue(plugin, id));
        snowball.setMetadata("color_1", new FixedMetadataValue(plugin, color1));
        snowball.setMetadata("color_2", new FixedMetadataValue(plugin, color2));
        snowball.setMetadata("is_second", new FixedMetadataValue(plugin, p.isSneaking()));

        try {
            p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1.0f, 1.2f);
        } catch (Exception ignored) {
        }
    }
}

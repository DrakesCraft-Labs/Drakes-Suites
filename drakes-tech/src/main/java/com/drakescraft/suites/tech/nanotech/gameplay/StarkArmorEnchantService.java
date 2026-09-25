package com.drakescraft.suites.tech.nanotech.gameplay;

import com.drakescraft.suites.tech.nanotech.content.NanotechContent;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/** Stark-only technological enchantments backed by immutable PDC identities. */
public final class StarkArmorEnchantService extends BukkitRunnable implements Listener {
    private final org.bukkit.plugin.Plugin plugin;
    private final NanotechContent content;
    private final NamespacedKey aegis;
    private final NamespacedKey regenesis;
    private final NamespacedKey anchor;
    private final Map<UUID, Long> anchorCooldown = new HashMap<>();

    public StarkArmorEnchantService(org.bukkit.plugin.Plugin plugin, NanotechContent content) {
        this.plugin = plugin;
        this.content = content;
        this.aegis = new NamespacedKey(plugin, "arc_aegis");
        this.regenesis = new NamespacedKey(plugin, "nanite_regenesis");
        this.anchor = new NamespacedKey(plugin, "zero_point_anchor");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onApply(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick() || !event.getPlayer().isSneaking()) return;
        String module = content.idOf(event.getItem());
        NamespacedKey key = switch (module) {
            case "ENCHANT_ARC_AEGIS" -> aegis;
            case "ENCHANT_NANITE_REGENESIS" -> regenesis;
            case "ENCHANT_ZERO_POINT_ANCHOR" -> anchor;
            default -> null;
        };
        if (key == null) return;
        Player player = event.getPlayer();
        ItemStack chest = player.getInventory().getChestplate();
        if (!isAdvancedStarkArmor(chest)) {
            player.sendMessage("§6DrakesNanotech §8· §cThis enchantment requires Mark L, XLVI, LXXXV or Ultron Infinity armor.");
            return;
        }
        event.setCancelled(true);
        var meta = chest.getItemMeta();
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            player.sendActionBar("§cThat technological enchantment is already installed");
            return;
        }
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        List<String> lore = meta.hasLore() ? new java.util.ArrayList<>(meta.getLore()) : new java.util.ArrayList<>();
        lore.add(switch (module) {
            case "ENCHANT_ARC_AEGIS" -> "§b✦ ARC Aegis VI";
            case "ENCHANT_NANITE_REGENESIS" -> "§c✦ Nanite Regenesis VI";
            default -> "§d✦ Zero-Point Anchor VI";
        });
        meta.setLore(lore);
        chest.setItemMeta(meta);
        event.getItem().setAmount(event.getItem().getAmount() - 1);
        player.getWorld().playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.2F, 0.65F);
        player.getWorld().spawnParticle(Particle.ENCHANT, player.getLocation().add(0, 1, 0), 80, 0.55, 0.9, 0.55, 0.7);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack chest = player.getInventory().getChestplate();
        if (!isAdvancedStarkArmor(chest)) return;
        var pdc = chest.getItemMeta().getPersistentDataContainer();
        if (pdc.has(aegis, PersistentDataType.BYTE)) {
            event.setDamage(Math.max(0.5D, event.getDamage() * 0.08D));
            player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 1, 0), 24, 0.5, 0.8, 0.5,
                    new Particle.DustOptions(Color.fromRGB(75, 225, 255), 1.25F));
        }
        if (!pdc.has(anchor, PersistentDataType.BYTE)) return;
        if (player.getHealth() - event.getFinalDamage() > 0D) return;
        long now = System.currentTimeMillis();
        if (anchorCooldown.getOrDefault(player.getUniqueId(), 0L) > now) return;
        anchorCooldown.put(player.getUniqueId(), now + 30L * 60L * 1000L);
        event.setCancelled(true);
        player.setHealth(Math.min(player.getMaxHealth(), 8D));
        player.setFireTicks(0);
        player.getWorld().playSound(player, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.5F, 0.55F);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 1, 0), 160, 0.7, 1.1, 0.7, 0.45);
        player.sendTitle("§dZERO-POINT ANCHOR", "§7Lethal state rejected · 30 minute cooldown", 5, 45, 15);
    }

    @Override
    public void run() {
        try {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                ItemStack chest = player.getInventory().getChestplate();
                if (!isAdvancedStarkArmor(chest)
                        || !chest.getItemMeta().getPersistentDataContainer().has(regenesis, PersistentDataType.BYTE)) continue;
                if (!consumeNanomass(player)) continue;
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 2D));
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (armor == null || !(armor.getItemMeta() instanceof Damageable damageable) || !damageable.hasDamage()) continue;
                    damageable.setDamage(Math.max(0, damageable.getDamage() - 12));
                    armor.setItemMeta(damageable);
                }
                player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 1, 0), 18, 0.42, 0.75, 0.42,
                        new Particle.DustOptions(Color.fromRGB(255, 45, 65), 0.9F));
            }
        } catch (RuntimeException error) {
            plugin.getLogger().log(Level.WARNING, "Stark armor regeneration aborted safely", error);
        }
    }

    private boolean consumeNanomass(Player player) {
        for (ItemStack stack : player.getInventory().getStorageContents()) {
            if (!content.is(stack, "NANOMASS_CANISTER")) continue;
            stack.setAmount(stack.getAmount() - 1);
            return true;
        }
        return false;
    }

    private boolean isAdvancedStarkArmor(ItemStack chest) {
        String id = content.idOf(chest);
        return id.equals("MARK_L_NANOCORE") || id.equals("MARK_XLVI_COMBAT_NANOCORE")
                || id.equals("MARK_LXXXV_NANOCORE") || id.equals("ULTRON_INFINITY_ARMOR");
    }
}

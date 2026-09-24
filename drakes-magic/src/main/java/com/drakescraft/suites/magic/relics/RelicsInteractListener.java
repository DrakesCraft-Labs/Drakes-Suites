package com.drakescraft.suites.magic.relics;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Listener que gestiona la interacción y desellado de reliquias de Cthonia.
 */
public class RelicsInteractListener implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final RelicsRegistry registry;

    public RelicsInteractListener(RelicsRegistry registry) {
        this.registry = registry;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!registry.isRelic(item)) return;

        event.setCancelled(true);

        RelicDefinition relic = registry.getRelic(item);
        if (relic == null) return;

        // Consumir 1 unidad
        item.subtract(1);

        // Otorgar experiencia y recompensa
        int xp = relic.xpReward();
        player.giveExp(xp);

        try {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        } catch (Throwable ignored) {}

        String msg = "<gradient:#a855f7:#38bdf8>✦ Reliquias de Cthonia ✦</gradient> " +
                "<gray>Has purificado " + relic.rarity().getColorTag() + "<bold>" + relic.displayName() + "</bold></gray> " +
                "<gray>obteniendo <yellow>+" + xp + " XP</yellow>.</gray>";
        player.sendMessage(MM.deserialize(msg));
    }
}

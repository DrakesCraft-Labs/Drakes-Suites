package com.drakescraft.suites.magic.souljars;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Listener que intercepta la muerte de criaturas y canaliza sus almas
 * hacia frascos vacíos o en progreso en el inventario del jugador.
 */
public class SoulJarListener implements Listener {

    private final SoulJarsModule module;
    private final SoulJarRegistry registry;

    public SoulJarListener(SoulJarsModule module, SoulJarRegistry registry) {
        this.module = module;
        this.registry = registry;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!module.isEnabled()) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (killer == null && event.getDamageSource() != null && event.getDamageSource().getCausingEntity() instanceof Player) {
            killer = (Player) event.getDamageSource().getCausingEntity();
        }
        if (killer == null) {
            return;
        }

        EntityType type = event.getEntityType();
        if (!registry.isSupportedEntity(type)) {
            return;
        }

        int requiredSouls = registry.getRequiredSouls(type);

        // 1. Buscar primero un frasco en progreso del mismo tipo
        for (int slot = 0; slot < killer.getInventory().getSize(); slot++) {
            ItemStack stack = killer.getInventory().getItem(slot);
            if (stack == null || stack.getType().isAir()) continue;

            if (registry.isSoulJar(stack) && registry.getJarEntityType(stack) == type) {
                int currentSouls = registry.getStoredSouls(stack);
                int nextSouls = currentSouls + 1;

                if (nextSouls >= requiredSouls) {
                    // Completar el frasco
                    ItemStack filledJar = registry.createFilledJar(type, requiredSouls);
                    if (stack.getAmount() > 1) {
                        stack.setAmount(stack.getAmount() - 1);
                        Map<Integer, ItemStack> leftover = killer.getInventory().addItem(filledJar);
                        for (ItemStack drop : leftover.values()) {
                            killer.getWorld().dropItemNaturally(killer.getLocation(), drop);
                        }
                    } else {
                        killer.getInventory().setItem(slot, filledJar);
                    }

                    killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
                    killer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&b[DrakesMagic · SoulJars] &6¡Has saturado completamente un frasco con el alma de &e"
                                    + SoulJarRegistry.humanize(type.name()) + "&6!"));
                } else {
                    // Incrementar el frasco en progreso
                    ItemStack updatedJar = registry.createSoulJar(type, nextSouls, requiredSouls);
                    if (stack.getAmount() > 1) {
                        stack.setAmount(stack.getAmount() - 1);
                        Map<Integer, ItemStack> leftover = killer.getInventory().addItem(updatedJar);
                        for (ItemStack drop : leftover.values()) {
                            killer.getWorld().dropItemNaturally(killer.getLocation(), drop);
                        }
                    } else {
                        killer.getInventory().setItem(slot, updatedJar);
                    }
                }
                return;
            }
        }

        // 2. Si no hay frasco en progreso para este mob, buscar un frasco vacío
        for (int slot = 0; slot < killer.getInventory().getSize(); slot++) {
            ItemStack stack = killer.getInventory().getItem(slot);
            if (stack == null || stack.getType().isAir()) continue;

            if (registry.isEmptyJar(stack)) {
                // Consumir 1 frasco vacío
                if (stack.getAmount() > 1) {
                    stack.setAmount(stack.getAmount() - 1);
                } else {
                    killer.getInventory().setItem(slot, null);
                }

                // Otorgar frasco iniciado con 1 alma
                ItemStack initialJar = registry.createSoulJar(type, 1, requiredSouls);
                Map<Integer, ItemStack> leftover = killer.getInventory().addItem(initialJar);
                for (ItemStack drop : leftover.values()) {
                    killer.getWorld().dropItemNaturally(killer.getLocation(), drop);
                }

                killer.playSound(killer.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.7f, 1.5f);
                killer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&b[DrakesMagic · SoulJars] &7Comenzando recolección de almas para: &e"
                                + SoulJarRegistry.humanize(type.name()) + " &7(1/" + requiredSouls + ")"));
                return;
            }
        }
    }
}

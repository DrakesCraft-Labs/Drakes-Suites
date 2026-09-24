package com.drakescraft.suites.core.compat;

import com.drakescraft.suites.core.runtime.PurpurRuntimeProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de compatibilidad cruzada universal entre Paper/Purpur 1.21.11 y Purpur/Paper 26.X.
 * 
 * Permite que todas las suites y plugins consolidados se ejecuten transparentemente
 * en ambas versiones sin romper por cambios de bytecode, Adventure 4 vs 5, o
 * transicion de componentes de Paper.
 * 
 * Autor: JackStar (JackStar6677-1)
 */
public final class CrossVersionAdapter {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    private CrossVersionAdapter() {}

    /**
     * Parsea un texto con soporte dual: MiniMessage (<gradient>, <color>) y colores legacy (&a, &b).
     */
    public static Component parseComponent(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        if (text.contains("<") && text.contains(">")) {
            try {
                return MINI_MESSAGE.deserialize(text);
            } catch (Throwable ignored) {}
        }
        return LEGACY_SERIALIZER.deserialize(text);
    }

    /**
     * Envia un mensaje enriquecido al CommandSender de forma segura en 1.21.11 y 26.X.
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender == null || message == null) return;
        sender.sendMessage(parseComponent(message));
    }

    /**
     * Envia un Action Bar a un jugador compatible con Adventure 4 y Adventure 5.
     */
    public static void sendActionBar(Player player, String message) {
        if (player == null || message == null) return;
        player.sendActionBar(parseComponent(message));
    }

    /**
     * Aplica nombre visible a un item de forma segura preservando Data Components.
     */
    public static void setItemName(ItemStack item, String displayName) {
        if (item == null || item.getType().isAir()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        setItemName(meta, displayName);
        item.setItemMeta(meta);
    }

    /**
     * Aplica nombre visible directamente a un ItemMeta.
     */
    public static void setItemName(ItemMeta meta, String displayName) {
        if (meta == null || displayName == null) return;
        meta.displayName(parseComponent(displayName));
    }

    /**
     * Aplica lore a un item compatible con 1.21.11 y 26.X.
     */
    public static void setItemLore(ItemStack item, List<String> loreLines) {
        if (item == null || item.getType().isAir() || loreLines == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        setItemLore(meta, loreLines);
        item.setItemMeta(meta);
    }

    /**
     * Aplica lore directamente a un ItemMeta.
     */
    public static void setItemLore(ItemMeta meta, List<String> loreLines) {
        if (meta == null || loreLines == null) return;
        List<Component> components = new ArrayList<>(loreLines.size());
        for (String line : loreLines) {
            components.add(parseComponent(line));
        }
        meta.lore(components);
    }


    /**
     * Aplica CustomModelData al item de forma retrocompatible.
     */
    public static void setCustomModelData(ItemStack item, int cmd) {
        if (item == null || item.getType().isAir()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setCustomModelData(cmd);
        item.setItemMeta(meta);
    }

    /**
     * Obtiene el CustomModelData del item si lo posee.
     */
    public static Integer getCustomModelData(ItemStack item) {
        if (item == null || item.getType().isAir()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasCustomModelData()) return null;
        return meta.getCustomModelData();
    }

    /**
     * Verifica si el entorno soporta las optimizaciones nativas de Purpur 26.2.
     */
    public static boolean isNextGenPurpur() {
        return PurpurRuntimeProvider.isPurpur();
    }
}

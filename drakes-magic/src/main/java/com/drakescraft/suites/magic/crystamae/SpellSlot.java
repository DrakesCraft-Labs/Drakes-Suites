package com.drakescraft.suites.magic.crystamae;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import javax.annotation.Nullable;

/**
 * Triggers de invocación en báculos mágicos de Crystamae.
 */
public enum SpellSlot {
    LEFT_CLICK(1, "Clic Izquierdo"),
    RIGHT_CLICK(2, "Clic Derecho"),
    SHIFT_LEFT_CLICK(3, "Agachado + Clic Izquierdo"),
    SHIFT_RIGHT_CLICK(4, "Agachado + Clic Derecho");

    private final int slotId;
    private final String description;

    SpellSlot(int slotId, String description) {
        this.slotId = slotId;
        this.description = description;
    }

    public int getSlotId() {
        return slotId;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public static SpellSlot fromAction(Player player, Action action) {
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            return player.isSneaking() ? SHIFT_LEFT_CLICK : LEFT_CLICK;
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            return player.isSneaking() ? SHIFT_RIGHT_CLICK : RIGHT_CLICK;
        }
        return null;
    }

    @Nullable
    public static SpellSlot getBySlotId(int slotId) {
        for (SpellSlot slot : values()) {
            if (slot.slotId == slotId) {
                return slot;
            }
        }
        return null;
    }
}

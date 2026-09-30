package com.drakescraft.suites.utility.endercarryon;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

/**
 * PDC and CustomModelData keys for EnderCarryOn.
 */
public final class CarryKeys {
    public static final NamespacedKey CARRY = new NamespacedKey("endercarryon", "carry");
    public static final NamespacedKey ENTITY_DATA = new NamespacedKey("endercarryon", "entity_data");

    public static final int CHEST_ID = 1001;
    public static final int TRAPPED_CHEST_ID = 1002;
    public static final int ENDER_CHEST_ID = 1003;
    public static final int BARREL_ID = 1004;
    public static final int ENTITY_ID = 2000;

    private CarryKeys() {}

    public static int getCustomModelData(Material material) {
        return switch (material) {
            case CHEST -> CHEST_ID;
            case TRAPPED_CHEST -> TRAPPED_CHEST_ID;
            case ENDER_CHEST -> ENDER_CHEST_ID;
            case BARREL -> BARREL_ID;
            default -> 0;
        };
    }
}

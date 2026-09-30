package com.drakescraft.suites.utility.notenoughaddons;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TerrariaShortsword specification.
 */
public class TerrariaShortsword {

    private static final Map<String, TerrariaShortsword> SWORDS = new HashMap<>();

    static {
        register(NEAItems.SHORTSWORD_COPPER, 5.0, 4.0, 0.04, 13);
        register(NEAItems.SHORTSWORD_TIN, 7.0, 4.5, 0.04, 12);
        register(NEAItems.SHORTSWORD_IRON, 8.0, 4.5, 0.04, 11);
        register(NEAItems.SHORTSWORD_LEAD, 9.0, 5.0, 0.04, 11);
        register(NEAItems.SHORTSWORD_SILVER, 9.0, 5.0, 0.04, 11);
        register(NEAItems.SHORTSWORD_TUNGSTEN, 10.0, 5.0, 0.04, 10);
        register(NEAItems.SHORTSWORD_GOLD, 11.0, 5.0, 0.04, 11);
        register(NEAItems.SHORTSWORD_PLATINUM, 13.0, 5.5, 0.04, 11);
    }

    private static void register(String id, double damage, double knockback, double critChance, int useTime) {
        SWORDS.put(id, new TerrariaShortsword(id, damage, knockback, critChance, useTime));
    }

    @Nullable
    public static TerrariaShortsword getById(String id) {
        return SWORDS.get(id);
    }

    public static Map<String, TerrariaShortsword> getAll() {
        return Collections.unmodifiableMap(SWORDS);
    }

    private final String id;
    private final double damage;
    private final double knockback;
    private final double critChance;
    private final int useTime;

    public TerrariaShortsword(String id, double damage, double knockback, double critChance, int useTime) {
        this.id = id;
        this.damage = damage;
        this.knockback = knockback;
        this.critChance = critChance;
        this.useTime = useTime;
    }

    public String getId() {
        return id;
    }

    public double getDamage() {
        return damage;
    }

    public double getKnockback() {
        return knockback;
    }

    public double getCritChance() {
        return critChance;
    }

    public int getUseTime() {
        return useTime;
    }
}

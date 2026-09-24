package com.drakescraft.suites.bio.slimybees;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

/**
 * Tipos de cromosomas que componen el genoma de una abeja en SlimyBees.
 */
@Getter
public enum BeeChromosomeType {

    SPECIES(new ItemStack(Material.PLAYER_HEAD), false),
    PRODUCTIVITY(new ItemStack(Material.HONEYCOMB), true),
    FERTILITY(new ItemStack(Material.BEE_SPAWN_EGG), true),
    LIFESPAN(new ItemStack(Material.CLOCK), true),
    RANGE(new ItemStack(Material.ELYTRA), true),
    PLANT(new ItemStack(Material.OXEYE_DAISY), false),
    EFFECT(new ItemStack(Material.DRAGON_BREATH), false);

    public static final int CHROMOSOME_COUNT = values().length;

    private final ItemStack displayItem;
    private final boolean displayAllValues;

    BeeChromosomeType(ItemStack displayItem, boolean displayAllValues) {
        this.displayItem = displayItem;
        this.displayAllValues = displayAllValues;
    }

    /**
     * Construye un UID canónico para un alelo dado de este cromosoma.
     */
    public String alleleUid(String name) {
        return (name().toLowerCase(Locale.ROOT) + ":" + name.toLowerCase(Locale.ROOT));
    }
}

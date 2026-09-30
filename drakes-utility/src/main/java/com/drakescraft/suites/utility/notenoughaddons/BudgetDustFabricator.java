package com.drakescraft.suites.utility.notenoughaddons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * BudgetDustFabricator - Low-cost automated dust extraction machine.
 * Extracts basic mineral dusts directly from cobblestone or granite/diorite/andesite.
 */
public class BudgetDustFabricator {

    public static final int ENERGY_CONSUMPTION = 15;
    public static final int CAPACITY = ENERGY_CONSUMPTION * 3;

    private static final List<Material> ACCEPTABLE_INPUTS = Arrays.asList(
            Material.COBBLESTONE,
            Material.ANDESITE,
            Material.DIORITE,
            Material.GRANITE
    );

    private static final List<String> DUST_OUTPUT_IDS = Arrays.asList(
            "IRON_DUST", "GOLD_DUST", "COPPER_DUST",
            "TIN_DUST", "ZINC_DUST", "ALUMINUM_DUST",
            "MAGNESIUM_DUST", "LEAD_DUST", "SILVER_DUST"
    );

    public static boolean isAcceptableInput(@Nullable ItemStack item) {
        if (item == null) return false;
        return ACCEPTABLE_INPUTS.contains(item.getType());
    }

    @Nonnull
    public static List<Material> getAcceptableInputs() {
        return Collections.unmodifiableList(ACCEPTABLE_INPUTS);
    }

    @Nonnull
    public static List<String> getDustOutputIds() {
        return Collections.unmodifiableList(DUST_OUTPUT_IDS);
    }

    public static int getCapacity() {
        return CAPACITY;
    }

    public static int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }
}

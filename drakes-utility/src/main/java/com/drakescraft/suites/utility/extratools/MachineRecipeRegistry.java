package com.drakescraft.suites.utility.extratools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registro de recetas y especificaciones energéticas de la maquinaria de ExtraTools.
 */
public final class MachineRecipeRegistry {

    private static final Map<Material, Material> CONCRETE_RECIPES = new HashMap<>();
    private static final Map<Material, Material> PULVERIZER_RECIPES = new HashMap<>();
    private static final Map<Material, Material> COMPOSTER_RECIPES = new HashMap<>();

    static {
        // Fábrica de Concreto (16 colores)
        registerConcrete(Material.WHITE_CONCRETE_POWDER, Material.WHITE_CONCRETE);
        registerConcrete(Material.ORANGE_CONCRETE_POWDER, Material.ORANGE_CONCRETE);
        registerConcrete(Material.MAGENTA_CONCRETE_POWDER, Material.MAGENTA_CONCRETE);
        registerConcrete(Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE);
        registerConcrete(Material.YELLOW_CONCRETE_POWDER, Material.YELLOW_CONCRETE);
        registerConcrete(Material.LIME_CONCRETE_POWDER, Material.LIME_CONCRETE);
        registerConcrete(Material.PINK_CONCRETE_POWDER, Material.PINK_CONCRETE);
        registerConcrete(Material.GRAY_CONCRETE_POWDER, Material.GRAY_CONCRETE);
        registerConcrete(Material.LIGHT_GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE);
        registerConcrete(Material.CYAN_CONCRETE_POWDER, Material.CYAN_CONCRETE);
        registerConcrete(Material.PURPLE_CONCRETE_POWDER, Material.PURPLE_CONCRETE);
        registerConcrete(Material.BLUE_CONCRETE_POWDER, Material.BLUE_CONCRETE);
        registerConcrete(Material.BROWN_CONCRETE_POWDER, Material.BROWN_CONCRETE);
        registerConcrete(Material.GREEN_CONCRETE_POWDER, Material.GREEN_CONCRETE);
        registerConcrete(Material.RED_CONCRETE_POWDER, Material.RED_CONCRETE);
        registerConcrete(Material.BLACK_CONCRETE_POWDER, Material.BLACK_CONCRETE);

        // Pulverizador
        PULVERIZER_RECIPES.put(Material.COBBLESTONE, Material.GRAVEL);
        PULVERIZER_RECIPES.put(Material.GRAVEL, Material.SAND);
        PULVERIZER_RECIPES.put(Material.SANDSTONE, Material.SAND);
        PULVERIZER_RECIPES.put(Material.RED_SANDSTONE, Material.RED_SAND);

        // Compostador Eléctrico
        COMPOSTER_RECIPES.put(Material.WHEAT, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.OAK_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.SPRUCE_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.BIRCH_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.JUNGLE_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.ACACIA_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.DARK_OAK_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.MANGROVE_LEAVES, Material.DIRT);
        COMPOSTER_RECIPES.put(Material.CHERRY_LEAVES, Material.DIRT);
    }

    private static void registerConcrete(Material powder, Material block) {
        CONCRETE_RECIPES.put(powder, block);
    }

    @Nullable
    public static Material getConcreteOutput(@Nonnull Material powder) {
        return CONCRETE_RECIPES.get(powder);
    }

    @Nullable
    public static Material getPulverizerOutput(@Nonnull Material input) {
        return PULVERIZER_RECIPES.get(input);
    }

    @Nullable
    public static Material getComposterOutput(@Nonnull Material input) {
        return COMPOSTER_RECIPES.get(input);
    }

    public static Map<Material, Material> getConcreteRecipes() {
        return Collections.unmodifiableMap(CONCRETE_RECIPES);
    }

    public static Map<Material, Material> getPulverizerRecipes() {
        return Collections.unmodifiableMap(PULVERIZER_RECIPES);
    }

    public static Map<Material, Material> getComposterRecipes() {
        return Collections.unmodifiableMap(COMPOSTER_RECIPES);
    }

    private MachineRecipeRegistry() {}
}

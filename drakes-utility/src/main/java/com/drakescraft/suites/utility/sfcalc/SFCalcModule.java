package com.drakescraft.suites.utility.sfcalc;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Módulo nativo SFCalc integrado en DrakesUtility.
 * Calcula árboles de crafteo recursivos, costos de materiales y componentes intermedios de Slimefun.
 */
public class SFCalcModule extends AbstractSuiteModule {

    private RecipeTreeCalculator calculator;
    private int depthLimit = 10;
    private boolean useCache = true;

    public SFCalcModule(JavaPlugin plugin) {
        super(plugin, "sfcalc", "SFCalc Recipe & Resource Calculator");
    }

    @Override
    public void onEnable() {
        this.depthLimit = Math.max(1, getConfig().getInt("features.depth-limit", 10));
        this.useCache = getConfig().getBoolean("performance.in-memory-tree-cache", true);

        this.calculator = new RecipeTreeCalculator(depthLimit, useCache);
        registerDefaultSlimefunRecipes();

        getPlugin().getLogger().info("[SFCalc] Módulo de cálculo de recetas inicializado (Profundidad máx: "
                + depthLimit + ", Cache: " + useCache + ").");
    }

    @Override
    public void onDisable() {
        if (calculator != null) {
            calculator.clearCache();
        }
        getPlugin().getLogger().info("[SFCalc] Módulo de cálculo deshabilitado.");
    }

    private void registerDefaultSlimefunRecipes() {
        // Recetas base de aleaciones estándar de Slimefun
        calculator.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "STEEL_INGOT", 1,
                List.of(
                        new RecipeTreeCalculator.RecipeInput("IRON_INGOT", "Lingote de Hierro", Material.IRON_INGOT, 1),
                        new RecipeTreeCalculator.RecipeInput("COAL", "Carbón", Material.COAL, 2)
                )
        ));

        calculator.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "DAMASCUS_STEEL_INGOT", 1,
                List.of(
                        new RecipeTreeCalculator.RecipeInput("STEEL_INGOT", "Lingote de Acero", Material.IRON_INGOT, 2),
                        new RecipeTreeCalculator.RecipeInput("CARBON", "Carbono Comprimido", Material.CHARCOAL, 1)
                )
        ));

        calculator.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "HARDENED_METAL_INGOT", 1,
                List.of(
                        new RecipeTreeCalculator.RecipeInput("DAMASCUS_STEEL_INGOT", "Acero de Damasco", Material.IRON_INGOT, 2),
                        new RecipeTreeCalculator.RecipeInput("COMPRESSED_CARBON", "Carbono Cuántico", Material.COAL_BLOCK, 1)
                )
        ));

        calculator.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "REINFORCED_ALLOY_INGOT", 1,
                List.of(
                        new RecipeTreeCalculator.RecipeInput("HARDENED_METAL_INGOT", "Metal Endurecido", Material.NETHERITE_INGOT, 1),
                        new RecipeTreeCalculator.RecipeInput("DIAMOND", "Diamante", Material.DIAMOND, 4)
                )
        ));
    }

    public RecipeTreeCalculator getCalculator() {
        return calculator;
    }

    public int getDepthLimit() {
        return depthLimit;
    }

    public boolean isUseCache() {
        return useCache;
    }
}

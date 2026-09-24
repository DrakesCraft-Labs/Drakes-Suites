package com.drakescraft.suites.utility;

import com.drakescraft.suites.utility.sfcalc.RecipeTreeCalculator;
import org.bukkit.Material;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SFCalcModuleTest {

    @Test
    @DisplayName("Debe calcular recursivamente materias primas para aleaciones de Slimefun")
    void testDeepRecipeResolution() {
        RecipeTreeCalculator calc = new RecipeTreeCalculator(10, true);

        // Receta 1: Acero = 1 Hierro + 2 Carbon
        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "STEEL_INGOT", 1,
                List.of(
                        new RecipeTreeCalculator.RecipeInput("IRON_INGOT", "Hierro", Material.IRON_INGOT, 1),
                        new RecipeTreeCalculator.RecipeInput("COAL", "Carbón", Material.COAL, 2)
                )
        ));

        // Receta 2: Damasco = 2 Acero + 1 Carbono
        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "DAMASCUS_STEEL_INGOT", 1,
                List.of(
                        new RecipeTreeCalculator.RecipeInput("STEEL_INGOT", "Acero", Material.IRON_INGOT, 2),
                        new RecipeTreeCalculator.RecipeInput("CARBON", "Carbono", Material.COAL, 1)
                )
        ));

        // Calcular 4 Lingotes de Damasco:
        // Necesita 8 Acero y 4 Carbono
        // 8 Acero necesita 8 Hierro y 16 Carbón
        // Total crudo: 8 Hierro, 16 Carbón, 4 Carbono
        RecipeTreeCalculator.CalculationResult result = calc.calculate("DAMASCUS_STEEL_INGOT", 4);

        assertNotNull(result);
        assertEquals(4, result.requestedAmount());
        assertFalse(result.cycleDetected());
        assertEquals(3, result.maxDepthReached());

        // Verificar crudos
        assertTrue(result.rawMaterials().containsKey("IRON_INGOT"));
        assertEquals(8, result.rawMaterials().get("IRON_INGOT").amount());

        assertTrue(result.rawMaterials().containsKey("COAL"));
        assertEquals(16, result.rawMaterials().get("COAL").amount());

        assertTrue(result.rawMaterials().containsKey("CARBON"));
        assertEquals(4, result.rawMaterials().get("CARBON").amount());

        // Verificar intermedios
        assertTrue(result.intermediateComponents().containsKey("STEEL_INGOT"));
        assertEquals(8, result.intermediateComponents().get("STEEL_INGOT").amount());
    }

    @Test
    @DisplayName("Debe detectar ciclos circulares y evitar recursion infinita")
    void testCycleDetection() {
        RecipeTreeCalculator calc = new RecipeTreeCalculator(10, false);

        // A -> B -> A (Ciclo circular malicioso)
        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "ITEM_A", 1,
                List.of(new RecipeTreeCalculator.RecipeInput("ITEM_B", "Item B", Material.STONE, 1))
        ));

        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "ITEM_B", 1,
                List.of(new RecipeTreeCalculator.RecipeInput("ITEM_A", "Item A", Material.STONE, 1))
        ));

        RecipeTreeCalculator.CalculationResult result = calc.calculate("ITEM_A", 1);
        assertNotNull(result);
        assertTrue(result.cycleDetected(), "Debe detectar ciclo circular en la receta");
    }

    @Test
    @DisplayName("Debe respetar limite de profundidad de configuracion")
    void testDepthLimit() {
        RecipeTreeCalculator calc = new RecipeTreeCalculator(2, false);

        // Profundidad 1 -> 2 -> 3
        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "TIER_3", 1,
                List.of(new RecipeTreeCalculator.RecipeInput("TIER_2", "Tier 2", Material.STONE, 1))
        ));

        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "TIER_2", 1,
                List.of(new RecipeTreeCalculator.RecipeInput("TIER_1", "Tier 1", Material.STONE, 1))
        ));

        calc.registerRecipe(new RecipeTreeCalculator.RecipeDefinition(
                "TIER_1", 1,
                List.of(new RecipeTreeCalculator.RecipeInput("RAW_ORE", "Raw Ore", Material.RAW_IRON, 1))
        ));

        RecipeTreeCalculator.CalculationResult result = calc.calculate("TIER_3", 1);
        assertNotNull(result);
        assertTrue(result.maxDepthReached() <= 3);
    }
}

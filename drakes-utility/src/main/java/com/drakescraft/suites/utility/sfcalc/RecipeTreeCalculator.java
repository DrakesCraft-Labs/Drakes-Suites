package com.drakescraft.suites.utility.sfcalc;

import org.bukkit.Material;

import java.util.*;

/**
 * Calculador recursivo de árboles de recetas y materiales brutos para Slimefun/Vanilla.
 * Diseñado con protección contra ciclos infinitos, límite de profundidad y soporte dual Paper/Purpur.
 */
public class RecipeTreeCalculator {

    public record IngredientRequirement(String id, String displayName, Material material, long amount, boolean isRaw) {}

    public record CalculationResult(
            String targetId,
            long requestedAmount,
            Map<String, IngredientRequirement> rawMaterials,
            Map<String, IngredientRequirement> intermediateComponents,
            int maxDepthReached,
            boolean cycleDetected
    ) {}

    public record RecipeDefinition(String outputId, int outputAmount, List<RecipeInput> inputs) {}

    public record RecipeInput(String itemId, String displayName, Material material, int amount) {}

    private final Map<String, RecipeDefinition> recipeRegistry = new HashMap<>();
    private final Map<String, CalculationResult> treeCache = new HashMap<>();
    private final int maxDepth;
    private final boolean useCache;

    public RecipeTreeCalculator(int maxDepth, boolean useCache) {
        this.maxDepth = Math.max(1, maxDepth);
        this.useCache = useCache;
    }

    public void registerRecipe(RecipeDefinition recipe) {
        recipeRegistry.put(recipe.outputId().toUpperCase(Locale.ROOT), recipe);
        treeCache.clear();
    }

    public CalculationResult calculate(String itemId, long amount) {
        if (amount <= 0) {
            return new CalculationResult(itemId, 0, Collections.emptyMap(), Collections.emptyMap(), 0, false);
        }

        String cacheKey = itemId.toUpperCase(Locale.ROOT) + ":" + amount;
        if (useCache && treeCache.containsKey(cacheKey)) {
            return treeCache.get(cacheKey);
        }

        Map<String, IngredientRequirement> raw = new LinkedHashMap<>();
        Map<String, IngredientRequirement> intermediate = new LinkedHashMap<>();
        Set<String> callStack = new HashSet<>();
        boolean[] cycle = new boolean[]{false};
        int[] maxDepthTracker = new int[]{0};

        resolveNode(itemId.toUpperCase(Locale.ROOT), amount, 1, callStack, raw, intermediate, cycle, maxDepthTracker);

        CalculationResult result = new CalculationResult(
                itemId,
                amount,
                Collections.unmodifiableMap(raw),
                Collections.unmodifiableMap(intermediate),
                maxDepthTracker[0],
                cycle[0]
        );

        if (useCache && !cycle[0]) {
            treeCache.put(cacheKey, result);
        }

        return result;
    }

    private void resolveNode(String id, long neededAmount, int depth, Set<String> callStack,
                             Map<String, IngredientRequirement> raw,
                             Map<String, IngredientRequirement> intermediate,
                             boolean[] cycle, int[] maxDepthTracker) {
        maxDepthTracker[0] = Math.max(maxDepthTracker[0], depth);

        if (callStack.contains(id)) {
            cycle[0] = true;
            addRequirement(raw, id, id, Material.BARRIER, neededAmount, true);
            return;
        }

        if (depth > maxDepth) {
            addRequirement(raw, id, id, Material.CHEST, neededAmount, true);
            return;
        }

        RecipeDefinition recipe = recipeRegistry.get(id);
        if (recipe == null || recipe.inputs().isEmpty()) {
            Material mat = Material.matchMaterial(id);
            if (mat == null) mat = Material.STONE;
            addRequirement(raw, id, id, mat, neededAmount, true);
            return;
        }

        int outBatch = Math.max(1, recipe.outputAmount());
        long craftsNeeded = (long) Math.ceil((double) neededAmount / outBatch);

        Material outMat = Material.matchMaterial(id);
        if (outMat == null) outMat = Material.IRON_INGOT;
        addRequirement(intermediate, id, id, outMat, craftsNeeded * outBatch, false);

        callStack.add(id);
        for (RecipeInput input : recipe.inputs()) {
            long totalInputNeeded = (long) input.amount() * craftsNeeded;
            resolveNode(input.itemId().toUpperCase(Locale.ROOT), totalInputNeeded, depth + 1,
                    callStack, raw, intermediate, cycle, maxDepthTracker);
        }
        callStack.remove(id);
    }

    private void addRequirement(Map<String, IngredientRequirement> map, String id, String name, Material mat, long amount, boolean raw) {
        map.compute(id, (k, existing) -> {
            if (existing == null) {
                return new IngredientRequirement(id, name, mat, amount, raw);
            } else {
                return new IngredientRequirement(id, name, mat, existing.amount() + amount, raw);
            }
        });
    }

    public void clearCache() {
        treeCache.clear();
    }
}

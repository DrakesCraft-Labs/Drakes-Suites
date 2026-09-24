package com.drakescraft.suites.bio.exoticgarden;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Registro canónico de identificadores PDC y configuración de ítems de Exotic Garden.
 * Preserva 100% de paridad con las claves de Slimefun: `slimefun:slimefun_item`.
 */
@Getter
public class ExoticGardenRegistry {

    private final Set<String> berries = new HashSet<>();
    private final Set<String> crops = new HashSet<>();
    private final Set<String> trees = new HashSet<>();
    private final Set<String> tools = new HashSet<>();
    private final Map<String, Integer> foodValues = new HashMap<>();

    public ExoticGardenRegistry() {
        registerBerries();
        registerCrops();
        registerTrees();
        registerToolsAndKitchen();
        registerFoodValues();
    }

    private void registerBerries() {
        Collections.addAll(berries,
                "GRAPE",
                "BLUEBERRY",
                "ELDERBERRY",
                "RASPBERRY",
                "BLACKBERRY",
                "CRANBERRY",
                "COWBERRY",
                "STRAWBERRY"
        );
    }

    private void registerCrops() {
        Collections.addAll(crops,
                "TOMATO",
                "LETTUCE",
                "TEA_LEAF",
                "CABBAGE",
                "SWEET_POTATO",
                "MUSTARD_SEED",
                "CURRY_LEAF",
                "ONION",
                "GARLIC",
                "CILANTRO",
                "BLACK_PEPPER",
                "CORN",
                "PINEAPPLE",
                "RED_BELL_PEPPER"
        );
    }

    private void registerTrees() {
        Collections.addAll(trees,
                "OAK_APPLE",
                "COCONUT",
                "CHERRY",
                "POMEGRANATE",
                "LEMON",
                "PLUM",
                "LIME",
                "ORANGE",
                "PEACH",
                "PEAR",
                "DRAGON_FRUIT"
        );
    }

    private void registerToolsAndKitchen() {
        Collections.addAll(tools,
                "CROOK",
                "KITCHEN",
                "ICE_CUBE",
                "GRASS_SEEDS"
        );
    }

    private void registerFoodValues() {
        foodValues.put("GRAPE", 2);
        foodValues.put("BLUEBERRY", 2);
        foodValues.put("ELDERBERRY", 2);
        foodValues.put("RASPBERRY", 2);
        foodValues.put("BLACKBERRY", 2);
        foodValues.put("CRANBERRY", 2);
        foodValues.put("COWBERRY", 2);
        foodValues.put("STRAWBERRY", 3);
        foodValues.put("TOMATO", 3);
        foodValues.put("LETTUCE", 2);
        foodValues.put("CABBAGE", 3);
        foodValues.put("SWEET_POTATO", 4);
        foodValues.put("CORN", 4);
        foodValues.put("PINEAPPLE", 5);
        foodValues.put("RED_BELL_PEPPER", 3);
        foodValues.put("OAK_APPLE", 4);
        foodValues.put("COCONUT", 3);
        foodValues.put("CHERRY", 2);
        foodValues.put("POMEGRANATE", 3);
        foodValues.put("LEMON", 2);
        foodValues.put("PLUM", 3);
        foodValues.put("LIME", 2);
        foodValues.put("ORANGE", 3);
        foodValues.put("PEACH", 3);
        foodValues.put("PEAR", 3);
        foodValues.put("DRAGON_FRUIT", 5);
    }

    public boolean isExoticBerry(String id) {
        return id != null && berries.contains(id.toUpperCase());
    }

    public boolean isExoticCrop(String id) {
        return id != null && crops.contains(id.toUpperCase());
    }

    public boolean isExoticTree(String id) {
        return id != null && trees.contains(id.toUpperCase());
    }

    public boolean isExoticPlantOrFruit(String id) {
        return isExoticBerry(id) || isExoticCrop(id) || isExoticTree(id);
    }

    public boolean isCrook(String id) {
        return "CROOK".equalsIgnoreCase(id);
    }

    public int getFoodValue(String id) {
        if (id == null) return 2;
        return foodValues.getOrDefault(id.toUpperCase(), 2);
    }
}

package com.drakescraft.suites.generators.ecopower;

import org.bukkit.Material;

/**
 * Tipos de generadores ecológicos y sostenibles de la suite DrakesGenerators.
 */
public enum EcoGeneratorType {
    WIND_TURBINE("ECO_WIND_TURBINE", "Turbina Eólica", Material.IRON_BLOCK, 32, 128),
    STEAM_TURBINE("ECO_STEAM_TURBINE", "Turbina de Vapor Geotérmica", Material.SMOOTH_STONE_SLAB, 64, 256),
    HIGH_ENERGY_SOLAR("ECO_SOLAR_HIGH", "Generador Solar de Alta Energía", Material.DAYLIGHT_DETECTOR, 128, 512),
    LUNAR_GENERATOR("ECO_LUNAR_GENERATOR", "Generador Lunar Nocturno", Material.DAYLIGHT_DETECTOR, 64, 256),
    LIGHTNING_RECEPTOR("ECO_LIGHTNING_RECEPTOR", "Receptor de Tormentas y Rayos", Material.LIGHTNING_ROD, 512, 2048);

    private final String slimefunId;
    private final String defaultName;
    private final Material material;
    private final int baseOutput;
    private final int capacity;

    EcoGeneratorType(String slimefunId, String defaultName, Material material, int baseOutput, int capacity) {
        this.slimefunId = slimefunId;
        this.defaultName = defaultName;
        this.material = material;
        this.baseOutput = baseOutput;
        this.capacity = capacity;
    }

    public String getSlimefunId() {
        return slimefunId;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getBaseOutput() {
        return baseOutput;
    }

    public int getCapacity() {
        return capacity;
    }
}

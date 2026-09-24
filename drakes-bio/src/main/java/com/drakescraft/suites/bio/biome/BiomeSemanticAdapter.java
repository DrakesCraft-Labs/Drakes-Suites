package com.drakescraft.suites.bio.biome;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador Semántico de Biomas para Datapacks (Terralith, Incendium, etc.).
 * Permite que los módulos biológicos de Slimefun (ExoticGarden, SlimyBees, etc.) reconozcan
 * dinámicamente biomas personalizados y los mapeen a sus arquetipos canónicos de Vanilla
 * según morfología, temperatura, precipitación y tipos de suelo.
 */
public class BiomeSemanticAdapter {

    private static final BiomeSemanticAdapter INSTANCE = new BiomeSemanticAdapter();

    private final Map<String, Biome> customBiomeCache = new ConcurrentHashMap<>();

    // Suelos tolerantes ampliados (incluyendo bloques de Terralith y 1.21+)
    private static final Set<Material> COMPATIBLE_SOILS = Set.of(
            Material.GRASS_BLOCK,
            Material.DIRT,
            Material.COARSE_DIRT,
            Material.ROOTED_DIRT,
            Material.PODZOL,
            Material.MYCELIUM,
            Material.MUD,
            Material.MUDDY_MANGROVE_ROOTS,
            Material.MOSS_BLOCK,
            Material.FARMLAND
    );

    public static BiomeSemanticAdapter getInstance() {
        return INSTANCE;
    }

    /**
     * Resuelve un bioma de Bukkit o clave de datapack a su bioma Vanilla más cercano.
     */
    public Biome resolveVanillaEquivalent(Biome biome, String namespacedKey, double temperature, boolean hasPrecipitation) {
        if (biome != null && biome != Biome.CUSTOM && (namespacedKey == null || namespacedKey.startsWith("minecraft:"))) {
            return biome;
        }

        if (namespacedKey == null || namespacedKey.isBlank()) {
            return fallbackFromClimate(temperature, hasPrecipitation);
        }

        String keyLower = namespacedKey.toLowerCase(Locale.ROOT);
        return customBiomeCache.computeIfAbsent(keyLower, k -> deduceVanillaBiome(k, temperature, hasPrecipitation));
    }

    /**
     * Resuelve el bioma a partir de un bloque en el mundo.
     */
    public Biome resolveBlockBiome(Block block) {
        if (block == null) return Biome.PLAINS;
        Biome directBiome = block.getBiome();
        if (directBiome != Biome.CUSTOM) {
            return directBiome;
        }

        // Si es CUSTOM, deducir por temperatura y precipitaciones del bloque
        return fallbackFromClimate(block.getTemperature(), block.getWorld().hasStorm());
    }

    private Biome deduceVanillaBiome(String key, double temperature, boolean hasPrecipitation) {
        // 1. Áridos / Desiertos / Badlands
        if (key.contains("desert") || key.contains("badlands") || key.contains("canyon")
                || key.contains("mesa") || key.contains("dune") || key.contains("sahara")) {
            return key.contains("badlands") || key.contains("canyon") || key.contains("mesa")
                    ? Biome.BADLANDS : Biome.DESERT;
        }

        // 2. Florales / Praderas / Huertos
        if (key.contains("bloom") || key.contains("flower") || key.contains("meadow")
                || key.contains("orchard") || key.contains("pasture") || key.contains("valley")) {
            return Biome.FLOWER_FOREST;
        }

        // 3. Selvas / Junglas / Rainforests
        if (key.contains("jungle") || key.contains("rainforest") || key.contains("tropical")) {
            return Biome.JUNGLE;
        }

        // 4. Fríos / Nevados / Taigas
        if (key.contains("snow") || key.contains("ice") || key.contains("frozen")
                || key.contains("wintry") || key.contains("tundra") || key.contains("frost")
                || key.contains("alpine") || key.contains("glacier") || key.contains("taiga")) {
            return key.contains("taiga") ? Biome.TAIGA : Biome.SNOWY_PLAINS;
        }

        // 5. Pantanos / Humedales
        if (key.contains("swamp") || key.contains("marsh") || key.contains("bog")
                || key.contains("bayou") || key.contains("wetland")) {
            return Biome.SWAMP;
        }

        // 6. Bosques Templados
        if (key.contains("forest") || key.contains("wood") || key.contains("grove")
                || key.contains("birch") || key.contains("pine")) {
            return key.contains("dark") ? Biome.DARK_FOREST : Biome.FOREST;
        }

        // 7. Sabanas / Estepas
        if (key.contains("savanna") || key.contains("steppe") || key.contains("scrub") || key.contains("plateau")) {
            return Biome.SAVANNA;
        }

        // 8. Océanos
        if (key.contains("ocean") || key.contains("sea") || key.contains("trench") || key.contains("reef")) {
            return Biome.OCEAN;
        }

        // 9. Cuevas / Subterráneo
        if (key.contains("cave") || key.contains("lush") || key.contains("dripstone")) {
            return Biome.LUSH_CAVES;
        }

        // Fallback por clima físico
        return fallbackFromClimate(temperature, hasPrecipitation);
    }

    private Biome fallbackFromClimate(double temperature, boolean hasPrecipitation) {
        if (temperature >= 1.5) {
            return Biome.DESERT;
        } else if (temperature <= 0.15) {
            return Biome.SNOWY_PLAINS;
        } else if (temperature >= 0.7 && hasPrecipitation) {
            return Biome.FOREST;
        } else {
            return Biome.PLAINS;
        }
    }

    /**
     * Determina si un suelo es apto para el cultivo o crecimiento biológico en biomas custom.
     */
    public boolean isCompatibleSoil(Material material) {
        return material != null && COMPATIBLE_SOILS.contains(material);
    }

    /**
     * Determina si el bioma es propicio para la polinización y aparición de abejas.
     */
    public boolean isBeeFriendly(Biome biome, String namespacedKey) {
        Biome resolved = resolveVanillaEquivalent(biome, namespacedKey, 0.7, true);
        return resolved == Biome.PLAINS
                || resolved == Biome.FLOWER_FOREST
                || resolved == Biome.MEADOW
                || resolved == Biome.FOREST
                || resolved == Biome.BIRCH_FOREST
                || resolved == Biome.OLD_GROWTH_BIRCH_FOREST
                || resolved == Biome.CHERRY_GROVE;
    }

    public void clearCache() {
        customBiomeCache.clear();
    }
}

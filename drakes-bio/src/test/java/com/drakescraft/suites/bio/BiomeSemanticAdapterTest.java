package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.biome.BiomeSemanticAdapter;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiomeSemanticAdapterTest {

    private BiomeSemanticAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = BiomeSemanticAdapter.getInstance();
        adapter.clearCache();
    }

    @Test
    @DisplayName("Biomas Vanilla estándar se preservan directamente")
    void testVanillaBiomesPreserved() {
        Biome result = adapter.resolveVanillaEquivalent(Biome.PLAINS, "minecraft:plains", 0.7, true);
        assertEquals(Biome.PLAINS, result);

        Biome desert = adapter.resolveVanillaEquivalent(Biome.DESERT, "minecraft:desert", 2.0, false);
        assertEquals(Biome.DESERT, desert);
    }

    @Test
    @DisplayName("Biomas de Terralith son mapeados semánticamente a equivalentes Vanilla")
    void testTerralithBiomeMapping() {
        // Blooming Plateau / Valley -> FLOWER_FOREST
        Biome bloom = adapter.resolveVanillaEquivalent(Biome.CUSTOM, "terralith:blooming_plateau", 0.8, true);
        assertEquals(Biome.FLOWER_FOREST, bloom);

        // Amethyst Canyon -> BADLANDS
        Biome canyon = adapter.resolveVanillaEquivalent(Biome.CUSTOM, "terralith:amethyst_canyon", 1.8, false);
        assertEquals(Biome.BADLANDS, canyon);

        // Wintry Lowlands / Alpine Grove -> SNOWY_PLAINS
        Biome cold = adapter.resolveVanillaEquivalent(Biome.CUSTOM, "terralith:wintry_lowlands", 0.05, false);
        assertEquals(Biome.SNOWY_PLAINS, cold);

        // Rainforest / Tropical -> JUNGLE
        Biome jungle = adapter.resolveVanillaEquivalent(Biome.CUSTOM, "terralith:tropical_jungle", 1.2, true);
        assertEquals(Biome.JUNGLE, jungle);

        // Marsh / Bayou -> SWAMP
        Biome swamp = adapter.resolveVanillaEquivalent(Biome.CUSTOM, "terralith:bayou", 0.8, true);
        assertEquals(Biome.SWAMP, swamp);
    }

    @Test
    @DisplayName("Suelos de Terralith y Vanilla son reconocidos como compatibles")
    void testSoilCompatibility() {
        assertTrue(adapter.isCompatibleSoil(Material.GRASS_BLOCK));
        assertTrue(adapter.isCompatibleSoil(Material.PODZOL));
        assertTrue(adapter.isCompatibleSoil(Material.MYCELIUM));
        assertTrue(adapter.isCompatibleSoil(Material.MUD));
        assertTrue(adapter.isCompatibleSoil(Material.ROOTED_DIRT));
        assertTrue(adapter.isCompatibleSoil(Material.MOSS_BLOCK));

        assertFalse(adapter.isCompatibleSoil(Material.STONE));
        assertFalse(adapter.isCompatibleSoil(Material.NETHERRACK));
    }

    @Test
    @DisplayName("Detección de biomas aptos para abejas (SlimyBees) en biomas custom")
    void testBeeFriendlyBiomes() {
        assertTrue(adapter.isBeeFriendly(Biome.CUSTOM, "terralith:blooming_valley"));
        assertTrue(adapter.isBeeFriendly(Biome.CUSTOM, "terralith:flower_meadow"));
        assertTrue(adapter.isBeeFriendly(Biome.PLAINS, "minecraft:plains"));

        assertFalse(adapter.isBeeFriendly(Biome.CUSTOM, "terralith:amethyst_canyon"));
        assertFalse(adapter.isBeeFriendly(Biome.DESERT, "minecraft:desert"));
    }
}

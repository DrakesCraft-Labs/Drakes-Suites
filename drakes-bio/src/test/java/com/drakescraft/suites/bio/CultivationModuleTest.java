package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.cultivation.CultivationBreedRegistry;
import com.drakescraft.suites.bio.cultivation.CultivationModule;
import com.drakescraft.suites.bio.cultivation.CultivationSeedItem;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CultivationModuleTest {

    private ServerMock server;
    private DrakesBioPlugin plugin;
    private CultivationModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
        module = new CultivationModule(plugin);
        module.onEnable();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Debe contener exactamente los 79 pares que generan 82 combinaciones botánicas de Cultivation")
    void testRegistryCompleteness() {
        assertEquals(79, CultivationBreedRegistry.getRecipeCount(), "Deben existir exactamente 79 pares de cruce distintos");
        assertEquals(82, CultivationBreedRegistry.getTotalOutcomeCount(), "El total de combinaciones (incluyendo los 3 pares duales) debe ser exactamente 82");
    }

    @Test
    @DisplayName("Los cruces deben ser independientes del orden de siembra (A+B == B+A)")
    void testOrderIndependence() {
        String forward = CultivationBreedRegistry.crossBreed("Air", "Light");
        String backward = CultivationBreedRegistry.crossBreed("Light", "Air");

        assertEquals("Chicken", forward);
        assertEquals("Chicken", backward);
    }

    @Test
    @DisplayName("Los 3 pares duales deben producir ambos resultados posibles")
    void testDualOutcomes() {
        // Cow + Sheep -> Bee o Pig
        Set<String> cowSheepOutcomes = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            cowSheepOutcomes.add(CultivationBreedRegistry.crossBreed("Cow", "Sheep"));
        }
        assertTrue(cowSheepOutcomes.contains("Bee"), "Debe poder dar Bee");
        assertTrue(cowSheepOutcomes.contains("Pig"), "Debe poder dar Pig");

        // Coal + Earth -> Raw Copper o Raw Iron
        Set<String> coalEarthOutcomes = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            coalEarthOutcomes.add(CultivationBreedRegistry.crossBreed("Coal", "Earth"));
        }
        assertTrue(coalEarthOutcomes.contains("Raw Copper"), "Debe poder dar Raw Copper");
        assertTrue(coalEarthOutcomes.contains("Raw Iron"), "Debe poder dar Raw Iron");

        // Cobblestone + Earth -> Deepslate o Gravel
        Set<String> cobbleEarthOutcomes = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            cobbleEarthOutcomes.add(CultivationBreedRegistry.crossBreed("Cobblestone", "Earth"));
        }
        assertTrue(cobbleEarthOutcomes.contains("Deepslate"), "Debe poder dar Deepslate");
        assertTrue(cobbleEarthOutcomes.contains("Gravel"), "Debe poder dar Gravel");
    }

    @Test
    @DisplayName("Par inexistente debe retornar null sin excepciones")
    void testNonExistentCombination() {
        assertNull(CultivationBreedRegistry.crossBreed("Diamond", "Netherite"));
        assertFalse(CultivationBreedRegistry.hasCombination("Diamond", "Netherite"));
    }

    @Test
    @DisplayName("Debe crear semillas con metadatos PDC y permitir su extracción")
    void testSeedPdc() {
        ItemStack seed = module.createSeed("Amethyst");
        assertNotNull(seed);

        String plantName = CultivationSeedItem.extractPlantName(seed);
        assertEquals("Amethyst", plantName);

        String sfId = com.drakescraft.suites.core.pdc.SuiteItemPdcBridge.getSlimefunId(seed);
        assertEquals(CultivationSeedItem.SLIMEFUN_ID_PREFIX + "AMETHYST", sfId);
    }
}

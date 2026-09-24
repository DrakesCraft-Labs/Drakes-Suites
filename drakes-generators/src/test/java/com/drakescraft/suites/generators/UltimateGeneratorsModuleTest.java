package com.drakescraft.suites.generators;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.generators.ultimategenerators.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UltimateGeneratorsModuleTest {

    private static ServerMock server;
    private static DrakesGeneratorsPlugin plugin;
    private static UltimateGeneratorsModule module;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesGeneratorsPlugin.class);
        module = (UltimateGeneratorsModule) plugin.getModuleManager().getModule("ultimate_generators");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("UltimateGeneratorsModule está registrado y habilitado en DrakesGenerators")
    void testModuleEnabled() {
        assertNotNull(module, "UltimateGeneratorsModule debe estar registrado");
        assertTrue(module.isEnabled(), "UltimateGeneratorsModule debe estar habilitado");
        assertEquals(128, module.getDieselRate());
        assertEquals(64, module.getBiofuelRate());
        assertEquals(256, module.getDragonBreathRate());
        assertEquals(512, module.getEndlessRate());
    }

    @Test
    @DisplayName("Items canónicos de UltimateGenerators2 preservan su PDC de Slimefun")
    void testCanonicalItemsRegistry() {
        Map<String, ItemStack> items = UltimateGeneratorsItemsRegistry.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                "ENDLESS_GENERATOR", "DIESEL_GENERATOR", "DIESEL_BUCKET",
                "BIOFUEL_GENERATOR", "BIOFUEL_BUCKET", "BIOMASS_EXTRACTION_MACHINE", "BIOMASS_BUCKET",
                "DRAGON_BREATH_GENERATOR", "REACTION_GENERATOR",
                "ENDER_CRYSTAL_GENERATOR", "UG_CAPACITOR_1"
        };

        for (String id : expectedIds) {
            ItemStack item = items.get(id);
            assertNotNull(item, "El ítem " + id + " debe existir en el registro");
            assertEquals(id, SuiteItemPdcBridge.getSlimefunId(item), "El PDC de Slimefun debe coincidir con " + id);
        }
    }

    @Test
    @DisplayName("Registro de combustibles calcula Joules por segundo y residuo adecuado")
    void testFuelProperties() {
        ItemStack diesel = module.getItem("DIESEL_BUCKET");
        GeneratorFuelRegistry.FuelProperty dieselProp = module.getFuelProperty(diesel);
        assertNotNull(dieselProp);
        assertEquals(128, dieselProp.getEnergyPerSecond());
        assertEquals(120, dieselProp.getBurnDurationSeconds());
        assertEquals(15360L, dieselProp.getTotalJoules());
        assertEquals(Material.BUCKET, dieselProp.getResidue());

        ItemStack biofuel = module.getItem("BIOFUEL_BUCKET");
        GeneratorFuelRegistry.FuelProperty bioProp = module.getFuelProperty(biofuel);
        assertNotNull(bioProp);
        assertEquals(64, bioProp.getEnergyPerSecond());
        assertEquals(90, bioProp.getBurnDurationSeconds());
        assertEquals(5760L, bioProp.getTotalJoules());

        ItemStack dragonBreath = new ItemStack(Material.DRAGON_BREATH);
        GeneratorFuelRegistry.FuelProperty dragonProp = module.getFuelProperty(dragonBreath);
        assertNotNull(dragonProp);
        assertEquals(256, dragonProp.getEnergyPerSecond());
        assertEquals(Material.GLASS_BOTTLE, dragonProp.getResidue());
    }

    @Test
    @DisplayName("Refinería de biomasa y síntesis de biocombustibles")
    void testBioRefineryProcessing() {
        // Extracción de biomasa: 8 materias orgánicas + 1 cubo
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(new ItemStack(Material.OAK_SAPLING, 8));
        inputs.add(new ItemStack(Material.BUCKET, 1));

        ItemStack biomass = module.extractBiomass(inputs);
        assertNotNull(biomass);
        assertEquals("BIOMASS_BUCKET", SuiteItemPdcBridge.getSlimefunId(biomass));

        // Refinación de biomasa a biocombustible
        ItemStack biofuel = module.refineBioFuel(biomass);
        assertNotNull(biofuel);
        assertEquals("BIOFUEL_BUCKET", SuiteItemPdcBridge.getSlimefunId(biofuel));

        // Refinación a diésel desde cubo de lava
        ItemStack diesel = module.refineDiesel(new ItemStack(Material.LAVA_BUCKET));
        assertNotNull(diesel);
        assertEquals("DIESEL_BUCKET", SuiteItemPdcBridge.getSlimefunId(diesel));
    }
}

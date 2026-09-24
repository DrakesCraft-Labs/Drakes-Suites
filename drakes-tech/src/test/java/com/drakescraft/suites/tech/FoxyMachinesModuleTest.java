package com.drakescraft.suites.tech;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.tech.foxy.FoxyMachinesModule;
import com.drakescraft.suites.tech.foxy.FoxyRefineryEngine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class FoxyMachinesModuleTest {

    private static ServerMock server;
    private static DrakesTechPlugin plugin;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesTechPlugin.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("FoxyMachinesModule se registra y habilita correctamente")
    void testModuleLifecycle() {
        FoxyMachinesModule module = (FoxyMachinesModule) plugin.getModuleManager().getModule("foxy");
        assertNotNull(module, "El módulo foxy debe estar registrado");
        assertTrue(module.isEnabled(), "El módulo foxy debe estar habilitado");
        assertNotNull(module.getEngine(), "El motor FoxyRefineryEngine no debe ser nulo");
    }

    @Test
    @DisplayName("FoxyRefineryEngine procesa refinado de oro y metales con energía")
    void testRefineryProcessing() {
        FoxyMachinesModule module = (FoxyMachinesModule) plugin.getModuleManager().getModule("foxy");
        FoxyRefineryEngine engine = module.getEngine();

        // Probar refinado con oro en bruto
        ItemStack rawGold = new ItemStack(Material.RAW_GOLD, 4);
        ItemStack refined = engine.processRefinery(rawGold, 50);

        assertNotNull(refined);
        assertEquals(Material.GOLD_INGOT, refined.getType());
        assertEquals(4, refined.getAmount());
        assertEquals("true", SuiteItemPdcBridge.getCustomString(refined, "drakestech", "foxy_refined"));

        // Probar falta de energía
        ItemStack noEnergy = engine.processRefinery(rawGold, 5);
        assertNull(noEnergy, "No debe procesar si no hay suficiente energía");
    }

    @Test
    @DisplayName("FoxyRefineryEngine administra almacenamiento y consumo de energía")
    void testEnergyBuffer() {
        FoxyMachinesModule module = (FoxyMachinesModule) plugin.getModuleManager().getModule("foxy");
        FoxyRefineryEngine engine = module.getEngine();

        String loc = "world:100,64,200";
        assertEquals(0, engine.getEnergy(loc));

        engine.storeEnergy(loc, 500);
        assertEquals(500, engine.getEnergy(loc));

        assertTrue(engine.consumeEnergy(loc, 200));
        assertEquals(300, engine.getEnergy(loc));

        assertFalse(engine.consumeEnergy(loc, 400), "No puede consumir más de lo disponible");
        assertEquals(300, engine.getEnergy(loc));
    }
}

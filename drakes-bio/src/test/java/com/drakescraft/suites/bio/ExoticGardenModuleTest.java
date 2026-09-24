package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.exoticgarden.ExoticGardenModule;
import com.drakescraft.suites.bio.exoticgarden.ExoticGardenRegistry;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class ExoticGardenModuleTest {

    private static ServerMock server;
    private static DrakesBioPlugin plugin;
    private static ExoticGardenModule module;
    private static ExoticGardenRegistry registry;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
        module = (ExoticGardenModule) plugin.getModuleManager().getModule("exotic_garden");
        registry = module.getRegistry();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("ExoticGardenModule está registrado y habilitado en DrakesBio")
    void testModuleEnabled() {
        assertNotNull(module, "ExoticGardenModule debe estar registrado");
        assertTrue(module.isEnabled(), "ExoticGardenModule debe estar habilitado");
        assertTrue(module.isBonemealEnabled(), "Aceleración de crecimiento por bonemeal debe estar activa por defecto");
    }

    @Test
    @DisplayName("Registry contiene los identificadores canónicos de bayas de Exotic Garden")
    void testBerriesPdc() {
        assertTrue(registry.isExoticBerry("GRAPE"));
        assertTrue(registry.isExoticBerry("BLUEBERRY"));
        assertTrue(registry.isExoticBerry("ELDERBERRY"));
        assertTrue(registry.isExoticBerry("RASPBERRY"));
        assertTrue(registry.isExoticBerry("BLACKBERRY"));
        assertTrue(registry.isExoticBerry("CRANBERRY"));
        assertTrue(registry.isExoticBerry("COWBERRY"));
        assertTrue(registry.isExoticBerry("STRAWBERRY"));
    }

    @Test
    @DisplayName("Registry contiene los identificadores canónicos de cultivos y plantas exóticas")
    void testCropsPdc() {
        assertTrue(registry.isExoticCrop("TOMATO"));
        assertTrue(registry.isExoticCrop("LETTUCE"));
        assertTrue(registry.isExoticCrop("TEA_LEAF"));
        assertTrue(registry.isExoticCrop("CABBAGE"));
        assertTrue(registry.isExoticCrop("SWEET_POTATO"));
        assertTrue(registry.isExoticCrop("CORN"));
        assertTrue(registry.isExoticCrop("PINEAPPLE"));
        assertTrue(registry.isExoticCrop("RED_BELL_PEPPER"));
    }

    @Test
    @DisplayName("Registry contiene los identificadores canónicos de árboles frutales")
    void testTreesPdc() {
        assertTrue(registry.isExoticTree("OAK_APPLE"));
        assertTrue(registry.isExoticTree("COCONUT"));
        assertTrue(registry.isExoticTree("CHERRY"));
        assertTrue(registry.isExoticTree("POMEGRANATE"));
        assertTrue(registry.isExoticTree("LEMON"));
        assertTrue(registry.isExoticTree("PLUM"));
        assertTrue(registry.isExoticTree("LIME"));
        assertTrue(registry.isExoticTree("ORANGE"));
        assertTrue(registry.isExoticTree("PEACH"));
        assertTrue(registry.isExoticTree("PEAR"));
        assertTrue(registry.isExoticTree("DRAGON_FRUIT"));
    }

    @Test
    @DisplayName("Valores nutricionales asignados correctamente para frutos exóticos")
    void testFoodValues() {
        assertEquals(3, registry.getFoodValue("TOMATO"));
        assertEquals(5, registry.getFoodValue("PINEAPPLE"));
        assertEquals(5, registry.getFoodValue("DRAGON_FRUIT"));
        assertEquals(2, registry.getFoodValue("UNKNOWN_ITEM"));
    }
}

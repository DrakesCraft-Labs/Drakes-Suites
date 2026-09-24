package com.drakescraft.suites.generators;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.generators.smg.SMGGeneratorEngine;
import com.drakescraft.suites.generators.smg.SMGGeneratorFactory;
import com.drakescraft.suites.generators.smg.SMGMaterialType;
import com.drakescraft.suites.generators.smg.SMGModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class SMGModuleTest {

    private static ServerMock server;
    private static DrakesGeneratorsPlugin plugin;
    private static SMGModule module;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesGeneratorsPlugin.class);
        module = (SMGModule) plugin.getModuleManager().getModule("smg");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("SMGModule está registrado y habilitado en DrakesGenerators")
    void testModuleEnabled() {
        assertNotNull(module, "SMGModule debe estar registrado");
        assertTrue(module.isEnabled(), "SMGModule debe estar habilitado");
        assertNotNull(module.getEngine(), "SMGGeneratorEngine debe estar inicializado");
    }

    @Test
    @DisplayName("Items de generadores preservan PDC canónico de Slimefun")
    void testGeneratorItemsPdc() {
        ItemStack cobbleGen = SMGGeneratorFactory.createGenerator(SMGMaterialType.COBBLESTONE, 4);
        assertNotNull(cobbleGen);
        assertTrue(SMGGeneratorFactory.isMaterialGenerator(cobbleGen));
        assertFalse(SMGGeneratorFactory.isBrokenGenerator(cobbleGen));
        assertEquals("SMG_GENERATOR_COBBLESTONE", SuiteItemPdcBridge.getSlimefunId(cobbleGen));
        assertEquals(SMGMaterialType.COBBLESTONE, SMGGeneratorFactory.getMaterialType(cobbleGen));

        ItemStack brokenStone = SMGGeneratorFactory.createBrokenGenerator(SMGMaterialType.STONE);
        assertNotNull(brokenStone);
        assertTrue(SMGGeneratorFactory.isBrokenGenerator(brokenStone));
        assertFalse(SMGGeneratorFactory.isMaterialGenerator(brokenStone));
        assertEquals("SMG_GENERATOR_STONE_BROKEN", SuiteItemPdcBridge.getSlimefunId(brokenStone));
        assertEquals(SMGMaterialType.STONE, SMGGeneratorFactory.getMaterialType(brokenStone));

        ItemStack guide = SMGGeneratorFactory.createMultiblockGuide();
        assertNotNull(guide);
        assertEquals("SMG_GENERATOR_MULTIBLOCK", SuiteItemPdcBridge.getSlimefunId(guide));
    }

    @Test
    @DisplayName("SMGGeneratorEngine acumula ticks y produce items al alcanzar la cuota")
    void testEngineTickingAndOutput() {
        SMGGeneratorEngine engine = new SMGGeneratorEngine(1.0);
        String blockPos = "world:100:64:200";

        // COBBLESTONE requiere 4 ticks
        assertFalse(engine.stepTick(blockPos, SMGMaterialType.COBBLESTONE));
        assertEquals(1, engine.getProgress(blockPos));

        assertFalse(engine.stepTick(blockPos, SMGMaterialType.COBBLESTONE));
        assertEquals(2, engine.getProgress(blockPos));

        assertFalse(engine.stepTick(blockPos, SMGMaterialType.COBBLESTONE));
        assertEquals(3, engine.getProgress(blockPos));

        // Tick 4: debe activarse
        assertTrue(engine.stepTick(blockPos, SMGMaterialType.COBBLESTONE));
        assertEquals(0, engine.getProgress(blockPos));

        ItemStack output = engine.produceOutput(SMGMaterialType.COBBLESTONE, 1);
        assertNotNull(output);
        assertEquals(Material.COBBLESTONE, output.getType());
        assertEquals(1, output.getAmount());
    }
}

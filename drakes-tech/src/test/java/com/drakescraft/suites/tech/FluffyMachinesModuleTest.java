package com.drakescraft.suites.tech;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.tech.fluffymachines.FluffyCrafterEngine;
import com.drakescraft.suites.tech.fluffymachines.FluffyMachineType;
import com.drakescraft.suites.tech.fluffymachines.FluffyMachinesFactory;
import com.drakescraft.suites.tech.fluffymachines.FluffyMachinesModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class FluffyMachinesModuleTest {

    private static ServerMock server;
    private static DrakesTechPlugin plugin;
    private static FluffyMachinesModule module;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesTechPlugin.class);
        module = (FluffyMachinesModule) plugin.getModuleManager().getModule("fluffymachines");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("FluffyMachinesModule está registrado y habilitado en DrakesTech")
    void testModuleEnabled() {
        assertNotNull(module, "FluffyMachinesModule debe estar registrado");
        assertTrue(module.isEnabled(), "FluffyMachinesModule debe estar habilitado");
        assertNotNull(module.getEngine(), "FluffyCrafterEngine debe estar inicializado");
    }

    @Test
    @DisplayName("Items de máquinas preservan PDC canónico de Slimefun")
    void testMachineItemsPdc() {
        ItemStack autoCraft = FluffyMachinesFactory.createMachine(FluffyMachineType.AUTO_CRAFTING_TABLE);
        assertNotNull(autoCraft);
        assertTrue(FluffyMachinesFactory.isFluffyMachine(autoCraft));
        assertEquals("AUTO_CRAFTING_TABLE", SuiteItemPdcBridge.getSlimefunId(autoCraft));
        assertEquals(FluffyMachineType.AUTO_CRAFTING_TABLE, FluffyMachinesFactory.getMachineType(autoCraft));

        ItemStack altar = FluffyMachinesFactory.createMachine(FluffyMachineType.AUTO_ANCIENT_ALTAR);
        assertNotNull(altar);
        assertTrue(FluffyMachinesFactory.isFluffyMachine(altar));
        assertEquals("AUTO_ANCIENT_ALTAR", SuiteItemPdcBridge.getSlimefunId(altar));
        assertEquals(FluffyMachineType.AUTO_ANCIENT_ALTAR, FluffyMachinesFactory.getMachineType(altar));

        ItemStack dustRecycler = FluffyMachinesFactory.createMachine(FluffyMachineType.ELECTRIC_DUST_RECYCLER);
        assertNotNull(dustRecycler);
        assertEquals("ELECTRIC_DUST_RECYCLER", SuiteItemPdcBridge.getSlimefunId(dustRecycler));
        assertEquals(FluffyMachineType.ELECTRIC_DUST_RECYCLER, FluffyMachinesFactory.getMachineType(dustRecycler));
    }

    @Test
    @DisplayName("FluffyCrafterEngine gestiona tick delays y validación de snapshots")
    void testCrafterEngine() {
        FluffyCrafterEngine engine = new FluffyCrafterEngine(2, true);
        String pos = "world:10:64:20";

        // Delay de 2 ticks
        assertFalse(engine.shouldCraft(pos));
        assertTrue(engine.shouldCraft(pos));

        // Snapshot validation
        ItemStack[] expected = new ItemStack[] {
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.STICK, 1)
        };
        ItemStack[] validInputs = new ItemStack[] {
                new ItemStack(Material.IRON_INGOT, 5),
                new ItemStack(Material.STICK, 2)
        };
        ItemStack[] invalidInputs = new ItemStack[] {
                new ItemStack(Material.IRON_INGOT, 1), // Cantidad insuficiente
                new ItemStack(Material.STICK, 2)
        };

        assertTrue(engine.validateSnapshot(validInputs, expected));
        assertFalse(engine.validateSnapshot(invalidInputs, expected));
    }
}

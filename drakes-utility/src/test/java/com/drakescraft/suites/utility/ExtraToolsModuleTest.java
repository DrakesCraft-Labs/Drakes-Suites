package com.drakescraft.suites.utility;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.utility.extratools.ExtraToolsItemsRegistry;
import com.drakescraft.suites.utility.extratools.ExtraToolsModule;
import com.drakescraft.suites.utility.extratools.HammerEngine;
import com.drakescraft.suites.utility.extratools.MachineRecipeRegistry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExtraToolsModuleTest {

    private ServerMock server;
    private DrakesUtilityPlugin plugin;
    private ExtraToolsModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
        module = (ExtraToolsModule) plugin.getModuleManager().getModule("extratools");
        assertNotNull(module, "ExtraToolsModule debe estar registrado en el ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("El módulo de ExtraTools debe inicializarse y cargarse activo")
    void testModuleRegisteredAndEnabled() {
        assertEquals("extratools", module.getId());
        assertTrue(module.isEnabled());
        assertTrue(module.isHammerEnabled());
        assertEquals(1.0, module.getEnergyConsumptionMultiplier());
        assertEquals(20, module.getCobbleGenIntervalTicks());
    }

    @Test
    @DisplayName("El catálogo de ExtraTools debe contener todos los ítems canónicos con PDC de Slimefun")
    void testCanonicalItemsInRegistry() {
        Map<String, ItemStack> items = ExtraToolsItemsRegistry.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                "HAMMER", "GOLD_TRANSMUTER", "ELECTRIC_COMPOSTER", "ELECTRIC_COMPOSTER_2",
                "COBBLESTONE_GENERATOR", "VAPORIZER", "CONCRETE_FACTORY", "PULVERIZER"
        };

        for (String id : expectedIds) {
            ItemStack item = items.get(id);
            assertNotNull(item, "El ítem " + id + " debe estar registrado en ExtraToolsItemsRegistry");
            assertEquals(id, SuiteItemPdcBridge.getSlimefunId(item),
                    "El ítem " + id + " debe tener su Slimefun PDC idéntico");
        }
    }

    @Test
    @DisplayName("Transformación de gotas pulverizadas con el Martillo")
    void testHammerDrops() {
        ItemStack dropStone = HammerEngine.getPulverizedDrop(Material.STONE);
        assertNotNull(dropStone);
        assertEquals(Material.GRAVEL, dropStone.getType());

        ItemStack dropCobble = HammerEngine.getPulverizedDrop(Material.COBBLESTONE);
        assertNotNull(dropCobble);
        assertEquals(Material.GRAVEL, dropCobble.getType());

        ItemStack dropGravel = HammerEngine.getPulverizedDrop(Material.GRAVEL);
        assertNotNull(dropGravel);
        assertEquals(Material.SAND, dropGravel.getType());

        ItemStack dropNetherrack = HammerEngine.getPulverizedDrop(Material.NETHERRACK);
        assertNotNull(dropNetherrack);
        assertEquals(Material.SOUL_SAND, dropNetherrack.getType());

        ItemStack dropIron = HammerEngine.getPulverizedDrop(Material.IRON_ORE);
        assertNotNull(dropIron);
        assertEquals("IRON_DUST", SuiteItemPdcBridge.getSlimefunId(dropIron));

        ItemStack dropGold = HammerEngine.getPulverizedDrop(Material.DEEPSLATE_GOLD_ORE);
        assertNotNull(dropGold);
        assertEquals("GOLD_DUST", SuiteItemPdcBridge.getSlimefunId(dropGold));

        ItemStack dropCopper = HammerEngine.getPulverizedDrop(Material.COPPER_ORE);
        assertNotNull(dropCopper);
        assertEquals("COPPER_DUST", SuiteItemPdcBridge.getSlimefunId(dropCopper));

        assertNull(HammerEngine.getPulverizedDrop(Material.DIAMOND_BLOCK));
    }

    @Test
    @DisplayName("Cálculo geométrico del área 3x3 del Martillo según orientación de la cara")
    void testHammer3x3GridCalculation() {
        WorldMock world = server.addSimpleWorld("mining_world");
        Block center = world.getBlockAt(100, 64, 200);

        // Golpe hacia arriba (cara UP -> plano X-Z)
        List<Block> gridUp = HammerEngine.calculate3x3Grid(center, BlockFace.UP);
        assertEquals(9, gridUp.size());
        for (Block b : gridUp) {
            assertEquals(64, b.getY(), "Todos los bloques deben estar en el mismo nivel Y=64");
            assertTrue(b.getX() >= 99 && b.getX() <= 101);
            assertTrue(b.getZ() >= 199 && b.getZ() <= 201);
        }

        // Golpe hacia el norte (cara NORTH -> plano X-Y)
        List<Block> gridNorth = HammerEngine.calculate3x3Grid(center, BlockFace.NORTH);
        assertEquals(9, gridNorth.size());
        for (Block b : gridNorth) {
            assertEquals(200, b.getZ(), "Todos los bloques deben estar en el mismo nivel Z=200");
            assertTrue(b.getX() >= 99 && b.getX() <= 101);
            assertTrue(b.getY() >= 63 && b.getY() <= 65);
        }
    }

    @Test
    @DisplayName("Catálogo de recetas para Fábrica de Concreto, Pulverizador y Compostador")
    void testMachineRecipes() {
        assertEquals(16, MachineRecipeRegistry.getConcreteRecipes().size());
        assertEquals(Material.RED_CONCRETE, MachineRecipeRegistry.getConcreteOutput(Material.RED_CONCRETE_POWDER));
        assertEquals(Material.CYAN_CONCRETE, MachineRecipeRegistry.getConcreteOutput(Material.CYAN_CONCRETE_POWDER));

        assertEquals(Material.GRAVEL, MachineRecipeRegistry.getPulverizerOutput(Material.COBBLESTONE));
        assertEquals(Material.SAND, MachineRecipeRegistry.getPulverizerOutput(Material.GRAVEL));

        assertEquals(Material.DIRT, MachineRecipeRegistry.getComposterOutput(Material.WHEAT));
        assertEquals(Material.DIRT, MachineRecipeRegistry.getComposterOutput(Material.OAK_LEAVES));
    }
}

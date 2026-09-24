package com.drakescraft.suites.utility;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.utility.chestterminal.ChestTerminalFactory;
import com.drakescraft.suites.utility.chestterminal.ChestTerminalIndex;
import com.drakescraft.suites.utility.chestterminal.ChestTerminalModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChestTerminalModuleTest {

    private static ServerMock server;
    private static DrakesUtilityPlugin plugin;
    private static ChestTerminalModule module;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
        module = (ChestTerminalModule) plugin.getModuleManager().getModule("chest_terminal");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("ChestTerminalModule está registrado y habilitado")
    void testModuleEnabled() {
        assertNotNull(module, "ChestTerminalModule debe estar registrado");
        assertTrue(module.isEnabled(), "ChestTerminalModule debe estar habilitado");
        assertNotNull(module.getIndex(), "El índice debe estar inicializado");
    }

    @Test
    @DisplayName("Items de ChestTerminal preservan PDC canónico de Slimefun")
    void testItemPdc() {
        ItemStack terminal = ChestTerminalFactory.createAccessTerminal();
        assertTrue(ChestTerminalFactory.isAccessTerminal(terminal));
        assertEquals("CHEST_TERMINAL", SuiteItemPdcBridge.getSlimefunId(terminal));

        ItemStack quartz = ChestTerminalFactory.createMilkyQuartz(4);
        assertEquals(4, quartz.getAmount());
        assertEquals("MILKY_QUARTZ", SuiteItemPdcBridge.getSlimefunId(quartz));

        ItemStack importBus = ChestTerminalFactory.createImportBus();
        assertEquals("CT_IMPORT_BUS", SuiteItemPdcBridge.getSlimefunId(importBus));

        ItemStack exportBus = ChestTerminalFactory.createExportBus();
        assertEquals("CT_EXPORT_BUS", SuiteItemPdcBridge.getSlimefunId(exportBus));

        ItemStack wireless16 = ChestTerminalFactory.createWirelessTerminal(16);
        assertEquals("CT_WIRELESS_ACCESS_TERMINAL_16", SuiteItemPdcBridge.getSlimefunId(wireless16));
        assertEquals(16, ChestTerminalFactory.getWirelessRange(wireless16));

        ItemStack wireless64 = ChestTerminalFactory.createWirelessTerminal(64);
        assertEquals("CT_WIRELESS_ACCESS_TERMINAL_64", SuiteItemPdcBridge.getSlimefunId(wireless64));
        assertEquals(64, ChestTerminalFactory.getWirelessRange(wireless64));

        ItemStack wireless128 = ChestTerminalFactory.createWirelessTerminal(128);
        assertEquals("CT_WIRELESS_ACCESS_TERMINAL_128", SuiteItemPdcBridge.getSlimefunId(wireless128));
        assertEquals(128, ChestTerminalFactory.getWirelessRange(wireless128));

        ItemStack wirelessTrans = ChestTerminalFactory.createWirelessTerminal(256);
        assertEquals("CT_WIRELESS_ACCESS_TERMINAL_TRANSDIMENSIONAL", SuiteItemPdcBridge.getSlimefunId(wirelessTrans));
        assertEquals(Integer.MAX_VALUE, ChestTerminalFactory.getWirelessRange(wirelessTrans));
    }

    @Test
    @DisplayName("ChestTerminalIndex filtra por nombre y ordena por cantidad")
    void testIndexFilteringAndSorting() {
        ChestTerminalIndex index = module.getIndex();
        assertNotNull(index);

        ItemStack item1 = new ItemStack(Material.DIAMOND, 10);
        ItemStack item2 = new ItemStack(Material.IRON_INGOT, 64);
        ItemStack item3 = new ItemStack(Material.GOLD_INGOT, 32);

        List<ItemStack> raw = Arrays.asList(item1, item2, item3);

        // Filtrar solo lingotes ("ingot") ordenados por cantidad descendente
        List<ItemStack> filtered = index.cleanAndFilter(raw, "ingot", ChestTerminalIndex.SortType.AMOUNT);
        assertEquals(2, filtered.size());
        assertEquals(Material.IRON_INGOT, filtered.get(0).getType());
        assertEquals(64, filtered.get(0).getAmount());
        assertEquals(Material.GOLD_INGOT, filtered.get(1).getType());
        assertEquals(32, filtered.get(1).getAmount());
    }

    @Test
    @DisplayName("ChestTerminalIndex throttle bloquea spam clicks anti-dupe")
    void testClickThrottle() {
        ChestTerminalIndex index = new ChestTerminalIndex(100, true);
        UUID playerId = UUID.randomUUID();

        assertFalse(index.isThrottled(playerId), "El primer clic debe ser permitido");
        assertTrue(index.isThrottled(playerId), "Clic inmediato posterior debe ser bloqueado por throttle");
    }
}

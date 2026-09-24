package com.drakescraft.suites.tech;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.tech.dynatech.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DynaTechModuleTest {

    private static ServerMock server;
    private static DrakesTechPlugin plugin;
    private static DynaTechModule module;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesTechPlugin.class);
        module = (DynaTechModule) plugin.getModuleManager().getModule("dynatech");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("DynaTechModule está registrado y habilitado en DrakesTech")
    void testModuleEnabled() {
        assertNotNull(module, "DynaTechModule debe estar registrado");
        assertTrue(module.isEnabled(), "DynaTechModule debe estar habilitado");
        assertNotNull(module.getTesseractManager(), "TesseractNetworkManager debe estar inicializado");
        assertNotNull(module.getAngelGemEngine(), "AngelGemEngine debe estar inicializado");
    }

    @Test
    @DisplayName("Items canónicos de DynaTech preservan su PDC de Slimefun")
    void testCanonicalItemsRegistry() {
        Map<String, ItemStack> items = DynaTechItemsRegistry.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                "DYNATECH_ANGEL_GEM", "DYNATECH_TESSERACT", "DYNATECH_TESSERACT_BINDER",
                "DYNATECH_WIRELESS_ENERGY_BANK", "DYNATECH_GROWTH_CHAMBER_MK2",
                "DYNATECH_WIND_MILL", "DYNATECH_STARDUST_REACTOR", "DYNATECH_ANCIENT_MACHINE_CORE"
        };

        for (String id : expectedIds) {
            ItemStack item = items.get(id);
            assertNotNull(item, "El ítem " + id + " debe existir en el registro");
            assertEquals(id, SuiteItemPdcBridge.getSlimefunId(item), "El PDC de Slimefun debe coincidir con " + id);
        }
    }

    @Test
    @DisplayName("Vinculación de canales y transmisión cuántica en Teseractos")
    void testTesseractChannelBinding() {
        TesseractNetworkManager manager = module.getTesseractManager();
        ItemStack binder = module.getItem("DYNATECH_TESSERACT_BINDER");
        assertNotNull(binder);

        String channel = "Nexus-Alpha-42";
        assertTrue(manager.bindChannel(binder, channel));
        assertEquals(channel, manager.getChannel(binder));

        // Buffer energético virtual del canal
        manager.pushEnergy(channel, 1000.0);
        assertEquals(1000.0, manager.getStoredEnergy(channel));

        double drawn = manager.pullEnergy(channel, 400.0);
        assertEquals(400.0, drawn);
        assertEquals(600.0, manager.getStoredEnergy(channel));
    }

    @Test
    @DisplayName("Carga, drenaje y activación de vuelo con la Gema de los Ángeles")
    void testAngelGemChargeAndFlight() {
        AngelGemEngine engine = module.getAngelGemEngine();
        ItemStack gem = module.getItem("DYNATECH_ANGEL_GEM");
        assertNotNull(gem);

        engine.setCharge(gem, 5000.0);
        assertEquals(5000.0, engine.getCharge(gem));

        assertTrue(engine.drain(gem, 1000.0));
        assertEquals(400.0, engine.getCharge(gem) - 3600.0); // 4000.0 restante
        assertFalse(engine.drain(gem, 10000.0), "No debe permitir drenar más de lo disponible");

        PlayerMock player = server.addPlayer();
        assertTrue(engine.toggleFlight(player, gem), "Con carga positiva, el vuelo debe activarse");
        assertTrue(player.getAllowFlight());
        assertTrue(engine.isFlying(player.getUniqueId()));

        // Alternar nuevamente desactiva
        assertFalse(engine.toggleFlight(player, gem));
        assertFalse(engine.isFlying(player.getUniqueId()));
    }

    @Test
    @DisplayName("Las Cámaras de Cultivo procesan semillas y aplican multiplicadores MK1 y MK2")
    void testGrowthChamberProcessing() {
        // MK1: Multiplicador x2
        List<ItemStack> mk1Result = module.processGrowth("DYNATECH_GROWTH_CHAMBER", Material.WHEAT_SEEDS);
        assertEquals(2, mk1Result.size());
        assertEquals(Material.WHEAT, mk1Result.get(0).getType());
        assertEquals(2, mk1Result.get(0).getAmount());
        assertEquals(Material.WHEAT_SEEDS, mk1Result.get(1).getType());

        // MK2: Multiplicador x3
        List<ItemStack> mk2Result = module.processGrowth("DYNATECH_GROWTH_CHAMBER_MK2", Material.WHEAT_SEEDS);
        assertEquals(3, mk2Result.get(0).getAmount());

        // Nether Wart
        List<ItemStack> netherResult = module.processGrowth("DYNATECH_GROWTH_CHAMBER_NETHER", Material.NETHER_WART);
        assertEquals(Material.NETHER_WART, netherResult.get(0).getType());
    }
}

package com.drakescraft.suites.core;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class SuiteItemPdcBridgeTest {

    private static ServerMock server;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Lectura y escritura canonica de Slimefun Item ID en PDC")
    void testSlimefunPdcReadWrite() {
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        assertNull(SuiteItemPdcBridge.getSlimefunId(item));
        assertFalse(SuiteItemPdcBridge.isSlimefunItem(item));

        assertTrue(SuiteItemPdcBridge.setSlimefunId(item, "NETWORKS_CABLE"));
        assertEquals("NETWORKS_CABLE", SuiteItemPdcBridge.getSlimefunId(item));
        assertTrue(SuiteItemPdcBridge.isSlimefunItem(item));
    }

    @Test
    @DisplayName("Persistencia de datos personalizados enteros y strings en PDC")
    void testCustomDataPdc() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        SuiteItemPdcBridge.setCustomString(item, "networks", "frequency", "FREQ_ALPHA_9");
        SuiteItemPdcBridge.setCustomInt(item, "networks", "channel", 42);

        assertEquals("FREQ_ALPHA_9", SuiteItemPdcBridge.getCustomString(item, "networks", "frequency"));
        assertEquals(42, SuiteItemPdcBridge.getCustomInt(item, "networks", "channel"));
    }

    @Test
    @DisplayName("Validacion de integridad de items detecta dupes y cantidades ilegales")
    void testItemIntegrityValidation() {
        ItemStack legalItem = new ItemStack(Material.GOLD_INGOT, 32);
        SuiteItemPdcBridge.setSlimefunId(legalItem, "GOLD_4K");
        assertTrue(SuiteItemPdcBridge.validateItemIntegrity(legalItem, "TestPlayer", "Loc(0,64,0)"));

        // Cantidad negativa (exploit de overflow de inventario)
        ItemStack negativeItem = new ItemStack(Material.GOLD_INGOT, 1);
        negativeItem.setAmount(-5);
        assertFalse(SuiteItemPdcBridge.validateItemIntegrity(negativeItem, "MaliciousPlayer", "Loc(0,64,0)"));

        // Slimefun ID ilegal con caracteres no permitidos
        ItemStack invalidIdItem = new ItemStack(Material.DIRT);
        SuiteItemPdcBridge.setSlimefunId(invalidIdItem, "INVALID$ID#HACK");
        assertFalse(SuiteItemPdcBridge.validateItemIntegrity(invalidIdItem, "Hacker", "Loc(0,64,0)"));
    }
}

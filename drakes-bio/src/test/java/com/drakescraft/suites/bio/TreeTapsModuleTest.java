package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.treetaps.TreeTapsModule;
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

class TreeTapsModuleTest {

    private static ServerMock server;
    private static DrakesBioPlugin plugin;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("TreeTapsModule inicializa items y preserva PDC canónico de Slimefun")
    void testTreeTapItemsAndPdc() {
        TreeTapsModule module = (TreeTapsModule) plugin.getModuleManager().getModule("slimytreetaps");
        assertNotNull(module, "El módulo slimytreetaps debe estar registrado");
        assertTrue(module.isEnabled(), "El módulo slimytreetaps debe estar habilitado");

        ItemStack standardTap = module.createTreeTap(TreeTapsModule.ID_TREE_TAP, Material.WOODEN_HOE, "Tree Tap", 25);
        assertNotNull(standardTap);
        assertEquals(TreeTapsModule.ID_TREE_TAP, SuiteItemPdcBridge.getSlimefunId(standardTap));
        assertTrue(SuiteItemPdcBridge.hasSlimefunId(standardTap, TreeTapsModule.ID_TREE_TAP));

        ItemStack resin = module.createStickyResin();
        assertNotNull(resin);
        assertEquals(TreeTapsModule.ID_STICKY_RESIN, SuiteItemPdcBridge.getSlimefunId(resin));

        ItemStack amber = module.createAmber();
        assertNotNull(amber);
        assertEquals(TreeTapsModule.ID_AMBER, SuiteItemPdcBridge.getSlimefunId(amber));
    }
}

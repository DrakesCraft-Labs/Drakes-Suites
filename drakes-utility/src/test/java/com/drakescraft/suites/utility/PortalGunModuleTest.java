package com.drakescraft.suites.utility;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.utility.portalgun.PortalGunItems;
import com.drakescraft.suites.utility.portalgun.PortalGunModule;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class PortalGunModuleTest {

    private ServerMock server;
    private DrakesUtilityPlugin plugin;
    private PortalGunModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
        module = (PortalGunModule) plugin.getModuleManager().getModule("portalgun");
        assertNotNull(module, "PortalGunModule must be registered in ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("PortalGun module must initialize and be enabled")
    void testModuleEnabled() {
        assertEquals("portalgun", module.getId());
        assertTrue(module.isEnabled());
        assertNotNull(module.getPortalGunItem());
        assertNotNull(module.getGravityGunItem());
    }

    @Test
    @DisplayName("Portal Gun and Gravity Gun must be registered with canonical PDC")
    void testItemsRegistered() {
        ItemStack pg = PortalGunItems.getItem("PORTAL_GUN");
        assertNotNull(pg);
        assertEquals("PORTAL_GUN", SuiteItemPdcBridge.getSlimefunId(pg));

        ItemStack gg = PortalGunItems.getItem("GRAVITY_GUN");
        assertNotNull(gg);
        assertEquals("GRAVITY_GUN", SuiteItemPdcBridge.getSlimefunId(gg));
    }
}

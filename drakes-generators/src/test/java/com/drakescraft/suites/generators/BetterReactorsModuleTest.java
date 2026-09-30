package com.drakescraft.suites.generators;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.generators.betterreactors.BetterReactorsItems;
import com.drakescraft.suites.generators.betterreactors.BetterReactorsModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BetterReactorsModuleTest {

    private ServerMock server;
    private DrakesGeneratorsPlugin plugin;
    private BetterReactorsModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesGeneratorsPlugin.class);
        module = (BetterReactorsModule) plugin.getModuleManager().getModule("better_reactors");
        assertNotNull(module, "BetterReactorsModule must be registered in ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("BetterReactors module must initialize and be enabled")
    void testModuleEnabled() {
        assertEquals("better_reactors", module.getId());
        assertTrue(module.isEnabled());
        assertTrue(module.isFuelRodLock());
        assertTrue(module.isCryoCoolantLoops());
        assertTrue(module.isUraniumEnrichment());
        assertTrue(module.isMeltdownContainment());
    }

    @Test
    @DisplayName("BetterReactors canonical items must be registered with correct PDC IDs")
    void testItemsRegistry() {
        Map<String, ItemStack> items = BetterReactorsItems.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                BetterReactorsItems.REACTOR_CORE,
                BetterReactorsItems.HEAT_SENSOR,
                BetterReactorsItems.REACTOR_STOP,
                BetterReactorsItems.SUPER_FREEZER,
                BetterReactorsItems.HEATED_COOLANT,
                BetterReactorsItems.LEAD_BLOCK,
                BetterReactorsItems.LEAD_GLASS,
                BetterReactorsItems.BORIUM_ROD,
                BetterReactorsItems.BORIUM,
                BetterReactorsItems.GRAPHITE,
                BetterReactorsItems.REACTOR_INPUT,
                BetterReactorsItems.REACTOR_OUTPUT,
                BetterReactorsItems.REACTOR_HATCH
        };

        for (String id : expectedIds) {
            ItemStack item = BetterReactorsItems.getItem(id);
            assertNotNull(item, "Item must exist: " + id);
            assertTrue(SuiteItemPdcBridge.hasSlimefunId(item, id), "PDC must match: " + id);
        }
    }
}

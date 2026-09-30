package com.drakescraft.suites.utility;

import com.drakescraft.suites.utility.notenoughaddons.NEAItems;
import com.drakescraft.suites.utility.notenoughaddons.NotEnoughAddonsModule;
import com.drakescraft.suites.utility.notenoughaddons.TerrariaShortsword;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class NotEnoughAddonsModuleTest {

    private ServerMock server;
    private DrakesUtilityPlugin plugin;
    private NotEnoughAddonsModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
        module = (NotEnoughAddonsModule) plugin.getModuleManager().getModule("notenoughaddons");
        assertNotNull(module, "NotEnoughAddonsModule must be registered in ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("NotEnoughAddons module must initialize and be enabled")
    void testModuleEnabled() {
        assertEquals("notenoughaddons", module.getId());
        assertTrue(module.isEnabled());
        assertNotNull(module.getAngelBlock());
        assertNotNull(module.getFlyingBubble());
    }

    @Test
    @DisplayName("Canonical items from NotEnoughAddons must be registered with proper PDC")
    void testItemsRegistered() {
        assertNotNull(NEAItems.getItem("BUDGET_DUST_FABRICATOR"));
        assertNotNull(NEAItems.getItem("FLYING_BUBBLE"));
        assertNotNull(NEAItems.getItem("ANGEL_BLOCK"));
        assertNotNull(NEAItems.getItem("MINER_BACKPACK"));
        assertNotNull(NEAItems.getItem("SHORTSWORD_COPPER"));
        assertNotNull(NEAItems.getItem("SHORTSWORD_PLATINUM"));

        TerrariaShortsword copper = TerrariaShortsword.getById("SHORTSWORD_COPPER");
        assertNotNull(copper);
        assertEquals(5.0, copper.getDamage());
        assertEquals(4.0, copper.getKnockback());
    }
}

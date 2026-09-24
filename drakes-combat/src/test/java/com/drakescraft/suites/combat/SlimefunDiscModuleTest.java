package com.drakescraft.suites.combat;

import com.drakescraft.suites.combat.slimefundisc.DiscRegistry;
import com.drakescraft.suites.combat.slimefundisc.DiscTrack;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import static org.junit.jupiter.api.Assertions.*;

class SlimefunDiscModuleTest {

    @BeforeAll
    static void setUp() {
        MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Debe registrar y construir discos musicales con metadatos y PDC válidos")
    void testCustomDiscItemCreation() {
        DiscRegistry registry = new DiscRegistry();
        DiscTrack bohemian = new DiscTrack(
                "SF_DISC_BOHEMIAN", "Bohemian Rhapsody", "Queen",
                Material.MUSIC_DISC_WAIT, 355
        );
        registry.registerTrack(bohemian);

        ItemStack disc = registry.createDiscItem(bohemian);
        assertNotNull(disc);
        assertEquals(Material.MUSIC_DISC_WAIT, disc.getType());

        assertTrue(SuiteItemPdcBridge.isSlimefunItem(disc));
        assertTrue(SuiteItemPdcBridge.hasSlimefunId(disc, "SF_DISC_BOHEMIAN"));
        assertEquals("SF_DISC_BOHEMIAN", SuiteItemPdcBridge.getSlimefunId(disc));
    }
}

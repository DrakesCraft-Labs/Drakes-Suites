package com.drakescraft.suites.combat;

import com.drakescraft.suites.combat.obsidian.ObsidianExpansionModule;
import com.drakescraft.suites.combat.obsidian.ObsidianItems;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObsidianExpansionModuleTest {

    private ServerMock server;
    private DrakesCombatPlugin plugin;
    private ObsidianExpansionModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesCombatPlugin.class);
        module = (ObsidianExpansionModule) plugin.getModuleManager().getModule("obsidian_expansion");
        assertNotNull(module, "ObsidianExpansionModule must be registered in ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("ObsidianExpansion module must initialize and be enabled")
    void testModuleEnabled() {
        assertEquals("obsidian_expansion", module.getId());
        assertTrue(module.isEnabled());
        assertTrue(module.isDamageReductionMemoization());
        assertTrue(module.isFullSuitBonusCheck());
        assertTrue(module.isBlastAbsorptionPlate());
        assertEquals(0.25, module.getTrueDamageResistance(), 0.001);
    }

    @Test
    @DisplayName("Obsidian items and armor must be registered with valid PDC tags")
    void testItemsRegistry() {
        Map<String, ItemStack> items = ObsidianItems.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                ObsidianItems.OBSIDIAN_ALLOY,
                ObsidianItems.OBSIDIAN_HELMET,
                ObsidianItems.OBSIDIAN_CHESTPLATE,
                ObsidianItems.OBSIDIAN_LEGGINGS,
                ObsidianItems.OBSIDIAN_BOOTS,
                ObsidianItems.OBSIDIAN_FORGE,
                ObsidianItems.CONTAINMENT_PICK,
                ObsidianItems.NETHERITE_GEN,
                ObsidianItems.OBSIDIAN_REACTOR,
                ObsidianItems.OBSIDIAN_PLATE,
                ObsidianItems.VOID_CORE,
                ObsidianItems.ADVANCED_VOID_CORE,
                ObsidianItems.OBSIDIAN_GEAR,
                ObsidianItems.DRAGON_SCALE,
                ObsidianItems.PHANTOM_SCALE,
                ObsidianItems.ANGEL_GEM,
                ObsidianItems.SINGLE_COMPRESSED,
                ObsidianItems.DOUBLE_COMPRESSED,
                ObsidianItems.TRIPLE_COMPRESSED,
                ObsidianItems.QUADRUPLE_COMPRESSED,
                ObsidianItems.QUINTUPLE_COMPRESSED
        };

        for (String id : expectedIds) {
            ItemStack item = ObsidianItems.getItem(id);
            assertNotNull(item, "Item must exist: " + id);
            assertTrue(SuiteItemPdcBridge.hasSlimefunId(item, id), "PDC must match: " + id);
        }

        // Verify armor enchantments
        ItemStack helm = ObsidianItems.getItem(ObsidianItems.OBSIDIAN_HELMET);
        assertNotNull(helm);
        assertEquals(6, helm.getEnchantmentLevel(Enchantment.BLAST_PROTECTION));
        assertEquals(6, helm.getEnchantmentLevel(Enchantment.FIRE_PROTECTION));
        assertEquals(4, helm.getEnchantmentLevel(Enchantment.PROTECTION));
    }
}

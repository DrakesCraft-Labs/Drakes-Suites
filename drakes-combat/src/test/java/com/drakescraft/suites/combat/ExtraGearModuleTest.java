package com.drakescraft.suites.combat;

import com.drakescraft.suites.combat.extragear.ExtraGearFactory;
import com.drakescraft.suites.combat.extragear.ExtraGearMaterial;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import static org.junit.jupiter.api.Assertions.*;

class ExtraGearModuleTest {

    @BeforeAll
    static void setUp() {
        MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Debe generar armaduras y armas con claves PDC idénticas a Slimefun upstream")
    void testSlimefunIdGeneration() {
        assertEquals("STEEL_SWORD", ExtraGearFactory.getSlimefunId(ExtraGearMaterial.STEEL, ExtraGearFactory.GearType.SWORD));
        assertEquals("STEEL_CHESTPLATE", ExtraGearFactory.getSlimefunId(ExtraGearMaterial.STEEL, ExtraGearFactory.GearType.CHESTPLATE));
        assertEquals("REINFORCED_ALLOY_HELMET", ExtraGearFactory.getSlimefunId(ExtraGearMaterial.REINFORCED_ALLOY, ExtraGearFactory.GearType.HELMET));
        assertEquals("DAMASCUS_STEEL_BOOTS", ExtraGearFactory.getSlimefunId(ExtraGearMaterial.DAMASCUS_STEEL, ExtraGearFactory.GearType.BOOTS));
        assertEquals("OBSIDIAN_LEGGINGS", ExtraGearFactory.getSlimefunId(ExtraGearMaterial.OBSIDIAN, ExtraGearFactory.GearType.LEGGINGS));
    }

    @Test
    @DisplayName("Debe crear ItemStack de espada de acero con encantamientos correctos y PDC")
    void testSteelSwordCreation() {
        ItemStack sword = ExtraGearFactory.createGear(ExtraGearMaterial.STEEL, ExtraGearFactory.GearType.SWORD);
        assertNotNull(sword);
        ItemMeta meta = sword.getItemMeta();
        assertNotNull(meta);

        // Clave PDC
        assertTrue(SuiteItemPdcBridge.isSlimefunItem(sword));
        assertTrue(SuiteItemPdcBridge.hasSlimefunId(sword, "STEEL_SWORD"));
        assertEquals("STEEL_SWORD", SuiteItemPdcBridge.getSlimefunId(sword));

        // Encantamientos: Sharpness 5, Unbreaking 6
        assertEquals(5, meta.getEnchantLevel(Enchantment.SHARPNESS));
        assertEquals(6, meta.getEnchantLevel(Enchantment.UNBREAKING));
    }

    @Test
    @DisplayName("Debe crear set completo de armadura de aleación reforzada")
    void testReinforcedAlloyArmor() {
        for (ExtraGearFactory.GearType type : ExtraGearFactory.GearType.values()) {
            ItemStack piece = ExtraGearFactory.createGear(ExtraGearMaterial.REINFORCED_ALLOY, type);
            assertNotNull(piece);
            ItemMeta meta = piece.getItemMeta();
            assertNotNull(meta);

            String expectedId = "REINFORCED_ALLOY_" + type.getSuffix();
            assertTrue(SuiteItemPdcBridge.isSlimefunItem(piece));
            assertTrue(SuiteItemPdcBridge.hasSlimefunId(piece, expectedId));
            assertEquals(expectedId, SuiteItemPdcBridge.getSlimefunId(piece));

            if (type == ExtraGearFactory.GearType.SWORD) {
                assertEquals(8, meta.getEnchantLevel(Enchantment.SHARPNESS));
            } else {
                assertEquals(5, meta.getEnchantLevel(Enchantment.PROTECTION));
                assertEquals(8, meta.getEnchantLevel(Enchantment.UNBREAKING));
            }
        }
    }
}

package com.drakescraft.suites.utility;

import com.drakescraft.suites.utility.endercarryon.CarryHelper;
import com.drakescraft.suites.utility.endercarryon.CarryKeys;
import com.drakescraft.suites.utility.endercarryon.EnderCarryOnModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import static org.junit.jupiter.api.Assertions.*;

class EnderCarryOnModuleTest {

    private ServerMock server;
    private DrakesUtilityPlugin plugin;
    private EnderCarryOnModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
        module = (EnderCarryOnModule) plugin.getModuleManager().getModule("endercarryon");
        assertNotNull(module, "EnderCarryOnModule must be registered in ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("EnderCarryOn module must initialize and be enabled")
    void testModuleEnabled() {
        assertEquals("endercarryon", module.getId());
        assertTrue(module.isEnabled());
        assertTrue(module.isAllowEntities());
        assertTrue(module.isSlowDownPlayer());
    }

    @Test
    @DisplayName("Carried block must contain PDC tag and custom model data")
    void testCarryBlockItemCreation() {
        WorldMock world = server.addSimpleWorld("test_world");
        Block chestBlock = world.getBlockAt(0, 64, 0);
        chestBlock.setType(Material.CHEST);

        ItemStack carryItem = CarryHelper.getCarryBlock(chestBlock);
        assertNotNull(carryItem);
        assertEquals(Material.CHEST, carryItem.getType());
        assertTrue(carryItem.hasItemMeta());
        assertTrue(carryItem.getItemMeta().getPersistentDataContainer().has(CarryKeys.CARRY, PersistentDataType.BYTE));
        assertTrue(EnderCarryOnModule.isCarriedItem(carryItem));
    }
}

package com.drakescraft.suites.utility;

import com.drakescraft.suites.utility.vanillapatpat.VanillaPatPatModule;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import static org.junit.jupiter.api.Assertions.*;

class VanillaPatPatModuleTest {

    private ServerMock server;
    private DrakesUtilityPlugin plugin;
    private VanillaPatPatModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
        module = (VanillaPatPatModule) plugin.getModuleManager().getModule("vanillapatpat");
        assertNotNull(module, "VanillaPatPatModule must be registered in ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("VanillaPatPat module must initialize and be enabled")
    void testModuleEnabled() {
        assertEquals("vanillapatpat", module.getId());
        assertTrue(module.isEnabled());
        assertEquals(500, module.getCooldownMs());
        assertEquals("You patted {name}!", module.getPatMessage());
    }

    @Test
    @DisplayName("Player petting entity while sneaking with empty hand triggers pat interaction")
    void testPettingEntity() {
        WorldMock world = server.addSimpleWorld("pat_world");
        PlayerMock player = server.addPlayer("Patter");
        player.setSneaking(true);

        Cow cow = world.spawn(player.getLocation(), Cow.class);
        PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, cow, EquipmentSlot.HAND);
        server.getPluginManager().callEvent(event);

        assertTrue(event.isCancelled(), "Petting interaction should cancel default mob interaction");
    }
}

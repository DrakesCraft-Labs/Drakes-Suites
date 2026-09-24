package com.drakescraft.suites.combat;

import com.drakescraft.suites.combat.mobdrops.MobDropRule;
import com.drakescraft.suites.combat.mobdrops.MobDropsModule;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MobDropsModuleTest {

    private static ServerMock server;
    private static DrakesCombatPlugin plugin;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesCombatPlugin.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("MobDropsModule inicializa reglas y genera botines con PDC correcto")
    void testMobDropRulesAndItems() {
        MobDropsModule module = (MobDropsModule) plugin.getModuleManager().getModule("sfmobdrops");
        assertNotNull(module, "El módulo sfmobdrops debe estar registrado");
        assertTrue(module.isEnabled(), "El módulo sfmobdrops debe estar activo");

        List<MobDropRule> zombieRules = module.getRulesFor(EntityType.ZOMBIE);
        assertFalse(zombieRules.isEmpty(), "Debe haber reglas de drop para Zombie");

        MobDropRule rule = zombieRules.get(0);
        ItemStack item = module.createDropItem(rule, 2);
        assertNotNull(item);
        assertEquals(2, item.getAmount());
        assertEquals("IRON_DUST", SuiteItemPdcBridge.getSlimefunId(item));
        assertTrue(SuiteItemPdcBridge.hasSlimefunId(item, "IRON_DUST"));
    }
}

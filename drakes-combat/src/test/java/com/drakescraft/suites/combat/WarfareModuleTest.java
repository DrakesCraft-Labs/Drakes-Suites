package com.drakescraft.suites.combat;

import com.drakescraft.suites.combat.warfare.WarfareModule;
import com.drakescraft.suites.combat.warfare.WarfareRegistry;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class WarfareModuleTest {

    private static ServerMock server;
    private static DrakesCombatPlugin plugin;
    private static WarfareModule module;
    private static WarfareRegistry registry;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesCombatPlugin.class);
        module = (WarfareModule) plugin.getModuleManager().getModule("warfare");
        registry = module.getRegistry();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("WarfareModule está registrado y habilitado en DrakesCombat")
    void testModuleEnabled() {
        assertNotNull(module, "WarfareModule debe estar registrado");
        assertTrue(module.isEnabled(), "WarfareModule debe estar habilitado");
        assertTrue(module.isAtomicAmmoConsumption(), "Consumo atómico de munición debe estar activo");
    }

    @Test
    @DisplayName("Registry contiene los identificadores PDC canónicos de armas balísticas y energía")
    void testWeaponsPdc() {
        assertTrue(registry.isWarfareWeapon("ENERGY_BLADE"));
        assertTrue(registry.isWarfareWeapon("ENERGY_RIFLE"));
        assertTrue(registry.isWarfareWeapon("PISTOL"));
        assertTrue(registry.isWarfareWeapon("SHOTGUN"));
        assertTrue(registry.isWarfareWeapon("SNIPER_RIFLE"));
        assertTrue(registry.isWarfareWeapon("GRENADE"));
        assertTrue(registry.isWarfareWeapon("NUCLEAR_BOMB"));
        assertTrue(registry.isWarfareWeapon("BULLET"));
    }

    @Test
    @DisplayName("Registry contiene piezas de exoesqueletos PowerSuit y máquinas balísticas")
    void testPowerSuitsAndMachines() {
        assertTrue(registry.isPowerSuitPiece("POWER_SUIT_HELMET"));
        assertTrue(registry.isPowerSuitPiece("POWER_SUIT_CHESTPLATE"));
        assertTrue(registry.isPowerSuitPiece("MODULE_MANIPULATOR"));

        assertTrue(registry.isWarfareMachine("ELEMENTAL_REACTOR"));
        assertTrue(registry.isWarfareMachine("BOOMINATOR_9000"));
        assertTrue(registry.isWarfareMachine("EXPLOSIVE_SYNTHESIZER"));
    }

    @Test
    @DisplayName("createEnergyBlade genera espada con PDC canónico ENERGY_BLADE")
    void testEnergyBladeCreation() {
        ItemStack blade = registry.createEnergyBlade();
        assertNotNull(blade);
        assertEquals(Material.NETHERITE_SWORD, blade.getType());
        assertEquals("ENERGY_BLADE", SuiteItemPdcBridge.getSlimefunId(blade));
        assertEquals(12.0, registry.getWeaponDamage("ENERGY_BLADE"));
    }

    @Test
    @DisplayName("createBullet genera proyectiles con PDC canónico BULLET")
    void testBulletCreation() {
        ItemStack bullets = registry.createBullet(32);
        assertNotNull(bullets);
        assertEquals(32, bullets.getAmount());
        assertEquals(Material.IRON_NUGGET, bullets.getType());
        assertEquals("BULLET", SuiteItemPdcBridge.getSlimefunId(bullets));
    }
}

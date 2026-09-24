package com.drakescraft.suites.magic;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.magic.souljars.SoulJarRegistry;
import com.drakescraft.suites.magic.souljars.SoulJarsModule;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SoulJarsModuleTest {

    private ServerMock server;
    private DrakesMagicPlugin plugin;
    private SoulJarsModule module;
    private SoulJarRegistry registry;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesMagicPlugin.class);
        module = (SoulJarsModule) plugin.getModuleManager().getModule("soul_jars");
        assertNotNull(module, "SoulJarsModule debe estar registrado");
        registry = module.getRegistry();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testEmptyJarCreationAndPdc() {
        ItemStack emptyJar = registry.createEmptyJar(1);
        assertNotNull(emptyJar);
        assertEquals(Material.GLASS_BOTTLE, emptyJar.getType());
        assertTrue(registry.isEmptyJar(emptyJar));
        assertEquals("SOUL_JAR", SuiteItemPdcBridge.getSlimefunId(emptyJar));
    }

    @Test
    void testSoulJarProgressAndPdc() {
        ItemStack soulJar = registry.createSoulJar(EntityType.ZOMBIE, 42, 128);
        assertNotNull(soulJar);
        assertEquals(Material.GLASS_BOTTLE, soulJar.getType());
        assertTrue(registry.isSoulJar(soulJar));
        assertFalse(registry.isFilledJar(soulJar));
        assertEquals("ZOMBIE_SOUL_JAR", SuiteItemPdcBridge.getSlimefunId(soulJar));
        assertEquals(EntityType.ZOMBIE, registry.getJarEntityType(soulJar));
        assertEquals(42, registry.getStoredSouls(soulJar));
    }

    @Test
    void testFilledJarCreationAndPdc() {
        ItemStack filledJar = registry.createFilledJar(EntityType.CREEPER, 128);
        assertNotNull(filledJar);
        assertEquals(Material.EXPERIENCE_BOTTLE, filledJar.getType());
        assertTrue(registry.isFilledJar(filledJar));
        assertFalse(registry.isSoulJar(filledJar));
        assertEquals("FILLED_CREEPER_SOUL_JAR", SuiteItemPdcBridge.getSlimefunId(filledJar));
        assertEquals(EntityType.CREEPER, registry.getJarEntityType(filledJar));
        assertEquals(128, registry.getStoredSouls(filledJar));
    }

    @Test
    void testBrokenSpawnerPdc() {
        ItemStack spawner = registry.createBrokenSpawner(EntityType.SKELETON);
        assertNotNull(spawner);
        assertEquals(Material.SPAWNER, spawner.getType());
        assertEquals("SKELETON_BROKEN_SPAWNER", SuiteItemPdcBridge.getSlimefunId(spawner));
    }

    @Test
    void testEntityDeathCapturesSoulIntoEmptyJar() {
        PlayerMock player = server.addPlayer();
        ItemStack emptyJar = registry.createEmptyJar(1);
        player.getInventory().addItem(emptyJar);

        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_ATTACK)
                .withCausingEntity(player)
                .withDirectEntity(player)
                .build();
        EntityDeathEvent deathEvent = new EntityDeathEvent(zombie, damageSource, new ArrayList<>());

        server.getPluginManager().callEvent(deathEvent);

        // Verificar que el frasco vacío fue transformado en ZOMBIE_SOUL_JAR con 1 alma
        boolean foundProgressJar = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && registry.isSoulJar(item) && registry.getJarEntityType(item) == EntityType.ZOMBIE) {
                assertEquals(1, registry.getStoredSouls(item));
                foundProgressJar = true;
                break;
            }
        }
        assertTrue(foundProgressJar, "El jugador debe tener un ZOMBIE_SOUL_JAR con 1 alma");
    }

    @Test
    void testProgressJarSaturationToFilledJar() {
        PlayerMock player = server.addPlayer();
        int required = registry.getRequiredSouls(EntityType.ZOMBIE);
        // Frasco al borde de la saturación (127/128)
        ItemStack almostFull = registry.createSoulJar(EntityType.ZOMBIE, required - 1, required);
        player.getInventory().addItem(almostFull);

        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_ATTACK)
                .withCausingEntity(player)
                .withDirectEntity(player)
                .build();
        EntityDeathEvent deathEvent = new EntityDeathEvent(zombie, damageSource, new ArrayList<>());

        server.getPluginManager().callEvent(deathEvent);

        // Verificar que el frasco se convirtió en FILLED_ZOMBIE_SOUL_JAR
        boolean foundFilledJar = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && registry.isFilledJar(item) && registry.getJarEntityType(item) == EntityType.ZOMBIE) {
                assertEquals(required, registry.getStoredSouls(item));
                foundFilledJar = true;
                break;
            }
        }
        assertTrue(foundFilledJar, "El frasco debe haberse saturado a FILLED_ZOMBIE_SOUL_JAR");
    }
}

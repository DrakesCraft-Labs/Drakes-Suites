package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.mobcapturer.MobCapturerModule;
import com.drakescraft.suites.bio.mobcapturer.MobCapturerRegistry;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

class MobCapturerModuleTest {

    private static ServerMock server;
    private static DrakesBioPlugin plugin;
    private static MobCapturerModule module;
    private static MobCapturerRegistry registry;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
        module = (MobCapturerModule) plugin.getModuleManager().getModule("mob_capturer");
        registry = module.getRegistry();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("MobCapturerModule está registrado y habilitado en DrakesBio")
    void testModuleEnabled() {
        assertNotNull(module, "MobCapturerModule debe estar registrado");
        assertTrue(module.isEnabled(), "MobCapturerModule debe estar habilitado");
    }

    @Test
    @DisplayName("createMobCannon genera item con PDC canónico MOB_CANNON")
    void testCannonPdc() {
        ItemStack cannon = registry.createMobCannon();
        assertNotNull(cannon);
        assertEquals(Material.CROSSBOW, cannon.getType());
        assertTrue(registry.isMobCannon(cannon));
        assertEquals("MOB_CANNON", SuiteItemPdcBridge.getSlimefunId(cannon));
    }

    @Test
    @DisplayName("createMobPellet genera perdigones con PDC MOB_CAPTURING_PELLET")
    void testPelletPdc() {
        ItemStack pellet = registry.createMobPellet(16);
        assertNotNull(pellet);
        assertEquals(16, pellet.getAmount());
        assertEquals(Material.SNOWBALL, pellet.getType());
        assertTrue(registry.isMobPellet(pellet));
        assertEquals("MOB_CAPTURING_PELLET", SuiteItemPdcBridge.getSlimefunId(pellet));
    }

    @Test
    @DisplayName("createMobEgg genera huevo con PDC <MOB>_MOB_EGG y tipo de entidad exacto")
    void testEggPdc() {
        ItemStack egg = registry.createMobEgg(EntityType.COW);
        assertNotNull(egg);
        assertTrue(registry.isMobEgg(egg));
        assertEquals("COW_MOB_EGG", SuiteItemPdcBridge.getSlimefunId(egg));
        assertEquals(EntityType.COW, registry.getEggEntityType(egg));
    }

    @Test
    @DisplayName("canCapture permite capturar animales pero bloquea bosses mayores")
    void testBossCapturePrevention() {
        PlayerMock player = server.addPlayer();
        Cow cow = (Cow) player.getWorld().spawnEntity(player.getLocation(), EntityType.COW);
        assertTrue(registry.canCapture(cow), "Una vaca común debe ser capturable");

        Wither wither = (Wither) player.getWorld().spawnEntity(player.getLocation(), EntityType.WITHER);
        assertFalse(registry.canCapture(wither), "Wither debe estar estrictamente bloqueado");
    }

    @Test
    @DisplayName("MobCannon consume munición al disparar")
    void testCannonShootConsumesPellet() {
        PlayerMock player = server.addPlayer();
        ItemStack cannon = registry.createMobCannon();
        ItemStack pellets = registry.createMobPellet(5);

        player.getInventory().setItemInMainHand(cannon);
        player.getInventory().addItem(pellets);

        PlayerInteractEvent interactEvent = new PlayerInteractEvent(
                player,
                Action.RIGHT_CLICK_AIR,
                cannon,
                null,
                null,
                EquipmentSlot.HAND
        );
        server.getPluginManager().callEvent(interactEvent);

        // Debe haber consumido 1 pellet (quedan 4)
        int remainingPellets = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && registry.isMobPellet(item)) {
                remainingPellets += item.getAmount();
            }
        }
        assertEquals(4, remainingPellets, "Debe quedar 4 perdigones tras el disparo");
    }
}

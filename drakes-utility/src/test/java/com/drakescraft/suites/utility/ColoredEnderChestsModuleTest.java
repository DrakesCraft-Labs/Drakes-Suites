package com.drakescraft.suites.utility;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.utility.enderchests.ColoredEnderChestsModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

class ColoredEnderChestsModuleTest {

    private static ServerMock server;
    private static DrakesUtilityPlugin plugin;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesUtilityPlugin.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("ColoredEnderChestsModule genera ítems con PDC canónico y canales cromáticos")
    void testEnderChestPdcAndCreation() {
        ColoredEnderChestsModule module = (ColoredEnderChestsModule) plugin.getModuleManager().getModule("colored_enderchests");
        assertNotNull(module, "El módulo colored_enderchests debe estar registrado");
        assertTrue(module.isEnabled(), "El módulo colored_enderchests debe estar activo");

        // Pequeño 0-0-0
        ItemStack smallChest = module.createEnderChestItemStack(false, 0, 0, 0);
        assertNotNull(smallChest);
        assertEquals(Material.ENDER_CHEST, smallChest.getType());
        assertEquals("COLORED_ENDER_CHEST_SMALL_0_0_0", SuiteItemPdcBridge.getSlimefunId(smallChest));
        assertEquals(Integer.valueOf(0), SuiteItemPdcBridge.getCustomInt(smallChest, "drakesutility", "enderchest_big"));
        assertTrue(module.isColoredEnderChest(smallChest));

        // Grande 14-2-9
        ItemStack bigChest = module.createEnderChestItemStack(true, 14, 2, 9);
        assertNotNull(bigChest);
        assertEquals("COLORED_ENDER_CHEST_BIG_14_2_9", SuiteItemPdcBridge.getSlimefunId(bigChest));
        assertEquals(Integer.valueOf(1), SuiteItemPdcBridge.getCustomInt(bigChest, "drakesutility", "enderchest_big"));
        assertTrue(module.isColoredEnderChest(bigChest));
    }

    @Test
    @DisplayName("ColoredEnderChestsModule abre y sincroniza contenidos por frecuencia")
    void testEnderChestStorageAndSync() {
        ColoredEnderChestsModule module = (ColoredEnderChestsModule) plugin.getModuleManager().getModule("colored_enderchests");
        assertNotNull(module);

        PlayerMock player = server.addPlayer();
        ItemStack chestItem = module.createEnderChestItemStack(false, 5, 5, 5);
        player.getInventory().setItemInMainHand(chestItem);

        // Abrir cofre
        module.openEnderChest(player, false, 5, 5, 5);
        assertEquals(27, player.getOpenInventory().getTopInventory().getSize());

        // Colocar un ítem dentro
        ItemStack diamond = new ItemStack(Material.DIAMOND, 16);
        player.getOpenInventory().getTopInventory().setItem(0, diamond);

        // Cerrar cofre
        player.closeInventory();

        // Verificar que la frecuencia almacenó los ítems
        String key = module.getFrequencyKey(false, 5, 5, 5);
        ItemStack[] stored = module.getFrequencyStorage().get(key);
        assertNotNull(stored);
        assertNotNull(stored[0]);
        assertEquals(Material.DIAMOND, stored[0].getType());
        assertEquals(16, stored[0].getAmount());
    }

    @Test
    @DisplayName("ColoredEnderChestsModule soporta frecuencias privadas con diamante y aisla inventarios")
    void testPrivateDiamondLockAndAccessIsolation() {
        ColoredEnderChestsModule module = (ColoredEnderChestsModule) plugin.getModuleManager().getModule("colored_enderchests");
        assertNotNull(module);

        PlayerMock playerA = server.addPlayer("PlayerA");
        PlayerMock playerB = server.addPlayer("PlayerB");

        // Player A abre cofre privado 7-7-7
        module.openEnderChest(playerA, false, 7, 7, 7, playerA.getUniqueId());
        ItemStack secretLoot = new ItemStack(Material.NETHERITE_INGOT, 8);
        playerA.getOpenInventory().getTopInventory().setItem(0, secretLoot);
        playerA.closeInventory();

        // Player B abre la frecuencia 7-7-7 en modo público
        module.openEnderChest(playerB, false, 7, 7, 7, null);
        assertNull(playerB.getOpenInventory().getTopInventory().getItem(0), "Player B en canal público no debe ver el botín privado de Player A");
        playerB.closeInventory();

        // Player B intenta abrir directamente la frecuencia privada de Player A sin permisos
        module.openEnderChest(playerB, false, 7, 7, 7, playerA.getUniqueId());
        // El cofre no debe abrirse para Player B
        assertTrue(playerB.getOpenInventory().getTopInventory() == null || playerB.getOpenInventory().getTopInventory().getType() == org.bukkit.event.inventory.InventoryType.CRAFTING,
                "Player B no debe poder abrir el cofre privado de Player A");

        // Verificar que Player A recupera sus ítems privados
        module.openEnderChest(playerA, false, 7, 7, 7, playerA.getUniqueId());
        ItemStack retrieved = playerA.getOpenInventory().getTopInventory().getItem(0);
        assertNotNull(retrieved);
        assertEquals(Material.NETHERITE_INGOT, retrieved.getType());
        assertEquals(8, retrieved.getAmount());
        playerA.closeInventory();
    }
}

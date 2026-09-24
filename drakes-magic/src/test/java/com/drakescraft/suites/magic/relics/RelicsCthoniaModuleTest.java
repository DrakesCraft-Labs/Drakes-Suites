package com.drakescraft.suites.magic.relics;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.magic.DrakesMagicPlugin;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelicsCthoniaModuleTest {

    private ServerMock server;
    private DrakesMagicPlugin plugin;
    private RelicsCthoniaModule module;
    private RelicsRegistry registry;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesMagicPlugin.class);
        module = (RelicsCthoniaModule) plugin.getModuleManager().getModule("relics_cthonia");
        assertNotNull(module, "RelicsCthoniaModule debe estar registrado");
        registry = module.getRegistry();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testModuleRegistration() {
        assertTrue(module.isEnabled(), "RelicsCthoniaModule debe estar activo");
        assertFalse(registry.getAllRelics().isEmpty(), "Debe haber reliquias registradas");
    }

    @Test
    void testRelicItemCreationAndPdc() {
        ItemStack item = registry.createRelic("CTHONIAN_PEARL", 1);
        assertNotNull(item);
        assertEquals(Material.ENDER_PEARL, item.getType());
        assertTrue(registry.isRelic(item));

        RelicDefinition def = registry.getRelic(item);
        assertNotNull(def);
        assertEquals("CTHONIAN_PEARL", def.id());
        assertEquals(RelicRarity.COMMON, def.rarity());
        assertEquals("CTHONIAN_RELIC_CTHONIAN_PEARL", SuiteItemPdcBridge.getSlimefunId(item));
    }

    @Test
    void testRelicVoiderCreation() {
        ItemStack voider = registry.createRelicVoider(1);
        assertNotNull(voider);
        assertEquals(Material.DISPENSER, voider.getType());
        assertEquals("RELIC_VOIDER", SuiteItemPdcBridge.getSlimefunId(voider));
    }

    @Test
    void testRelicInteractAndConsumption() {
        PlayerMock player = server.addPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        ItemStack relicItem = registry.createRelic("AGED_WINE", 2);
        player.getInventory().setItemInMainHand(relicItem);

        int xpBefore = player.getTotalExperience();

        PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, relicItem, null, null, EquipmentSlot.HAND);
        server.getPluginManager().callEvent(event);

        assertTrue(event.isCancelled(), "La interacción debe cancelarse para no consumirse como comida");
        assertEquals(1, player.getInventory().getItemInMainHand().getAmount(), "Debe haberse consumido 1 reliquia");
        assertTrue(player.getTotalExperience() > xpBefore, "El jugador debe haber ganado experiencia");
    }

    @Test
    void testMiningDrop() {
        PlayerMock player = server.addPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Forzar 100% de drop rate para test unitario
        module.setMiningDropChance(1.0);

        Block block = player.getWorld().getBlockAt(0, 64, 0);
        block.setType(Material.DEEPSLATE);

        BlockBreakEvent event = new BlockBreakEvent(block, player);
        server.getPluginManager().callEvent(event);

        // Verificar que una reliquia fue spawneada en el mundo
        boolean dropped = player.getWorld().getEntitiesByClass(Item.class).stream()
                .anyMatch(i -> registry.isRelic(i.getItemStack()));
        assertTrue(dropped, "Debe soltarse una reliquia al minar deepslate");
    }

    @Test
    void testMobDrop() {
        PlayerMock player = server.addPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Forzar 100% de drop rate para test unitario
        module.setMobDropChance(1.0);

        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        zombie.setKiller(player);
        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_ATTACK)
                .withCausingEntity(player)
                .withDirectEntity(player)
                .build();

        List<ItemStack> drops = new ArrayList<>();
        EntityDeathEvent event = new EntityDeathEvent(zombie, damageSource, drops);
        server.getPluginManager().callEvent(event);

        boolean dropped = drops.stream().anyMatch(registry::isRelic);
        assertTrue(dropped, "Debe añadirse una reliquia al matar un zombie");
    }

    @Test
    void testRarityDistribution() {
        for (RelicRarity rarity : RelicRarity.values()) {
            RelicDefinition def = registry.rollRandomRelic(rarity);
            assertNotNull(def, "Debe haber al menos una reliquia para cada rareza: " + rarity);
            assertEquals(rarity, def.rarity());
        }

        RelicDefinition rolled = registry.rollRandomRelic();
        assertNotNull(rolled);
    }
}

package com.drakescraft.suites.core;

import com.drakescraft.suites.core.command.CommandModalityGate;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import static org.junit.jupiter.api.Assertions.*;

class CommandModalityGateTest {

    private static ServerMock server;
    private static DrakesCorePlugin plugin;
    private static CommandModalityGate gate;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesCorePlugin.class);
        gate = plugin.getCommandGate();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Comando /sf es bloqueado en world_clasico")
    void testCommandBlockedInClasico() {
        WorldMock clasicoWorld = server.addSimpleWorld("world_clasico");
        PlayerMock player = server.addPlayer();
        player.teleport(clasicoWorld.getSpawnLocation());

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, "/sf");
        server.getPluginManager().callEvent(event);

        assertTrue(event.isCancelled(), "Comando /sf debe ser cancelado en Survival Clásico");
    }

    @Test
    @DisplayName("Comando /sf es permitido en world (Survival Slimefun)")
    void testCommandAllowedInNormalWorld() {
        WorldMock normalWorld = server.addSimpleWorld("world");
        PlayerMock player = server.addPlayer();
        player.teleport(normalWorld.getSpawnLocation());

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, "/sf");
        server.getPluginManager().callEvent(event);

        assertFalse(event.isCancelled(), "Comando /sf debe estar permitido en world");
    }

    @Test
    @DisplayName("Comando /is es bloqueado en world_oneblock y sugiere /ob")
    void testIsBlockedInOneBlock() {
        WorldMock oneblockWorld = server.addSimpleWorld("world_oneblock");
        PlayerMock player = server.addPlayer();
        player.teleport(oneblockWorld.getSpawnLocation());

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, "/is");
        server.getPluginManager().callEvent(event);

        assertTrue(event.isCancelled(), "Comando /is debe ser cancelado en OneBlock");
    }

    @Test
    @DisplayName("Bypass con permiso drakescore.commandgate.bypass permite ejecutar comandos bloqueados")
    void testBypassPermission() {
        WorldMock clasicoWorld = server.addSimpleWorld("world_clasico");
        PlayerMock admin = server.addPlayer();
        admin.teleport(clasicoWorld.getSpawnLocation());
        admin.addAttachment(plugin, "drakescore.commandgate.bypass", true);

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(admin, "/sf");
        server.getPluginManager().callEvent(event);

        assertFalse(event.isCancelled(), "Admin con bypass no debe tener comandos bloqueados");
    }
}

package com.drakescraft.suites.utility;

import com.drakescraft.suites.utility.slimehud.HudMachineInfo;
import com.drakescraft.suites.utility.slimehud.SlimeHUDModule;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

class SlimeHUDModuleTest {

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
    @DisplayName("SlimeHUDModule carga configuración y parámetros correctamente")
    void testSlimeHUDModuleLifecycle() {
        SlimeHUDModule module = (SlimeHUDModule) plugin.getModuleManager().getModule("slimehud");
        assertNotNull(module, "El módulo slimehud debe estar registrado");
        assertTrue(module.isEnabled(), "El módulo slimehud debe estar activo");

        assertTrue(module.getRaytraceRateTicks() > 0, "El raytrace rate debe ser mayor a 0");
        assertTrue(module.isClientDisplayOnly(), "Debe ser client display only");
        assertTrue(module.isShowMachineEnergy(), "Debe mostrar energía de máquinas");
    }

    @Test
    @DisplayName("HudMachineInfo formatea Componentes y texto plano sin errores")
    void testHudMachineInfoRendering() {
        HudMachineInfo info = new HudMachineInfo(
                "NUCLEAR_REACTOR",
                "Reactor Nuclear Primordial",
                25000,
                50000,
                0.50,
                "Enfriamiento Óptimo"
        );

        assertNotNull(info.toPlainText());
        assertTrue(info.toPlainText().contains("Reactor Nuclear Primordial"));
        assertTrue(info.toPlainText().contains("25000/50000 J"));

        Component comp = info.toComponent();
        assertNotNull(comp, "Component no debe ser nulo");
    }

    @Test
    @DisplayName("SlimeHUDModule gestiona preferencias de jugador y alternancia")
    void testPlayerToggleAndActionBar() {
        SlimeHUDModule module = (SlimeHUDModule) plugin.getModuleManager().getModule("slimehud");
        assertNotNull(module);

        PlayerMock player = server.addPlayer();
        assertTrue(module.isPlayerEnabled(player.getUniqueId()), "Por defecto el jugador tiene HUD activo");

        // Desactivar HUD
        boolean enabledAfterToggle = module.togglePlayer(player.getUniqueId());
        assertFalse(enabledAfterToggle);
        assertFalse(module.isPlayerEnabled(player.getUniqueId()));

        // Intentar enviar HUD mientras está desactivado (no debe fallar)
        HudMachineInfo info = new HudMachineInfo("ENHANCED_FURNACE", "Horno Mejorado", 50, 100, 0.25, "Fundiendo");
        module.sendMachineHud(player, info);

        // Reactivar HUD
        enabledAfterToggle = module.togglePlayer(player.getUniqueId());
        assertTrue(enabledAfterToggle);
        assertTrue(module.isPlayerEnabled(player.getUniqueId()));

        // Enviar HUD activo
        module.sendMachineHud(player, info);
        module.clearPlayerHud(player);
    }
}

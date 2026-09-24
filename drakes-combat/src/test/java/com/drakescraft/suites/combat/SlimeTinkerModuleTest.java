package com.drakescraft.suites.combat;

import com.drakescraft.suites.combat.tinker.SlimeTinkerModule;
import com.drakescraft.suites.combat.tinker.SlimefunArmorAdaptation;
import com.drakescraft.suites.combat.tinker.TinkerModifier;
import com.drakescraft.suites.combat.tinker.TinkerToolAssemblyEngine;
import com.drakescraft.suites.combat.tinker.TinkerTrait;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlimeTinkerModuleTest {

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
    @DisplayName("SlimeTinkerModule se registra y habilita correctamente en DrakesCombat")
    void testModuleLifecycle() {
        SlimeTinkerModule module = (SlimeTinkerModule) plugin.getModuleManager().getModule("slimetinker");
        assertNotNull(module, "SlimeTinkerModule debe estar registrado");
        assertTrue(module.isEnabled(), "SlimeTinkerModule debe estar habilitado");
        assertNotNull(module.getAssemblyEngine(), "AssemblyEngine no debe ser nulo");
        assertNotNull(module.getArmorAdaptation(), "ArmorAdaptation no debe ser nulo");
    }

    @Test
    @DisplayName("TinkerToolAssemblyEngine valida ensamblaje y límite de modificadores")
    void testAssemblyAndModifiers() {
        TinkerToolAssemblyEngine engine = new TinkerToolAssemblyEngine(5, true);

        // Ensamblaje válido
        assertTrue(engine.canAssembleTool("COBALT", "WOOD", "STRING"));
        assertTrue(engine.canAssembleTool("INFINITY", "SLIME", "CHAIN"));

        // Ensamblaje inválido
        assertFalse(engine.canAssembleTool(null, "WOOD", "STRING"));
        assertFalse(engine.canAssembleTool("", "WOOD", "STRING"));

        // Límite de modificadores
        List<TinkerModifier> modifiers = new ArrayList<>();
        modifiers = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.LIFESTEAL, 1, 0.85));
        modifiers = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.LIGHTNING_STRIKE, 1, 0.85));
        modifiers = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.VOID_EDGE, 1, 0.85));
        modifiers = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.DIVINE_SLAYER, 1, 0.85));
        modifiers = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.MOMENTUM, 1, 0.85));
        assertEquals(5, modifiers.size());

        // 6to modificador debe ser rechazado por límite de slots (max 5)
        List<TinkerModifier> overflow = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.COSMIC_DECAY, 1, 0.85));
        assertNull(overflow, "No debe permitir más modificadores que el límite configurado");

        // Stacking de trait existente debe actualizar nivel sin consumir slot
        List<TinkerModifier> upgraded = engine.applyModifier(modifiers, new TinkerModifier(TinkerTrait.LIFESTEAL, 2, 0.85));
        assertNotNull(upgraded);
        assertEquals(5, upgraded.size(), "El tamaño no debe aumentar al mejorar un trait existente");
        assertEquals(3, upgraded.get(0).getLevel(), "El nivel debe sumarse (1 + 2 = 3)");
    }

    @Test
    @DisplayName("SlimefunArmorAdaptation aplica daño verdadero canónico contra AFK y sets Infinity (AGENTS.md)")
    void testArmorAdaptationTrueDamage() {
        SlimefunArmorAdaptation adaptation = new SlimefunArmorAdaptation(0.05, true);

        // Set de Infinity (Tier 10) ante un ataque de Dios de 100 de daño:
        // Tier 10 -> base 45% true damage (45.0)
        double damageT10 = adaptation.calculateTrueDamagePenetration(10, 0, 100.0);
        assertEquals(45.0, damageT10, 0.001);

        // Con 10 golpes consecutivos en modo AFK/estático (anti-afk penalty: 1 + 10 * 0.05 = 1.5x)
        double damageT10Afk = adaptation.calculateTrueDamagePenetration(10, 10, 100.0);
        assertEquals(67.5, damageT10Afk, 0.001);

        // Tier 3 Tinker Armor (15% true damage base)
        double damageT3 = adaptation.calculateTrueDamagePenetration(3, 0, 100.0);
        assertEquals(15.0, damageT3, 0.001);
    }
}

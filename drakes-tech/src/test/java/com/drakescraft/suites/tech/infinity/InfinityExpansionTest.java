package com.drakescraft.suites.tech.infinity;

import com.drakescraft.suites.tech.DrakesTechPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class InfinityExpansionTest {

    private static ServerMock server;
    private static DrakesTechPlugin plugin;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesTechPlugin.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("InfinityExpansionModule se registra y activa en DrakesTechPlugin")
    void testInfinityModuleLifecycle() {
        InfinityExpansionModule module = (InfinityExpansionModule) plugin.getModuleManager().getModule("infinity");
        assertNotNull(module, "InfinityExpansionModule debe estar registrado");
        assertTrue(module.isEnabled(), "InfinityExpansionModule debe estar habilitado");
        assertNotNull(module.getSingularityEngine(), "SingularityEngine no debe ser nulo");
        assertNotNull(module.getArmorEngine(), "ArmorEngine no debe ser nulo");
        assertNotNull(module.getQuarryEngine(), "QuarryEngine no debe ser nulo");
    }

    @Test
    @DisplayName("InfinitySingularityEngine valida compresión y previene dupes")
    void testSingularitiesValidation() {
        InfinitySingularityEngine engine = new InfinitySingularityEngine(true);

        assertTrue(engine.isRegistered("NETHERITE"));
        assertEquals(1000, engine.getRequiredAmount("NETHERITE"));
        assertEquals(5000000L, engine.getRequiredEnergy("NETHERITE"));

        // Validar operación legítima
        assertTrue(engine.validateCompression("NETHERITE", 1000, 5000000L));
        assertTrue(engine.validateCompression("NETHERITE", 1500, 6000000L));

        // Rechazar con falta de ítems
        assertFalse(engine.validateCompression("NETHERITE", 999, 5000000L));

        // Rechazar con falta de energía
        assertFalse(engine.validateCompression("NETHERITE", 1000, 4999999L));

        // Rechazar cantidades negativas o inexistentes
        assertFalse(engine.validateCompression("NETHERITE", -10, 5000000L));
        assertFalse(engine.validateCompression("NON_EXISTENT", 1000, 5000000L));
    }

    @Test
    @DisplayName("InfinityArmorEngine mitiga daño, absorbe vacío y respeta penetración divina (AGENTS.md)")
    void testInfinityArmorEngine() {
        // Reducción 95%, anti-void activo, bypass de daño verdadero activo
        InfinityArmorEngine engine = new InfinityArmorEngine(0.95, true, true);

        // Daño convencional de 100 -> mitigado al 5% (5.0)
        double damageConventional = engine.calculateDamage(100.0, false, false);
        assertEquals(5.0, damageConventional, 0.001);

        // Daño de vacío -> absorbido por Anti-Void (0.0)
        double damageVoid = engine.calculateDamage(100.0, true, false);
        assertEquals(0.0, damageVoid, 0.001);

        // Daño Verdadero de Dioses (DrakesBosses / Mahoraga / Zeus) -> 100% de penetración
        double damageGod = engine.calculateDamage(100.0, false, true);
        assertEquals(100.0, damageGod, 0.001);
    }

    @Test
    @DisplayName("InfinityQuarryEngine escala consumo de energía y protege chunks de saturación")
    void testInfinityQuarryEngine() {
        InfinityQuarryEngine engine = new InfinityQuarryEngine(8, 250L, true);

        // Tier 1 vs Tier 3 energía
        long energyT1 = engine.calculateEnergyRequired(1, 1.0);
        long energyT3 = engine.calculateEnergyRequired(3, 1.0);
        assertTrue(energyT3 > energyT1, "Tier 3 debe consumir más energía que Tier 1");

        // Protección de chunks (máximo 2 por chunk)
        String chunk = "world:10:20";
        assertTrue(engine.registerQuarryInChunk(chunk, 2));
        assertTrue(engine.registerQuarryInChunk(chunk, 2));
        assertFalse(engine.registerQuarryInChunk(chunk, 2), "Tercera cantera en el mismo chunk debe ser rechazada");

        // Liberar cantera
        engine.unregisterQuarryInChunk(chunk);
        assertTrue(engine.registerQuarryInChunk(chunk, 2), "Al liberar debe permitir una nueva cantera");
    }
}

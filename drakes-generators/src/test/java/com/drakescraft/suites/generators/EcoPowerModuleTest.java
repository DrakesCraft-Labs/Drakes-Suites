package com.drakescraft.suites.generators;

import com.drakescraft.suites.generators.ecopower.EcoGeneratorType;
import com.drakescraft.suites.generators.ecopower.EcoPowerEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EcoPowerModuleTest {

    @Test
    @DisplayName("Debe calcular correctamente el ciclo día y noche para energía solar/lunar")
    void testDayNightCycle() {
        // Ticks de día: 1000, 6000 (mediodía)
        assertTrue(EcoPowerEngine.isDay(1000L));
        assertTrue(EcoPowerEngine.isDay(6000L));
        assertFalse(EcoPowerEngine.isNight(6000L));

        // Ticks de noche: 14000, 18000 (medianoche)
        assertFalse(EcoPowerEngine.isDay(14000L));
        assertTrue(EcoPowerEngine.isNight(14000L));
        assertTrue(EcoPowerEngine.isNight(18000L));
    }

    @Test
    @DisplayName("Generador Solar debe producir al 100% de día despejado y 0 de noche")
    void testSolarGeneration() {
        int base = 128;
        // Mediodía despejado
        assertEquals(128, EcoPowerEngine.calculateSolarPower(base, 6000L, false));

        // Mediodía con lluvia/tormenta (reducción a 20%)
        assertEquals(25, EcoPowerEngine.calculateSolarPower(base, 6000L, true));

        // Medianoche (0 producción)
        assertEquals(0, EcoPowerEngine.calculateSolarPower(base, 18000L, false));
        assertEquals(0, EcoPowerEngine.calculateSolarPower(base, 18000L, true));
    }

    @Test
    @DisplayName("Generador Lunar debe producir solo de noche")
    void testLunarGeneration() {
        int base = 64;
        // Día (0 producción)
        assertEquals(0, EcoPowerEngine.calculateLunarPower(base, 6000L, false));

        // Noche despejada
        assertEquals(64, EcoPowerEngine.calculateLunarPower(base, 18000L, false));

        // Noche con tormenta (reducción a la mitad)
        assertEquals(32, EcoPowerEngine.calculateLunarPower(base, 18000L, true));
    }

    @Test
    @DisplayName("Turbina Eólica debe escalar con la altura Y y el viento de tormenta")
    void testWindTurbineScaling() {
        int base = 32;

        // A nivel del mar (Y=64)
        assertEquals(32, EcoPowerEngine.calculateWindPower(base, 64, false));

        // A gran altura (Y=192)
        int highAltitude = EcoPowerEngine.calculateWindPower(base, 192, false);
        assertTrue(highAltitude > 32, "Debe generar más energía en las alturas");

        // En tormenta debe haber un boost del 50%
        int stormPower = EcoPowerEngine.calculateWindPower(base, 64, true);
        assertEquals(48, stormPower);
    }

    @Test
    @DisplayName("Receptor de Rayos debe captar energía masiva durante tormenta eléctrica")
    void testLightningReceptor() {
        int base = 512;

        // Despejado
        assertEquals(0, EcoPowerEngine.calculateLightningReceptorPower(base, false, false));

        // Lluvia sin truenos
        assertEquals(256, EcoPowerEngine.calculateLightningReceptorPower(base, false, true));

        // Tormenta con truenos y rayos
        assertEquals(1024, EcoPowerEngine.calculateLightningReceptorPower(base, true, true));
    }

    @Test
    @DisplayName("Verificar tipos y metadatos de EcoGeneratorType")
    void testGeneratorTypeMetadata() {
        for (EcoGeneratorType type : EcoGeneratorType.values()) {
            assertNotNull(type.getSlimefunId());
            assertTrue(type.getSlimefunId().startsWith("ECO_"));
            assertNotNull(type.getDefaultName());
            assertNotNull(type.getMaterial());
            assertTrue(type.getBaseOutput() > 0);
            assertTrue(type.getCapacity() >= type.getBaseOutput());
        }
    }
}

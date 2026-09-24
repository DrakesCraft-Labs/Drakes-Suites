package com.drakescraft.suites.generators.litexpansion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiteXpansionEngineTest {

    @Test
    void solarGenerationRequiresSkyVisibility() {
        LiteXpansionEngine engine = new LiteXpansionEngine(4096, 1.0, true);

        assertEquals(0, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.ADVANCED, true, false));
        assertEquals(0, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.HYBRID, false, false));
    }

    @Test
    void solarGenerationRatesMatchSpecification() {
        LiteXpansionEngine engine = new LiteXpansionEngine(4096, 1.0, true);

        // Advanced: 80 day, 10 night
        assertEquals(80, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.ADVANCED, true, true));
        assertEquals(10, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.ADVANCED, false, true));

        // Hybrid: 640 day, 80 night
        assertEquals(640, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.HYBRID, true, true));
        assertEquals(80, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.HYBRID, false, true));

        // Ultimate: 5120 day, 640 night
        assertEquals(5120, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.ULTIMATE, true, true));
        assertEquals(640, engine.calculateSolarGeneration(LiteXpansionEngine.SolarType.ULTIMATE, false, true));
    }

    @Test
    void reactorRequiresThoriumFuel() {
        LiteXpansionEngine engine = new LiteXpansionEngine(4096, 1.5, true);

        assertEquals(0, engine.calculateReactorOutput(false));
        assertEquals(6144, engine.calculateReactorOutput(true));
        assertTrue(engine.isVoidQuarryAllowed());
    }
}

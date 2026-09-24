package com.drakescraft.suites.tech.supreme;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupremeCompressionEngineTest {

    @Test
    void tierLimitBoundsEnforced() {
        SupremeCompressionEngine engine = new SupremeCompressionEngine(10, 4.0, true);
        assertEquals(10, engine.getTierLimit());
        assertEquals(4.0, engine.getQuantumReactorMultiplier());
        assertTrue(engine.isStrictCraftingCheck());

        SupremeCompressionEngine clamped = new SupremeCompressionEngine(99, 4.0, false);
        assertEquals(10, clamped.getTierLimit());
    }

    @Test
    void quantumEnergyScalingWithTiers() {
        SupremeCompressionEngine engine = new SupremeCompressionEngine(10, 4.0, true);

        // Tier 1: 4^0 * 1000 = 1000
        assertEquals(1000L, engine.calculateQuantumEnergyOutput(1, 1000L));

        // Tier 2: 4^1 * 1000 = 4000
        assertEquals(4000L, engine.calculateQuantumEnergyOutput(2, 1000L));

        // Tier 3: 4^2 * 1000 = 16000
        assertEquals(16000L, engine.calculateQuantumEnergyOutput(3, 1000L));

        // Invalid tiers
        assertEquals(0L, engine.calculateQuantumEnergyOutput(0, 1000L));
        assertEquals(0L, engine.calculateQuantumEnergyOutput(11, 1000L));
    }

    @Test
    void strictCraftingAntiDupeValidation() {
        SupremeCompressionEngine strictEngine = new SupremeCompressionEngine(10, 4.0, true);

        // Without matching PDC signature in strict mode -> rejected
        assertFalse(strictEngine.isValidCraftingOperation(64, 32, false));
        // With matching PDC signature and sufficient input -> accepted
        assertTrue(strictEngine.isValidCraftingOperation(64, 32, true));
        // Insufficient input -> rejected
        assertFalse(strictEngine.isValidCraftingOperation(16, 32, true));

        // Non-strict engine allows matching without PDC
        SupremeCompressionEngine nonStrict = new SupremeCompressionEngine(10, 4.0, false);
        assertTrue(nonStrict.isValidCraftingOperation(64, 32, false));
    }
}

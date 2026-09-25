// SPDX-License-Identifier: GPL-3.0-or-later
package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.geneticchickens.ExcitationEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcitationEngineTest {

    @Test
    @DisplayName("Excitation cycle duration scales properly across tiers")
    void testCycleTicks() {
        // baseTime = 50, tier = 0, dnaStrength = 0
        assertEquals(50, ExcitationEngine.calculateCycleTicks(50, 0, 0, 1));
        assertEquals(25, ExcitationEngine.calculateCycleTicks(50, 0, 0, 2));
        assertEquals(5, ExcitationEngine.calculateCycleTicks(50, 0, 0, 10));
        assertEquals(2, ExcitationEngine.calculateCycleTicks(50, 0, 0, 25));
    }

    @Test
    @DisplayName("Excitation duration never drops below 1 tick")
    void testTickFloor() {
        // Optimized chicken: baseTime = 14, tier = 1, dnaStrength = 8 -> rawTime = 1
        assertEquals(1, ExcitationEngine.calculateCycleTicks(14, 1, 8, 10)); // Ultimate
        assertEquals(1, ExcitationEngine.calculateCycleTicks(14, 1, 8, 25)); // Quantum
    }

    @Test
    @DisplayName("Quantum Excitation Chamber (25x) produces 2 to 3 items per cycle at tick floor")
    void testQuantumYieldMultiplier() {
        // Optimized chicken: baseTime = 14, tier = 1, dnaStrength = 8
        int yieldUltimate = ExcitationEngine.calculateYieldAmount(14, 1, 8, 10);
        assertEquals(1, yieldUltimate, "Ultimate should yield 1 item per cycle");

        for (int i = 0; i < 50; i++) {
            int yieldQuantum = ExcitationEngine.calculateYieldAmount(14, 1, 8, 25);
            assertTrue(yieldQuantum >= 2 && yieldQuantum <= 3,
                    "Quantum Excitation should yield 2 or 3 items (average 2.5x), got: " + yieldQuantum);
        }
    }
}

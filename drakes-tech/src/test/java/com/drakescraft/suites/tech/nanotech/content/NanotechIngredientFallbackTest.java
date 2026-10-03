package com.drakescraft.suites.tech.nanotech.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NanotechIngredientFallbackTest {

    /** Core Slimefun IDs used as the final fallback when no addon provides the ingredient. */
    private static final Set<String> CORE_SLIMEFUN_IDS = Set.of(
            "BASIC_CIRCUIT_BOARD", "ADVANCED_CIRCUIT_BOARD", "ANDROID_MEMORY_CORE",
            "ELECTRIC_MOTOR", "SMALL_CAPACITOR", "ENERGIZED_CAPACITOR",
            "CARBONADO_EDGED_CAPACITOR", "NETHERSTAR_REACTOR");

    @Test
    @DisplayName("Cada tier termina en un ingrediente del core de Slimefun (Nanotech no se cae sin addons)")
    void everyTierEndsWithCoreSlimefunItem() {
        for (int tier = 0; tier <= 7; tier++) {
            String[] control = NanotechContent.controlCandidates(tier);
            String[] power = NanotechContent.powerCandidates(tier);
            assertTrue(CORE_SLIMEFUN_IDS.contains(control[control.length - 1]),
                    "control tier " + tier + " sin respaldo del core: " + control[control.length - 1]);
            assertTrue(CORE_SLIMEFUN_IDS.contains(power[power.length - 1]),
                    "power tier " + tier + " sin respaldo del core: " + power[power.length - 1]);
        }
    }

    @Test
    @DisplayName("Los ingredientes de addons conservan prioridad sobre el respaldo del core")
    void addonIngredientsKeepPriority() {
        assertTrue(NanotechContent.controlCandidates(5)[0].equals("INFINITE_CIRCUIT"));
        assertTrue(NanotechContent.powerCandidates(5)[0].equals("INFINITY_CAPACITOR"));
        assertTrue(NanotechContent.powerCandidates(6)[0].equals("INFINITY_REACTOR"));
    }
}

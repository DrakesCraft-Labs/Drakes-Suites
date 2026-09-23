package com.drakescraft.suites.core;

import com.drakescraft.suites.core.nativeengine.NativeEngineBridge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NativeEngineBridgeTest {

    @Test
    @DisplayName("Suma saturada de energia EnergyNet previene desbordamiento int")
    void testSumSaturating() {
        assertEquals(0, NativeEngineBridge.sumSaturating(null));
        assertEquals(0, NativeEngineBridge.sumSaturating(new int[0]));
        assertEquals(150, NativeEngineBridge.sumSaturating(new int[]{50, 75, 25}));

        // Overflow
        int[] overflow = new int[]{Integer.MAX_VALUE - 10, 20};
        assertEquals(Integer.MAX_VALUE, NativeEngineBridge.sumSaturating(overflow));

        // Underflow
        int[] underflow = new int[]{Integer.MIN_VALUE + 10, -20};
        assertEquals(Integer.MIN_VALUE, NativeEngineBridge.sumSaturating(underflow));
    }

    @Test
    @DisplayName("Validacion de transferencia Cargo/Networks previene teleport-dupes")
    void testValidateCargoTransfer() {
        // Distancia normal (<= 128 bloques)
        assertTrue(NativeEngineBridge.validateCargoTransfer(0, 64, 0, 10, 64, 10, "NETWORKS_CABLE", 1));

        // Distancia excesiva (> 128 bloques, sospecha de teleport dupe)
        assertFalse(NativeEngineBridge.validateCargoTransfer(0, 64, 0, 500, 64, 500, "INFINITY_INGOT", 64));

        // Cantidad o item invalido
        assertFalse(NativeEngineBridge.validateCargoTransfer(0, 64, 0, 5, 64, 5, "TEST", 0));
        assertFalse(NativeEngineBridge.validateCargoTransfer(0, 64, 0, 5, 64, 5, null, 10));
    }

    @Test
    @DisplayName("Calculo 3D euclidiano de aura de jefes divinos")
    void testIsInsideBossAura() {
        // En radio de 15 bloques
        assertTrue(NativeEngineBridge.isInsideBossAura(100, 64, 100, 105, 64, 105, 15.0));

        // Fuera de radio
        assertFalse(NativeEngineBridge.isInsideBossAura(100, 64, 100, 150, 64, 150, 15.0));
    }

    @Test
    @DisplayName("Registro off-heap de bloques y maquinas")
    void testOffHeapBlockRegistry() {
        NativeEngineBridge.setBlockOffHeap("world", 10, 64, 10, "SOLAR_PANEL", "tier=3");
        assertEquals("SOLAR_PANEL;tier=3", NativeEngineBridge.getBlockOffHeap("world", 10, 64, 10));

        NativeEngineBridge.removeBlockOffHeap("world", 10, 64, 10);
        assertNull(NativeEngineBridge.getBlockOffHeap("world", 10, 64, 10));
    }
}

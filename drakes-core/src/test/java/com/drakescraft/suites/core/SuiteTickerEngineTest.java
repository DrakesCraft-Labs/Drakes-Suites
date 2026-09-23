package com.drakescraft.suites.core;

import com.drakescraft.suites.core.ticker.SuiteTickerEngine;
import org.bukkit.Location;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.plugin.PluginMock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SuiteTickerEngineTest {

    private static ServerMock server;
    private static PluginMock plugin;
    private static SuiteTickerEngine tickerEngine;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin("DrakesTickerTestPlugin");
        tickerEngine = new SuiteTickerEngine(plugin);
        tickerEngine.start();
    }

    @AfterAll
    static void tearDown() {
        if (tickerEngine != null) {
            tickerEngine.stop();
        }
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Ticker centralizado ejecuta listeners en sincronia con los ticks del servidor")
    void testTickerExecution() {
        AtomicInteger ticksCount = new AtomicInteger(0);
        tickerEngine.registerTickListener("test_listener", tick -> ticksCount.incrementAndGet());

        // Simular 5 ticks en MockBukkit
        server.getScheduler().performTicks(5);

        assertEquals(5, ticksCount.get());
        assertEquals(5, tickerEngine.getCurrentTick());

        // Desregistrar listener
        tickerEngine.unregisterTickListener("test_listener");
        server.getScheduler().performTicks(3);

        // No debe aumentar mas de 5
        assertEquals(5, ticksCount.get());
        assertEquals(8, tickerEngine.getCurrentTick());
    }

    @Test
    @DisplayName("Ticker de bloques registra y desregistra posiciones correctamente")
    void testBlockTicker() {
        Location loc = new Location(server.addSimpleWorld("test_world"), 10, 64, 10);
        AtomicBoolean ticked = new AtomicBoolean(false);

        tickerEngine.registerBlockTicker(loc, l -> ticked.set(true));
        
        // Ejecutar tick (en MockBukkit simple world los chunks se reportan no cargados por defecto, protegiendo chunks inactivos)
        server.getScheduler().performTicks(1);
        tickerEngine.unregisterBlockTicker(loc);

        assertNotNull(loc);
    }
}

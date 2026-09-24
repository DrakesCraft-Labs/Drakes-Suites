package com.drakescraft.suites.core;

import com.drakescraft.suites.core.runtime.PurpurRuntimeProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class PurpurRuntimeProviderTest {

    private static ServerMock server;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("PurpurRuntimeProvider detecta servidor y provee telemetria sin excepciones")
    void testRuntimeDetection() {
        assertNotNull(PurpurRuntimeProvider.getServerBrand());
        assertNotNull(PurpurRuntimeProvider.getServerVersion());

        // Debe ejecutarse sin lanzar excepciones en entorno de prueba
        double[] tps = PurpurRuntimeProvider.getRecentTps();
        assertNotNull(tps);
        assertEquals(3, tps.length);

        double mspt = PurpurRuntimeProvider.getAverageTickTimeMs();
        assertTrue(mspt > 0);

        // Fallback seguro en entidad nula
        assertFalse(PurpurRuntimeProvider.setEntityRidable(null, true));

        // Registro de diagnostico no debe fallar
        Logger logger = Logger.getLogger("PurpurTestLogger");
        assertDoesNotThrow(() -> PurpurRuntimeProvider.logRuntimeDiagnostics(logger));
    }
}

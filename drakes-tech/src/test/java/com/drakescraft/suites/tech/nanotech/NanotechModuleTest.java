package com.drakescraft.suites.tech.nanotech;

import com.drakescraft.suites.tech.DrakesTechPlugin;
import com.drakescraft.suites.tech.nanotech.content.ContentDefinition;
import com.drakescraft.suites.tech.nanotech.content.NanotechCatalog;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NanotechModuleTest {

    private static ServerMock server;
    private static DrakesTechPlugin plugin;
    private static NanotechModule module;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesTechPlugin.class);
        module = (NanotechModule) plugin.getModuleManager().getModule("nanotech");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("NanotechModule está registrado y habilitado en DrakesTech")
    void testModuleEnabled() {
        assertNotNull(module, "NanotechModule debe estar registrado");
        assertTrue(module.isEnabled(), "NanotechModule debe estar habilitado");
    }

    @Test
    @DisplayName("Catálogo canónico de Nanotecnología posee ítems válidos en inglés universal")
    void testCatalogValid() {
        List<ContentDefinition> items = NanotechCatalog.items();
        assertFalse(items.isEmpty(), "El catálogo de Nanotech no debe estar vacío");
        assertTrue(items.size() >= 40, "Debe tener al menos 40 ítems registrados");

        boolean hasArc = items.stream().anyMatch(i -> i.id().equals("PORTABLE_ARC_REACTOR"));
        boolean hasNano = items.stream().anyMatch(i -> i.id().equals("MARK_L_NANOCORE"));
        boolean hasGauntlet = items.stream().anyMatch(i -> i.id().equals("EMPTY_NANOGAUNTLET"));

        assertTrue(hasArc, "Debe incluir PORTABLE_ARC_REACTOR");
        assertTrue(hasNano, "Debe incluir MARK_L_NANOCORE");
        assertTrue(hasGauntlet, "Debe incluir EMPTY_NANOGAUNTLET");
    }
}

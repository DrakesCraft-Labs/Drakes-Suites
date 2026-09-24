package com.drakescraft.suites.server.economy;

import com.drakescraft.suites.server.DrakesServerPlugin;
import com.drakescraft.suites.server.economy.company.CompanyManager;
import com.drakescraft.suites.server.economy.sii.SIITaxEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChileanEconomyTest {

    private static ServerMock server;
    private static DrakesServerPlugin plugin;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesServerPlugin.class);
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("DrakesEconomyModule se inicializa y carga en DrakesServerPlugin")
    void testModuleLifecycle() {
        DrakesEconomyModule module = (DrakesEconomyModule) plugin.getModuleManager().getModule("market");
        assertNotNull(module, "DrakesEconomyModule debe estar registrado");
        assertTrue(module.isEnabled(), "DrakesEconomyModule debe estar habilitado");
        assertNotNull(module.getScale(), "ChileanEconomyScale no debe ser nulo");
        assertNotNull(module.getTaxEngine(), "SIITaxEngine no debe ser nulo");
        assertNotNull(module.getCompanyManager(), "CompanyManager no debe ser nulo");
    }

    @Test
    @DisplayName("ChileanEconomyScale escala a x1000 y formatea en pesos chilenos bancarios sin centavos")
    void testChileanEconomyScale() {
        ChileanEconomyScale scale = new ChileanEconomyScale(1000L);

        // 1 peso base -> 1.000 CLP ("la luca")
        assertEquals(1000L, scale.toChileanPesos(1.0));
        assertEquals(5500L, scale.toChileanPesos(5.5));
        assertEquals(1000000L, scale.toChileanPesos(1000.0));

        // Formato con punto de miles chileno
        assertEquals("$1.000 CLP", scale.format(1000L));
        assertEquals("$1.500.000 CLP", scale.format(1500000L));
        assertEquals("₯1.000", scale.formatDragmas(1000L));
        assertEquals("₯50.000", scale.formatDragmas(50000L));

        // Subsidio dinámico de novatos entre $50.000 y $250.000
        for (int i = 0; i < 50; i++) {
            long grant = scale.generateDynamicNewbieGrant(50000L, 250000L);
            assertTrue(grant >= 50000L && grant <= 250000L, "Grant debe estar en el rango configurado");
            assertEquals(0, grant % 1000L, "Grant debe ser múltiplo de $1.000 CLP");
        }
    }

    @Test
    @DisplayName("SIITaxEngine recauda IVA 19%, tasa /pay, impuesto de patrimonio y acumula en Tesoro Fiscal")
    void testSiiTaxEngine() {
        SIITaxEngine sii = new SIITaxEngine(0.19, 0.03, true);

        // IVA 19% sobre $100.000 -> $19.000
        long iva = sii.calculateIva(100000L);
        assertEquals(19000L, iva);
        assertEquals(19000L, sii.getFiscalTreasuryBalance());

        // Tax de transferencia /pay (3% sobre $50.000 -> $1.500)
        long payTax = sii.calculateTransferTax(50000L);
        assertEquals(1500L, payTax);
        assertEquals(20500L, sii.getFiscalTreasuryBalance()); // 19000 + 1500

        // Impuesto de riqueza progresivo:
        // Menos de 1M -> Exento
        assertEquals(0L, sii.calculateWealthTax(500000L));
        // Entre 1M y 10M -> 0.5% (sobre $5.000.000 -> $25.000)
        assertEquals(25000L, sii.calculateWealthTax(5000000L));
        // Más de 100M -> 3.0% (sobre $200.000.000 -> $6.000.000)
        assertEquals(6000000L, sii.calculateWealthTax(200000000L));

        // Retiro del Tesoro Fiscal para eventos o subsidios
        long currentTreasury = sii.getFiscalTreasuryBalance();
        long withdrawn = sii.withdrawFromTreasury(20000L);
        assertEquals(20000L, withdrawn);
        assertEquals(currentTreasury - 20000L, sii.getFiscalTreasuryBalance());
    }

    @Test
    @DisplayName("CompanyManager genera RUT chileno con Módulo 11 y gestiona socios y dividendos")
    void testCompanyManager() {
        CompanyManager manager = new CompanyManager(500000L);
        assertEquals(500000L, manager.getConstitutionFee());

        // Verificación algoritmo Módulo 11
        char dv76 = CompanyManager.calculateRutDv(76000001);
        assertTrue(Character.isLetterOrDigit(dv76));

        UUID founder = UUID.randomUUID();
        CompanyManager.Company comp = manager.registerCompany("Drakes Mining SpA", founder, "SpA");
        assertNotNull(comp);
        assertTrue(comp.getRut().startsWith("76."));
        assertEquals("Drakes Mining SpA", comp.getName());
        assertEquals("SpA", comp.getCompanyType());

        // No permite nombres duplicados
        assertNull(manager.registerCompany("Drakes Mining SpA", UUID.randomUUID(), "SpA"));

        // Depósitos y retiros
        comp.deposit(1000000L); // 1 millón
        assertEquals(1000000L, comp.getBalance());
        assertTrue(comp.withdraw(200000L));
        assertEquals(800000L, comp.getBalance());

        // Asignación de socios: Fundador 60%, Socio 40%
        UUID partner = UUID.randomUUID();
        comp.setShareholder(founder, 0.60);
        comp.setShareholder(partner, 0.40);

        // Reparto de dividendos de $500.000
        Map<UUID, Long> dividends = comp.distributeDividends(500000L);
        assertEquals(300000L, dividends.get(founder));
        assertEquals(200000L, dividends.get(partner));
        assertEquals(300000L, comp.getBalance()); // 800.000 - 500.000 = 300.000 restante
    }
}

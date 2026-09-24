package com.drakescraft.suites.server.economy;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import com.drakescraft.suites.server.economy.company.CompanyManager;
import com.drakescraft.suites.server.economy.sii.SIITaxEngine;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo de Economía Chilena, Impuestos (SII) y Sociedades Comerciales para DrakesServer.
 * Adapta el sistema financiero de Dallas a la escala monetaria de Chile (1 peso = $1.000 CLP / "lucas"),
 * aplicando retenciones de IVA (19%), impuestos de transferencias, auditoría y gestión de empresas.
 */
public class DrakesEconomyModule extends AbstractSuiteModule {

    private ChileanEconomyScale scale;
    private SIITaxEngine taxEngine;
    private CompanyManager companyManager;

    public DrakesEconomyModule(JavaPlugin plugin) {
        super(plugin, "market", "Drakes SlimeMarket, SII & Chilean Economy Engine");
    }

    @Override
    public void onEnable() {
        long multiplier = config.getLong("economy.scale-multiplier", 1000L);
        this.scale = new ChileanEconomyScale(multiplier);

        double ivaRate = config.getDouble("sii.iva-rate", 0.19);
        double transferTax = config.getDouble("sii.transfer-tax-rate", 0.03);
        boolean wealthTax = config.getBoolean("sii.wealth-tax-enabled", true);
        this.taxEngine = new SIITaxEngine(ivaRate, transferTax, wealthTax);

        long constitutionFee = config.getLong("companies.constitution-fee", 500000L);
        this.companyManager = new CompanyManager(constitutionFee);

        getPlugin().getLogger().info("[DrakesEconomy] Módulo de Economía Chilena & SII habilitado " +
                "(Escala: x" + multiplier + ", IVA: " + Math.round(ivaRate * 100) + "%, " +
                "TransferTax: " + Math.round(transferTax * 100) + "%).");
    }

    @Override
    public void onDisable() {
        this.scale = null;
        this.taxEngine = null;
        this.companyManager = null;
        getPlugin().getLogger().info("[DrakesEconomy] Módulo de Economía deshabilitado limpiamente.");
    }

    public ChileanEconomyScale getScale() {
        return scale;
    }

    public SIITaxEngine getTaxEngine() {
        return taxEngine;
    }

    public CompanyManager getCompanyManager() {
        return companyManager;
    }
}

package com.drakescraft.suites.tech;

import com.drakescraft.suites.core.DrakesCorePlugin;
import com.drakescraft.suites.core.module.GenericSuiteModule;
import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesTech - Mega-Suite de Logística Digital, Redes, Almacenamiento Cuántico e Industria Pesada.
 */
public class DrakesTechPlugin extends JavaPlugin {

    private static DrakesTechPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos técnicos absorbidos
        this.moduleManager.registerModule(new GenericSuiteModule(this, "networks", "Networks Digital Transport"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "infinity", "Infinity Expansion & Singularities"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "dynatech", "DynaTech Machines & Generators"));
        this.moduleManager.registerModule(new com.drakescraft.suites.tech.supreme.SupremeModule(this));
        this.moduleManager.registerModule(new com.drakescraft.suites.tech.fluffymachines.FluffyMachinesModule(this));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "nanotech", "Drakes Nanotechnology"));
        this.moduleManager.registerModule(new com.drakescraft.suites.tech.foxy.FoxyMachinesModule(this));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "sensibletoolbox", "SensibleToolbox Logistics"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "emctech", "EMCTech Matter Conversion"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "quaptics", "Quaptics Quantum Optics"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "advancedtech", "AdvancedTech Quantum Compressors & Accelerators"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "danktech", "DankTech2 Mass Condensers & Fluid Storage"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "globiamachines", "GlobiaMachines Industrial Processing Lines"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "nexcavate", "Nexcavate Autonomous Quarry & Mining"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "privatestorage", "PrivateStorage Biometric Secure Vaults"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "customizer", "SlimeCustomizer & Ryken Dynamic Engine"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "idreamofeasy", "IDreamOfEasy Compact Automation & Cells"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "sanecrafting", "SaneCrafting Balanced Workstations"));

        this.moduleManager.enableAll();

        // Registrar en DrakesCore
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().registerSuite("tech", this, this.moduleManager);
        }

        getLogger().info("DrakesTech v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().unregisterSuite("tech");
        }
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesTech deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesTechPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

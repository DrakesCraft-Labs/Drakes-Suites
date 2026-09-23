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
        this.moduleManager.registerModule(new GenericSuiteModule(this, "supreme", "Supreme Industrial Alloys"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "fluffymachines", "FluffyMachines Automation"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "nanotech", "Drakes Nanotechnology"));

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

package com.drakescraft.suites.tech;

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
        // Registro de módulos técnicos (Networks, DynaTech, Infinity, Supreme, FastMachines, Nanotech)
        this.moduleManager.enableAll();

        getLogger().info("DrakesTech v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
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

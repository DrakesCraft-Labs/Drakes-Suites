package com.drakescraft.suites.server;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesServer - Mega-Suite de Servidor, Motor Odysseia (Rust/Java), Aislamiento de Inventarios y Persistencia.
 */
public class DrakesServerPlugin extends JavaPlugin {

    private static DrakesServerPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos de servidor (OdysseiaBridge, InvSwitcher, PlayerVaultZ, AxGraves, BreweryX, Economy)
        this.moduleManager.enableAll();

        getLogger().info("DrakesServer v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesServer deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesServerPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

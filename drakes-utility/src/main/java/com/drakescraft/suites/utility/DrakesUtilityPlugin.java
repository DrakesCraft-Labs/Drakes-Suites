package com.drakescraft.suites.utility;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesUtility - Mega-Suite de Almacenamiento, Mochilas Tintadas, Terminales Inalámbricas y QoL.
 */
public class DrakesUtilityPlugin extends JavaPlugin {

    private static DrakesUtilityPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos de utilidad (DyedBackpacks, ColoredEnderChests, ChestTerminal, SFCalc, SlimeHUD)
        this.moduleManager.enableAll();

        getLogger().info("DrakesUtility v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesUtility deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesUtilityPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

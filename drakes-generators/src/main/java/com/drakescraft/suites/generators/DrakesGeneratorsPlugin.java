package com.drakescraft.suites.generators;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesGenerators - Mega-Suite de Matriz Energética, Reactores Nucleares, Generación Solar y Síntesis Mineral.
 */
public class DrakesGeneratorsPlugin extends JavaPlugin {

    private static DrakesGeneratorsPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos de generación (LiteXpansion, SMG, UltimateGenerators, EcoPower, OreChunks)
        this.moduleManager.enableAll();

        getLogger().info("DrakesGenerators v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesGenerators deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesGeneratorsPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

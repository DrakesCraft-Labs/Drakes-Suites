package com.drakescraft.suites.bio;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesBio - Mega-Suite de Biotecnología, Genética (Tiers 0 a 9), Botánica y Apicultura Slime.
 */
public class DrakesBioPlugin extends JavaPlugin {

    private static DrakesBioPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos biotecnológicos (GeneticChickens, ExoticGarden, Cultivation, SlimyBees, MobCapturer)
        this.moduleManager.enableAll();

        getLogger().info("DrakesBio v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesBio deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesBioPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

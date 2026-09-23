package com.drakescraft.suites.magic;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesMagic - Mega-Suite de Alquimia, Mística, Cristales de Resonancia y Relicarios de Cthonia.
 */
public class DrakesMagicPlugin extends JavaPlugin {

    private static DrakesMagicPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos arcanos (AlchimiaVitae, Crystamae, RelicsOfCthonia, SoulJars, Netheopoiesis)
        this.moduleManager.enableAll();

        getLogger().info("DrakesMagic v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesMagic deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesMagicPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

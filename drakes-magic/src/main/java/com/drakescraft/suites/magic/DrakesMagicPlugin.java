package com.drakescraft.suites.magic;

import com.drakescraft.suites.core.DrakesCorePlugin;
import com.drakescraft.suites.core.module.GenericSuiteModule;
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
        // Registro de módulos mágicos absorbidos
        this.moduleManager.registerModule(new GenericSuiteModule(this, "alchimia_vitae", "AlchimiaVitae Transmutations"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "crystamae", "Crystamae Resonance"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "relics_cthonia", "Relics of Cthonia Underworld"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "soul_jars", "Soul Containment Jars"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "netheopoiesis", "Netheopoiesis Flora"));

        this.moduleManager.enableAll();

        // Registrar en DrakesCore
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().registerSuite("magic", this, this.moduleManager);
        }

        getLogger().info("DrakesMagic v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().unregisterSuite("magic");
        }
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

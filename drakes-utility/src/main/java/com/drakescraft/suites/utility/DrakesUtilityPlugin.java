package com.drakescraft.suites.utility;

import com.drakescraft.suites.core.DrakesCorePlugin;
import com.drakescraft.suites.core.module.GenericSuiteModule;
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
        // Registro de módulos de utilidad y almacenamiento absorbidos
        this.moduleManager.registerModule(new GenericSuiteModule(this, "backpacks", "DyedBackpacks & Filters"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "colored_enderchests", "Frequency Colored EnderChests"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "chest_terminal", "ChestTerminal Digital Access"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "sfcalc", "SFCalc Recipe Calculator"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "slimehud", "SlimeHUD Holographic Overlay"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "soundmuffler", "SoundMuffler Noise Dampener"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "simpleutils", "SimpleUtils Utility Blocks"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "slimeframe", "SlimeFrame Item Displays"));

        this.moduleManager.enableAll();

        // Registrar en DrakesCore
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().registerSuite("utility", this, this.moduleManager);
        }

        getLogger().info("DrakesUtility v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().unregisterSuite("utility");
        }
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

package com.drakescraft.suites.core;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import com.drakescraft.suites.core.ticker.SuiteTickerEngine;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesCore - Kernel del Ecosistema DrakesCraft: Core Engine, API, Modular Loader y Ticker Centralizado.
 */
public class DrakesCorePlugin extends JavaPlugin {

    private static DrakesCorePlugin instance;
    private SuiteTickerEngine tickerEngine;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.tickerEngine = new SuiteTickerEngine(this);
        this.tickerEngine.start();

        this.moduleManager = new SuiteModuleManager(this);
        this.moduleManager.enableAll();

        getLogger().info("DrakesCore v" + getPluginMeta().getVersion() + " (Kernel & Ticker Engine) inicializado con exito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        if (tickerEngine != null) {
            tickerEngine.stop();
        }
        getLogger().info("DrakesCore deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesCorePlugin getInstance() {
        return instance;
    }

    public SuiteTickerEngine getTickerEngine() {
        return tickerEngine;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

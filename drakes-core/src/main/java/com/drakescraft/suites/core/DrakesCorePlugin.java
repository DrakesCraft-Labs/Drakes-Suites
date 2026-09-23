package com.drakescraft.suites.core;

import com.drakescraft.suites.core.command.DrakesSuitesCommand;
import com.drakescraft.suites.core.module.SuiteModuleManager;
import com.drakescraft.suites.core.registry.SuiteRegistry;
import com.drakescraft.suites.core.ticker.SuiteTickerEngine;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesCore - Kernel del Ecosistema DrakesCraft: Core Engine, API, Modular Loader y Ticker Centralizado.
 */
public class DrakesCorePlugin extends JavaPlugin {

    private static DrakesCorePlugin instance;
    private SuiteTickerEngine tickerEngine;
    private SuiteModuleManager moduleManager;
    private SuiteRegistry suiteRegistry;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.suiteRegistry = new SuiteRegistry();

        this.tickerEngine = new SuiteTickerEngine(this);
        this.tickerEngine.start();

        this.moduleManager = new SuiteModuleManager(this);
        this.moduleManager.enableAll();

        // Inicializar aceleración nativa Rust FFM (Slimefun-Rust y Odysseia-Rust)
        com.drakescraft.suites.core.nativeengine.NativeEngineBridge.initialize(getDataFolder().toPath());

        // Registrar Suite 0 en el registro global
        this.suiteRegistry.registerSuite("core", this, this.moduleManager);

        // Registrar comando maestro
        PluginCommand cmd = getCommand("drakessuites");
        if (cmd != null) {
            DrakesSuitesCommand executor = new DrakesSuitesCommand(this);
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }

        getLogger().info("DrakesCore v" + getPluginMeta().getVersion() + " (Kernel & Ticker Engine) inicializado con exito.");
    }

    @Override
    public void onDisable() {
        if (suiteRegistry != null) {
            suiteRegistry.unregisterSuite("core");
        }
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

    public SuiteRegistry getSuiteRegistry() {
        return suiteRegistry;
    }
}

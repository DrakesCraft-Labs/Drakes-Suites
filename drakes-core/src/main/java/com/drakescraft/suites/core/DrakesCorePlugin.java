package com.drakescraft.suites.core;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesCore - Kernel del Ecosistema DrakesCraft: Core Engine, API y Ticker Centralizado.
 */
public class DrakesCorePlugin extends JavaPlugin {

    private static DrakesCorePlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesCore v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesCore deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesCorePlugin getInstance() {
        return instance;
    }
}

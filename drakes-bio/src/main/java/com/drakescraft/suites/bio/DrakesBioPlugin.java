package com.drakescraft.suites.bio;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesBio - Mega-Suite de Biotecnologia, Genetica Avanzada (T0-T9), Clonacion y Botanica.
 */
public class DrakesBioPlugin extends JavaPlugin {

    private static DrakesBioPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesBio v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesBio deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesBioPlugin getInstance() {
        return instance;
    }
}

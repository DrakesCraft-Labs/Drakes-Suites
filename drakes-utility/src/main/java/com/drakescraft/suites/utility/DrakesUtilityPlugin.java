package com.drakescraft.suites.utility;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesUtility - Mega-Suite de Utilidades, Mochilas Tintadas, EnderChests y Herramientas QoL.
 */
public class DrakesUtilityPlugin extends JavaPlugin {

    private static DrakesUtilityPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesUtility v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesUtility deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesUtilityPlugin getInstance() {
        return instance;
    }
}

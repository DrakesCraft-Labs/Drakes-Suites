package com.drakescraft.suites.server;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesServer - Mega-Suite de Servidor y Gameplay Standalone: Motor Odysseia, Economia y Social.
 */
public class DrakesServerPlugin extends JavaPlugin {

    private static DrakesServerPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesServer v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesServer deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesServerPlugin getInstance() {
        return instance;
    }
}

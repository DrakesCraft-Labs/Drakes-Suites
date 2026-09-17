package com.drakescraft.suites.tech;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesTech - Mega-Suite de Logistica Digital, Redes, Almacenamiento Cuantico e Industria Pesada.
 */
public class DrakesTechPlugin extends JavaPlugin {

    private static DrakesTechPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesTech v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesTech deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesTechPlugin getInstance() {
        return instance;
    }
}

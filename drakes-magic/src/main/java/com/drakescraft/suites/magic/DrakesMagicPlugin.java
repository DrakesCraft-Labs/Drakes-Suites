package com.drakescraft.suites.magic;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesMagic - Mega-Suite de Artes Arcanas, Alquimia, Reliquias Antiguas y Misticismo.
 */
public class DrakesMagicPlugin extends JavaPlugin {

    private static DrakesMagicPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesMagic v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesMagic deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesMagicPlugin getInstance() {
        return instance;
    }
}

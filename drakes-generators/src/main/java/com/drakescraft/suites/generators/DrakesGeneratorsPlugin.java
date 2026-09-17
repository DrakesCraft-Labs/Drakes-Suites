package com.drakescraft.suites.generators;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesGenerators - Mega-Suite de Matriz Energetica, Reactores Nucleares/Solares y Generacion Cuantica.
 */
public class DrakesGeneratorsPlugin extends JavaPlugin {

    private static DrakesGeneratorsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesGenerators v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesGenerators deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesGeneratorsPlugin getInstance() {
        return instance;
    }
}

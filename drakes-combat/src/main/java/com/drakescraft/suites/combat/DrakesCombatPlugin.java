package com.drakescraft.suites.combat;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesCombat - Mega-Suite de Arsenal Tactico, Armas de Energia, Armaduras y SlimefunWarfare.
 */
public class DrakesCombatPlugin extends JavaPlugin {

    private static DrakesCombatPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DrakesCombat v" + getPluginMeta().getVersion() + " inicializado con exito.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DrakesCombat deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesCombatPlugin getInstance() {
        return instance;
    }
}

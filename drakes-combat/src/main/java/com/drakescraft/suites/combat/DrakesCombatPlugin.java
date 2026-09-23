package com.drakescraft.suites.combat;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesCombat - Mega-Suite de Arsenal, SlimeTinker, Armamento Bélico y Exoesqueletos de Combate.
 */
public class DrakesCombatPlugin extends JavaPlugin {

    private static DrakesCombatPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos bélicos (SlimeTinker, SlimefunWarfare, ExtraGear, ExtraTools, LuckyBlocks)
        this.moduleManager.enableAll();

        getLogger().info("DrakesCombat v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesCombat deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesCombatPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

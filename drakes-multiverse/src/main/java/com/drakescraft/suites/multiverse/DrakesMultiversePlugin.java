package com.drakescraft.suites.multiverse;

import com.chagui68.multiversenets.MultiverseNets;
import com.drakescraft.suites.core.DrakesCorePlugin;
import com.drakescraft.suites.core.module.GenericSuiteModule;
import com.drakescraft.suites.core.module.SuiteModuleManager;

/**
 * DrakesMultiverse - Suite Oficial del Multiverso concebida y creada por Chagui68.
 * Absorbe y consolida de forma modular:
 * - MultiverseCreatures (criaturas del multiverso, bosses, rituales y dimensiones)
 * - MultiverseNets (redes digitales standalone de almacenamiento y transporte sin Slimefun)
 *
 * Autor Canónico: Chagui68
 */
public class DrakesMultiversePlugin extends MultiverseNets {

    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        // Inicializar motor soberano de redes digitales de Chagui68
        super.onEnable();

        this.moduleManager = new SuiteModuleManager(this);

        // Módulos soberanos de la Suite de Chagui
        this.moduleManager.registerModule(new GenericSuiteModule(this, "creatures", "Multiverse Creatures & Bosses (por Chagui68)"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "nets", "Multiverse Standalone Digital Networks (por Chagui68)"));

        this.moduleManager.enableAll();

        DrakesCorePlugin core = DrakesCorePlugin.getInstance();
        if (core != null && core.getSuiteRegistry() != null) {
            core.getSuiteRegistry().registerSuite("multiverse", this, this.moduleManager);
        }

        getLogger().info("§5[DrakesMultiverse] §dSuite Multiverse de Chagui68 v" + getPluginMeta().getVersion() + " inicializada con exito.");
    }

    @Override
    public void onDisable() {
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        DrakesCorePlugin core = DrakesCorePlugin.getInstance();
        if (core != null && core.getSuiteRegistry() != null) {
            core.getSuiteRegistry().unregisterSuite("multiverse");
        }
        super.onDisable();
        getLogger().info("[DrakesMultiverse] Suite de Chagui68 deshabilitada limpiamente.");
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

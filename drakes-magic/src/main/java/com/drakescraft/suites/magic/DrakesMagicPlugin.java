package com.drakescraft.suites.magic;

import com.drakescraft.suites.core.DrakesCorePlugin;
import com.drakescraft.suites.core.module.GenericSuiteModule;
import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * DrakesMagic - Mega-Suite de Alquimia, Mística, Cristales de Resonancia y Relicarios de Cthonia.
 */
public class DrakesMagicPlugin extends JavaPlugin {

    private static DrakesMagicPlugin instance;
    private SuiteModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.moduleManager = new SuiteModuleManager(this);
        // Registro de módulos mágicos absorbidos
        this.moduleManager.registerModule(new com.drakescraft.suites.magic.alchimiavitae.AlchimiaVitaeModule(this));
        this.moduleManager.registerModule(new com.drakescraft.suites.magic.crystamae.CrystamaeModule(this));
        this.moduleManager.registerModule(new com.drakescraft.suites.magic.relics.RelicsCthoniaModule(this));
        this.moduleManager.registerModule(new com.drakescraft.suites.magic.souljars.SoulJarsModule(this));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "netheopoiesis", "Netheopoiesis Flora"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "transcendence", "TranscEndence Dimensional Arcana"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "spiritsunchained", "SpiritsUnchained Ancestral Evocation"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "demonic_expansion", "DemonicExpansion Dark Arts & Pacts"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "element_manipulation", "ElementManipulation Quad-Elemental Arts"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "magic_xpansion", "MagicXpansion Thaumaturgy & Grimoires"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "slimechem", "SlimeChem Alchemical Chemistry & Transmutation"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "coronalis", "Coronalis Solar Radiance & Plasma Energy"));
        this.moduleManager.registerModule(new GenericSuiteModule(this, "infernal_expansion", "InfernalExpansion Nether Arcana & Basalt Fusion"));

        this.moduleManager.enableAll();

        // Registrar en DrakesCore
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().registerSuite("magic", this, this.moduleManager);
        }

        getLogger().info("DrakesMagic v" + getPluginMeta().getVersion() + " inicializado con éxito.");
    }

    @Override
    public void onDisable() {
        if (DrakesCorePlugin.getInstance() != null && DrakesCorePlugin.getInstance().getSuiteRegistry() != null) {
            DrakesCorePlugin.getInstance().getSuiteRegistry().unregisterSuite("magic");
        }
        if (moduleManager != null) {
            moduleManager.disableAll();
        }
        getLogger().info("DrakesMagic deshabilitado limpiamente.");
        instance = null;
    }

    public static DrakesMagicPlugin getInstance() {
        return instance;
    }

    public SuiteModuleManager getModuleManager() {
        return moduleManager;
    }
}

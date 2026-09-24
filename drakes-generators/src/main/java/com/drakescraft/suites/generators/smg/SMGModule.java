package com.drakescraft.suites.generators.smg;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo de generadores de materiales SMG para DrakesGenerators.
 */
public class SMGModule extends AbstractSuiteModule {

    private SMGGeneratorEngine engine;

    public SMGModule(JavaPlugin plugin) {
        super(plugin, "smg", "SMG Material Generators");
    }

    @Override
    public void onEnable() {
        double overclock = config.getDouble("features.overclock-speed", 1.0);
        this.engine = new SMGGeneratorEngine(overclock);

        getPlugin().getLogger().info("[DrakesGenerators] SMGModule habilitado con overclock x" + overclock + ".");
    }

    @Override
    public void onDisable() {
        if (this.engine != null) {
            this.engine.clear();
            this.engine = null;
        }
        getPlugin().getLogger().info("[DrakesGenerators] SMGModule deshabilitado limpiamente.");
    }

    public SMGGeneratorEngine getEngine() {
        return engine;
    }
}

package com.drakescraft.suites.generators.litexpansion;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo de generación de energía LiteXpansion para DrakesGenerators.
 * Soporta paneles solares avanzados (Advanced, Hybrid, Ultimate) y reactores de vacío con Torio.
 */
public class LiteXpansionModule extends AbstractSuiteModule {

    private LiteXpansionEngine engine;

    public LiteXpansionModule(JavaPlugin plugin) {
        super(plugin, "litexpansion", "LiteXpansion Void Reactors & Solar Grids");
    }

    @Override
    public void onEnable() {
        int reactorJoules = config.getInt("reactor-joules-per-tick", 4096);
        double fuelRate = config.getDouble("fuel-consumption-rate", 1.0);
        boolean allowQuarry = config.getBoolean("allow-void-quarry", true);

        this.engine = new LiteXpansionEngine(reactorJoules, fuelRate, allowQuarry);
        getPlugin().getLogger().info("[DrakesGenerators] LiteXpansionModule activado (Reactor=" + reactorJoules + " J/t).");
    }

    @Override
    public void onDisable() {
        this.engine = null;
        getPlugin().getLogger().info("[DrakesGenerators] LiteXpansionModule deshabilitado limpiamente.");
    }

    public LiteXpansionEngine getEngine() {
        return engine;
    }
}

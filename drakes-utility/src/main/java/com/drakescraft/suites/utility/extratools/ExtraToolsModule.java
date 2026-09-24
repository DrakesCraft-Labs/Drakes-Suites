package com.drakescraft.suites.utility.extratools;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo nativo de ExtraTools para DrakesUtility.
 * Integra el Martillo pulverizador de área 3x3 y maquinaria industrial compacta:
 * generadores de adoquín eléctricos, fábricas de concreto, pulverizadores y transmutadores.
 */
public class ExtraToolsModule extends AbstractSuiteModule {

    private boolean hammerEnabled = true;
    private double energyConsumptionMultiplier = 1.0;
    private int cobbleGenIntervalTicks = 20;

    public ExtraToolsModule(JavaPlugin plugin) {
        super(plugin, "extratools", "ExtraTools 3x3 Mining & Multi-Harvest Tools");
    }

    @Override
    public void onEnable() {
        this.hammerEnabled = config.getBoolean("hammer.enabled", true);
        this.energyConsumptionMultiplier = config.getDouble("machines.energy-consumption-multiplier", 1.0);
        this.cobbleGenIntervalTicks = config.getInt("cobblestone-generator.interval-ticks", 20);

        getPlugin().getLogger().info("[DrakesUtility] ExtraToolsModule habilitado con "
                + ExtraToolsItemsRegistry.getAllItems().size() + " herramientas y máquinas industriales.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[DrakesUtility] ExtraToolsModule deshabilitado limpiamente.");
    }

    @Override
    public void onReload() {
        this.hammerEnabled = config.getBoolean("hammer.enabled", true);
        this.energyConsumptionMultiplier = config.getDouble("machines.energy-consumption-multiplier", 1.0);
        this.cobbleGenIntervalTicks = config.getInt("cobblestone-generator.interval-ticks", 20);
        getPlugin().getLogger().info("[DrakesUtility] ExtraToolsModule recargado con éxito.");
    }

    public boolean isHammerEnabled() {
        return hammerEnabled;
    }

    public double getEnergyConsumptionMultiplier() {
        return energyConsumptionMultiplier;
    }

    public int getCobbleGenIntervalTicks() {
        return cobbleGenIntervalTicks;
    }
}

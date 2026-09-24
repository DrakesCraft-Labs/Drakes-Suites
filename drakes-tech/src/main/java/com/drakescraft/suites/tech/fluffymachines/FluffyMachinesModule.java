package com.drakescraft.suites.tech.fluffymachines;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo de automatización y auto-crafteadores FluffyMachines para DrakesTech.
 */
public class FluffyMachinesModule extends AbstractSuiteModule {

    private FluffyCrafterEngine engine;

    public FluffyMachinesModule(JavaPlugin plugin) {
        super(plugin, "fluffymachines", "FluffyMachines Automation");
    }

    @Override
    public void onEnable() {
        int delay = config.getInt("performance.auto-crafter-tick-delay", 2);
        boolean snapshotValidation = config.getBoolean("anti-dupe.inventory-snapshot-validation", true);

        this.engine = new FluffyCrafterEngine(delay, snapshotValidation);

        getPlugin().getLogger().info("[DrakesTech] FluffyMachinesModule habilitado (Tick delay: "
                + delay + ", Snapshot validation: " + snapshotValidation + ").");
    }

    @Override
    public void onDisable() {
        if (this.engine != null) {
            this.engine.clear();
            this.engine = null;
        }
        getPlugin().getLogger().info("[DrakesTech] FluffyMachinesModule deshabilitado limpiamente.");
    }

    public FluffyCrafterEngine getEngine() {
        return engine;
    }
}

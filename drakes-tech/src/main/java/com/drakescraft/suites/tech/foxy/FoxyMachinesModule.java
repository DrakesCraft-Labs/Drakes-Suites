package com.drakescraft.suites.tech.foxy;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo FoxyMachines integrado en DrakesTech.
 * Administra maquinaria pesada de refinamiento eléctrico, forjas de mejora y protecciones de buffer.
 */
public class FoxyMachinesModule extends AbstractSuiteModule {

    private FoxyRefineryEngine engine;

    public FoxyMachinesModule(JavaPlugin plugin) {
        super(plugin, "foxy", "FoxyMachines Automation");
    }

    @Override
    public void onEnable() {
        boolean chunkGuard = config.getBoolean("performance.chunk-guard", true);
        int maxBuffer = config.getInt("performance.max-buffer", 1024);

        this.engine = new FoxyRefineryEngine(chunkGuard, maxBuffer);

        getPlugin().getLogger().info("[DrakesTech] FoxyMachinesModule habilitado (Chunk guard: "
                + chunkGuard + ", Max buffer: " + maxBuffer + " J).");
    }

    @Override
    public void onDisable() {
        if (this.engine != null) {
            this.engine.clear();
            this.engine = null;
        }
        getPlugin().getLogger().info("[DrakesTech] FoxyMachinesModule deshabilitado limpiamente.");
    }

    public FoxyRefineryEngine getEngine() {
        return engine;
    }
}

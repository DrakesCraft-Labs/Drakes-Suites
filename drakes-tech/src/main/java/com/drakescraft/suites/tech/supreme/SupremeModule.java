package com.drakescraft.suites.tech.supreme;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo Supreme de compresión cuántica, forjas pesadas y reactores de alto rendimiento para DrakesTech.
 */
public class SupremeModule extends AbstractSuiteModule {

    private SupremeCompressionEngine engine;

    public SupremeModule(JavaPlugin plugin) {
        super(plugin, "supreme", "Supreme Expansion & Quantum Compression");
    }

    @Override
    public void onEnable() {
        int tierLimit = config.getInt("features.supreme-tier-limit", 10);
        double multiplier = config.getDouble("features.quantum-reactor-multiplier", 4.0);
        boolean strictCrafting = config.getBoolean("anti-dupe.strict-crafting-check", true);

        this.engine = new SupremeCompressionEngine(tierLimit, multiplier, strictCrafting);
        getPlugin().getLogger().info("[DrakesTech] SupremeModule activado (Tiers 1-" + tierLimit + ", Multiplier=" + multiplier + "x).");
    }

    @Override
    public void onDisable() {
        this.engine = null;
        getPlugin().getLogger().info("[DrakesTech] SupremeModule deshabilitado limpiamente.");
    }

    public SupremeCompressionEngine getEngine() {
        return engine;
    }
}

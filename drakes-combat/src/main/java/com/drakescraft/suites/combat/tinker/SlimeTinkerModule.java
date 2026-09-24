package com.drakescraft.suites.combat.tinker;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo SlimeTinker integrado nativamente en DrakesCombat para 26.x.
 * Administra herramientas modulares, modificadores elementales y el adaptador
 * canónico de armaduras (True Damage contra jugadores AFK/overgeared).
 */
public class SlimeTinkerModule extends AbstractSuiteModule {

    private TinkerToolAssemblyEngine assemblyEngine;
    private SlimefunArmorAdaptation armorAdaptation;

    public SlimeTinkerModule(JavaPlugin plugin) {
        super(plugin, "slimetinker", "SlimeTinker Custom Tools & Traits");
    }

    @Override
    public void onEnable() {
        int maxModifiers = config.getInt("max-modifiers-per-tool", 5);
        boolean allowInfinity = config.getBoolean("allow-infinity-materials", true);
        double decayRate = config.getDouble("anti-afk-decay-rate", 0.05);
        boolean trueDamage = config.getBoolean("enable-true-damage-adaptation", true);

        this.assemblyEngine = new TinkerToolAssemblyEngine(maxModifiers, allowInfinity);
        this.armorAdaptation = new SlimefunArmorAdaptation(decayRate, trueDamage);

        getPlugin().getLogger().info("[DrakesCombat] SlimeTinkerModule activado " +
                "(MaxModifiers: " + maxModifiers +
                ", AllowInfinity: " + allowInfinity +
                ", AntiAfkDecay: " + decayRate + ").");
    }

    @Override
    public void onDisable() {
        this.assemblyEngine = null;
        this.armorAdaptation = null;
        getPlugin().getLogger().info("[DrakesCombat] SlimeTinkerModule deshabilitado limpiamente.");
    }

    public TinkerToolAssemblyEngine getAssemblyEngine() {
        return assemblyEngine;
    }

    public SlimefunArmorAdaptation getArmorAdaptation() {
        return armorAdaptation;
    }
}

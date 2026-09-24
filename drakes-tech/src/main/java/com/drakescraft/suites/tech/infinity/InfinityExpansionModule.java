package com.drakescraft.suites.tech.infinity;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo Infinity Expansion integrado nativamente en DrakesTech para 26.x.
 * Administra singularidades atómicas, armaduras cósmicas (con counter para jefes divinos)
 * y canteras cuánticas optimizadas para el rendimiento de Dallas.
 */
public class InfinityExpansionModule extends AbstractSuiteModule {

    private InfinitySingularityEngine singularityEngine;
    private InfinityArmorEngine armorEngine;
    private InfinityQuarryEngine quarryEngine;

    public InfinityExpansionModule(JavaPlugin plugin) {
        super(plugin, "infinity", "Infinity Expansion & Singularities");
    }

    @Override
    public void onEnable() {
        boolean strictAntiDupe = config.getBoolean("anti-dupe.strict-crafting-check", true);
        this.singularityEngine = new InfinitySingularityEngine(strictAntiDupe);

        // Cargar singularidades personalizadas del yaml si existen
        if (config.isConfigurationSection("singularities")) {
            for (String key : config.getConfigurationSection("singularities").getKeys(false)) {
                int req = config.getInt("singularities." + key + ".required", config.getInt("singularities." + key, -1));
                if (req > 0) {
                    long energy = config.getLong("singularities." + key + ".energy", 1000000L);
                    singularityEngine.registerSingularity(key, req, energy);
                }
            }
        }

        double damageReduction = config.getDouble("infinity-armor.damage-reduction", 0.95);
        boolean antiVoid = config.getBoolean("infinity-armor.anti-void", true);
        boolean godBypass = config.getBoolean("infinity-armor.god-true-damage-bypass", true);
        this.armorEngine = new InfinityArmorEngine(damageReduction, antiVoid, godBypass);

        int maxBlocksPerTick = config.getInt("quarries.max-blocks-per-tick", 8);
        long baseEnergy = config.getLong("quarries.base-energy-per-block", 250L);
        boolean chunkProtection = config.getBoolean("quarries.chunk-protection", true);
        this.quarryEngine = new InfinityQuarryEngine(maxBlocksPerTick, baseEnergy, chunkProtection);

        getPlugin().getLogger().info("[DrakesTech] InfinityExpansionModule activado " +
                "(Singularidades: " + singularityEngine.getRegisteredSingularities().size() +
                ", AntiVoid: " + antiVoid +
                ", GodBypass: " + godBypass +
                ", QuarrySpeed: " + maxBlocksPerTick + " blk/t).");
    }

    @Override
    public void onDisable() {
        if (quarryEngine != null) {
            quarryEngine.clear();
            quarryEngine = null;
        }
        singularityEngine = null;
        armorEngine = null;
        getPlugin().getLogger().info("[DrakesTech] InfinityExpansionModule deshabilitado limpiamente.");
    }

    public InfinitySingularityEngine getSingularityEngine() {
        return singularityEngine;
    }

    public InfinityArmorEngine getArmorEngine() {
        return armorEngine;
    }

    public InfinityQuarryEngine getQuarryEngine() {
        return quarryEngine;
    }
}

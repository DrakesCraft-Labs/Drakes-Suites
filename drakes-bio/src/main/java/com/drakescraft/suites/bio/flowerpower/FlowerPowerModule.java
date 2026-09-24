package com.drakescraft.suites.bio.flowerpower;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo nativo de FlowerPower para DrakesBio.
 * Integra maquinaria botánica, aceleración de floración con Semillas de Sobrecrecimiento,
 * almacenamiento condensado de experiencia y amuletos de atributos arcanos.
 */
public class FlowerPowerModule extends AbstractSuiteModule {

    private int minSeedYield = 2;
    private int maxSeedYield = 4;
    private int maxCauldronExp = ExperienceCauldronEngine.DEFAULT_MAX_EXP;

    public FlowerPowerModule(JavaPlugin plugin) {
        super(plugin, "flowerpower", "FlowerPower Botanical Acceleration");
    }

    @Override
    public void onEnable() {
        this.minSeedYield = config.getInt("seed.min-yield", 2);
        this.maxSeedYield = config.getInt("seed.max-yield", 4);
        this.maxCauldronExp = config.getInt("cauldron.max-exp", ExperienceCauldronEngine.DEFAULT_MAX_EXP);

        getPlugin().getLogger().info("[DrakesBio] FlowerPowerModule habilitado con "
                + FlowerPowerItemsRegistry.getAllItems().size() + " artefactos botánicos, "
                + CharmType.values().length + " amuletos y compatibilidad para "
                + FlowerDuplicationEngine.getCompatibleFlowers().size() + " especies florales.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[DrakesBio] FlowerPowerModule deshabilitado limpiamente.");
    }

    @Override
    public void onReload() {
        this.minSeedYield = config.getInt("seed.min-yield", 2);
        this.maxSeedYield = config.getInt("seed.max-yield", 4);
        this.maxCauldronExp = config.getInt("cauldron.max-exp", ExperienceCauldronEngine.DEFAULT_MAX_EXP);
        getPlugin().getLogger().info("[DrakesBio] FlowerPowerModule recargado con éxito.");
    }

    public int getMinSeedYield() {
        return minSeedYield;
    }

    public int getMaxSeedYield() {
        return maxSeedYield;
    }

    public int getMaxCauldronExp() {
        return maxCauldronExp;
    }
}

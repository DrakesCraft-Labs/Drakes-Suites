package com.drakescraft.suites.bio.slimybees;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Módulo nativo de apicultura y genética de abejas SlimyBees para drakes-bio.
 */
@Getter
public class SlimyBeesModule extends AbstractSuiteModule {

    private double mutationRateMultiplier = 1.0;
    private boolean manualCentrifugeEnabled = true;
    private int electricCentrifugeSpeed = 1;
    private int breedingCycleSeconds = 60;

    public SlimyBeesModule(JavaPlugin plugin) {
        super(plugin, "slimy_bees", "SlimyBees Genetic Apiary");
    }

    @Override
    public void onEnable() {
        applyConfiguration();
        getPlugin().getLogger().info("[SlimyBees] Módulo de apicultura y genética activado con éxito.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[SlimyBees] Módulo de apicultura desactivado.");
    }

    @Override
    public void onReload() {
        super.onReload();
        applyConfiguration();
    }

    private void applyConfiguration() {
        FileConfiguration cfg = getConfig();
        if (cfg != null) {
            this.mutationRateMultiplier = cfg.getDouble("mutation-rate-multiplier", 1.0);
            this.manualCentrifugeEnabled = cfg.getBoolean("manual-centrifuge-enabled", true);
            this.electricCentrifugeSpeed = cfg.getInt("electric-centrifuge-speed", 1);
            this.breedingCycleSeconds = cfg.getInt("breeding-cycle-seconds", 60);
        }
    }

    /**
     * Cruza una princesa y un zángano usando los genomas codificados en sus ItemStacks.
     */
    public BeeBreedingEngine.BreedingOutcome breed(ItemStack princessItem, ItemStack droneItem, double extraModifier) {
        BeeGenome princessGenome = BeeItemHelper.extractGenome(princessItem);
        BeeGenome droneGenome = BeeItemHelper.extractGenome(droneItem);

        if (princessGenome == null || droneGenome == null) {
            return null;
        }

        double multiplier = this.mutationRateMultiplier * extraModifier;
        return BeeBreedingEngine.breed(princessGenome, droneGenome, multiplier);
    }

    /**
     * Procesa un panal en la centrifugadora.
     */
    public List<ItemStack> processComb(String combType) {
        return CentrifugeRecipeRegistry.processComb(combType);
    }
}

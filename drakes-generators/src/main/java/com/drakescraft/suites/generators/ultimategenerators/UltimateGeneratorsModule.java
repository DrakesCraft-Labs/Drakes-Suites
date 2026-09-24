package com.drakescraft.suites.generators.ultimategenerators;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Módulo nativo de Generadores Pesados, Redes de Combustión y Biorefinerías UltimateGenerators2 para drakes-generators.
 */
@Getter
public class UltimateGeneratorsModule extends AbstractSuiteModule {

    private int dieselRate = 128;
    private int biofuelRate = 64;
    private int dragonBreathRate = 256;
    private int endlessRate = 512;

    public UltimateGeneratorsModule(JavaPlugin plugin) {
        super(plugin, "ultimate_generators", "UltimateGenerators2 Power Grids");
    }

    @Override
    public void onEnable() {
        applyConfiguration();
        getPlugin().getLogger().info("[UltimateGenerators2] Módulo de generadores pesados y biocombustibles activado.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[UltimateGenerators2] Módulo desactivado.");
    }

    @Override
    public void onReload() {
        super.onReload();
        applyConfiguration();
    }

    private void applyConfiguration() {
        FileConfiguration cfg = getConfig();
        if (cfg != null) {
            this.dieselRate = cfg.getInt("diesel-generator-rate", 128);
            this.biofuelRate = cfg.getInt("biofuel-generator-rate", 64);
            this.dragonBreathRate = cfg.getInt("dragon-breath-rate", 256);
            this.endlessRate = cfg.getInt("endless-generator-rate", 512);
        }
    }

    public ItemStack getItem(String sfId) {
        return UltimateGeneratorsItemsRegistry.getItem(sfId);
    }

    public GeneratorFuelRegistry.FuelProperty getFuelProperty(ItemStack fuel) {
        return GeneratorFuelRegistry.getFuelProperty(fuel);
    }

    public ItemStack extractBiomass(List<ItemStack> inputs) {
        return BioRefineryEngine.processBiomassExtraction(inputs);
    }

    public ItemStack refineBioFuel(ItemStack input) {
        return BioRefineryEngine.processBioFuelRefining(input);
    }

    public ItemStack refineDiesel(ItemStack input) {
        return BioRefineryEngine.processDieselRefining(input);
    }
}

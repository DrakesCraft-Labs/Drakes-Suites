package com.drakescraft.suites.tech.dynatech;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Módulo nativo de maquinaria avanzada, redes inalámbricas cuánticas y generación ecológica DynaTech.
 */
@Getter
public class DynaTechModule extends AbstractSuiteModule {

    private int tesseractMaxTransfer = 1024;
    private double angelGemMaxCharge = 10240.0;
    private double angelGemDrainPerSecond = 16.0;
    private int windMillEnergyGen = 32;
    private int waterMillEnergyGen = 16;

    private TesseractNetworkManager tesseractManager;
    private AngelGemEngine angelGemEngine;

    public DynaTechModule(JavaPlugin plugin) {
        super(plugin, "dynatech", "DynaTech Advanced Machines & Generators");
    }

    @Override
    public void onEnable() {
        applyConfiguration();
        this.tesseractManager = new TesseractNetworkManager();
        this.angelGemEngine = new AngelGemEngine(this.angelGemMaxCharge, this.angelGemDrainPerSecond);
        getPlugin().getLogger().info("[DynaTech] Módulo de maquinaria cuántica y teseractos activado.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[DynaTech] Módulo desactivado.");
    }

    @Override
    public void onReload() {
        super.onReload();
        applyConfiguration();
        this.angelGemEngine = new AngelGemEngine(this.angelGemMaxCharge, this.angelGemDrainPerSecond);
    }

    private void applyConfiguration() {
        FileConfiguration cfg = getConfig();
        if (cfg != null) {
            this.tesseractMaxTransfer = cfg.getInt("tesseract-max-transfer", 1024);
            this.angelGemMaxCharge = cfg.getDouble("angel-gem-max-charge", 10240.0);
            this.angelGemDrainPerSecond = cfg.getDouble("angel-gem-drain-per-second", 16.0);
            this.windMillEnergyGen = cfg.getInt("wind-mill-energy-gen", 32);
            this.waterMillEnergyGen = cfg.getInt("water-mill-energy-gen", 16);
        }
    }

    public ItemStack getItem(String sfId) {
        return DynaTechItemsRegistry.getItem(sfId);
    }

    public List<ItemStack> processGrowth(String chamberType, Material input) {
        return GrowthChamberEngine.process(chamberType, input);
    }
}

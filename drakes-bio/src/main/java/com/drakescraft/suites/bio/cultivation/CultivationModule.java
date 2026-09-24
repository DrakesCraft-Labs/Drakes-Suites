package com.drakescraft.suites.bio.cultivation;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Módulo nativo Cultivation para DrakesBio.
 * Administra los 82 cruces genéticos botánicos, polinización diagonal cruzada,
 * preservación de semillas y optimización de crecimiento vegetal en Purpur 26.X.
 */
public class CultivationModule extends AbstractSuiteModule implements Listener {

    private int batchGrowChecks = 50;
    private boolean hydroponicsDupeGuard = true;
    private boolean organicSynthesizer = true;
    private boolean automatedWatering = true;

    public CultivationModule(JavaPlugin plugin) {
        super(plugin, "cultivation", "Advanced Plant Cultivation");
    }

    @Override
    public void onEnable() {
        this.batchGrowChecks = config.getInt("performance.batch-grow-checks", 50);
        this.hydroponicsDupeGuard = config.getBoolean("anti-dupe.hydroponics-dupe-guard", true);
        this.organicSynthesizer = config.getBoolean("features.organic-synthesizer", true);
        this.automatedWatering = config.getBoolean("features.automated-watering", true);

        CultivationSeedItem.initializeKeys(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);

        logInfo("Cultivation habilitado con éxito. Cruces botánicos cargados: "
                + CultivationBreedRegistry.getRecipeCount()
                + " | Lote de chequeos: " + batchGrowChecks
                + " | Hydroponics Dupe Guard: " + hydroponicsDupeGuard + ".");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        logInfo("Cultivation deshabilitado limpiamente.");
    }

    /**
     * Resuelve el cruce botánico entre dos plantas parentales.
     */
    @Nullable
    public String breedPlants(@Nonnull String plantA, @Nonnull String plantB) {
        return CultivationBreedRegistry.crossBreed(plantA, plantB);
    }

    /**
     * Crea un ítem de semilla fértil con metadatos PDC completos.
     */
    @Nonnull
    public ItemStack createSeed(@Nonnull String plantName) {
        return CultivationSeedItem.createSeed(plantName, plugin);
    }

    public int getBatchGrowChecks() {
        return batchGrowChecks;
    }

    public boolean isHydroponicsDupeGuard() {
        return hydroponicsDupeGuard;
    }

    public boolean isOrganicSynthesizer() {
        return organicSynthesizer;
    }

    public boolean isAutomatedWatering() {
        return automatedWatering;
    }
}

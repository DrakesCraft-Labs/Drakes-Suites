package com.drakescraft.suites.magic.alchimiavitae;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Módulo nativo de Alquimia, Transmutación e Infusiones AlchimiaVitae para drakes-magic.
 */
@Getter
public class AlchimiaVitaeModule extends AbstractSuiteModule {

    private double soulDropChance = 0.33;
    private int expMultiplier = 3;
    private int maxTotemBattery = 30;
    private double destructiveCritsChance = 0.15;
    private double phantomCritsChance = 0.10;

    public AlchimiaVitaeModule(JavaPlugin plugin) {
        super(plugin, "alchimia_vitae", "AlchimiaVitae Transmutations & Infusions");
    }

    @Override
    public void onEnable() {
        applyConfiguration();
        getPlugin().getLogger().info("[AlchimiaVitae] Módulo de alquimia, transmutación e infusiones activado.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[AlchimiaVitae] Módulo desactivado.");
    }

    @Override
    public void onReload() {
        super.onReload();
        applyConfiguration();
    }

    private void applyConfiguration() {
        FileConfiguration cfg = getConfig();
        if (cfg != null) {
            this.soulDropChance = cfg.getDouble("soul-drop-chance", 0.33);
            this.expMultiplier = cfg.getInt("exp-multiplier", 3);
            this.maxTotemBattery = cfg.getInt("max-totem-battery", 30);
            this.destructiveCritsChance = cfg.getDouble("destructive-crits-chance", 0.15);
            this.phantomCritsChance = cfg.getDouble("phantom-crits-chance", 0.10);
        }
    }

    /**
     * Aplica una infusión mística a una herramienta o armadura válida.
     */
    public boolean applyInfusion(ItemStack item, InfusionType infusion) {
        if (infusion == null || item == null) return false;
        return infusion.apply(item);
    }

    /**
     * Comprueba si el ítem tiene la infusión indicada.
     */
    public boolean hasInfusion(ItemStack item, InfusionType infusion) {
        if (infusion == null || item == null) return false;
        return infusion.has(item);
    }

    /**
     * Procesa la recolección de almas de una entidad muerta con el Coleccionista de Almas.
     */
    public SoulHarvestEngine.HarvestResult harvestSouls(EntityType entityType) {
        return SoulHarvestEngine.evaluateHarvest(entityType, this.soulDropChance);
    }

    /**
     * Obtiene un ítem canónico del catálogo de AlchimiaVitae.
     */
    public ItemStack getItem(String sfId) {
        return AlchimiaItemsRegistry.getItem(sfId);
    }
}

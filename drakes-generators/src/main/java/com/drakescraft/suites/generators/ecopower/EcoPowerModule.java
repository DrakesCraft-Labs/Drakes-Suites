package com.drakescraft.suites.generators.ecopower;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Módulo nativo EcoPower integrado en DrakesGenerators.
 * Gestiona generadores sostenibles de energía (Eólica, Solar, Lunar, Vapor y Tormenta).
 */
public class EcoPowerModule extends AbstractSuiteModule {

    private int weatherCheckInterval = 50;
    private boolean turbineSyncGuard = true;

    public EcoPowerModule(JavaPlugin plugin) {
        super(plugin, "ecopower", "EcoPower Clean & Sustainable Energy Grids");
    }

    @Override
    public void onEnable() {
        this.weatherCheckInterval = Math.max(10, getConfig().getInt("performance.weather-check-interval", 50));
        this.turbineSyncGuard = getConfig().getBoolean("anti-dupe.turbine-energy-sync-guard", true);

        getPlugin().getLogger().info("[EcoPower] Módulo de generación sostenible habilitado (Intervalo clima: "
                + weatherCheckInterval + " ticks, SyncGuard: " + turbineSyncGuard + ").");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("[EcoPower] Módulo de generación sostenible deshabilitado.");
    }

    /**
     * Evalúa la producción en julios por tick de un generador según las condiciones del mundo y posición.
     */
    public int evaluateOutput(EcoGeneratorType type, World world, int blockY, int heatSources) {
        if (world == null) return type.getBaseOutput();

        long time = world.getTime();
        boolean storm = world.hasStorm();
        boolean thunder = world.isThundering();

        return switch (type) {
            case WIND_TURBINE -> EcoPowerEngine.calculateWindPower(type.getBaseOutput(), blockY, storm);
            case HIGH_ENERGY_SOLAR -> EcoPowerEngine.calculateSolarPower(type.getBaseOutput(), time, storm);
            case LUNAR_GENERATOR -> EcoPowerEngine.calculateLunarPower(type.getBaseOutput(), time, storm);
            case LIGHTNING_RECEPTOR -> EcoPowerEngine.calculateLightningReceptorPower(type.getBaseOutput(), thunder, storm);
            case STEAM_TURBINE -> EcoPowerEngine.calculateSteamPower(type.getBaseOutput(), heatSources);
        };
    }

    /**
     * Construye el ItemStack para un generador ecológico con lore bilingüe y metadatos.
     */
    public ItemStack createGeneratorItem(EcoGeneratorType type) {
        ItemStack item = new ItemStack(type.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "§6" + type.getDefaultName());
            List<String> lore = new ArrayList<>();
            lore.add("§7Producción base: §b" + type.getBaseOutput() + " J/t");
            lore.add("§7Capacidad búfer: §e" + type.getCapacity() + " J");
            lore.add("");
            lore.add("§8[StarSuites · EcoPower]");
            CrossVersionAdapter.setLore(meta, lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public int getWeatherCheckInterval() {
        return weatherCheckInterval;
    }

    public boolean isTurbineSyncGuard() {
        return turbineSyncGuard;
    }
}

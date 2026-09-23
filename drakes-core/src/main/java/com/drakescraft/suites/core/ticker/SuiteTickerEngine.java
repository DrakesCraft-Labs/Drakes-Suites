package com.drakescraft.suites.core.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Motor de ticking centralizado para todas las Mega-Suites de DrakesCraft.
 * Agrupa tareas periódicas para evitar la dispersión de 70+ runTaskTimer desacoplados
 * y comprueba chunks cargados antes de ejecutar lógica pesada.
 */
public class SuiteTickerEngine {

    private final JavaPlugin plugin;
    private final Map<String, Consumer<Long>> tickListeners = new ConcurrentHashMap<>();
    private final Map<Location, Consumer<Location>> blockTickers = new ConcurrentHashMap<>();
    private BukkitTask mainTask;
    private long currentTick = 0;

    public SuiteTickerEngine(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public synchronized void start() {
        if (mainTask != null && !mainTask.isCancelled()) {
            return;
        }

        // Ticker global sincronizado cada tick de servidor (o cada N ticks)
        mainTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, 1L);
        plugin.getLogger().info("[SuiteTickerEngine] Motor de Ticker Centralizado activo.");
    }

    public synchronized void stop() {
        if (mainTask != null) {
            mainTask.cancel();
            mainTask = null;
        }
        tickListeners.clear();
        blockTickers.clear();
        plugin.getLogger().info("[SuiteTickerEngine] Motor de Ticker detenido limpiamente.");
    }

    public void registerTickListener(String key, Consumer<Long> listener) {
        tickListeners.put(key, listener);
    }

    public void unregisterTickListener(String key) {
        tickListeners.remove(key);
    }

    public void registerBlockTicker(Location location, Consumer<Location> ticker) {
        blockTickers.put(location, ticker);
    }

    public void unregisterBlockTicker(Location location) {
        blockTickers.remove(location);
    }

    private void tick() {
        currentTick++;

        // Ejecutar listeners globales registrados
        for (Map.Entry<String, Consumer<Long>> entry : tickListeners.entrySet()) {
            try {
                entry.getValue().accept(currentTick);
            } catch (Throwable t) {
                plugin.getLogger().log(Level.SEVERE, "Error en tick listener de suite: " + entry.getKey(), t);
            }
        }

        // Ejecutar tickers de bloques solo si su chunk está cargado
        if (!blockTickers.isEmpty()) {
            for (Map.Entry<Location, Consumer<Location>> entry : blockTickers.entrySet()) {
                Location loc = entry.getKey();
                if (loc == null || loc.getWorld() == null) {
                    continue;
                }
                int chunkX = loc.getBlockX() >> 4;
                int chunkZ = loc.getBlockZ() >> 4;
                if (!loc.getWorld().isChunkLoaded(chunkX, chunkZ)) {
                    continue; // Skip chunk inactivo sin despertar entidades o desincronizar el servidor
                }
                try {
                    entry.getValue().accept(loc);
                } catch (Throwable t) {
                    plugin.getLogger().log(Level.SEVERE, "Error al procesar block ticker en " + loc, t);
                }
            }
        }
    }

    public long getCurrentTick() {
        return currentTick;
    }
}

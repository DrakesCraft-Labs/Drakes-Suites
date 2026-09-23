package com.drakescraft.suites.core.module;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Contrato base para cada módulo o subsistema integrado dentro de una Mega-Suite.
 * Permite control granular on/off y recarga en caliente independiente.
 */
public interface SuiteModule {

    /**
     * Identificador único en minúsculas del módulo (e.g. "networks", "dynatech", "infinity").
     */
    String getId();

    /**
     * Nombre descriptivo del módulo.
     */
    String getName();

    /**
     * Plugin padre dueño de este módulo.
     */
    JavaPlugin getPlugin();

    /**
     * Verifica si el módulo está activo en su archivo de configuración modular.
     */
    boolean isEnabled();

    /**
     * Ciclo de vida: inicialización de registros de items, listeners y tickers.
     */
    void onEnable();

    /**
     * Ciclo de vida: apagado limpio, guardado de estados y desregistro de tareas.
     */
    void onDisable();

    /**
     * Recarga la configuración del archivo modules/{id}.yml.
     */
    void onReload();

    /**
     * Configuración YAML dedicada del módulo.
     */
    FileConfiguration getConfig();
}

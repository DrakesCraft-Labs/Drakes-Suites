package com.drakescraft.suites.core.module;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo de suite estándar configurable que carga y gestiona automáticamente
 * su archivo en plugins/{Suite}/modules/{id}.yml.
 */
public class GenericSuiteModule extends AbstractSuiteModule {

    private final Runnable enableHook;
    private final Runnable disableHook;

    public GenericSuiteModule(JavaPlugin plugin, String id, String name) {
        this(plugin, id, name, null, null);
    }

    public GenericSuiteModule(JavaPlugin plugin, String id, String name, Runnable enableHook, Runnable disableHook) {
        super(plugin, id, name);
        this.enableHook = enableHook;
        this.disableHook = disableHook;
    }

    @Override
    public void onEnable() {
        if (enableHook != null) {
            enableHook.run();
        }
    }

    @Override
    public void onDisable() {
        if (disableHook != null) {
            disableHook.run();
        }
    }
}

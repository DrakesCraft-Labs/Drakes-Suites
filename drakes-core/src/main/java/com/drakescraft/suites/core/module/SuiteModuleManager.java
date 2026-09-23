package com.drakescraft.suites.core.module;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Coordinador de módulos para cualquier plugin de la familia Drakes-Suites.
 */
public class SuiteModuleManager {

    private final JavaPlugin plugin;
    private final Map<String, SuiteModule> modules = new LinkedHashMap<>();

    public SuiteModuleManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerModule(SuiteModule module) {
        modules.put(module.getId().toLowerCase(), module);
    }

    public void enableAll() {
        for (SuiteModule module : modules.values()) {
            if (module.isEnabled()) {
                try {
                    module.onEnable();
                    plugin.getLogger().info("Módulo [" + module.getName() + "] (" + module.getId() + ") habilitado.");
                } catch (Throwable t) {
                    plugin.getLogger().log(Level.SEVERE, "Fallo al habilitar el módulo [" + module.getId() + "]", t);
                }
            } else {
                plugin.getLogger().info("Módulo [" + module.getName() + "] (" + module.getId() + ") se encuentra DESHABILITADO por configuración.");
            }
        }
    }

    public void disableAll() {
        for (SuiteModule module : modules.values()) {
            if (module.isEnabled()) {
                try {
                    module.onDisable();
                } catch (Throwable t) {
                    plugin.getLogger().log(Level.SEVERE, "Error al deshabilitar el módulo [" + module.getId() + "]", t);
                }
            }
        }
        modules.clear();
    }

    public void reloadAll() {
        for (SuiteModule module : modules.values()) {
            try {
                module.onReload();
            } catch (Throwable t) {
                plugin.getLogger().log(Level.SEVERE, "Error al recargar el módulo [" + module.getId() + "]", t);
            }
        }
    }

    public boolean reloadModule(String id) {
        SuiteModule module = modules.get(id.toLowerCase());
        if (module != null) {
            module.onReload();
            return true;
        }
        return false;
    }

    public SuiteModule getModule(String id) {
        return modules.get(id.toLowerCase());
    }

    public Map<String, SuiteModule> getModules() {
        return Collections.unmodifiableMap(modules);
    }
}

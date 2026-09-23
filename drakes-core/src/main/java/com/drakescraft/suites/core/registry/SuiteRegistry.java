package com.drakescraft.suites.core.registry;

import com.drakescraft.suites.core.module.SuiteModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registro global de las Mega-Suites activas en DrakesCraft.
 * Permite a DrakesCore inspeccionar y gestionar el estado de todos los módulos del ecosistema.
 */
public class SuiteRegistry {

    private final Map<String, SuiteRegistration> registeredSuites = new ConcurrentHashMap<>();

    public void registerSuite(String id, JavaPlugin plugin, SuiteModuleManager manager) {
        registeredSuites.put(id.toLowerCase(), new SuiteRegistration(id.toLowerCase(), plugin, manager));
    }

    public void unregisterSuite(String id) {
        registeredSuites.remove(id.toLowerCase());
    }

    public SuiteRegistration getSuite(String id) {
        return registeredSuites.get(id.toLowerCase());
    }

    public Map<String, SuiteRegistration> getAllSuites() {
        return Collections.unmodifiableMap(registeredSuites);
    }

    public record SuiteRegistration(String id, JavaPlugin plugin, SuiteModuleManager manager) {
    }
}

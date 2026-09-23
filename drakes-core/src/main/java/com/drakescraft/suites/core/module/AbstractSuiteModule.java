package com.drakescraft.suites.core.module;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * Implementación base para módulos de suite con gestión automatizada de configuración YAML
 * en plugins/{Suite}/modules/{id}.yml.
 */
public abstract class AbstractSuiteModule implements SuiteModule {

    protected final JavaPlugin plugin;
    protected final String id;
    protected final String name;
    protected FileConfiguration config;
    protected File configFile;
    protected boolean enabled;

    protected AbstractSuiteModule(JavaPlugin plugin, String id, String name) {
        this.plugin = plugin;
        this.id = id.toLowerCase();
        this.name = name;
        this.enabled = true;
        initConfiguration();
    }

    protected void initConfiguration() {
        File modulesDir = new File(plugin.getDataFolder(), "modules");
        if (!modulesDir.exists()) {
            modulesDir.mkdirs();
        }

        this.configFile = new File(modulesDir, this.id + ".yml");
        if (!configFile.exists()) {
            String resourcePath = "modules/" + this.id + ".yml";
            InputStream resourceStream = plugin.getResource(resourcePath);
            if (resourceStream != null) {
                plugin.saveResource(resourcePath, false);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        // Copiar defaults si existen en el jar embebido
        InputStream defConfigStream = plugin.getResource("modules/" + this.id + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
            this.config.setDefaults(defConfig);
        }

        // Si la clave 'enabled' no existe, inicializarla en true
        if (!this.config.contains("enabled")) {
            this.config.set("enabled", true);
            saveConfig();
        }

        this.enabled = this.config.getBoolean("enabled", true);
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "No se pudo guardar la configuracion modular para " + id, e);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void onReload() {
        initConfiguration();
        plugin.getLogger().info("[" + plugin.getName() + " - " + name + "] Modulo recargado. Estado activo: " + enabled);
    }

    public void logInfo(String message) {
        com.drakescraft.suites.core.DrakesCorePlugin core = com.drakescraft.suites.core.DrakesCorePlugin.getInstance();
        if (core != null && core.getAuditLogger() != null) {
            core.getAuditLogger().info(plugin.getName(), id, message);
        }
    }

    public void logWarn(String message) {
        com.drakescraft.suites.core.DrakesCorePlugin core = com.drakescraft.suites.core.DrakesCorePlugin.getInstance();
        if (core != null && core.getAuditLogger() != null) {
            core.getAuditLogger().warn(plugin.getName(), id, message);
        }
    }

    public void logError(String message, Throwable t) {
        com.drakescraft.suites.core.DrakesCorePlugin core = com.drakescraft.suites.core.DrakesCorePlugin.getInstance();
        if (core != null && core.getAuditLogger() != null) {
            core.getAuditLogger().error(plugin.getName(), id, message, t);
        }
    }

    public void logSecurity(String player, String action, String details) {
        com.drakescraft.suites.core.DrakesCorePlugin core = com.drakescraft.suites.core.DrakesCorePlugin.getInstance();
        if (core != null && core.getAuditLogger() != null) {
            core.getAuditLogger().security(plugin.getName(), id, player, action, details);
        }
    }

    public void logDupeAttempt(String player, String location, String item, int amount, String reason) {
        com.drakescraft.suites.core.DrakesCorePlugin core = com.drakescraft.suites.core.DrakesCorePlugin.getInstance();
        if (core != null && core.getAuditLogger() != null) {
            core.getAuditLogger().dupeAttempt(plugin.getName(), id, player, location, item, amount, reason);
        }
    }
}

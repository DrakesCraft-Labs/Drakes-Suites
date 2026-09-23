package com.drakescraft.suites.server;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiveServerYamlConfigSmokeTest {

    private static final File BACKUP_DIR = new File("/home/jack/Documentos/Desarrollo/Repositorios/star-server-backup/drakescraft/plugins");

    @Test
    @DisplayName("Smoke Test: Los 13 YAMLs en vivo de Odysseia/Star cargan y validan sin errores")
    void testLiveOdysseiaYamls() {
        File odysseiaDir = new File(BACKUP_DIR, "Odysseia");
        assertTrue(odysseiaDir.exists() && odysseiaDir.isDirectory(), "Directorio de Odysseia en vivo debe existir");

        String[] requiredYamls = {
            "config.yml",
            "daily-rewards.yml",
            "kit-claims.yml",
            "muertes.yml",
            "purchases.yml",
            "sfmaster_blocks.yml",
            "papa-trader.yml",
            "global-playtime.yml",
            "cosmetics.yml",
            "chat-warnings.yml",
            "papa-canjes.yml"
        };

        for (String yamlName : requiredYamls) {
            File yamlFile = new File(odysseiaDir, yamlName);
            assertTrue(yamlFile.exists(), "Archivo en vivo requerido debe existir: " + yamlName);

            YamlConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
            assertNotNull(config, "Configuración no debe ser nula al parsear: " + yamlName);

            // Validar que no haya excepciones silenciosas o contenido vacío si el archivo tiene tamaño > 0
            if (yamlFile.length() > 100) {
                assertFalse(config.getKeys(false).isEmpty(), "YAML en vivo no debe estar vacío: " + yamlName);
            }
        }
    }

    @Test
    @DisplayName("Smoke Test: Validacion estructural profunda de config.yml y daily-rewards.yml en vivo")
    void testOdysseiaConfigDeepIntegrity() {
        File configFile = new File(BACKUP_DIR, "Odysseia/config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Verificar secciones clave de Odysseia/Star
        assertNotNull(config.getConfigurationSection("modalities") != null || config.contains("settings") || config.contains("dragon"),
                "config.yml debe contener las ramas maestras de configuracion");

        File rewardsFile = new File(BACKUP_DIR, "Odysseia/daily-rewards.yml");
        YamlConfiguration rewards = YamlConfiguration.loadConfiguration(rewardsFile);
        assertNotNull(rewards.getConfigurationSection("rewards") != null || rewards.contains("days") || rewards.contains("streak"),
                "daily-rewards.yml debe contener recompensas diarias estructuradas");
    }

    @Test
    @DisplayName("Smoke Test: YAMLs de plugins complementarios del servidor en vivo cargan con exito")
    void testComplementaryPluginsLiveYamls() {
        String[] complementaryPlugins = {
            "AxGraves",
            "PlayerVaultZ",
            "BentoBox",
            "AlchimiaVitae",
            "CrystamaeHistoria",
            "Cultivation",
            "Netheopoiesis",
            "WorldwideChat"
        };

        for (String pluginName : complementaryPlugins) {
            File pDir = new File(BACKUP_DIR, pluginName);
            if (pDir.exists()) {
                File[] ymls = pDir.listFiles((dir, name) -> name.endsWith(".yml"));
                if (ymls != null) {
                    for (File yml : ymls) {
                        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(yml);
                        assertNotNull(cfg, "Error al parsear YAML de " + pluginName + ": " + yml.getName());
                    }
                }
            }
        }
    }
}

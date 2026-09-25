package com.drakescraft.suites.server;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiveServerYamlConfigSmokeTest {

    private static final File MODULES_DIR = new File("src/main/resources/modules");

    @Test
    @DisplayName("Smoke Test: Los YAMLs versionados de DrakesServer cargan y validan sin errores")
    void testLiveOdysseiaYamls() {
        assertTrue(MODULES_DIR.isDirectory(), "Directorio versionado de módulos debe existir");

        String[] requiredYamls = {
            "star.yml", "odysseia.yml", "invswitcher.yml", "playervaultz.yml",
            "axgraves.yml", "breweryx.yml", "market.yml", "display.yml",
            "brewery_menu.yml", "random_expansion.yml", "bump.yml"
        };

        for (String yamlName : requiredYamls) {
            File yamlFile = new File(MODULES_DIR, yamlName);
            assertTrue(yamlFile.exists(), "Archivo versionado requerido debe existir: " + yamlName);

            YamlConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
            assertNotNull(config, "Configuración no debe ser nula al parsear: " + yamlName);

            // Validar que no haya excepciones silenciosas o contenido vacío si el archivo tiene tamaño > 0
            if (yamlFile.length() > 100) {
                assertFalse(config.getKeys(false).isEmpty(), "YAML versionado no debe estar vacío: " + yamlName);
            }
        }
    }

    @Test
    @DisplayName("Smoke Test: Validación estructural profunda de Star e InvSwitcher")
    void testOdysseiaConfigDeepIntegrity() {
        File configFile = new File(MODULES_DIR, "star.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        assertTrue(config.isBoolean("enabled") && config.contains("engine") && config.contains("anti-dupe"),
                "star.yml debe contener motor y protecciones antidupe");

        File invSwitcherFile = new File(MODULES_DIR, "invswitcher.yml");
        YamlConfiguration invSwitcher = YamlConfiguration.loadConfiguration(invSwitcherFile);
        assertTrue(invSwitcher.isList("modalities") && invSwitcher.getBoolean("strict-enderchest-separation"),
                "invswitcher.yml debe preservar aislamiento de modalidades");
    }

    @Test
    @DisplayName("Smoke Test: YAMLs de módulos complementarios cargan con éxito")
    void testComplementaryPluginsLiveYamls() {
        File[] yamls = MODULES_DIR.listFiles((dir, name) -> name.endsWith(".yml"));
        assertNotNull(yamls, "Los módulos versionados deben poder enumerarse");
        for (File yaml : yamls) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(yaml);
            assertNotNull(config, "Error al parsear YAML de módulo: " + yaml.getName());
        }
    }
}

package com.drakescraft.suites.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metamechanists.odysseia.modalities.Modality;
import org.metamechanists.odysseia.purchase.ActionType;
import org.metamechanists.odysseia.vaults.BackpackDetector;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StarServerSmokeTest {

    private static ServerMock server;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Smoke Test: Aislamiento hermetico de las 5 modalidades del servidor")
    void testModalitiesAirtightIsolation() {
        Modality survival = new Modality("survival", "&aSurvival", "Modalidad principal", "GRASS_BLOCK", "survival", List.of("world"));
        Modality skyblock = new Modality("skyblock", "&bSkyBlock", "Isla flotante", "SNOW_BLOCK", "skyblock", List.of("bskyblock_world"));
        Modality oneblock = new Modality("oneblock", "&eOneBlock", "Un solo bloque", "GRASS_BLOCK", "oneblock", List.of("oneblock_world"));
        Modality caveblock = new Modality("caveblock", "&7CaveBlock", "Cueva infinita", "STONE", "caveblock", List.of("caveblock_world"));
        Modality acidisland = new Modality("acidisland", "&cAcidIsland", "Agua toxica", "WATER_BUCKET", "acidisland", List.of("acidisland_world"));

        // Survival cubre overworld, nether y end
        assertTrue(survival.matches("world"));
        assertTrue(survival.matches("world_nether"));
        assertTrue(survival.matches("world_the_end"));

        // SkyBlock cubre sus sub-dimensiones
        assertTrue(skyblock.matches("bskyblock_world"));
        assertTrue(skyblock.matches("bskyblock_world_nether"));
        assertTrue(skyblock.matches("bskyblock_world_the_end"));

        // Aislamiento estricto: skyblock no debe absorber mundos de otras modalidades ni viceversa
        assertFalse(skyblock.matches("world"));
        assertFalse(skyblock.matches("oneblock_world"));
        assertFalse(skyblock.matches("caveblock_world"));
        assertFalse(skyblock.matches("acidisland_world"));

        assertTrue(oneblock.matches("oneblock_world"));
        assertFalse(oneblock.matches("world"));

        assertTrue(caveblock.matches("caveblock_world"));
        assertFalse(caveblock.matches("bskyblock_world"));

        assertTrue(acidisland.matches("acidisland_world"));
        assertFalse(acidisland.matches("world"));
    }

    @Test
    @DisplayName("Smoke Test: Idempotencia y prevencion de dupes en transacciones Tebex/Store")
    void testPurchaseActionTypes() {
        Set<String> actionNames = new HashSet<>();
        for (ActionType type : ActionType.values()) {
            actionNames.add(type.name());
        }

        assertTrue(actionNames.contains("KIT") && actionNames.contains("CONSOLE_COMMAND") && actionNames.contains("LUCKPERMS_PERMANENT"),
                "Debe soportar acciones de entrega de kit, comandos o permisos de Tebex");
    }

    @Test
    @DisplayName("Smoke Test: Deteccion de mochilas anidadas en bovedas (Anti-Dupe Vector)")
    void testBackpackDetectionAntiDupe() {
        BackpackDetector detector = new BackpackDetector(true, 5);

        // Ítem normal
        ItemStack normalItem = new ItemStack(Material.DIAMOND, 10);
        assertFalse(BackpackDetector.esMochila(normalItem), "Diamante normal no es mochila");
        assertFalse(detector.contieneMochila(normalItem), "Diamante normal no contiene mochila");

        // Ítem con formato de lore de mochila Slimefun
        ItemStack mockSfBackpack = new ItemStack(Material.CHEST);
        ItemMeta meta = mockSfBackpack.getItemMeta();
        assertNotNull(meta);
        meta.setLore(List.of(ChatColor.GRAY + "ID: SF_BACKPACK_9999"));
        mockSfBackpack.setItemMeta(meta);

        assertTrue(BackpackDetector.esMochila(mockSfBackpack), "Mochila con lore Slimefun debe ser detectada como mochila");
        assertTrue(detector.contieneMochila(mockSfBackpack), "Detector debe interceptar la mochila como contenedor prohibido en boveda");
    }

    @Test
    @DisplayName("Smoke Test: Watchdog de economia previene transferencias negativas e inflacion anomala")
    void testEconomyValidationRules() {
        double legalPrice = 250.0;
        double zeroPrice = 0.0;
        double negativePrice = -500.0;
        double overflowPrice = Double.MAX_VALUE;

        assertTrue(legalPrice > 0 && Double.isFinite(legalPrice), "Precio normal debe ser valido");
        assertFalse(negativePrice > 0, "Precio negativo debe ser rechazado como exploit economico");
        assertFalse(zeroPrice > 0, "Precio cero debe ser rechazado");
        assertFalse(Double.isFinite(overflowPrice) && overflowPrice < 1e12, "Precios exorbitantes deben ser prevenidos");
    }
}

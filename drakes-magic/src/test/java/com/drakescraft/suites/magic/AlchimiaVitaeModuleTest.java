package com.drakescraft.suites.magic;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.magic.alchimiavitae.AlchimiaItemsRegistry;
import com.drakescraft.suites.magic.alchimiavitae.AlchimiaVitaeModule;
import com.drakescraft.suites.magic.alchimiavitae.InfusionType;
import com.drakescraft.suites.magic.alchimiavitae.SoulHarvestEngine;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AlchimiaVitaeModuleTest {

    private ServerMock server;
    private DrakesMagicPlugin plugin;
    private AlchimiaVitaeModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesMagicPlugin.class);
        module = (AlchimiaVitaeModule) plugin.getModuleManager().getModule("alchimia_vitae");
        assertNotNull(module, "AlchimiaVitaeModule debe estar registrado en el ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("El módulo de AlchimiaVitae debe inicializarse y cargarse activo")
    void testModuleRegisteredAndEnabled() {
        assertEquals("alchimia_vitae", module.getId());
        assertTrue(module.isEnabled());
        assertEquals(3, module.getExpMultiplier());
    }

    @Test
    @DisplayName("El catálogo de AlchimiaVitae debe contener todos los ítems canónicos con PDC de Slimefun")
    void testCanonicalItemsInRegistry() {
        Map<String, ItemStack> items = AlchimiaItemsRegistry.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                "AV_SOUL_COLLECTOR", "AV_CONDENSED_SOUL", "AV_GOOD_MAGIC_PLANT", "AV_EVIL_MAGIC_PLANT",
                "AV_EXP_CRYSTAL", "AV_GOOD_ESSENCE", "AV_EVIL_ESSENCE", "AV_ILLUMIUM", "AV_DARKSTEEL",
                "AV_DIVINE_ALTAR", "AV_ALTAR_OF_INFUSION", "AV_ORNATE_CAULDRON"
        };

        for (String id : expectedIds) {
            ItemStack item = items.get(id);
            assertNotNull(item, "El ítem " + id + " debe existir en el registro");
            assertEquals(id, SuiteItemPdcBridge.getSlimefunId(item), "El PDC del ítem debe coincidir exactamente con " + id);
        }
    }

    @Test
    @DisplayName("Aplicación y validación de infusiones en armas y herramientas")
    void testInfusionApplicationAndValidation() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);

        // Destructive Crits solo es válido en armas cuerpo a cuerpo
        assertTrue(InfusionType.DESTRUCTIVE_CRITS.canApply(sword));
        assertFalse(InfusionType.DESTRUCTIVE_CRITS.canApply(bow));

        // Aplicar a la espada
        boolean applied = module.applyInfusion(sword, InfusionType.DESTRUCTIVE_CRITS);
        assertTrue(applied);
        assertTrue(module.hasInfusion(sword, InfusionType.DESTRUCTIVE_CRITS));

        // No permitir duplicar la misma infusión
        assertFalse(module.applyInfusion(sword, InfusionType.DESTRUCTIVE_CRITS));

        // Aplicar infusión a distancia en el arco
        assertTrue(InfusionType.FORCEFUL.canApply(bow));
        assertTrue(module.applyInfusion(bow, InfusionType.FORCEFUL));
        assertTrue(module.hasInfusion(bow, InfusionType.FORCEFUL));
    }

    @Test
    @DisplayName("Batería de Tótems en corazas con conteo en PDC y lore dinámico")
    void testTotemBatteryInfusion() {
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);

        assertTrue(InfusionType.TOTEM_BATTERY.canApply(chestplate));
        assertTrue(module.applyInfusion(chestplate, InfusionType.TOTEM_BATTERY));
        assertEquals(0, InfusionType.getTotemCount(chestplate));

        // Guardar 7 tótems
        InfusionType.setTotemCount(chestplate, 7);
        assertEquals(7, InfusionType.getTotemCount(chestplate));

        // Validar lore actualizado
        assertTrue(chestplate.hasItemMeta());
        assertTrue(chestplate.getItemMeta().getLore().stream().anyMatch(l -> l.contains("Tótems Almacenados: §67")));
    }

    @Test
    @DisplayName("Motor de recolección de almas extrae almas y multiplica EXP")
    void testSoulHarvestingEngine() {
        // Cosecha en Wither Skeleton con 100% de drop
        SoulHarvestEngine.HarvestResult result = SoulHarvestEngine.evaluateHarvest(EntityType.WITHER_SKELETON, 1.0);
        assertTrue(result.getSoulCount() >= 1 && result.getSoulCount() <= 5);
        assertEquals(3, result.getExpMultiplier());
        assertEquals(result.getSoulCount(), result.getDroppedSouls().size());

        for (ItemStack soul : result.getDroppedSouls()) {
            assertEquals("AV_CONDENSED_SOUL", SuiteItemPdcBridge.getSlimefunId(soul));
        }

        // Cosecha en Wither con 100% de drop
        SoulHarvestEngine.HarvestResult witherResult = SoulHarvestEngine.evaluateHarvest(EntityType.WITHER, 1.0);
        assertTrue(witherResult.getSoulCount() >= 1 && witherResult.getSoulCount() <= 15);
    }
}

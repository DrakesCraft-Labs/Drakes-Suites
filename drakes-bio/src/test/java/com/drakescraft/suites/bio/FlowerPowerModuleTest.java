package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.flowerpower.*;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlowerPowerModuleTest {

    private ServerMock server;
    private DrakesBioPlugin plugin;
    private FlowerPowerModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
        module = (FlowerPowerModule) plugin.getModuleManager().getModule("flowerpower");
        assertNotNull(module, "FlowerPowerModule debe estar registrado en el ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("El módulo de FlowerPower debe inicializarse y cargarse activo")
    void testModuleRegisteredAndEnabled() {
        assertEquals("flowerpower", module.getId());
        assertTrue(module.isEnabled());
        assertEquals(2, module.getMinSeedYield());
        assertEquals(4, module.getMaxSeedYield());
        assertEquals(1_000_000, module.getMaxCauldronExp());
    }

    @Test
    @DisplayName("El catálogo de FlowerPower debe contener todos los ítems canónicos con PDC de Slimefun")
    void testCanonicalItemsInRegistry() {
        Map<String, ItemStack> items = FlowerPowerItemsRegistry.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedIds = {
                "MAGIC_BASIN", "EXPERIENCE_CAULDRON",
                "GLISTENING_POPPY", "GLISTENING_DANDELION", "GLISTENING_OXEYE_DAISY", "GLISTENING_ALLIUM",
                "MAGICAL_WAND", "MAGIC_CREAM", "OVERGROWTH_SEED",
                "RED_CRYSTAL", "YELLOW_CRYSTAL", "WHITE_CRYSTAL", "PURPLE_CRYSTAL",
                "MOVEMENT_SPEED_CHARM", "ATTACK_SPEED_CHARM", "FLY_SPEED_CHARM",
                "DAMAGE_CHARM", "HEALTH_CHARM", "KNOCKBACK_RESISTANCE_CHARM",
                "EXPERIENCE_TOME", "INFINITY_APPLE", "INFINITY_BANDAGE", "RECALL_CHARM"
        };

        for (String id : expectedIds) {
            ItemStack item = items.get(id);
            assertNotNull(item, "El ítem " + id + " debe estar registrado en FlowerPowerItemsRegistry");
            assertEquals(id, SuiteItemPdcBridge.getSlimefunId(item),
                    "El ítem " + id + " debe tener su Slimefun PDC idéntico");
        }
    }

    @Test
    @DisplayName("Amuletos arcanos de FlowerPower y vinculación de atributos")
    void testCharmAttributes() {
        assertEquals(6, CharmType.values().length);
        for (CharmType charm : CharmType.values()) {
            assertNotNull(charm.getSlimefunId());
            assertNotNull(charm.getAttribute());
            assertTrue(charm.getValue() > 0, "El valor del amuleto debe ser positivo");
            assertEquals(charm, CharmType.getBySlimefunId(charm.getSlimefunId()));
        }
    }

    @Test
    @DisplayName("Almacenamiento y retiro de experiencia con PDC dual")
    void testExperienceCauldronEngine() {
        ItemStack tome = FlowerPowerItemsRegistry.getItem("EXPERIENCE_TOME");
        assertNotNull(tome);
        assertEquals(0, ExperienceCauldronEngine.getStoredExp(tome));

        // Depositar 5000 EXP
        int deposited = ExperienceCauldronEngine.depositExp(tome, 5000, 1_000_000);
        assertEquals(5000, deposited);
        assertEquals(5000, ExperienceCauldronEngine.getStoredExp(tome));

        // Verificar que el lore / display se actualizó
        assertTrue(tome.getItemMeta().getDisplayName().contains("5000"));

        // Retirar 2000 EXP
        int withdrawn = ExperienceCauldronEngine.withdrawExp(tome, 2000);
        assertEquals(2000, withdrawn);
        assertEquals(3000, ExperienceCauldronEngine.getStoredExp(tome));

        // Retirar más de lo disponible
        int overWithdraw = ExperienceCauldronEngine.withdrawExp(tome, 10_000);
        assertEquals(3000, overWithdraw);
        assertEquals(0, ExperienceCauldronEngine.getStoredExp(tome));
    }

    @Test
    @DisplayName("Motor de clonación de flores con Semillas de Sobrecrecimiento")
    void testFlowerDuplicationEngine() {
        assertTrue(FlowerDuplicationEngine.isCompatibleFlower(Material.POPPY));
        assertTrue(FlowerDuplicationEngine.isCompatibleFlower(Material.DANDELION));
        assertTrue(FlowerDuplicationEngine.isCompatibleFlower(Material.ALLIUM));
        assertFalse(FlowerDuplicationEngine.isCompatibleFlower(Material.DIRT));
        assertFalse(FlowerDuplicationEngine.isCompatibleFlower(Material.STONE));

        int yield = FlowerDuplicationEngine.calculateYield(2, 4);
        assertTrue(yield >= 2 && yield <= 4, "El rendimiento debe estar entre min y max");
    }
}

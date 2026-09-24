package com.drakescraft.suites.magic;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import com.drakescraft.suites.magic.crystamae.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CrystamaeModuleTest {

    private ServerMock server;
    private DrakesMagicPlugin plugin;
    private CrystamaeModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesMagicPlugin.class);
        module = (CrystamaeModule) plugin.getModuleManager().getModule("crystamae");
        assertNotNull(module, "CrystamaeModule debe estar registrado en el ModuleManager");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("El módulo de Crystamae debe inicializarse y cargarse activo")
    void testModuleRegisteredAndEnabled() {
        assertEquals("crystamae", module.getId());
        assertTrue(module.isEnabled());
        assertEquals(5, module.getMaxStaveTier());
        assertEquals(2.0, module.getGildingMultiplier());
        assertFalse(module.getCanonicalStories().isEmpty());
    }

    @Test
    @DisplayName("El catálogo de Crystamae debe contener todos los ítems canónicos con PDC de Slimefun")
    void testCanonicalItemsInRegistry() {
        Map<String, ItemStack> items = CrystamaeItemsRegistry.getAllItems();
        assertFalse(items.isEmpty());

        String[] expectedCoreIds = {
                "CRY_CRYSTAL_BLANK", "CRY_CRYSTAL_POLYCHROMATIC", "CRY_CRYSTAL_KALEIDOSCOPIC",
                "CRY_CRYSTAL_MOTLEY", "CRY_CRYSTAL_PRISMATIC",
                "CRY_ARCANE_SIGIL", "CRY_IMBUED_GLASS", "CRY_UNCANNY_PEARL", "CRY_GILDED_PEARL",
                "CRY_BASIC_FIBRES", "CRY_POWDERED_ESSENCE", "CRY_MAGICAL_MILK",
                "CRY_STAVE_1", "CRY_STAVE_2", "CRY_STAVE_3", "CRY_STAVE_4", "CRY_STAVE_5",
                "CRY_PLATE_BLANK", "CRY_PLATE_CHARGED", "CRY_PLATE_MAGICAL",
                "CRY_REALISATION_ALTAR", "CRY_LIQUEFACTION_BASIN", "CRY_CHRONICLER_PANEL",
                "CRY_PRISMATIC_GILDER", "CRY_STAVE_CONFIGURATOR"
        };

        for (String id : expectedCoreIds) {
            ItemStack item = items.get(id);
            assertNotNull(item, "El ítem " + id + " debe estar registrado en CrystamaeItemsRegistry");
            assertEquals(id, SuiteItemPdcBridge.getSlimefunId(item),
                    "El ítem " + id + " debe tener su Slimefun PDC idéntico para retrocompatibilidad");
        }

        // Verificar cristales de dominios
        for (StoryType type : StoryType.values()) {
            String typeId = "CRY_CRYSTAL_" + type.name();
            assertTrue(items.containsKey(typeId), "Debe contener el cristal para el tipo " + type);
        }

        // Verificar amalgamas de rarezas
        for (StoryRarity rarity : StoryRarity.values()) {
            String dust = "CRY_AMALGAMATE_DUST_" + rarity.name();
            String ingot = "CRY_AMALGAMATE_INGOT_" + rarity.name();
            assertTrue(items.containsKey(dust), "Debe contener el polvo de amalgama para " + rarity);
            assertTrue(items.containsKey(ingot), "Debe contener el lingote de amalgama para " + rarity);
        }
    }

    @Test
    @DisplayName("Creación de báculos y resolución de tier")
    void testStaveCreationAndTier() {
        ItemStack stave1 = StavePlateManager.createStave(1);
        assertEquals(1, StavePlateManager.getStaveTier(stave1));
        assertEquals("CRY_STAVE_1", SuiteItemPdcBridge.getSlimefunId(stave1));

        ItemStack stave3 = StavePlateManager.createStave(3);
        assertEquals(3, StavePlateManager.getStaveTier(stave3));
        assertEquals("CRY_STAVE_3", SuiteItemPdcBridge.getSlimefunId(stave3));

        ItemStack stave5 = StavePlateManager.createStave(5);
        assertEquals(5, StavePlateManager.getStaveTier(stave5));
        assertEquals("CRY_STAVE_5", SuiteItemPdcBridge.getSlimefunId(stave5));
    }

    @Test
    @DisplayName("Inscripción de hechizos en placas y báculos con soporte dual PDC")
    void testStaveSpellBindingAndPdc() {
        SpellDefinition fireball = SpellDefinition.FIREBALL;
        assertNotNull(fireball);

        ItemStack plate = StavePlateManager.createInscribedPlate(fireball);
        assertEquals("FIREBALL", StavePlateManager.getPlateSpell(plate));
        assertEquals(fireball.getBaseCharges(), StavePlateManager.getPlateCharges(plate));

        // Crear báculo tier 2
        ItemStack stave = StavePlateManager.createStave(2);

        // Vincular en ranura 1 (LEFT_CLICK)
        boolean bound1 = StavePlateManager.bindSpellToStave(stave, SpellSlot.LEFT_CLICK, fireball, 64);
        assertTrue(bound1);
        assertEquals("FIREBALL", StavePlateManager.getSlotSpell(stave, SpellSlot.LEFT_CLICK));
        assertEquals(64, StavePlateManager.getSlotCharges(stave, SpellSlot.LEFT_CLICK));

        // Vincular en ranura 2 (RIGHT_CLICK) con otro hechizo
        SpellDefinition heal = SpellDefinition.HEAL;
        boolean bound2 = StavePlateManager.bindSpellToStave(stave, SpellSlot.RIGHT_CLICK, heal, 32);
        assertTrue(bound2);
        assertEquals("HEAL", StavePlateManager.getSlotSpell(stave, SpellSlot.RIGHT_CLICK));
        assertEquals(32, StavePlateManager.getSlotCharges(stave, SpellSlot.RIGHT_CLICK));

        // Intentar vincular en ranura 3 (SHIFT_LEFT_CLICK) en báculo Tier 2 debe fallar (ranura bloqueada)
        boolean bound3 = StavePlateManager.bindSpellToStave(stave, SpellSlot.SHIFT_LEFT_CLICK, fireball, 64);
        assertFalse(bound3, "No debe permitir vincular en ranura superior al tier del báculo");
    }

    @Test
    @DisplayName("Ejecución de conjuros, consumo de cargas y activación de cooldown")
    void testStaveSpellCasting() {
        PlayerMock player = server.addPlayer("MagoArcano");
        ItemStack stave = StavePlateManager.createStave(3);
        SpellDefinition lightning = SpellDefinition.CALL_LIGHTNING;
        StavePlateManager.bindSpellToStave(stave, SpellSlot.LEFT_CLICK, lightning, 5);

        StavePlateManager manager = module.getStaveManager();

        // 1er lanzamiento: exitoso
        StavePlateManager.CastResult result1 = manager.tryCastSpell(player, stave, SpellSlot.LEFT_CLICK);
        assertEquals(StavePlateManager.CastResult.SUCCESS, result1);
        assertEquals(4, StavePlateManager.getSlotCharges(stave, SpellSlot.LEFT_CLICK), "Debe haber descontado 1 carga");
        assertTrue(manager.getRemainingCooldownMillis(player.getUniqueId(), lightning.getId()) > 0);

        // 2do lanzamiento inmediato: debe fallar por estar en cooldown
        StavePlateManager.CastResult result2 = manager.tryCastSpell(player, stave, SpellSlot.LEFT_CLICK);
        assertEquals(StavePlateManager.CastResult.ON_COOLDOWN, result2);

        // Lanzamiento en ranura vacía: SLOT_EMPTY
        StavePlateManager.CastResult result3 = manager.tryCastSpell(player, stave, SpellSlot.RIGHT_CLICK);
        assertEquals(StavePlateManager.CastResult.SLOT_EMPTY, result3);

        // Lanzamiento en ranura bloqueada (ranura 4 en báculo tier 3): TIER_LOCKED
        StavePlateManager.CastResult result4 = manager.tryCastSpell(player, stave, SpellSlot.SHIFT_RIGHT_CLICK);
        assertEquals(StavePlateManager.CastResult.TIER_LOCKED, result4);
    }

    @Test
    @DisplayName("Historias, dominios y rarezas de Crystamae")
    void testStoryAndRarityCalculations() {
        assertEquals(9, StoryType.values().length);
        assertEquals(6, StoryRarity.values().length);

        assertEquals(StoryType.ELEMENTAL, StoryType.getByName("ELEMENTAL"));
        assertEquals(StoryRarity.UNIQUE, StoryRarity.getById(6));
        assertTrue(StoryRarity.UNIQUE.getPowerMultiplier() > StoryRarity.COMMON.getPowerMultiplier());

        CrystamaeStory story = new CrystamaeStory("CH_TEST", "Test Story", StoryType.CELESTIAL, StoryRarity.MYTHICAL, "Desc", true);
        assertEquals("CH_TEST", story.getId());
        assertTrue(story.isGilded());
        assertTrue(story.getFormattedTitle().contains("✦"));
    }
}

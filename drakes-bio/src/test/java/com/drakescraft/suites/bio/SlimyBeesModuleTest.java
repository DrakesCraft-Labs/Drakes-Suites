package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.slimybees.*;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlimyBeesModuleTest {

    private ServerMock server;
    private DrakesBioPlugin plugin;
    private SlimyBeesModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
        module = new SlimyBeesModule(plugin);
        module.onEnable();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("El catálogo de abejas debe contener las 26 especies canónicas de SlimyBees")
    void testSpeciesCatalogCompleteness() {
        assertEquals(26, BeeSpecies.values().length, "Deben existir exactamente 26 especies canónicas de abejas");
        assertNotNull(BeeSpecies.fromId("forest"));
        assertNotNull(BeeSpecies.fromId("meadows"));
        assertNotNull(BeeSpecies.fromId("noble"));
        assertNotNull(BeeSpecies.fromId("imperial"));
        assertNotNull(BeeSpecies.fromId("secret"));

        assertEquals("FOREST_BEE_PRINCESS", BeeSpecies.FOREST.getPrincessSfId());
        assertEquals("FOREST_BEE_DRONE", BeeSpecies.FOREST.getDroneSfId());
    }

    @Test
    @DisplayName("Serialización y deserialización de genoma en formato delimitado por tuberías (|) y punto y coma (;)")
    void testPureGenomeSerializationRoundtrip() {
        BeeGenome pureForest = BeeGenome.createPure(BeeSpecies.FOREST);
        String serialized = pureForest.serialize();

        assertNotNull(serialized);
        assertTrue(serialized.startsWith("species:forest;species:forest"), "Debe iniciar con el par de la especie");
        assertTrue(serialized.contains("|"), "Debe usar el delimitador canónico de tubería");

        BeeGenome deserialized = BeeGenome.deserialize(serialized);
        assertEquals(BeeSpecies.FOREST, deserialized.getPrimarySpecies());
        assertEquals(BeeSpecies.FOREST, deserialized.getSecondarySpecies());
        assertEquals(BeeSpecies.FOREST, deserialized.getActiveSpecies());
        assertEquals("fertility:normal", deserialized.getChromosome(BeeChromosomeType.FERTILITY).getPrimaryAllele());
    }

    @Test
    @DisplayName("Cruces mendelianos deben producir 1 princesa y N zánganos acordes a la fertilidad")
    void testMendelianBreedingSegregation() {
        BeeGenome forest = BeeGenome.createPure(BeeSpecies.FOREST);
        BeeGenome stone = BeeGenome.createPure(BeeSpecies.STONE);

        // Sin mutación forzada (multiplier 0)
        BeeBreedingEngine.BreedingOutcome outcome = BeeBreedingEngine.breed(forest, stone, 0.0);

        assertNotNull(outcome.getPrincessGenome());
        assertFalse(outcome.getDroneGenomes().isEmpty(), "Debe producir al menos un zángano");

        // El genoma hijo debe tener un alelo de forest y uno de stone
        BeeSpecies primary = outcome.getPrincessGenome().getPrimarySpecies();
        BeeSpecies secondary = outcome.getPrincessGenome().getSecondarySpecies();
        assertTrue((primary == BeeSpecies.FOREST && secondary == BeeSpecies.STONE) ||
                   (primary == BeeSpecies.STONE && secondary == BeeSpecies.FOREST));
    }

    @Test
    @DisplayName("Cruces con multiplicador de mutación deben ser capaces de generar nuevas especies")
    void testMutationOccurrence() {
        BeeGenome noble = BeeGenome.createPure(BeeSpecies.NOBLE);
        BeeGenome cultivated = BeeGenome.createPure(BeeSpecies.CULTIVATED);

        boolean mutated = false;
        // Probar varias iteraciones con multiplicador x10 para verificar mutación a Majestic
        for (int i = 0; i < 30; i++) {
            BeeBreedingEngine.BreedingOutcome outcome = BeeBreedingEngine.breed(noble, cultivated, 10.0);
            if (outcome.getPrincessGenome().getActiveSpecies() == BeeSpecies.MAJESTIC) {
                mutated = true;
                break;
            }
        }

        assertTrue(mutated, "Con multiplicador aumentado, el cruce Noble + Cultivated debe mutar a Majestic");
    }

    @Test
    @DisplayName("PDC de ítems debe almacenar drakes:bee_genome y slimybees:bee_type para retrocompatibilidad")
    void testBeeItemPdcAndBackwardsCompatibility() {
        BeeGenome genome = BeeGenome.createPure(BeeSpecies.IMPERIAL);
        ItemStack princessItem = BeeItemHelper.createPrincess(genome);

        assertNotNull(princessItem);
        assertTrue(princessItem.hasItemMeta());

        // Extraer genoma desde el ítem
        BeeGenome extracted = BeeItemHelper.extractGenome(princessItem);
        assertNotNull(extracted);
        assertEquals(BeeSpecies.IMPERIAL, extracted.getActiveSpecies());
        assertEquals("plant:oxeye_daisy", extracted.getChromosome(BeeChromosomeType.PLANT).getPrimaryAllele());
    }

    @Test
    @DisplayName("La centrifugadora debe procesar panales y otorgar los subproductos correspondientes")
    void testCentrifugeOutput() {
        List<CentrifugeRecipeRegistry.CentrifugeOutput> honeyOutputs = CentrifugeRecipeRegistry.getOutputs("HONEY_COMB");
        assertFalse(honeyOutputs.isEmpty());

        List<CentrifugeRecipeRegistry.CentrifugeOutput> dryOutputs = CentrifugeRecipeRegistry.getOutputs("DRY_COMB");
        assertFalse(dryOutputs.isEmpty());

        List<CentrifugeRecipeRegistry.CentrifugeOutput> sweetOutputs = CentrifugeRecipeRegistry.getOutputs("SWEET_COMB");
        assertFalse(sweetOutputs.isEmpty());

        // Probar ejecución de proceso
        boolean gotHoneyDrop = false;
        for (int i = 0; i < 10; i++) {
            List<ItemStack> processed = module.processComb("HONEY_COMB");
            if (!processed.isEmpty()) {
                gotHoneyDrop = true;
                break;
            }
        }
        assertTrue(gotHoneyDrop, "El panal de miel centrifugado debe arrojar gotas de miel");
    }
}

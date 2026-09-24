package com.drakescraft.suites.bio;

import com.drakescraft.suites.bio.geneticchickens.ChickenDNA;
import com.drakescraft.suites.bio.geneticchickens.ChickenEggItem;
import com.drakescraft.suites.bio.geneticchickens.ChickenGene;
import com.drakescraft.suites.bio.geneticchickens.ChickenSpeciesRegistry;
import com.drakescraft.suites.bio.geneticchickens.GeneticChickensModule;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class GeneticChickensModuleTest {

    private ServerMock server;
    private DrakesBioPlugin plugin;
    private GeneticChickensModule module;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DrakesBioPlugin.class);
        module = new GeneticChickensModule(plugin);
        module.onEnable();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Debe contener exactamente 64 especies catalogadas en ChickenSpeciesRegistry")
    void testSpeciesRegistryCompleteness() {
        assertEquals(64, ChickenSpeciesRegistry.count(), "Deben existir exactamente 64 combinaciones (2^6)");

        for (int i = 0; i < 64; i++) {
            ChickenSpeciesRegistry.ChickenSpec spec = ChickenSpeciesRegistry.get(i);
            assertNotNull(spec, "La especie con typing " + i + " no debe ser nula");
            assertEquals(i, spec.typing(), "El typing debe coincidir con el índice");
            assertNotNull(spec.name(), "El nombre de la especie no puede ser nulo");
            assertTrue(spec.tier() >= 0 && spec.tier() <= 9, "El tier debe estar entre 0 y 9");
        }
    }

    @Test
    @DisplayName("Pollo Feather debe ser typing 63 (Tier 0) y Netherite debe ser typing 0 (Tier 9)")
    void testExtremesTyping() {
        ChickenSpeciesRegistry.ChickenSpec feather = ChickenSpeciesRegistry.get(63);
        assertEquals("Feather", feather.name());
        assertEquals(0, feather.tier());

        ChickenSpeciesRegistry.ChickenSpec netherite = ChickenSpeciesRegistry.get(0);
        assertEquals("Netherite", netherite.name());
        assertEquals(9, netherite.tier());

        ChickenDNA featherDna = new ChickenDNA(63);
        assertEquals(63, featherDna.getTyping());
        assertEquals(6, featherDna.getActiveGeneCount());

        ChickenDNA netheriteDna = new ChickenDNA(0);
        assertEquals(0, netheriteDna.getTyping());
        assertEquals(0, netheriteDna.getActiveGeneCount());
    }

    @Test
    @DisplayName("Prueba de dominancia y herencia meiótica sin mutación")
    void testGeneticsInheritance() {
        // Pollo A: doble dominante en todos los loci (BBCCDDFFSSWW = typing 63)
        ChickenDNA parentDominant = new ChickenDNA(63);
        // Pollo B: doble recesivo en todos los loci (bbccddffssww = typing 0)
        ChickenDNA parentRecessive = new ChickenDNA(0);

        // Sin mutación (0% de chance)
        ChickenDNA child = parentDominant.breed(parentRecessive, 0.0, 0);

        // Al recibir un alelo mayúscula de A y uno minúscula de B, todos los loci son Bb, Cc, etc.
        // Como tienen un alelo mayúscula, son dominantes -> typing resultante debe seguir siendo 63
        assertEquals(63, child.getTyping());
        assertEquals(6, child.getActiveGeneCount());

        for (ChickenGene gene : child.getSequence()) {
            assertEquals(1, gene.getState(), "Cada gen heterocigoto debe tener estado 1");
            assertTrue(gene.isDominant(), "El gen heterocigoto debe expresarse dominante");
        }
    }

    @Test
    @DisplayName("Debe crear huevos genéticos con PDC intacto y recuperable")
    void testEggItemCreationAndPdc() {
        ChickenDNA dna = new ChickenDNA(1); // Diamond (000001)
        ItemStack egg = module.createEgg(dna);

        assertNotNull(egg);
        assertEquals(ChickenEggItem.SLIMEFUN_ID, com.drakescraft.suites.core.pdc.SuiteItemPdcBridge.getSlimefunId(egg));

        ChickenDNA extractedDna = ChickenEggItem.extractDna(egg);
        assertNotNull(extractedDna);
        assertEquals(1, extractedDna.getTyping(), "El ADN extraído debe retener el typing original (1 = Diamond)");
        assertEquals(dna.toNotation(), extractedDna.toNotation());

        int extractedTyping = ChickenEggItem.extractTyping(egg);
        assertEquals(1, extractedTyping);
    }
}

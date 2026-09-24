package com.drakescraft.suites.bio.slimybees;

import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Motor de cruce genético y herencia mendeliana para abejas.
 */
public final class BeeBreedingEngine {

    @Getter
    public static final class BreedingOutcome {
        private final BeeGenome princessGenome;
        private final List<BeeGenome> droneGenomes;

        public BreedingOutcome(BeeGenome princessGenome, List<BeeGenome> droneGenomes) {
            this.princessGenome = princessGenome;
            this.droneGenomes = droneGenomes;
        }
    }

    private BeeBreedingEngine() {}

    /**
     * Realiza el cruce genético entre una princesa y un zángano.
     *
     * @param princess           Genoma de la princesa
     * @param drone              Genoma del zángano
     * @param mutationMultiplier Multiplicador de probabilidad de mutación (ej. marcos o colmena industrial)
     * @return Resultado del cruce con la princesa heredera y los zánganos
     */
    public static BreedingOutcome breed(BeeGenome princess, BeeGenome drone, double mutationMultiplier) {
        Objects.requireNonNull(princess, "princess no puede ser nula");
        Objects.requireNonNull(drone, "drone no puede ser nulo");

        // 1. Princesa heredera
        BeeGenome childPrincess = combineGenomes(princess, drone, mutationMultiplier);

        // 2. Determinar fertilidad (cantidad de zánganos resultantes)
        int fertility = princess.getFertilityCount();
        int droneCount = Math.max(1, fertility);

        List<BeeGenome> childDrones = new ArrayList<>(droneCount);
        for (int i = 0; i < droneCount; i++) {
            childDrones.add(combineGenomes(princess, drone, mutationMultiplier));
        }

        return new BreedingOutcome(childPrincess, childDrones);
    }

    /**
     * Combina dos genomas generando un hijo con alelos meióticos y posibles mutaciones.
     */
    public static BeeGenome combineGenomes(BeeGenome parentA, BeeGenome parentB, double mutationMultiplier) {
        Map<BeeChromosomeType, BeeChromosome> childMap = new EnumMap<>(BeeChromosomeType.class);

        // Cruce de especies
        BeeChromosome speciesA = parentA.getChromosome(BeeChromosomeType.SPECIES);
        BeeChromosome speciesB = parentB.getChromosome(BeeChromosomeType.SPECIES);

        String alleleA = speciesA != null ? speciesA.meioticSelect() : BeeSpecies.COMMON.getSpeciesUid();
        String alleleB = speciesB != null ? speciesB.meioticSelect() : BeeSpecies.COMMON.getSpeciesUid();

        BeeSpecies specA = BeeSpecies.fromUid(alleleA);
        BeeSpecies specB = BeeSpecies.fromUid(alleleB);
        if (specA == null) specA = BeeSpecies.COMMON;
        if (specB == null) specB = BeeSpecies.COMMON;

        // Comprobar mutación
        Optional<BeeSpecies> mutation = BeeMutationRegistry.attemptMutation(specA, specB, mutationMultiplier);
        if (mutation.isPresent()) {
            BeeSpecies mutated = mutation.get();
            // Ambos alelos mutan hacia la nueva especie descubierta
            childMap.put(BeeChromosomeType.SPECIES, new BeeChromosome(BeeChromosomeType.SPECIES, mutated.getSpeciesUid()));
        } else {
            childMap.put(BeeChromosomeType.SPECIES, new BeeChromosome(BeeChromosomeType.SPECIES, alleleA, alleleB));
        }

        // Cruce de los demás cromosomas
        for (BeeChromosomeType type : BeeChromosomeType.values()) {
            if (type == BeeChromosomeType.SPECIES) continue;

            BeeChromosome chrA = parentA.getChromosome(type);
            BeeChromosome chrB = parentB.getChromosome(type);

            String selectedA = chrA != null ? chrA.meioticSelect() : type.alleleUid("normal");
            String selectedB = chrB != null ? chrB.meioticSelect() : type.alleleUid("normal");

            childMap.put(type, new BeeChromosome(type, selectedA, selectedB));
        }

        return new BeeGenome(childMap);
    }
}

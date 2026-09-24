package com.drakescraft.suites.bio.slimybees;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Registro de mutaciones genéticas entre especies de abejas.
 */
public final class BeeMutationRegistry {

    @Getter
    public static final class MutationEntry {
        private final BeeSpecies parent1;
        private final BeeSpecies parent2;
        private final BeeSpecies result;
        private final double chance;

        public MutationEntry(BeeSpecies parent1, BeeSpecies parent2, BeeSpecies result, double chance) {
            this.parent1 = parent1;
            this.parent2 = parent2;
            this.result = result;
            this.chance = chance;
        }

        public boolean matches(BeeSpecies a, BeeSpecies b) {
            return (parent1 == a && parent2 == b) || (parent1 == b && parent2 == a);
        }
    }

    private static final List<MutationEntry> MUTATIONS = new ArrayList<>();

    static {
        // Abejas base nobles e industriales
        addMutation(BeeSpecies.CULTIVATED, BeeSpecies.COMMON, BeeSpecies.NOBLE, 0.15);
        addMutation(BeeSpecies.NOBLE, BeeSpecies.CULTIVATED, BeeSpecies.MAJESTIC, 0.10);
        addMutation(BeeSpecies.MAJESTIC, BeeSpecies.NOBLE, BeeSpecies.IMPERIAL, 0.05);

        addMutation(BeeSpecies.CULTIVATED, BeeSpecies.COMMON, BeeSpecies.DILIGENT, 0.15);
        addMutation(BeeSpecies.DILIGENT, BeeSpecies.CULTIVATED, BeeSpecies.UNWEARY, 0.10);
        addMutation(BeeSpecies.UNWEARY, BeeSpecies.DILIGENT, BeeSpecies.INDUSTRIOUS, 0.05);

        // Agrícolas
        addMutation(BeeSpecies.MEADOWS, BeeSpecies.COMMON, BeeSpecies.FARMER, 0.25);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.WHEAT, 0.30);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.SUGAR_CANE, 0.30);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.MELON, 0.20);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.PUMPKIN, 0.20);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.POTATO, 0.20);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.CARROT, 0.20);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.BEETROOT, 0.10);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.COCOA, 0.10);
        addMutation(BeeSpecies.FARMER, BeeSpecies.COMMON, BeeSpecies.BERRY, 0.10);

        // Secreta
        addMutation(BeeSpecies.ENDER, BeeSpecies.NETHER, BeeSpecies.SECRET, 0.50);

        // Mutaciones dinámicas para Common y Cultivated a partir de abejas salvajes
        BeeSpecies[] wild = {
                BeeSpecies.FOREST, BeeSpecies.MEADOWS, BeeSpecies.STONE,
                BeeSpecies.SANDY, BeeSpecies.WATER, BeeSpecies.NETHER, BeeSpecies.ENDER
        };
        for (int i = 0; i < wild.length; i++) {
            for (int j = i + 1; j < wild.length; j++) {
                addMutation(wild[i], wild[j], BeeSpecies.COMMON, 0.25);
            }
            addMutation(wild[i], BeeSpecies.COMMON, BeeSpecies.CULTIVATED, 0.20);
        }
    }

    private static void addMutation(BeeSpecies p1, BeeSpecies p2, BeeSpecies res, double chance) {
        MUTATIONS.add(new MutationEntry(p1, p2, res, chance));
    }

    public static List<MutationEntry> getMutations() {
        return Collections.unmodifiableList(MUTATIONS);
    }

    /**
     * Intenta mutar las dos especies de los padres aplicando un multiplicador ambiental/tecnológico.
     * Retorna la especie mutada si la probabilidad tiene éxito.
     */
    public static Optional<BeeSpecies> attemptMutation(BeeSpecies parentA, BeeSpecies parentB, double chanceMultiplier) {
        List<MutationEntry> matching = new ArrayList<>();
        for (MutationEntry m : MUTATIONS) {
            if (m.matches(parentA, parentB)) {
                matching.add(m);
            }
        }
        if (matching.isEmpty()) {
            return Optional.empty();
        }

        for (MutationEntry entry : matching) {
            double roll = ThreadLocalRandom.current().nextDouble();
            double finalChance = Math.min(1.0, entry.getChance() * chanceMultiplier);
            if (roll < finalChance) {
                return Optional.of(entry.getResult());
            }
        }

        return Optional.empty();
    }
}

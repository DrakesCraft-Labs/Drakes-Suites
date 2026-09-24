package com.drakescraft.suites.bio.geneticchickens;

import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;

/**
 * Representa la cadena de ADN de un pollo genético con 6 loci/genes.
 * Cada gen corresponde a uno de los alelos: {'b', 'c', 'd', 'f', 's', 'w'}.
 */
public class ChickenDNA {

    public static final char[] ALLELES = new char[] {'b', 'c', 'd', 'f', 's', 'w'};
    private final ChickenGene[] sequence;

    /**
     * Construye ADN a partir de un tipo numérico (0 a 63).
     */
    public ChickenDNA(int typing) {
        this.sequence = new ChickenGene[6];
        int clamped = Math.max(0, Math.min(63, typing));
        String typeStr = Integer.toBinaryString(clamped);
        String padded = String.format("%6s", typeStr).replace(' ', '0');

        for (int i = 0; i < 6; i++) {
            int bit = padded.charAt(i) - '0';
            // 3 para doble dominante si bit es 1, 0 para doble recesivo si bit es 0
            this.sequence[i] = new ChickenGene(ALLELES[i], 3 * bit);
        }
    }

    /**
     * Construye ADN a partir de gametos aportados por dos padres.
     */
    public ChickenDNA(char[] half1, char[] half2) {
        this.sequence = new ChickenGene[6];
        for (int i = 0; i < 6; i++) {
            this.sequence[i] = new ChickenGene(new char[] {half1[i], half2[i]});
        }
    }

    /**
     * Construye ADN a partir de una notación de 12 caracteres (ej: "BBccDdFfSSww").
     */
    public ChickenDNA(@Nonnull String notation) {
        this.sequence = new ChickenGene[6];
        if (notation.length() >= 12) {
            char[] chars = notation.toCharArray();
            for (int i = 0; i < 6; i++) {
                this.sequence[i] = new ChickenGene(new char[] {chars[2 * i], chars[2 * i + 1]});
            }
        } else {
            // Si es un string numérico de 6 dígitos de estado
            char[] stateChars = String.format("%6s", notation).replace(' ', '0').toCharArray();
            for (int i = 0; i < 6; i++) {
                int state = Character.getNumericValue(stateChars[i]);
                this.sequence[i] = new ChickenGene(ALLELES[i], state);
            }
        }
    }

    /**
     * Realiza el cruce genético con un compañero, aplicando segregación meiótica
     * y probabilidades de mutación biológica configurables.
     */
    @Nonnull
    public ChickenDNA breed(@Nonnull ChickenDNA partner, double mutationChance, int maxMutations) {
        char[] half1 = new char[6];
        char[] half2 = new char[6];

        for (int i = 0; i < 6; i++) {
            half1[i] = this.sequence[i].split();
            half2[i] = partner.sequence[i].split();
        }

        // Aplicación de mutaciones aleatorias controladas
        if (mutationChance > 0 && ThreadLocalRandom.current().nextDouble() < mutationChance) {
            int mutationsToApply = ThreadLocalRandom.current().nextInt(1, Math.max(2, maxMutations + 1));
            for (int m = 0; m < mutationsToApply; m++) {
                int geneIndex = ThreadLocalRandom.current().nextInt(6);
                // Invertir dominancia de uno de los alelos
                if (ThreadLocalRandom.current().nextBoolean()) {
                    half1[geneIndex] = Character.isUpperCase(half1[geneIndex])
                            ? Character.toLowerCase(half1[geneIndex])
                            : Character.toUpperCase(half1[geneIndex]);
                } else {
                    half2[geneIndex] = Character.isUpperCase(half2[geneIndex])
                            ? Character.toLowerCase(half2[geneIndex])
                            : Character.toUpperCase(half2[geneIndex]);
                }
            }
        }

        return new ChickenDNA(half1, half2);
    }

    /**
     * Calcula el tipo numérico del pollo (0 a 63).
     * Cada gen dominante aporta su peso binario:
     * gen 0 = 32, gen 1 = 16, gen 2 = 8, gen 3 = 4, gen 4 = 2, gen 5 = 1.
     */
    public int getTyping() {
        int typing = 0;
        for (int i = 0; i < 6; i++) {
            if (this.sequence[i].isDominant()) {
                typing |= (1 << (5 - i));
            }
        }
        return typing;
    }

    /**
     * Cantidad de genes activos (bits en 1).
     */
    public int getActiveGeneCount() {
        return Integer.bitCount(getTyping());
    }

    public ChickenGene[] getSequence() {
        return sequence;
    }

    /**
     * Notación textual de 12 caracteres.
     */
    public String toNotation() {
        StringBuilder sb = new StringBuilder(12);
        for (ChickenGene gene : sequence) {
            sb.append(gene.toString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toNotation() + " (" + getTyping() + ")";
    }
}

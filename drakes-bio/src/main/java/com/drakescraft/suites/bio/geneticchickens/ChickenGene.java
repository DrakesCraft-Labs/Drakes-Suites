package com.drakescraft.suites.bio.geneticchickens;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa un par de alelos para un rasgo genético específico en un pollo.
 * Soporta alelos mayúsculos (dominantes) y minúsculos (recesivos).
 */
public class ChickenGene {

    private final char[] alleles;

    public ChickenGene(char[] alleles) {
        this.alleles = new char[] {alleles[0], alleles[1]};
        Arrays.sort(this.alleles);
    }

    public ChickenGene(char markup, int state) {
        this.alleles = new char[] {markup, markup};
        for (int i = 0; i < 2; i++) {
            if ((state & (i + 1)) == (i + 1)) {
                this.alleles[i] = Character.toUpperCase(this.alleles[i]);
            }
        }
        Arrays.sort(this.alleles);
    }

    /**
     * Retorna el estado del gen:
     * 0 = doble recesivo (ej. bb)
     * 1 = heterocigoto / simple dominante (ej. Bb)
     * 3 = doble dominante (ej. BB)
     */
    public int getState() {
        int state = 0;
        for (int i = 0; i < 2; i++) {
            if (Character.isUpperCase(alleles[i])) {
                state += (i + 1);
            }
        }
        return state;
    }

    public boolean isDominant() {
        return getState() > 0;
    }

    public boolean isHomozygous() {
        return alleles[0] == alleles[1];
    }

    public char[] getAlleles() {
        return new char[] {alleles[0], alleles[1]};
    }

    /**
     * Segregación meiótica: dona un alelo al azar para la cría.
     */
    public char split() {
        return ThreadLocalRandom.current().nextBoolean() ? alleles[0] : alleles[1];
    }

    @Override
    public String toString() {
        return new String(alleles);
    }
}

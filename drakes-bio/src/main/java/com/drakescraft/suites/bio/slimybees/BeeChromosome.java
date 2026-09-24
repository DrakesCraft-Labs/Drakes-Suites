package com.drakescraft.suites.bio.slimybees;

import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa un par cromosómico diploide (alelo primario y secundario).
 */
@Getter
public class BeeChromosome {

    private final BeeChromosomeType type;
    private final String primaryAllele;
    private final String secondaryAllele;

    public BeeChromosome(BeeChromosomeType type, String primaryAllele, String secondaryAllele) {
        this.type = Objects.requireNonNull(type, "type no puede ser nulo");
        this.primaryAllele = Objects.requireNonNull(primaryAllele, "primaryAllele no puede ser nulo");
        this.secondaryAllele = Objects.requireNonNull(secondaryAllele, "secondaryAllele no puede ser nulo");
    }

    public BeeChromosome(BeeChromosomeType type, String homozygousAllele) {
        this(type, homozygousAllele, homozygousAllele);
    }

    /**
     * Retorna verdadero si ambos alelos son idénticos (homocigoto).
     */
    public boolean isHomozygous() {
        return primaryAllele.equalsIgnoreCase(secondaryAllele);
    }

    /**
     * Selecciona aleatoriamente uno de los dos alelos durante la meiosis (50/50).
     */
    public String meioticSelect() {
        return ThreadLocalRandom.current().nextBoolean() ? primaryAllele : secondaryAllele;
    }

    /**
     * Serializa en formato canónico de SlimyBees: "primary;secondary".
     */
    public String serialize() {
        return primaryAllele + ";" + secondaryAllele;
    }

    /**
     * Deserializa desde formato "primary;secondary".
     */
    public static BeeChromosome deserialize(BeeChromosomeType type, String str, String defaultAllele) {
        if (str == null || str.trim().isEmpty()) {
            return new BeeChromosome(type, defaultAllele, defaultAllele);
        }
        String[] parts = str.split(";");
        String p = parts.length > 0 && !parts[0].isEmpty() ? parts[0] : defaultAllele;
        String s = parts.length > 1 && !parts[1].isEmpty() ? parts[1] : p;
        return new BeeChromosome(type, p, s);
    }

    @Override
    public String toString() {
        return serialize();
    }
}

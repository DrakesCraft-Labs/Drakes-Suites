package com.drakescraft.suites.bio.slimybees;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Genoma completo de una abeja en SlimyBees (7 pares cromosómicos).
 */
@Getter
public class BeeGenome {

    public static final String CHROMOSOME_DELIMITER = "|";

    private final Map<BeeChromosomeType, BeeChromosome> chromosomes = new EnumMap<>(BeeChromosomeType.class);

    public BeeGenome(Map<BeeChromosomeType, BeeChromosome> chromosomeMap) {
        this.chromosomes.putAll(chromosomeMap);
    }

    /**
     * Construye un genoma puro (homocigoto) para una especie dada con sus rasgos canónicos por defecto.
     */
    public static BeeGenome createPure(BeeSpecies species) {
        Objects.requireNonNull(species, "species no puede ser nula");
        Map<BeeChromosomeType, BeeChromosome> map = new EnumMap<>(BeeChromosomeType.class);

        map.put(BeeChromosomeType.SPECIES, new BeeChromosome(BeeChromosomeType.SPECIES, species.getSpeciesUid()));
        map.put(BeeChromosomeType.PRODUCTIVITY, new BeeChromosome(BeeChromosomeType.PRODUCTIVITY, BeeChromosomeType.PRODUCTIVITY.alleleUid(species.getDefaultProductivity())));
        map.put(BeeChromosomeType.FERTILITY, new BeeChromosome(BeeChromosomeType.FERTILITY, BeeChromosomeType.FERTILITY.alleleUid(species.getDefaultFertility())));
        map.put(BeeChromosomeType.LIFESPAN, new BeeChromosome(BeeChromosomeType.LIFESPAN, BeeChromosomeType.LIFESPAN.alleleUid(species.getDefaultLifespan())));
        map.put(BeeChromosomeType.RANGE, new BeeChromosome(BeeChromosomeType.RANGE, BeeChromosomeType.RANGE.alleleUid(species.getDefaultRange())));
        map.put(BeeChromosomeType.PLANT, new BeeChromosome(BeeChromosomeType.PLANT, BeeChromosomeType.PLANT.alleleUid(species.getDefaultPlant())));
        map.put(BeeChromosomeType.EFFECT, new BeeChromosome(BeeChromosomeType.EFFECT, BeeChromosomeType.EFFECT.alleleUid(species.getDefaultEffect())));

        return new BeeGenome(map);
    }

    public BeeChromosome getChromosome(BeeChromosomeType type) {
        return chromosomes.get(type);
    }

    public BeeSpecies getPrimarySpecies() {
        BeeChromosome chr = chromosomes.get(BeeChromosomeType.SPECIES);
        if (chr == null) return BeeSpecies.COMMON;
        BeeSpecies s = BeeSpecies.fromUid(chr.getPrimaryAllele());
        return s != null ? s : BeeSpecies.COMMON;
    }

    public BeeSpecies getSecondarySpecies() {
        BeeChromosome chr = chromosomes.get(BeeChromosomeType.SPECIES);
        if (chr == null) return BeeSpecies.COMMON;
        BeeSpecies s = BeeSpecies.fromUid(chr.getSecondaryAllele());
        return s != null ? s : BeeSpecies.COMMON;
    }

    /**
     * Resuelve la especie fenotípicamente activa según dominancia mendeliana.
     */
    public BeeSpecies getActiveSpecies() {
        BeeSpecies prim = getPrimarySpecies();
        BeeSpecies sec = getSecondarySpecies();
        if (prim == sec) return prim;
        if (prim.isDominant() && !sec.isDominant()) return prim;
        if (!prim.isDominant() && sec.isDominant()) return sec;
        // Si ambas son dominantes o ambas recesivas, prevalece el alelo primario
        return prim;
    }

    /**
     * Retorna el número de crías que esta abeja suele dar según el alelo de fertilidad.
     */
    public int getFertilityCount() {
        BeeChromosome chr = chromosomes.get(BeeChromosomeType.FERTILITY);
        if (chr == null) return 2;
        String allele = chr.getPrimaryAllele().toLowerCase();
        if (allele.contains("very_high")) return 4;
        if (allele.contains("high")) return 3;
        if (allele.contains("low")) return 1;
        return 2; // normal
    }

    /**
     * Serializa el genoma completo en formato canónico de SlimyBees:
     * "chr0_prim;chr0_sec|chr1_prim;chr1_sec|..."
     */
    public String serialize() {
        StringJoiner joiner = new StringJoiner(CHROMOSOME_DELIMITER);
        for (BeeChromosomeType type : BeeChromosomeType.values()) {
            BeeChromosome chr = chromosomes.get(type);
            if (chr != null) {
                joiner.add(chr.serialize());
            } else {
                joiner.add("default;default");
            }
        }
        return joiner.toString();
    }

    /**
     * Deserializa un genoma desde una cadena delimitada por tuberías (|).
     */
    public static BeeGenome deserialize(String genomeStr) {
        if (genomeStr == null || genomeStr.trim().isEmpty()) {
            return createPure(BeeSpecies.COMMON);
        }

        String[] parts = genomeStr.split("\\" + CHROMOSOME_DELIMITER);
        BeeChromosomeType[] types = BeeChromosomeType.values();

        // 1. Resolver especie primero
        String speciesStr = parts.length > 0 ? parts[0] : "";
        BeeChromosome speciesChr = BeeChromosome.deserialize(BeeChromosomeType.SPECIES, speciesStr, BeeSpecies.COMMON.getSpeciesUid());
        BeeSpecies primarySpecies = BeeSpecies.fromUid(speciesChr.getPrimaryAllele());
        if (primarySpecies == null) primarySpecies = BeeSpecies.COMMON;

        Map<BeeChromosomeType, BeeChromosome> map = new EnumMap<>(BeeChromosomeType.class);
        map.put(BeeChromosomeType.SPECIES, speciesChr);

        for (int i = 1; i < types.length; i++) {
            BeeChromosomeType type = types[i];
            String partStr = i < parts.length ? parts[i] : null;

            String defaultAllele = switch (type) {
                case PRODUCTIVITY -> type.alleleUid(primarySpecies.getDefaultProductivity());
                case FERTILITY -> type.alleleUid(primarySpecies.getDefaultFertility());
                case LIFESPAN -> type.alleleUid(primarySpecies.getDefaultLifespan());
                case RANGE -> type.alleleUid(primarySpecies.getDefaultRange());
                case PLANT -> type.alleleUid(primarySpecies.getDefaultPlant());
                case EFFECT -> type.alleleUid(primarySpecies.getDefaultEffect());
                default -> type.alleleUid("normal");
            };

            map.put(type, BeeChromosome.deserialize(type, partStr, defaultAllele));
        }

        return new BeeGenome(map);
    }

    @Override
    public String toString() {
        return serialize();
    }
}

package com.drakescraft.suites.bio.cultivation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Catálogo canónico de los 82 cruces botánicos de Cultivation.
 * Resuelve hibridaciones entre dos plantas maduras adyacentes o diagonales,
 * soportando orden simétrico (A+B == B+A) y resultados aleatorios múltiples.
 */
public final class CultivationBreedRegistry {

    public record BreedOutcome(String resultPlant, double weight) {}

    public record PlantPair(String plantA, String plantB) {
        public PlantPair {
            // Normalizar orden alfabético para que (A, B) sea equivalente a (B, A)
            String aNorm = plantA.trim().toUpperCase(Locale.ROOT);
            String bNorm = plantB.trim().toUpperCase(Locale.ROOT);
            if (aNorm.compareTo(bNorm) > 0) {
                plantA = bNorm;
                plantB = aNorm;
            } else {
                plantA = aNorm;
                plantB = bNorm;
            }
        }
    }

    private static final Map<PlantPair, List<BreedOutcome>> RECIPES = new HashMap<>();

    static {
        // Los 82 cruces canónicos leídos del código fuente de DrakesCraft
        register("Air", "Light", "Chicken");
        register("Amethyst", "Emerald", "Echo");
        register("Blaze", "Magma Cube", "Ghast");
        register("Chicken", "Earth", "Rabbit");
        register("Chicken", "Water", "Turtle");

        // Los 3 pares con doble resultado al azar
        registerDual("Coal", "Earth", "Raw Copper", 0.5, "Raw Iron", 0.5);
        registerDual("Cobblestone", "Earth", "Deepslate", 0.5, "Gravel", 0.5);
        registerDual("Cow", "Sheep", "Bee", 0.5, "Pig", 0.5);

        register("Cobblestone", "Enderman", "End Stone");
        register("Cobblestone", "Fire", "Igneous");
        register("Cow", "Chicken", "Sheep");
        register("Creeper", "Phantom", "Blaze");
        register("Darkness", "Air", "Skeleton");
        register("Darkness", "Darkness", "Spider");
        register("Darkness", "Earth", "Zombie");
        register("Darkness", "Fire", "Creeper");
        register("Darkness", "Light", "Spectrum");
        register("Deepslate", "Scrappy", "Reinforced");
        register("Dirt", "Darkness", "Mud");
        register("Dirt", "Earth", "Sand");
        register("Dirt", "Fire", "Netherrack");
        register("Dirt", "Flower", "Vine");
        register("Dirt", "Light", "Sapling");
        register("Dirt", "Water", "Grass");
        register("Drowned", "Turtle", "Guardian");
        register("Dusty", "Water", "Concrete");
        register("Earth", "Spectrum", "Amethyst");
        register("Earth", "Water", "Dirt");
        register("End Stone", "Shulker", "Purpur");
        register("Enderman", "Power", "Ender Dragon");
        register("Enderman", "Turtle", "Shulker");
        register("Fire", "Earth", "Coal");
        register("Fire", "Water", "Cobblestone");
        register("Fish", "Cow", "Squid");
        register("Flower", "Slime", "Mushroom");
        register("Flower", "Wither", "Wither Rose");
        register("Frog", "Light", "Dim Lit");
        register("Glass", "Rainbow", "Stained");
        register("Grass", "Cobblestone", "Moss");
        register("Grass", "Light", "Cow");
        register("Grass", "Water", "Flower");
        register("Gravel", "Water", "Clay");
        register("Guardian", "Power", "Elder Guardian");
        register("Igneous", "Fire", "Blackstone");
        register("Lapis", "Darkness", "Nether Quartz");
        register("Lapis", "Wealth", "Diamond");
        register("Magma Cube", "Netherrack", "Magma");
        register("Nether Quartz", "Power", "Scrappy");
        register("Netherrack", "Cobblestone", "Basalt");
        register("Netherrack", "Grass", "Dark Grass");
        register("Phantom", "Darkness", "Enderman");
        register("Pig", "Squid", "Slime");
        register("Rainbow", "Bee", "Waxy");
        register("Rainbow", "Earth", "Terra");
        register("Rainbow", "Sheep", "Woolly");
        register("Raw Gold", "Flower", "Redstone");
        register("Raw Iron", "Raw Copper", "Raw Gold");
        register("Redstone", "Raw Gold", "Lapis");
        register("Sand", "Earth", "Red Sand");
        register("Sand", "Fire", "Glass");
        register("Sand", "Netherrack", "Soul");
        register("Sheep", "Power", "Goat");
        register("Skeleton", "Power", "Wither Skeleton");
        register("Slime", "Fire", "Magma Cube");
        register("Spectrum", "Spectrum", "Rainbow");
        register("Spider", "Chicken", "Phantom");
        register("Squid", "Bee", "Frog");
        register("Squid", "Light", "Glow Squid");
        register("Terra", "Fire", "Glazed");
        register("Terra", "Sand", "Dusty");
        register("Villager", "Fire", "Witch");
        register("Villager", "Villager", "Darkness");
        register("Villager", "Wealth", "Emerald");
        register("Vine", "Light", "Glowing Vine");
        register("Vine", "Netherrack", "Dark Flora");
        register("Water", "Light", "Fish");
        register("Wither Skeleton", "Power", "Wither");
        register("Zombie", "Light", "Villager");
        register("Zombie", "Water", "Drowned");
    }

    private static void register(String p1, String p2, String result) {
        RECIPES.put(new PlantPair(p1, p2), Collections.singletonList(new BreedOutcome(result, 1.0)));
    }

    private static void registerDual(String p1, String p2, String r1, double w1, String r2, double w2) {
        RECIPES.put(new PlantPair(p1, p2), Arrays.asList(new BreedOutcome(r1, w1), new BreedOutcome(r2, w2)));
    }

    @Nullable
    public static String crossBreed(@Nonnull String plantA, @Nonnull String plantB) {
        PlantPair pair = new PlantPair(plantA, plantB);
        List<BreedOutcome> outcomes = RECIPES.get(pair);
        if (outcomes == null || outcomes.isEmpty()) {
            return null;
        }

        if (outcomes.size() == 1) {
            return outcomes.get(0).resultPlant();
        }

        // Selección ponderada al azar
        double roll = ThreadLocalRandom.current().nextDouble();
        double cumulative = 0.0;
        for (BreedOutcome outcome : outcomes) {
            cumulative += outcome.weight();
            if (roll <= cumulative) {
                return outcome.resultPlant();
            }
        }
        return outcomes.get(outcomes.size() - 1).resultPlant();
    }

    public static boolean hasCombination(@Nonnull String plantA, @Nonnull String plantB) {
        return RECIPES.containsKey(new PlantPair(plantA, plantB));
    }

    public static int getRecipeCount() {
        return RECIPES.size();
    }

    public static int getTotalOutcomeCount() {
        int total = 0;
        for (List<BreedOutcome> outcomes : RECIPES.values()) {
            total += outcomes.size();
        }
        return total;
    }

    public static Map<PlantPair, List<BreedOutcome>> getRecipes() {
        return Collections.unmodifiableMap(RECIPES);
    }
}

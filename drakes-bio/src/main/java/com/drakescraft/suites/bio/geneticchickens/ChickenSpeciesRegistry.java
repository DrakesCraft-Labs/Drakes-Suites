package com.drakescraft.suites.bio.geneticchickens;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Catálogo maestro de las 64 especies de pollos genéticos de GeneticChickengineering.
 * Mapea cada identificador binario (0 a 63) con su nombre, material/ítem resultante,
 * tier biológico y tiempo base de puesta de recursos.
 */
public final class ChickenSpeciesRegistry {

    public record ChickenSpec(
            int typing,
            String name,
            int tier,
            Material fallbackMaterial,
            String slimefunId,
            int baseLayingIntervalSeconds
    ) {}

    private static final Map<Integer, ChickenSpec> SPECIES = new LinkedHashMap<>();

    static {
        // Tier 0: 6 genes activos (111111)
        register(63, "Feather", 0, Material.FEATHER, null, 120);

        // Tier 1: 5 genes activos
        register(31, "Bone", 1, Material.BONE, null, 150);
        register(47, "Cobblestone", 1, Material.COBBLESTONE, null, 150);
        register(55, "Dirt", 1, Material.DIRT, null, 150);
        register(59, "Flint", 1, Material.FLINT, null, 150);
        register(61, "Sand", 1, Material.SAND, null, 150);
        register(62, "Water", 1, Material.WATER_BUCKET, "GCE_WATER_EGG", 150);

        // Tier 2: 4 genes activos
        register(15, "Coal", 2, Material.COAL, null, 180);
        register(23, "String", 2, Material.STRING, null, 180);
        register(27, "Leather", 2, Material.LEATHER, null, 180);
        register(29, "Sugar", 2, Material.SUGAR, null, 180);
        register(30, "Sponge", 2, Material.SPONGE, null, 240);
        register(39, "Diorite", 2, Material.DIORITE, null, 180);
        register(43, "Andesite", 2, Material.ANDESITE, null, 180);
        register(45, "Gravel", 2, Material.GRAVEL, null, 180);
        register(46, "Ice", 2, Material.ICE, null, 180);
        register(51, "Granite", 2, Material.GRANITE, null, 180);
        register(53, "Clay", 2, Material.CLAY_BALL, null, 180);
        register(54, "Oak Log", 2, Material.OAK_LOG, null, 180);
        register(57, "Gunpowder", 2, Material.GUNPOWDER, null, 200);
        register(58, "Kelp", 2, Material.KELP, null, 180);
        register(60, "Slime Ball", 2, Material.SLIME_BALL, null, 200);

        // Tier 3: 3 genes activos
        register(7, "Gold", 3, Material.GOLD_INGOT, null, 240);
        register(11, "Netherrack", 3, Material.NETHERRACK, null, 200);
        register(13, "Glass", 3, Material.GLASS, null, 200);
        register(14, "Lapis", 3, Material.LAPIS_LAZULI, null, 220);
        register(19, "Iron", 3, Material.IRON_INGOT, null, 220);
        register(21, "Iron Dust", 3, Material.IRON_NUGGET, "IRON_DUST", 240);
        register(22, "Gold Dust", 3, Material.GOLD_NUGGET, "GOLD_DUST", 240);
        register(25, "Silver Dust", 3, Material.IRON_NUGGET, "SILVER_DUST", 260);
        register(26, "Zinc Dust", 3, Material.IRON_NUGGET, "ZINC_DUST", 260);
        register(28, "Cake", 3, Material.CAKE, null, 300);
        register(35, "Obsidian", 3, Material.OBSIDIAN, null, 260);
        register(37, "Copper Dust", 3, Material.COPPER_INGOT, "COPPER_DUST", 220);
        register(38, "Magnesium Dust", 3, Material.SUGAR, "MAGNESIUM_DUST", 260);
        register(41, "Lava", 3, Material.LAVA_BUCKET, "GCE_LAVA_EGG", 260);
        register(42, "Tin Dust", 3, Material.IRON_NUGGET, "TIN_DUST", 240);
        register(44, "Snowball", 3, Material.SNOWBALL, null, 180);
        register(49, "Redstone", 3, Material.REDSTONE, null, 220);
        register(50, "Cactus", 3, Material.CACTUS, null, 200);
        register(52, "Aluminum Dust", 3, Material.IRON_NUGGET, "ALUMINUM_DUST", 240);
        register(56, "Lead Dust", 3, Material.IRON_NUGGET, "LEAD_DUST", 240);

        // Tier 4: 2 genes activos
        register(3, "Blackstone", 4, Material.BLACKSTONE, null, 260);
        register(5, "Soul Soil", 4, Material.SOUL_SOIL, null, 260);
        register(9, "Blaze Rod", 4, Material.BLAZE_ROD, null, 300);
        register(17, "Ghast Tear", 4, Material.GHAST_TEAR, null, 360);
        register(33, "Sulfate", 4, Material.GUNPOWDER, "SULFATE", 280);
        register(6, "Shroomlight", 4, Material.SHROOMLIGHT, null, 280);
        register(10, "Quartz", 4, Material.QUARTZ, null, 260);
        register(18, "Basalt", 4, Material.BASALT, null, 260);
        register(34, "Crying Obsidian", 4, Material.CRYING_OBSIDIAN, null, 320);
        register(12, "Soul Sand", 4, Material.SOUL_SAND, null, 260);
        register(20, "Ender Pearl", 4, Material.ENDER_PEARL, null, 320);
        register(36, "Nether Wart", 4, Material.NETHER_WART, null, 280);
        register(24, "Phantom Membrane", 4, Material.PHANTOM_MEMBRANE, null, 340);
        register(40, "Magma Cream", 4, Material.MAGMA_CREAM, null, 300);
        register(48, "Glowstone Dust", 4, Material.GLOWSTONE_DUST, null, 260);

        // Tier 5: 1 gen activo
        register(1, "Diamond", 5, Material.DIAMOND, null, 420);
        register(2, "End Stone", 5, Material.END_STONE, null, 360);
        register(4, "Prismarine Crystals", 5, Material.PRISMARINE_CRYSTALS, null, 360);
        register(8, "Prismarine Shard", 5, Material.PRISMARINE_SHARD, null, 360);
        register(16, "Experience", 5, Material.EXPERIENCE_BOTTLE, null, 380);
        register(32, "Emerald", 5, Material.EMERALD, null, 450);

        // Tier 9: 0 genes activos (000000 - Cima genética)
        register(0, "Netherite", 9, Material.NETHERITE_INGOT, null, 600);
    }

    private static void register(int typing, String name, int tier, Material mat, @Nullable String sfId, int interval) {
        SPECIES.put(typing, new ChickenSpec(typing, name, tier, mat, sfId, interval));
    }

    @Nonnull
    public static ChickenSpec get(int typing) {
        ChickenSpec spec = SPECIES.get(typing);
        return spec != null ? spec : SPECIES.get(63); // Fallback a Feather
    }

    @Nullable
    public static ChickenSpec findByName(String name) {
        for (ChickenSpec spec : SPECIES.values()) {
            if (spec.name().equalsIgnoreCase(name)) {
                return spec;
            }
        }
        return null;
    }

    public static Map<Integer, ChickenSpec> getAll() {
        return Collections.unmodifiableMap(SPECIES);
    }

    public static int count() {
        return SPECIES.size();
    }
}

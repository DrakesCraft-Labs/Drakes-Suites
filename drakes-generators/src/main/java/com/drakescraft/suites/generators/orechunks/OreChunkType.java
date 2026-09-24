package com.drakescraft.suites.generators.orechunks;

/**
 * Catálogo canónico de Ore Chunks de Slimefun en DrakesCraft.
 * Preserva al 100% las texturas históricas en base64/hash y los IDs de Slimefun.
 */
public enum OreChunkType {

    IRON("IRON_ORE_CHUNK", "Iron Ore Chunk", "44cc1ccc75d0f724af8a5fe273edaf4c6d5951f9e4d038f9f16e4f2673ce3833", 4),
    GOLD("GOLD_ORE_CHUNK", "Gold Ore Chunk", "3184478b211439f3e2c509c3424ea5ff2fce73825c8bebf96cfccd103e4922eb", 2),
    COPPER("COPPER_ORE_CHUNK", "Copper Ore Chunk", "60d748757d6efddde852e0a4a1a9b92f2e4c58b1ea9a1731a32f6cedf2c23b36", 5),
    TIN("TIN_ORE_CHUNK", "Tin Ore Chunk", "de2c955177ff65a2d55af17743755090a5a6b68b3586ccbc31a342dad9ef7799", 3),
    SILVER("SILVER_ORE_CHUNK", "Silver Ore Chunk", "dde8f949bbf3a42782c531fbf8de9dc2d8cd84dd7cb8f5d5328eeda83956aac8", 2),
    ALUMINUM("ALUMINUM_ORE_CHUNK", "Aluminum Ore Chunk", "46732368c980b4c27495664bd50b5820cc37c573fb37a88f34c5d3a0dec66219", 4),
    LEAD("LEAD_ORE_CHUNK", "Lead Ore Chunk", "2333fcec07c89c5fdb886caf5e3ebf8c6a536dd662b31f91c1a6dbd913bc3db0", 2),
    ZINC("ZINC_ORE_CHUNK", "Zinc Ore Chunk", "63f82f20266b4b8e0456110379f941fca16413846e231e8ac202dc2caf7ffb41", 3),
    MAGNESIUM("MAGNESIUM_ORE_CHUNK", "Magnesium Ore Chunk", "e8c99d857a5b34331699ce6b5449d8d75f6c50b294ea1a29108f66ca086528bb", 4),
    NICKEL("NICKEL_ORE_CHUNK", "Nickel Ore Chunk", "3ba30df8316cdfe3c5b1ad7aa9775c94c3ad5e502ea1254efeb41344f7962381", 2),
    COBALT("COBALT_ORE_CHUNK", "Cobalt Ore Chunk", "ec54a54b1a49c29686be1c6e3e05dd068f85e994c8c893838cc5878b5446bc8a", 1);

    private final String slimefunId;
    private final String displayName;
    private final String textureHash;
    private final int amplifier;

    OreChunkType(String slimefunId, String displayName, String textureHash, int amplifier) {
        this.slimefunId = slimefunId;
        this.displayName = displayName;
        this.textureHash = textureHash;
        this.amplifier = amplifier;
    }

    public String getSlimefunId() { return slimefunId; }
    public String getDisplayName() { return displayName; }
    public String getTextureHash() { return textureHash; }
    public int getAmplifier() { return amplifier; }

    public static OreChunkType getBySlimefunId(String id) {
        if (id == null) return null;
        for (OreChunkType type : values()) {
            if (type.slimefunId.equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }
}

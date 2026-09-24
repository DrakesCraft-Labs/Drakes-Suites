package com.drakescraft.suites.bio.slimybees;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Catálogo canónico de especies de abejas de SlimyBees.
 */
@Getter
public enum BeeSpecies {

    // Salvajes / Nido
    FOREST("forest", "Bosque", true, "§2", "normal", "normal", "long", "very_long", "none", "none"),
    MEADOWS("meadows", "Pradera", true, "§2", "normal", "normal", "long", "very_long", "none", "none"),
    STONE("stone", "Piedra", true, "§7", "normal", "normal", "short", "normal", "none", "none"),
    SANDY("sandy", "Arenosa", true, "§e", "normal", "normal", "short", "normal", "none", "none"),
    WATER("water", "Acuática", true, "§1", "normal", "high", "normal", "normal", "none", "none"),
    NETHER("nether", "Del Nether", true, "§4", "normal", "low", "normal", "normal", "none", "none"),
    ENDER("ender", "Del End", true, "§5", "normal", "normal", "normal", "long", "none", "none"),

    // Nobles e Industriales
    COMMON("common", "Común", false, "§f", "normal", "normal", "long", "very_long", "none", "none"),
    CULTIVATED("cultivated", "Cultivada", true, "§b", "normal", "normal", "long", "very_long", "none", "none"),
    NOBLE("noble", "Noble", false, "§6", "normal", "normal", "long", "very_long", "none", "none"),
    MAJESTIC("majestic", "Majestuosa", true, "§6", "normal", "very_high", "long", "very_long", "none", "none"),
    IMPERIAL("imperial", "Imperial", false, "§6", "normal", "normal", "long", "very_long", "oxeye_daisy", "none"),
    DILIGENT("diligent", "Diligente", false, "§e", "normal", "normal", "short", "normal", "none", "none"),
    UNWEARY("unweary", "Incansable", true, "§e", "normal", "low", "short", "normal", "none", "none"),
    INDUSTRIOUS("industrious", "Industriosa", false, "§e", "normal", "normal", "short", "normal", "none", "none"),

    // Agrícolas
    FARMER("farmer", "Granjera", false, "§a", "normal", "normal", "normal", "long", "none", "none"),
    WHEAT("wheat", "Trigo", false, "§a", "normal", "normal", "normal", "long", "wheat", "none"),
    SUGAR_CANE("sugar_cane", "Caña de Azúcar", false, "§a", "normal", "normal", "normal", "long", "sugar_cane", "none"),
    MELON("melon", "Sandía", false, "§a", "normal", "normal", "normal", "long", "melon", "none"),
    PUMPKIN("pumpkin", "Calabaza", false, "§a", "normal", "normal", "normal", "long", "pumpkin", "none"),
    POTATO("potato", "Patata", false, "§a", "normal", "normal", "normal", "long", "potato", "none"),
    CARROT("carrot", "Zanahoria", false, "§a", "normal", "normal", "normal", "long", "carrot", "none"),
    BEETROOT("beetroot", "Remolacha", false, "§a", "normal", "normal", "normal", "long", "beetroot", "none"),
    COCOA("cocoa", "Cacao", false, "§a", "normal", "normal", "normal", "long", "cocoa", "none"),
    BERRY("berry", "Baya Dulce", false, "§a", "normal", "normal", "normal", "long", "berry", "none"),

    // Secreta / Cósmica
    SECRET("secret", "Secreta", true, "§3", "high", "normal", "very_long", "very_long", "none", "firework");

    private final String id;
    private final String displayName;
    private final boolean dominant;
    private final String colorCode;
    private final String defaultProductivity;
    private final String defaultFertility;
    private final String defaultLifespan;
    private final String defaultRange;
    private final String defaultPlant;
    private final String defaultEffect;

    private static final Map<String, BeeSpecies> BY_ID;
    private static final Map<String, BeeSpecies> BY_UID;

    static {
        Map<String, BeeSpecies> byId = new HashMap<>();
        Map<String, BeeSpecies> byUid = new HashMap<>();
        for (BeeSpecies species : values()) {
            byId.put(species.id.toLowerCase(Locale.ROOT), species);
            byUid.put(species.getSpeciesUid(), species);
        }
        BY_ID = Collections.unmodifiableMap(byId);
        BY_UID = Collections.unmodifiableMap(byUid);
    }

    BeeSpecies(String id, String displayName, boolean dominant, String colorCode,
               String defaultProductivity, String defaultFertility, String defaultLifespan,
               String defaultRange, String defaultPlant, String defaultEffect) {
        this.id = id;
        this.displayName = displayName;
        this.dominant = dominant;
        this.colorCode = colorCode;
        this.defaultProductivity = defaultProductivity;
        this.defaultFertility = defaultFertility;
        this.defaultLifespan = defaultLifespan;
        this.defaultRange = defaultRange;
        this.defaultPlant = defaultPlant;
        this.defaultEffect = defaultEffect;
    }

    /**
     * Retorna el UID canónico en formato SlimyBees "species:nombre".
     */
    public String getSpeciesUid() {
        return "species:" + id.toLowerCase(Locale.ROOT);
    }

    /**
     * Retorna el ID de Slimefun de la princesa.
     */
    public String getPrincessSfId() {
        return id.toUpperCase(Locale.ROOT) + "_BEE_PRINCESS";
    }

    /**
     * Retorna el ID de Slimefun del zángano.
     */
    public String getDroneSfId() {
        return id.toUpperCase(Locale.ROOT) + "_BEE_DRONE";
    }

    /**
     * Retorna el ID de Slimefun de la reina.
     */
    public String getQueenSfId() {
        return id.toUpperCase(Locale.ROOT) + "_BEE_QUEEN";
    }

    public static BeeSpecies fromId(String id) {
        if (id == null) return null;
        return BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static BeeSpecies fromUid(String uid) {
        if (uid == null) return null;
        return BY_UID.get(uid.toLowerCase(Locale.ROOT));
    }
}

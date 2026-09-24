package com.drakescraft.suites.magic.crystamae;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Definición canónica de hechizos arcanos de Crystamae.
 */
public class SpellDefinition {

    private static final Map<String, SpellDefinition> REGISTRY = new HashMap<>();

    public static final SpellDefinition FIREBALL = register(new SpellDefinition(
            "FIREBALL", "Bola de Fuego", StoryType.ELEMENTAL, StoryRarity.COMMON, 20, 64,
            "Lanza un proyectil flamígero que calcina el objetivo impactado."));

    public static final SpellDefinition HEAL = register(new SpellDefinition(
            "HEAL", "Manto Curativo", StoryType.HUMAN, StoryRarity.COMMON, 40, 32,
            "Restaura vitalidad y salud al taumaturgo o aliados cercanos."));

    public static final SpellDefinition FROST_NOVA = register(new SpellDefinition(
            "FROST_NOVA", "Nova de Escarcha", StoryType.ELEMENTAL, StoryRarity.UNCOMMON, 60, 48,
            "Congela el suelo en un radio circundante ralentizando a todos los enemigos."));

    public static final SpellDefinition ESCAPE_ROPE = register(new SpellDefinition(
            "ESCAPE_ROPE", "Cuerda de Escape", StoryType.HISTORICAL, StoryRarity.COMMON, 100, 16,
            "Teletransporta al invocador verticalmente al bloque de superficie más cercano."));

    public static final SpellDefinition TELEPORT = register(new SpellDefinition(
            "TELEPORT", "Distorsión del Vacío", StoryType.VOID, StoryRarity.RARE, 30, 64,
            "Abre una fisura espacial parpadeando instantáneamente hacia el punto de mira."));

    public static final SpellDefinition TUNNEL_BORE = register(new SpellDefinition(
            "TUNNEL_BORE", "Perforador Telúrico", StoryType.MECHANICAL, StoryRarity.RARE, 40, 50,
            "Desintegra un túnel de 3x3 bloques en la roca subterránea."));

    public static final SpellDefinition HARVEST_MOON = register(new SpellDefinition(
            "HARVEST_MOON", "Luna de Cosecha", StoryType.ANIMAL, StoryRarity.RARE, 80, 24,
            "Acelera el crecimiento botánico y nutre las crías de animales alrededor."));

    public static final SpellDefinition CALL_LIGHTNING = register(new SpellDefinition(
            "CALL_LIGHTNING", "Cólera de la Tempestad", StoryType.CELESTIAL, StoryRarity.EPIC, 50, 32,
            "Convoca un rayo celestial fulminante sobre el objetivo apuntado."));

    public static final SpellDefinition TIME_DILATION = register(new SpellDefinition(
            "TIME_DILATION", "Dilatación Temporal", StoryType.PHILOSOPHICAL, StoryRarity.EPIC, 120, 16,
            "Altera el flujo temporal acelerando tus movimientos y congelando a los adversarios."));

    public static final SpellDefinition BLOOD_MAGICS = register(new SpellDefinition(
            "BLOOD_MAGICS", "Pacto de Sangre", StoryType.ALCHEMICAL, StoryRarity.EPIC, 60, 20,
            "Canaliza la fuerza vital para amplificar masivamente el daño arcano infligido."));

    public static final SpellDefinition STAR_FALL = register(new SpellDefinition(
            "STAR_FALL", "Lluvia de Estrellas", StoryType.CELESTIAL, StoryRarity.MYTHICAL, 160, 12,
            "Bombardea una extensa zona con fragmentos cósmicos que infligen daño cósmico masivo."));

    public static final SpellDefinition HOLY_COW = register(new SpellDefinition(
            "HOLY_COW", "Bovino Sagrado", StoryType.ANIMAL, StoryRarity.UNIQUE, 200, 8,
            "Manifiesta un avatar bovino bendito que emite auras de invulnerabilidad divina."));

    private final String id;
    private final String name;
    private final StoryType storyType;
    private final StoryRarity storyRarity;
    private final int cooldownTicks;
    private final int baseCharges;
    private final String description;

    public SpellDefinition(String id, String name, StoryType storyType, StoryRarity storyRarity,
                           int cooldownTicks, int baseCharges, String description) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.storyType = Objects.requireNonNull(storyType, "storyType cannot be null");
        this.storyRarity = Objects.requireNonNull(storyRarity, "storyRarity cannot be null");
        this.cooldownTicks = cooldownTicks;
        this.baseCharges = baseCharges;
        this.description = description != null ? description : "";
    }

    public static SpellDefinition register(SpellDefinition spell) {
        REGISTRY.put(spell.getId().toUpperCase(), spell);
        return spell;
    }

    @Nullable
    public static SpellDefinition getById(String id) {
        if (id == null) return null;
        return REGISTRY.get(id.toUpperCase());
    }

    public static Map<String, SpellDefinition> getAll() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StoryType getStoryType() {
        return storyType;
    }

    public StoryRarity getStoryRarity() {
        return storyRarity;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public int getBaseCharges() {
        return baseCharges;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedName() {
        return storyRarity.getColor() + "" + ChatColor.BOLD + name;
    }
}

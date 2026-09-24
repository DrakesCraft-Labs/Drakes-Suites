package com.drakescraft.suites.magic.crystamae;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Módulo nativo de Crystamae Historia para DrakesMagic.
 * Administra el sistema de narrativas cristalizadas, dominios conceptuales,
 * placas de inscripción taumatúrgica y báculos arcanos de resonancia.
 */
public class CrystamaeModule extends AbstractSuiteModule {

    private final StavePlateManager staveManager = new StavePlateManager();
    private final List<CrystamaeStory> canonicalStories = new ArrayList<>();
    private double gildingMultiplier = 2.0;
    private int maxStaveTier = 5;

    public CrystamaeModule(JavaPlugin plugin) {
        super(plugin, "crystamae", "Crystamae Resonance");
    }

    @Override
    public void onEnable() {
        // Cargar parámetros de configuración
        this.gildingMultiplier = config.getDouble("stories.gilding-multiplier", 2.0);
        this.maxStaveTier = config.getInt("staves.max-tier", 5);

        // Inicializar historias canónicas
        initCanonicalStories();

        getPlugin().getLogger().info("[DrakesMagic] CrystamaeModule habilitado con "
                + CrystamaeItemsRegistry.getAllItems().size() + " ítems arcanos, "
                + SpellDefinition.getAll().size() + " hechizos y "
                + canonicalStories.size() + " historias canónicas.");
    }

    @Override
    public void onDisable() {
        canonicalStories.clear();
        getPlugin().getLogger().info("[DrakesMagic] CrystamaeModule deshabilitado limpiamente.");
    }

    @Override
    public void onReload() {
        this.gildingMultiplier = config.getDouble("stories.gilding-multiplier", 2.0);
        this.maxStaveTier = config.getInt("staves.max-tier", 5);
        getPlugin().getLogger().info("[DrakesMagic] CrystamaeModule recargado con éxito.");
    }

    private void initCanonicalStories() {
        canonicalStories.clear();
        canonicalStories.add(new CrystamaeStory("CH_PYROCLASM", "El Piroclasma Primordial",
                StoryType.ELEMENTAL, StoryRarity.EPIC, "Narra el nacimiento de los primeros continentes bajo ríos de lava ardiente.", false));
        canonicalStories.add(new CrystamaeStory("CH_CLOCKWORK_HEART", "El Corazón de Mecanismo",
                StoryType.MECHANICAL, StoryRarity.RARE, "Un relojero antiguo que otorgó su propio pulso a un autómata eterno.", false));
        canonicalStories.add(new CrystamaeStory("CH_PHILOSOPHERS_DECREE", "El Decreto Filosofal",
                StoryType.ALCHEMICAL, StoryRarity.MYTHICAL, "La fórmula olvidada para convertir plomo mortal en oro espiritual.", false));
        canonicalStories.add(new CrystamaeStory("CH_FALL_OF_ATLAN", "La Caída de Atlán",
                StoryType.HISTORICAL, StoryRarity.COMMON, "Fragmento que describe la última noche antes de que las olas tragaran la ciudadela.", false));
        canonicalStories.add(new CrystamaeStory("CH_HUMAN_AMBITION", "La Ambición de Ícaro",
                StoryType.HUMAN, StoryRarity.UNCOMMON, "Alas de cera forjadas para desafiar la gravedad celeste.", false));
        canonicalStories.add(new CrystamaeStory("CH_BEAST_PACK", "El Aullido de la Manada",
                StoryType.ANIMAL, StoryRarity.COMMON, "Lealtad instintiva que trasciende las eras salvajes.", false));
        canonicalStories.add(new CrystamaeStory("CH_SUPERNOVA", "La Danza de las Supernovas",
                StoryType.CELESTIAL, StoryRarity.MYTHICAL, "El destello final de un astro que fecunda nebulosas de polvo estelar.", false));
        canonicalStories.add(new CrystamaeStory("CH_VOID_ECHO", "El Eco del Vacío Infinito",
                StoryType.VOID, StoryRarity.UNIQUE, "El susurro de la nada que antecedió al tiempo y la creación.", false));
        canonicalStories.add(new CrystamaeStory("CH_DUALITY_PARADOX", "La Paradoja de la Dualidad",
                StoryType.PHILOSOPHICAL, StoryRarity.EPIC, "Una verdad que solo existe cuando su opuesto es plenamente aceptado.", false));
    }

    public StavePlateManager getStaveManager() {
        return staveManager;
    }

    public List<CrystamaeStory> getCanonicalStories() {
        return Collections.unmodifiableList(canonicalStories);
    }

    public double getGildingMultiplier() {
        return gildingMultiplier;
    }

    public int getMaxStaveTier() {
        return maxStaveTier;
    }
}

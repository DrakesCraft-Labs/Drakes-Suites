package com.drakescraft.suites.magic.relics;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Registro central canónico de reliquias de Cthonia, probabilidades y recompensas.
 */
public class RelicsRegistry {

    public static final NamespacedKey KEY_RELIC_ID = new NamespacedKey("relics_cthonia", "relic_id");
    public static final NamespacedKey KEY_RELIC_RARITY = new NamespacedKey("relics_cthonia", "rarity");

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final Map<String, RelicDefinition> relics = new ConcurrentHashMap<>();
    private final Map<RelicRarity, List<RelicDefinition>> relicsByRarity = new EnumMap<>(RelicRarity.class);

    private final Set<Material> miningSources = ConcurrentHashMap.newKeySet();
    private final Set<EntityType> mobSources = ConcurrentHashMap.newKeySet();

    public RelicsRegistry() {
        for (RelicRarity r : RelicRarity.values()) {
            relicsByRarity.put(r, new ArrayList<>());
        }
        registerDefaultRelics();
        registerDefaultSources();
    }

    private void registerDefaultRelics() {
        // COMMON
        register(new RelicDefinition("CTHONIAN_PEARL", "Perla de Cthonia", Material.ENDER_PEARL, RelicRarity.COMMON, "Una perla imbuida con la esencia del inframundo.", 25));
        register(new RelicDefinition("FISHING_SACK", "Saco de Pesca de Cthonia", Material.BUNDLE, RelicRarity.COMMON, "Contiene cebos antiguos conservados en salmuera marina.", 20));
        register(new RelicDefinition("HEALING_POTION", "Elixir Cthoniano", Material.POTION, RelicRarity.COMMON, "Brebaje restaurativo destilado en cavernas profundas.", 30));
        register(new RelicDefinition("RING_OF_SUFFERING", "Anillo del Sufrimiento", Material.IRON_NUGGET, RelicRarity.COMMON, "Forjado en cadenas de condenados.", 35));
        register(new RelicDefinition("PETTY_MARBLE_BLOCK", "Mármol de Cthonia", Material.QUARTZ_BLOCK, RelicRarity.COMMON, "Fragmento de arquitectura de templos sepultados.", 20));

        // UNCOMMON
        register(new RelicDefinition("AGED_WINE", "Vino Envejecido de Dionisio", Material.HONEY_BOTTLE, RelicRarity.UNCOMMON, "Fermentado durante siglos en catacumbas.", 60));
        register(new RelicDefinition("BLUE_GLOWSTONE", "Piedra Luminosa Azul", Material.GLOWSTONE, RelicRarity.UNCOMMON, "Emite un fulgor espectral gélido.", 50));
        register(new RelicDefinition("CTHONIAN_TOKEN", "Medalla del Inframundo", Material.GOLD_NUGGET, RelicRarity.UNCOMMON, "Moneda funeraria con el blasón de Hades.", 75));
        register(new RelicDefinition("TANZANITE_BLOCK", "Bloque de Tanzanita", Material.AMETHYST_BLOCK, RelicRarity.UNCOMMON, "Cristal de resonancia arcana subterránea.", 80));

        // RARE
        register(new RelicDefinition("BLAZE_ASHES", "Cenizas de Fuego Eterno", Material.GUNPOWDER, RelicRarity.RARE, "Residuos de llamas inextinguibles del averno.", 150));
        register(new RelicDefinition("CERULEAN_GEM", "Gema Cerúlea", Material.DIAMOND, RelicRarity.RARE, "Joya pura tallada por orfebres de Cthonia.", 180));
        register(new RelicDefinition("CROSSED_SWORDS", "Espadas Cruzadas de Ares", Material.IRON_SWORD, RelicRarity.RARE, "Emblema bélico conmemorativo de batallas primordiales.", 200));
        register(new RelicDefinition("HORN_OF_TAURUS", "Cuerno del Minotauro", Material.GOAT_HORN, RelicRarity.RARE, "Ruge con el eco del laberinto subterráneo.", 220));

        // EPIC
        register(new RelicDefinition("BOTTLE_O_POWER", "Frasco de Poder Primordial", Material.DRAGON_BREATH, RelicRarity.EPIC, "Condensación de energía cósmica pura.", 400));
        register(new RelicDefinition("FERVOR_HELMET", "Yelmo del Fervor", Material.NETHERITE_HELMET, RelicRarity.EPIC, "Armadura consagrada a los dioses del abismo.", 500));
        register(new RelicDefinition("THUNDER_IN_A_BOTTLE", "Trueno Embotellado", Material.EXPERIENCE_BOTTLE, RelicRarity.EPIC, "Chispa de Zeus capturada en vidrio templado.", 450));
        register(new RelicDefinition("VALIANT_TALISMAN", "Talismán del Valiente", Material.TOTEM_OF_UNDYING, RelicRarity.EPIC, "Otorga fortaleza indestructible al portador.", 600));

        // LEGENDARY
        register(new RelicDefinition("EYE_OF_SAURON", "Ojo del Cataclismo", Material.ENDER_EYE, RelicRarity.LEGENDARY, "Observa a través de las dimensiones con mirada flamígera.", 1200));
        register(new RelicDefinition("MAGMA_GAUNTLET", "Guantelete de Magma", Material.NETHERITE_CHESTPLATE, RelicRarity.LEGENDARY, "Forjado en el corazón del volcán primordial.", 1500));
        register(new RelicDefinition("SKULL_OF_PROMETHEUS", "Cráneo de Prometeo", Material.WITHER_SKELETON_SKULL, RelicRarity.LEGENDARY, "Contiene el fuego original robado a los dioses.", 2000));
        register(new RelicDefinition("MYSTERIOUS_HOLOCRON", "Holocrón Misterioso", Material.CONDUIT, RelicRarity.LEGENDARY, "Artefacto que almacena los secretos de la creación.", 2500));
    }

    private void registerDefaultSources() {
        // Bloques de minería que pueden contener reliquias
        miningSources.add(Material.STONE);
        miningSources.add(Material.DEEPSLATE);
        miningSources.add(Material.NETHERRACK);
        miningSources.add(Material.END_STONE);
        miningSources.add(Material.ANCIENT_DEBRIS);
        miningSources.add(Material.OBSIDIAN);
        miningSources.add(Material.CRYING_OBSIDIAN);

        // Mobs que pueden soltar reliquias
        mobSources.add(EntityType.ZOMBIE);
        mobSources.add(EntityType.SKELETON);
        mobSources.add(EntityType.WITHER_SKELETON);
        mobSources.add(EntityType.BLAZE);
        mobSources.add(EntityType.ENDERMAN);
        mobSources.add(EntityType.PIGLIN_BRUTE);
        mobSources.add(EntityType.WITHER);
    }

    public void register(RelicDefinition relic) {
        relics.put(relic.id(), relic);
        relicsByRarity.get(relic.rarity()).add(relic);
    }

    public RelicDefinition getById(String id) {
        return relics.get(id);
    }

    public Collection<RelicDefinition> getAllRelics() {
        return Collections.unmodifiableCollection(relics.values());
    }

    public List<RelicDefinition> getByRarity(RelicRarity rarity) {
        return Collections.unmodifiableList(relicsByRarity.get(rarity));
    }

    public Set<Material> getMiningSources() {
        return miningSources;
    }

    public Set<EntityType> getMobSources() {
        return mobSources;
    }

    public ItemStack createRelic(String relicId, int amount) {
        RelicDefinition def = relics.get(relicId);
        if (def == null) return null;

        ItemStack item = new ItemStack(def.material(), Math.max(1, amount));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String color = def.rarity().getColorTag();
            meta.displayName(MM.deserialize(color + "<bold>" + def.displayName() + "</bold>"));

            List<Component> lore = new ArrayList<>();
            lore.add(MM.deserialize("<gray>Rareza: " + color + def.rarity().getDisplayName()));
            lore.add(Component.empty());
            lore.add(MM.deserialize("<dark_gray><i>" + def.description() + "</i>"));
            lore.add(Component.empty());
            lore.add(MM.deserialize("<yellow>▶ Haz clic derecho para purificar y desellar."));
            meta.lore(lore);

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(KEY_RELIC_ID, PersistentDataType.STRING, def.id());
            pdc.set(KEY_RELIC_RARITY, PersistentDataType.STRING, def.rarity().name());
            item.setItemMeta(meta);
        }

        // Bridge a Slimefun PDC ID
        SuiteItemPdcBridge.setSlimefunId(item, def.getPdcKey());
        return item;
    }

    public ItemStack createRelicVoider(int amount) {
        ItemStack item = new ItemStack(Material.DISPENSER, Math.max(1, amount));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(MM.deserialize("<dark_purple><bold>Anulador de Reliquias (Relic Voider)</bold>"));
            List<Component> lore = new ArrayList<>();
            lore.add(MM.deserialize("<gray>Dispositivo que desintegra reliquias no deseadas"));
            lore.add(MM.deserialize("<gray>convirtiéndolas en polvo de experiencia pura."));
            lore.add(Component.empty());
            lore.add(MM.deserialize("<aqua>StarSuites · DrakesMagic"));
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, "RELIC_VOIDER");
        return item;
    }

    public boolean isRelic(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(KEY_RELIC_ID, PersistentDataType.STRING);
    }

    public RelicDefinition getRelic(ItemStack item) {
        if (!isRelic(item)) return null;
        String id = item.getItemMeta().getPersistentDataContainer().get(KEY_RELIC_ID, PersistentDataType.STRING);
        return id != null ? relics.get(id) : null;
    }

    public RelicDefinition rollRandomRelic(RelicRarity rarity) {
        List<RelicDefinition> pool = relicsByRarity.get(rarity);
        if (pool == null || pool.isEmpty()) return null;
        int idx = ThreadLocalRandom.current().nextInt(pool.size());
        return pool.get(idx);
    }

    public RelicDefinition rollRandomRelic() {
        double roll = ThreadLocalRandom.current().nextDouble(100.0);
        RelicRarity target;
        if (roll < 2.0) {
            target = RelicRarity.LEGENDARY;
        } else if (roll < 10.0) {
            target = RelicRarity.EPIC;
        } else if (roll < 25.0) {
            target = RelicRarity.RARE;
        } else if (roll < 50.0) {
            target = RelicRarity.UNCOMMON;
        } else {
            target = RelicRarity.COMMON;
        }
        return rollRandomRelic(target);
    }
}

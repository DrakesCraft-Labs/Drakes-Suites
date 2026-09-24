package com.drakescraft.suites.magic.crystamae;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Catálogo canónico de ítems, cristales, reactivos y mecanismos de Crystamae Historia.
 */
public final class CrystamaeItemsRegistry {

    private static final Map<String, ItemStack> ITEMS = new HashMap<>();

    static {
        // Cristales Matrices
        register("CRY_CRYSTAL_BLANK", createItem(Material.AMETHYST_SHARD, "CRY_CRYSTAL_BLANK",
                ChatColor.WHITE + "Cristal en Blanco", "Sustrato puro listo para capturar resonancias espirituales."));
        register("CRY_CRYSTAL_POLYCHROMATIC", createGlowingItem(Material.PRISMARINE_SHARD, "CRY_CRYSTAL_POLYCHROMATIC",
                ChatColor.AQUA + "Cristal Policromático", "Refracta luz en múltiples frecuencias de historias elementales."));
        register("CRY_CRYSTAL_KALEIDOSCOPIC", createGlowingItem(Material.DIAMOND, "CRY_CRYSTAL_KALEIDOSCOPIC",
                ChatColor.LIGHT_PURPLE + "Cristal Caleidoscópico", "Alberga infinitas combinaciones narrativas en su interior."));
        register("CRY_CRYSTAL_MOTLEY", createGlowingItem(Material.EMERALD, "CRY_CRYSTAL_MOTLEY",
                ChatColor.GREEN + "Cristal Abigarrado", "Entrelaza hilos del destino orgánico y animal."));
        register("CRY_CRYSTAL_PRISMATIC", createGlowingItem(Material.NETHER_STAR, "CRY_CRYSTAL_PRISMATIC",
                ChatColor.GOLD + "" + ChatColor.BOLD + "Cristal Prismático", "La máxima condensación de luz y poder taumatúrgico."));

        // Cristales Tipados por Dominio
        for (StoryType type : StoryType.values()) {
            String id = "CRY_CRYSTAL_" + type.name();
            register(id, createGlowingItem(Material.AMETHYST_SHARD, id,
                    type.getColor() + "Cristal " + type.getDisplayName(),
                    "Contiene narrativas e impresiones concentradas del dominio " + type.getDisplayName() + "."));
        }

        // Amalgamates (Polvos y Lingotes por Rareza)
        for (StoryRarity rarity : StoryRarity.values()) {
            String dustId = "CRY_AMALGAMATE_DUST_" + rarity.name();
            String ingotId = "CRY_AMALGAMATE_INGOT_" + rarity.name();
            register(dustId, createItem(Material.REDSTONE, dustId,
                    rarity.getColor() + "Polvo de Amalgama (" + rarity.getDisplayName() + ")",
                    "Residuo pulverizado de cristales de grado " + rarity.getDisplayName() + "."));
            register(ingotId, createGlowingItem(Material.IRON_INGOT, ingotId,
                    rarity.getColor() + "" + ChatColor.BOLD + "Lingote de Amalgama (" + rarity.getDisplayName() + ")",
                    "Aleación arcana fundida a partir de esencias de grado " + rarity.getDisplayName() + "."));
        }

        // Componentes y Reactivos
        register("CRY_ARCANE_SIGIL", createGlowingItem(Material.COMPASS, "CRY_ARCANE_SIGIL",
                ChatColor.DARK_PURPLE + "Sigilo Arcano", "Emblema esotérico para sintonizar báculos y pedestales."));
        register("CRY_IMBUED_GLASS", createItem(Material.TINTED_GLASS, "CRY_IMBUED_GLASS",
                ChatColor.LIGHT_PURPLE + "Vidrio Imbuido", "Cristal óptico capaz de aislar emanaciones mágicas inestables."));
        register("CRY_UNCANNY_PEARL", createGlowingItem(Material.ENDER_PEARL, "CRY_UNCANNY_PEARL",
                ChatColor.DARK_AQUA + "Perla Inquieta", "Vibra intensamente con pulsos dimensionales del vacío."));
        register("CRY_GILDED_PEARL", createGlowingItem(Material.ENDER_EYE, "CRY_GILDED_PEARL",
                ChatColor.GOLD + "Perla Dorada de Resonancia", "Potenciada mediante dorado prismático en el taller alquímico."));
        register("CRY_BASIC_FIBRES", createItem(Material.STRING, "CRY_BASIC_FIBRES",
                ChatColor.YELLOW + "Fibras de Seda Mística", "Hilos orgánicos receptivos a encantamientos cinéticos."));
        register("CRY_POWDERED_ESSENCE", createItem(Material.GLOWSTONE_DUST, "CRY_POWDERED_ESSENCE",
                ChatColor.AQUA + "Polvo de Esencia Primordial", "Condensado tras liquefactar cristales de historia."));
        register("CRY_MAGICAL_MILK", createItem(Material.MILK_BUCKET, "CRY_MAGICAL_MILK",
                ChatColor.WHITE + "Leche Alquímica Purificada", "Neutraliza impurezas taumatúrgicas residuales."));

        // Báculos Arcanos (Tier 1 a 5)
        for (int i = 1; i <= 5; i++) {
            register("CRY_STAVE_" + i, StavePlateManager.createStave(i));
        }

        // Placas de Hechizos
        register("CRY_PLATE_BLANK", createItem(Material.PAPER, "CRY_PLATE_BLANK",
                ChatColor.WHITE + "Placa en Blanco", "Soporte pergamino listo para la inscripción de conjuros."));
        register("CRY_PLATE_CHARGED", createGlowingItem(Material.MAP, "CRY_PLATE_CHARGED",
                ChatColor.YELLOW + "Placa Energizada", "Infundida con cargas crudas de éter arcano."));
        register("CRY_PLATE_MAGICAL", createGlowingItem(Material.FILLED_MAP, "CRY_PLATE_MAGICAL",
                ChatColor.LIGHT_PURPLE + "Placa de Conjuro Activo", "Contiene un hechizo calibrado listo para el báculo."));

        // Herramientas y Artefactos
        register("CRY_SATCHEL", createItem(Material.BUNDLE, "CRY_SATCHEL",
                ChatColor.GOLD + "Bolsa de Cristalógrafo", "Almacena cristales y fragmentos de historia en compartimentos dimensionales."));
        register("CRY_LUMINESCENCE_SCOOP", createItem(Material.IRON_SHOVEL, "CRY_LUMINESCENCE_SCOOP",
                ChatColor.AQUA + "Pala de Luminiscencia", "Cosecha fuentes luminosas conservando su carga etérea."));
        register("CRY_DISPLACER", createGlowingItem(Material.STICK, "CRY_DISPLACER",
                ChatColor.DARK_PURPLE + "Desplazador Espacial", "Altera la posición de pedestales y armaduras vivientes."));
        register("CRY_BALMY_SPONGE", createItem(Material.SPONGE, "CRY_BALMY_SPONGE",
                ChatColor.GREEN + "Esponja Balsámica", "Absorbe fluidos alquímicos y residuos nocivos."));
        register("CRY_CONNECTING_COMPASS", createItem(Material.COMPASS, "CRY_CONNECTING_COMPASS",
                ChatColor.AQUA + "Brújula de Resonancia", "Sintoniza frecuencias entre fuentes de historias activas."));
        register("CRY_THAUMATURGIC_SALT", createItem(Material.SUGAR, "CRY_THAUMATURGIC_SALT",
                ChatColor.WHITE + "Sales Taumatúrgicas", "Traza círculos de contención mística en el suelo."));
        register("CRY_REFRACTING_LENS", createGlowingItem(Material.SPYGLASS, "CRY_REFRACTING_LENS",
                ChatColor.GOLD + "Lente Refractaria", "Enfoca y calibra rayos de transmutación en el altar."));

        // Mecanismos Místicos
        register("CRY_REALISATION_ALTAR", createGlowingItem(Material.ENCHANTING_TABLE, "CRY_REALISATION_ALTAR",
                ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Altar de Realización",
                "Cristaliza historias abstractas en gemas físicas palpables."));
        register("CRY_LIQUEFACTION_BASIN", createItem(Material.CAULDRON, "CRY_LIQUEFACTION_BASIN",
                ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Cuenca de Licuefacción",
                "Funde cristales en fluidos arcanos esenciales y fórmulas de conjuros."));
        register("CRY_CHRONICLER_PANEL", createItem(Material.LECTERN, "CRY_CHRONICLER_PANEL",
                ChatColor.GOLD + "" + ChatColor.BOLD + "Panel del Cronista",
                "Extrae e inscribe historias procedentes de bloques del mundo."));
        register("CRY_PRISMATIC_GILDER", createItem(Material.SMITHING_TABLE, "CRY_PRISMATIC_GILDER",
                ChatColor.AQUA + "" + ChatColor.BOLD + "Dorador Prismático",
                "Aplica dorados relucientes que multiplican la potencia de las historias."));
        register("CRY_STAVE_CONFIGURATOR", createItem(Material.CARTOGRAPHY_TABLE, "CRY_STAVE_CONFIGURATOR",
                ChatColor.YELLOW + "" + ChatColor.BOLD + "Configurador de Báculos",
                "Inserta y remueve placas de conjuros en las ranuras del báculo arcano."));
    }

    private static void register(String id, ItemStack item) {
        ITEMS.put(id, item);
    }

    @Nullable
    public static ItemStack getItem(String id) {
        ItemStack item = ITEMS.get(id);
        return item != null ? item.clone() : null;
    }

    public static Map<String, ItemStack> getAllItems() {
        return Collections.unmodifiableMap(ITEMS);
    }

    private static ItemStack createItem(Material material, String slimefunId, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            if (lore.length > 0) {
                List<String> loreList = new ArrayList<>();
                for (String line : lore) {
                    loreList.add(ChatColor.GRAY + line);
                }
                meta.setLore(loreList);
            }
            SuiteItemPdcBridge.setSlimefunId(meta, slimefunId);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createGlowingItem(Material material, String slimefunId, String displayName, String... lore) {
        ItemStack item = createItem(material, slimefunId, displayName, lore);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private CrystamaeItemsRegistry() {}
}

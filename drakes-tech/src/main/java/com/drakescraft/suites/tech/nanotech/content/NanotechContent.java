package com.drakescraft.suites.tech.nanotech.content;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Registers the declarative catalog into the real Drakes Slimefun registry. */
public final class NanotechContent {
    private final org.bukkit.plugin.java.JavaPlugin plugin;
    private final com.github.drakescraft_labs.slimefun4.api.SlimefunAddon addon;
    private final Map<String, SlimefunItemStack> items = new HashMap<>();
    private final ItemGroup group;
    private final AddonItemResolver addons;

    public NanotechContent(org.bukkit.plugin.java.JavaPlugin plugin, com.github.drakescraft_labs.slimefun4.api.SlimefunAddon addon) {
        this.plugin = plugin;
        this.addon = addon;
        this.addons = new AddonItemResolver(plugin);
        ItemStack icon = new ItemStack(Material.HEART_OF_THE_SEA);
        var meta = icon.getItemMeta();
        meta.setDisplayName("§b§lDrakes Nanotech");
        meta.setLore(java.util.List.of("§7Programmable matter, ARC engineering,", "§7gamma science and cosmic containment."));
        icon.setItemMeta(meta);
        group = new ItemGroup(new NamespacedKey(plugin, "nanotech"), icon, 6);
    }

    /** Registers components first so later recipes can safely reference them. */
    public void registerAll() {
        for (ContentDefinition definition : NanotechCatalog.items()) registerItem(definition);
        for (MachineDefinition definition : NanotechCatalog.machines()) registerMachine(definition);
        addons.logSummary();
    }

    private void registerItem(ContentDefinition definition) {
        SlimefunItemStack stack = new SlimefunItemStack(definition.id(), definition.material(), definition.name(),
                "", "&8Branch: &f" + title(definition.branch()), "&8Technology Tier: &e" + definition.tier(),
                "&7" + definition.description(), "", "&4Ultra-endgame component");
        items.put(definition.id(), stack);
        ItemStack[] recipe = expensiveRecipe(definition);
        if (definition.id().matches("STARK_(STORAGE|NANO|ARMORY)_VAULT")) {
            new StarkStorageBlock(group, stack, recipe).register(addon);
        } else {
            new SlimefunItem(group, stack, RecipeType.ENHANCED_CRAFTING_TABLE, recipe).register(addon);
        }
    }

    private void registerMachine(MachineDefinition definition) {
        SlimefunItemStack stack = new SlimefunItemStack(definition.id(), definition.textureValue(), "&b" + definition.name(),
                "", "&8Machine Tier: &e" + definition.tier() + " &8| Branch: &f" + title(definition.branch()),
                "&7Power: &b" + definition.energyPerTick() + " J/t &8| Buffer: &b" + formatEnergy(definition.buffer()),
                "&7Process: &f" + definition.seconds() + "s &8| Batch: &f1", "&7Input: &f" + definition.input(),
                "&7Output: &f" + definition.output(), "&6Hazard: &f" + definition.hazard(),
                "&6Containment: &f" + definition.containment(), "&cShutdown: &f" + definition.shutdown(),
                "", "&aSingle-block machine &8· &7Owner protections apply");
        items.put(definition.id(), stack);
        if (definition.id().equals("UNIVERSAL_AUTOMATION_AI")) {
            new UniversalAutomationAI(plugin, group, stack, machineRecipe(definition)).register(addon);
            return;
        }
        String[] process = machineProcess(definition.id());
        ItemStack output = items.get(process[1]).clone();
        if (process.length > 2) output.setAmount(Integer.parseInt(process[2]));
        new NanotechPoweredMachine(group, stack, machineRecipe(definition), definition, process[0], output).register(addon);
    }

    private ItemStack[] expensiveRecipe(ContentDefinition definition) {
        ItemStack center = progressionCore(definition);
        ItemStack circuit = controlIngredient(definition.tier());
        ItemStack power = powerIngredient(definition.tier());
        ItemStack structure = addons.require("tier " + definition.tier() + " structural frame",
                definition.tier() >= 4 ? "SUPREME_ALLOY_TITANIUM" : "REINFORCED_PLATE",
                "REINFORCED_ALLOY_INGOT");
        return new ItemStack[]{structure.clone(), circuit.clone(), structure.clone(),
                power.clone(), center, power.clone(), structure.clone(), circuit.clone(), structure.clone()};
    }

    /** Enforces branch prerequisites so tier labels represent progression, not decorative rarity. */
    private ItemStack progressionCore(ContentDefinition definition) {
        String prerequisite = switch (definition.branch()) {
            case ARC -> definition.id().equals("PALLADIUM_COIL") ? null
                    : items.containsKey("ARC_REACTOR_CORE") ? "ARC_REACTOR_CORE" : "PALLADIUM_COIL";
            case NANOTECH -> items.containsKey("NANOCARBON_MATRIX") ? "NANOCARBON_MATRIX" : "ARC_REACTOR_CORE";
            case GAMMA -> items.containsKey("GAMMA_ISOTOPE") ? "GAMMA_ISOTOPE" : "PORTABLE_ARC_REACTOR";
            case WAKANDAN -> "PORTABLE_ARC_REACTOR";
            case LATVERIAN -> "ARC_REACTOR_CORE";
            case COSMIC -> items.containsKey("COSMIC_FRAGMENT") ? "COSMIC_FRAGMENT" : "LATVERIAN_CORE";
            case SALVAGED -> null;
        };
        SlimefunItemStack registered = prerequisite == null ? null : items.get(prerequisite);
        if (registered != null) return registered.clone();
        return new ItemStack(definition.tier() >= 5 ? Material.NETHER_STAR
                : definition.tier() >= 3 ? Material.NETHERITE_INGOT : Material.DIAMOND);
    }

    private ItemStack[] machineRecipe(MachineDefinition definition) {
        ItemStack core = definition.tier() >= 5 && items.containsKey("COSMIC_CIRCUIT")
                ? items.get("COSMIC_CIRCUIT").clone()
                : items.get("ARC_REACTOR_CORE").clone();
        ItemStack circuit = controlIngredient(definition.tier());
        ItemStack power = powerIngredient(definition.tier());
        ItemStack cargo = addons.require(definition.name() + " logistics", definition.tier() >= 4
                ? new String[]{"NETWORK_CONTROLLER", "NETWORK_BRIDGE", "CARGO_MANAGER"}
                : new String[]{"CARGO_MOTOR", "ELECTRIC_MOTOR"});
        return new ItemStack[]{power.clone(), circuit.clone(), power.clone(), cargo.clone(), core,
                cargo.clone(), power.clone(), circuit.clone(), power.clone()};
    }

    /** Raises control complexity from basic SF electronics to real Infinity circuitry. */
    private ItemStack controlIngredient(int tier) {
        return switch (tier) {
            case 0 -> addons.require("salvaged controls", "BASIC_CIRCUIT_BOARD");
            case 1, 2 -> addons.require("advanced controls", "ADVANCED_CIRCUIT_BOARD");
            case 3 -> addons.require("Supreme control substrate", "SUPREME_CARD_ELECTRIC_MOTOR", "ADVANCED_CIRCUIT_BOARD");
            case 4 -> addons.require("networked control substrate", "NETWORK_CONTROLLER", "NETWORK_BRIDGE", "ADVANCED_CIRCUIT_BOARD");
            case 5 -> addons.require("infinite control substrate", "INFINITE_CIRCUIT", "INFINITE_MACHINE_CIRCUIT", "NETWORK_CONTROLLER");
            default -> addons.require("universal control substrate", "INFINITE_MACHINE_CIRCUIT", "INFINITY_MATRIX", "INFINITE_CIRCUIT");
        };
    }

    /** Forces players to build Slimefun power infrastructure, including Supreme Ventus progression. */
    private ItemStack powerIngredient(int tier) {
        return switch (tier) {
            case 0 -> addons.require("salvaged drive", "ELECTRIC_MOTOR");
            case 1 -> addons.require("ARC regulation", "ENERGY_REGULATOR", "SMALL_CAPACITOR");
            case 2 -> addons.require("nanotech logistics", "CARGO_MOTOR", "ENERGIZED_CAPACITOR");
            case 3 -> addons.require("Ventus power", "SUPREME_BASIC_VENTUS_GENERATOR", "SUPREME_CETRUS_VENTUS", "ENERGIZED_CAPACITOR");
            case 4 -> addons.require("sovereign network power", "NETWORK_CAPACITOR_2", "SUPREME_VENTUS_GENERATOR", "CARBONADO_EDGED_CAPACITOR");
            case 5 -> addons.require("cosmic power", "INFINITY_CAPACITOR", "NETWORK_CAPACITOR_4", "SUPREME_SUPREME_CAPACITOR");
            default -> addons.require("universal power", "INFINITY_REACTOR", "INFINITY_CAPACITOR", "SUPREME_SUPREME_GENERATOR");
        };
    }

    /** Connects every machine to one registered input and output; startup tests audit these IDs. */
    static String[] machineProcess(String machineId) {
        return switch (machineId) {
            case "CARBYNE_PULVERIZER" -> new String[]{"SALVAGED_SERVO", "NANOCARBON_MATRIX"};
            case "MOLECULAR_SYNTHESIZER" -> new String[]{"NANOCARBON_MATRIX", "PROGRAMMABLE_NANOCELL"};
            case "NANOCELL_PROGRAMMER" -> new String[]{"NANOCARBON_MATRIX", "PROGRAMMABLE_NANOCELL", "2"};
            case "NANOFORGE" -> new String[]{"PROGRAMMABLE_NANOCELL", "NANO_SERVO_CLUSTER"};
            case "ARMOR_ASSEMBLY_BENCH" -> new String[]{"NANO_SERVO_CLUSTER", "MARK_L_NANOCORE"};
            case "PALLADIUM_WINDER" -> new String[]{"SALVAGED_SERVO", "PALLADIUM_COIL"};
            case "NEW_ELEMENT_ACCELERATOR" -> new String[]{"ARC_REACTOR_CORE", "NEW_ELEMENT_INGOT"};
            case "GAMMA_CYCLOTRON" -> new String[]{"GAMMA_ISOTOPE", "ROSS_ABSORBER"};
            case "BANNER_STABILIZATION_CHAMBER" -> new String[]{"GAMMA_ISOTOPE", "BANNER_STABILIZER"};
            case "TECHNO_ARCANE_FORGE" -> new String[]{"ARC_REACTOR_CORE", "LATVERIAN_CORE"};
            case "COSMIC_SPECTROMETER" -> new String[]{"COSMIC_FRAGMENT", "COSMIC_CIRCUIT"};
            case "SINGULARITY_GROWTH_CHAMBER" -> new String[]{"COSMIC_CIRCUIT", "POWER_STONE"};
            case "STELLAR_SWARM_FABRICATOR" -> new String[]{"PROGRAMMABLE_NANOCELL", "DYSON_SWARM_SEGMENT"};
            case "GRAVITON_FIELD_REGULATOR" -> new String[]{"GRAVITON_CONTAINMENT_COIL", "GOD_PRISON_FIELD_CORE"};
            default -> throw new IllegalArgumentException("Missing machine process: " + machineId);
        };
    }

    public boolean is(ItemStack stack, String id) {
        SlimefunItem item = SlimefunItem.getByItem(stack);
        return item != null && item.getId().equals(id);
    }

    public boolean isBareStone(ItemStack stack) {
        SlimefunItem item = SlimefunItem.getByItem(stack);
        return item != null && item.getId().matches("(POWER|SPACE|REALITY|MIND|TIME|SOUL)_STONE");
    }

    /** Returns an ID only when the stack belongs to this addon, never for another Slimefun addon. */
    public String idOf(ItemStack stack) {
        SlimefunItem item = SlimefunItem.getByItem(stack);
        if (item == null || !items.containsKey(item.getId())) return "";
        return item.getId();
    }

    public int itemCount() { return NanotechCatalog.items().size() + NanotechCatalog.machines().size(); }
    public int machineCount() { return NanotechCatalog.machines().size(); }
    public int multiblockCount() { return NanotechCatalog.multiblocks().size(); }
    private static String title(TechnologyBranch branch) { return branch.name().toLowerCase(Locale.ROOT).replace('_', ' '); }
    private static String formatEnergy(int energy) { return energy >= 1_000_000_000 ? energy / 1_000_000_000 + " GJ" : energy / 1_000_000 + " MJ"; }
}

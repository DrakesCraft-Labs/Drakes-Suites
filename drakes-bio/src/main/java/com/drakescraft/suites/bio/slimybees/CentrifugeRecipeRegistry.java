package com.drakescraft.suites.bio.slimybees;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Registro de recetas y probabilidades de procesamiento en la Centrifugadora de SlimyBees.
 */
public final class CentrifugeRecipeRegistry {

    @Getter
    public static final class CentrifugeOutput {
        private final ItemStack item;
        private final double chance;

        public CentrifugeOutput(ItemStack item, double chance) {
            this.item = item;
            this.chance = chance;
        }
    }

    private static final Map<String, List<CentrifugeOutput>> RECIPES = new HashMap<>();

    public static final ItemStack ITEM_BEESWAX = createCustomItem("BEESWAX", Material.GLOWSTONE_DUST, ChatColor.YELLOW + "Aceite de Cera");
    public static final ItemStack ITEM_HONEY_DROP = createCustomItem("HONEY_DROP", Material.GOLD_NUGGET, ChatColor.GOLD + "Gota de Miel");
    public static final ItemStack ITEM_ROYAL_JELLY = createCustomItem("ROYAL_JELLY", Material.LIGHT_GRAY_DYE, ChatColor.GOLD + "" + ChatColor.BOLD + "Jalea Real");
    public static final ItemStack ITEM_POLLEN = createCustomItem("POLLEN", Material.PUMPKIN_SEEDS, ChatColor.GOLD + "" + ChatColor.BOLD + "Polen");

    static {
        // Panal de Miel estándar
        List<CentrifugeOutput> honeyOutputs = new ArrayList<>();
        honeyOutputs.add(new CentrifugeOutput(ITEM_HONEY_DROP, 1.0));
        honeyOutputs.add(new CentrifugeOutput(ITEM_BEESWAX, 0.50));
        RECIPES.put("HONEY_COMB", honeyOutputs);

        // Panal Seco
        List<CentrifugeOutput> dryOutputs = new ArrayList<>();
        dryOutputs.add(new CentrifugeOutput(ITEM_BEESWAX, 0.75));
        dryOutputs.add(new CentrifugeOutput(ITEM_POLLEN, 0.35));
        RECIPES.put("DRY_COMB", dryOutputs);

        // Panal Dulce
        List<CentrifugeOutput> sweetOutputs = new ArrayList<>();
        sweetOutputs.add(new CentrifugeOutput(ITEM_HONEY_DROP, 1.0));
        sweetOutputs.add(new CentrifugeOutput(ITEM_ROYAL_JELLY, 0.30));
        RECIPES.put("SWEET_COMB", sweetOutputs);
    }

    private CentrifugeRecipeRegistry() {}

    private static ItemStack createCustomItem(String sfId, Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, sfId);
        return item;
    }

    public static List<CentrifugeOutput> getOutputs(String combId) {
        if (combId == null) return Collections.emptyList();
        return RECIPES.getOrDefault(combId.toUpperCase(), Collections.emptyList());
    }

    /**
     * Procesa un panal simulando la centrifugación con cálculo de probabilidades estocásticas.
     */
    public static List<ItemStack> processComb(String combId) {
        List<CentrifugeOutput> outputs = getOutputs(combId);
        if (outputs.isEmpty()) return Collections.emptyList();

        List<ItemStack> result = new ArrayList<>();
        for (CentrifugeOutput out : outputs) {
            if (ThreadLocalRandom.current().nextDouble() <= out.getChance()) {
                result.add(out.getItem().clone());
            }
        }
        return result;
    }
}

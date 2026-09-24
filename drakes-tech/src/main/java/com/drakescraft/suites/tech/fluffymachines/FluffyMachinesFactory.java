package com.drakescraft.suites.tech.fluffymachines;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Fábrica de items para la suite de automatización FluffyMachines en DrakesTech.
 */
public final class FluffyMachinesFactory {

    public static final String PDC_NAMESPACE = "drakestech";
    public static final String PDC_KEY_TYPE = "fluffy_type";
    public static final String PDC_KEY_ENERGY = "energy_cost";

    private FluffyMachinesFactory() {}

    public static ItemStack createMachine(FluffyMachineType type) {
        ItemStack item = new ItemStack(type.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            CrossVersionAdapter.setDisplayName(meta, "&b" + type.getDisplayName());
            CrossVersionAdapter.setLore(meta, Arrays.asList(
                    "&8StarSuites · DrakesTech",
                    "&7Máquina de automatización industrial.",
                    "&8\u21E8 &e\u26A1 &7Consumo: &b" + type.getEnergyPerTickJ() + " J/t",
                    "",
                    "&7Optimizado para redes logísticas de alta densidad.",
                    "&eAutomated industrial workstation."
            ));
            item.setItemMeta(meta);
        }
        SuiteItemPdcBridge.setSlimefunId(item, type.getCanonicalId());
        SuiteItemPdcBridge.setCustomString(item, PDC_NAMESPACE, PDC_KEY_TYPE, type.name());
        SuiteItemPdcBridge.setCustomInt(item, PDC_NAMESPACE, PDC_KEY_ENERGY, type.getEnergyPerTickJ());
        return item;
    }

    public static boolean isFluffyMachine(ItemStack item) {
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId == null) return false;
        for (FluffyMachineType type : FluffyMachineType.values()) {
            if (sfId.equalsIgnoreCase(type.getCanonicalId())) {
                return true;
            }
        }
        return false;
    }

    public static FluffyMachineType getMachineType(ItemStack item) {
        String typeStr = SuiteItemPdcBridge.getCustomString(item, PDC_NAMESPACE, PDC_KEY_TYPE);
        if (typeStr != null) {
            try {
                return FluffyMachineType.valueOf(typeStr);
            } catch (IllegalArgumentException ignored) {}
        }
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId == null) return null;
        for (FluffyMachineType type : FluffyMachineType.values()) {
            if (sfId.equalsIgnoreCase(type.getCanonicalId())) {
                return type;
            }
        }
        return null;
    }
}

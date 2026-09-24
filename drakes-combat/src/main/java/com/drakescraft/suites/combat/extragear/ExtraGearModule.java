package com.drakescraft.suites.combat.extragear;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Módulo nativo ExtraGear integrado en DrakesCombat.
 * Registra sets completos de armaduras y armas forjadas con aleaciones de Slimefun.
 */
public class ExtraGearModule extends AbstractSuiteModule {

    private final Map<String, ItemStack> registeredGear = new LinkedHashMap<>();
    private boolean obsidianArmorEnabled = true;
    private boolean potionEffectsOnHit = true;

    public ExtraGearModule(JavaPlugin plugin) {
        super(plugin, "extragear", "ExtraGear Modular Armors & Elemental Weapons");
    }

    @Override
    public void onEnable() {
        this.obsidianArmorEnabled = getConfig().getBoolean("features.obsidian-armor-set", true);
        this.potionEffectsOnHit = getConfig().getBoolean("features.potion-effects-on-hit", true);

        registeredGear.clear();
        for (ExtraGearMaterial mat : ExtraGearMaterial.values()) {
            if (mat == ExtraGearMaterial.OBSIDIAN && !obsidianArmorEnabled) {
                continue;
            }
            for (ExtraGearFactory.GearType type : ExtraGearFactory.GearType.values()) {
                String id = ExtraGearFactory.getSlimefunId(mat, type);
                ItemStack item = ExtraGearFactory.createGear(mat, type);
                registeredGear.put(id, item);
            }
        }

        getPlugin().getLogger().info("[ExtraGear] Módulo de armaduras y armas habilitado ("
                + registeredGear.size() + " piezas registradas, ObsidianSet: " + obsidianArmorEnabled + ").");
    }

    @Override
    public void onDisable() {
        registeredGear.clear();
        getPlugin().getLogger().info("[ExtraGear] Módulo de armaduras deshabilitado.");
    }

    public Map<String, ItemStack> getRegisteredGear() {
        return Collections.unmodifiableMap(registeredGear);
    }

    public boolean isObsidianArmorEnabled() {
        return obsidianArmorEnabled;
    }

    public boolean isPotionEffectsOnHit() {
        return potionEffectsOnHit;
    }
}

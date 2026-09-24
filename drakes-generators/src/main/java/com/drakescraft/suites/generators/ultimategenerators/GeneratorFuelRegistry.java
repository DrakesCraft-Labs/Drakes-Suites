package com.drakescraft.suites.generators.ultimategenerators;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registro de combustibles y tasas de rendimiento energético de UltimateGenerators2.
 */
public final class GeneratorFuelRegistry {

    @Getter
    public static final class FuelProperty {
        private final int energyPerSecond;
        private final int burnDurationSeconds;
        private final Material residue;

        public FuelProperty(int energyPerSecond, int burnDurationSeconds, Material residue) {
            this.energyPerSecond = energyPerSecond;
            this.burnDurationSeconds = burnDurationSeconds;
            this.residue = residue;
        }

        public long getTotalJoules() {
            return (long) energyPerSecond * burnDurationSeconds;
        }
    }

    private static final Map<String, FuelProperty> FUELS = new HashMap<>();

    static {
        FUELS.put("DIESEL_BUCKET", new FuelProperty(128, 120, Material.BUCKET));
        FUELS.put("BIOFUEL_BUCKET", new FuelProperty(64, 90, Material.BUCKET));
        FUELS.put("BIOMASS_BUCKET", new FuelProperty(32, 45, Material.BUCKET));
        FUELS.put("DRAGON_BREATH", new FuelProperty(256, 60, Material.GLASS_BOTTLE));
        FUELS.put("LAVA_BUCKET", new FuelProperty(32, 30, Material.BUCKET));
    }

    private GeneratorFuelRegistry() {}

    public static FuelProperty getFuelProperty(ItemStack item) {
        if (item == null) return null;

        // Comprobar por ID de Slimefun
        String sfId = SuiteItemPdcBridge.getSlimefunId(item);
        if (sfId != null && FUELS.containsKey(sfId.toUpperCase())) {
            return FUELS.get(sfId.toUpperCase());
        }

        // Comprobar por Material vanilla
        String matName = item.getType().name();
        return FUELS.get(matName);
    }

    public static Map<String, FuelProperty> getAllFuels() {
        return Collections.unmodifiableMap(FUELS);
    }
}

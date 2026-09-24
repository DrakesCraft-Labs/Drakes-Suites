package com.drakescraft.suites.utility.chestterminal;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Módulo ChestTerminal para DrakesUtility.
 * Brinda acceso digital remoto y centralizado a inventarios de cofres y redes de carga.
 */
public class ChestTerminalModule extends AbstractSuiteModule {

    private ChestTerminalIndex index;
    private int defaultWirelessRange = 128;

    public ChestTerminalModule(JavaPlugin plugin) {
        super(plugin, "chest_terminal", "ChestTerminal Digital Access");
    }

    @Override
    public void onEnable() {
        int throttleMs = config.getInt("anti-dupe.rapid-click-throttle-ms", 100);
        boolean ghostClearing = config.getBoolean("anti-dupe.item-ghost-clearing", true);
        this.defaultWirelessRange = config.getInt("features.wireless-range", 128);

        this.index = new ChestTerminalIndex(throttleMs, ghostClearing);

        getPlugin().getLogger().info("[DrakesUtility] ChestTerminalModule habilitado con throttle de "
                + throttleMs + "ms y rango de " + defaultWirelessRange + " bloques.");
    }

    @Override
    public void onDisable() {
        this.index = null;
        getPlugin().getLogger().info("[DrakesUtility] ChestTerminalModule deshabilitado limpiamente.");
    }

    public ChestTerminalIndex getIndex() {
        return index;
    }

    public int getDefaultWirelessRange() {
        return defaultWirelessRange;
    }
}

package com.drakescraft.suites.utility.slimehud;

import com.drakescraft.suites.core.module.AbstractSuiteModule;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Módulo SlimeHUD nativo en DrakesUtility.
 * Proporciona telemetría holográfica en tiempo real (Action Bar / BossBar / Overlay)
 * de energía, progreso y almacenamiento de máquinas de Slimefun al apuntar con la mira.
 */
public class SlimeHUDModule extends AbstractSuiteModule {

    private int raytraceRateTicks = 4;
    private boolean clientDisplayOnly = true;
    private boolean showMachineEnergy = true;
    private boolean showStoredItems = true;
    private boolean bossbarMode = false;

    // Caché de preferencias de usuarios que han desactivado el HUD (/slimehud toggle)
    private final Set<UUID> disabledPlayers = ConcurrentHashMap.newKeySet();
    // Última información enviada a cada jugador para evitar spam de paquetes
    private final Map<UUID, String> lastSentPacketContent = new ConcurrentHashMap<>();

    public SlimeHUDModule(JavaPlugin plugin) {
        super(plugin, "slimehud", "SlimeHUD Holographic Overlay");
    }

    @Override
    public void onEnable() {
        this.raytraceRateTicks = Math.max(1, getConfig().getInt("performance.raytrace-rate-ticks", 4));
        this.clientDisplayOnly = getConfig().getBoolean("anti-dupe.client-display-only", true);
        this.showMachineEnergy = getConfig().getBoolean("features.show-machine-energy", true);
        this.showStoredItems = getConfig().getBoolean("features.show-stored-items", true);
        this.bossbarMode = getConfig().getBoolean("features.bossbar-mode", false);

        getPlugin().getLogger().info("[SlimeHUD] Módulo inicializado (Rate: " + raytraceRateTicks
                + " ticks, Energy: " + showMachineEnergy + ", Stored: " + showStoredItems + ").");
    }

    @Override
    public void onDisable() {
        disabledPlayers.clear();
        lastSentPacketContent.clear();
        getPlugin().getLogger().info("[SlimeHUD] Módulo deshabilitado y memorias limpiadas.");
    }

    /**
     * Alterna la visibilidad del HUD para un jugador específico.
     * @return true si ahora está habilitado, false si se desactivó.
     */
    public boolean togglePlayer(UUID playerUuid) {
        if (disabledPlayers.contains(playerUuid)) {
            disabledPlayers.remove(playerUuid);
            return true;
        } else {
            disabledPlayers.add(playerUuid);
            lastSentPacketContent.remove(playerUuid);
            return false;
        }
    }

    public boolean isPlayerEnabled(UUID playerUuid) {
        return !disabledPlayers.contains(playerUuid);
    }

    /**
     * Envía la información de la máquina a la Action Bar del jugador si no la tiene silenciada
     * y el contenido ha cambiado.
     */
    public void sendMachineHud(Player player, HudMachineInfo info) {
        if (player == null || !player.isOnline() || !isPlayerEnabled(player.getUniqueId())) {
            return;
        }

        String plain = info.toPlainText();
        String previous = lastSentPacketContent.get(player.getUniqueId());
        if (plain.equals(previous)) {
            return; // Evita enviar paquetes redundantes
        }

        lastSentPacketContent.put(player.getUniqueId(), plain);
        Component component = info.toComponent();
        player.sendActionBar(component);
    }

    /**
     * Limpia la barra de acción cuando el jugador deja de mirar una máquina.
     */
    public void clearPlayerHud(Player player) {
        if (player == null) return;
        UUID uuid = player.getUniqueId();
        if (lastSentPacketContent.remove(uuid) != null) {
            player.sendActionBar(Component.empty());
        }
    }

    public void removePlayerSession(UUID uuid) {
        disabledPlayers.remove(uuid);
        lastSentPacketContent.remove(uuid);
    }

    public int getRaytraceRateTicks() {
        return raytraceRateTicks;
    }

    public boolean isClientDisplayOnly() {
        return clientDisplayOnly;
    }

    public boolean isShowMachineEnergy() {
        return showMachineEnergy;
    }

    public boolean isShowStoredItems() {
        return showStoredItems;
    }

    public boolean isBossbarMode() {
        return bossbarMode;
    }

    public Set<UUID> getDisabledPlayers() {
        return Collections.unmodifiableSet(disabledPlayers);
    }
}

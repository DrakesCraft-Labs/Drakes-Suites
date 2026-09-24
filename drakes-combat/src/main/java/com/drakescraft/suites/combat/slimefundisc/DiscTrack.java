package com.drakescraft.suites.combat.slimefundisc;

import org.bukkit.Material;

/**
 * Representa una pista musical personalizada en un disco de Slimefun.
 */
public record DiscTrack(
        String id,
        String title,
        String artist,
        Material material,
        int durationSeconds
) {}

package com.drakescraft.suites.server.modalities;

import java.util.List;
import java.util.Locale;

/** Immutable definition of a game modality and its world namespace. */
public record Modality(String id, String displayName, String description, String icon,
                       String command, List<String> worlds) {

    /** Returns whether a world belongs to this modality, including its dimensions. */
    public boolean matches(String worldName) {
        if (worldName == null) {
            return false;
        }
        String name = worldName.toLowerCase(Locale.ROOT);
        return worlds.stream()
                .map(prefix -> prefix.toLowerCase(Locale.ROOT))
                .anyMatch(prefix -> name.equals(prefix) || name.startsWith(prefix + "_"));
    }
}

package com.drakescraft.suites.tech.nanotech.content;

import org.bukkit.Material;

/** Immutable source of truth for a registered component or wearable. */
public record ContentDefinition(String id, Material material, String name, TechnologyBranch branch,
                                int tier, String description) {
    public ContentDefinition {
        if (id == null || !id.matches("[A-Z0-9_]+")) throw new IllegalArgumentException("Invalid item id: " + id);
        if (tier < 0 || tier > 6) throw new IllegalArgumentException("Tier must be between 0 and 6");
    }
}

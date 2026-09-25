package com.drakescraft.suites.tech.nanotech.content;

/** Complete player-facing specification for one single-block machine head. */
public record MachineDefinition(String id, String name, TechnologyBranch branch, int tier,
                                int energyPerTick, int buffer, int seconds, String input,
                                String output, String hazard, String containment, String shutdown,
                                String textureValue) {}

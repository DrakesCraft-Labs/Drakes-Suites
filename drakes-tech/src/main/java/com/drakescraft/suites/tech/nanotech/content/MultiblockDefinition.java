package com.drakescraft.suites.tech.nanotech.content;

/** Immutable field manual entry for an ultra-endgame structure. */
public record MultiblockDefinition(String id, String name, TechnologyBranch branch, int tier,
                                   String footprint, String controller, String structure,
                                   String power, String purpose, String safety) {}

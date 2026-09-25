package com.drakescraft.suites.tech.nanotech.content;

import org.bukkit.Material;

import java.util.List;

/** Declarative launch catalog; adding future chapters does not expand registration logic. */
public final class NanotechCatalog {
    private NanotechCatalog() {}

    public static List<ContentDefinition> items() {
        return List.of(
            d("SALVAGED_SERVO", Material.IRON_NUGGET, "&7Salvaged Servo", TechnologyBranch.SALVAGED, 0, "Recovered precision actuator."),
            d("PALLADIUM_COIL", Material.COPPER_INGOT, "&fPalladium Induction Coil", TechnologyBranch.ARC, 1, "Early ARC magnetic winding."),
            d("ARC_REACTOR_CORE", Material.NETHER_STAR, "&bStabilized ARC Core", TechnologyBranch.ARC, 2, "Compact clean-energy heart."),
            d("PORTABLE_ARC_REACTOR", Material.HEART_OF_THE_SEA, "&bPortable ARC Reactor", TechnologyBranch.ARC, 3, "Wearable energy supply: 40 MJ."),
            d("NEW_ELEMENT_INGOT", Material.NETHERITE_INGOT, "&fNew Element Ingot", TechnologyBranch.ARC, 4, "Vacuum-forged exotic lattice."),
            d("REPULSOR_EMITTER", Material.DIAMOND, "&bRepulsor Emitter", TechnologyBranch.ARC, 2, "Right-click: charged PvE energy ray."),
            d("UNIBEAM_LENS", Material.BEACON, "&fUnibeam Focusing Lens", TechnologyBranch.ARC, 3, "Chest-mounted coherent beam optic."),
            d("MICRO_MISSILE_RACK", Material.FIREWORK_ROCKET, "&cMicro-Missile Rack", TechnologyBranch.ARC, 3, "Controlled creature-targeting payload rack."),
            d("SYNTHETIC_VIBRANIUM_DUST", Material.GUNPOWDER, "&9Synthetic Vibranium Dust", TechnologyBranch.WAKANDAN, 4, "Kinetic resonance precursor."),
            d("KINETIC_WEAVE", Material.BLACK_CARPET, "&5Kinetic Weave", TechnologyBranch.WAKANDAN, 4, "Stores a bounded fraction of impacts."),
            d("NANOCARBON_MATRIX", Material.ECHO_SHARD, "&8Nanocarbon Matrix", TechnologyBranch.NANOTECH, 2, "Programmable matter scaffold."),
            d("PROGRAMMABLE_NANOCELL", Material.PRISMARINE_CRYSTALS, "&cProgrammable Nanocell", TechnologyBranch.NANOTECH, 2, "One billion synchronized assemblers."),
            d("SELF_REPAIRING_WEAVE", Material.STRING, "&6Self-Repairing Weave", TechnologyBranch.NANOTECH, 3, "Consumes nanomass to restore integrity."),
            d("NANOMASS_CANISTER", Material.HONEY_BOTTLE, "&cNanomass Canister", TechnologyBranch.NANOTECH, 3, "Sealed reserve for adaptive equipment."),
            d("QUANTUM_PROCESSOR", Material.RECOVERY_COMPASS, "&dQuantum Processor", TechnologyBranch.NANOTECH, 4, "Coordinates remote suit segments."),
            d("ENERGY_WEAVE_EMITTER", Material.END_CRYSTAL, "&bEnergy Weave Emitter", TechnologyBranch.NANOTECH, 4, "Projects body-bound hexagonal armor."),
            d("MARK_I_HELMET", Material.IRON_HELMET, "&7Mark I Helmet", TechnologyBranch.ARC, 2, "Heavy prototype targeting shell."),
            d("MARK_I_CHESTPLATE", Material.IRON_CHESTPLATE, "&7Mark I Chestplate", TechnologyBranch.ARC, 2, "Armored prototype ARC housing."),
            d("MARK_III_HELMET", Material.GOLDEN_HELMET, "&6Mark III Helmet", TechnologyBranch.ARC, 3, "Flight telemetry and threat display."),
            d("MARK_III_CHESTPLATE", Material.GOLDEN_CHESTPLATE, "&cMark III Chestplate", TechnologyBranch.ARC, 3, "Repulsor flight distribution frame."),
            d("MARK_V_SUITCASE", Material.RED_SHULKER_BOX, "&cMark V Suitcase", TechnologyBranch.NANOTECH, 3, "Emergency low-endurance deployment rig."),
            d("MARK_XLII_CONTROL_NODE", Material.ENDER_EYE, "&cMark XLII Control Node", TechnologyBranch.NANOTECH, 4, "Calls owner-bound modular segments."),
            d("MARK_L_NANOCORE", Material.NETHERITE_CHESTPLATE, "&cMark L Nanocore", TechnologyBranch.NANOTECH, 5, "Forms armor and weapons from nanomass."),
            d("HULKBUSTER_FRAME", Material.NETHERITE_CHESTPLATE, "&4Hulkbuster Frame", TechnologyBranch.GAMMA, 5, "Anchored gamma-containment exoframe."),
            d("RESCUE_BARRIER_NODE", Material.AMETHYST_SHARD, "&dRescue Barrier Node", TechnologyBranch.NANOTECH, 4, "Projects cooperative energy barriers."),
            d("GAMMA_ISOTOPE", Material.LIME_DYE, "&aGamma Isotope", TechnologyBranch.GAMMA, 3, "Boss-derived irradiated precursor."),
            d("BANNER_STABILIZER", Material.TOTEM_OF_UNDYING, "&aBanner Stabilizer", TechnologyBranch.GAMMA, 4, "Controls adaptive gamma mutation."),
            d("ROSS_ABSORBER", Material.SCULK_CATALYST, "&2Ross Energy Absorber", TechnologyBranch.GAMMA, 4, "Captures bounded gamma discharge."),
            d("LATVERIAN_CORE", Material.EMERALD, "&2Latverian Arcane Core", TechnologyBranch.LATVERIAN, 4, "Runic ARC hybrid with unstable resonance."),
            d("DOOMBOT_NEURAL_MATRIX", Material.SKELETON_SKULL, "&2Doombot Neural Matrix", TechnologyBranch.LATVERIAN, 5, "Owner-bound synthetic control matrix."),
            d("COSMIC_FRAGMENT", Material.AMETHYST_CLUSTER, "&dUnresolved Cosmic Fragment", TechnologyBranch.COSMIC, 5, "A boss relic without a stable domain."),
            d("URU_CONTAINMENT_TONGS", Material.NETHERITE_HOE, "&6Uru Containment Tongs", TechnologyBranch.COSMIC, 5, "Required to handle exposed artifacts."),
            d("EMPTY_COSMIC_CAPSULE", Material.GLASS_BOTTLE, "&fEmpty Cosmic Capsule", TechnologyBranch.COSMIC, 5, "Level-V containment vessel."),
            d("POWER_STONE", Material.PURPLE_DYE, "&5Power Stone", TechnologyBranch.COSMIC, 6, "UNCONTAINED: catastrophic carrier exposure."),
            d("SPACE_STONE", Material.BLUE_DYE, "&9Space Stone", TechnologyBranch.COSMIC, 6, "UNCONTAINED: spatial displacement risk."),
            d("REALITY_STONE", Material.RED_DYE, "&cReality Stone", TechnologyBranch.COSMIC, 6, "UNCONTAINED: perceptual collapse risk."),
            d("MIND_STONE", Material.YELLOW_DYE, "&eMind Stone", TechnologyBranch.COSMIC, 6, "UNCONTAINED: neural overload risk."),
            d("TIME_STONE", Material.LIME_DYE, "&aTime Stone", TechnologyBranch.COSMIC, 6, "UNCONTAINED: localized stasis risk."),
            d("SOUL_STONE", Material.ORANGE_DYE, "&6Soul Stone", TechnologyBranch.COSMIC, 6, "UNCONTAINED: vitality fracture risk."),
            d("EMPTY_NANOGAUNTLET", Material.NETHERITE_CHESTPLATE, "&8Empty Nanogauntlet", TechnologyBranch.COSMIC, 6, "Six persistent, duplicate-safe artifact sockets."),
            d("EMPTY_GOLDEN_GAUNTLET", Material.GOLDEN_CHESTPLATE, "&6Empty Uru Gauntlet", TechnologyBranch.COSMIC, 6, "Maximum output, severe cooldown platform."),
            d("COSMIC_CIRCUIT", Material.COMPARATOR, "&dCosmic Control Circuit", TechnologyBranch.COSMIC, 5, "Mediates six incompatible domains."),
            d("INFINITY_CONDUIT", Material.END_ROD, "&fInfinity Conduit", TechnologyBranch.COSMIC, 6, "Carries stabilized universal energy."),
            d("CHRONAL_ALLOY", Material.CLOCK, "&3Chronal Alloy", TechnologyBranch.COSMIC, 5, "Remembers bounded positional states."),
            d("TEMPORAL_ANCHOR", Material.LODESTONE, "&3Temporal Anchor", TechnologyBranch.COSMIC, 5, "Safe return point; never stores inventory."),
            d("ANTI_GRAVITON_PROJECTOR", Material.END_CRYSTAL, "&bAnti-Graviton Projector", TechnologyBranch.COSMIC, 5, "Lifts eligible creatures with a visible field."),
            d("SYNTHETHEZOID_MATRIX", Material.NETHER_STAR, "&eSynthezoid Matrix", TechnologyBranch.NANOTECH, 6, "A solar-powered programmable density core."),
            d("SPIDER_WEB_FLUID", Material.COBWEB, "&fSynthetic Web Fluid", TechnologyBranch.NANOTECH, 2, "High-tensile biodegradable restraint polymer."),
            d("WEB_SHOOTER", Material.TRIPWIRE_HOOK, "&fWeb Shooter", TechnologyBranch.NANOTECH, 3, "Restrains creatures with a visible polymer filament."),
            d("SPIDER_SENSE_NODE", Material.SPIDER_EYE, "&cSpider-Sense Node", TechnologyBranch.NANOTECH, 3, "Predictive proximity and projectile warning module."),
            d("CLASSIC_SPIDER_MASK", Material.LEATHER_HELMET, "&cClassic Spider Mask", TechnologyBranch.NANOTECH, 3, "Optical lenses, threat telemetry and toxin filter."),
            d("CLASSIC_SPIDER_SUIT", Material.LEATHER_CHESTPLATE, "&9Classic Spider Suit", TechnologyBranch.NANOTECH, 3, "Agile kinetic weave with wall-assist anchors."),
            d("STEALTH_SPIDER_SUIT", Material.LEATHER_CHESTPLATE, "&8Stealth Spider Suit", TechnologyBranch.NANOTECH, 4, "Low-emission infiltration weave and silent webbing."),
            d("IRON_SPIDER_NANOCORE", Material.NETHERITE_CHESTPLATE, "&cIron Spider Nanocore", TechnologyBranch.NANOTECH, 5, "Adaptive armor reservoir with four visual waldoes."),
            d("SPIDER_WALDO_MODULE", Material.END_ROD, "&6Spider Waldo Module", TechnologyBranch.NANOTECH, 5, "Articulated particle limb for defense and traversal."),
            d("HAWKEYE_COMPOUND_BOW", Material.BOW, "&5Hawkeye Compound Bow", TechnologyBranch.ARC, 3, "Smart bow with cartridge-driven tactical payloads."),
            d("TRICK_ARROW_CARTRIDGE", Material.ARROW, "&dTrick Arrow Cartridge", TechnologyBranch.ARC, 3, "Configurable EMP, cable, sonic and foam arrow base."),
            d("SONIC_ARROW", Material.SPECTRAL_ARROW, "&bSonic Disruption Arrow", TechnologyBranch.ARC, 4, "Visible concussive pulse against creatures."),
            d("EMP_ARROW", Material.TIPPED_ARROW, "&eEMP Arrow", TechnologyBranch.ARC, 4, "Temporarily disrupts authorized machine targets."),
            d("GRAPPLE_ARROW", Material.ARROW, "&fGrapple Arrow", TechnologyBranch.ARC, 3, "Traversal tether with protection-safe landing checks."),
            d("WIDOW_BITE_GAUNTLET", Material.LIGHTNING_ROD, "&eWidow's Bite Gauntlet", TechnologyBranch.ARC, 4, "Creature-only chained electroshock weapon."),
            d("WIDOW_BATON", Material.BLAZE_ROD, "&bElectroshock Baton", TechnologyBranch.ARC, 3, "Close-range stun conductor with visible arcs."),
            d("WIDOW_LINE_LAUNCHER", Material.FISHING_ROD, "&8Widow Line Launcher", TechnologyBranch.ARC, 3, "Silent tactical ascent and extraction line."),
            d("RED_ROOM_CLOAKING_MESH", Material.GRAY_CARPET, "&8Red Room Cloaking Mesh", TechnologyBranch.NANOTECH, 4, "Short-duration optical signature suppression."),
            d("ULTRON_AI_CORE", Material.OMINOUS_TRIAL_KEY, "&cUltron AI Core", TechnologyBranch.NANOTECH, 5, "Sandboxed synthetic intelligence with corruption fuse."),
            d("ULTRON_PRIMARY_SHELL", Material.NETHERITE_CHESTPLATE, "&8Ultron Primary Shell", TechnologyBranch.NANOTECH, 5, "Secondary-adamantium autonomous combat body."),
            d("ULTRON_SENTINEL_POD", Material.HEAVY_CORE, "&cUltron Sentinel Pod", TechnologyBranch.NANOTECH, 5, "Owner-limited temporary drone deployment capsule."),
            d("ULTRON_INFINITY_CORE", Material.END_CRYSTAL, "&5Ultron Infinity Core", TechnologyBranch.COSMIC, 6, "Artificial six-domain convergence intelligence."),
            d("ULTRON_INFINITY_BEAM", Material.END_ROD, "&dUltron Infinity Beam", TechnologyBranch.COSMIC, 6, "Sky-splitting cosmic ray with protected-region lockout."),
            d("SLING_RING", Material.GOLD_NUGGET, "&6Sling Ring", TechnologyBranch.LATVERIAN, 4, "Opens a short safe portal to an unprotected destination."),
            d("MYSTIC_CLOAK", Material.RED_BANNER, "&cMystic Levitation Cloak", TechnologyBranch.LATVERIAN, 4, "Owner-bound levitation and emergency rescue weave."),
            d("MIRROR_DIMENSION_PRISM", Material.PRISMARINE_SHARD, "&dMirror Dimension Prism", TechnologyBranch.LATVERIAN, 5, "Projects reversible geometry without changing blocks."),
            d("CRIMSON_BANDS_RELIC", Material.LEAD, "&cCrimson Bands Relic", TechnologyBranch.LATVERIAN, 5, "Binds creatures; never players or protected NPCs."),
            d("EYE_OF_AGAMOTTO_REPLICA", Material.ENDER_EYE, "&aChronal Eye Replica", TechnologyBranch.COSMIC, 6, "Bounded temporal field without world rollback."),
            d("REALITY_FRACTURE_DEVICE", Material.RESPAWN_ANCHOR, "&5Reality Fracture Device", TechnologyBranch.COSMIC, 6, "Cinematic dimensional rupture; zero block mutation."),
            d("ORBITAL_SKY_LANCE", Material.LIGHTNING_ROD, "&bOrbital Sky Lance", TechnologyBranch.COSMIC, 6, "Calls a column of energy from sky to ground against creatures."),
            d("CELESTIAL_NULLIFIER", Material.BEACON, "&fCelestial Nullifier", TechnologyBranch.COSMIC, 6, "Massive PvE suppression field with hard safety budgets."),
            d("SINGULARITY_WARHEAD", Material.END_CRYSTAL, "&8Singularity Warhead", TechnologyBranch.COSMIC, 6, "Controlled gravitational implosion; visual only for blocks."),
            d("NANITE_DISASSEMBLER", Material.NETHERITE_SWORD, "&cNanite Disassembler", TechnologyBranch.NANOTECH, 6, "Dissolves eligible creatures into bounded ash effects."),
            d("DIMENSIONAL_BREACH_CHARGE", Material.CRYING_OBSIDIAN, "&dDimensional Breach Charge", TechnologyBranch.COSMIC, 6, "Creates a temporary visual scar in unprotected wilderness."),
            d("UNIVERSAL_FORGE_KEY", Material.TRIAL_KEY, "&dUniversal Forge Key", TechnologyBranch.COSMIC, 6, "Proof of mastery across every branch."),
            d("MICRO_ARC_CAPACITOR", Material.LAPIS_LAZULI, "&bMicro ARC Capacitor", TechnologyBranch.ARC, 2, "Pulse-rated energy cell for one suit subsystem."),
            d("REPULSOR_MICRO_LENS", Material.AMETHYST_SHARD, "&fRepulsor Micro-Lens", TechnologyBranch.ARC, 2, "Diamond-cut optic aligned to one hundredth of a degree."),
            d("THRUST_VECTORING_NOZZLE", Material.COPPER_BULB, "&6Thrust-Vectoring Nozzle", TechnologyBranch.ARC, 3, "Micro-gimballed boot-flight exhaust."),
            d("FLIGHT_STABILIZER_GYRO", Material.COMPASS, "&bFlight Stabilizer Gyro", TechnologyBranch.ARC, 3, "Triple-redundant inertial correction package."),
            d("NANO_SERVO_CLUSTER", Material.IRON_NUGGET, "&cNano-Servo Cluster", TechnologyBranch.NANOTECH, 3, "Sixteen synchronized armor articulation motors."),
            d("HELMET_HUD_ARRAY", Material.SPYGLASS, "&eHelmet HUD Array", TechnologyBranch.ARC, 3, "Threat, altitude and energy telemetry optics."),
            d("ATMOSPHERIC_SEAL", Material.RABBIT_HIDE, "&7Atmospheric Seal", TechnologyBranch.NANOTECH, 3, "Self-testing pressure membrane for hostile environments."),
            d("CRYO_PROTECTION_LAYER", Material.PACKED_ICE, "&bCryogenic Protection Layer", TechnologyBranch.NANOTECH, 4, "Aerogel nanoweave against upper-atmosphere icing."),
            d("KINETIC_DAMPER", Material.PISTON, "&9Kinetic Damper", TechnologyBranch.WAKANDAN, 4, "Routes impact impulse away from the pilot."),
            d("HEAT_SINK_FIN", Material.COPPER_GRATE, "&6Micro Heat-Sink Fin", TechnologyBranch.ARC, 2, "One segment of a regenerative thermal lattice."),
            d("ARC_POWER_BUS", Material.POWERED_RAIL, "&bARC Power Bus", TechnologyBranch.ARC, 3, "Superconducting suit-wide energy backbone."),
            d("SMART_ALLOY_PLATE", Material.NETHERITE_SCRAP, "&8Smart-Alloy Plate", TechnologyBranch.NANOTECH, 4, "Shape-memory armor tile with embedded diagnostics."),
            d("NANITE_INJECTOR", Material.BREEZE_ROD, "&cNanite Injector", TechnologyBranch.NANOTECH, 4, "Meters authenticated nanomass into damaged suit zones."),
            d("MICRO_MISSILE", Material.FIREWORK_STAR, "&cGuided Micro-Missile", TechnologyBranch.ARC, 4, "Creature-lock payload with inert block-impact fuse."),
            d("PULSE_BARRIER_EMITTER", Material.PRISMARINE_CRYSTALS, "&bPulse Barrier Emitter", TechnologyBranch.NANOTECH, 4, "Short-lived hexagonal energy defense projector."),
            d("MARK_VI_ARC_CORE", Material.SEA_LANTERN, "&bMark VI Triangular ARC Core", TechnologyBranch.ARC, 4, "High-output triangular reactor architecture."),
            d("MARK_VII_DEPLOYMENT_POD", Material.RED_SHULKER_BOX, "&cMark VII Deployment Pod", TechnologyBranch.ARC, 4, "Autonomous rapid-assembly armor capsule."),
            d("MARK_XVII_ARTILLERY_CORE", Material.TNT, "&4Mark XVII Artillery Core", TechnologyBranch.ARC, 5, "Heartbreaker-class sustained repulsor distribution."),
            d("MARK_XXV_CONSTRUCTION_FRAME", Material.ANVIL, "&6Mark XXV Construction Frame", TechnologyBranch.ARC, 5, "Striker-class industrial exoskeleton skeleton."),
            d("MARK_XXXVIII_HEAVY_FRAME", Material.HEAVY_CORE, "&9Mark XXXVIII Heavy Frame", TechnologyBranch.ARC, 5, "Igor-class lifting and structural rescue chassis."),
            d("MARK_XLVI_COMBAT_NANOCORE", Material.NETHERITE_CHESTPLATE, "&cMark XLVI Combat Nanocore", TechnologyBranch.NANOTECH, 5, "Segmented high-speed combat armor reservoir."),
            d("MARK_LXXXV_NANOCORE", Material.NETHER_STAR, "&6Mark LXXXV Nanocore", TechnologyBranch.NANOTECH, 6, "Mature programmable armor with tool-forming reserves."),
            d("WAR_MACHINE_TARGETING_CORE", Material.TARGET, "&8War Machine Targeting Core", TechnologyBranch.ARC, 5, "Independent creature-only heavy-weapon fire control."),
            d("VIBRANIUM_SHIELD_BLANK", Material.DISC_FRAGMENT_5, "&9Vibranium Shield Blank", TechnologyBranch.WAKANDAN, 4, "Unpainted circular kinetic alloy laminate."),
            d("KINETIC_SHIELD_MATRIX", Material.SCULK_CATALYST, "&5Kinetic Shield Matrix", TechnologyBranch.WAKANDAN, 5, "Captures impact energy without reflecting it at players."),
            d("MAGNETIC_SHIELD_RECALL", Material.COMPASS, "&bMagnetic Shield Recall", TechnologyBranch.ARC, 4, "Owner-locked return guidance and wrist receiver."),
            d("TACTICAL_SHIELD_HARNESS", Material.LEATHER_CHESTPLATE, "&8Tactical Shield Harness", TechnologyBranch.ARC, 3, "Back-mounted retention and recall charging rig."),
            d("CAPTAIN_SHIELD", Material.SHIELD, "&cKinetic Captain Shield", TechnologyBranch.WAKANDAN, 5, "Right-click: protected creature-only ricochet and return."),
            d("STEALTH_CAPTAIN_SHIELD", Material.SHIELD, "&8Stealth Kinetic Shield", TechnologyBranch.WAKANDAN, 5, "Low-visibility creature-only ricochet platform."),
            d("STABILIZED_SUPER_SOLDIER_SERUM", Material.POTION, "&bStabilized Enhancement Serum", TechnologyBranch.GAMMA, 5, "Sterile endgame catalyst; no uncontrolled mutation."),
            d("CLEANROOM_CONTROLLER", Material.LODESTONE, "&fCleanroom Controller", TechnologyBranch.NANOTECH, 3, "Validates sealed multiblock walls before synthesis."),
            d("CLEANROOM_WALL", Material.WHITE_STAINED_GLASS, "&fCleanroom Wall", TechnologyBranch.NANOTECH, 3, "Particle-controlled structural panel."),
            d("VACUUM_SEAL_RING", Material.IRON_TRAPDOOR, "&7Vacuum Seal Ring", TechnologyBranch.ARC, 4, "Pressure-rated multiblock chamber collar."),
            d("ARC_CONTAINMENT_PYLON", Material.LIGHTNING_ROD, "&bARC Containment Pylon", TechnologyBranch.ARC, 4, "Grounds one quadrant of an accelerator field."),
            d("GAMMA_SHIELDING_BLOCK", Material.LIME_GLAZED_TERRACOTTA, "&aGamma Shielding Block", TechnologyBranch.GAMMA, 4, "Layered boron-vibranium radiation barrier."),
            d("COSMIC_FOCUSING_RING", Material.END_PORTAL_FRAME, "&dCosmic Focusing Ring", TechnologyBranch.COSMIC, 6, "One calibrated segment of a six-domain aperture."),
            d("INFINITY_FORGE_CONTROLLER", Material.RESPAWN_ANCHOR, "&dInfinity Forge Controller", TechnologyBranch.COSMIC, 6, "Audits the complete universal-forge multiblock."),
            d("SNAP_CALIBRATION_MATRIX", Material.CALIBRATED_SCULK_SENSOR, "&6Snap Calibration Matrix", TechnologyBranch.COSMIC, 6, "Hard-locks universal discharge to eligible mobs."),
            d("INFINITY_GAUNTLET", Material.GOLDEN_CHESTPLATE, "&6Infinity Gauntlet", TechnologyBranch.COSMIC, 6, "Right-click: spectacular bounded Snap; mobs only, never players."),
            d("NANO_INFINITY_GAUNTLET", Material.NETHERITE_CHESTPLATE, "&cNano Infinity Gauntlet", TechnologyBranch.COSMIC, 6, "Programmable universal discharge with the same immutable safety lock."),
            d("STARK_UNIBEAM_ASSEMBLY", Material.BEACON, "&fStark Unibeam Assembly", TechnologyBranch.ARC, 5, "Right-click: charged wide-spectrum creature-only beam."),
            d("SYNTHETIC_TISSUE_LATTICE", Material.PHANTOM_MEMBRANE, "&fSynthetic Tissue Lattice", TechnologyBranch.NANOTECH, 5, "Vibranium-bonded living substrate for a synthezoid body."),
            d("ULTRON_VISION_FACEPLATE", Material.NETHERITE_HELMET, "&cUltron-Vision Faceplate", TechnologyBranch.NANOTECH, 6, "Density-phasing crown built around an artificial Mind socket."),
            d("ULTRON_INFINITY_ARMOR", Material.NETHERITE_CHESTPLATE, "&5Ultron Infinity Armor", TechnologyBranch.COSMIC, 6, "Wearable six-domain synthezoid armor with extreme nanite manifestation."),
            d("ULTRON_INFINITY_WINGS", Material.ELYTRA, "&dInfinity Sentinel Wings", TechnologyBranch.COSMIC, 6, "Hard-light wings formed from autonomous vibranium nanites."),
            d("STARK_SACRIFICE_BUS", Material.REDSTONE_TORCH, "&cStark Sacrifice Bus", TechnologyBranch.NANOTECH, 5, "Cheap universal current path that cannot shield its wearer from a Snap."),
            d("URU_STAR_CRUCIBLE", Material.NETHERITE_BLOCK, "&6Uru Star Crucible", TechnologyBranch.COSMIC, 6, "Contains Uru only while fed by a dying-star collector."),
            d("MOLTEN_STAR_URU", Material.RAW_GOLD_BLOCK, "&6Molten Star-Forged Uru", TechnologyBranch.COSMIC, 6, "Impossible alloy condensed inside a stellar megastructure."),
            d("DYING_STAR_PLASMA", Material.MAGMA_CREAM, "&eDying-Star Plasma", TechnologyBranch.COSMIC, 6, "Magnetically bottled photospheric matter."),
            d("DYSON_SWARM_SEGMENT", Material.DAYLIGHT_DETECTOR, "&eDyson Swarm Segment", TechnologyBranch.COSMIC, 6, "One autonomous collector from a star-enclosing power swarm."),
            d("STELLAR_FLUX_LENS", Material.TINTED_GLASS, "&bStellar Flux Lens", TechnologyBranch.COSMIC, 6, "Focuses a fraction of a star without vaporizing the forge."),
            d("GRAVITON_CONTAINMENT_COIL", Material.BREEZE_ROD, "&bGraviton Containment Coil", TechnologyBranch.NANOTECH, 5, "Bends local inertia inside an owner-bound prison field."),
            d("GOD_PRISON_POWER_CELL", Material.ECHO_SHARD, "&dGod-Prison Power Cell", TechnologyBranch.COSMIC, 6, "Consumable 4 GJ cell; larger protected volumes burn more cells."),
            d("GOD_PRISON_FIELD_CORE", Material.VAULT, "&5God-Prison Field Core", TechnologyBranch.COSMIC, 6, "Right-click inside your ProtectionStone to toggle bounded survival flight."),
            d("TESLA_STEP_EMITTER", Material.LIGHT_WEIGHTED_PRESSURE_PLATE, "&bTesla-Step Emitter", TechnologyBranch.ARC, 5, "Ionizes every airborne step into visible blue-white arcs."),
            d("NANITE_AUREOLE_PROJECTOR", Material.HEART_OF_THE_SEA, "&dNanite Aureole Projector", TechnologyBranch.NANOTECH, 6, "Renders six adaptive orbital domains around advanced armor.")
            ,d("ENCHANT_ARC_AEGIS", Material.ENCHANTED_BOOK, "&bARC Aegis Enchantment", TechnologyBranch.ARC, 6, "Shift-right-click while wearing advanced Stark armor: extreme damage conversion.")
            ,d("ENCHANT_NANITE_REGENESIS", Material.ENCHANTED_BOOK, "&cNanite Regenesis Enchantment", TechnologyBranch.NANOTECH, 6, "Repairs the wearer and rebuilds critical armor from nanomass.")
            ,d("ENCHANT_ZERO_POINT_ANCHOR", Material.ENCHANTED_BOOK, "&dZero-Point Anchor Enchantment", TechnologyBranch.COSMIC, 6, "Cancels one lethal event before a long universal cooldown.")
            ,d("STARK_STORAGE_VAULT", Material.RED_GLAZED_TERRACOTTA, "&cStark Modular Storage Vault", TechnologyBranch.NANOTECH, 4, "54-slot cargo inventory compatible with Slimefun transport and Networks accessors.")
            ,d("STARK_NANO_VAULT", Material.BLACK_GLAZED_TERRACOTTA, "&8Stark Nanomass Vault", TechnologyBranch.NANOTECH, 5, "54-slot programmable-matter storage endpoint for Networks and Slimefun cargo.")
            ,d("STARK_ARMORY_VAULT", Material.LIGHT_GRAY_GLAZED_TERRACOTTA, "&fStark Armory Vault", TechnologyBranch.ARC, 5, "54-slot armor and module depot exposed through standard cargo contracts.")
        );
    }

    private static ContentDefinition d(String id, Material material, String name, TechnologyBranch branch, int tier, String description) {
        return new ContentDefinition(id, material, name, branch, tier, description);
    }

    public static List<MachineDefinition> machines() {
        String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ1Y2Y5MmJjNzllYzE5ZjQxMDY0NDFhZmZmZjE0MDZhMTM2NzAxMGRjYWZiMTk3ZGQ5NGNmY2ExYTZkZTBmYyJ9fX0=";
        return List.of(
            m("CARBYNE_PULVERIZER", "Carbyne Pulverizer", TechnologyBranch.SALVAGED, 1, 256, 2_000_000, 16, "Carbon + reinforced alloy", "Carbyne precursor", "Carbon dust", "Industrial I", "Stops when output is full", texture),
            m("MOLECULAR_SYNTHESIZER", "Molecular Synthesizer", TechnologyBranch.NANOTECH, 2, 1024, 25_000_000, 30, "Carbyne precursor + graphene", "Nanocarbon matrix", "Nanite contamination", "Cleanroom II", "Seals chamber and purges batch", texture),
            m("NANOCELL_PROGRAMMER", "Nanocell Programmer", TechnologyBranch.NANOTECH, 2, 2048, 50_000_000, 45, "Nanocarbon + control circuit", "Programmable nanocell x2", "Logic instability", "Cleanroom II", "Quarantines invalid firmware", texture),
            m("NANOFORGE", "Nanoforge", TechnologyBranch.NANOTECH, 3, 4096, 100_000_000, 60, "Nanocells + exotic alloy", "Suit components", "Heat / nanite contamination", "Cleanroom III", "SCRAM; consumes no ingredients", texture),
            m("ARMOR_ASSEMBLY_BENCH", "Armor Assembly Bench", TechnologyBranch.ARC, 3, 2048, 80_000_000, 40, "Armor frame + ARC core + modules", "Owner-bound suit core", "Stored charge", "Ownership lock", "Rejects mixed-generation modules", texture),
            m("PALLADIUM_WINDER", "Palladium Coil Winder", TechnologyBranch.ARC, 1, 512, 5_000_000, 20, "Palladium + copper wire", "Palladium coil", "Metal fumes", "Ventilation I", "Safe idle; no destructive failure", texture),
            m("NEW_ELEMENT_ACCELERATOR", "New Element Accelerator", TechnologyBranch.ARC, 4, 8192, 250_000_000, 120, "Exotic lattice + singularity", "New element ingot", "Extreme heat", "Containment IV", "ARC SCRAM and visual EMP", texture),
            m("GAMMA_CYCLOTRON", "Gamma Cyclotron", TechnologyBranch.GAMMA, 3, 4096, 120_000_000, 75, "Boss isotope + shielding", "Charged gamma isotope", "Ionizing radiation", "Gamma III", "Drops field and seals isotope", texture),
            m("BANNER_STABILIZATION_CHAMBER", "Banner Stabilization Chamber", TechnologyBranch.GAMMA, 4, 6144, 180_000_000, 90, "Gamma isotope + genetic catalyst", "Controlled mutation serum", "Mutation / radiation", "Gamma IV", "Neutralizes batch into inert waste", texture),
            m("TECHNO_ARCANE_FORGE", "Techno-Arcane Forge", TechnologyBranch.LATVERIAN, 4, 8192, 300_000_000, 100, "ARC component + arcane essence", "Latverian component", "Runic feedback", "Ward IV", "Grounds resonance; never breaks blocks", texture),
            m("COSMIC_SPECTROMETER", "Cosmic Fragment Spectrometer", TechnologyBranch.COSMIC, 5, 16384, 500_000_000, 180, "Unresolved fragment", "Domain signature", "Cosmic exposure", "Containment V", "Ejects sealed capsule", texture),
            m("SINGULARITY_GROWTH_CHAMBER", "Singularity Growth Chamber", TechnologyBranch.COSMIC, 6, 32768, 2_000_000_000, 300, "Domain signature + boss core", "Contained cosmic replica", "Reality collapse", "Containment VI", "Emergency stasis; zero block damage", texture)
            ,m("STELLAR_SWARM_FABRICATOR", "Stellar Swarm Fabricator", TechnologyBranch.COSMIC, 6, 262144, 2_000_000_000, 240, "Nanocells + new element + solar lattice", "Dyson swarm segment", "Coronal induction", "Stellar VI", "Defocuses every lens and grounds collector charge", texture)
            ,m("GRAVITON_FIELD_REGULATOR", "Graviton Field Regulator", TechnologyBranch.COSMIC, 6, 131072, 2_000_000_000, 180, "Graviton coils + prison cells + quantum processor", "God-Prison field core", "Inertial shear", "Owner region VI", "Revokes flight immediately and preserves every block", texture)
            ,m("UNIVERSAL_AUTOMATION_AI", "Universal Automation Intelligence", TechnologyBranch.NANOTECH, 7, 131072, 2_000_000_000, 259200, "Target template + registered recipe materials", "One audited target item", "Long-running state corruption", "Transactional AI VII", "Persists progress and never consumes the target template", texture)
        );
    }

    private static MachineDefinition m(String id, String name, TechnologyBranch branch, int tier, int ept,
                                       int buffer, int seconds, String input, String output, String hazard,
                                       String containment, String shutdown, String texture) {
        return new MachineDefinition(id, name, branch, tier, ept, buffer, seconds, input, output, hazard, containment, shutdown, texture);
    }

    /** Documented structures whose controllers validate shape before processing. */
    public static List<MultiblockDefinition> multiblocks() {
        return List.of(
            mb("STERILE_NANITE_CLEANROOM", "Sterile Nanite Cleanroom", TechnologyBranch.NANOTECH, 3, "7x7x5", "Cleanroom Controller", "48 Cleanroom Walls, 4 filters, sealed door", "8,192 J/t; 250 MJ", "Programs contamination-free nanocells", "Stops and seals the batch if any wall opens"),
            mb("NEW_ELEMENT_ACCELERATOR_ARRAY", "New Element Accelerator Array", TechnologyBranch.ARC, 5, "11x5x5", "New Element Accelerator", "8 ARC Pylons, 24 Vacuum Rings, 2 beam dumps", "65,536 J/t; 4 GJ", "Forges one New Element lattice", "Four-point SCRAM; no explosion and no block mutation"),
            mb("GAMMA_CONTAINMENT_VAULT", "Gamma Containment Vault", TechnologyBranch.GAMMA, 5, "9x9x7", "Gamma Cyclotron", "96 Gamma Shielding Blocks, airlock, absorber crown", "32,768 J/t; 2 GJ", "Stabilizes boss-derived gamma isotopes", "Fails closed and converts the batch to inert waste"),
            mb("HULKBUSTER_ASSEMBLY_GANTRY", "Hulkbuster Assembly Gantry", TechnologyBranch.ARC, 5, "9x9x12", "Armor Assembly Bench", "4 cranes, 8 anchors, 16 servo columns", "16,384 J/t; 1 GJ", "Assembles heavy modular armor frames", "Owner lock, empty deployment volume and emergency brakes"),
            mb("ORBITAL_FABRICATION_SILO", "Orbital Fabrication Silo", TechnologyBranch.NANOTECH, 6, "13x13x15", "Nanoforge", "4 cleanroom decks, 12 power buses, launch aperture", "131,072 J/t; 12 GJ", "Builds Mark LXXXV and War Machine systems", "Aborts on entities in the assembly volume"),
            mb("UNIVERSAL_INFINITY_FORGE", "Universal Infinity Forge", TechnologyBranch.COSMIC, 6, "17x17x9", "Infinity Forge Controller", "6 Focusing Rings, 12 pylons, Snap Matrix, containment dais", "524,288 J/t; 60 GJ", "Sockets six contained domains into one gauntlet", "Protection scan, six-domain SCRAM and zero block damage"),
            mb("DYING_STAR_DYSON_FOUNDRY", "Dying-Star Dyson Foundry", TechnologyBranch.COSMIC, 7, "41x41x29", "Stellar Swarm Fabricator", "48 swarm segments, 12 flux lenses, 8 magnetic crowns, Uru crucible", "2,097,152 J/t; 1.2 TJ", "Melts one batch of star-forged Uru for the safe original gauntlet", "Full SCRAM on lens loss; never ignites, explodes or mutates blocks"),
            mb("GOD_PRISON_GRAVITON_CAGE", "God-Prison Graviton Cage", TechnologyBranch.COSMIC, 6, "ProtectionStone region", "Graviton Field Regulator", "8 coils, 4 Tesla steps, quantum governor, metered power-cell bank", "131,072 J/t base + protected-volume tariff", "Owner-only survival flight: +20/-10 blocks from activation altitude", "Revokes flight outside owner region, altitude envelope or available energy")
        );
    }

    private static MultiblockDefinition mb(String id, String name, TechnologyBranch branch, int tier,
                                            String footprint, String controller, String structure, String power,
                                            String purpose, String safety) {
        return new MultiblockDefinition(id, name, branch, tier, footprint, controller, structure, power, purpose, safety);
    }
}

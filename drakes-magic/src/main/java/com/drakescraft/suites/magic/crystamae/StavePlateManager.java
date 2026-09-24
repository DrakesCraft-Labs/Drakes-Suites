package com.drakescraft.suites.magic.crystamae;

import com.drakescraft.suites.core.pdc.SuiteItemPdcBridge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Gestor de Báculos Arcanos y Placas de Inscripción de Hechizos.
 * Mantiene compatibilidad dual PDC con CrystamaeHistoria legado.
 */
public class StavePlateManager {

    // Claves PDC Modernas (drakes)
    public static final NamespacedKey KEY_STAVE_TIER = new NamespacedKey("drakes", "stv_tier");
    public static final NamespacedKey KEY_PLATE_SPELL = new NamespacedKey("drakes", "p_s");
    public static final NamespacedKey KEY_PLATE_CHARGES = new NamespacedKey("drakes", "p_c");
    public static final NamespacedKey KEY_PLATE_MAX_CHARGES = new NamespacedKey("drakes", "p_mc");

    // Claves PDC Legadas (crystamaehistoria)
    public static final NamespacedKey LEGACY_KEY_PLATE_SPELL = new NamespacedKey("crystamaehistoria", "p_s");
    public static final NamespacedKey LEGACY_KEY_PLATE_CHARGES = new NamespacedKey("crystamaehistoria", "p_c");

    // Cooldown in-memory por jugador y hechizo (UUID -> SpellId -> ExpirationTimestamp)
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public enum CastResult {
        SUCCESS,
        SLOT_EMPTY,
        TIER_LOCKED,
        NO_CHARGES,
        ON_COOLDOWN,
        INVALID_STAVE
    }

    /**
     * Crea una placa mágica inscrita con un hechizo específico.
     */
    @Nonnull
    public static ItemStack createInscribedPlate(@Nonnull SpellDefinition spell) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Placa Mágica: " + spell.getFormattedName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Dominio: " + spell.getStoryType().getColor() + spell.getStoryType().getDisplayName());
            lore.add(ChatColor.GRAY + "Rareza: " + spell.getStoryRarity().getColor() + spell.getStoryRarity().getDisplayName());
            lore.add(ChatColor.GRAY + "Cargas: " + ChatColor.YELLOW + spell.getBaseCharges() + "/" + spell.getBaseCharges());
            lore.add(ChatColor.DARK_GRAY + spell.getDescription());
            meta.setLore(lore);

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            SuiteItemPdcBridge.setSlimefunId(meta, "CRY_PLATE_MAGICAL");
            pdc.set(KEY_PLATE_SPELL, PersistentDataType.STRING, spell.getId());
            pdc.set(KEY_PLATE_CHARGES, PersistentDataType.INTEGER, spell.getBaseCharges());
            pdc.set(KEY_PLATE_MAX_CHARGES, PersistentDataType.INTEGER, spell.getBaseCharges());

            // Legado
            pdc.set(LEGACY_KEY_PLATE_SPELL, PersistentDataType.STRING, spell.getId());
            pdc.set(LEGACY_KEY_PLATE_CHARGES, PersistentDataType.INTEGER, spell.getBaseCharges());

            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Obtiene el hechizo inscrito en una placa mágica.
     */
    @Nullable
    public static String getPlateSpell(@Nullable ItemStack plate) {
        if (plate == null || !plate.hasItemMeta()) return null;
        PersistentDataContainer pdc = plate.getItemMeta().getPersistentDataContainer();
        if (pdc.has(KEY_PLATE_SPELL, PersistentDataType.STRING)) {
            return pdc.get(KEY_PLATE_SPELL, PersistentDataType.STRING);
        }
        if (pdc.has(LEGACY_KEY_PLATE_SPELL, PersistentDataType.STRING)) {
            return pdc.get(LEGACY_KEY_PLATE_SPELL, PersistentDataType.STRING);
        }
        return null;
    }

    /**
     * Obtiene las cargas restantes de una placa mágica.
     */
    public static int getPlateCharges(@Nullable ItemStack plate) {
        if (plate == null || !plate.hasItemMeta()) return 0;
        PersistentDataContainer pdc = plate.getItemMeta().getPersistentDataContainer();
        if (pdc.has(KEY_PLATE_CHARGES, PersistentDataType.INTEGER)) {
            Integer val = pdc.get(KEY_PLATE_CHARGES, PersistentDataType.INTEGER);
            return val != null ? val : 0;
        }
        if (pdc.has(LEGACY_KEY_PLATE_CHARGES, PersistentDataType.INTEGER)) {
            Integer val = pdc.get(LEGACY_KEY_PLATE_CHARGES, PersistentDataType.INTEGER);
            return val != null ? val : 0;
        }
        return 0;
    }

    /**
     * Crea un báculo arcano virgen de tier 1 a 5.
     */
    @Nonnull
    public static ItemStack createStave(int tier) {
        int clampedTier = Math.max(1, Math.min(5, tier));
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ChatColor tierColor = getTierColor(clampedTier);
            meta.setDisplayName(tierColor + "" + ChatColor.BOLD + "Báculo Arcano de Crystamae (Tier " + clampedTier + ")");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Canalizador resonante de historias y conjuros.");
            lore.add(ChatColor.GRAY + "Ranuras desbloqueadas: " + ChatColor.GOLD + clampedTier + "/4");
            lore.add("");
            lore.add(ChatColor.DARK_GRAY + "Ranuras de Conjuro:");
            for (SpellSlot slot : SpellSlot.values()) {
                boolean unlocked = slot.getSlotId() <= clampedTier;
                String status = unlocked ? ChatColor.DARK_GRAY + "Vía disponible" : ChatColor.RED + "Bloqueado";
                lore.add(ChatColor.YELLOW + "✦ " + slot.getDescription() + ": " + status);
            }
            meta.setLore(lore);

            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            SuiteItemPdcBridge.setSlimefunId(meta, "CRY_STAVE_" + clampedTier);
            pdc.set(KEY_STAVE_TIER, PersistentDataType.INTEGER, clampedTier);

            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Determina el tier de un báculo. Retorna 0 si no es báculo.
     */
    public static int getStaveTier(@Nullable ItemStack stave) {
        if (stave == null || !stave.hasItemMeta()) return 0;
        PersistentDataContainer pdc = stave.getItemMeta().getPersistentDataContainer();
        if (pdc.has(KEY_STAVE_TIER, PersistentDataType.INTEGER)) {
            Integer t = pdc.get(KEY_STAVE_TIER, PersistentDataType.INTEGER);
            return t != null ? t : 0;
        }
        String sfId = SuiteItemPdcBridge.getSlimefunId(stave);
        if (sfId != null && sfId.startsWith("CRY_STAVE_")) {
            try {
                return Integer.parseInt(sfId.substring("CRY_STAVE_".length()));
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    /**
     * Vincula un hechizo en una ranura específica del báculo.
     */
    public static boolean bindSpellToStave(@Nonnull ItemStack stave, @Nonnull SpellSlot slot,
                                           @Nonnull SpellDefinition spell, int charges) {
        int tier = getStaveTier(stave);
        if (tier < slot.getSlotId()) {
            return false;
        }

        ItemMeta meta = stave.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey keySpell = new NamespacedKey("drakes", "stv_" + slot.name().toLowerCase() + "_spell");
        NamespacedKey keyCharges = new NamespacedKey("drakes", "stv_" + slot.name().toLowerCase() + "_charges");
        NamespacedKey legacyKeySpell = new NamespacedKey("crystamaehistoria", "stv_" + slot.name().toLowerCase() + "_spell");
        NamespacedKey legacyKeyCharges = new NamespacedKey("crystamaehistoria", "stv_" + slot.name().toLowerCase() + "_charges");

        pdc.set(keySpell, PersistentDataType.STRING, spell.getId());
        pdc.set(keyCharges, PersistentDataType.INTEGER, charges);
        pdc.set(legacyKeySpell, PersistentDataType.STRING, spell.getId());
        pdc.set(legacyKeyCharges, PersistentDataType.INTEGER, charges);

        updateStaveLore(meta, tier);
        stave.setItemMeta(meta);
        return true;
    }

    /**
     * Obtiene el hechizo configurado en una ranura del báculo.
     */
    @Nullable
    public static String getSlotSpell(@Nonnull ItemStack stave, @Nonnull SpellSlot slot) {
        if (!stave.hasItemMeta()) return null;
        PersistentDataContainer pdc = stave.getItemMeta().getPersistentDataContainer();
        NamespacedKey keySpell = new NamespacedKey("drakes", "stv_" + slot.name().toLowerCase() + "_spell");
        if (pdc.has(keySpell, PersistentDataType.STRING)) {
            return pdc.get(keySpell, PersistentDataType.STRING);
        }
        NamespacedKey legacyKeySpell = new NamespacedKey("crystamaehistoria", "stv_" + slot.name().toLowerCase() + "_spell");
        if (pdc.has(legacyKeySpell, PersistentDataType.STRING)) {
            return pdc.get(legacyKeySpell, PersistentDataType.STRING);
        }
        return null;
    }

    /**
     * Obtiene las cargas de una ranura del báculo.
     */
    public static int getSlotCharges(@Nonnull ItemStack stave, @Nonnull SpellSlot slot) {
        if (!stave.hasItemMeta()) return 0;
        PersistentDataContainer pdc = stave.getItemMeta().getPersistentDataContainer();
        NamespacedKey keyCharges = new NamespacedKey("drakes", "stv_" + slot.name().toLowerCase() + "_charges");
        if (pdc.has(keyCharges, PersistentDataType.INTEGER)) {
            Integer val = pdc.get(keyCharges, PersistentDataType.INTEGER);
            return val != null ? val : 0;
        }
        NamespacedKey legacyKeyCharges = new NamespacedKey("crystamaehistoria", "stv_" + slot.name().toLowerCase() + "_charges");
        if (pdc.has(legacyKeyCharges, PersistentDataType.INTEGER)) {
            Integer val = pdc.get(legacyKeyCharges, PersistentDataType.INTEGER);
            return val != null ? val : 0;
        }
        return 0;
    }

    /**
     * Intenta lanzar el hechizo asignado a una ranura específica del báculo.
     */
    public CastResult tryCastSpell(@Nonnull Player player, @Nonnull ItemStack stave, @Nonnull SpellSlot slot) {
        int tier = getStaveTier(stave);
        if (tier <= 0) return CastResult.INVALID_STAVE;
        if (slot.getSlotId() > tier) return CastResult.TIER_LOCKED;

        String spellId = getSlotSpell(stave, slot);
        if (spellId == null) return CastResult.SLOT_EMPTY;

        SpellDefinition def = SpellDefinition.getById(spellId);
        if (def == null) return CastResult.SLOT_EMPTY;

        int charges = getSlotCharges(stave, slot);
        if (charges <= 0) return CastResult.NO_CHARGES;

        // Verificar cooldown
        long now = System.currentTimeMillis();
        Map<String, Long> playerCds = cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        Long expires = playerCds.get(spellId);
        if (expires != null && now < expires) {
            return CastResult.ON_COOLDOWN;
        }

        // Aplicar cooldown (ticks * 50ms)
        long cdMillis = def.getCooldownTicks() * 50L;
        playerCds.put(spellId, now + cdMillis);

        // Descontar carga
        int newCharges = charges - 1;
        bindSpellToStave(stave, slot, def, newCharges);

        return CastResult.SUCCESS;
    }

    public long getRemainingCooldownMillis(@Nonnull UUID playerUuid, @Nonnull String spellId) {
        Map<String, Long> map = cooldowns.get(playerUuid);
        if (map == null) return 0;
        Long expires = map.get(spellId);
        if (expires == null) return 0;
        long diff = expires - System.currentTimeMillis();
        return Math.max(0, diff);
    }

    private static void updateStaveLore(ItemMeta meta, int tier) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Canalizador resonante de historias y conjuros.");
        lore.add(ChatColor.GRAY + "Ranuras desbloqueadas: " + ChatColor.GOLD + tier + "/4");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "Ranuras de Conjuro:");

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        for (SpellSlot slot : SpellSlot.values()) {
            if (slot.getSlotId() > tier) {
                lore.add(ChatColor.DARK_GRAY + "✦ " + slot.getDescription() + ": " + ChatColor.RED + "Bloqueado");
                continue;
            }
            NamespacedKey keySpell = new NamespacedKey("drakes", "stv_" + slot.name().toLowerCase() + "_spell");
            String spId = pdc.get(keySpell, PersistentDataType.STRING);
            if (spId == null) {
                lore.add(ChatColor.YELLOW + "✦ " + slot.getDescription() + ": " + ChatColor.GRAY + "Vacío");
            } else {
                SpellDefinition def = SpellDefinition.getById(spId);
                NamespacedKey keyCharges = new NamespacedKey("drakes", "stv_" + slot.name().toLowerCase() + "_charges");
                Integer ch = pdc.get(keyCharges, PersistentDataType.INTEGER);
                int charges = ch != null ? ch : 0;
                String spellName = def != null ? def.getName() : spId;
                lore.add(ChatColor.YELLOW + "✦ " + slot.getDescription() + ": " + ChatColor.AQUA + spellName
                        + ChatColor.GRAY + " (" + ChatColor.YELLOW + charges + ChatColor.GRAY + " cargas)");
            }
        }
        meta.setLore(lore);
    }

    private static ChatColor getTierColor(int tier) {
        return switch (tier) {
            case 1 -> ChatColor.WHITE;
            case 2 -> ChatColor.GREEN;
            case 3 -> ChatColor.BLUE;
            case 4 -> ChatColor.DARK_PURPLE;
            case 5 -> ChatColor.GOLD;
            default -> ChatColor.GRAY;
        };
    }
}

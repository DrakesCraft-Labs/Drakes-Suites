package com.drakescraft.suites.combat.tinker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Motor de ensamblaje, forja y aplicación de modificadores de herramientas para SlimeTinker.
 * Regula el límite de modificadores por herramienta y protege contra duplicaciones de traits.
 */
public class TinkerToolAssemblyEngine {

    private final int maxModifiersPerTool;
    private final boolean allowInfinityMaterials;

    public TinkerToolAssemblyEngine(int maxModifiersPerTool, boolean allowInfinityMaterials) {
        this.maxModifiersPerTool = Math.max(1, maxModifiersPerTool);
        this.allowInfinityMaterials = allowInfinityMaterials;
    }

    /**
     * Valida si se puede ensamblar una herramienta con las partes proporcionadas.
     *
     * @param headPart Material del cabezal (ej. "COBALT", "ARDITE", "INFINITY")
     * @param handlePart Material del mango (ej. "WOOD", "SLIME", "STEEL")
     * @param bindingPart Material de la unión (ej. "STRING", "LEATHER", "CHAIN")
     * @return true si la combinación es válida
     */
    public boolean canAssembleTool(String headPart, String handlePart, String bindingPart) {
        if (headPart == null || handlePart == null || bindingPart == null) {
            return false;
        }

        if (headPart.equalsIgnoreCase("INFINITY") && !allowInfinityMaterials) {
            return false;
        }

        return !headPart.isBlank() && !handlePart.isBlank() && !bindingPart.isBlank();
    }

    /**
     * Intenta aplicar un modificador a una herramienta existente con una lista de modificadores.
     *
     * @param currentModifiers Modificadores actuales
     * @param newModifier Modificador que se desea agregar
     * @return Lista inmutable resultante con el nuevo modificador, o null si se excede el límite
     */
    public List<TinkerModifier> applyModifier(List<TinkerModifier> currentModifiers, TinkerModifier newModifier) {
        Objects.requireNonNull(newModifier, "newModifier cannot be null");
        List<TinkerModifier> list = currentModifiers == null ? new ArrayList<>() : new ArrayList<>(currentModifiers);

        // Si ya contiene el trait, se busca para mejorar nivel o rechazar si está en max level
        for (int i = 0; i < list.size(); i++) {
            TinkerModifier existing = list.get(i);
            if (existing.getTrait() == newModifier.getTrait()) {
                int upgradedLevel = Math.min(10, existing.getLevel() + newModifier.getLevel());
                list.set(i, new TinkerModifier(existing.getTrait(), upgradedLevel, newModifier.getActivationChance()));
                return Collections.unmodifiableList(list);
            }
        }

        // Si es un trait nuevo, verificar si no supera el límite de modificadores
        if (list.size() >= maxModifiersPerTool) {
            return null; // Límite de slots alcanzado
        }

        list.add(newModifier);
        return Collections.unmodifiableList(list);
    }

    public int getMaxModifiersPerTool() {
        return maxModifiersPerTool;
    }

    public boolean isAllowInfinityMaterials() {
        return allowInfinityMaterials;
    }
}

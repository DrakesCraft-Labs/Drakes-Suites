package com.drakescraft.suites.utility.trash;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Holder marcador exclusivo para la papelera de reciclaje de DrakesUtility.
 * Evita confusiones o exploits con otros inventarios o cofres.
 */
public class TrashHolder implements InventoryHolder {

    private Inventory inventory;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}

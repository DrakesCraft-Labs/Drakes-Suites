package com.drakescraft.suites.core;

import com.drakescraft.suites.core.compat.CrossVersionAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrossVersionAdapterTest {

    private static ServerMock server;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("CrossVersionAdapter maneja componentes y mensajes sin excepciones")
    void testComponentParsing() {
        Component compLegacy = CrossVersionAdapter.parseComponent("&aTexto &bVerde");
        assertNotNull(compLegacy);

        Component compMini = CrossVersionAdapter.parseComponent("<gold>Texto Dorado");
        assertNotNull(compMini);

        PlayerMock player = server.addPlayer();
        assertDoesNotThrow(() -> CrossVersionAdapter.sendMessage(player, "&6[DrakesCraft] &eBienvenido"));
        assertDoesNotThrow(() -> CrossVersionAdapter.sendActionBar(player, "<green>Barra Activa"));
    }

    @Test
    @DisplayName("CrossVersionAdapter maneja ItemMeta, nombres y lore de forma retrocompatible")
    void testItemMetaAdapters() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        CrossVersionAdapter.setItemName(item, "<gradient:gold:yellow>Espada del Olimpo</gradient>");
        CrossVersionAdapter.setItemLore(item, List.of("&7Filo de los dioses", "&eTier X"));
        CrossVersionAdapter.setCustomModelData(item, 10025);

        assertNotNull(item.getItemMeta());
        assertEquals(Integer.valueOf(10025), CrossVersionAdapter.getCustomModelData(item));
    }
}

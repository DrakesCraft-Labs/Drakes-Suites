package com.drakescraft.suites.utility.backpacks;

import org.bukkit.Material;

/**
 * Representa los 16 colores canónicos para el módulo de mochilas tintadas (DyedBackpacks).
 * Mantiene compatibilidad exacta de texturas (skin hashes) con la producción de Slimefun.
 */
public enum BackpackColor {
    WHITE("Blanco", "<white>Blanco</white>", Material.WHITE_WOOL, Material.WHITE_DYE, "ebdf8d53bdb932c223c627bbb8c1e0c5e351a616cd8056929c66e6dce44433db"),
    ORANGE("Naranja", "<gold>Naranja</gold>", Material.ORANGE_WOOL, Material.ORANGE_DYE, "a37a35522f67b2af92345592846b702b9afb9d7c8dbad5ea150673c9e44de3"),
    MAGENTA("Magenta", "<light_purple>Magenta</light_purple>", Material.MAGENTA_WOOL, Material.MAGENTA_DYE, "36575fcccadae87c0842f53de5e0ffa75851696866d81e1b72828348db5256"),
    LIGHT_BLUE("Celeste", "<aqua>Celeste</aqua>", Material.LIGHT_BLUE_WOOL, Material.LIGHT_BLUE_DYE, "a3c153c391c34e2d328a60839e683a9f82ad3048299d8bc6a39e6f915cc5a"),
    YELLOW("Amarillo", "<yellow>Amarillo</yellow>", Material.YELLOW_WOOL, Material.YELLOW_DYE, "a254aacbf623175ff98df7ae366e0b89e91713441752f3cdf965f038b174b5"),
    LIME("Lima", "<green>Lima</green>", Material.LIME_WOOL, Material.LIME_DYE, "a9909a9779b946b9787442fa483af4de4b2f19fd40dc2370f7a9b8f521f21ddc"),
    PINK("Rosa", "<light_purple>Rosa</light_purple>", Material.PINK_WOOL, Material.PINK_DYE, "bddafdcb1a8df426229d7879b1e4a336fc9ab3bdc146bb4ed3be4bbf7b5b835"),
    DARK_GRAY("Gris Oscuro", "<dark_gray>Gris Oscuro</dark_gray>", Material.GRAY_WOOL, Material.GRAY_DYE, "6536ad978e1ce5050f43b7a6b3859eb49406b4f1043802a711cdc80c090c35d"),
    LIGHT_GRAY("Gris Claro", "<gray>Gris Claro</gray>", Material.LIGHT_GRAY_WOOL, Material.LIGHT_GRAY_DYE, "5a5fc7635296ca183fd30b0fb5f4c18cfc216768f0fbebb865e0211ab43b7b"),
    CYAN("Cian", "<dark_aqua>Cian</dark_aqua>", Material.CYAN_WOOL, Material.CYAN_DYE, "df70fab3246fe027ce0bba885a73c6e82d8ff8f358231e8461f956560cfa58f"),
    PURPLE("Púrpura", "<dark_purple>Púrpura</dark_purple>", Material.PURPLE_WOOL, Material.PURPLE_DYE, "5eb65bbe744945841e9234a33b5ce5cc236f6a2fc93a1a3ae42df77c9084df1e"),
    BLUE("Azul", "<blue>Azul</blue>", Material.BLUE_WOOL, Material.BLUE_DYE, "8224b2c7391eb5bfcb278431d5c827cb26349526c7bc535b1e95f6df9f3fdf3"),
    BROWN("Marrón", "<gold>Marrón</gold>", Material.BROWN_WOOL, Material.BROWN_DYE, "efb6a3d7dba97bb6e7f79a15627aec6369791233f833fa749ef21bed79e59e98"),
    GREEN("Verde", "<dark_green>Verde</dark_green>", Material.GREEN_WOOL, Material.GREEN_DYE, "28a127f1cfd79986e7bd95d92de4f4f68040e4f899f81b1f8f3ca15b64f50f3"),
    RED("Rojo", "<dark_red>Rojo</dark_red>", Material.RED_WOOL, Material.RED_DYE, "85e4f9da68c81fa481eecdca48a138cecde2cddffeeae84ab1afd24a363e028"),
    BLACK("Negro", "<dark_gray>Negro</dark_gray>", Material.BLACK_WOOL, Material.BLACK_DYE, "a9ab1fdcbe878d1e55bdd43cebc5e43836a6da69541f4a233fe88f1305668");

    private final String rawName;
    private final String formattedName;
    private final Material woolMaterial;
    private final Material dyeMaterial;
    private final String textureHash;

    BackpackColor(String rawName, String formattedName, Material woolMaterial, Material dyeMaterial, String textureHash) {
        this.rawName = rawName;
        this.formattedName = formattedName;
        this.woolMaterial = woolMaterial;
        this.dyeMaterial = dyeMaterial;
        this.textureHash = textureHash;
    }

    public String getRawName() {
        return rawName;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public Material getWoolMaterial() {
        return woolMaterial;
    }

    public Material getDyeMaterial() {
        return dyeMaterial;
    }

    public String getTextureHash() {
        return textureHash;
    }
}

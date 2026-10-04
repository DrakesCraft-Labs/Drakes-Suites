package com.chagui68.multiversenets.persist;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeStoreEncodingTest {

    @Test
    void compressedEncodingKeepsLargeNodePayloadBelowNbtUtfLimit() {
        NodeBlob blob = NodeBlob.create("MVN_CRAFTER");
        blob.blueprintData = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            blob.blueprintData.add("recipe=" + i + ";ingredient=" + "minecraft:diamond;".repeat(96));
        }

        String encoded = NodeStore.encode(blob);

        assertTrue(encoded.length() < 65_535, "compressed node PDC must fit writeUTF");
        assertEquals(blob.blueprintData, NodeStore.decode(encoded).blueprintData);
    }
}

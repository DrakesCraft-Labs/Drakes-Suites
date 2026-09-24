package com.drakescraft.suites.utility.slimehud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Representa la información contextual de una máquina de Slimefun observada por un jugador.
 */
public record HudMachineInfo(
        String machineId,
        String displayName,
        int energyStored,
        int energyCapacity,
        double progressPercentage,
        String statusDescription
) {

    public Component toComponent() {
        StringBuilder sb = new StringBuilder();
        sb.append("<gradient:#FFD700:#FFA500><bold>").append(displayName).append("</bold></gradient>");

        if (energyCapacity > 0) {
            sb.append(" <gray>│</gray> <green>⚡ ")
                    .append(energyStored)
                    .append("/")
                    .append(energyCapacity)
                    .append(" J</green>");
        }

        if (progressPercentage >= 0.0) {
            int pct = (int) Math.round(progressPercentage * 100.0);
            sb.append(" <gray>│</gray> <aqua>⚙ ")
                    .append(pct)
                    .append("%</aqua>");
        }

        if (statusDescription != null && !statusDescription.isBlank()) {
            sb.append(" <gray>│</gray> <yellow>")
                    .append(statusDescription)
                    .append("</yellow>");
        }

        return MiniMessage.miniMessage().deserialize(sb.toString());
    }

    public String toPlainText() {
        return displayName + (energyCapacity > 0 ? " | " + energyStored + "/" + energyCapacity + " J" : "");
    }
}

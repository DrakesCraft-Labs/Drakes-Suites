package com.drakescraft.suites.core.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Gateway de control y restricción de comandos por modalidad/mundo.
 * Asegura que comandos como /sf no se ejecuten en Clásico, o /is no se ejecute en OneBlock.
 */
public class CommandModalityGate implements Listener {

    public static class ModalityRule {
        private final String name;
        private final List<Pattern> worldPatterns = new ArrayList<>();
        private final List<String> blockedCommands = new ArrayList<>();
        private final String blockedMessage;

        public ModalityRule(String name, List<String> regexes, List<String> blockedCommands, String blockedMessage) {
            this.name = name;
            if (regexes != null) {
                for (String regex : regexes) {
                    try {
                        this.worldPatterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
                    } catch (Exception ignored) {
                    }
                }
            }
            if (blockedCommands != null) {
                for (String cmd : blockedCommands) {
                    if (cmd != null && !cmd.trim().isEmpty()) {
                        String clean = cmd.trim().toLowerCase(Locale.ROOT);
                        if (!clean.startsWith("/")) {
                            clean = "/" + clean;
                        }
                        this.blockedCommands.add(clean);
                    }
                }
            }
            this.blockedMessage = blockedMessage != null ? blockedMessage : "<red>❌ Este comando no está disponible en esta modalidad.</red>";
        }

        public boolean matchesWorld(String worldName) {
            if (worldName == null) return false;
            for (Pattern pattern : worldPatterns) {
                if (pattern.matcher(worldName).matches()) {
                    return true;
                }
            }
            return false;
        }

        public boolean isCommandBlocked(String commandLine) {
            if (commandLine == null) return false;
            String lower = commandLine.trim().toLowerCase(Locale.ROOT);
            for (String blocked : blockedCommands) {
                if (lower.equals(blocked) || lower.startsWith(blocked + " ")) {
                    return true;
                }
            }
            return false;
        }

        public String getName() {
            return name;
        }

        public String getBlockedMessage() {
            return blockedMessage;
        }

        public List<String> getBlockedCommands() {
            return Collections.unmodifiableList(blockedCommands);
        }
    }

    private final List<ModalityRule> rules = new ArrayList<>();
    private boolean enabled = true;

    public CommandModalityGate() {
    }

    public void reload(FileConfiguration config) {
        rules.clear();
        if (config == null) {
            return;
        }

        this.enabled = config.getBoolean("command-gate.enabled", true);
        ConfigurationSection section = config.getConfigurationSection("command-gate.rules");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection ruleSec = section.getConfigurationSection(key);
                if (ruleSec != null) {
                    List<String> patterns = ruleSec.getStringList("world-patterns");
                    List<String> blocked = ruleSec.getStringList("blocked-commands");
                    String msg = ruleSec.getString("blocked-message");
                    rules.add(new ModalityRule(key, patterns, blocked, msg));
                }
            }
        }
    }

    public void addRule(ModalityRule rule) {
        if (rule != null) {
            rules.add(rule);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!enabled) {
            return;
        }

        Player player = event.getPlayer();
        if (player.hasPermission("drakescore.commandgate.bypass")) {
            return;
        }

        String worldName = player.getWorld().getName();
        String message = event.getMessage();

        for (ModalityRule rule : rules) {
            if (rule.matchesWorld(worldName)) {
                if (rule.isCommandBlocked(message)) {
                    event.setCancelled(true);
                    String primaryCmd = message.split(" ")[0];
                    String response = rule.getBlockedMessage()
                            .replace("{command}", primaryCmd)
                            .replace("{modality}", rule.getName());
                    player.sendMessage(MiniMessage.miniMessage().deserialize(response));
                    return;
                }
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<ModalityRule> getRules() {
        return Collections.unmodifiableList(rules);
    }
}

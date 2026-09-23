package com.drakescraft.suites.core.logging;

import com.drakescraft.suites.core.DrakesCorePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Gestor de Logs Aislados y Auditoria de Seguridad para modulos y suites.
 * Garantiza que cada addon/suite tenga su propio archivo de log rotativo
 * para trazabilidad forense de errores, seguridad, bugs y duplicaciones (dupes).
 */
public final class SuiteAuditLogger {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final File logsFolder;
    private final Map<String, PrintWriter> writers = new ConcurrentHashMap<>();
    private final ExecutorService logExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Drakes-Audit-Logger");
        t.setDaemon(true);
        return t;
    });

    public SuiteAuditLogger(JavaPlugin plugin) {
        this.logsFolder = new File(plugin.getDataFolder(), "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
    }

    private PrintWriter getOrCreateWriter(String suite, String module) {
        String key = suite + "_" + module;
        return writers.computeIfAbsent(key, k -> {
            try {
                File suiteDir = new File(logsFolder, suite);
                if (!suiteDir.exists()) suiteDir.mkdirs();
                File logFile = new File(suiteDir, module + "-audit.log");
                return new PrintWriter(new FileWriter(logFile, true), true);
            } catch (IOException e) {
                return null;
            }
        });
    }

    private void writeEntry(String suite, String module, String level, String message) {
        logExecutor.submit(() -> {
            PrintWriter pw = getOrCreateWriter(suite, module);
            if (pw != null) {
                String timestamp = DATE_FORMAT.format(new Date());
                pw.println("[" + timestamp + "] [" + level + "] " + message);
            }
        });
    }

    public void info(String suite, String module, String message) {
        writeEntry(suite, module, "INFO", message);
    }

    public void warn(String suite, String module, String message) {
        writeEntry(suite, module, "WARN", message);
    }

    public void error(String suite, String module, String message, Throwable t) {
        writeEntry(suite, module, "ERROR", message + (t != null ? " - " + t.getMessage() : ""));
        if (t != null) {
            logExecutor.submit(() -> {
                PrintWriter pw = getOrCreateWriter(suite, module);
                if (pw != null) {
                    t.printStackTrace(pw);
                }
            });
        }
    }

    public void security(String suite, String module, String player, String action, String details) {
        String msg = "[SECURITY-ALERT] Jugador: " + player + " | Accion: " + action + " | Detalles: " + details;
        writeEntry(suite, module, "SECURITY", msg);

        DrakesCorePlugin core = DrakesCorePlugin.getInstance();
        if (core != null && core.getDatabaseEngine() != null) {
            core.getDatabaseEngine().recordAuditEvent(suite, module, "SECURITY", player, "", "", 0, action + ": " + details);
        }
    }

    public void dupeAttempt(String suite, String module, String player, String location, String item, int amount, String reason) {
        String msg = "[DUPE-GUARD] INVENTARIO/RED INTERCEPTADO -> Jugador: " + player + " | Loc: " + location + " | Item: " + item + " x" + amount + " | Causa: " + reason;
        writeEntry(suite, module, "DUPE_GUARD", msg);

        // Registro permanente en la base de datos WAL
        DrakesCorePlugin core = DrakesCorePlugin.getInstance();
        if (core != null && core.getDatabaseEngine() != null) {
            core.getDatabaseEngine().recordAuditEvent(suite, module, "DUPE_ATTEMPT", player, location, item, amount, reason);
        }
    }

    public void close() {
        logExecutor.shutdown();
        for (PrintWriter pw : writers.values()) {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
        writers.clear();
    }
}

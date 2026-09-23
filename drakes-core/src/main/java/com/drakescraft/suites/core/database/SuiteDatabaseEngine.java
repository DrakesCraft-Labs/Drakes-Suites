package com.drakescraft.suites.core.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Motor centralizado de Base de Datos SQLite WAL para todo el ecosistema Drakes-Suites.
 * Consolida el almacenamiento de estados de maquinas, almacenamiento cuantico y auditorias
 * en una conexion asincrona de alto rendimiento sin pausas de GC ni bloqueos en el hilo principal.
 */
public final class SuiteDatabaseEngine {

    private final JavaPlugin plugin;
    private final File dbFile;
    private Connection connection;
    private final BlockingQueue<DatabaseTask> writeQueue = new LinkedBlockingQueue<>(50000);
    private volatile boolean running = false;
    private Thread workerThread;

    @FunctionalInterface
    public interface DatabaseTask {
        void execute(Connection conn) throws SQLException;
    }

    public SuiteDatabaseEngine(JavaPlugin plugin) {
        this.plugin = plugin;
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        this.dbFile = new File(dataDir, "drakes-suites.db");
    }

    public synchronized void start() {
        if (running) return;
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            
            // Configurar SQLite WAL para maxima concurrencia y cero pausas de I/O
            try (Statement stmt = this.connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode = WAL;");
                stmt.execute("PRAGMA synchronous = NORMAL;");
                stmt.execute("PRAGMA temp_store = MEMORY;");
                stmt.execute("PRAGMA cache_size = -64000;"); // 64MB cache en memoria
                stmt.execute("PRAGMA busy_timeout = 5000;");
            }

            initializeTables();
            this.running = true;

            // Iniciar hilo de persistencia asincrona por lotes (Batch Flusher)
            this.workerThread = new Thread(this::flushLoop, "Drakes-Suites-DB-Worker");
            this.workerThread.setDaemon(true);
            this.workerThread.start();

            plugin.getLogger().info("§a[DrakesDatabase] Motor SQLite WAL unificado iniciado en: " + dbFile.getName());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "§c[DrakesDatabase] Error critico al inicializar base de datos SQLite WAL", e);
        }
    }

    private void initializeTables() throws SQLException {
        try (Statement stmt = this.connection.createStatement()) {
            // Tabla de estados de bloques y maquinas (DrakesTech, DrakesGenerators, etc.)
            stmt.execute("CREATE TABLE IF NOT EXISTS suite_block_states (" +
                    "world TEXT NOT NULL, " +
                    "x INTEGER NOT NULL, " +
                    "y INTEGER NOT NULL, " +
                    "z INTEGER NOT NULL, " +
                    "suite TEXT NOT NULL, " +
                    "module TEXT NOT NULL, " +
                    "slimefun_id TEXT NOT NULL, " +
                    "state_json TEXT, " +
                    "updated_at INTEGER NOT NULL, " +
                    "PRIMARY KEY (world, x, y, z)" +
                    ");");

            // Tabla de almacenamiento masivo cuantico e inventarios
            stmt.execute("CREATE TABLE IF NOT EXISTS suite_quantum_storage (" +
                    "storage_id TEXT NOT NULL PRIMARY KEY, " +
                    "owner_uuid TEXT, " +
                    "item_id TEXT NOT NULL, " +
                    "stored_amount INTEGER NOT NULL, " +
                    "capacity INTEGER NOT NULL, " +
                    "updated_at INTEGER NOT NULL" +
                    ");");

            // Tabla de auditoria de seguridad, transferencias y deteccion de dupes
            stmt.execute("CREATE TABLE IF NOT EXISTS suite_audit_events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "timestamp INTEGER NOT NULL, " +
                    "suite TEXT NOT NULL, " +
                    "module TEXT NOT NULL, " +
                    "event_type TEXT NOT NULL, " +
                    "player_uuid TEXT, " +
                    "location TEXT, " +
                    "item_id TEXT, " +
                    "amount INTEGER, " +
                    "details TEXT" +
                    ");");

            stmt.execute("CREATE INDEX IF NOT EXISTS idx_audit_player ON suite_audit_events(player_uuid);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_audit_time ON suite_audit_events(timestamp);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_block_suite ON suite_block_states(suite, module);");
        }
    }

    private void flushLoop() {
        while (running || !writeQueue.isEmpty()) {
            try {
                DatabaseTask task = writeQueue.poll(500, TimeUnit.MILLISECONDS);
                if (task != null && connection != null && !connection.isClosed()) {
                    connection.setAutoCommit(false);
                    int count = 0;
                    try {
                        task.execute(connection);
                        count++;
                        // Drenar lote de tareas pendientes para un unico commit
                        while (count < 200) {
                            DatabaseTask next = writeQueue.poll();
                            if (next == null) break;
                            next.execute(connection);
                            count++;
                        }
                        connection.commit();
                    } catch (SQLException e) {
                        connection.rollback();
                        plugin.getLogger().log(Level.WARNING, "[DrakesDatabase] Fallo en transaccion por lotes, rollback ejecutado", e);
                    } finally {
                        connection.setAutoCommit(true);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "[DrakesDatabase] Error inesperado en hilo de escritura", e);
            }
        }
    }

    /**
     * Encola una tarea de escritura asincrona sin bloquear el servidor.
     */
    public void executeAsync(DatabaseTask task) {
        if (!running) return;
        if (!writeQueue.offer(task)) {
            plugin.getLogger().warning("[DrakesDatabase] Cola de escritura llena (>50k). Descartando para proteger RAM.");
        }
    }

    /**
     * Registra un evento de seguridad o posible dupe de manera asincrona.
     */
    public void recordAuditEvent(String suite, String module, String eventType, String playerUuid, String location, String itemId, int amount, String details) {
        executeAsync(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO suite_audit_events (timestamp, suite, module, event_type, player_uuid, location, item_id, amount, details) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setLong(1, System.currentTimeMillis());
                ps.setString(2, suite);
                ps.setString(3, module);
                ps.setString(4, eventType);
                ps.setString(5, playerUuid);
                ps.setString(6, location);
                ps.setString(7, itemId);
                ps.setInt(8, amount);
                ps.setString(9, details);
                ps.executeUpdate();
            }
        });
    }

    public synchronized void stop() {
        this.running = false;
        if (workerThread != null) {
            try {
                workerThread.join(3000);
            } catch (InterruptedException ignored) {}
        }
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ignored) {}
        }
        plugin.getLogger().info("[DrakesDatabase] Base de datos SQLite cerrada limpiamente.");
    }
}

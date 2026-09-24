package com.drakescraft.suites.core.runtime;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proveedor de deteccion y puente en tiempo de ejecucion para Purpur 26.2 y Paper.
 * 
 * Permite a las suites (DrakesCombat, DrakesBio, DrakesTech, DrakesServer) aprovechar
 * las capacidades avanzadas de Purpur (IA de entidades sin NMS, Ridable Mobs, optimizaciones
 * de red y lag throttling) cuando este presente, manteniendo compatibilidad total y fallback
 * seguro en Paper 1.21.11+.
 * 
 * Autor: JackStar (JackStar6677-1)
 */
public final class PurpurRuntimeProvider {

    private static final Logger LOGGER = Logger.getLogger("DrakesSuites-Purpur");

    private static final boolean IS_PURPUR;
    private static final boolean IS_PAPER;
    private static final String SERVER_BRAND;
    private static final String SERVER_VERSION;

    private static final boolean SUPPORTS_CUSTOM_MOB_GOALS;
    private static final boolean SUPPORTS_RIDABLE_ENTITIES;
    private static final boolean SUPPORTS_LAG_THROTTLING;

    // Cache de metodos reflectivos para Purpur
    private static Method SET_RIDABLE_METHOD;

    static {
        Server server = Bukkit.getServer();
        String brand = "Unknown";
        String version = "Unknown";

        if (server != null) {
            brand = server.getName() != null ? server.getName() : "Unknown";
            version = server.getVersion() != null ? server.getVersion() : "Unknown";
        }

        SERVER_BRAND = brand;
        SERVER_VERSION = version;

        // Deteccion de Purpur por clase y marca
        boolean purpurDetected = false;
        try {
            Class.forName("org.purpurmc.purpur.PurpurConfig");
            purpurDetected = true;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("org.purpurmc.purpur.event.entity.RidableMoveEvent");
                purpurDetected = true;
            } catch (ClassNotFoundException ignored) {
                if (brand.toLowerCase().contains("purpur")) {
                    purpurDetected = true;
                }
            }
        }
        IS_PURPUR = purpurDetected;

        // Deteccion de Paper
        boolean paperDetected = false;
        try {
            Class.forName("io.papermc.paper.configuration.Configuration");
            paperDetected = true;
        } catch (ClassNotFoundException e) {
            if (brand.toLowerCase().contains("paper") || IS_PURPUR) {
                paperDetected = true;
            }
        }
        IS_PAPER = paperDetected;

        // Deteccion de Ridable Entities de Purpur
        boolean ridableSupported = false;
        try {
            // En Purpur, org.bukkit.entity.Entity o subinterfaces exponen setRidable(boolean)
            for (Method m : Entity.class.getMethods()) {
                if ("setRidable".equals(m.getName()) && m.getParameterCount() == 1) {
                    SET_RIDABLE_METHOD = m;
                    ridableSupported = true;
                    break;
                }
            }
        } catch (Throwable ignored) {
            ridableSupported = false;
        }
        SUPPORTS_RIDABLE_ENTITIES = ridableSupported;

        // Mob Goals de Paper / Purpur
        boolean mobGoals = false;
        try {
            Class.forName("com.destroystokyo.paper.entity.ai.MobGoals");
            mobGoals = true;
        } catch (ClassNotFoundException ignored) {
            mobGoals = false;
        }
        SUPPORTS_CUSTOM_MOB_GOALS = mobGoals;

        // Lag Throttling / TPS granular
        SUPPORTS_LAG_THROTTLING = IS_PURPUR;
    }

    private PurpurRuntimeProvider() {}

    /**
     * @return true si el servidor en ejecucion es Purpur
     */
    public static boolean isPurpur() {
        return IS_PURPUR;
    }

    /**
     * @return true si el servidor en ejecucion es Paper o un derivado (incluyendo Purpur)
     */
    public static boolean isPaper() {
        return IS_PAPER;
    }

    public static String getServerBrand() {
        return SERVER_BRAND;
    }

    public static String getServerVersion() {
        return SERVER_VERSION;
    }

    public static boolean supportsCustomMobGoals() {
        return SUPPORTS_CUSTOM_MOB_GOALS;
    }

    public static boolean supportsRidableEntities() {
        return SUPPORTS_RIDABLE_ENTITIES;
    }

    public static boolean supportsLagThrottling() {
        return SUPPORTS_LAG_THROTTLING;
    }

    /**
     * Hace que una entidad sea montable y controlable por jugadores si el motor es Purpur.
     * En servidores Paper/Spigot, realiza una operacion segura sin error.
     *
     * @param entity la entidad a modificar
     * @param ridable true para habilitar montura
     * @return true si fue aplicado nativamente por Purpur
     */
    public static boolean setEntityRidable(Entity entity, boolean ridable) {
        if (entity == null) return false;
        if (SET_RIDABLE_METHOD != null) {
            try {
                SET_RIDABLE_METHOD.invoke(entity, ridable);
                return true;
            } catch (Throwable t) {
                LOGGER.log(Level.FINE, "No se pudo invocar setRidable en {0}: {1}", new Object[]{entity.getType(), t.getMessage()});
            }
        }
        return false;
    }

    /**
     * Obtiene el TPS reciente (1m, 5m, 15m) de forma segura.
     *
     * @return arreglo de 3 doubles [1m, 5m, 15m], o [20.0, 20.0, 20.0] si no esta disponible.
     */
    public static double[] getRecentTps() {
        try {
            Server server = Bukkit.getServer();
            if (server != null) {
                Method tpsMethod = server.getClass().getMethod("getTPS");
                return (double[]) tpsMethod.invoke(server);
            }
        } catch (Throwable ignored) {}
        return new double[]{20.0, 20.0, 20.0};
    }

    /**
     * Obtiene el tiempo medio por tick en milisegundos (MSPT).
     *
     * @return mspt promedio o 50.0 si es desconocido.
     */
    public static double getAverageTickTimeMs() {
        try {
            Server server = Bukkit.getServer();
            if (server != null) {
                Method msptMethod = server.getClass().getMethod("getAverageTickTime");
                Object val = msptMethod.invoke(server);
                if (val instanceof Number num) {
                    return num.doubleValue();
                }
            }
        } catch (Throwable ignored) {}
        return 50.0;
    }

    /**
     * Emite un informe de diagnostico completo de la plataforma en los logs del servidor.
     */
    public static void logRuntimeDiagnostics(Logger logger) {
        if (logger == null) logger = LOGGER;
        String javaVer = System.getProperty("java.version", "desconocida");
        String platform = IS_PURPUR ? "Purpur (Next-Gen Optimizado)" : (IS_PAPER ? "Paper (Estandar)" : "Bukkit/Spigot");

        logger.info("==========================================================");
        logger.info(" 🏛️ [StarSuites-Runtime] Telemetria de Plataforma:");
        logger.info("  • Motor detectado:   " + platform + " (" + SERVER_BRAND + ")");
        logger.info("  • Version Minecraft: " + SERVER_VERSION);
        logger.info("  • Runtime Java:      " + javaVer + " (LTS)");
        logger.info("  • IA Mob Goals:      " + (SUPPORTS_CUSTOM_MOB_GOALS ? "ACTIVO (Paper/Purpur API)" : "DESHABILITADO"));
        logger.info("  • Mobs Montables:    " + (SUPPORTS_RIDABLE_ENTITIES ? "ACTIVO (Purpur Native)" : "FALLBACK SEGURO"));
        logger.info("  • Lag Throttling:    " + (SUPPORTS_LAG_THROTTLING ? "ACTIVO (Purpur Engine)" : "MODO PAPER"));
        logger.info("==========================================================");
    }
}

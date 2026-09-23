package com.drakescraft.suites.core.nativeengine;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Motor de aceleracion nativa JNI para conectar las Mega-Suites de DrakesCraft
 * con las librerias Rust off-heap:
 * - libslimefun_ffi.so (calculo de energia SIMD, suma saturada, precios de mercado)
 * - libodysseia_ffi.so (evaluacion de auras 3D de bosses, redstone clock guard)
 * 
 * Totalmente compatible con Java 21 LTS estandar con fallback gracil en Java.
 */
public final class NativeEngineBridge {

    private static final Logger LOGGER = Logger.getLogger("DrakesSuites-Native");
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static volatile boolean nativeAvailable = false;

    private NativeEngineBridge() {}

    public static synchronized void initialize(Path dataFolder) {
        if (INITIALIZED.getAndSet(true)) {
            return;
        }

        try {
            String os = System.getProperty("os.name", "").toLowerCase();
            if (!os.contains("linux")) {
                LOGGER.info("[DrakesCore-Rust] Entorno no Linux detectado; operando con fallback Java puro.");
                return;
            }

            String libName = "libslimefun_ffi.so";
            Path nativesDir = dataFolder.resolve("natives");
            Path targetLib = nativesDir.resolve(libName);

            if (!Files.isRegularFile(targetLib)) {
                try (InputStream is = NativeEngineBridge.class.getResourceAsStream("/natives/" + libName)) {
                    if (is != null) {
                        Files.createDirectories(nativesDir);
                        Files.copy(is, targetLib, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }

            if (Files.isRegularFile(targetLib)) {
                System.load(targetLib.toAbsolutePath().toString());
                nativeAvailable = true;
                LOGGER.info("[DrakesCore-Rust] Biblioteca nativa cargada desde: " + targetLib);
            } else {
                try {
                    System.loadLibrary("slimefun_ffi");
                    nativeAvailable = true;
                    LOGGER.info("[DrakesCore-Rust] Biblioteca nativa slimefun_ffi cargada desde library path.");
                } catch (UnsatisfiedLinkError e) {
                    LOGGER.info("[DrakesCore-Rust] Biblioteca no presente; motor activo en modo fallback Java seguro.");
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "[DrakesCore-Rust] Fallback a Java tras excepcion en carga nativa: " + t.getMessage());
            nativeAvailable = false;
        }
    }

    public static boolean isNativeAvailable() {
        return nativeAvailable;
    }

    /**
     * Suma saturada para EnergyNet / redes de energia.
     */
    public static int sumSaturating(int[] values) {
        if (values == null || values.length == 0) return 0;

        if (nativeAvailable) {
            try {
                return nativeSumSaturating(values);
            } catch (UnsatisfiedLinkError | NoClassDefFoundError ignored) {
                nativeAvailable = false;
            }
        }

        // Fallback Java seguro
        int sum = 0;
        for (int v : values) {
            long res = (long) sum + (long) v;
            if (res > Integer.MAX_VALUE) sum = Integer.MAX_VALUE;
            else if (res < Integer.MIN_VALUE) sum = Integer.MIN_VALUE;
            else sum = (int) res;
        }
        return sum;
    }

    /**
     * Comprueba si una posicion esta dentro del radio de aura de un Boss.
     */
    public static boolean isInsideBossAura(double bx, double by, double bz, double px, double py, double pz, double radius) {
        if (nativeAvailable) {
            try {
                return nativeCheckBossAura(bx, by, bz, px, py, pz, radius) == 1;
            } catch (UnsatisfiedLinkError | NoClassDefFoundError ignored) {
                nativeAvailable = false;
            }
        }

        // Fallback Java euclidiano 3D
        double dx = bx - px;
        double dy = by - py;
        double dz = bz - pz;
        return (dx * dx + dy * dy + dz * dz) <= (radius * radius);
    }

    /**
     * Evalua el comportamiento de un reloj de redstone.
     * Retorna: 0 = ALLOW, 1 = THROTTLE, 2 = BREAK.
     */
    public static int evaluateRedstoneClock(int x, int y, int z, boolean isStructure, boolean isProtectedSf) {
        if (nativeAvailable) {
            try {
                return nativeEvaluateClock(x, y, z, isStructure ? 1 : 0, isProtectedSf ? 1 : 0);
            } catch (UnsatisfiedLinkError | NoClassDefFoundError ignored) {
                nativeAvailable = false;
            }
        }

        // Fallback Java permisivo
        return 0;
    }

    // Metodos JNI nativos enlazados con libslimefun_ffi.so / libodysseia_ffi.so
    private static native int nativeSumSaturating(int[] values);
    private static native int nativeCheckBossAura(double bx, double by, double bz, double px, double py, double pz, double radius);
    private static native int nativeEvaluateClock(int x, int y, int z, int isStructure, int isProtectedSf);
}

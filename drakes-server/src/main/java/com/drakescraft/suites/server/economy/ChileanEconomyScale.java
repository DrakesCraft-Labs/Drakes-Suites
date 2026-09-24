package com.drakescraft.suites.server.economy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Escala Monetaria Chilena (CLP) para el ecosistema DrakesCraft y Star.
 * Convierte el estándar de 1 peso base a 1.000 pesos/Dragmas funcionales (la "luca"),
 * eliminando decimales superfluos y aplicando el formato numérico bancario chileno (puntos de miles).
 */
public class ChileanEconomyScale {

    public static final long DEFAULT_MULTIPLIER = 1000L;
    private final long scaleMultiplier;
    private final DecimalFormat clpFormatter;

    public ChileanEconomyScale() {
        this(DEFAULT_MULTIPLIER);
    }

    public ChileanEconomyScale(long scaleMultiplier) {
        this.scaleMultiplier = Math.max(1L, scaleMultiplier);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY); // Usa '.' como separador de miles
        symbols.setGroupingSeparator('.');
        this.clpFormatter = new DecimalFormat("#,###", symbols);
    }

    /**
     * Convierte un valor base tradicional (ej. $5 antiguos) a la escala chilena ($5.000 CLP).
     */
    public long toChileanPesos(double baseAmount) {
        return Math.round(baseAmount * scaleMultiplier);
    }

    /**
     * Formatea una cantidad como Moneda Chilena / Dragmas (ej. "$1.000 CLP" o "$1.500.000 Dragmas").
     */
    public String format(long amount) {
        return "$" + clpFormatter.format(amount) + " CLP";
    }

    /**
     * Formatea una cantidad con el símbolo canónico de Dragmas (ej. "₯1.000").
     */
    public String formatDragmas(long amount) {
        return "₯" + clpFormatter.format(amount);
    }

    /**
     * Genera un subsidio dinámico de novatos (Newbie Grant) en escala chilena.
     * En lugar de entregar una cifra plana, genera montos orgánicos entre minPesos y maxPesos
     * redondeados a múltiplos de $1.000 para preservar realismo económico.
     *
     * @param minPesos Mínimo en pesos chilenos (ej. 50.000)
     * @param maxPesos Máximo en pesos chilenos (ej. 250.000)
     * @return Monto otorgado en pesos chilenos
     */
    public long generateDynamicNewbieGrant(long minPesos, long maxPesos) {
        long min = Math.max(1000L, minPesos);
        long max = Math.max(min, maxPesos);

        long randomAmount = ThreadLocalRandom.current().nextLong(min, max + 1);
        // Redondear al millar más cercano ($1.000)
        return (randomAmount / 1000L) * 1000L;
    }

    public long getScaleMultiplier() {
        return scaleMultiplier;
    }
}

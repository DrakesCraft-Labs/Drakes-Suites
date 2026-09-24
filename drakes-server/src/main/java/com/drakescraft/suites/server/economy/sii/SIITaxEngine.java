package com.drakescraft.suites.server.economy.sii;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio de Impuestos Internos (S.I.I.) de Star y DrakesCraft.
 * Regula la política fiscal macroeconómica:
 * - IVA (19%): Recaudado en transacciones del mercado oficial.
 * - Impuesto a Transferencias (/pay): Desincentiva transferencias fantasmas y drena circulante.
 * - Impuesto Progresivo al Patrimonio: Gravamen a grandes fortunas para contener hiperinflación.
 * - Tesoro Fiscal Soberano: Fondo redistributivo para ChatGames, novatos y eventos comunitarios.
 */
public class SIITaxEngine {

    public static final double DEFAULT_IVA_RATE = 0.19; // 19% IVA chileno
    public static final double DEFAULT_PAY_TAX_RATE = 0.03; // 3% comisión de transferencia

    private final double ivaRate;
    private final double transferTaxRate;
    private final boolean wealthTaxEnabled;
    private final AtomicLong fiscalTreasury = new AtomicLong(0L);

    public SIITaxEngine() {
        this(DEFAULT_IVA_RATE, DEFAULT_PAY_TAX_RATE, true);
    }

    public SIITaxEngine(double ivaRate, double transferTaxRate, boolean wealthTaxEnabled) {
        this.ivaRate = Math.max(0.0, Math.min(0.50, ivaRate));
        this.transferTaxRate = Math.max(0.0, Math.min(0.20, transferTaxRate));
        this.wealthTaxEnabled = wealthTaxEnabled;
    }

    /**
     * Calcula el desglose de IVA (19%) sobre un monto neto o bruto.
     *
     * @param netPrice Precio neto sin impuesto
     * @return Retención de IVA recaudada
     */
    public long calculateIva(long netPrice) {
        if (netPrice <= 0L) return 0L;
        long tax = Math.round(netPrice * ivaRate);
        fiscalTreasury.addAndGet(tax);
        return tax;
    }

    /**
     * Calcula el impuesto aplicado a una transferencia entre jugadores (/pay).
     *
     * @param transferAmount Cantidad enviada
     * @return Impuesto retenido
     */
    public long calculateTransferTax(long transferAmount) {
        if (transferAmount <= 0L) return 0L;
        long tax = Math.max(1L, Math.round(transferAmount * transferTaxRate));
        fiscalTreasury.addAndGet(tax);
        return tax;
    }

    /**
     * Calcula el impuesto progresivo semanal de patrimonio según el tramo de fortuna.
     * Tramo 1 (< $1.000.000): 0% (Exento)
     * Tramo 2 ($1.000.000 a $10.000.000): 0.5%
     * Tramo 3 ($10.000.000 a $100.000.000): 1.5%
     * Tramo 4 (> $100.000.000): 3.0%
     *
     * @param totalWealth Fortuna total del jugador en pesos chilenos
     * @return Contribución a recaudar
     */
    public long calculateWealthTax(long totalWealth) {
        if (!wealthTaxEnabled || totalWealth < 1000000L) {
            return 0L; // Exento
        }

        double rate;
        if (totalWealth > 100000000L) {
            rate = 0.030; // 3%
        } else if (totalWealth > 10000000L) {
            rate = 0.015; // 1.5%
        } else {
            rate = 0.005; // 0.5%
        }

        long tax = Math.round(totalWealth * rate);
        fiscalTreasury.addAndGet(tax);
        return tax;
    }

    /**
     * Retira fondos del Tesoro Fiscal para financiar premios de ChatGames o subsidios de novatos.
     *
     * @param amount Monto a extraer
     * @return Monto realmente transferido
     */
    public long withdrawFromTreasury(long amount) {
        if (amount <= 0L) return 0L;
        while (true) {
            long current = fiscalTreasury.get();
            if (current <= 0L) return 0L;
            long grant = Math.min(current, amount);
            if (fiscalTreasury.compareAndSet(current, current - grant)) {
                return grant;
            }
        }
    }

    public long getFiscalTreasuryBalance() {
        return fiscalTreasury.get();
    }

    public double getIvaRate() {
        return ivaRate;
    }

    public double getTransferTaxRate() {
        return transferTaxRate;
    }

    public boolean isWealthTaxEnabled() {
        return wealthTaxEnabled;
    }
}

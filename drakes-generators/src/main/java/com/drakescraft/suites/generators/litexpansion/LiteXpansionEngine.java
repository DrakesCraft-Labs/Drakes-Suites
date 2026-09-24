package com.drakescraft.suites.generators.litexpansion;

/**
 * Motor puro de cálculo energético para generadores solares avanzados y reactores de vacío de LiteXpansion.
 */
public final class LiteXpansionEngine {

    public enum SolarType {
        ADVANCED(80, 10, 320, 320_000),
        HYBRID(640, 80, 1200, 1_000_000),
        ULTIMATE(5120, 640, 5120, 10_000_000);

        private final int dayRate;
        private final int nightRate;
        private final int outputRate;
        private final int capacity;

        SolarType(int dayRate, int nightRate, int outputRate, int capacity) {
            this.dayRate = dayRate;
            this.nightRate = nightRate;
            this.outputRate = outputRate;
            this.capacity = capacity;
        }

        public int getDayRate() { return dayRate; }
        public int getNightRate() { return nightRate; }
        public int getOutputRate() { return outputRate; }
        public int getCapacity() { return capacity; }
    }

    private final int reactorJoulesPerTick;
    private final double fuelConsumptionRate;
    private final boolean voidQuarryAllowed;

    public LiteXpansionEngine(int reactorJoulesPerTick, double fuelConsumptionRate, boolean voidQuarryAllowed) {
        this.reactorJoulesPerTick = Math.max(0, reactorJoulesPerTick);
        this.fuelConsumptionRate = Math.max(0.1, fuelConsumptionRate);
        this.voidQuarryAllowed = voidQuarryAllowed;
    }

    public int calculateSolarGeneration(SolarType type, boolean isDay, boolean canSeeSky) {
        if (!canSeeSky) {
            return 0;
        }
        return isDay ? type.getDayRate() : type.getNightRate();
    }

    public int calculateReactorOutput(boolean hasThoriumFuel) {
        if (!hasThoriumFuel) {
            return 0;
        }
        return (int) Math.round(reactorJoulesPerTick * fuelConsumptionRate);
    }

    public int getReactorJoulesPerTick() {
        return reactorJoulesPerTick;
    }

    public double getFuelConsumptionRate() {
        return fuelConsumptionRate;
    }

    public boolean isVoidQuarryAllowed() {
        return voidQuarryAllowed;
    }
}

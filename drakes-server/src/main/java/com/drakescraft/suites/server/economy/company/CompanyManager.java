package com.drakescraft.suites.server.economy.company;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gestor de Empresas y Sociedades Comerciales (SpA / Ltda) para DrakesCraft.
 * Permite a jugadores asociarse, constituir razones sociales con RUT chileno verificado (Módulo 11),
 * mantener fondos corporativos y distribuir dividendos de utilidades.
 */
public class CompanyManager {

    private static final AtomicInteger RUT_SEQUENCE = new AtomicInteger(76000000);
    private final Map<String, Company> companiesByRut = new ConcurrentHashMap<>();
    private final Map<String, String> rutByName = new ConcurrentHashMap<>();
    private final long constitutionFee;

    public CompanyManager(long constitutionFee) {
        this.constitutionFee = Math.max(0L, constitutionFee);
    }

    /**
     * Calcula el Dígito Verificador chileno según el algoritmo estándar Módulo 11.
     */
    public static char calculateRutDv(int rutNumber) {
        int m = 0, s = 1;
        for (int t = rutNumber; t != 0; t /= 10) {
            s = (s + t % 10 * (9 - m++ % 6)) % 11;
        }
        return (char) (s != 0 ? s + 47 : 75); // 75 = 'K'
    }

    /**
     * Genera un RUT de persona jurídica chilena con formato 76.XXX.XXX-X.
     */
    public synchronized String generateNextRut() {
        int num = RUT_SEQUENCE.incrementAndGet();
        char dv = calculateRutDv(num);
        String s = String.valueOf(num);
        return s.substring(0, 2) + "." + s.substring(2, 5) + "." + s.substring(5, 8) + "-" + dv;
    }

    /**
     * Constituye una nueva empresa en el sistema.
     *
     * @param name Nombre comercial (ej: "Minera Olimpo")
     * @param founderUuid UUID del fundador
     * @param companyType Tipo societario ("SpA", "Ltda", "EIRL")
     * @return Instancia de la empresa creada, o null si el nombre ya existe
     */
    public synchronized Company registerCompany(String name, UUID founderUuid, String companyType) {
        Objects.requireNonNull(name, "Company name cannot be null");
        Objects.requireNonNull(founderUuid, "Founder UUID cannot be null");

        String normalizedName = name.trim();
        if (rutByName.containsKey(normalizedName.toLowerCase())) {
            return null; // Nombre duplicado
        }

        String rut = generateNextRut();
        Company company = new Company(rut, normalizedName, founderUuid, companyType);
        companiesByRut.put(rut, company);
        rutByName.put(normalizedName.toLowerCase(), rut);
        return company;
    }

    public Optional<Company> getCompanyByRut(String rut) {
        return Optional.ofNullable(companiesByRut.get(rut));
    }

    public Optional<Company> getCompanyByName(String name) {
        if (name == null) return Optional.empty();
        String rut = rutByName.get(name.trim().toLowerCase());
        return rut == null ? Optional.empty() : Optional.ofNullable(companiesByRut.get(rut));
    }

    public Collection<Company> getAllCompanies() {
        return Collections.unmodifiableCollection(companiesByRut.values());
    }

    public long getConstitutionFee() {
        return constitutionFee;
    }

    /**
     * Modelo de Empresa con balance corporativo y tabla de socios/accionistas.
     */
    public static class Company {
        private final String rut;
        private final String name;
        private final UUID founder;
        private final String companyType;
        private long balance;
        private final Map<UUID, Double> shareholders = new HashMap<>(); // UUID -> Porcentaje (0.0 a 1.0)

        public Company(String rut, String name, UUID founder, String companyType) {
            this.rut = rut;
            this.name = name;
            this.founder = founder;
            this.companyType = companyType != null ? companyType : "SpA";
            this.shareholders.put(founder, 1.0); // 100% de inicio
            this.balance = 0L;
        }

        public synchronized void deposit(long amount) {
            if (amount > 0L) {
                this.balance += amount;
            }
        }

        public synchronized boolean withdraw(long amount) {
            if (amount > 0L && this.balance >= amount) {
                this.balance -= amount;
                return true;
            }
            return false;
        }

        /**
         * Asigna participación accionaria. La suma debe respetar el 100% (1.0).
         */
        public synchronized boolean setShareholder(UUID member, double percentage) {
            if (percentage <= 0.0) {
                shareholders.remove(member);
                return true;
            }
            shareholders.put(member, Math.min(1.0, percentage));
            return true;
        }

        /**
         * Liquida dividendos repartiendo un monto del balance entre todos los accionistas.
         *
         * @param totalDistribution Monto total a distribuir
         * @return Mapa con UUID del socio y la cantidad a abonar
         */
        public synchronized Map<UUID, Long> distributeDividends(long totalDistribution) {
            if (totalDistribution <= 0L || this.balance < totalDistribution) {
                return Collections.emptyMap();
            }

            Map<UUID, Long> payouts = new HashMap<>();
            long allocated = 0L;

            for (Map.Entry<UUID, Double> entry : shareholders.entrySet()) {
                long share = Math.round(totalDistribution * entry.getValue());
                if (share > 0L) {
                    payouts.put(entry.getKey(), share);
                    allocated += share;
                }
            }

            this.balance -= allocated;
            return payouts;
        }

        public String getRut() {
            return rut;
        }

        public String getName() {
            return name;
        }

        public UUID getFounder() {
            return founder;
        }

        public String getCompanyType() {
            return companyType;
        }

        public synchronized long getBalance() {
            return balance;
        }

        public synchronized Map<UUID, Double> getShareholders() {
            return Collections.unmodifiableMap(new HashMap<>(shareholders));
        }
    }
}

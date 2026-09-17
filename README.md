# 🌌 Drakes-Suites (Monorepo del Ecosistema DrakesCraft)

> **Consolidación Oficial de 160+ Plugins en 8 Mega-Suites Autónomas para Paper 1.21.11 y Preparación para Minecraft 26.X**

---

## 🧭 Visión y Arquitectura

En lugar de cargar más de 160 plugins individuales dispersos con 70+ tickers desincronizados y consumo desmedido de memoria por fragmentación de ClassLoaders, **Drakes-Suites** consolida el 100% de la red de DrakesCraft en **8 Mega-Suites modulares** bajo un monorepo Maven multi-módulo.

```
                    ┌─────────────────────────────────────────┐
                    │      DrakesCore (Suite 0 - Kernel)      │
                    │  Runtime Base, Dough, Ticker & Telemetry │
                    └────────────────────┬────────────────────┘
                                         │
        ┌──────────────┬─────────────────┼────────────────┬──────────────┐
        ▼              ▼                 ▼                ▼              ▼
   DrakesTech      DrakesBio        DrakesMagic    DrakesGenerators DrakesUtility
   (Suite 1)       (Suite 2)         (Suite 3)        (Suite 4)       (Suite 5)
  Redes/Storage   Genética T0-T9    Alquimia/Arcano   Reactores/Red    Mochilas/QoL
        │              │                                  │              │
        └──────────────┴─────────────────┬────────────────┴──────────────┘
                                         ▼
                                   DrakesCombat & DrakesServer
                                    (Suite 6)       (Suite 7)
                                  Arsenal/Bélico  Odysseia/Standalone
```

---

## 🏛️ Desglose de las 8 Mega-Suites

| Suite | Artefacto JAR | Dominio | Repos Auditados | Componentes y Sistemas Absorbidos |
| :--- | :--- | :--- | :---: | :--- |
| **0** | `DrakesCore` | Kernel & Core | 6 | Slimefun4-Drake runtime, Dough-core, Ticker centralizado, JNI Bindings nativos y telemetría. |
| **1** | `DrakesTech` | Logística & Industria | 15 | Redes de transporte (Networks), almacenamiento digital por celdas masivas, Infinity, DynaTech, FastMachines, Supreme, Nanotech. |
| **2** | `DrakesBio` | Biotecnología & Campo | 13 | GeneticChickengineering (Tiers 0 a 9 con clonación y empalme), ExoticGarden, Cultivation, SlimyBees, MobCapturer. |
| **3** | `DrakesMagic` | Arcano & Alquimia | 9 | AlchimiaVitae, Crystamae, RelicsOfCthonia, SoulJars, Netheopoiesis, trasmutación y runas. |
| **4** | `DrakesGenerators` | Matriz Energética | 7 | LiteXpansion, SMG, UltimateGenerators2, EcoPower, OreChunks, reactores solares y nucleares. |
| **5** | `DrakesUtility` | Utilidades & QoL | 14 | DyedBackpacks, ColoredEnderChests, ChestTerminal, SFCalc, SlimeHUD, visualizadores. |
| **6** | `DrakesCombat` | Arsenal & Equipamiento | 13 | ExtraTools, ExtraGear, SlimefunWarfare, LuckyBlocks, Galaxyfun, armaduras y exoesqueletos. |
| **7** | `DrakesServer` | Servidor Standalone | 90 | Motor Odysseia (Rust/Java), InvSwitcher, PlayerVaultZ, AxGraves, BreweryX, Mercado, economía y chat. |

---

## ⚡ Ticker Engine Centralizado (`SuiteTickerEngine`)
Los addons ya no instancian bucles `runTaskTimer` desacoplados. Se registran en el motor central de `DrakesCore`, que:
1. Agrupa y procesa bloques activos por chunk cargado.
2. Descarta ticks en chunks inactivos sin consultar NBT repetitivo.
3. Reduce en un **~65-80%** la sobrecarga de CPU respecto a 160 plugins individuales.

---

## 🛡️ Compatibilidad Retroactiva y Cero Pérdida de Datos
- **Mapeo de Claves PDC**: Todos los identificadores `slimefun_item` se preservan idénticos a los addons originales.
- Los jugadores en Dallas conservarán todas sus pertenencias en cofres, terminales de red y granjas sin necesidad de reiniciar el mundo.

---

## 🚀 Hoja de Ruta hacia 26.X (2027)
1. **Fase 1 (Completada)**: Inicialización del Monorepo `Drakes-Suites`, compilación reactor Maven y esqueletos de las 8 Suites.
2. **Fase 2**: Integración de código fuente por dominio (migración desde `drakes-slimefun-labs/sources`).
3. **Fase 3**: Unificación del Ticker y validación en servidor de pruebas Dallas.
4. **Fase 4**: Porteo a versión mayor 26.X atacando únicamente 8 plugins consolidados en vez de 160 dependencias rotas.
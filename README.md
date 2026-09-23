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

## 🏛️ Desglose Canónico de las 8 Mega-Suites

| Suite | Artefacto JAR | Dominio | Repos Auditados | Componentes y Sistemas Absorbidos |
| :--- | :--- | :--- | :---: | :--- |
| **0** | `DrakesCore` | Kernel & Core | 6 | Slimefun4-Drake runtime, Dough-core, Ticker centralizado (`SuiteTickerEngine`), JNI Bindings nativos y telemetría de rendimiento. |
| **1** | `DrakesTech` | Logística & Industria | 15 | Redes digitales (Networks), almacenamiento digital por celdas masivas, Infinity, DynaTech, FastMachines, Supreme, Nanotech, FluffyMachines. |
| **2** | `DrakesBio` | Biotecnología & Campo | 13 | GeneticChickengineering (Tiers 0 a 9 con clonación y empalme genético), ExoticGarden, Cultivation, SlimyBees, MobCapturer. |
| **3** | `DrakesMagic` | Arcano & Alquimia | 9 | AlchimiaVitae, Crystamae, RelicsOfCthonia, SoulJars, Netheopoiesis, transmutación, runas y rituales taumatúrgicos. |
| **4** | `DrakesGenerators` | Matriz Energética | 7 | LiteXpansion, SMG, UltimateGenerators2, EcoPower, OreChunks, reactores nucleares y solares avanzados. |
| **5** | `DrakesUtility` | Utilidades & QoL | 14 | DyedBackpacks, ColoredEnderChests, ChestTerminal, SFCalc, SlimeHUD, visualizadores holográficos y guías dinámicas. |
| **6** | `DrakesCombat` | Arsenal & Equipamiento | 13 | ExtraTools, ExtraGear, SlimefunWarfare, LuckyBlocks, Galaxyfun, exoesqueletos y armaduras reactivas. |
| **7** | `DrakesServer` | Servidor Standalone | 90 | Motor Odysseia (Rust/Java), InvSwitcher, PlayerVaultZ, AxGraves, BreweryX, Mercado Slimefun, economía y chat. |

---

## ⚙️ Estándar de Configuración Modular (`modules/*.yml`)

Cada Mega-Suite elimina los archivos monolíticos inmanejables implementando un sistema desacoplado de sub-módulos gestionados por `AbstractSuiteModule` y `SuiteModuleManager`:

```
plugins/
├── DrakesCore/
│   ├── config.yml                # Configuración del kernel, telemetría y SuiteTickerEngine
│   └── modules/
├── DrakesTech/
│   ├── config.yml                # Master switch y perfiles de rendimiento tecnológico
│   └── modules/
│       ├── networks.yml          # Límites de cables, ancho de banda y transferencia por tick
│       ├── dynatech.yml          # Máquinas dinámicas y generadores cuánticos
│       ├── infinity.yml          # Fórmulas de singularidades y recetas infinitas
│       ├── supreme.yml           # Balance de maquinaria Supreme y overclockers
│       └── fastmachines.yml      # Multiplicadores de velocidad y buffers
├── DrakesBio/
│   ├── config.yml
│   └── modules/
│       ├── genetic_chickens.yml  # Tiers 0-9, mutaciones, tasas de drop y incubadoras
│       ├── exotic_garden.yml     # Crecimiento de cultivos y recetas culinarias
│       └── slimy_bees.yml        # Panales, genética apícola y producción de miel
├── DrakesMagic/
│   ├── config.yml
│   └── modules/
│       ├── alchimia_vitae.yml    # Calbazas de transmutación y elixires
│       ├── crystamae.yml         # Rituales cristalinos y resonadores
│       └── relics_cthonia.yml    # Reliquias del abismo y sacrificios
├── DrakesGenerators/
│   ├── config.yml
│   └── modules/
│       ├── litexpansion.yml      # Reactores y generadores de vacío
│       └── ultimategenerators.yml# Tasas de Joules (J/t) y disipación de calor
├── DrakesUtility/
│   ├── config.yml
│   └── modules/
│       ├── backpacks.yml         # Tamaños de mochilas tintables y filtros
│       └── chest_terminal.yml    # Rango de acceso inalámbrico e indexación
├── DrakesCombat/
│   ├── config.yml
│   └── modules/
│       ├── warfare.yml           # Armas balísticas, municiones y explosivos
│       ├── slimetinker.yml       # Modificadores de piezas, aleaciones y durabilidad
│       └── extragear.yml         # Sets de protección y mitigación de daño
└── DrakesServer/
    ├── config.yml
    └── modules/
        ├── odysseia.yml          # Puente Rust-Java y persistencia SQL
        ├── invswitcher.yml       # Separación estricta de inventarios por modalidad
        └── playervaultz.yml      # Bóvedas virtuales seguras
```

### Estructura de un Módulo (`modules/<modulo>.yml`):
```yaml
# Control maestro del sub-módulo
enabled: true

# Ajustes de rendimiento y balance
tick-rate: 1
max-operations-per-tick: 64

# Opciones específicas del dominio
features:
  wireless-transfer: true
  auto-crafting: true
  particle-effects: true
```

---

## 🔒 Plugins Propietarios y Binarios Cerrados en Dallas

Al ejecutar `/plugins` en la consola de producción de **DrakesCraft (Dallas)**, coexisten dos categorías de software claramente delimitadas:

1. **Software Open-Source y Custom Drakes (160+ repos):**
   - Absorbido al 100% dentro del monorepo **Drakes-Suites** (código fuente limpio, auditado, con compatibilidad PDC asegurada).
2. **Plugins Comerciales / Closed-Source / Binarios de Terceros:**
   - Ejemplos activos en Dallas: `BentoBox` (BSkyBlock/OneBlock), `UltimateClaims`, `ModelEngine`, `MythicMobs`, `DeluxeMenus`, `Castle`.
   - **Directriz de Arquitectura:** Estos plugins **NO** se decompilan ni se versionan en Git abierto para evitar violaciones de licencias y corrupción binaria. Se cargan como JARs independientes en el directorio `/plugins` del servidor Paper.
   - Si una Suite requiere interactuar con ellos (e.g. comprobar un reclamo de `UltimateClaims` o una isla de `BentoBox`), se utilizan **Soft-Dependencies** con invocaciones reflexivas o dependencias Maven con `<scope>provided</scope>` y `<systemPath>`.

---

## 📦 Repositorios en la Organización GitHub vs Drakes-Suites

La organización GitHub de DrakesCraft contiene más de 180 repositorios originados en años de desarrollo iterativo.

### Política de Deprecación y Archivado:
A medida que las suites absorben los dominios:
1. El código activo pasa a residir **únicamente** en `DrakesCraft-Labs/Drakes-Suites`.
2. Los repositorios individuales reciben el banner oficial en su `README.md`:
   ```markdown
   > ⚠️ **ARCHIVADO / CONSOLIDADO EN DRAKES-SUITES**
   > Este repositorio ha sido consolidado de forma definitiva en el monorepo oficial:
   > [**Drakes-Suites**](https://github.com/DrakesCraft-Labs/Drakes-Suites)
   > Todo el desarrollo activo, correcciones para Paper 1.21.11 y porteo a 26.X se realiza exclusivamente allí.
   ```
3. El repositorio se archiva como **Read-Only** para preservar el historial de commits y evitar bifurcaciones desincronizadas.

---

## ⚡ Ticker Engine Centralizado (`SuiteTickerEngine`)

Los addons ya no instancian bucles `runTaskTimer` desacoplados. Se registran en el motor central de `DrakesCore`, que:
1. Agrupa y procesa bloques activos por chunk cargado.
2. Descarta ticks en chunks inactivos sin consultar NBT repetitivo (`Chunk.isLoaded()`).
3. Reduce en un **~65-80%** la sobrecarga de CPU respecto a 160 plugins individuales corriendo tareas Bukkit asíncronas dispersas.

---

## 🛡️ Compatibilidad Retroactiva y Cero Pérdida de Datos

- **Mapeo de Claves PDC**: Todos los identificadores `slimefun:slimefun_item` se preservan idénticos a los addons originales (e.g. `NETWORKS_CABLE`, `INFINITY_SINGULARITY`, `GCE_CHICKEN_T9`).
- Los jugadores en Dallas conservan todas sus pertenencias en cofres, terminales de red y granjas sin necesidad de reiniciar el mundo.

---

## 🚀 Hoja de Ruta hacia 26.X (2027)

1. **Fase 1 (Completada)**: Inicialización del Monorepo `Drakes-Suites`, compilación reactor Maven y esqueletos de las 8 Suites.
2. **Fase 2 (En Progreso)**: Implementación del subsistema modular `modules/*.yml` y unificación del Ticker Engine en `DrakesCore`.
3. **Fase 3**: Migración de fuentes desde `drakes-slimefun-labs/sources` hacia los módulos correspondientes.
4. **Fase 4**: Validación en servidor de staging Dallas con telemetría de ticks.
5. **Fase 5**: Porteo a la versión mayor 26.X atacando únicamente 8 plugins consolidados en vez de 160 dependencias rotas.
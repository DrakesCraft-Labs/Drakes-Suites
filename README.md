# 🌌 StarSuites (Monorepo Drakes-Suites)

> **Arquitectura Maestra de Suites concebida por JackStar para el ecosistema de software y Minecraft**  
> Consolidación Oficial de 180+ Plugins en Suites Autónomas para Paper 1.21.11+ y Preparación para Minecraft 26.X

---

### 🌟 Los Pilares de Ingeniería Soberana
Este monorepo es parte del ecosistema de software de **JackStar (Jack)**:
1. 🏫 **Tecnología Educativa e Infraestructura Institucional:** Ingeniería de software y redes de campus privadas.
2. ⭐ **Star / JackStar:** Arquitectura de sistemas, orquestador autónomo **SAORI** y autoría de **StarSuites**.
3. 🐉 **DrakesCraft:** Servidor de Minecraft en producción (Dallas, TX), comunidad, economía y modalidades.

> ℹ️ **Reconocimiento Canónico Upstream (Slimefun):**  
> Slimefun original es obra de **TheBusyBiscuit** y la comunidad open-source. **JackStar no es el creador original de Slimefun**, sino el arquitecto de modernización: rescate de rendimiento en Paper 1.21.11, erradicación de 70+ tickers asíncronos duplicados, aceleración off-heap en Rust ([`Slimefun-Rust`](https://github.com/DrakesCraft-Labs/Slimefun-Rust)) y consolidación masiva en suites desacopladas.

---

## 🏛️ Desglose Canónico de las Suites

| Suite | Artefacto JAR | Dominio | Sistemas y Repositorios Consolidados |
| :--- | :--- | :--- | :--- |
| **Suite 0** | `drakes-core.jar` | Kernel & Core | Slimefun4-Drake runtime, Dough-core, Ticker centralizado (`SuiteTickerEngine`), JNI Bindings Rust (`NativeEngineBridge`), DB SQLite WAL y Logs de Auditoría. |
| **Suite 1** | `drakes-tech.jar` | Logística & Industria | Redes digitales (Networks), almacenamiento digital por celdas masivas, Infinity, DynaTech, FastMachines, Supreme, Nanotech, FluffyMachines. |
| **Suite 2** | `drakes-bio.jar` | Biotecnología & Campo | GeneticChickengineering (Tiers 0 a 9 con clonación y empalme genético), ExoticGarden, Cultivation, SlimyBees, MobCapturer. |
| **Suite 3** | `drakes-magic.jar` | Arcano & Alquimia | AlchimiaVitae, Crystamae, RelicsOfCthonia, SoulJars, Netheopoiesis, transmutación, runas y rituales taumatúrgicos. |
| **Suite 4** | `drakes-generators.jar` | Matriz Energética | LiteXpansion, SMG, UltimateGenerators2, EcoPower, OreChunks, reactores nucleares y solares avanzados. |
| **Suite 5** | `drakes-utility.jar` | Utilidades & QoL | DyedBackpacks, ColoredEnderChests, ChestTerminal, SFCalc, SlimeHUD, visualizadores holográficos y guías dinámicas. |
| **Suite 6** | `drakes-combat.jar` | Arsenal & Combate | DrakesBosses, SlimeTinker, SlimefunWarfare, ExtraGear, LuckyBlocks, Galaxyfun, exoesqueletos y armaduras reactivas. |
| **Suite 7** | `drakes-server.jar` | Servidor Standalone | Motor Odysseia (Rust/Java), InvSwitcher (5 modalidades), PlayerVaultZ, AxGraves, BreweryX, Mercado Slimefun, economía y chat. |
| **Suite Multiverse** | `drakes-multiverse.jar` | **Autoría: Chagui68** | Absorbe `MultiverseCreatures` (bosses, criaturas, dimensiones y rituales) y `MultiverseNets` (redes de logística standalone sin Slimefun). |

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

## 🔒 Clasificación Canónica de Plugins en Dallas (`/plugins`)

En el servidor de producción (**Dallas**), coexisten cuatro categorías de software que ningún agente ni desarrollador debe confundir:

1. **Mega-Suites StarSuites (Monorepo `Drakes-Suites`):**
   - Consolida los 160+ plugins y addons en las suites oficiales (`drakes-core` a `drakes-server`) más la suite `drakes-multiverse` de Chagui68.
   - **Estrategia Operativa:** Durante la temporada actual en producción, se mantienen los JARs individuales para no perturbar a los jugadores ni arriesgar pérdida de inventarios en reinicios constantes. StarSuites se afina y valida para su despliegue en la nueva temporada.

2. **Forks Soberanos de la Organización (`DrakesCraft-Labs` - Open Source):**
   - Proyectos de código abierto que Jack mantiene, compila y parchea con lógica propietaria adaptada a Paper 1.21.11+:
   - **`BentoBox-Drake`**: **NO es cerrado.** Es el fork oficial soberano del motor de islas (BSkyBlock, OneBlock, CaveBlock, AcidIsland) con el parche crítico anti-destrucción de ítems de Paper Data Components en `ItemStackTypeAdapter`.
   - **`AxGraves-Drakes`**, **`WorldwideChat-Drake`**, **`PlayerVaultZ-Drake`**, **`BreweryX-Drake`**, **`Inventory-Rollback-Plus-Drake`**, etc.

3. **Plugins Comerciales / Closed-Source / Binarios de Terceros:**
   - Plugins comerciales adquiridos externamente o dependencias de terceros sin repositorio en la organización:
   - **Ejemplos activos:** `UltimateClaims` (Songoda), `ModelEngine`, `MythicMobs`, `Castle`, `DeluxeMenus`, `nLogin`.
   - **Directriz Canónica:** NO son código abierto ni deben ser descompilados. Viven como archivos JAR en `/plugins`. Las Suites interactúan con ellos mediante soft-dependencies y eventos Bukkit.

4. **Componentes Nativos Híbridos en Rust (Off-Heap / FFM Java 21):**
   - **`Slimefun-Rust` (`libslimefun_ffi.so`):** Grafos logísticos y matrices de energía sin pausas de GC.
   - **`Odysseia-Rust` (`libodysseia_ffi.so`):** `RedstoneClockGuard`, `ChatFilterEngine` de alto rendimiento y evaluación de auras divinas.

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
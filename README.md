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

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE

> ### 🛑 OBLIGATORIO PARA DESARROLLADORES Y AGENTES DE IA (ANTIGRAVITY / CODEX / CLAUDE):
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), agregados de recetas o ítems (`feat`), parches de balance, optimizaciones de rendimiento y adaptaciones a Paper 1.21.11+ o Minecraft 26.X para **CUALQUIERA de los plugins y addons absorbidos** debe realizarse **EXCLUSIVAMENTE DENTRO DE ESTE MONOREPO (STARSUITES)**.
> 
> * **NO modificar repositorios individuales antiguos:** Los 180+ micro-repositorios en `DrakesCraft-Labs` quedan formalmente archivados (*Read-Only*).
> * **Trazabilidad Unificada:** Las compilaciones se gestionan a través de Maven Reactor en la raíz del monorepo (`mvn clean package`).
> * **Modularidad:** Cada funcionalidad se controla mediante su respectivo `modules/<modulo>.yml` y clase heredada de `AbstractSuiteModule`.

---

## 🔒 Clasificación Canónica de Plugins en Dallas (`/plugins`)

En el servidor de producción (**Dallas**), coexisten cuatro categorías de software que ningún agente ni desarrollador debe confundir:

1. **Mega-Suites StarSuites (Monorepo `Drakes-Suites`):**
   - Consolida y reemplaza los 160+ plugins y addons sueltos por los 8 JARs oficiales (`drakes-core` hasta `drakes-server`) más la suite soberana `drakes-multiverse` de Chagui68.
   - **`drakes-server.jar` (Star Engine):** Evolución canónica de Odysseia a **Star**. Kernel unificado de servidor, telemetría Star, verificación idempotente de compras Tebex (`purchases.db`), vigilancia de economía macroeconómica (SII) y puente Rust nativo.
   - **Estrategia Operativa:** Durante la temporada actual en producción, se mantienen los JARs individuales activos para garantizar cero interrupciones y cero riesgo de pérdida de inventarios de jugadores. StarSuites entrará en producción limpia al inicio de la nueva temporada.

2. **Forks Soberanos de la Organización (`DrakesCraft-Labs` - Open Source):**
   - Proyectos de código abierto que Jack mantiene, compila y parchea con lógica propietaria adaptada al stack Paper 1.21.11+:
   - **`BentoBox-Drake`**: **NO es cerrado.** Fork oficial soberano del motor de islas (BSkyBlock, OneBlock, CaveBlock, AcidIsland) con el parche crítico anti-destrucción de ítems de Paper Data Components en `ItemStackTypeAdapter` (Incidente #276).
   - **`CrazyAuctions-Drake`**: Subastas de jugadores (`/ah`) adaptadas al stack moderno sin bugs de duplicación de GUI.
   - **`DrakesSlimeMarket`**: Motor macroeconómico dinámico (`/sm`), con 777 ofertas controladas y suavizado exponencial.
   - **`AxGraves-Drakes`**, **`WorldwideChat-Drake`**, **`PlayerVaultZ-Drake`**, **`BreweryX-Drake`**, **`Inventory-Rollback-Plus-Drake`**, **`DrakesBosses`**, **`MultiverseCreatures`**, **`DrakesRankup`**, **`ExcellentCrates-Drake` (`DrakesCrates`)**, **`LevelledMobs-Drake`**.

3. **Plugins Comerciales / Closed-Source / Binarios Licenciados (Licenciados por Jack):**
   - Plugins comerciales adquiridos legítimamente para la red, cuyas configuraciones YAML y bases de datos se preservan íntegramente:
   - **`nLogin` (NickUC):** Licencia permanente de por vida pagada por Jack. Autenticación empresarial anti-bot, cifrado moderno de credenciales y soporte Java/Bedrock. Acompañado de **`nAntiBot`**.
   - **`TAB` (NEZNAMY):** Versión premium para tablist avanzada, prefijos de rangos y sincronización Bedrock/Geyser.
   - **`UltimateShop` (PQguanfang):** Tiendas GUI comerciales y transacción comunitaria multi-idioma.
   - **`SlotMachine`**: Juegos de azar y entretenimiento con Dragmas.
   - **`StaffPlus` / `StaffPlusPlus`**: Suite administrativa de moderación, inspección y congelamiento.
   - **`LibsDisguises Premium`**: Transformación y disfraces para eventos y jefes.
   - **`ModelEngine` & `FancyNpcs`**: Modelos 3D interactivos y NPCs de modalidades.
   - **`DeluxeMenus`**: Menús interactivos personalizados (`/menu`, `/rangos`, `/minas`).
   - **`GrimAC`**: Anti-cheat predictivo por paquetes.
   - **Plataforma de Red:** `WorldGuard`, `CoreProtect`, `InteractiveChat`, `MiniMOTD`, `ChatGames`, `Geyser-Spigot`, `Floodgate`, `DiscordSRV`.
   - **Directriz Canónica:** NO son código abierto ni deben ser descompilados a la fuerza. Viven como archivos JAR en `/plugins`.

4. **Componentes Nativos Híbridos en Rust (Off-Heap / FFM Java 21):**
   - **`Slimefun-Rust` (`libslimefun_ffi.so`):** Grafos logísticos (Networks/Cargo) y matrices de energía SIMD sin pausas de Garbage Collector.
   - **`Star-Rust` / `Odysseia-Rust` (`libodysseia_ffi.so`):** `RedstoneClockGuard` (detección y throttling de loops), `ChatFilterEngine` de alto rendimiento y evaluación de auras divinas 3D.

---

## 📦 Repositorios en la Organización GitHub vs StarSuites

La organización GitHub de DrakesCraft contiene repositorios históricos originados en años de desarrollo iterativo.

### Política de Deprecación y Archivado:
A medida que las suites absorben los dominios:
1. El código activo pasa a residir **única y exclusivamente** en `DrakesCraft-Labs/Drakes-Suites`.
2. Los repositorios individuales reciben el banner oficial en su `README.md`:
   ```markdown
   > ⚠️ **ARCHIVADO / CONSOLIDADO EN STARSUITES (DRAKES-SUITES)**
   > Este repositorio ha sido consolidado de forma definitiva en el monorepo oficial:
   > [**StarSuites**](https://github.com/DrakesCraft-Labs/Drakes-Suites)
   > Todo el desarrollo activo, correcciones para Paper 1.21.11+ y porteo a 26.X se realiza exclusivamente allí.
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
2. **Fase 2 (Completada)**: Implementación del subsistema modular `modules/*.yml` y unificación del Ticker Engine en `DrakesCore`.
3. **Fase 3 (Completada)**: Integración y sombreado de dependencias core (incluyendo evolución de Odysseia a **Star** en `drakes-server`).
4. **Fase 4 (En Validación)**: Smoke tests de configuraciones YAML en vivo, auditoría anti-dupes y pruebas de aislamiento de modalidades.
5. **Fase 5 (Próxima Temporada)**: Despliegue atómico de las suites consolidadas en el inicio de la nueva temporada en Dallas.
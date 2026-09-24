# 🧰 Suite 5: DrakesUtility (`drakes-utility.jar`)

> **Herramientas de Soporte, Almacenamiento Portátil e Interfaces QoL de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Dual Paper 1.21.11 & Purpur 26.X (Java 21 / 25)

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), visualización holográfica y mitigación de dupes en contenedores portátiles para los siguientes addons **SE REALIZA EXCLUSIVAMENTE DE FORMA NATIVA EN ESTE MÓDULO**:
> * **DyedBackpacks** (Mochilas teñidas, 96 variantes, tamaños dinámicos y guardas anti-anidación)
> * **ColoredEnderChests** (4,096 frecuencias de cofres de ender por código de color de lana)
> * **ChestTerminal** (Terminal inalámbrica y visualizador de cofres en red)
> * **SFCalc / SlimeHUD** (Calculadora de recetas en GUI y HUD de información de máquinas)
> * **SoundMuffler / SimpleUtils / SlimeFrame** (Silenciadores de maquinaria y marcos)
> 
> *Los micro-repositorios independientes antiguos quedan formalmente archivados en modo Read-Only.*

---

## 🎒 Módulos Nativos Implementados

### 1. `BackpacksModule` (`com.drakescraft.suites.utility.backpacks`)
Consolida el addon histórico `DyedBackpacks`:
* **96 Combinaciones:** 6 Tiers canónicos (`BACKPACK_SMALL` [9 slots], `BACKPACK_MEDIUM` [18 slots], `BACKPACK_LARGE` [27 slots], `WOVEN_BACKPACK` [36 slots], `GILDED_BACKPACK` [45 slots], `RADIANT_BACKPACK` [54 slots]) $\times$ 16 colores cromáticos.
* **Preservación Absoluta de PDC:** Claves idénticas a Slimefun (`DYED_BACKPACK_SMALL_RED`, etc.) en `slimefun:slimefun_item`. Cero pérdida de ítems existentes en producción.
* **Salvaguarda Anti-Dupe Soberana:** Detección y bloqueo inmediato contra anidación de mochilas (*backpack inside backpack dupe*), cancelando clics de inserción y transferencias de tipo shift-click hacia mochilas abiertas con registro en auditoría.
* **Compatibilidad Dual:** Construcción de nombres y lore mediante `CrossVersionAdapter`, renderizando sin excepciones en Paper 1.21.11 (Adventure 4) y Purpur 26.X (Adventure 5).

### 2. `ColoredEnderChestsModule` (`com.drakescraft.suites.utility.enderchests`)
Consolida el addon histórico `ColoredEnderChests`:
* **4,096 Frecuencias Cromáticas:** Matriz de 3 canales $16 \times 16 \times 16$ identificados por código `c1_c2_c3`.
* **Dos Tamaños:** Pequeño (27 slots) y Grande (54 slots) con identificadores canónicos `COLORED_ENDER_CHEST_SMALL_c1_c2_c3` y `COLORED_ENDER_CHEST_BIG_c1_c2_c3`.
* **Sincronización Interdimensional:** Inventarios persistentes sincronizados entre Overworld, Nether y End con audio espacial vanilla (`BLOCK_ENDER_CHEST_OPEN` / `CLOSE`).

### 3. `SoundMufflerModule` (`com.drakescraft.suites.utility.soundmuffler`)
Consolida el addon histórico `SoundMuffler`:
* **Amortiguación de Ruidos de Maquinaria:** Silencia o atenúa ruidos en un radio de 8 bloques configurable.
* **Interfaz Gráfica Interactiva:** Menú de ajuste granular de volumen (0% a 100%) e interruptor de encendido/apagado.
* **PDC Canónico:** `SOUND_MUFFLER` con material base `WHITE_CONCRETE`.

### 4. `SimpleUtilsModule` (`com.drakescraft.suites.utility.simpleutils`)
Consolida el addon histórico `SimpleUtils`:
* **Simple Elevator (`SIMPLE_ELEVATOR`):** Elevadores por salto (ascenso) y agachado (descenso) entre bloques de cuarzo alineados.
* **Simple Workbench (`SIMPLE_WORKBENCH`):** Mesa de crafteo unificada y portátil.
* **Simple Wrench (`SIMPLE_WRENCH`):** Llave inglesa para desmontaje y mantenimiento rápido de máquinas.

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada herramienta cuenta con su archivo de configuración desacoplado en `plugins/DrakesUtility/modules/`:
* `backpacks.yml`: Tamaños de inventario, soporte de tintes, filtros y guardias anti-dupe.
* `colored_enderchests.yml`: Red de 4096 canales de frecuencia, bloqueo de concurrencia y caché.
* `chest_terminal.yml`: Rango inalámbrico e indexación de bloques adyacentes.
* `sfcalc.yml`, `slimehud.yml`, `soundmuffler.yml`, `simpleutils.yml`, `slimeframe.yml`, `extratools.yml`, `extrautils.yml`, `portalgun.yml`, `smallspace.yml`, `guidetools.yml`, `worldedit_sf.yml`, `geyser_heads.yml`.

Recarga en caliente sin reiniciar el servidor:
```bash
/drakessuites reload utility backpacks
/drakessuites reload utility colored_enderchests
```

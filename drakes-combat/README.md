# ⚔️ Suite 6: DrakesCombat (`drakes-combat.jar`)

> **Arsenal Bélico, Modificadores de Armas, Sets de Combate y Jefes de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), balance de armaduras, mitigación de daño y sinergia marcial para los siguientes addons **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **DrakesBosses Integration** (Adaptación anti-infinity, daño verdadero a armaduras singulares)
> * **SlimeTinker** (Partes de herramientas modulares, modificadores y aleaciones)
> * **SlimefunWarfare / MissileWarfare** (Armas de fuego, torretas, misiles y explosivos)
> * **ExtraGear / LuckyBlocks** (Armaduras pesadas reactivas y bloques de la suerte)
> * **Galaxyfun / SFMobDrops / SlimefunDisc**
> 
> *Los repositorios independientes antiguos quedan formalmente archivados en modo Read-Only.*

## 🛡️ Módulos Nativos Implementados

### 1. `MobDropsModule` (`com.drakescraft.suites.combat.mobdrops`)
Consolida el addon histórico `SFMobDrops`:
* **Tablas de Botín Dinámicas:** Configuración de drops por tipo de entidad con rangos de cantidad y probabilidades porcentuales ($0.01\%$ a $100\%$).
* **Preservación Absoluta de PDC:** Genera ítems con Slimefun ID (`IRON_DUST`, `COPPER_DUST`, `SULFUR`, `MAGNESIUM_DUST`, etc.) y compatibilidad con drops de mobs personalizados.
* **Integración con Panteones de Dioses:** Diseñado para coexistir armónicamente con `DrakesBosses` para drops míticos.

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada sistema de combate cuenta con su archivo de configuración desacoplado en `plugins/DrakesCombat/modules/`:
* `slimetinker.yml`: Materiales, dureza, velocidad de minado y modificadores.
* `warfare.yml`: Balística, velocidad de proyectil, cadencia de tiro y retroceso.
* `extragear.yml`: Multiplicadores de mitigación de daño y efectos de set.
* `luckyblocks.yml`: Tablas de loot y eventos aleatorios.
* `missilewarfare.yml`, `sfmobdrops.yml`, `slimefundisc.yml`.

Recarga en caliente:
```
/drakessuites reload drakes-combat [modulo]
```

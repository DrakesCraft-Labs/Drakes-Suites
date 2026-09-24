# 🧬 Suite 2: DrakesBio (`drakes-bio.jar`)

> **Biotecnología, Genética Aviar/Apícola y Agricultura Avanzada de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), balance de tasas de drops, genética y recetas botánicas para los siguientes addons **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **GeneticChickengineering** (Pollos Tiers 0 a 9, incubadoras genéticas, empalme de ADN)
> * **ExoticGarden / Cultivation** (Frutas exóticas, árboles personalizados, cocina)
> * **SlimyBees** (Abejas modificadas, paneles de colmenas y producción de recursos)
> * **MobCapturer / SlimyTreeTaps / Gastronomicon / FlowerPower**
> 
> *Los repositorios independientes antiguos quedan formalmente archivados en modo Read-Only.*

## 🌿 Módulos Nativos Implementados

### 1. `TreeTapsModule` (`com.drakescraft.suites.bio.treetaps`)
Consolida el addon histórico `SlimyTreeTaps`:
* **Extracción de Resina y Ámbar:** Cosecha directa en troncos de madera vivos con herramientas graduadas (`TREE_TAP`, `REINFORCED_TREE_TAP`, `DIAMOND_TREE_TAP` y `TREE_SCRAPER`).
* **Preservación Absoluta de PDC:** Claves idénticas a Slimefun (`STICKY_RESIN`, `RUBBER`, `RAW_PLASTIC`, `AMBER`, `AMBER_BLOCK`).
* **Mecánica Orgánica:** Descortezado dinámico de troncos con retroalimentación acústica y desgaste de herramientas.

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada sistema biológico cuenta con su archivo de configuración desacoplado en `plugins/DrakesBio/modules/`:
* `genetic_chickens.yml`: Tasa de incubación, mutaciones por tier (0 al 9) y drops.
* `exotic_garden.yml`: Crecimiento de cultivos y recetas.
* `cultivation.yml`: Granja automatizada y nutrientes.
* `slimy_bees.yml`: Genética apícola y colmenas.
* `mob_capturer.yml`, `slimytreetaps.yml`, `gastronomicon.yml`, `flowerpower.yml`.

Recarga en caliente:
```
/drakessuites reload drakes-bio [modulo]
```

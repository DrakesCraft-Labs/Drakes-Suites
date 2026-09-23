# ⚡ Suite 1: DrakesTech (`drakes-tech.jar`)

> **Logística Digital, Almacenamiento Cuántico y Maquinaria Pesada de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), balance de recetas, optimización de transferencia de ítems o agregado de máquinas para los siguientes addons **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **Networks** (Cables, Network Bridges, Terminales, Monitors)
> * **InfinityExpansion** (Singularidades, reactores de vacío, materiales infinitos)
> * **DynaTech** (Generadores cuánticos, auto-crafters, wireless energy)
> * **FastMachines / Supreme / Nanotech** (Maquinaria overclockeada y procesadores)
> * **FluffyMachines / FoxyMachines** (Auto-procesadores, transportadores)
> * **SensibleToolbox / EMCTech / Quaptics / SimpleStorage**
> 
> *Los repositorios independientes antiguos de estos addons quedan archivados en modo Read-Only.*

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada tecnología cuenta con su archivo de configuración desacoplado en `plugins/DrakesTech/modules/`:
* `networks.yml`: Capacidad de cables, transferencias por tick, límites por chunk.
* `infinity.yml`: Multiplicadores de singularidades y recetas.
* `dynatech.yml`: Límites de generación y wireless.
* `supreme.yml`, `fastmachines.yml`, `nanotech.yml`, `foxy.yml`, `sensibletoolbox.yml`, `emctech.yml`, `quaptics.yml`.

Recarga en caliente:
```
/drakessuites reload drakes-tech [modulo]
```

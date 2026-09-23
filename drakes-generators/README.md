# ⚡ Suite 4: DrakesGenerators (`drakes-generators.jar`)

> **Matriz Energética, Reactores Nucleares/Solares y Generación de Energía de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), balance de tasas energéticas (J/t) y simulación termodinámica de reactores para los siguientes addons **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **LiteXpansion** (Generadores solares cuánticos, condensadores de vacío)
> * **SMG (Simple Material Generators)** (Generación de lingotes y minerales)
> * **UltimateGenerators2 / BetterNuclearGenerator** (Reactores nucleares, refrigerantes)
> * **EcoPower / SlimefunOreChunks** (Energías renovables y procesamiento de menas)
> 
> *Los repositorios independientes antiguos quedan formalmente archivados en modo Read-Only.*

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada generador cuenta con su archivo de configuración desacoplado en `plugins/DrakesGenerators/modules/`:
* `litexpansion.yml`: Tasas de generación solar y capacidades de buffer.
* `smg.yml`: Tiempos de ciclo para generadores de materiales.
* `ultimategenerators.yml`: Coeficientes de combustible nuclear y refrigeración.
* `ecopower.yml`: Generadores eólicos, hidroeléctricos y geotérmicos.
* `orechunks.yml`, `nuclear_reactor.yml`.

Recarga en caliente:
```
/drakessuites reload drakes-generators [modulo]
```

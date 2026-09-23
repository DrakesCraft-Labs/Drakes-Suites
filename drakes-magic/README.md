# 🔮 Suite 3: DrakesMagic (`drakes-magic.jar`)

> **Magia Arcana, Alquimia, Transmutación y Nigromancia de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, balance mágico, corrección de bugs (`fix`), hechizos, rituales y transmutaciones para los siguientes addons **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **AlchimiaVitae** (Calbazas de transmutación, elixires, infusiones de almas)
> * **Crystamae** (Rituales cristalinos, resonadores y geometrías sagradas)
> * **RelicsOfCthonia** (Reliquias abisales, invocaciones oscuras y sacrificios)
> * **SoulJars / Netheopoiesis** (Captura de almas y transmutación del Nether)
> * **Transcendence / SpiritsUnchained**
> 
> *Los repositorios independientes antiguos quedan formalmente archivados en modo Read-Only.*

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada sistema mágico cuenta con su archivo de configuración desacoplado en `plugins/DrakesMagic/modules/`:
* `alchimia_vitae.yml`: Fórmulas alquímicas y transmutaciones.
* `crystamae.yml`: Resonancia cristalina y tiempos de canalización.
* `relics_cthonia.yml`: Poder de reliquias y penalizaciones del abismo.
* `soul_jars.yml`: Capacidad de contención y tipos de almas.
* `netheopoiesis.yml`, `transcendence.yml`, `spiritsunchained.yml`.

Recarga en caliente:
```
/drakessuites reload drakes-magic [modulo]
```

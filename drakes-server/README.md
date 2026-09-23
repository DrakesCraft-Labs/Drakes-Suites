# ⭐ Suite 7: DrakesServer (`drakes-server.jar`)

> **Star Server Engine: Núcleo de Jugabilidad, Aislamiento de Modalidades y Economía**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), lógica de servidor, aislamiento de mundos, conciliación Tebex y vigilancia macroeconómica **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **Star Engine** (Evolución canónica de Odysseia a **Star**)
> * **InvSwitcher** (Aislamiento hermético de inventarios entre las 5 modalidades: Survival, OneBlock, SkyBlock, CaveBlock, AcidIsland)
> * **PlayerVaultZ** (Bóvedas virtuales seguras con detección anti-dupe de mochilas anidadas)
> * **Economía & Macroeconomía (SII)** (Watchdog contra precios negativos, desbordamientos e inflación en subastas y tiendas)
> * **Tebex / Store Delivery** (Procesamiento idempotente de compras en `purchases.db` con soporte de kits, permisos y comandos)
> * **Telemetría Star Hub & Bridge Rust (`Star-Rust`)**
> 
> *El repositorio individual histórico de Odysseia queda consolidado y absorbido en este artefacto.*

---

## ⚙️ Configuración Modular (`modules/*.yml`)
El motor Star se configura de forma modular en `plugins/DrakesServer/modules/`:
* `star.yml`: Master switch de Star Engine, canal de telemetría y retrocompatibilidad `/odysseia`.
* `invswitcher.yml`: Declaración de modalidades, prefijos de mundos y sincronización de inventarios.
* `playervaultz.yml`: Límite de bóvedas por rango y guardias anti-anidamiento de shulkers/mochilas.
* `economy_watchdog.yml`: Límites de precios mínimos/máximos y suavizado exponencial.

---

## 🧪 Pruebas y Smoke Tests
Compilación y pruebas de compatibilidad con los 13 YAMLs de producción en vivo:
```bash
mvn -pl drakes-server test
```

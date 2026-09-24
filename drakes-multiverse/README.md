# 🌌 Suite Multiverse: DrakesMultiverse (`drakes-multiverse.jar`)

> **Criaturas Multiversales, Bosses y Redes Logísticas Digitales Standalone**  
> Autoría Soberana: **Chagui68** (`Chagui`) | Mantenimiento: Chagui & Tríada SRE | Plataforma: Dual Paper 1.21.11 & Purpur 26.X (Java 21 / 25)

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), nuevas recetas de Quantum Workbench o crafteos vanilla 3x3, adición de bosses multiversales y optimizaciones de logística digital para:
> * **MultiverseNets** (Redes logísticas digitales standalone sin dependencia de Slimefun, cables de enlace, controladores y terminales de acceso)
> * **MultiverseCreatures** (Criaturas místicas, panteones, invocaciones rituales y dimensiones)
> 
> **SE REALIZA EXCLUSIVAMENTE EN ESTE MÓDULO O EN COLABORACIÓN DIRECTA CON CHAGUI68**.
> 
> *Nota canónica:* MultiverseNets es completamente standalone (funciona con mesas vanilla 3x3 y su propio banco de trabajo cuántico) y NO depende de la infraestructura de Slimefun.

---

## 📦 Componentes y Módulos Nativos

### 1. `MultiverseNets` (`com.chagui68.multiversenets`)
El motor de logística digital y almacenamiento masivo creado por Chagui68, completamente integrado en la suite:
* **Standalone Puro:** Funciona sin Slimefun (con puente opcional `SlimefunBridge` si está presente para interactuar con máquinas).
* **Infraestructura de Red:** Controladores, cables, nodos *grabber*, *pusher*, *vacuum*, *purger*, células de memoria T1-T6, terminales digitales fijas e inalámbricas.
* **Mesa de Trabajo Cuántica (Quantum Workbench):** Sistema de ensamblado avanzado con 30 recetas personalizadas.
* **Barriles Infinitos & Guardas Anti-Dupe:** Protección integral en interfaces de usuario (`GuiDupeGuardTest`) contra exploits de inventario.
* **109 Pruebas Unitarias Automatizadas:** Verificadas al 100% tanto en perfil `paper-21` (Paper 1.21.11) como en `purpur-26` (Purpur 26.X).

### 2. `MultiverseCreatures`
* Panteones de jefes dimensionales, rituales cósmicos y entidades divinas concebidas por Chagui68.

---

## 💻 Comandos y Permisos

| Comando | Alias | Permiso | Descripción |
| :--- | :--- | :--- | :--- |
| `/mvnets help` | `/mvn` | `multiversenets.use` | Muestra el menú de ayuda de redes |
| `/mvnets info` | `/mvn info` | `multiversenets.use` | Diagnóstico de red vinculada al jugador |
| `/mvnets devices` | `/mvn devices` | `multiversenets.use` | Catálogo interactivo de dispositivos disponibles |
| `/mvnets give <dispositivo>` | `/mvn give` | `multiversenets.admin` | Otorga dispositivos de red (Solo Staff) |
| `/mvnets doctor` | `/mvn doctor` | `multiversenets.admin` | Inspección profunda de integridad de redes |
| `/mvnets stats` | `/mvn stats` | `multiversenets.admin` | Estadísticas globales de transferencia y memoria |

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Configuración desacoplada en `plugins/DrakesMultiverse/modules/`:
* `nets.yml`: Configuración de enrutamiento asíncrono, ancho de banda de cables por tick y guardas anti-dupe.
* `creatures.yml`: Spawns, habilidades de entidades y recompensas de combate.

Recarga en caliente:
```bash
/drakessuites reload multiverse nets
/drakessuites reload multiverse creatures
```

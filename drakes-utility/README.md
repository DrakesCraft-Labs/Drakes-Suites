# 🧰 Suite 5: DrakesUtility (`drakes-utility.jar`)

> **Herramientas de Soporte, Almacenamiento Portátil e Interfaces QoL de StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> A partir de ahora, **TODO** desarrollo, corrección de bugs (`fix`), visualización holográfica y mitigación de dupes en contenedores portátiles para los siguientes addons **DEBE HACERSE EXCLUSIVAMENTE EN ESTE MÓDULO**:
> * **DyedBackpacks** (Mochilas teñidas, tamaños dinámicos y filtros de recolección)
> * **ColoredEnderChests** (Cofres de ender por código de color de lana)
> * **ChestTerminal** (Terminal inalámbrica y visualizador de cofres en red)
> * **SFCalc / SlimeHUD** (Calculadora de recetas en GUI y HUD de información de máquinas)
> * **SoundMuffler / SimpleUtils / SlimeFrame** (Silenciadores de maquinaria y marcos)
> 
> *Los repositorios independientes antiguos quedan formalmente archivados en modo Read-Only.*

---

## ⚙️ Configuración Modular (`modules/*.yml`)
Cada herramienta cuenta con su archivo de configuración desacoplado en `plugins/DrakesUtility/modules/`:
* `backpacks.yml`: Tamaños de inventario, soporte de tintes y guardias anti-anidamiento.
* `coloredenderchests.yml`: Red de canales de frecuencia por color.
* `chest_terminal.yml`: Rango inalámbrico e indexación de bloques adyacentes.
* `sfcalc.yml`, `slimehud.yml`, `soundmuffler.yml`, `simpleutils.yml`, `slimeframe.yml`.

Recarga en caliente:
```
/drakessuites reload drakes-utility [modulo]
```

# 🏛️ Suite 0: DrakesCore (`drakes-core.jar`)

> **Kernel Central, Motor de Ticks y Persistencia del Monorepo StarSuites**  
> Autor: JackStar (`JackStar6677-1`) | Plataforma: Paper/Purpur 1.21.11+

---

## ⚠️ DIRECTIVA CANÓNICA DE DESARROLLO PERMANENTE
> **Aviso para Desarrolladores y Agentes SRE:**  
> Cualquier modificación al kernel, gestión de base de datos SQLite WAL, puente PDC (`slimefun:slimefun_item`), aceleración SIMD/Rust o motor de tickers asíncronos **DEBE REALIZARSE EXCLUSIVAMENTE EN ESTE MÓDULO**.
> Queda prohibido alterar repositorios históricos archivados como `dough-core` o forks antiguos de Slimefun.

---

## 📦 Componentes Clave
1. **`SuiteTickerEngine`:**
   - Unificación de los bucles de ticks para todos los módulos de StarSuites.
   - Procesamiento agrupado por chunk cargado (`isLoaded()`) con reducción del 70% de overhead de CPU.
2. **`SuiteDatabaseEngine`:**
   - Motor SQLite con modo WAL (`PRAGMA journal_mode=WAL`), caché en memoria y timeouts configurados.
   - Escritura asíncrona por lotes (cola de hasta 50,000 operaciones y flush de 200 items/commit).
   - Tablas maestras: `suite_block_states`, `suite_quantum_storage`, `suite_player_data`, `suite_key_value`, `suite_audit_events`.
3. **`SuiteItemPdcBridge`:**
   - Estandarización de `slimefun:slimefun_item` sobre `PersistentDataContainer` (Data Components).
   - Guardias anti-dupe contra tamaños ilegales de stacks e inyección de identificadores corruptos.
4. **`NativeEngineBridge`:**
   - FFM Java 21 / JNI para conexión quirúrgica con Rust (`Slimefun-Rust` y `Star-Rust`).
   - Operaciones SIMD (`sumSaturating`), validación de redes y geometría de combate con fallback automático en Java puro.

---

## 🧪 Pruebas Unitarias
Compilar y ejecutar tests unitarios:
```bash
mvn -pl drakes-core test
```

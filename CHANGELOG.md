# Changelog

Todos los cambios significativos del proyecto serán documentados en este archivo.
## [0.2.0] - 2026-08-08

#### Agregado

* Implementación de la clase abstracta `GameState` como molde base para las pantallas.
* Creación del gestor de estados de juego (`GameStateManager`) basado en una estructura de pila (`Stack`).
* Configuración del ciclo de vida principal y cámaras (`cam` y `hudCam`) en la clase `Main`.
* Implementación de la pantalla `Play` con movimiento omnidireccional mediante teclas WASD.
* Generación por código de un sprite temporal de 32x32 píxeles utilizando `Pixmap`.
* Cálculo de velocidad del jugador independiente de la tasa de refresco mediante Delta Time.

#### Modificado

* Reorganización de la estructura de paquetes del proyecto a `com.fragmentsofyou` (`states`, `handlers`).
* Ajuste de la versión de Gradle JVM para compatibilidad con la compilación del proyecto.
* Configuración de la tarea de ejecución en el launcher de escritorio (`Lwjgl3Launcher`).
  
## [0.1.0] - 2026-07-14

### Agregado
- Inicialización del proyecto mediante LibGDX Liftoff.
- Creación de los módulos `core` y `lwjgl3`.
- Incorporación de Box2D y Box2DLights como dependencias del proyecto.
- Configuración inicial del archivo `.gitignore`.
- Creación del `README.md` y del `CHANGELOG.md`.
- Publicación de la propuesta formal del proyecto en la Wiki de GitHub.

### Modificado
- Actualización de la configuración de compilación a Java 21 en `build.gradle` (sourceCompatibility y targetCompatibility).
- Reorganización y corrección de formato en `README.md` según requerimientos de la cátedra.
- Ajuste de requisitos técnicos en el `README.md` (Java 21 LTS).
- Clarificación de tecnologías previstas (SQLite y JDBC) en la sección de Tecnologías.

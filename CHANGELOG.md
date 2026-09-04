# Changelog

Todos los cambios significativos del proyecto serán documentados en este archivo.

## [0.6.0] - 2026-09-03

### Agregado
- **Sistema de partículas de impacto:** Efecto visual (`ParticleEffect`) disparado al impactar la sobrecarga de la linterna sobre el enemigo.
- **Indicadores numéricos en HUD:** Visualización de valores numéricos (`actual / máximo`) para la salud del jugador y la energía de la linterna.
- **Módulo `AudioManager`:** Clase dedicada para la carga, reproducción y control de volumen de pistas ambientales y efectos sonoros.
- **Módulo `HUD`:** Clase encapsulada encargada del renderizado de barras de estado y tipografía en su propio `Viewport`.

### Modificado
- **Refactorización de `Play.java`:** Desacoplamiento de la lógica de sonido e interfaz de usuario para mejorar la legibilidad y cumplir el principio de responsabilidad única (SRP).
- **Calibración visual de partículas:** Reducción de escala, velocidad y tiempo de vida (`Life`/`Duration`) de las partículas para adaptarlas a la resolución nativa de 320x180.
- **Ajuste de audio:** Atenuación del volumen base de la música ambiental para priorizar los efectos de sonido de combate.

### Corregido
- Normalización de rutas y nombres de archivos de assets para compatibilidad con el empaquetado de LibGDX.

## [0.5.0] - 2026-09-02

### Añadido
- Lógica de combate cuerpo a cuerpo polimórfica en la clase base `Enemigo`, incluyendo cálculo de distancia euclídea (`rangoAtaque`) y temporizador de enfriamiento (`cooldownTotal`/`cooldownActual`).
- Sistema de gestión de energía en la clase `Linterna`, integrando consumo por disparo de sobrecarga y recarga progresiva basada en tiempo (`dt`) al mantener presionada la tecla `R`.
- Mecanismo de impacto único (`danioAplicado`, `registrarImpacto`) para aplicar daño plano instantáneo con la sobrecarga en vez de acumulación continua por frame (metodo viejo).
- Transición de fin de partida delegada al `GameStateManager` (`GAMEOVER`) cuando la salud del jugador llega a 0.
- Desacoplamiento del enumerador de orientaciones (`Direction8`) para el control de sprites en `EightDirectionalAnimator`.

### Modificado
- Reducción del tiempo de sobrecarga (`duracionFlash`) a 0.18 segundos para un efecto visual más seco e inmediato.
- Interfaz (HUD) en `Play` actualizada para consumir valores dinámicos reales mediante getters de `Jugador` y `Linterna` en lugar de variables locales fijas.
- Detención explícita del audio ambiental (`musicaAmbiente.stop()`) antes de su liberación en el método `dispose()` de `Play`.

### Corregido
- Vulnerabilidad de desincronización y persistencia de daño en bucle al impactar con el haz de sobrecarga.
- Desacoplamiento de barras de vida y energía en el HUD que impedía reflejar el daño recibido y el gasto de batería.

## [0.4.0] -2026-08-27

### Añadido
- Jerarquía de clases: Entidad -> Enemigo -> Mecento.
- Sistema de combate con daño continuo , instantáneo , feedback de impacto y muerte.
- Cuerpos de Box2D en colisiones para sombras de iluminación dinámica (linterna con colisiones).

### Modificado
- Jugador y Enemigo refactorizado para heredar de Entidad.
- Creacion de clase Mecento como clase hija de enemigo.
- MapCollision optimizado para evitar instanciación constante de objetos en memoria.

### Corregido
- Daño continuo que no se aplicaba por problemas de enteros (cambiado a float).
- Ataque de sobrecarga atravesando obstáculos sólidos.

## [0.3.0] - 2026-08-27

#### Agregado

* Implementación de la clase Linterna y adición de las dos primeras mecánicas de ataque (sin daño incluido).

* Creación de la clase Menu y adición de los assets correspondientes a la interfaz de inicio.

* Implementación de la clase Enemigo junto con sus sprites asociados.

* Creación de CharacterAnimator, adición de animaciones de movimiento para el protagonista y definición del enum Direction.

* Integración de iluminación dinámica mediante Box2DLights y ajuste de resolución general a formato 16:9.

* Implementación de la clase Jugador, desacoplamiento de la entrada de teclado/mouse y optimización del sistema de colisiones.

* Creación y carga del mapa, implementación de la clase de colisiones, gestión de colisiones y definición del punto de spawn.

* Configuración inicial del mapa en Tiled provisorio con FitViewport.

* Implementación de la rotación del personaje/objeto hacia la posición del mouse y adición de imagen PNG provisoria.

#### Modificado

Corrección de errores en el menú principal y en la carga de archivos exportados desde Tiled.

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

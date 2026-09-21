# Fragments of You

**Integrantes:** Rocío Abril Cuello, Valentino Dacal

## Descripción
El proyecto, “Fragments of You”, surge como una propuesta para la materia de LPOO. El juego se centra en Glenn,
personaje protagonista que atraviesa el duelo por la pérdida y el asesinato de su abuela. A través de una narrativa metafórica y sentimental, el 
jugador deberá superar enemigos que representan los sentimientos durante el duelo, buscando que la jugabilidad no sea solamente un desafío 
técnico, sino también una experiencia psicológica.

## Tecnologías
* **Lenguaje:** Java 21 (JDK 21 LTS)
* **Framework:** LibGDX (v1.14.2.0)
* **Físicas:** Box2D
* **Iluminación:** Box2DLights
* **Persistencia:** SQLite y JDBC (Tecnologías previstas para etapas posteriores).
* **Plataforma:** Escritorio (LWJGL3)


## Cómo compilar y ejecutar
Para compilar y ejecutar el proyecto, es necesario tener instalado **JDK 21** en el sistema.

1. Clona este repositorio en tu equipo local.
2. Abre la carpeta raíz del proyecto en **IntelliJ IDEA**.
3. El proyecto será reconocido automáticamente como un proyecto Gradle.
4. Una vez sincronizado, abre una terminal posicionada en la **carpeta raíz** del proyecto y ejecuta el comando correspondiente según tu sistema operativo:

   * **Windows:** `gradlew.bat lwjgl3:run`
   * **Linux/macOS:** `./gradlew lwjgl3:run`

## Estado actual
* **Pre-entrega N°2 :** Prototipo funcional con ciclo de juego cerrado y arquitectura orientada a objetos:
  * **Gestión de Estados (`states`):** Flujo de navegación completo administrado por una administrador estados (`GameStateManager`), integrando Menú principal, Estado jugable (`Play`) y Pantalla de Game Over (Proximamente se agregara Settings).
  * **Jerarquía de Entidades y Animaciones (`entities` / `animadores`):** Modelo de herencia (`Entidad` base extendida a `Jugador` y `Enemigo`/`Mecento`). Sistema de animaciones con soporte de movimiento en 8 direcciones (`EightDirection`).
  * **Mecánicas y Armamento (`armas`):** Lógica de combate centrada en la `Linterna` (gestión de energía, aturdimiento por destello, sobrecarga) y feedback visual reactivo mediante partículas (`ParticleEffect`).
  * **Mundo, Colisiones e Iluminación:** Carga de mapas ortogonales de Tiled, comprobación de obstáculos y línea de visión, junto con un sistema de sombras y luz ambiental en tiempo real montado sobre `Box2DLights` y `RayHandler`.
  * **Módulos de Soporte y UI (`handlers`):** Se creo interfaz gráfica (`HUD`) con barras de estado e indicadores numéricos en tiempo real, y control centralizado de música y efectos sonoros (`AudioManager`).
    
### Enlace al video de demostración:
[Ver video del boceto jugable](https://drive.google.com/file/d/1Y5e2poWt0CtV-W30OcwiVuI6Nn_TUc6O/view?usp=sharing)

## Documentación
El historial de cambios y la propuesta formal del proyecto se encuentran en la Wiki y en el archivo CHANGELOG.md.

**Enlace a la Wiki del Proyecto (Propuesta Detallada):**
[Ver la Propuesta Completa del Proyecto](https://github.com/RocioCuello7/Proyecto-Final-Fragments-of-You/wiki/Propuesta-del-Proyecto-%E2%80%90-Fragments-of-You)

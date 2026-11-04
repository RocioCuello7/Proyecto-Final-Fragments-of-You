package com.fragmentsofyou.misiones;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.fragmentsofyou.entities.Jugador;
import com.fragmentsofyou.entities.Shadox;
import com.fragmentsofyou.handlers.AudioManager;
import com.fragmentsofyou.handlers.MapCollision;

public class GestorCuras {

    private Rectangle rectCura1;
    private Rectangle rectCura2;
    private Rectangle rectCura3;

    private boolean cura1Recolectada = false;
    private boolean cura2Recolectada = false;
    private boolean cura3Recolectada = false;

    private boolean cura1Disponible = false;
    private boolean curasFinalesDisponibles = false;

    public GestorCuras(MapCollision mapCollision) {
        rectCura1 = mapCollision.obtenerRectanguloPorNombre("cura1");
        rectCura2 = mapCollision.obtenerRectanguloPorNombre("cura2");
        rectCura3 = mapCollision.obtenerRectanguloPorNombre("cura3");
    }

    public void update(Rectangle rectJugador, Jugador jugador, AudioManager audio, Shadox shadox) {
        if (shadox != null) {
            if (shadox.getFaseActual() >= 2) {
                cura1Disponible = true;
            }

            if (shadox.getFaseActual() >= 3) {
                curasFinalesDisponibles = true;
            }
        }

        if (cura1Disponible && !cura1Recolectada
            && rectCura1 != null
            && rectJugador.overlaps(rectCura1)
            && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            cura1Recolectada = true;
            audio.agarrarObjeto.play();
            jugador.setVidaJugador(30);
        }

        if (curasFinalesDisponibles && !cura2Recolectada
            && rectCura2 != null
            && rectJugador.overlaps(rectCura2)
            && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            cura2Recolectada = true;
            audio.agarrarObjeto.play();
            jugador.setVidaJugador(30);
        }

        if (curasFinalesDisponibles && !cura3Recolectada
            && rectCura3 != null
            && rectJugador.overlaps(rectCura3)
            && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            cura3Recolectada = true;
            audio.agarrarObjeto.play();
            jugador.setVidaJugador(30);
        }
    }

    public boolean isCurasFinalesDisponibles() { return curasFinalesDisponibles; }
    public Rectangle getRectCura1() { return rectCura1; }
    public Rectangle getRectCura2() { return rectCura2; }
    public Rectangle getRectCura3() { return rectCura3; }
    public boolean isCura1Recolectada() { return cura1Recolectada; }
    public boolean isCura2Recolectada() { return cura2Recolectada; }
    public boolean isCura3Recolectada() { return cura3Recolectada; }
    public boolean isCura1Disponible() { return cura1Disponible; }
}

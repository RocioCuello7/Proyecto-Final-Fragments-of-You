package com.fragmentsofyou.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.fragmentsofyou.entities.Jugador;

public class ZonaTP {
    private DialogoManager dialogoManager;
    private Rectangle area;
    private Vector2 destino;
    private boolean requiereObjeto;
    private boolean activada = false;

    public ZonaTP(Rectangle area, Vector2 destino, boolean requiereObjeto, DialogoManager dialogoManager) {
        this.area = area;
        this.destino = destino;
        this.requiereObjeto = requiereObjeto;
        this.dialogoManager = dialogoManager;
    }

    public boolean verificarYActivar(Jugador jugador, ScreenFader fader, boolean objetoEncontrado) {
        if (activada || area == null) return false;

        Rectangle rectJugador = new Rectangle(jugador.getX() - 8f, jugador.getY() - 8f, 16f, 16f);

        if (rectJugador.overlaps(area)) {
            if (requiereObjeto && !objetoEncontrado) {
                if (dialogoManager != null && !dialogoManager.isMostrandoDialogo()) {
                }
                return false;
            }

            fader.startFadeOut(Color.BLACK, 1.5f);
            jugador.setPuedoMoverme(false);
            activada = true;
            return true;
        }
        return false;
    }

    public Vector2 getDestino() {
        return destino;
    }

    public boolean isActivada() {
        return activada;
    }

    public void reset() {
        activada = false;
    }
}

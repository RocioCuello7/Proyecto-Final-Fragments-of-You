package com.fragmentsofyou.misiones;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.fragmentsofyou.entities.Jugador;
import com.fragmentsofyou.handlers.ScreenFader;

public class GestorTeletransporte {

    private boolean esperandoTP = false;
    private float timerDelayTP = 0f;
    private final float TIEMPO_DELAY = 0.8f;
    private Vector2 destinoActualTP;

    public void iniciar(Vector2 destino) {
        esperandoTP = true;
        timerDelayTP = TIEMPO_DELAY;
        destinoActualTP = destino;
    }

    public void update(float dt, Jugador jugador, ScreenFader fader) {
        if (esperandoTP) {
            timerDelayTP -= dt;
            if (timerDelayTP <= 0) {
                esperandoTP = false;
                if (destinoActualTP != null) {
                    jugador.teletransportar(destinoActualTP.x, destinoActualTP.y);
                }
                fader.startFlash(Color.BLACK, 2.0f);
                jugador.setPuedoMoverme(true);
            }
        }
    }
}

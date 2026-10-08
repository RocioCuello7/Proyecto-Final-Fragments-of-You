package com.fragmentsofyou.handlers;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.fragmentsofyou.entities.Abuela;
import com.fragmentsofyou.entities.Jugador;
import com.fragmentsofyou.enumeradores.EstadoMision;

public class MisionManager {

    private EstadoMision misionActual = EstadoMision.HABLAR_CON_ABUELA;

    private Rectangle rectCama;
    private Rectangle rectNota;
    private Rectangle rectPuerta;
    private Vector2 puntoDestinoTP;

    private boolean abuelaPresente =true;
    private boolean puertaUsada = false;
    private boolean transicionPuertaUsada = false;
    private boolean esperandoTP = false;
    private float timerDelayPuerta = 0f;
    private final float TIEMPO_DELAY = 0.8f;

    public MisionManager(MapCollision mapCollision, Vector2 puntoDestinoTP){
        this.rectCama = mapCollision.obtenerRectanguloPorNombre("cama");
        this.rectNota = mapCollision.obtenerRectanguloPorNombre("nota-abuela");
        this.rectPuerta = mapCollision.obtenerRectanguloPorNombre("puertacasa");
        this.puntoDestinoTP = puntoDestinoTP;
    }

    public void update(float dt, Jugador jugador, Abuela abuela, ScreenFader fader, RayHandler rayHandler){
        Rectangle rectJugador = new Rectangle(jugador.getX() - 8f, jugador.getY() - 8f, 16f, 16f);

        if(abuelaPresente && abuela!=null){
            abuela.update(dt, jugador.getX(),jugador.getY());

            if(abuela.isDialogoTerminado() && misionActual == EstadoMision.HABLAR_CON_ABUELA){
                misionActual = EstadoMision.IR_A_DORMIR;
           }
        }

        if(misionActual == EstadoMision.IR_A_DORMIR && rectCama != null){
            if(Gdx.input.isKeyJustPressed(Input.Keys.E) && rectJugador.overlaps(rectCama)){
                misionActual = EstadoMision.COMPLETADO;
                fader.startFadeOut(Color.BLACK,1.5f);
            }
        }

        if (misionActual == EstadoMision.COMPLETADO && fader.isFinished()) {
            fader.startFlash(Color.BLACK, 2.0f);
            misionActual = EstadoMision.EXPLORAR;
            rayHandler.setAmbientLight(0.07f);
            abuelaPresente = false;
        }

        if(misionActual== EstadoMision.EXPLORAR && !jugador.isDialogoNotaTerminado() && rectNota!=null){
            if(rectJugador.overlaps(rectNota) && Gdx.input.isKeyJustPressed(Input.Keys.E)){
                if(!jugador.isMostrandoDialogo()) {
                    String[] textoNota = new String[]{
                        "¿Qué es esto ... ?",
                        ". . . ",
                        " donde esta la           abuela ? "
                    };
                    jugador.iniciarDialogoNota(textoNota);
                }
            }
        }

        if (jugador.isDialogoNotaTerminado() && !puertaUsada && rectPuerta != null) {
            if (rectJugador.overlaps(rectPuerta)) {
                fader.startFadeOut(Color.BLACK, 1.5f);
                transicionPuertaUsada = true;
                puertaUsada = true;
                jugador.setPuedoMoverme(false);
            }
        }

        if (transicionPuertaUsada && fader.isFinished()) {
            transicionPuertaUsada = false;
            esperandoTP = true;
            timerDelayPuerta = TIEMPO_DELAY;
        }

        if(esperandoTP){
            timerDelayPuerta -=dt;
            if (timerDelayPuerta<=0){
                esperandoTP=false;
                if(puntoDestinoTP!=null){
                    jugador.teletransportar(puntoDestinoTP.x,puntoDestinoTP.y);
                }
                fader.startFlash(Color.BLACK,2.0f);
                jugador.setPuedoMoverme(true);
            }
        }

    }

    public EstadoMision getMisionActual() { return misionActual; }
    public boolean isAbuelaPresente() { return abuelaPresente; }

}

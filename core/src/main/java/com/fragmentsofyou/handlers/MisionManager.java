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

    private ZonaTP puertaCasaTP;
    private ZonaTP puertaPasilloTP2;
    private ZonaTP puertaSalidaTP1;
    private ZonaTP puertaSiguienteTP3;

    private boolean esperandoTP = false;
    private float timerDelayTP = 0f;
    private final float TIEMPO_DELAY = 0.8f;
    private Vector2 destinoActualTP;

    private EstadoMision misionActual = EstadoMision.HABLAR_CON_ABUELA;

    private Rectangle rectCama;
    private Rectangle rectNota;
    private Rectangle rectObjetoAbuela;

    private MapCollision mapCollision;
    private Vector2 puntoDestinoTP;
    private Vector2 puntoDestinoTP2;

    private boolean abuelaPresente =true;
    private boolean enemigoSpawned = false;
    private boolean enemigoDerrotado=false;
    private boolean objetoEncontrado = false;

    private Vector2 tp3;

    public MisionManager(MapCollision mapCollision, Vector2 tp1, Vector2 tp2, Vector2 tp3){
        this.mapCollision=mapCollision;
        this.rectCama = mapCollision.obtenerRectanguloPorNombre("cama");
        this.rectNota = null;
        this.rectObjetoAbuela=mapCollision.obtenerRectanguloPorNombre("cepilloDientesAbuela");

        this.puertaCasaTP = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertacasa"), tp1, false);
        this.puertaPasilloTP2 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaPasillo"), tp2, false);
        this.puertaSalidaTP1 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaSalidaCombate1"), tp1, true);
        this.puertaSiguienteTP3 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaSiguiente"), tp3, true);
        this.tp3 = tp3;

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

            rectNota=mapCollision.obtenerRectanguloPorNombre("nota-abuela");
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

        if (jugador.isDialogoNotaTerminado() && puertaCasaTP.verificarYActivar(jugador, fader, objetoEncontrado)) {
            esperandoTP = true;
            timerDelayTP = TIEMPO_DELAY;
            destinoActualTP = puertaCasaTP.getDestino();
        }

        if (jugador.isDialogoNotaTerminado() && puertaPasilloTP2.verificarYActivar(jugador, fader, objetoEncontrado)) {
            esperandoTP = true;
            timerDelayTP = TIEMPO_DELAY;
            if (objetoEncontrado) {
                destinoActualTP = tp3;
            } else {
                destinoActualTP = puertaPasilloTP2.getDestino();
                enemigoSpawned = true;
                misionActual = EstadoMision.COMBATE_Y_BUSQUEDA;
            }
        }

        if (puertaSalidaTP1.verificarYActivar(jugador, fader, objetoEncontrado)) {
            if (objetoEncontrado) {
                esperandoTP = true;
                timerDelayTP = TIEMPO_DELAY;
                destinoActualTP = puertaSalidaTP1.getDestino();
            }
        }

        if (puertaSiguienteTP3.verificarYActivar(jugador, fader, objetoEncontrado)) {
            esperandoTP = true;
            timerDelayTP = TIEMPO_DELAY;
            destinoActualTP = puertaSiguienteTP3.getDestino();
        }

        if(esperandoTP){
            timerDelayTP -= dt;
            if (timerDelayTP <= 0){
                esperandoTP = false;
                if(destinoActualTP != null){
                    jugador.teletransportar(destinoActualTP.x, destinoActualTP.y);
                }
                fader.startFlash(Color.BLACK, 2.0f);
                jugador.setPuedoMoverme(true);
            }
        }

        if(!objetoEncontrado && rectObjetoAbuela != null && rectJugador.overlaps(rectObjetoAbuela)){
            if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
                if(!jugador.isMostrandoDialogo()){
                    String[] textoObjeto = new String[]{
                        "¡Encontré el cepillo de abuela!",
                        "Esto significa que ella debe",
                        "estar cerca... No me voy a rendir."
                    };
               jugador.iniciarDialogoNota(textoObjeto);
                objetoEncontrado=true;
                puertaPasilloTP2.reset();
                misionActual = EstadoMision.BUSQUEDA;

                }
            }
        }

    }

    public EstadoMision getMisionActual() { return misionActual; }
    public boolean isAbuelaPresente() { return abuelaPresente; }

    public Rectangle getRectNota() {
        return rectNota;
    }

    public boolean debeSpawnarEnemigo() {
        return enemigoSpawned && !enemigoDerrotado;
    }
    public boolean isObjetoEncontrado(){
        return objetoEncontrado;
    }
    public Rectangle getRectObjetoAbuela(){
        return rectObjetoAbuela;
    }

    public void setEnemigoDerrotado(boolean derrotado){
        this.enemigoDerrotado=derrotado;
    }
}

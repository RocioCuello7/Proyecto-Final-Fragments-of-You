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
    private ZonaTP puertaSalidaCombate2;
    private ZonaTP puertaSiguienteTP4;
    private ZonaTP puertasalidaCombate3;

    private boolean esperandoTP = false;
    private boolean esperandoDespertar=false;

    private float timerDelayTP = 0f;
    private final float TIEMPO_DELAY = 0.8f;
    private Vector2 destinoActualTP;

    private EstadoMision misionActual = EstadoMision.HABLAR_CON_ABUELA;

    private Rectangle rectCama;
    private Rectangle rectLinterna;
    private Rectangle rectNota;
    private Rectangle rectObjetoAbuela;
    private Rectangle rectEspejoAbuela;
    private Rectangle rectFotoFinal;
    private Rectangle rectAbuela;

    private MapCollision mapCollision;

    private boolean abuelaPresente =true;
    private boolean linternaRecolectada=false;
    private boolean enemigoSpawned = false;
    private boolean enemigoDerrotado=false;
    private boolean objetoEncontrado = false;
    private boolean espejoRecolectado=false;
    private boolean notaLeida=false;
    private boolean fotoFinalRecolectada=false;

    private boolean argosSpawned=false;

    private boolean primerEnemigoDerrotado = false;
    private boolean todosEnemigosDerrotados = false;
    private boolean jefeDerrotado = false;

    private Vector2 tp1;
    private Vector2 tp2;
    private Vector2 tp3;
    private Vector2 tp4;

    private DialogoManager dialogoManager;

    public MisionManager(MapCollision mapCollision, Vector2 tp1, Vector2 tp2, Vector2 tp3, DialogoManager dialogoManager){
        this.mapCollision=mapCollision;
        this.tp1=tp1;
        this.tp2 = tp2;
        this.tp3 = tp3;


        this.dialogoManager = dialogoManager;

        this.rectCama = mapCollision.obtenerRectanguloPorNombre("cama");
        this.rectLinterna =null;
        this.rectNota = null;
        this.rectObjetoAbuela=mapCollision.obtenerRectanguloPorNombre("cepilloDientesAbuela");
        this.rectEspejoAbuela=mapCollision.obtenerRectanguloPorNombre("espejoAbuela");
        this.rectFotoFinal=mapCollision.obtenerRectanguloPorNombre("fotoFinal");
        this.rectAbuela=mapCollision.obtenerRectanguloPorNombre("abuela");

        this.puertaCasaTP = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertacasa"), tp1, false, dialogoManager);

        this.puertaPasilloTP2 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaPasillo"), tp2, false, dialogoManager);
        this.puertaSalidaTP1 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaSalidaCombate1"), tp1, true, dialogoManager);

        this.puertaSiguienteTP3 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaPasillo"), tp3, true, dialogoManager);
        this.puertaSalidaCombate2 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaSalidaCombate2"), tp1, true, dialogoManager);

        this.puertaSiguienteTP4 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaPasillo"), tp4, true, dialogoManager);
        this.puertasalidaCombate3 = new ZonaTP(mapCollision.obtenerRectanguloPorNombre("puertaSalidaCombate3"), tp1, true, dialogoManager);
    }

    public void update(float dt, Jugador jugador, Abuela abuela, ScreenFader fader, RayHandler rayHandler, AudioManager audio){
            Rectangle rectJugador = new Rectangle(jugador.getX() - 8f, jugador.getY() - 8f, 16f, 16f);

            if (abuelaPresente && abuela != null) {
                abuela.update(dt, jugador.getX(), jugador.getY(), jugador, dialogoManager);

                if (abuela.isDialogoTerminado() && misionActual == EstadoMision.HABLAR_CON_ABUELA) {
                    misionActual = EstadoMision.IR_A_DORMIR;
                }
            }

            if (misionActual == EstadoMision.IR_A_DORMIR && rectCama != null) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.E) && rectJugador.overlaps(rectCama)) {
                    esperandoDespertar = true;
                    fader.startFadeOut(Color.BLACK, 1.5f);
                }
            }

            if (esperandoDespertar && fader.isFinished()) {
                esperandoDespertar = false;
                fader.startFlash(Color.BLACK, 2.0f);
                misionActual = EstadoMision.EXPLORAR;
                rayHandler.setAmbientLight(0.07f);
                abuelaPresente = false;

                rectNota = mapCollision.obtenerRectanguloPorNombre("nota-abuela");
                rectLinterna = mapCollision.obtenerRectanguloPorNombre("linternaGlenn");
            }

            if (misionActual == EstadoMision.EXPLORAR && !linternaRecolectada && rectLinterna != null) {
                if (rectJugador.overlaps(rectLinterna) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    if (!dialogoManager.isMostrandoDialogo()) {
                        String[] textoLinterna = new String[]{
                            "Encontré la linterna de la abuela.",
                            "Ahora debería poder alumbrar",
                            "en esta oscuridad."
                        };
                        dialogoManager.iniciarDialogo(textoLinterna, jugador);
                        linternaRecolectada = true;
                        jugador.setLinternaHabilitada(true);
                    }
                }
            }

            if (misionActual == EstadoMision.EXPLORAR && !notaLeida && rectNota != null) {
                if (rectJugador.overlaps(rectNota) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    if (!dialogoManager.isMostrandoDialogo()) {
                        String[] textoNota = new String[]{
                            "¿Qué es esto ... ?",
                            ". . . ",
                            " donde esta la           abuela ? "
                        };
                        dialogoManager.iniciarDialogo(textoNota, jugador);
                        notaLeida=true;
                    }
                }
            }

        if (!dialogoManager.isMostrandoDialogo() && puertaCasaTP.verificarYActivar(jugador, fader, objetoEncontrado)) {
                esperandoTP = true;
                timerDelayTP = TIEMPO_DELAY;
                destinoActualTP = puertaCasaTP.getDestino();
            }

        if (!dialogoManager.isMostrandoDialogo() && puertaPasilloTP2.verificarYActivar(jugador, fader, objetoEncontrado)) {
                esperandoTP = true;
                timerDelayTP = TIEMPO_DELAY;
                if (objetoEncontrado) {
                    destinoActualTP = tp3;
                    argosSpawned = true;
                    misionActual = EstadoMision.COMBATE_Y_BUSQUEDA;
                    jugador.setDestelloHabilitado(true);
                } else {
                    destinoActualTP = puertaPasilloTP2.getDestino();
                    enemigoSpawned = true;
                    misionActual = EstadoMision.COMBATE_Y_BUSQUEDA;
                    jugador.setSobrecargaHabilitada(true);
                }
            }

            if (puertaSalidaTP1.verificarYActivar(jugador, fader, objetoEncontrado)) {
                if (objetoEncontrado) {
                    esperandoTP = true;
                    timerDelayTP = TIEMPO_DELAY;
                    destinoActualTP = puertaSalidaTP1.getDestino();
                    misionActual=EstadoMision.EXPLORAR_PASILLO;
                }
            }

            if (espejoRecolectado) {
                if (puertaSalidaCombate2.verificarYActivar(jugador, fader, true)) {
                    esperandoTP = true;
                    timerDelayTP = TIEMPO_DELAY;
                    destinoActualTP = tp1;
                    misionActual=EstadoMision.EXPLORAR_PASILLO;
                }
            }

            if (esperandoTP) {
                timerDelayTP -= dt;
                if (timerDelayTP <= 0) {
                    esperandoTP = false;
                    if (destinoActualTP != null) {
                        jugador.teletransportar(destinoActualTP.x, destinoActualTP.y);
                    }
                    fader.startFlash(Color.BLACK, 2.0f);
                    jugador.setPuedoMoverme(true);
                    audio.pausarMusicaAmbiente();
                    audio.iniciarMusicaAngel();
                }
            }

            if (puertaSiguienteTP3.verificarYActivar(jugador, fader, objetoEncontrado)) {
                esperandoTP = true;
                timerDelayTP = TIEMPO_DELAY;
                destinoActualTP = puertaSiguienteTP3.getDestino();
                argosSpawned = true;

                jugador.setDestelloHabilitado(true);
            }

            if (!objetoEncontrado && rectObjetoAbuela != null && rectJugador.overlaps(rectObjetoAbuela)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    if (!dialogoManager.isMostrandoDialogo()) {
                        String[] textoObjeto = new String[]{
                            "¡Encontré el cepillo de abuela!",
                            "Esto significa que ella debe",
                            "estar cerca... No me voy a rendir."
                        };
                        dialogoManager.iniciarDialogo(textoObjeto, jugador);
                        objetoEncontrado = true;
                        puertaPasilloTP2.reset();
                        misionActual = EstadoMision.BUSCAR_SALIDA;
                        jugador.setVidaJugador(80);
                    }
                }
            }

            if (!espejoRecolectado && rectEspejoAbuela != null && rectJugador.overlaps(rectEspejoAbuela)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    if (!dialogoManager.isMostrandoDialogo()) {
                        String[] textoEspejo = new String[]{
                            "Encontré el espejo de la abuela . . .",
                            "Siento que la voy a encontrar pronto."
                        };
                        dialogoManager.iniciarDialogo(textoEspejo, jugador);
                        espejoRecolectado = true;
                        puertaSiguienteTP3.reset();
                        misionActual = EstadoMision.BUSCAR_SALIDA;
                        jugador.setVidaJugador(80);
                    }
                }
            }

            if (objetoEncontrado && espejoRecolectado) {
                if (puertaSiguienteTP4.verificarYActivar(jugador, fader, true)) {
                    esperandoTP = true;
                    timerDelayTP = TIEMPO_DELAY;
                    destinoActualTP = tp4;
                    misionActual = EstadoMision.FASES_FINALES;
                }
            }

            if (puertasalidaCombate3.verificarYActivar(jugador, fader, true)) {
                esperandoTP = true;
                timerDelayTP = TIEMPO_DELAY;
                destinoActualTP = tp1;
            }

            if (jefeDerrotado && !fotoFinalRecolectada && rectFotoFinal != null && rectJugador.overlaps(rectFotoFinal)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    fotoFinalRecolectada = true;
                }
            }
    }

    public String getTextoIndicador(Jugador jugador){
        if(dialogoManager.isMostrandoDialogo()) return null;

        Rectangle rectJugador=new Rectangle(jugador.getX()-8f, jugador.getY()-8f,16f,16f);

        if(abuelaPresente && rectAbuela!=null && rectJugador.overlaps(rectAbuela)){
            return "| E | Hablar";
        }

        if(misionActual==EstadoMision.IR_A_DORMIR && rectCama!=null && rectJugador.overlaps(rectCama)){
            return "| E | Dormir";
        }

        if(misionActual==EstadoMision.EXPLORAR && !linternaRecolectada && rectLinterna!=null && rectJugador.overlaps(rectLinterna)){
            return "| E | Recoger Linterna";
        }

        if(misionActual==EstadoMision.EXPLORAR && rectNota!=null && rectJugador.overlaps(rectNota)){
            return "| E | Leer Nota";
        }

        if(!objetoEncontrado && rectObjetoAbuela!=null && rectJugador.overlaps(rectObjetoAbuela)){
            return "| E | Recoger Fragmento";
        }

        if(!espejoRecolectado && rectEspejoAbuela!=null && rectJugador.overlaps(rectEspejoAbuela)){
            return "| E | Recoger Fragmento";
        }

        if(jefeDerrotado && !fotoFinalRecolectada && rectFotoFinal!=null && rectJugador.overlaps(rectFotoFinal)){
            return "| E | Recoger Fragmento";
        }

        if (misionActual == EstadoMision.COMBATE_Y_BUSQUEDA) {
            if (jugador.isDestelloHabilitado()) {
                return "[R] Recargar | [Click Izq] Disparar | [Click Der] Destello";
            } else {
                return "[R] Recargar | [Click Izq] Disparar";
            }
        }

        if (linternaRecolectada) {
            return "[F] Activar linterna";
        }
        return null;
    }

    public boolean debeSpawnarArgos() {
        if (argosSpawned) {
            argosSpawned = false;
            return true;
        }
        return false;
    }
    public boolean debeSpawnarEnemigo() {
        return enemigoSpawned && !primerEnemigoDerrotado;
    }

    public boolean isPrimerEnemigoDerrotado() {
        return primerEnemigoDerrotado;
    }

    public void setPrimerEnemigoDerrotado(boolean derrotado) {
        this.primerEnemigoDerrotado = derrotado;
    }

    public boolean isTodosEnemigosDerrotados() {
        return todosEnemigosDerrotados;
    }

    public void setTodosEnemigosDerrotados(boolean derrotados) {
        this.todosEnemigosDerrotados = derrotados;
        this.enemigoDerrotado = derrotados;
    }

    public EstadoMision getMisionActual() { return misionActual; }
    public Rectangle getRectNota() {return rectNota;}
    public Rectangle getRectObjetoAbuela(){return rectObjetoAbuela;}
    public Rectangle getRectLinterna() {return rectLinterna;}
    public Rectangle getRectEspejoAbuela() {return rectEspejoAbuela;}

    public boolean isAbuelaPresente() { return abuelaPresente; }
    public boolean isObjetoEncontrado(){return objetoEncontrado;}
    public boolean isLinternaRecolectada() {return linternaRecolectada;}
    public boolean isEspejoRecolectado() {return espejoRecolectado; }

    public void setEnemigoDerrotado(boolean derrotado){
        this.enemigoDerrotado=derrotado;
    }
}

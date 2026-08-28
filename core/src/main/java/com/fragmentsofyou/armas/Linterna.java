package com.fragmentsofyou.armas;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Linterna {

    private final float DISTANCIA_NORMAL = 115f;
    private final float CONO_NORMAL = 38f;
    private final Color COLOR_NORMAL = new Color(1f, 0.95f, 0.8f, 0.95f);

    private final float DISTANCIA_SOBRECARGA = 180f;
    private final float CONO_SOBRECARGA = 60f;

    private ConeLight linterna;

    private boolean encendida;
    private boolean sobrecargada;

    private float tiempoCooldown;
    private float tiempoCastigo = 4.0f;

    private float duracionFlash=0.6f;
    private float tiempoFlash = 0f;

    private float duracionEfectoSobrecarga = 0.25f;
    private float tiempoEfectoSobrecarga = 0f;
    private float cooldownDestello = 0f;
    private float tiempoEsperaDestello = 5.0f;

    public Linterna(RayHandler rayHandler, float x, float y, float rotacion){
        this.encendida = true;
        this.sobrecargada=false;
        this.tiempoCooldown=0f;

        float rotacionSegura = Float.isNaN(rotacion) ? 0f : rotacion;

        this.linterna = new ConeLight(
            rayHandler,
            64,
            COLOR_NORMAL,
            DISTANCIA_NORMAL,
            x, y,
            rotacionSegura,
            CONO_NORMAL
        );
    }

    public void update(float dt, float x, float y, float rotacion) {
        if (sobrecargada) {
            tiempoCooldown -= dt;
            if (tiempoCooldown <= 0f) {
                tiempoCooldown = 0f;
                sobrecargada = false;
            }
        }

        if (tiempoEfectoSobrecarga > 0f) {
            tiempoEfectoSobrecarga -= dt;
            if (tiempoEfectoSobrecarga <= 0f) {
                tiempoEfectoSobrecarga = 0f;
                linterna.setColor(COLOR_NORMAL);
                linterna.setDistance(DISTANCIA_NORMAL);
                linterna.setConeDegree(CONO_NORMAL);
                encendida = false;
                linterna.setActive(false);
            }
        }

        if (tiempoFlash > 0f) {
            tiempoFlash -= dt;
            if (tiempoFlash < 0f) tiempoFlash = 0f;
        }
        if (cooldownDestello > 0f) {
            cooldownDestello -= dt;
            if (cooldownDestello < 0f) cooldownDestello = 0f;
        }

        linterna.setPosition(x, y);
        if (!Float.isNaN(rotacion)) {
            linterna.setDirection(rotacion);
        }
    }

    public void dispararSobrecarga() {
        if (sobrecargada) return;

        sobrecargada = true;
        tiempoCooldown = tiempoCastigo;
        tiempoEfectoSobrecarga = duracionEfectoSobrecarga;

        linterna.setActive(true);
        linterna.setColor(Color.WHITE);
        linterna.setDistance(DISTANCIA_SOBRECARGA);
        linterna.setConeDegree(CONO_SOBRECARGA);
        linterna.setSoft(false);
    }

    public float getAlphaFlash(){
        if(duracionFlash==0)return 0f;

        return tiempoFlash/duracionFlash;
    }

    public void alternarEncendido(){
        if(sobrecargada){
            return;
        }
        encendida = !encendida;//me gusta esta logica
        linterna.setActive(encendida);
    }

    public void dispararDestello() {
        if (cooldownDestello > 0f) return;

        cooldownDestello = tiempoEsperaDestello;
        tiempoFlash = duracionFlash;
    }

    public boolean estaEnRangoSobrecarga(float px, float py, float rotacion, float ex, float ey) {
        float dx = ex - px;
        float dy = ey - py;
        float distancia = (float) Math.sqrt(dx * dx + dy * dy);

        if (distancia > DISTANCIA_SOBRECARGA) return false;

        float anguloEnemigo = MathUtils.radiansToDegrees * MathUtils.atan2(dy, dx);
        float diff = Math.abs(rotacion - anguloEnemigo);
        if (diff > 180f) diff = 360f - diff;

        return diff <= (CONO_SOBRECARGA / 2f);
    }

    public boolean isSobrecargada() {
        return sobrecargada;
    }

    public void dispose(){
        if(linterna!=null){
            linterna.dispose();
        }
    }
}

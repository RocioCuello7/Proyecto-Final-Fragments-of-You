package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.entities.Enemigo;
import com.fragmentsofyou.entities.Jugador;

import java.util.ArrayList;

public class CombateManager {
    private ParticleEffect efectoSobrecarga;
    private boolean particulaActiva = false;

    public CombateManager() {
        efectoSobrecarga = new ParticleEffect();
        efectoSobrecarga.load(
            Gdx.files.internal("particulas/ParticulaAmarilla.p"),
            Gdx.files.internal("particulas")
        );
        efectoSobrecarga.scaleEffect(0.18f);
    }

    public void update(float dt, Jugador jugador, ArrayList<Enemigo> enemigos , MapCollision mapCollision, AudioManager audio) {
        if (enemigos == null || enemigos.isEmpty()) return;

        for (Enemigo e : enemigos) {
            if (e != null && !e.isMuerto()) {
                e.update(dt, mapCollision);
            }
        }

        if (jugador.getLinterna().puedeHacerDanio()) {
            Enemigo enemigoMasCercano = null;
            float menorDistanciaSq = Float.MAX_VALUE;

            for (Enemigo enemigo : enemigos) {
                if (enemigo != null && !enemigo.isMuerto()) {

                    boolean lineaLibre = mapCollision.hayLineaDeVision(
                        jugador.getX(), jugador.getY(), enemigo.getX(), enemigo.getY()
                    );

                    boolean alcanzada = jugador.getLinterna().estaEnRangoSobrecarga(
                        jugador.getX(), jugador.getY(), jugador.getRotacion(), enemigo.getX(), enemigo.getY()
                    );

                    if (alcanzada && lineaLibre) {
                        float dx = enemigo.getX() - jugador.getX();
                        float dy = enemigo.getY() - jugador.getY();
                        float distanciaSq = dx * dx + dy * dy;

                        if (distanciaSq < menorDistanciaSq) {
                            menorDistanciaSq = distanciaSq;
                            enemigoMasCercano = enemigo;
                        }
                    }
                }
            }

            if (enemigoMasCercano != null) {
                audio.playDestello();
                enemigoMasCercano.relentizar(3.0f);
                enemigoMasCercano.recibirDanio(30f);
                jugador.getLinterna().registrarImpacto();

                efectoSobrecarga.reset();
                efectoSobrecarga.setPosition(enemigoMasCercano.getX() + 3f, enemigoMasCercano.getY() + 3f);
                efectoSobrecarga.start();
                particulaActiva = true;
            }
        }

        if (jugador.consumioDestello()) {

            boolean activado = jugador.getLinterna().dispararDestello();


            if (activado) {
                audio.playDestello();

                if (enemigos != null) {
                    for (Enemigo enemigo : enemigos) {
                        if (enemigo != null && !enemigo.isMuerto()) {
                            enemigo.aturdir(2.0f);
                            enemigo.recibirDanio(25f);
                        }
                    }
                }
            }
        }

        if (particulaActiva) {
            efectoSobrecarga.update(dt);
            if (efectoSobrecarga.isComplete()) {
                particulaActiva = false;
            }
        }
    }

    public void renderParticulas(SpriteBatch sb) {
        if (particulaActiva) {
            efectoSobrecarga.draw(sb);
        }
    }

    public void dispose() {
        if (efectoSobrecarga != null) {
            efectoSobrecarga.dispose();
        }
    }
}

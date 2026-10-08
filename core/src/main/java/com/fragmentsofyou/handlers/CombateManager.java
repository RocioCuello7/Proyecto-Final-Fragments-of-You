package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.entities.Enemigo;
import com.fragmentsofyou.entities.Jugador;

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

    public void update(float dt, Jugador jugador, Enemigo enemigo, MapCollision mapCollision, AudioManager audio) {
        if (enemigo == null) return;

        enemigo.update(dt, mapCollision);

        boolean lineaLibre = mapCollision.hayLineaDeVision(
            jugador.getX(), jugador.getY(), enemigo.getX(), enemigo.getY()
        );

        if (jugador.getLinterna().puedeHacerDanio()) {
            boolean alcanzada = jugador.getLinterna().estaEnRangoSobrecarga(
                jugador.getX(), jugador.getY(), jugador.getRotacion(), enemigo.getX(), enemigo.getY()
            );

            if (alcanzada && lineaLibre) {
                audio.playDestello();
                enemigo.relentizar(3.0f);
                enemigo.recibirDanio(30f);
                jugador.getLinterna().registrarImpacto();

                efectoSobrecarga.reset();
                efectoSobrecarga.setPosition(enemigo.getX() + 3f, enemigo.getY() + 3f);
                efectoSobrecarga.start();
                particulaActiva = true;
            }
        }

        if (jugador.consumioDestello()) {
            audio.playDestello();
            enemigo.aturdir(2.0f);
            enemigo.recibirDanio(25f);
        }

        if (enemigo.isMuerto()) {
            enemigo.dispose();
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

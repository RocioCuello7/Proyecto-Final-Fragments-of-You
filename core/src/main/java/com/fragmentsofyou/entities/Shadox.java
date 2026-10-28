package com.fragmentsofyou.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.animadores.Animacion4Direcciones;
import com.fragmentsofyou.animadores.AnimacionJefe;
import com.fragmentsofyou.enumeradores.EstadoJefe;
import com.fragmentsofyou.handlers.MapCollision;

public class Shadox extends Enemigo{

    private AnimacionJefe animador;

    private boolean atacando = false;
    private boolean impactoAplicado = false;

    private float tiempoAtaque = 0f;

    private static final float MOMENTO_IMPACTO = 0.45f;

    public Shadox(float startX, float startY, Entidad objetivo) {
        super(startX, startY, 24f, 24f, 40f, 150, 15f, 28f, 1.2f, objetivo);

        animador = new AnimacionJefe("enemigos/Shadox.png");

    }

    @Override
    public void update(float dt, MapCollision mapCollision) {

        if (isMuerto()) {
            return;
        }

        if (atacando) {

            tiempoAtaque += dt;

            if (cooldownActual > 0f) {
                cooldownActual -= dt;

                if (cooldownActual < 0f) {
                    cooldownActual = 0f;
                }
            }

            animador.update(dt, dirX);

            if (!impactoAplicado && tiempoAtaque >= MOMENTO_IMPACTO) {

                impactoAplicado = true;

                if (objetivo != null) {
                    float dx = objetivo.getX() - x;
                    float dy = objetivo.getY() - y;

                    float distancia = (float) Math.sqrt(dx * dx + dy * dy);

                    if (distancia <= rangoAtaque) {
                        objetivo.recibirDanio(danio);
                    }
                }
            }

            if (animador.terminoAnimacion()) {
                atacando = false;
                tiempoAtaque = 0f;
                impactoAplicado = false;
                animador.setEstado(EstadoJefe.IDLE);
            }

            return;
        }

        super.update(dt, mapCollision);

        if (atacando) {
            animador.update(dt, dirX);
        } else {
            animador.setEstado(EstadoJefe.IDLE);
            animador.update(dt, dirX);
        }
    }

    @Override
    protected void atacar() {

        if (atacando || objetivo == null) {
            return;
        }

        atacando = true;
        impactoAplicado = false;
        tiempoAtaque = 0f;

        cooldownActual = cooldownTotal;

        animador.setEstado(EstadoJefe.ATAQUE);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(
            animador.getCurrentFrame(),
            x - 16f,
            y - 16f,
            32f,
            32f
        );
    }

    @Override
    public void dispose() {
        animador.dispose();
    }
}

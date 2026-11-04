package com.fragmentsofyou.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.fragmentsofyou.animadores.Animacion4Direcciones;
import com.fragmentsofyou.animadores.AnimacionJefe;
import com.fragmentsofyou.enumeradores.EstadoJefe;
import com.fragmentsofyou.handlers.MapCollision;

import java.util.ArrayList;

public class Shadox extends Enemigo{

    private AnimacionJefe animador;
    private ArrayList<ProyectilOcular> listaProyectiles;

    private boolean atacando = false;
    private boolean impactoAplicado = false;
    private float tiempoAtaque = 0f;
    private static final float MOMENTO_IMPACTO = 0.35f;

    private float temporizadorHabilidad = 0f;
    private float intervaloHabilidad=2.8f;
    private boolean teletransportandose=false;

    private int faseActual = 1;
    private int distanciaAtaqueObligatoria=100;


    public Shadox(float startX, float startY, Entidad objetivo, ArrayList<ProyectilOcular> proyectiles) {
        super(startX, startY, 24f, 24f, 40f, 500, 10f, 28f, 1.2f, objetivo);

        animador = new AnimacionJefe("enemigos/Shadox.png");
        this.listaProyectiles=proyectiles;

    }

    @Override
    public void update(float dt, MapCollision mapCollision) {

        if (isMuerto()) {
            return;
        }
        actualizarFase();
        actualizarVelocidadAtaque();

        if (!atacando && !teletransportandose && objetivo != null) {
            temporizadorHabilidad += dt;

            if (temporizadorHabilidad >= intervaloHabilidad) {
                temporizadorHabilidad = 0f;

                float dx = objetivo.getX() - x;
                float dy = objetivo.getY() - y;
                float distancia = (float) Math.sqrt(dx * dx + dy * dy);

                if (faseActual == 1) {
                    if (distancia <= rangoAtaque) {
                        atacar();
                    }
                }

                else {
                    if (distancia > distanciaAtaqueObligatoria) {
                        dispararCeguera();
                    }
                    else if (distancia <= rangoAtaque) {
                        atacar();
                    }
                    else {
                        float decision = MathUtils.random();

                        if (decision < 0.5f) {
                            iniciarTeletransporte();
                        } else {
                            dispararCeguera();
                        }
                    }
                }
            }
        }

        if(teletransportandose){
            animador.update(dt,dirX);
            if (animador.getEstado() == EstadoJefe.DESAPARECER
                && animador.terminoAnimacion()) {

                buscarPosicionSegura(mapCollision);

                animador.setEstado(EstadoJefe.APARECER);

            } else if (animador.getEstado() == EstadoJefe.APARECER
                && animador.terminoAnimacion()) {

                teletransportandose = false;
                animador.setEstado(EstadoJefe.IDLE);
            }
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
        animador.setEstado(EstadoJefe.IDLE);
        animador.update(dt, dirX);
    }

    private void actualizarFase() {
        float porcentajeVida = (float)vidaActual / vidaMax;

        if (porcentajeVida <= 0.33f) {
            faseActual = 3;
        } else if (porcentajeVida <= 0.66f) {
            faseActual = 2;
        } else {
            faseActual = 1;
        }
    }

    private void actualizarVelocidadAtaque() {
        if (faseActual == 3) {
            cooldownTotal = 1.2f / 1.5f;
            speed = 40f * 1.4f;
        } else {
            cooldownTotal = 1.2f;
            speed = 40f;
        }
    }

    private void iniciarTeletransporte(){
        teletransportandose=true;
        animador.setEstado(EstadoJefe.DESAPARECER);
    }

    private boolean buscarPosicionSegura(MapCollision mapCollision) {
        if (objetivo == null || mapCollision == null) {
            return false;
        }

        for (int i = 0; i < 25; i++) {
            float nuevaX = objetivo.getX() + MathUtils.random(-50f, 50f);
            float nuevaY = objetivo.getY() + MathUtils.random(-50f, 50f);

            if (!mapCollision.isColliding(nuevaX, nuevaY, 24f, 24f)) {
                x = nuevaX;
                y = nuevaY;
                return true;
            }
        }

        return false;
    }

    private void dispararCeguera(){
        if(objetivo == null || listaProyectiles == null) return;

        float anguloBase=MathUtils.atan2(objetivo.getY()-y, objetivo.getX()-x);
        float offsetAngulo=0.35f;

        for(int i=-1; i<=1; i++){
            float anguloFinal=anguloBase+(i*offsetAngulo);

            float objX = x + MathUtils.cos(anguloFinal) * 120f;
            float objY = y + MathUtils.sin(anguloFinal) * 120f;

            listaProyectiles.add(new ProyectilOcular(x, y, objX, objY));
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
        if (tiempoDanio > 0f) {
            sb.setColor(1f, 0.2f, 0.2f, 1f);
        } else if (tiempoAturdido > 0f) {
            sb.setColor(0.3f, 0.7f, 1f, 1f);
        } else if (tiempoRelentizado > 0f) {
            sb.setColor(1f, 0.85f, 0.3f, 1f);
        }

        sb.draw(
            animador.getCurrentFrame(),
            x - 16f,
            y - 16f,
            32f,
            32f
        );

        sb.setColor(Color.WHITE);
    }

    public int getFaseActual() {
        return faseActual;
    }

    @Override
    public void dispose() {
        animador.dispose();
    }
}

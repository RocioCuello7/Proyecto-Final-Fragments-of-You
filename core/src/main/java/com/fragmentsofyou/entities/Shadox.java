package com.fragmentsofyou.entities;

import com.badlogic.gdx.Gdx;
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
    private static final float MOMENTO_IMPACTO = 0.45f;

    private float temporizadorHabilidad = 0f;
    private float intervaloHabilidad=3.5f;
    private boolean teletransportandose=false;

    public Shadox(float startX, float startY, Entidad objetivo, ArrayList<ProyectilOcular> proyectiles) {
        super(startX, startY, 24f, 24f, 40f, 1, 15f, 28f, 1.2f, objetivo);

        animador = new AnimacionJefe("enemigos/Shadox.png");
        this.listaProyectiles=proyectiles;

    }

    @Override
    public void update(float dt, MapCollision mapCollision) {

        if (isMuerto()) {
            return;
        }

        if(!atacando && !teletransportandose && objetivo != null){
            temporizadorHabilidad+=dt;
            if(temporizadorHabilidad>=intervaloHabilidad){
                temporizadorHabilidad=0f;
                float decision= MathUtils.random();

                if(decision<0.35f){
                    iniciarTeletransporte();
                }else if(decision<0.70f){
                    dispararCeguera();
                }else{
                    atacar();
                }
            }
        }

        if(teletransportandose){
            animador.update(dt,dirX);
            if(animador.getEstado() == EstadoJefe.DESAPARECER && animador.terminoAnimacion()){
                x=objetivo.getX() + MathUtils.random(-50f,50f);
                y=objetivo.getY() + MathUtils.random(-50f,50f);
                animador.setEstado(EstadoJefe.APARECER);
            }else if(animador.getEstado() == EstadoJefe.APARECER && animador.terminoAnimacion()){
                teletransportandose=false;
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

    private void iniciarTeletransporte(){
        teletransportandose=true;
        animador.setEstado(EstadoJefe.DESAPARECER);
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

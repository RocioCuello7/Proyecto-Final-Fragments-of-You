package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fragmentsofyou.animadores.Animacion2Direcciones;
import com.fragmentsofyou.handlers.MapCollision;

import java.util.ArrayList;

public class Argos extends Enemigo{

    private Animacion2Direcciones animador;
    private ArrayList<ProyectilOcular> listaProyectiles;
    private float tiempoDisparo=0f;
    private final float cooldownsDisparo=0.7f;

    private final float rangoDisparo = 100f;
    private final float tiempoPreparacion = 0.35f;

    private float tiempoPreparando = 0f;
    private boolean preparandoDisparo = false;

    private final float rangoCuerpoACuerpo = 20f;
    private final float danioCuerpoACuerpo = 40f;
    private final float cooldownCuerpoACuerpo = 1.2f;
    private float tiempoCuerpoACuerpo = 0f;

    private float objetivoAnteriorX;
    private float objetivoAnteriorY;
    private float velocidadObjetivoX;
    private float velocidadObjetivoY;
    private boolean posicionObjetivoInicializada = false;

    public Argos(float startX, float startY, Entidad objetivo, ArrayList<ProyectilOcular> proyectiles){
        super(startX, startY,14f,14f,22f,130,15f,100f,2.0f,objetivo);
        this.listaProyectiles=proyectiles;
        this.animador = new Animacion2Direcciones("enemigos/", "enemigo2Left.png", "enemigo2Right.png", 6, 0.14f);
        this.rangoAtaque = 0f;
    }

    public void update(float dt, MapCollision mapCollision){
        super.update(dt, mapCollision);


        if (isMuerto()) return;

        if (objetivo != null && dt > 0f) {
            float jugadorX = objetivo.getX();
            float jugadorY = objetivo.getY();

            if (posicionObjetivoInicializada) {
                velocidadObjetivoX = (jugadorX - objetivoAnteriorX) / dt;
                velocidadObjetivoY = (jugadorY - objetivoAnteriorY) / dt;
            } else {
                posicionObjetivoInicializada = true;
            }

            objetivoAnteriorX = jugadorX;
            objetivoAnteriorY = jugadorY;
        }

        animador.update(dt, dirX);

        if (tiempoAturdido > 0f) {
            preparandoDisparo = false;
            tiempoPreparando = 0f;
            tiempoDisparo = 0f;
            tiempoCuerpoACuerpo = 0f;
            return;
        }

        if (objetivo == null) return;

        float diffX = objetivo.getX() - x;
        float diffY = objetivo.getY() - y;
        float distancia = (float) Math.sqrt(
            diffX * diffX + diffY * diffY
        );

        boolean puedeDisparar =
            distancia <= rangoDisparo &&
                (mapCollision == null ||
                    mapCollision.hayLineaDeVision(
                        x, y, objetivo.getX(), objetivo.getY()
                    ));

        if (!puedeDisparar) {
            preparandoDisparo = false;
            tiempoPreparando = 0f;
            tiempoDisparo = 0f;
            return;
        }

        if (tiempoAturdido <= 0f) {
            tiempoCuerpoACuerpo += dt;
            atacarCuerpoACuerpo();
        }

        tiempoDisparo += dt;

        if (!preparandoDisparo &&
            tiempoDisparo >= cooldownsDisparo) {
            preparandoDisparo = true;
            tiempoPreparando = 0f;
        }

        if (preparandoDisparo) {
            tiempoPreparando += dt;

            if (tiempoPreparando >= tiempoPreparacion) {
                disparar();
                preparandoDisparo = false;
                tiempoPreparando = 0f;
                tiempoDisparo = 0f;
            }
        }
    }

    private void disparar() {
        if (listaProyectiles == null || objetivo == null) {
            return;
        }

        float anticipacion = 0.20f;

        float objetivoX = objetivo.getX()
            + velocidadObjetivoX * anticipacion;

        float objetivoY = objetivo.getY()
            + velocidadObjetivoY * anticipacion;

        float desviacionMaxima = 22f;

        float desplazamientoX = objetivoX - objetivo.getX();
        float desplazamientoY = objetivoY - objetivo.getY();

        desplazamientoX = Math.max(
            -desviacionMaxima,
            Math.min(desviacionMaxima, desplazamientoX)
        );

        desplazamientoY = Math.max(
            -desviacionMaxima,
            Math.min(desviacionMaxima, desplazamientoY)
        );

        objetivoX = objetivo.getX() + desplazamientoX;
        objetivoY = objetivo.getY() + desplazamientoY;

        listaProyectiles.add(
            new ProyectilOcular(
                x,
                y,
                objetivoX,
                objetivoY
            )
        );
    }

    private void atacarCuerpoACuerpo() {
        if (objetivo == null || objetivo.isMuerto()) {
            return;
        }

        float centroArgosX = x + width / 2f;
        float centroArgosY = y + height / 2f;

        float centroJugadorX = objetivo.getX() + objetivo.getWidth() / 2f;
        float centroJugadorY = objetivo.getY() + objetivo.getHeight() / 2f;

        float diffX = centroJugadorX - centroArgosX;
        float diffY = centroJugadorY - centroArgosY;

        float distancia = (float) Math.sqrt(
            diffX * diffX + diffY * diffY
        );

        if (distancia <= rangoCuerpoACuerpo
            && tiempoCuerpoACuerpo >= cooldownCuerpoACuerpo) {

            objetivo.recibirDanio(danioCuerpoACuerpo);
            tiempoCuerpoACuerpo = 0f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        TextureRegion currentFrame=animador.getCurrentFrame();

        if(tiempoDanio>0f){
            sb.setColor(1f,0.2f,0.2f,1f);
        }else if(tiempoAturdido>0f){
            sb.setColor(0.3f,0.7f,1f,1f);
        }else if(tiempoRelentizado>0){
            sb.setColor(1f,0.85f,0.3f,1f);
        }
        sb.draw(currentFrame, x-12, y-12,24,24);
        sb.setColor(Color.WHITE);

    }

    @Override
    public void dispose() {
        if(animador!=null) animador.dispose();

    }
}

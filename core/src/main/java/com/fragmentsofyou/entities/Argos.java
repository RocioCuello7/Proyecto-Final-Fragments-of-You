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
    private final float cooldownsDisparo=1.4f;

    private final float rangoDisparo = 100f;
    private final float tiempoPreparacion = 0.35f;

    private float tiempoPreparando = 0f;
    private boolean preparandoDisparo = false;

    public Argos(float startX, float startY, Entidad objetivo, ArrayList<ProyectilOcular> proyectiles){
        super(startX, startY,14f,14f,22f,130,15f,100f,2.0f,objetivo);
        this.listaProyectiles=proyectiles;
        this.animador = new Animacion2Direcciones("enemigos/", "enemigo2Left.png", "enemigo2Right.png", 6, 0.14f);
        this.rangoAtaque = 0f;
    }

    public void update(float dt, MapCollision mapCollision){
        super.update(dt, mapCollision);

        if (isMuerto()) return;

        animador.update(dt, dirX);

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
        if(listaProyectiles != null && objetivo != null){
            listaProyectiles.add(new ProyectilOcular(x, y, objetivo.getX(), objetivo.getY()));
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

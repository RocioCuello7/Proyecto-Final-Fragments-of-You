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
    private final float cooldownsDisparo=2.2f;

    public Argos(float startX, float startY, Entidad objetivo, ArrayList<ProyectilOcular> proyectiles){
        super(startX, startY,14f,14f,22f,130,15f,100f,2.0f,objetivo);
        this.listaProyectiles=proyectiles;
        this.animador = new Animacion2Direcciones("enemigos/", "enemigo2Left_2.png", "enemigo2Right_2.png", 6, 0.14f);
    }

    public void update(float dt, MapCollision mapCollision){
        super.update(dt, mapCollision);

        if(isMuerto()) return;
        animador.update(dt, dirX);

        if(objetivo != null){
            float diffX= objetivo.getX()-x;
            float diffY= objetivo.getY()-y;
            float distancia = (float) Math.sqrt(diffX*diffX + diffY*diffY);

            if(distancia <= rangoAtaque){
                tiempoDisparo+=dt;
                if(tiempoDisparo >= cooldownsDisparo){
                    disparar();
                    tiempoDisparo=0f;
                }
            }
        }
    }

    private void disparar() {
        if(listaProyectiles != null && objetivo != null){
            listaProyectiles.add(new ProyectilOcular(x,y, objetivo.getX(), objetivo.getY()));
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

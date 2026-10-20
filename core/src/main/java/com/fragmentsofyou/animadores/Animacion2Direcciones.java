package com.fragmentsofyou.animadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animacion2Direcciones {

    private Array<Texture> texturas = new Array<> (2);
    private Array<Animation<TextureRegion>> animaciones = new Array<> (2);
    private boolean mirandoDerecha=true;
    private float stateTime=0f;

    public Animacion2Direcciones(String rutaBase, String enemigo2Left, String enemigo2Right, int cols, float frameDuration){
        Texture texIzq = new Texture(rutaBase + enemigo2Left);
        Texture texDer = new Texture(rutaBase + enemigo2Right);

        texturas.add(texIzq);
        texturas.add(texDer);

        animaciones.add(crearAnimacion(texIzq, cols, frameDuration));
        animaciones.add(crearAnimacion(texDer, cols, frameDuration));
    }

    private Animation<TextureRegion> crearAnimacion(Texture sheet, int cols, float frameDuration){
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight());
        TextureRegion[] frames = new TextureRegion[cols];
        for (int i = 0; i < cols; i++) {
            frames[i] = tmp[0][i];
        }
        return new Animation<>(frameDuration, frames);
    }

    public void update(float dt, float dirX){
        stateTime+=dt;
        if(dirX > 0){
            mirandoDerecha=true;
        }else if(dirX < 0){
            mirandoDerecha=false;
        }
    }

    public TextureRegion getCurrentFrame(){
        int indice=mirandoDerecha ? 1 : 0;
        return animaciones.get(indice).getKeyFrame(stateTime, true);
    }

    public void dispose(){
        for(Texture tex : texturas){
            if(tex != null) tex.dispose();
        }
    }
}

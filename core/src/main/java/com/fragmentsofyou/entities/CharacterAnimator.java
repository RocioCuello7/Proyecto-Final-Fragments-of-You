package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class CharacterAnimator {

    private Texture spriteSheet;

    private TextureRegion frameQuieto;
    private Animation<TextureRegion> animAbajo;
    private Animation<TextureRegion> animArriba;
    private Animation<TextureRegion> animDerecha;
    private Animation<TextureRegion> animIzquierda;

    private Direction direccionActual= Direction.DOWN;
    private float stateTime = 0f;
    private boolean enMovimiento = false;

    public CharacterAnimator(String rutaTextura, float frameDuration){
        spriteSheet = new Texture(rutaTextura);

        int frameW = spriteSheet.getWidth()/6;
        int frameH = spriteSheet.getHeight()/6;

        TextureRegion [][] tmp = TextureRegion.split(spriteSheet,frameW, frameH);


        animAbajo = new Animation<>(frameDuration, tmp[0][0]);
        animArriba = new Animation<>(frameDuration, tmp[3][1]);
        animDerecha = new Animation<>(frameDuration, tmp[5][2], tmp[5][3]);
        frameQuieto = tmp[0][3];

        TextureRegion izq1 = new TextureRegion(tmp[5][2]);
        izq1.flip(true, false);
        TextureRegion izq2 = new TextureRegion(tmp[5][3]);
        izq2.flip(true, false);
        animIzquierda=new Animation<>(frameDuration,izq1,izq2);
    }

    public void update(float dt,float dirX, float dirY){
        enMovimiento = (dirX!=0 || dirY !=0);

        if(enMovimiento){
            stateTime+=dt;

            if(dirY>0){
                direccionActual= Direction.UP;
            }else if (dirY < 0) {
                direccionActual = Direction.DOWN;
            } else if (dirX > 0) {
                direccionActual = Direction.RIGHT;
            } else if (dirX < 0) {
                direccionActual = Direction.LEFT;
            }


        }else{
            stateTime = 0f;
        }
    }

    public TextureRegion getCurrentFrame() {
        if (!enMovimiento) {
            return frameQuieto;
        }

        Animation<TextureRegion> animActual;

        switch (direccionActual) {
            case UP:
                animActual = animArriba;
                break;
            case RIGHT:
                animActual = animDerecha;
                break;
            case LEFT:
                animActual = animIzquierda;
                break;
            case DOWN:
            default:
                animActual = animAbajo;
                break;
        }

        return animActual.getKeyFrame(stateTime, true);
    }

    public Direction getDireccionActual() {
        return direccionActual;
    }

    public void setDireccionActual(Direction direccionActual) {
        this.direccionActual = direccionActual;
    }

    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }

}

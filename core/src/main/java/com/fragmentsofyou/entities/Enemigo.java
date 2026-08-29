package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fragmentsofyou.handlers.MapCollision;
import com.badlogic.gdx.graphics.Texture;

public class Enemigo {

    private float x, y;
    private float speed = 20f;

    private float width = 12f;
    private float height = 12f;

    private Texture texSouth, texNorth, texEast, texWest, texSouthEast, texSouthWest, texNorthEast, texNorthWest;
    private Animation<TextureRegion> animSouth, animNorth, animEast, animWest, animSouthEast, animSouthWest, animNorthEast, animNorthWest;

    private Direction direccionActual = Direction.DOWN;
    private float stateTime = 0f;
    private boolean enMovimiento = true;

    private float tiempoAturdido=0f;


    private float tiempoRelentizado;
    private float factorVelocidad=0.35f;


    public Enemigo(float startX, float startY) {
        this.x = startX;
        this.y = startY;

        texSouth = new Texture("enemigos/south.png");
        texNorth = new Texture("enemigos/north.png");
        texEast = new Texture("enemigos/east.png");
        texWest = new Texture("enemigos/west.png");
        texSouthEast = new Texture("enemigos/south-east.png");
        texSouthWest = new Texture("enemigos/south-west.png");
        texNorthEast = new Texture("enemigos/north-east.png");
        texNorthWest = new Texture("enemigos/north-west.png");

        animSouth = crearAnimacion(texSouth, 0.12f);
        animNorth = crearAnimacion(texNorth, 0.12f);
        animEast = crearAnimacion(texEast, 0.12f);
        animWest = crearAnimacion(texWest, 0.12f);
        animSouthEast = crearAnimacion(texSouthEast, 0.12f);
        animSouthWest = crearAnimacion(texSouthWest, 0.12f);
        animNorthEast = crearAnimacion(texNorthEast, 0.12f);
        animNorthWest = crearAnimacion(texNorthWest, 0.12f);
    }

    //metodo para cortar las texturas de 2x4
    private Animation<TextureRegion> crearAnimacion(Texture sheet, float frameDuration) {
        int cols = 2;
        int rows = 4;
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    public void update(float dt, MapCollision mapCollision, float targetX, float targetY) {
        if(tiempoAturdido>0f){
            tiempoAturdido-=dt;
            if(tiempoAturdido<0f) {
                tiempoAturdido = 0f;
            }
            return;
        }

        float velocidadActual = speed;

        if(tiempoRelentizado>0f){
            tiempoRelentizado-=dt;
            if(tiempoRelentizado<0f){
                tiempoRelentizado=0f;
            }
            velocidadActual= speed*factorVelocidad;
        }

        stateTime += dt;
        //que siga al jugador
        float dirX = 0;
        float dirY = 0;

        float diffX = targetX - x;
        float diffY = targetY - y;

        if (Math.abs(diffX) > 1f) dirX = Math.signum(diffX);
        if (Math.abs(diffY) > 1f) dirY = Math.signum(diffY);

        if (dirY != 0) {
            float intentoY = y + (dirY * velocidadActual * dt);
            if (!mapCollision.isColliding(x, intentoY, width, height)) {
                y = intentoY;
            }
        }

        if (dirX != 0) {
            float intentoX = x + (dirX * velocidadActual * dt);
            if (!mapCollision.isColliding(intentoX, y, width, height)) {
                x = intentoX;
            }
        }


        if (dirY > 0 && dirX == 0) direccionActual = Direction.UP;
        else if (dirY < 0 && dirX == 0) direccionActual = Direction.DOWN;
        else if (dirX > 0 && dirY == 0) direccionActual = Direction.RIGHT;
        else if (dirX < 0 && dirY == 0) direccionActual = Direction.LEFT;
        else if (dirY > 0 && dirX > 0) direccionActual = Direction.UP;
        else if (dirY > 0 && dirX < 0) direccionActual = Direction.UP;
        else if (dirY < 0 && dirX > 0) direccionActual = Direction.DOWN;
        else if (dirY < 0 && dirX < 0) direccionActual = Direction.DOWN;
    }

    public void render(SpriteBatch sb) {

        Animation<TextureRegion> animActual;


        switch (direccionActual) {
            case UP:
                animActual = animNorth;
                break;
            case RIGHT:
                animActual = animEast;
                break;
            case LEFT:
                animActual = animWest;
                break;
            case DOWN:
            default:
                animActual = animSouth;
                break;
        }

        TextureRegion currentFrame=animActual.getKeyFrame(stateTime, true);



        if(tiempoAturdido>0f){
            sb.setColor(0.3f, 0.7f, 1f, 1f);
        }else if (tiempoRelentizado > 0f) {
            sb.setColor(1f, 0.85f, 0.3f, 1f);
        }

        sb.draw(currentFrame, x-12, y-12, 24, 24);

        sb.setColor(Color.WHITE);
    }

    public void relentizar(float duracion){
        this.tiempoRelentizado=duracion;
    }

    public void aturdir(float duracion){
        this.tiempoAturdido=duracion;
    }

    public float getX(){return x;}
    public float getY(){return y;}

    public void dispose(){
        if(texSouth != null) texSouth.dispose();
        if(texNorth != null) texNorth.dispose();
        if(texEast != null) texEast.dispose();
        if(texWest != null) texWest.dispose();
        if (texSouthEast != null) texSouthEast.dispose();
        if (texSouthWest != null) texSouthWest.dispose();
        if (texNorthEast != null) texNorthEast.dispose();
        if (texNorthWest != null) texNorthWest.dispose();
    }
}



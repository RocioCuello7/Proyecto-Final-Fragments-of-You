package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.handlers.MapCollision;

public abstract class Entidad {

    protected float x,y;
    protected float width, height;
    protected float speed;
    protected int vidaMax;
    protected int vidaActual;

    public Entidad(float x, float y, float width, float height, float speed, int vidaMax){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.vidaMax = vidaMax;
        this.vidaActual = vidaMax;
    }
    public abstract void update(float dt, MapCollision mapCollision);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();

    public void mover(float dirX, float dirY, float velocidad, float dt, MapCollision mapCollision){
        if(dirY!=0){
            float intentoY = y +(dirY*velocidad*dt);
            if(!mapCollision.isColliding(x,intentoY,width,height)){
                y = intentoY;
            }
        }

        if(dirX!=0){
            float intentoX= x+ (dirX*velocidad*dt);
            if(!mapCollision.isColliding(intentoX,y,width,height)){
                x=intentoX;
            }
        }
    }

    public void recibirDanio(float danio){
        this.vidaActual-=danio;
        if(this.vidaActual<0)this.vidaActual=0;
    }

    public boolean isMuerto(){
        if(vidaActual==0){
            return true;
        }
        return false;
    }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getVidaActual() { return vidaActual; }
}

package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.handlers.MapCollision;

public abstract class Enemigo extends Entidad {

    protected Entidad objetivo;

    protected Direction direccionActual = Direction.DOWN;
    protected float stateTime = 0f;

    protected float tiempoAturdido = 0f;
    protected float tiempoRelentizado = 0f;
    protected float factorVelocidad = 0.35f;

    protected float tiempoDanio=0f;

    public Enemigo(float startX, float startY, float width, float height, float speed, int vidaMax, Entidad objetivo) {
        super(startX, startY, width, height, speed, vidaMax);
        this.objetivo=objetivo;
    }

    @Override
    public void update(float dt, MapCollision mapCollision) {
        if(isMuerto()){
            return;
        }

        if(tiempoDanio>0f){
            tiempoDanio-=dt;
            if(tiempoDanio<0f){
                tiempoDanio=0f;
            }

        }

        if (tiempoAturdido > 0f) {
            tiempoAturdido -= dt;
            if (tiempoAturdido < 0f) {
                tiempoAturdido = 0f;
            }
            return;
        }

        float velocidadActual = speed;
        if (tiempoRelentizado > 0f) {
            tiempoRelentizado -= dt;
            if (tiempoRelentizado < 0f) {
                tiempoRelentizado = 0f;
            }
            velocidadActual = speed * factorVelocidad;
        }

        stateTime += dt;

        float dirX = 0;
        float dirY = 0;

        if (objetivo != null) {
            float diffX = objetivo.getX() - x;
            float diffY = objetivo.getY() - y;

            if (Math.abs(diffX) > 1f) dirX = Math.signum(diffX);
            if (Math.abs(diffY) > 1f) dirY = Math.signum(diffY);
        }

        mover(dirX, dirY, velocidadActual, dt, mapCollision);

        if (dirY > 0 && dirX == 0) direccionActual = Direction.UP;
        else if (dirY < 0 && dirX == 0) direccionActual = Direction.DOWN;
        else if (dirX > 0 && dirY == 0) direccionActual = Direction.RIGHT;
        else if (dirX < 0 && dirY == 0) direccionActual = Direction.LEFT;
        else if (dirY > 0 && dirX > 0) direccionActual = Direction.UP;
        else if (dirY > 0 && dirX < 0) direccionActual = Direction.UP;
        else if (dirY < 0 && dirX > 0) direccionActual = Direction.DOWN;
        else if (dirY < 0 && dirX < 0) direccionActual = Direction.DOWN;
    }
    @Override
    public void recibirDanio(float danio){
        super.recibirDanio(danio);
        this.tiempoDanio=0.12f;
    }

    public void relentizar(float duracion) {
        this.tiempoRelentizado = duracion;
    }

    public void aturdir(float duracion) {
        this.tiempoAturdido = duracion;
    }

    @Override
    public abstract void render(SpriteBatch sb);

    @Override
    public abstract void dispose();
}

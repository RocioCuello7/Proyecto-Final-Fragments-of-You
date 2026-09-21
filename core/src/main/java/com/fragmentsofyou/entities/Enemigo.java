package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.enumeradores.FourDirection;
import com.fragmentsofyou.handlers.MapCollision;

public abstract class Enemigo extends Entidad {

    protected Entidad objetivo;

    protected FourDirection direccionActual = FourDirection.SOUTH;
    protected float stateTime = 0f;

    protected float tiempoAturdido = 0f;
    protected float tiempoRelentizado = 0f;
    protected float factorVelocidad = 0.35f;

    protected float tiempoDanio=0f;
    protected float dirX = 0f;
    protected float dirY = 0f;

    protected float danio;
    protected float rangoAtaque;

    protected float cooldownTotal;
    protected float cooldownActual=0f;

    public Enemigo(float startX, float startY, float width, float height, float speed, int vidaMax,float danio, float rangoAtaque, float cooldownTotal, Entidad objetivo) {
        super(startX, startY, width, height, speed, vidaMax);
        this.objetivo=objetivo;
        this.danio=danio;
        this.rangoAtaque=rangoAtaque;
        this.cooldownTotal=cooldownTotal;
    }

    @Override
    public void update(float dt, MapCollision mapCollision) {
        if(isMuerto()){
            return;
        }

        if(cooldownActual>0f){
            cooldownActual-=dt;
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

        dirX = 0;
        dirY = 0;

        if (objetivo != null) {
            float diffX = objetivo.getX() - x;
            float diffY = objetivo.getY() - y;

            float distancia = (float) Math.sqrt(diffX * diffX + diffY * diffY);

            if(distancia<=rangoAtaque && cooldownActual<=0f){
                atacar();
            }
            if (Math.abs(diffX) > 1f) dirX = Math.signum(diffX);
            if (Math.abs(diffY) > 1f) dirY = Math.signum(diffY);

        }

        mover(dirX, dirY, velocidadActual, dt, mapCollision);

        if (dirY > 0 && dirX == 0) direccionActual = FourDirection.NORTH;
        else if (dirY < 0 && dirX == 0) direccionActual = FourDirection.SOUTH;
        else if (dirX > 0 && dirY == 0) direccionActual = FourDirection.EAST;
        else if (dirX < 0 && dirY == 0) direccionActual = FourDirection.WEST;
        else if (dirY > 0 && dirX > 0) direccionActual = FourDirection.NORTH;
        else if (dirY > 0 && dirX < 0) direccionActual = FourDirection.NORTH;
        else if (dirY < 0 && dirX > 0) direccionActual = FourDirection.SOUTH;
        else if (dirY < 0 && dirX < 0) direccionActual = FourDirection.SOUTH;
    }

    private void atacar() {
        objetivo.recibirDanio(danio);
        cooldownActual = cooldownTotal;
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

package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fragmentsofyou.animadores.Animacion8Direcciones;
import com.fragmentsofyou.handlers.MapCollision;

public class Mecento extends Enemigo {

    private Animacion8Direcciones animador;



    public Mecento(float startX, float startY, Entidad objetivo) {
        super(startX, startY, 12f, 12f, 20f, 150, objetivo);
        this.animador = new Animacion8Direcciones("enemigos/", 0.12f);
    }

    @Override
    public void update(float dt, MapCollision mapCollision) {
        super.update(dt, mapCollision);
        animador.update(dt, dirX, dirY);
    }

    @Override
    public void render(SpriteBatch sb) {
        TextureRegion currentFrame = animador.getCurrentFrame();

        if (tiempoDanio > 0f) {
            sb.setColor(1f, 0.2f, 0.2f, 1f);
        } else if (tiempoAturdido > 0f) {
            sb.setColor(0.3f, 0.7f, 1f, 1f);
        } else if (tiempoRelentizado > 0f) {
            sb.setColor(1f, 0.85f, 0.3f, 1f);
        }

        sb.draw(currentFrame, x - 12, y - 12, 24, 24);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        if (animador != null) {
            animador.dispose();
        }
    }
}

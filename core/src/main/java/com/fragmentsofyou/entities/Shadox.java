package com.fragmentsofyou.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.animadores.Animacion4Direcciones;

public class Shadox extends Enemigo{

    private Animacion4Direcciones animador;

    public Shadox(float startX, float startY, float width, float height, float speed, int vidaMax, float danio, float rangoAtaque, float cooldownTotal, Entidad objetivo) {
        super(startX, startY, 24f, 24f, 40f, 150, 15f, 28f, 1.2f, objetivo);


    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}

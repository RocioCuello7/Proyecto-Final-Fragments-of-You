package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fragmentsofyou.handlers.DialogoManager;

public class Abuela {

    private Sprite sprite;
    private Texture textura;
    private float x, y;
    private boolean visible = true;

    private String[] dialogos;
    private float distanciaInteraccion = 35f;
    private boolean dialogoTerminado = false;
    private boolean dialogoIniciado = false;

    public Abuela(float x, float y) {
        this.x = x;
        this.y = y;

        textura = new Texture("abuela/Abuela.png");
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
        sprite.setScale(0.5f);

        dialogos = new String[] {
            "Glenn... escuchaste esos ruidos afuera?",
            "La luz de la casa esta fallando...",
            "creo que esta ",
            " JANONEX  . . . "
        };
    }

    public void update(float dt, float jugadorX, float jugadorY, Jugador jugador, DialogoManager dialogoManager) {
        if (!visible) return;

        float distToAbuela = Vector2.dst(jugadorX, jugadorY, x, y);

        if (com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E) && distToAbuela <= distanciaInteraccion) {
            if (!dialogoManager.isMostrandoDialogo() && !dialogoIniciado) {
                dialogoIniciado = true;
                dialogoManager.iniciarDialogoConPosicion(dialogos, jugador, x + 8f, y + 40f);
            }
        }

        if (dialogoManager.isDialogoTerminado() && !dialogoTerminado && dialogoIniciado) {
            dialogoTerminado = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (!visible) return;
        sprite.draw(sb);
    }

    public boolean isDialogoTerminado() {
        return dialogoTerminado;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose() {
        if (textura != null) textura.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String[] getDialogos() {
        return dialogos;
    }
}

package com.fragmentsofyou.entities;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.armas.Linterna;
import com.fragmentsofyou.handlers.MapCollision;

public class Jugador {

    private CharacterAnimator animador;
    private Linterna linterna;

    private float x, y;
    private float speed = 90f;
    private float rotacionMouse;

    private float dirX, dirY;
    private float width = 10f;
    private float height = 10f;

    private Vector3 mousePos = new Vector3();

    public Jugador(float startX, float startY, RayHandler rayHandler) {
        this.x = startX;
        this.y = startY;

        this.animador = new CharacterAnimator("spriteGlenn.png", 0.15f);
        this.linterna = new Linterna(rayHandler, startX, startY, 0f);
    }

    public void handleInput(Viewport viewport) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        float deltaX = mousePos.x - x;
        float deltaY = mousePos.y - y;
        rotacionMouse = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

        dirX = 0;
        dirY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dirY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dirY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dirX += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dirX -= 1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            linterna.alternarEncendido();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            linterna.dispararSobrecarga();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            linterna.dispararDestello();
        }
    }

    public void update(float dt, MapCollision mapCollision) {
        if (dirY != 0) {
            float intentoY = y + (dirY * speed * dt);
            if (!mapCollision.isColliding(x, intentoY, width, height)) {
                y = intentoY;
            }
        }

        if (dirX != 0) {
            float intentoX = x + (dirX * speed * dt);
            if (!mapCollision.isColliding(intentoX, y, width, height)) {
                x = intentoX;
            }
        }

        animador.update(dt, dirX, dirY);
        linterna.update(dt, x, y, rotacionMouse);
    }

    public void render(SpriteBatch sb) {
        sb.draw(animador.getCurrentFrame(), x - 8, y - 8, 16, 16);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getRotacion() { return rotacionMouse; }
    public Linterna getLinterna() { return linterna; }

    public void dispose() {
        if (animador != null) animador.dispose();
        if (linterna != null) linterna.dispose();
    }
}

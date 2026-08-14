package com.fragmentsofyou.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.MapCollision;

public class Jugador{

    private Texture texture;
    private float x, y;
    private float speed = 90f;
    private float rotacion;

    private float dirX, dirY;
    private float width = 10f;
    private float height = 10f;

    private Vector3 mousePos = new Vector3();

    public Jugador(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.texture = new Texture("meiSprite.png");
    }

    public void handleInput(Viewport viewport) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        float deltaX = mousePos.x - x;
        float deltaY = mousePos.y - y;
        rotacion = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

        dirX = 0;
        dirY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dirY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dirY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dirX += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dirX -= 1;
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
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture,
            x - 5, y - 5,
            5, 5,
            10, 10,
            1, 1,
            rotacion,
            0, 0, 32, 32, false, false);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public float getRotacion() {
        return rotacion;
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}

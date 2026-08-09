
package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.fragmentsofyou.handlers.GameStateManager;

public class Play extends GameState {

    // Sprite y posición del personaje temporal
    private Texture tempSprite;
    private float playerX;
    private float playerY;
    private float speed = 220f; // Velocidad de movimiento en píxeles/segundo

    public Play(GameStateManager gsm) {
        super(gsm);

        // 1. Configurar la camara top-down
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 2. Generar el cuadrado blanco temporal (32x32) en memoria
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        tempSprite = new Texture(pixmap);
        pixmap.dispose();

        // 3. Posicion inicial en el centro de la pantalla
        playerX = Gdx.graphics.getWidth() / 2f;
        playerY = Gdx.graphics.getHeight() / 2f;
    }

    @Override
    public void handleInput() {
        float dt = Gdx.graphics.getDeltaTime();

        // Movimiento WASD
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += speed * dt;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        // esto seria si quisiera que siga al jugador
       // cam.position.set(playerX, playerY, 0);
        cam.update();
    }

    @Override
    public void render() {
        //fondo gris
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(tempSprite, playerX - 16, playerY - 16);

        sb.end();
    }

    @Override
    public void dispose() {
        if (tempSprite != null) {
            tempSprite.dispose();
        }
    }
}


package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;

public class Play extends GameState {

    private Viewport playView;

    // Sprite y posición del personaje temporal
    private Texture tempSprite;
    private Texture meiSprite;
    private float playerX;
    private float playerY;
    private float miraX;
    private float miraY;
    private float speed = 220f; // Velocidad de movimiento en píxeles/segundo
    private float rotacionJugador;

    public Play(GameStateManager gsm) {
        super(gsm);



        // 1. Configurar la camara top-down
        cam.setToOrtho(false, 1280, 720);

        playView= new ExtendViewport(1280, 720, cam);

        // 2. Generar el cuadrado blanco temporal (32x32) en memoria
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1, 1, 1);
        pixmap.fill();
        tempSprite = new Texture(pixmap);
        pixmap.dispose();

        meiSprite = new Texture("C:\\Users\\Rocío Cuello\\IdeaProjects\\Proyecto-Final-Fragments-of-You\\lwjgl3\\src\\main\\resources\\meiSprite.png");


        // 3. Posicion inicial en el centro de la pantalla
        playerX = 1280 / 2f;
        playerY = 720 / 2f;
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

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        playView.unproject(mousePos);

        miraX = Gdx.input.getX();
        miraY = Gdx.graphics.getHeight() - Gdx.input.getY();

        float anguloRadianes = MathUtils.atan2(miraY - playerY, miraX - playerX);

        float anguloGrados = MathUtils.radiansToDegrees * anguloRadianes;

        rotacionJugador = anguloGrados;

        // esto seria si quisiera que siga al jugador
        //cam.position.set(playerX, playerY, 0);
        cam.update();
    }

    @Override
    public void render() {

        playView.apply();

        //fondo gris
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(meiSprite,
            playerX - 16, playerY - 16,
            16, 16,
            32, 32,
            1, 1,
            rotacionJugador,
            0, 0, 32, 32, false, false);

        sb.end();
    }
    @Override
    public void resize(int width, int height) {
        playView.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (meiSprite != null) {
            meiSprite.dispose();
        }
    }
}

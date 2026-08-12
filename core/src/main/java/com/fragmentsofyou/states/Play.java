
package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;

public class Play extends GameState {

    private Viewport playView;


    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    // Sprite y posición del personaje temporal
    private Texture mapaFondo;
    private Texture meiSprite;
    private float playerX;
    private float playerY;
    private float speed = 220f; // Velocidad de movimiento en píxeles/segundo
    private float rotacionJugador;

    private Vector3 mousePos = new Vector3();

    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 240, 192);
        playView = new FitViewport(240, 192, cam);


        map = new TmxMapLoader().load("mapas/mapaPrueba.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // CARGA DE TEXTURAS
        meiSprite = new Texture("meiSprite.png");

        playerX = 240 / 2f;
        playerY = 192 / 2f;
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

        // 1. Obtenemos las coordenadas del mouse y las traducimos al mundo de juego
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        playView.unproject(mousePos);

        // 2. Calculamos la diferencia usando mousePos (¡NO miraX/miraY!)
        float deltaX = mousePos.x - playerX;
        float deltaY = mousePos.y - playerY;

        // 3. Convertimos a grados
        rotacionJugador = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

        cam.update();
    }

    @Override
    public void render() {

        playView.apply();

        //fondo gris
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playView.apply();

        mapRenderer.setView(cam);
        mapRenderer.render();

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
        playView.update(width, height, true);    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (meiSprite != null) meiSprite.dispose();
    }
}

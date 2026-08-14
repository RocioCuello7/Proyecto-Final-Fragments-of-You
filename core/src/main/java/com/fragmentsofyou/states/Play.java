
package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;
import com.fragmentsofyou.handlers.MapCollision;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

public class Play extends GameState {

    private Viewport playView;

    private TiledMap map;
    private MapCollision mapCollision;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Texture meiSprite;
    private float playerX;
    private float playerY;
    private float speed = 220f;
    private float rotacionJugador;

    private float playerWidth = 16f;   // Ancho de la caja de colisión
    private float playerHeight = 16f;  // Alto de la caja de colisión

    private Vector3 mousePos = new Vector3();

    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 240, 192);
        playView = new FitViewport(240, 192, cam);

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        meiSprite = new Texture("meiSprite.png");

        MapLayer capa = map.getLayers().get("paredes y muebles");
        if (capa != null && capa.getObjects().get("spawn") != null) {
            MapObject spawnPoint = capa.getObjects().get("spawn");
            playerX = (float) spawnPoint.getProperties().get("x");
            playerY = (float) spawnPoint.getProperties().get("y");
        } else {
            playerX = 240 / 2f;
            playerY = 192 / 2f;

        }
    }

    @Override
    public void handleInput() {
        // Vacío para evitar doble movimiento
    }

    @Override
    public void update(float dt) {
        handleInput();

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        playView.unproject(mousePos);

        float nuevaX = playerX;
        float nuevaY = playerY;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            nuevaY += speed * dt;
            if (!mapCollision.isColliding(nuevaX, nuevaY, playerWidth, playerHeight)) {
                playerY = nuevaY;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            nuevaY -= speed * dt;
            if (!mapCollision.isColliding(nuevaX, nuevaY, playerWidth, playerHeight)) {
                playerY = nuevaY;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            nuevaX -= speed * dt;
            if (!mapCollision.isColliding(nuevaX, nuevaY, playerWidth, playerHeight)) {
                playerX = nuevaX;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            nuevaX += speed * dt;
            if (!mapCollision.isColliding(nuevaX, nuevaY, playerWidth, playerHeight)) {
                playerX = nuevaX;
            }
        }

        cam.position.set(playerX, playerY, 0);
        cam.update();
    }

    @Override
    public void render() {
        playView.apply();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(cam);
        mapRenderer.render();

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        // Dibujamos manteniendo el tamaño real de 32x32 de tu textura centrado en playerX/Y
        sb.draw(meiSprite,
            playerX - 5, playerY - 5,
            16, 16,
            10, 10,
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
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (meiSprite != null) meiSprite.dispose();
    }
}

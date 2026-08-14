package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.entities.Jugador;
import com.fragmentsofyou.handlers.GameStateManager;
import com.fragmentsofyou.handlers.MapCollision;

public class Play extends GameState {

    private Viewport playView;

    private TiledMap map;
    private MapCollision mapCollision;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Jugador jugador;

    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 240, 192);
        playView = new FitViewport(240, 192, cam);

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        float spawnX = 240 / 2f;
        float spawnY = 192 / 2f;

        MapLayer capa = map.getLayers().get("paredes y muebles");
        if (capa != null && capa.getObjects().get("spawn") != null) {
            MapObject spawnPoint = capa.getObjects().get("spawn");
            spawnX = (float) spawnPoint.getProperties().get("x");
            spawnY = (float) spawnPoint.getProperties().get("y");
        }

        jugador = new Jugador(spawnX,spawnY);
    }

    @Override
    public void handleInput() {
        jugador.handleInput(playView);
    }

    @Override
    public void update(float dt) {
        handleInput();

        jugador.update(dt,mapCollision);

        cam.position.set(jugador.getX(), jugador.getY(), 0);
        cam.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playView.apply();

        mapRenderer.setView(cam);
        mapRenderer.render();

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        jugador.render(sb);
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
        if (jugador != null) jugador.dispose();
    }
}

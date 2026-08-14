package com.fragmentsofyou.states;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
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

    private World world;
    private RayHandler rayHandler;
    private ConeLight linterna;

    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        playView = new FitViewport(320, 180, cam);

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        float spawnX = 320 / 2f;
        float spawnY = 180 / 2f;

        MapLayer capa = map.getLayers().get("paredes y muebles");
        if (capa != null && capa.getObjects().get("spawn") != null) {
            MapObject spawnPoint = capa.getObjects().get("spawn");
            spawnX = (float) spawnPoint.getProperties().get("x");
            spawnY = (float) spawnPoint.getProperties().get("y");
        }

        jugador = new Jugador(spawnX,spawnY);

        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);

        rayHandler.setAmbientLight(0.06f);

        linterna = new ConeLight(
            rayHandler,
            64,                                   // cant de rayos
            new Color(1f, 0.95f, 0.8f, 0.95f),    // Color de linterna
            115f,                                 // alcance de la luz
            jugador.getX(), jugador.getY(),         // Posicion inicial
            jugador.getRotacion(),                 // Dirección inicial
            38f                                   // angulo de apertura del cono (38 grados)
        );
    }

    @Override
    public void handleInput() {
        jugador.handleInput(playView);
    }

    @Override
    public void update(float dt) {
        handleInput();

        jugador.update(dt,mapCollision);

        linterna.setPosition(jugador.getX(),jugador.getY());
        linterna.setDirection(jugador.getRotacion());
        rayHandler.update();

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

        rayHandler.setCombinedMatrix(cam);
        rayHandler.render();
    }

    @Override
    public void resize(int width, int height) {
        playView.update(width, height, true);

        rayHandler.useCustomViewport(
            playView.getScreenX(),
            playView.getScreenY(),
            playView.getScreenWidth(),
            playView.getScreenHeight()
        );
    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (jugador != null) jugador.dispose();

        if (rayHandler != null) rayHandler.dispose();
        if (world != null) world.dispose();
    }
}

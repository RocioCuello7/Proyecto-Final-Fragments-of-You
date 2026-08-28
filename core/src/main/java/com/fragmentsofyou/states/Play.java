package com.fragmentsofyou.states;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.entities.Enemigo;
import com.fragmentsofyou.entities.Jugador;
import com.fragmentsofyou.handlers.GameStateManager;
import com.fragmentsofyou.handlers.MapCollision;

public class Play extends GameState {

    private Viewport playView;

    private TiledMap map;
    private MapCollision mapCollision;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Jugador jugador;
    private Enemigo enemigo;

    private World world;
    private RayHandler rayHandler;

    private ShapeRenderer shapeRenderer;

    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        playView = new FitViewport(320, 180, cam);

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        setupIluminacion();

        Vector2 spawn = obtenerSpawn();
        jugador = new Jugador(spawn.x, spawn.y, rayHandler);
        enemigo = new Enemigo(spawn.x + 22, spawn.y + 27);

        shapeRenderer = new ShapeRenderer();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private Vector2 obtenerSpawn() {
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            MapLayer capa = map.getLayers().get(i);
            MapObject spawnPoint = capa.getObjects().get("spawn");

            if (spawnPoint != null) {
                if (spawnPoint instanceof RectangleMapObject) {
                    RectangleMapObject rect = (RectangleMapObject) spawnPoint;
                    return new Vector2(rect.getRectangle().x, rect.getRectangle().y);
                } else if (spawnPoint.getProperties().containsKey("x")) {
                    return new Vector2(
                        spawnPoint.getProperties().get("x", Float.class),
                        spawnPoint.getProperties().get("y", Float.class)
                    );
                }
            }
        }
        return new Vector2(160f, 90f);
    }

    private void setupIluminacion() {
        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.20f);
    }

    @Override
    public void handleInput() {
        jugador.handleInput(playView);
    }

    @Override
    public void update(float dt) {
        handleInput();

        jugador.update(dt, mapCollision);
        enemigo.update(dt, mapCollision, jugador.getX(), jugador.getY());

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
        enemigo.render(sb);
        sb.end();

        rayHandler.setCombinedMatrix(cam);
        rayHandler.render();

        float alpha = jugador.getLinterna().getAlphaFlash();

        if (alpha > 0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(cam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(1f, 1f, 1f, alpha);

            shapeRenderer.rect(
                cam.position.x - cam.viewportWidth / 2f,
                cam.position.y - cam.viewportHeight / 2f,
                cam.viewportWidth,
                cam.viewportHeight
            );

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
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
        if (enemigo != null) enemigo.dispose();
        if (rayHandler != null) rayHandler.dispose();
        if (world != null) world.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}

package com.fragmentsofyou.states;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
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
import com.fragmentsofyou.entities.Abuela;
import com.fragmentsofyou.entities.Enemigo;
import com.fragmentsofyou.entities.Jugador;
import com.fragmentsofyou.handlers.*;

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

    private HUD hud;
    private AudioManager audio;
    private Abuela abuela;
    private ScreenFader fader;

    private MisionManager misionManager;
    private CombateManager combateManager;



    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        playView = new FitViewport(320, 180, cam);

        hud = new HUD();
        audio = new AudioManager();
        audio.iniciarMusica();

        setupIluminacion();

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles", world);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        Vector2 spawnJugador = obtenerSpawnPorNombre("spawn", 160f, 90f);
        jugador = new Jugador(spawnJugador.x, spawnJugador.y, rayHandler);
        // enemigo = new Mecento(spawn.x + 22, spawn.y + 25, jugador);

        shapeRenderer = new ShapeRenderer();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        combateManager = new CombateManager();

        Vector2 spawnAbuela = obtenerSpawnPorNombre("spawnAbuela", 220f, 220f);

        abuela = new Abuela(spawnAbuela.x, spawnAbuela.y);

        fader = new ScreenFader();

        Vector2 puntoDestinoTP = obtenerSpawnPorNombre("tp1", 500f, 300f);
        misionManager = new MisionManager(mapCollision, puntoDestinoTP);
    }

    private Vector2 obtenerSpawnPorNombre(String nombreSpawn, float xDefecto, float yDefecto) {
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            MapLayer capa = map.getLayers().get(i);
            MapObject spawnPoint = capa.getObjects().get(nombreSpawn);

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
        return new Vector2(xDefecto, yDefecto);
    }

    private void setupIluminacion() {
        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.4f);
    }

    @Override
    public void handleInput() {
        jugador.handleInput(playView);
    }

    @Override
    public void update(float dt) {
        handleInput();

        jugador.update(dt, mapCollision);

        boolean dialogoEstabaActivo = jugador.isMostrandoDialogo();
        misionManager.update(dt, jugador, abuela, fader, rayHandler);

        if (dialogoEstabaActivo && Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E)) {
            jugador.avanzarDialogo();
        }

        fader.update(dt);

        if (jugador.isMuerto()) {
            gsm.setState(GameStateManager.GAMEOVER);
            return;
        }

        combateManager.update(dt, jugador, enemigo, mapCollision, audio);
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
        if (!jugador.isMuerto()) {
            jugador.render(sb);
        }
        if (enemigo != null) {
            enemigo.render(sb);
        }
        combateManager.renderParticulas(sb);
        if (misionManager.isAbuelaPresente() && abuela != null) {
            abuela.render(sb);
        }

        sb.end();

        rayHandler.setCombinedMatrix(cam);
        rayHandler.render();

        float alpha = jugador.getLinterna().getAlphaFlash();
        if (alpha > 0f) {
            fader.renderOverlay(cam, Color.WHITE, alpha);
        }
        fader.renderFade(cam);
        sb.setProjectionMatrix(cam.combined.cpy().setToOrtho2D(0, 0, cam.viewportWidth, cam.viewportHeight));
        sb.begin();

        hud.renderFuenteObjetivo(
            sb,
            misionManager.getMisionActual().getTextoObjetivo(),
            cam.viewportWidth - 100f,
            cam.viewportHeight - 8f
        );
        sb.end();
        hud.render(sb, jugador);
    }

    @Override
    public void resize(int width, int height) {
        playView.update(width, height, true);
        hud.resize(width, height);

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
        if (shapeRenderer != null) shapeRenderer.dispose();

        if (audio != null) audio.dispose();
        if (hud != null) hud.dispose();
        if (abuela != null) abuela.dispose();
        if (fader != null) fader.dispose();
        if (combateManager != null) combateManager.dispose();
    }
}//hola

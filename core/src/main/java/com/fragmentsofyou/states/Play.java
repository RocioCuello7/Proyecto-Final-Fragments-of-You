package com.fragmentsofyou.states;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.fragmentsofyou.entities.Mecento;
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

    private Viewport hudView;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;


    private com.badlogic.gdx.audio.Music musicaAmbiente;
    private com.badlogic.gdx.audio.Sound sonidoDestello;
    private com.badlogic.gdx.audio.Sound sonidoDisparo;


    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        playView = new FitViewport(320, 180, cam);

        hudCam.setToOrtho(false, 320, 180);
        hudView = new FitViewport(320, 180, hudCam);
        font = new BitmapFont();
        font.getData().setScale(0.5f);

        setupIluminacion();

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles",world);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        Vector2 spawn = obtenerSpawn();
        jugador = new Jugador(spawn.x, spawn.y, rayHandler);
        enemigo = new Mecento(spawn.x+22,spawn.y+25,jugador);

        shapeRenderer = new ShapeRenderer();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        musicaAmbiente = Gdx.audio.newMusic(Gdx.files.internal("musica/ambiente.mp3"));
        musicaAmbiente.setLooping(true);
        musicaAmbiente.setVolume(0.5f);
        musicaAmbiente.play();

        sonidoDestello = Gdx.audio.newSound(Gdx.files.internal("sonidos/destello.mp3"));
        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("sonidos/disparo.mp3"));

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

        if(jugador.isMuerto()){
            gsm.setState(GameStateManager.GAMEOVER);
            return;
        }

        if(enemigo!=null) {
            enemigo.update(dt, mapCollision);


            boolean lineaLibre = mapCollision.hayLineaDeVision(jugador.getX(), jugador.getY(),
                enemigo.getX(),enemigo.getY());


                if (jugador.getLinterna().puedeHacerDanio()) {
                    boolean alcanzada = jugador.getLinterna().estaEnRangoSobrecarga(
                        jugador.getX(), jugador.getY(), jugador.getRotacion(), enemigo.getX(), enemigo.getY());

                    if (alcanzada && lineaLibre) {
                        enemigo.relentizar(3.0f);
                        enemigo.recibirDanio(30f);
                        jugador.getLinterna().registrarImpacto();
                    }
                }

            if (jugador.consumioDestello()) {
                sonidoDestello.play(1.0f);
                enemigo.aturdir(2.0f);
                enemigo.recibirDanio(25f);
            }

            if (enemigo.isMuerto()) {
                enemigo.dispose();
                enemigo = null;
            }
        }

        rayHandler.update();

        cam.position.set(jugador.getX(), jugador.getY(), 0);
        cam.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playView.apply();

        mapRenderer.setView(cam);
        mapRenderer.render();

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        if (!jugador.isMuerto()) {
            jugador.render(sb);
        }
        if(enemigo != null){
            enemigo.render(sb);
        }
        sb.end();

        rayHandler.setCombinedMatrix(cam);
        rayHandler.render();

        float alpha = jugador.getLinterna().getAlphaFlash();
        if(alpha > 0f){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(cam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1f,1f,1f,alpha);
            shapeRenderer.rect(
                cam.position.x - cam.viewportWidth / 2f,
                cam.position.y - cam.viewportHeight / 2f,
                cam.viewportWidth,
                cam.viewportHeight
            );
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        hudView.apply();
        hudCam.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(hudCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float anchoVida = 50f * ((float) jugador.getVidaActual() / jugador.getVidaMax());
        float anchoEnergia = 50f * (jugador.getLinterna().getEnergia() / jugador.getLinterna().getEnergiaMax());

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.8f);
        shapeRenderer.rect(35, 168, 50, 5);
        shapeRenderer.setColor(0.9f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(35, 168, anchoVida, 5);

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.8f);
        shapeRenderer.rect(35, 161, 50, 4);
        shapeRenderer.setColor(0.2f, 0.7f, 0.9f, 1f);
        shapeRenderer.rect(35, 161, anchoEnergia, 4);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        font.draw(sb, "HP", 10, 173);
        font.draw(sb, "EN", 10, 165);
        sb.end();

    }

    @Override
    public void resize(int width, int height) {
        playView.update(width, height, true);
        hudView.update(width, height, true);

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

        if (rayHandler != null) {
            rayHandler.dispose();
        }

        if (world != null) world.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        if (musicaAmbiente != null) {
            musicaAmbiente.stop();
            musicaAmbiente.dispose();
        }
        if (sonidoDestello != null) sonidoDestello.dispose();
        if (sonidoDisparo != null) sonidoDisparo.dispose();
    }
}

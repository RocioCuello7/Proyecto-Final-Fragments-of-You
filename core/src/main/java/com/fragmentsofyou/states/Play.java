package com.fragmentsofyou.states;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.entities.*;
import com.fragmentsofyou.handlers.*;

import java.util.ArrayList;

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

    private Texture texturaNota;
    private Texture texturaObjetoAbuela;
    private Texture texturaLinterna;

    private ArrayList<Enemigo> enemigos = new ArrayList<>();
    private boolean segundaOleadaSpawned = false;
    private ArrayList<ProyectilOcular> listaProyectilesOculares = new ArrayList<>();

    private boolean argosSpawned = false;

    public Play(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        playView = new FitViewport(320, 180, cam);

        hud = new HUD();
        audio = new AudioManager();
        audio.iniciarMusicaAmbiente();

        setupIluminacion();

        map = new TmxMapLoader().load("mapas/CasaFOY.tmx");
        mapCollision = new MapCollision(map, "paredes y muebles", world);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        Vector2 spawnJugador = obtenerSpawnPorNombre("spawn", 160f, 90f);
        jugador = new Jugador(spawnJugador.x, spawnJugador.y, rayHandler);

        shapeRenderer = new ShapeRenderer();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        combateManager = new CombateManager();

        Vector2 spawnAbuela = obtenerSpawnPorNombre("spawnAbuela", 220f, 220f);

        abuela = new Abuela(spawnAbuela.x, spawnAbuela.y);

        fader = new ScreenFader();

        texturaNota=new Texture(Gdx.files.internal("mapas/hojaCarta.png"));
        texturaObjetoAbuela=new Texture(Gdx.files.internal("mapas/cepilloDientesAbuela.png"));
        texturaLinterna=new Texture(Gdx.files.internal("mapas/linternaGlenn.png"));


        Vector2 puntoDestinoTP = obtenerSpawnPorNombre("tp1", 500f, 300f);
        Vector2 puntoDestinoTP2 = obtenerSpawnPorNombre("tp2", 700f, 400f);
        Vector2 puntoDestinoTP3 = obtenerSpawnPorNombre("tp3",900f,1000f);

        misionManager = new MisionManager(mapCollision, puntoDestinoTP, puntoDestinoTP2,puntoDestinoTP3);
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
        misionManager.update(dt, jugador, abuela, fader, rayHandler,audio);

        if (dialogoEstabaActivo && Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E)) {
            jugador.avanzarDialogo();
        }

        fader.update(dt);

        if(misionManager.debeSpawnarEnemigo() && enemigos.isEmpty() && !segundaOleadaSpawned){
            Vector2 spawnEnemigo =obtenerSpawnPorNombre("spawnEnemigo1", 200f, 200f);
            enemigos.add(new Mecento(spawnEnemigo.x, spawnEnemigo.y, jugador));
        }

        if(misionManager.isPrimerEnemigoDerrotado() && !segundaOleadaSpawned){
            segundaOleadaSpawned = true;

            Vector2 spawn2 = obtenerSpawnPorNombre("spawnEnemigo2",200f,200f);
            Vector2 spawn3 = obtenerSpawnPorNombre("spawnEnemigo3",250f,250f);

            enemigos.add(new Mecento(spawn2.x,spawn2.y,jugador));
            enemigos.add(new Mecento(spawn2.x + 15f ,spawn2.y,jugador));


            enemigos.add(new Mecento(spawn3.x,spawn3.y,jugador));
            enemigos.add(new Mecento(spawn3.x-15f,spawn3.y,jugador));


        }

        if (misionManager.debeSpawnarArgos() && !argosSpawned) {
            argosSpawned = true;

            Vector2 spawnArgos = obtenerSpawnPorNombre(
                "spawnArgos", 400f, 200f
            );

            enemigos.add(new Argos(
                spawnArgos.x,
                spawnArgos.y,
                jugador,
                listaProyectilesOculares
            ));
        }
        for (int i = listaProyectilesOculares.size() - 1; i >= 0; i--) {
            ProyectilOcular p = listaProyectilesOculares.get(i);
            p.update(dt, mapCollision, jugador);
            if (!p.isActivo()) {
                listaProyectilesOculares.remove(i);
            }
        }

        combateManager.update(dt, jugador, enemigos, mapCollision, audio);

        for (int i = enemigos.size() - 1; i >= 0; i--) {
            Enemigo e = enemigos.get(i);

            if (e.isMuerto()) {
                if (!segundaOleadaSpawned) {
                    misionManager.setPrimerEnemigoDerrotado(true);
                }
                enemigos.remove(i);
            }
        }

        if (segundaOleadaSpawned && enemigos.isEmpty()) {
            misionManager.setTodosEnemigosDerrotados(true);
            rayHandler.setAmbientLight(0.35f);
        }

        if (jugador.isMuerto()) {
            gsm.setState(GameStateManager.GAMEOVER);
            return;
        }

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
        if(!misionManager.isAbuelaPresente() && misionManager.getRectNota() != null){
            Rectangle rNota=misionManager.getRectNota();
            sb.draw(texturaNota, rNota.x, rNota.y);
        }
        if(!misionManager.isObjetoEncontrado() && misionManager.getRectObjetoAbuela() != null){
            Rectangle rCepillo= misionManager.getRectObjetoAbuela();
            sb.draw(texturaObjetoAbuela, rCepillo.x, rCepillo.y);
        }
        if(!misionManager.isLinternaRecolectada() && misionManager.getRectLinterna() != null){
            Rectangle rLinterna = misionManager.getRectLinterna();
            sb.draw(texturaLinterna, rLinterna.x, rLinterna.y);
        }
        if (!jugador.isMuerto()) {
            jugador.render(sb);
        }
        for (Enemigo e : enemigos) {
            if (!e.isMuerto()) {
                e.render(sb);
            }
        }

        for(ProyectilOcular p : listaProyectilesOculares){
            p.render(sb);
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
            cam.viewportWidth - 180f,
            cam.viewportHeight - 20f
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
        if (rayHandler != null) {
            rayHandler.dispose();
            rayHandler=null;
        }
        if (world != null) world.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        if (audio != null) audio.dispose();
        if (hud != null) hud.dispose();
        if (abuela != null) abuela.dispose();
        if (fader != null) fader.dispose();
        if(texturaNota!=null) texturaNota.dispose();
        if(texturaObjetoAbuela!=null) texturaObjetoAbuela.dispose();
        if (combateManager != null) combateManager.dispose();
        if (texturaLinterna != null) texturaLinterna.dispose();

        ProyectilOcular.dispose();

        if (enemigos != null) {
            for (Enemigo e : enemigos) {
                e.dispose();
            }
            enemigos.clear();
        }
    }
}//hola

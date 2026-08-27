package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;

public class Menu extends GameState {
    private Viewport menuView;

    private TiledMap menuMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Rectangle botonPlay;
    private Rectangle botonSettings;
    private Rectangle botonExit;

    private Vector3 touchPos;

    public Menu(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        menuView = new FitViewport(320, 180, cam);
        touchPos = new Vector3();

        menuMap = new TmxMapLoader().load("mapas/menuFOY.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(menuMap);

        MapLayer capaBotones = menuMap.getLayers().get("botonesObjeto");

        if (capaBotones != null) {
            MapObjects objetos = capaBotones.getObjects();

            for (int i = 0; i < objetos.getCount(); i++) {
                MapObject objeto = objetos.get(i);

                if (objeto instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) objeto).getRectangle();
                    String nombre = objeto.getName();

                    if (nombre != null) {
                        if (nombre.equalsIgnoreCase("play")) {
                            botonPlay = rect;
                        } else if (nombre.equalsIgnoreCase("settings")) {
                            botonSettings = rect;
                        } else if (nombre.equalsIgnoreCase("exit")) {
                            botonExit = rect;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            menuView.unproject(touchPos);

            if (botonPlay != null && botonPlay.contains(touchPos.x, touchPos.y)) {
                gsm.setState(GameStateManager.PLAY);
            }

            if (botonSettings != null && botonSettings.contains(touchPos.x, touchPos.y)) {
                // gsm.setState(GameStateManager.SETTINGS);
            }

            if (botonExit != null && botonExit.contains(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.03f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuView.apply();

        mapRenderer.setView(cam);
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        menuView.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (menuMap != null) menuMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}

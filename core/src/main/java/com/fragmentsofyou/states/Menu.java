package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapObject;
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

    // Tiled
    private TiledMap menuMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Rectángulos de los botones leídos desde Tiled
    private Rectangle playBounds;
    private Rectangle settingsBounds;
    private Rectangle exitBounds;

    public Menu(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        menuView = new FitViewport(320, 180, cam);

        menuMap = new TmxMapLoader().load("mapas/menu.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(menuMap);

        if (menuMap.getLayers().get("botonesObjeto") != null) {
            for (MapObject object : menuMap.getLayers().get("botonesObjeto").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    String name = object.getName();

                    if (name != null) {
                        if (name.equalsIgnoreCase("play")) playBounds = rect;
                        else if (name.equalsIgnoreCase("settings")) settingsBounds = rect;
                        else if (name.equalsIgnoreCase("exit")) exitBounds = rect;
                    }
                }
            }
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            menuView.unproject(touchPos);

            if (playBounds != null && playBounds.contains(touchPos.x, touchPos.y)) {
                gsm.setState(GameStateManager.PLAY);
            }

            // Detectar clic en Exit
            if (exitBounds != null && exitBounds.contains(touchPos.x, touchPos.y)) {
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

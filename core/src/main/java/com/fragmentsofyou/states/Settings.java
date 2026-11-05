package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class Settings extends GameState {
    private Viewport settingsView;
    private OrthographicCamera settingsCam;

    private TiledMap settingsMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Texture texturaUI;
    private TextureRegion barraVolumenTex;
    private TextureRegion rellenoVolumenTex;

    private Rectangle rectExit;
    private Rectangle rectSubirVol;
    private Rectangle rectBajarVol;
    private Rectangle rectBarraVolumen;

    private Vector3 touchPos;

    public Settings(GameStateManager gsm) {
        super(gsm);

        settingsCam = new OrthographicCamera();
        settingsCam.setToOrtho(false, 320, 180);
        settingsView = new FitViewport(320, 180, settingsCam);
        touchPos = new Vector3();

        settingsMap = new TmxMapLoader().load("mapas/SettingsFOY.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(settingsMap);

        MapLayer capaObjetos = settingsMap.getLayers().get("objetos");

        if (capaObjetos != null) {
            MapObjects objetos = capaObjetos.getObjects();

            for (int i = 0; i < objetos.getCount(); i++) {
                MapObject objeto = objetos.get(i);

                if (objeto instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) objeto).getRectangle();
                    String nombre = objeto.getName();

                    if (nombre != null) {
                        String n = nombre.trim();
                        if (n.equalsIgnoreCase("exit")) {
                            rectExit = rect;
                        } else if (n.equalsIgnoreCase("subirVol")) {
                            rectSubirVol = rect;
                        } else if (n.equalsIgnoreCase("bajarVol")) {
                            rectBajarVol = rect;
                        } else if (n.equalsIgnoreCase("barraVolumen")) {
                            rectBarraVolumen = rect;
                        }
                    }
                }
            }
        }

        texturaUI = new Texture(Gdx.files.internal("GUI/Bars_1x.png"));
        barraVolumenTex = new TextureRegion(texturaUI, 40, 12, 24, 8);
        rellenoVolumenTex = new TextureRegion(texturaUI, 0, 40, 16, 3);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            settingsView.unproject(touchPos);

            if (rectExit != null && rectExit.contains(touchPos.x, touchPos.y)) {
                gsm.popState();
                return;
            }

            if (rectSubirVol != null && rectSubirVol.contains(touchPos.x, touchPos.y)) {
                audio.subirVolumen();
            }

            if (rectBajarVol != null && rectBajarVol.contains(touchPos.x, touchPos.y)) {
                audio.bajarVolumen();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            gsm.popState();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        settingsView.apply();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(settingsCam);
        mapRenderer.render();

        sb.setProjectionMatrix(settingsCam.combined);
        sb.begin();

        if (rectBarraVolumen != null) {
            sb.draw(barraVolumenTex, rectBarraVolumen.x, rectBarraVolumen.y, rectBarraVolumen.width, rectBarraVolumen.height);

            float margenX = 4f;
            float margenY = 6f;
            float anchoMaxRelleno = rectBarraVolumen.width - (margenX * 2);
            float anchoRellenoActual = anchoMaxRelleno * audio.getVolumenMaestro();;

            sb.draw(
                rellenoVolumenTex,
                rectBarraVolumen.x + margenX,
                rectBarraVolumen.y + margenY,
                anchoRellenoActual,
                Math.max(3f, rectBarraVolumen.height - (margenY * 2))
            );
        }

        sb.end();
    }

    @Override
    public void resize(int width, int height) {
        settingsView.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (settingsMap != null) settingsMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (texturaUI != null) texturaUI.dispose();
    }
}

package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;

public class Victoria extends GameState{

    private TiledMap victoryMap;
    private Viewport victoryView;
    private OrthogonalTiledMapRenderer mapRender;
    private float tiempoEspera=0f;
    private final float TIEMPO_ESPERA_TOTAL=6f;

    public Victoria(GameStateManager gsm){
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        victoryView=new FitViewport(320,180,cam);

        victoryMap=new TmxMapLoader().load("mapas/VictoriaFOY.tmx");
        mapRender= new OrthogonalTiledMapRenderer(victoryMap);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            gsm.setState(GameStateManager.MENU);
        }

    }

    @Override
    public void update(float dt) {
        handleInput();

        tiempoEspera+=dt;
        if(tiempoEspera >= TIEMPO_ESPERA_TOTAL){
            gsm.setState(GameStateManager.MENU);
        }

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.02f, 0.05f, 0.02f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        victoryView.apply();

        mapRender.setView(cam);
        mapRender.render();

    }

    @Override
    public void resize(int width, int height) {
        victoryView.update(width, height, true);
    }

    @Override
    public void dispose() {
        if(victoryMap!=null) victoryMap.dispose();
        if(mapRender!=null) mapRender.dispose();


    }
}

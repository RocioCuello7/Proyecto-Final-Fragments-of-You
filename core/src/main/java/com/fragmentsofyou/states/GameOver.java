package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;

public class GameOver extends GameState{

    private TiledMap gameOverMap;

    private Viewport gameOverView;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float tiempoEspera=0f;
    private final float TIEMPOESPERATOTAL=5f;

    public GameOver(GameStateManager gsm){
        super(gsm);

        cam.setToOrtho(false, 320, 180);
        gameOverView = new FitViewport(320, 180, cam);

        gameOverMap = new TmxMapLoader().load("mapas/GameOverFOY.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(gameOverMap);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void handleInput(){

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            gsm.setState(GameStateManager.MENU);
        }
    }

    @Override
    public void update(float dt){
        handleInput();

        tiempoEspera+=dt;

        if (tiempoEspera >= TIEMPOESPERATOTAL) {
            gsm.setState(GameStateManager.MENU);
        }


    }

    @Override
    public  void render(){
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.03f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameOverView.apply();

        mapRenderer.setView(cam);
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        gameOverView.update(width, height, true);
    }

    @Override
    public  void dispose(){
        if (gameOverMap != null) gameOverMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}

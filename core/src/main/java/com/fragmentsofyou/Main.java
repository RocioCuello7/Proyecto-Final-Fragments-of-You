package com.fragmentsofyou;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.handlers.GameStateManager;

public class Main extends ApplicationAdapter {

    public static final String TITLE = "Fragments of You";

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;

    @Override
    public void create() {
        sb = new SpriteBatch();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gsm = new GameStateManager(this);//se le pasa la misma clase(main)
    }

    @Override
    public void render() {
        // Corre el ciclo del juego usando el tiempo entre frames (deltaTime)
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();
    }

    @Override
    public void dispose() {
        sb.dispose();
    }

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHUDCamera() { return hudCam; }
}

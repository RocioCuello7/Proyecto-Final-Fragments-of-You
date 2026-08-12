package com.fragmentsofyou.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.Main;
import com.fragmentsofyou.handlers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gsm;
    protected Main game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.game = gsm.getGame();
        this.sb = game.getSpriteBatch();
        this.cam = game.getCamera();
        this.hudCam = game.getHUDCamera();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
    public void resize(int width, int height){

    }
}

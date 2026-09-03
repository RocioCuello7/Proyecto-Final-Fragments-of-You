package com.fragmentsofyou.handlers;

import com.fragmentsofyou.Main;
import com.fragmentsofyou.states.GameOver;
import com.fragmentsofyou.states.GameState;
import com.fragmentsofyou.states.Menu;
import com.fragmentsofyou.states.Play;

import java.util.Stack;

public class GameStateManager {

    private Main game;
    private Stack<GameState> gameStates;

    public static final int MENU = 0;
    public static final int PLAY = 1;
    public static final int SETTINGS = 2;
    public static final int GAMEOVER = 3;

    public GameStateManager(Main game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(GAMEOVER);
    }

    public Main getGame() {
        return game;
    }

    public void update(float dt) {
        if (!gameStates.isEmpty()) {
            gameStates.peek().update(dt);
        }
    }

    public void render() {
        if (!gameStates.isEmpty()) {
            gameStates.peek().render();
        }
    }

    public void resize(int width, int height) {
        if (!gameStates.isEmpty()) {
            gameStates.peek().resize(width, height);
        }
    }

    private GameState getState(int state) {
        if (state == MENU) return new Menu(this);
        if (state == PLAY) return new Play(this);
        if(state == GAMEOVER) return new GameOver(this);
    return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        GameState nextState = getState(state);
        if (nextState != null) {
            gameStates.push(nextState);
        }
    }

    public void popState() {
        if (!gameStates.isEmpty()) {
            GameState g = gameStates.pop();
            g.dispose();
        }
    }

    public void dispose() {
        while (!gameStates.isEmpty()) {
            gameStates.pop().dispose();
        }
    }
}

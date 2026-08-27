package com.fragmentsofyou.handlers;

import com.fragmentsofyou.Main;
import com.fragmentsofyou.states.GameState;
import com.fragmentsofyou.states.Menu;
import com.fragmentsofyou.states.Play;

import java.util.Stack;

public class GameStateManager {

    private Main game;
    private Stack<GameState> gameStates;

    public static final int MENU = 0; // Identificador para el menú
    public static final int PLAY = 482;

    public GameStateManager(Main game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENU);
    }

    public Main getGame() {
        return game;
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public void resize(int width, int height) {
        if (!gameStates.isEmpty()) {
            gameStates.peek().resize(width, height);
        }
    }

    private GameState getState(int state) {
        if (state == PLAY) return new Play(this);
        if (state == MENU) return new Menu(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

    public void set(GameState state) {
        if (!gameStates.isEmpty()) {
            gameStates.pop().dispose();
        }
        gameStates.push(state);
    }
}

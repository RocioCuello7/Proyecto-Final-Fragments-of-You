package com.fragmentsofyou;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fragmentsofyou.handlers.AudioManager;
import com.fragmentsofyou.handlers.ConexionSQLite;
import com.fragmentsofyou.handlers.GameStateManager;

import java.sql.SQLException;

public class Main extends ApplicationAdapter {

    public static final String TITLE = "Fragments of You";

    private AudioManager audio;
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

        audio=new AudioManager();
        gsm = new GameStateManager(this);
        try {
            ConexionSQLite db = new ConexionSQLite();

            db.conectar();
            db.consultarJugadores();

            db.cerrarConexion();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();
    }

    public void resize(int width, int height) {
        if (gsm != null) {
            gsm.resize(width, height);
        }
    }


    @Override
    public void dispose() {
        if (gsm != null) gsm.dispose();
        if (audio != null) audio.dispose();
        sb.dispose();
    }

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHUDCamera() { return hudCam; }
    public AudioManager getAudio() { return audio; }
}


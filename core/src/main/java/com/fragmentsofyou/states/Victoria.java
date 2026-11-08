package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.ConexionSQLite;
import com.fragmentsofyou.handlers.GameStateManager;
import com.fragmentsofyou.handlers.ScreenFader;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Victoria extends GameState{

    private TiledMap victoryMap;
    private Viewport victoryView;
    private OrthogonalTiledMapRenderer mapRender;
    private ScreenFader fader;
    private float tiempoEspera=0f;
    private final float TIEMPO_ESPERA_TOTAL=6f;
    private float tiempoPartida;

    private Stage stage;
    private Skin skin;
    private BitmapFont fuente;

    private TextField campoNombre;
    private TextButton botonGuardar;
    private Label mensaje;

    private Texture texturaUI;

    private Table tablaVictoria;
    private boolean partidaGuardada = false;

    private Table tablaRanking;


    public Victoria(GameStateManager gsm,  float tiempoPartida){
        super(gsm);

        this.tiempoPartida = tiempoPartida;


        cam.setToOrtho(false, 320, 180);
        victoryView = new FitViewport(320, 180, cam);

        victoryMap = new TmxMapLoader().load("mapas/VictoriaFOY.tmx");
        mapRender = new OrthogonalTiledMapRenderer(victoryMap);

        fader = new ScreenFader();
        fader.startFlash(Color.BLACK, 1.5f); // Arranca en negro y se aclara

        crearInterfaz();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void crearInterfaz() {

        stage = new Stage(new FitViewport(320, 180));

        skin = new Skin();

        fuente = new BitmapFont();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        texturaUI = new Texture(pixmap);
        pixmap.dispose();

        skin.add("font", fuente, BitmapFont.class);
        skin.add("white", texturaUI, Texture.class);

        TextureRegionDrawable fondoOscuro =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextureRegionDrawable fondoBoton =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextureRegionDrawable fondoBotonPresionado =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextureRegionDrawable fondoCampo =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextField.TextFieldStyle estiloCampo = new TextField.TextFieldStyle();
        estiloCampo.font = fuente;
        estiloCampo.fontColor = Color.WHITE;
        estiloCampo.cursor = fondoOscuro.tint(Color.WHITE);
        estiloCampo.selection = fondoOscuro.tint(Color.GRAY);
        estiloCampo.background = fondoCampo.tint(new Color(0.12f, 0.12f, 0.12f, 1f));

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = fuente;
        estiloBoton.fontColor = Color.WHITE;
        estiloBoton.up = fondoBoton.tint(new Color(0.20f, 0.38f, 0.25f, 1f));
        estiloBoton.down = fondoBotonPresionado.tint(new Color(0.12f, 0.25f, 0.16f, 1f));
        estiloBoton.over = fondoBoton.tint(new Color(0.30f, 0.48f, 0.35f, 1f));

        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font = fuente;
        estiloLabel.fontColor = Color.WHITE;

        Label titulo = new Label("¡VICTORIA!", estiloLabel);

        Label indicacion = new Label("Ingresa tu nombre:", estiloLabel);

        campoNombre = new TextField("", estiloCampo);
        campoNombre.setMessageText("Tu nombre...");

        botonGuardar = new TextButton("GUARDAR PARTIDA", estiloBoton);

        botonGuardar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                guardarPartida();
            }
        });

        mensaje = new Label("", estiloLabel);

        tablaVictoria = new Table();
        tablaVictoria.setFillParent(true);
        tablaVictoria.center();

        tablaVictoria.add(titulo).padBottom(6).row();
        tablaVictoria.add(indicacion).padBottom(2.5f).row();
        tablaVictoria.add(campoNombre).width(90).height(14).padBottom(4).row();
        tablaVictoria.add(botonGuardar).width(75).height(15).padBottom(4).row();
        tablaVictoria.add(mensaje).row();
        fuente.getData().setScale(0.5f);

        tablaVictoria.setVisible(false);

        stage.addActor(tablaVictoria);

        tablaRanking = new Table();
        tablaRanking.setFillParent(true);
        tablaRanking.center();

        Label tituloRanking = new Label("RANKING", estiloLabel);

        tablaRanking.add(tituloRanking).padBottom(8).row();

        tablaRanking.setVisible(false);

        stage.addActor(tablaRanking);

        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            gsm.setState(GameStateManager.MENU);
        }

    }

    @Override
    public void update(float dt) {

        fader.update(dt);

        tiempoEspera += dt;

        if (tiempoEspera >= TIEMPO_ESPERA_TOTAL && !partidaGuardada) {
            tablaVictoria.setVisible(true);
        }

        stage.act(dt);
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0.02f, 0.02f, 0.02f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (tiempoEspera < TIEMPO_ESPERA_TOTAL) {
            victoryView.apply();
            cam.update();

            mapRender.setView(cam);
            mapRender.render();

            fader.renderFade(cam);

        } else {
            stage.getViewport().apply();
        }

        stage.draw();
    }
    private void mostrarRanking(ArrayList<String> ranking) {

        tablaVictoria.setVisible(false);
        tablaRanking.setVisible(true);

        for (int i = 0; i < ranking.size(); i++) {

            Label puesto = new Label(
                (i + 1) + ". " + ranking.get(i),
                mensaje.getStyle()
            );

            tablaRanking.add(puesto).padBottom(3).row();
        }
    }

    private void guardarPartida() {

        if (partidaGuardada) {
            return;
        }

        String nombre = campoNombre.getText().trim();

        if (nombre.isEmpty()) {
            mensaje.setText("¡Ingresá tu nombre!");
            return;
        }

        int tiempoSegundos = Math.max(1, Math.round(tiempoPartida));

        ConexionSQLite db = new ConexionSQLite();

        try {

            db.conectar();

            db.guardarPartida(nombre, tiempoSegundos);

            ArrayList<String> ranking = db.consultarRanking();

            partidaGuardada = true;

            mostrarRanking(ranking);

        } catch (SQLException e) {

            mensaje.setText("Error al guardar la partida.");
            e.printStackTrace();

        } finally {

            try {
                db.cerrarConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        victoryView.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (fuente != null) fuente.dispose();
        if (texturaUI != null) texturaUI.dispose();

    }
}

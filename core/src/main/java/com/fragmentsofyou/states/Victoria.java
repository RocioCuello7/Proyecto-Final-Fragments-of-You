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
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.handlers.GameStateManager;
import com.fragmentsofyou.handlers.ScreenFader;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


import java.awt.*;

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

        // Creamos el Stage
        stage = new Stage(new FitViewport(320, 180));

        // Creamos el Skin
        skin = new Skin();

        // Fuente
        fuente = new BitmapFont();

        // Textura blanca que utilizaremos para los fondos
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        texturaUI = new Texture(pixmap);
        pixmap.dispose();

        skin.add("font", fuente, BitmapFont.class);
        skin.add("white", texturaUI, Texture.class);

        // Fondos con diferentes colores
        TextureRegionDrawable fondoOscuro =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextureRegionDrawable fondoBoton =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextureRegionDrawable fondoBotonPresionado =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        TextureRegionDrawable fondoCampo =
            new TextureRegionDrawable(new TextureRegion(texturaUI));

        // Estilo del campo de texto
        TextField.TextFieldStyle estiloCampo = new TextField.TextFieldStyle();
        estiloCampo.font = fuente;
        estiloCampo.fontColor = Color.WHITE;
        estiloCampo.cursor = fondoOscuro.tint(Color.WHITE);
        estiloCampo.selection = fondoOscuro.tint(Color.GRAY);
        estiloCampo.background = fondoCampo.tint(new Color(0.12f, 0.12f, 0.12f, 1f));

        // Estilo del botón
        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = fuente;
        estiloBoton.fontColor = Color.WHITE;
        estiloBoton.up = fondoBoton.tint(new Color(0.20f, 0.38f, 0.25f, 1f));
        estiloBoton.down = fondoBotonPresionado.tint(new Color(0.12f, 0.25f, 0.16f, 1f));
        estiloBoton.over = fondoBoton.tint(new Color(0.30f, 0.48f, 0.35f, 1f));

        // Estilo de los textos
        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font = fuente;
        estiloLabel.fontColor = Color.WHITE;

        // Creamos los elementos
        Label titulo = new Label("¡VICTORIA!", estiloLabel);

        Label indicacion = new Label("Ingresa tu nombre:", estiloLabel);

        campoNombre = new TextField("", estiloCampo);
        campoNombre.setMessageText("Tu nombre...");

        botonGuardar = new TextButton("GUARDAR PARTIDA", estiloBoton);

        mensaje = new Label("", estiloLabel);

        // Organizamos los elementos
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        tabla.add(titulo).padBottom(12).row();
        tabla.add(indicacion).padBottom(5).row();
        tabla.add(campoNombre).width(180).height(28).padBottom(8).row();
        tabla.add(botonGuardar).width(150).height(30).padBottom(8).row();
        tabla.add(mensaje).row();

        stage.addActor(tabla);

        // La interfaz recibe los eventos del teclado y del mouse
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

        stage.act(dt);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.02f, 0.05f, 0.02f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        victoryView.apply();
        cam.update();

        mapRender.setView(cam);
        mapRender.render();


        fader.renderFade(cam);

        stage.draw();

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

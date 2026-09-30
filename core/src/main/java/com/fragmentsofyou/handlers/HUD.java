package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.entities.Jugador;

public class HUD {

    private OrthographicCamera hudCam;
    private Viewport hudView;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private BitmapFont fuentePixel;

    public HUD() {
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, 320, 180);
        hudView = new FitViewport(320, 180, hudCam);

        font = new BitmapFont();
        font.getData().setScale(0.5f);

        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixeloidSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        parameter.mono = true;

        fuentePixel = generator.generateFont(parameter);
        generator.dispose();

        fuentePixel.getData().setScale(0.58f, 0.5f);
    }

    public void resize(int width, int height) {
        hudView.update(width, height, true);
    }

    public void render(SpriteBatch sb, Jugador jugador) {
        hudView.apply();
        hudCam.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(hudCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float anchoVida = 50f * ((float) jugador.getVidaActual() / jugador.getVidaMax());
        float anchoEnergia = 50f * (jugador.getLinterna().getEnergia() / jugador.getLinterna().getEnergiaMax());

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.8f);
        shapeRenderer.rect(35, 168, 50, 5);
        shapeRenderer.setColor(0.9f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(35, 168, anchoVida, 5);

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.8f);
        shapeRenderer.rect(35, 161, 50, 4);
        shapeRenderer.setColor(0.2f, 0.7f, 0.9f, 1f);
        shapeRenderer.rect(35, 161, anchoEnergia, 4);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        int vidaAct = (int)jugador.getVidaActual();
        int vidaMax = (int)jugador.getVidaMax();
        int enAct = (int) jugador.getLinterna().getEnergia();
        int enMax = (int) jugador.getLinterna().getEnergiaMax();

        font.draw(sb, "HP", 10, 173);
        font.draw(sb, "EN", 10, 165);

        font.draw(sb, vidaAct + "/" + vidaMax, 90, 173);
        font.draw(sb, enAct + "/" + enMax, 90, 165);
        sb.end();
    }

    public void renderFuenteObjetivo(SpriteBatch sb, String textoObjetivo, float x, float y) {
        fuentePixel.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        fuentePixel.draw(sb, textoObjetivo, x, y);
    }

    public void dispose() {
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}

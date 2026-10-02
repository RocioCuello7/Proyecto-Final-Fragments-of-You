package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fragmentsofyou.enumeradores.ModoFade;

public class ScreenFader {


    private ShapeRenderer shapeRenderer;
    private float alpha;
    private float velocidad;
    private boolean activo;
    private ModoFade modoActual;
    private Color colorActual;

    public ScreenFader() {
        shapeRenderer = new ShapeRenderer();

        this.alpha = 0f;
        this.velocidad = 0f;
        this.activo = false;
        this.modoActual = ModoFade.FADE_OUT;
        this.colorActual = new Color(0f, 0f, 0f, 0f);
    }

    public void startFadeOut(Color color, float duracionSegundos) {
        this.colorActual.set(color);
        this.alpha = 0f;
        this.velocidad = 1f / duracionSegundos;
        this.modoActual = ModoFade.FADE_OUT;
        this.activo = true;
    }

    public void startFlash(Color color, float duracionSegundos) {
        this.colorActual.set(color);
        this.alpha = 1.0f;
        this.velocidad = 1f / duracionSegundos;
        this.modoActual = ModoFade.FADE_IN;
        this.activo = true;
    }

    public void update(float dt) {
        if (!activo) return;

        if (modoActual == ModoFade.FADE_OUT) {
            alpha += velocidad * dt;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
            }
        } else if (modoActual == ModoFade.FADE_IN) {
            alpha -= velocidad * dt;
            if (alpha <= 0.0f) {
                alpha = 0.0f;
                activo = false;
            }
        }
    }

    public void renderOverlay(OrthographicCamera cam, Color color, float alphaOverlay) {
        if (alphaOverlay <= 0f) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color.r, color.g, color.b, alphaOverlay);
        shapeRenderer.rect(
            cam.position.x - cam.viewportWidth / 2f,
            cam.position.y - cam.viewportHeight / 2f,
            cam.viewportWidth,
            cam.viewportHeight
        );
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void renderFade(OrthographicCamera cam) {
        if (!activo && alpha <= 0f) return;
        renderOverlay(cam, colorActual, alpha);
    }

    public boolean isFinished() {
        if (modoActual == ModoFade.FADE_OUT) {
            return alpha >= 1.0f;
        } else {
            return alpha <= 0.0f;
        }
    }

    public void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}

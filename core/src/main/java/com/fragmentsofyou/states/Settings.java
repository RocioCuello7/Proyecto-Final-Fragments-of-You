
package com.fragmentsofyou.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fragmentsofyou.handlers.GameStateManager;

public class Settings extends GameState {

    private Texture texturaUI;
    private TextureRegion barraVolumen;
    private TextureRegion rellenoVolumen;

    public Settings(GameStateManager gsm) {
        super(gsm);

        texturaUI = new Texture(
            Gdx.files.internal("GUI/Bars_1x.png")
        );

        barraVolumen = new TextureRegion(
            texturaUI, 40, 12, 24, 8
        );

        rellenoVolumen = new TextureRegion(
            texturaUI, 0, 40, 16, 3
        );
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(hudCam.combined);

        sb.begin();

        sb.draw(barraVolumen, 300, 250, 120, 40);
        sb.draw(rellenoVolumen, 315, 265, 90, 10);

        sb.draw(barraVolumen, 420, 250, 120, 40);
        sb.draw(rellenoVolumen, 435, 265, 90, 10);

        sb.draw(barraVolumen, 540, 250, 120, 40);
        sb.draw(rellenoVolumen, 555, 265, 90, 10);

        sb.draw(barraVolumen, 660, 250, 120, 40);
        sb.draw(rellenoVolumen, 675, 265, 90, 10);
        sb.end();
    }

    @Override
    public void dispose() {
        if (texturaUI != null) {
            texturaUI.dispose();
        }
    }
}

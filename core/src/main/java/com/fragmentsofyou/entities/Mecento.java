package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Mecento extends Enemigo {

    private Texture texSouth, texNorth, texEast, texWest, texSouthEast, texSouthWest, texNorthEast, texNorthWest;
    private Animation<TextureRegion> animSouth, animNorth, animEast, animWest, animSouthEast, animSouthWest, animNorthEast, animNorthWest;

    public Mecento(float startX, float startY, Entidad objetivo) {

        super(startX, startY, 12f, 12f, 20f, 150, objetivo);

        texSouth = new Texture("enemigos/south.png");
        texNorth = new Texture("enemigos/north.png");
        texEast = new Texture("enemigos/east.png");
        texWest = new Texture("enemigos/west.png");
        texSouthEast = new Texture("enemigos/south-east.png");
        texSouthWest = new Texture("enemigos/south-west.png");
        texNorthEast = new Texture("enemigos/north-east.png");
        texNorthWest = new Texture("enemigos/north-west.png");

        animSouth = crearAnimacion(texSouth, 0.12f);
        animNorth = crearAnimacion(texNorth, 0.12f);
        animEast = crearAnimacion(texEast, 0.12f);
        animWest = crearAnimacion(texWest, 0.12f);
        animSouthEast = crearAnimacion(texSouthEast, 0.12f);
        animSouthWest = crearAnimacion(texSouthWest, 0.12f);
        animNorthEast = crearAnimacion(texNorthEast, 0.12f);
        animNorthWest = crearAnimacion(texNorthWest, 0.12f);
    }

    private Animation<TextureRegion> crearAnimacion(Texture sheet, float frameDuration) {
        int cols = 2;
        int rows = 4;
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    @Override
    public void render(SpriteBatch sb) {
        Animation<TextureRegion> animActual;

        switch (direccionActual) {
            case UP:
                animActual = animNorth;
                break;
            case RIGHT:
                animActual = animEast;
                break;
            case LEFT:
                animActual = animWest;
                break;
            case DOWN:
            default:
                animActual = animSouth;
                break;
        }

        TextureRegion currentFrame = animActual.getKeyFrame(stateTime, true);

        if (tiempoDanio > 0f) {
            sb.setColor(1f, 0.2f, 0.2f, 1f);
        } else if (tiempoAturdido > 0f) {
            sb.setColor(0.3f, 0.7f, 1f, 1f);
        } else if (tiempoRelentizado > 0f) {
            sb.setColor(1f, 0.85f, 0.3f, 1f);
        }

        sb.draw(currentFrame, x - 12, y - 12, 24, 24);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        if (texSouth != null) texSouth.dispose();
        if (texNorth != null) texNorth.dispose();
        if (texEast != null) texEast.dispose();
        if (texWest != null) texWest.dispose();
        if (texSouthEast != null) texSouthEast.dispose();
        if (texSouthWest != null) texSouthWest.dispose();
        if (texNorthEast != null) texNorthEast.dispose();
        if (texNorthWest != null) texNorthWest.dispose();
    }
}

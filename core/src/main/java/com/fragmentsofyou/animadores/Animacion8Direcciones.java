package com.fragmentsofyou.animadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.fragmentsofyou.enumeradores.EightDirection;

public class Animacion8Direcciones {



    private Array<Texture> texturas = new Array<>(8);
    private Array<Animation<TextureRegion>> animaciones = new Array<>(8);
    private EightDirection direccionActual = EightDirection.SOUTH;
    private float stateTime = 0f;

    public Animacion8Direcciones(String rutaBase, float frameDuration) {
        String[] archivos = {
            "north.png", "south.png", "east.png", "west.png",
            "north-east.png", "north-west.png", "south-east.png", "south-west.png"
        };

        for (int i = 0; i < 8; i++) {
            Texture tex = new Texture(rutaBase + archivos[i]);
            texturas.add(tex);
            animaciones.add(crearAnimacion(tex, frameDuration));
        }
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

    public void update(float dt, float dirX, float dirY) {
        boolean enMovimiento = (dirX != 0 || dirY != 0);

        if (enMovimiento) {
            stateTime += dt;

            if (dirY > 0 && dirX > 0) direccionActual = EightDirection.NORTH_EAST;
            else if (dirY > 0 && dirX < 0) direccionActual = EightDirection.NORTH_WEST;
            else if (dirY < 0 && dirX > 0) direccionActual = EightDirection.SOUTH_EAST;
            else if (dirY < 0 && dirX < 0) direccionActual = EightDirection.SOUTH_WEST;
            else if (dirY > 0) direccionActual = EightDirection.NORTH;
            else if (dirY < 0) direccionActual = EightDirection.SOUTH;
            else if (dirX > 0) direccionActual = EightDirection.EAST;
            else if (dirX < 0) direccionActual = EightDirection.WEST;
        } else {
            stateTime = 0f;
        }
    }

    public TextureRegion getCurrentFrame() {
        return animaciones.get(direccionActual.ordinal()).getKeyFrame(stateTime, true);
    }

    public void dispose() {
        for (Texture tex : texturas) {
            if (tex != null) tex.dispose();
        }
    }
}

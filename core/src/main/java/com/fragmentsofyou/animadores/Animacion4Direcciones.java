package com.fragmentsofyou.animadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.fragmentsofyou.enumeradores.FourDirection;

public class Animacion4Direcciones {

    private Array<Texture> texturas = new Array<>(4);
    private Array<Animation<TextureRegion>> animaciones = new Array<>(4);
    private FourDirection direccionActual = FourDirection.SOUTH;
    private float stateTime = 0f;

    public Animacion4Direcciones(String rutaBase, float frameDuration) {
        String[] archivos = {
            "animArriba.png",
            "animAbajo.png",
            "animDerecha.png",
            "animIzquierda.png"
        };

        for (int i = 0; i < 4; i++) {
            Texture tex = new Texture(rutaBase + archivos[i]);
            texturas.add(tex);
            animaciones.add(crearAnimacion(tex, frameDuration));
        }
    }

    private Animation<TextureRegion> crearAnimacion(Texture sheet, float frameDuration) {
        int cols = 4;
        int rows = 1;
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

            if (dirY > 0) direccionActual = FourDirection.NORTH;
            else if (dirY < 0) direccionActual = FourDirection.SOUTH;
            else if (dirX > 0) direccionActual = FourDirection.EAST;
            else if (dirX < 0) direccionActual = FourDirection.WEST;
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

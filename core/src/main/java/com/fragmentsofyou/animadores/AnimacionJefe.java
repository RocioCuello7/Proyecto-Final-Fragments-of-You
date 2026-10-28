package com.fragmentsofyou.animadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.fragmentsofyou.enumeradores.EstadoJefe;

import java.util.EnumMap;

public class AnimacionJefe{

    private static final int CELDA = 32; // ajustar al tamaño real del frame

    private Texture textura;
    private TextureRegion[][] celdas;
    private EnumMap<EstadoJefe, Animation<TextureRegion>> animaciones = new EnumMap<>(EstadoJefe.class);
    private EstadoJefe estadoActual = EstadoJefe.IDLE;
    private float stateTime = 0f;
    private boolean mirandoIzquierda = false;

    public AnimacionJefe(String ruta) {
        textura = new Texture(ruta);
        celdas = TextureRegion.split(textura, CELDA, CELDA);

        animaciones.put(EstadoJefe.IDLE,
            crear(0, 0, 4, 0.15f, Animation.PlayMode.LOOP));
        animaciones.put(EstadoJefe.ATAQUE,
            crear(6, 5, 9, 0.10f, Animation.PlayMode.NORMAL));
        animaciones.put(EstadoJefe.DESAPARECER,
            crear(4, 5, 9, 0.12f, Animation.PlayMode.NORMAL));
        animaciones.put(EstadoJefe.APARECER,
            crear(6, 5, 9, 0.12f, Animation.PlayMode.NORMAL));
    }

    private Animation<TextureRegion> crear(int fila, int colIni, int colFin,
                                           float duracion, Animation.PlayMode modo) {
        Array<TextureRegion> arr = new Array<>();
        for (int c = colIni; c <= colFin; c++) {
            arr.add(celdas[fila][c]);
        }
        return new Animation<>(duracion, arr, modo);
    }

    public void setEstado(EstadoJefe nuevo) {
        if (nuevo != estadoActual) {
            estadoActual = nuevo;
            stateTime = 0f; // reinicia la animación
        }
    }

    public void update(float dt, float dirX) {
        stateTime += dt;
        if (dirX < 0) mirandoIzquierda = true;
        else if (dirX > 0) mirandoIzquierda = false;
    }

    public boolean terminoAnimacion() {
        return animaciones.get(estadoActual).isAnimationFinished(stateTime);
    }

    public TextureRegion getCurrentFrame() {
        TextureRegion frame = new TextureRegion(
            animaciones.get(estadoActual).getKeyFrame(stateTime)
        );

        if (frame.isFlipX() != mirandoIzquierda) {
            frame.flip(true, false);
        }

        return frame;
    }

    public EstadoJefe getEstado() {
        return estadoActual;
    }

    public void dispose() {
        textura.dispose();
    }
}

package com.fragmentsofyou.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.fragmentsofyou.handlers.MapCollision;


public class ProyectilOcular {
    private float x, y;
    private float velX, velY;
    private float velocidad=110f;
    private Rectangle bounds;
    private boolean activo=true;
    private static Texture texturaProyectil;

    private static final float ANCHO = 8f;
    private static final float ALTO = 8f;

    private float curvatura = 2.5f;

    public ProyectilOcular(float startX, float startY, float objetivoX, float objetivoY){
        this.x = startX ;
        this.y = startY ;
        this.bounds=new Rectangle(x - ANCHO / 2f, y - ALTO / 2f, ANCHO, ALTO);

        if(texturaProyectil==null){
            Pixmap pixmap = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
            pixmap.setColor(new Color(0.8f,0.3f,1f,1f));
            pixmap.fillCircle(4,4,4);
            texturaProyectil=new Texture(pixmap);
            pixmap.dispose();
        }

        float angulo=(float) Math.atan2(objetivoY - startY, objetivoX - startX);
        this.velX=(float) Math.cos(angulo)*velocidad;
        this.velY=(float) Math.sin(angulo)*velocidad;
    }

    public void update(float dt, MapCollision mapCollision, Jugador jugador) {
        if (!activo) return;

        float distanciaMovimiento = velocidad * dt;
        int pasos = Math.max(1, (int) Math.ceil(distanciaMovimiento / 2f));
        float subDt = dt / pasos;

        for (int i = 0; i < pasos; i++) {

            float anguloActual = (float) Math.atan2(velY, velX);

            float anguloObjetivo = (float) Math.atan2(
                jugador.getY() - y,
                jugador.getX() - x
            );

            float diferencia = anguloObjetivo - anguloActual;

            while (diferencia > Math.PI) {
                diferencia -= 2f * (float) Math.PI;
            }

            while (diferencia < -Math.PI) {
                diferencia += 2f * (float) Math.PI;
            }

            float giroMaximo = curvatura * subDt;

            diferencia = Math.max(
                -giroMaximo,
                Math.min(giroMaximo, diferencia)
            );

            float nuevoAngulo = anguloActual + diferencia;

            velX = (float) Math.cos(nuevoAngulo) * velocidad;
            velY = (float) Math.sin(nuevoAngulo) * velocidad;

            float siguienteX = x + velX * subDt;
            float siguienteY = y + velY * subDt;

            if (mapCollision != null &&
                mapCollision.isColliding(
                    siguienteX, siguienteY, ANCHO, ALTO
                )) {
                activo = false;
                return;
            }

            x = siguienteX;
            y = siguienteY;

            bounds.setPosition(
                x - ANCHO / 2f,
                y - ALTO / 2f
            );

            float dx = x - jugador.getX();
            float dy = y - jugador.getY();

            float radioX = ANCHO / 2f + jugador.getWidth() / 2f;
            float radioY = ALTO / 2f + jugador.getHeight() / 2f;

            if (Math.abs(dx) <= radioX &&
                Math.abs(dy) <= radioY) {
                jugador.recibirImpactoOcular(10f);
                activo = false;
                return;
            }
        }
    }

    public void render(SpriteBatch sb){
        if(activo && texturaProyectil!=null){
            sb.draw(texturaProyectil, x - ANCHO / 2f, y - ALTO / 2f, ANCHO, ALTO);
        }
    }

    public static void dispose() {
        if (texturaProyectil != null) {
            texturaProyectil.dispose();
            texturaProyectil = null;
        }
    }

    public boolean isActivo(){ return activo; }
}

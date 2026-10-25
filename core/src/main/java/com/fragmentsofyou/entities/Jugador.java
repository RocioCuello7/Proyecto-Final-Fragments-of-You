package com.fragmentsofyou.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.animadores.Animacion4Direcciones;
import com.fragmentsofyou.armas.Linterna;
import com.fragmentsofyou.handlers.MapCollision;

public class Jugador extends Entidad {

    private Animacion4Direcciones animador;
    private Linterna linterna;

    private float rotacionMouse;

    private boolean destelloDisparado = false;
    private boolean linternaHabilitada = false;
    private boolean ataquesHabilitados = false;

    private float dirX, dirY;

    private Vector3 mousePos = new Vector3();

    private float anchoLuzPersonal = 32f, altoLuzPersonal = 32f;
    private PointLight luzPersonal;

    private boolean puedoMoverme = true;

    private boolean sobrecargaHabilitada = false;
    private boolean destelloHabilitado = false;

    private int impactosOcularesRecibidos = 0;
    private float tiempoRalentizadoOcular = 0f;
    private float tiempoInmovilOcular = 0f;

    public Jugador(float startX, float startY, RayHandler rayHandler) {
        super(startX, startY, 10f, 10f, 90f, 100);

        luzPersonal = new PointLight(rayHandler, 64, new Color(1f, 1f, 1f, 0.85f), 30f, x, y);
        luzPersonal.setSoft(true);
        luzPersonal.setXray(true);
        this.animador = new Animacion4Direcciones("glenn/", 0.15f);
        this.linterna = new Linterna(rayHandler, startX, startY, 0f);
    }

    public void handleInput(Viewport viewport) {
        if (!puedoMoverme) {
            dirX = 0;
            dirY = 0;
            return;
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        float deltaX = mousePos.x - x;
        float deltaY = mousePos.y - y;
        rotacionMouse = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

        dirX = 0;
        dirY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dirY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dirY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dirX += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dirX -= 1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (linternaHabilitada) {
                linterna.alternarEncendido();
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (sobrecargaHabilitada && linterna.puedeConsumirEnergia(20f)) {
                linterna.dispararSobrecarga();
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (destelloHabilitado) {
                destelloDisparado = true;
            }
        }
    }

    @Override
    public void update(float dt, MapCollision mapCollision) {
        float velocidadActual = speed;

        if (tiempoRalentizadoOcular > 0f) {
            velocidadActual = speed * 0.6f;
        }
        mover(dirX, dirY, velocidadActual, dt, mapCollision);

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            linterna.recargar(dt);
        }

        if (luzPersonal != null) {
            luzPersonal.setPosition(x + (anchoLuzPersonal / 2f) - 16f, y + (altoLuzPersonal / 2f) - 16f);
        }
        animador.update(dt, dirX, dirY);
        linterna.update(dt, x, y, rotacionMouse);

        if (tiempoRalentizadoOcular > 0f) {
            tiempoRalentizadoOcular -= dt;
            if (tiempoRalentizadoOcular <= 0f) {
                impactosOcularesRecibidos = 0;
            }
        }

        if (tiempoInmovilOcular > 0f) {
            tiempoInmovilOcular -= dt;
            if (tiempoInmovilOcular <= 0f) {
                puedoMoverme = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(animador.getCurrentFrame(), x - 8, y - 8, 16, 16);
    }

    public void teletransportar(float nuevoX, float nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;

        if (luzPersonal != null) {
            luzPersonal.setPosition(x + (anchoLuzPersonal / 2f) - 16f, y + (altoLuzPersonal / 2f) - 16f);
        }
        if (linterna != null) {
            linterna.update(0, x, y, rotacionMouse);
        }
    }

    public boolean consumioDestello() {
        if (destelloDisparado) {
            destelloDisparado = false;
            return true;
        }
        return false;
    }

    public void recibirImpactoOcular(float danio) {
        recibirDanio(danio);
        impactosOcularesRecibidos++;

        if (impactosOcularesRecibidos == 1 || impactosOcularesRecibidos == 2) {
            tiempoRalentizadoOcular = 3.0f;
        } else if (impactosOcularesRecibidos >= 3) {
            tiempoInmovilOcular = 2.0f;
            puedoMoverme = false;
            impactosOcularesRecibidos = 0;
            tiempoRalentizadoOcular = 0;
        }
    }

    public void setPuedoMoverme(boolean puedoMoverme) {
        this.puedoMoverme = puedoMoverme;
    }

    public float getRotacion() {
        return rotacionMouse;
    }

    public Linterna getLinterna() {
        return linterna;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void dispose() {
        if (animador != null) animador.dispose();
    }

    public void setLinternaHabilitada(boolean linternaHabilitada) {
        this.linternaHabilitada = linternaHabilitada;
    }

    public void setSobrecargaHabilitada(boolean sobrecargaHabilitada) {
        this.sobrecargaHabilitada = sobrecargaHabilitada;
        if (sobrecargaHabilitada) {
            this.linternaHabilitada = true;
            if (this.linterna != null && !this.linterna.isEncendida()) {
                this.linterna.alternarEncendido();
            }
        }
    }

    public void setVidaJugador(int vida) {
        this.vidaActual += vida;
        if (vidaActual > vidaMax) {
            this.vidaActual = 100;
        }
    }

    public void setDestelloHabilitado(boolean destelloHabilitado) {
        this.destelloHabilitado = destelloHabilitado;
    }

    public boolean isDestelloHabilitado() {
        return destelloHabilitado;
    }
}

package com.fragmentsofyou.entities;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.animadores.CharacterAnimator;
import com.fragmentsofyou.armas.Linterna;
import com.fragmentsofyou.handlers.MapCollision;

public class Jugador extends Entidad{

    private CharacterAnimator animador;
    private Linterna linterna;

    private float rotacionMouse;

    private boolean destelloDisparado = false;

    private float dirX, dirY;

    private Vector3 mousePos = new Vector3();

    public Jugador(float startX, float startY, RayHandler rayHandler) {
        super(startX,startY,10f,10f,90f,100);


        this.animador = new CharacterAnimator("spriteGlenn.png", 0.15f);
        this.linterna = new Linterna(rayHandler, startX, startY, 0f);
    }

    public void handleInput(Viewport viewport) {
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
            linterna.alternarEncendido();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            linterna.dispararSobrecarga();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if(linterna.dispararDestello()) {
                destelloDisparado=true;
            }
        }
    }

    @Override
    public void update(float dt, MapCollision mapCollision) {

        mover(dirX,dirY,speed,dt,mapCollision);

        animador.update(dt, dirX, dirY);
        linterna.update(dt, x, y, rotacionMouse);
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.draw(animador.getCurrentFrame(), x - 8, y - 8, 16, 16);
    }

    public boolean consumioDestello() {
        if (destelloDisparado) {
            destelloDisparado = false;
            return true;
        }
        return false;
    }

    public float getRotacion() { return rotacionMouse; }
    public Linterna getLinterna() { return linterna; }

    public void dispose() {
        if (animador != null) animador.dispose();
        if (linterna != null) linterna.dispose();
    }
}

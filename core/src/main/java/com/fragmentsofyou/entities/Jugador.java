package com.fragmentsofyou.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fragmentsofyou.animadores.Animacion4Direcciones;
import com.fragmentsofyou.armas.Linterna;
import com.fragmentsofyou.handlers.MapCollision;

public class Jugador extends Entidad{

    private Animacion4Direcciones animador;
    private Linterna linterna;

    private float rotacionMouse;

    private boolean destelloDisparado = false;

    private float dirX, dirY;

    private Vector3 mousePos = new Vector3();

    private float anchoLuzPersonal = 32f, altoLuzPersonal = 32f;
    private PointLight luzPersonal;

    private boolean puedoMoverme = true;

    private BitmapFont fuentePixel;
    private GlyphLayout layout = new GlyphLayout();
    private NinePatch fondoGloboTexto;
    private String[] dialogosNota;
    private int indiceDialogo = 0;
    private boolean mostrandoDialogo = false;
    private boolean dialogoNotaTerminado = false;

    private float paddingX = 15f;
    private float paddingY = 25f;


    public Jugador(float startX, float startY, RayHandler rayHandler) {
        super(startX,startY,10f,10f,90f,100);

        luzPersonal = new PointLight(rayHandler, 64, new Color(1f, 1f, 1f, 0.85f), 30f, x, y);
        luzPersonal.setSoft(true);
        luzPersonal.setXray(true);
        this.animador = new Animacion4Direcciones("glenn/", 0.15f);
        this.linterna = new Linterna(rayHandler, startX, startY, 0f);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixeloidSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        parameter.mono = true;
        fuentePixel = generator.generateFont(parameter);
        generator.dispose();

        fuentePixel.getData().setScale(0.58f, 0.5f);

        Texture texBocadillo = new Texture("image-Photoroom.png");
        fondoGloboTexto = new NinePatch(texBocadillo, 4, 4, 4, 4);
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
            linterna.alternarEncendido();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (linterna.puedeConsumirEnergia(20f)) {
                    linterna.dispararSobrecarga();
            }
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

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            linterna.recargar(dt);
        }

        if (luzPersonal != null) {
            luzPersonal.setPosition(x + (anchoLuzPersonal / 2f)-16f, y + (altoLuzPersonal / 2f)-16f);
        }
        animador.update(dt, dirX, dirY);
        linterna.update(dt, x, y, rotacionMouse);
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.draw(animador.getCurrentFrame(), x - 8, y - 8, 16, 16);

        if (mostrandoDialogo && dialogosNota != null) {
            String texto = dialogosNota[indiceDialogo];
            float anchoMaximoTexto = 60f;

            layout.setText(fuentePixel, texto, com.badlogic.gdx.graphics.Color.BLACK, anchoMaximoTexto, Align.left, true);

            float anchoTexto = layout.width;
            float altoTexto = layout.height;

            float anchoGlobo = anchoTexto + (paddingX * 2f);
            float altoGlobo = altoTexto + (paddingY * 2f) + 15f;

            float globoX = x - (anchoGlobo / 2f);
            float globoY = y - 35f;

            sb.setColor(1f, 1f, 1f, 1f);
            fondoGloboTexto.draw(sb, globoX, globoY, anchoGlobo, altoGlobo);

            float textoX = globoX + paddingX + 8f;
            float textoY = globoY + altoGlobo - paddingY + 4f;

            fuentePixel.setColor(com.badlogic.gdx.graphics.Color.BLACK);
            fuentePixel.draw(sb, texto, textoX, textoY, anchoMaximoTexto, Align.left, true);
        }
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

    public void iniciarDialogoNota(String[] lineas) {
        this.dialogosNota = lineas;
        this.indiceDialogo = 0;
        this.mostrandoDialogo = true;
        this.dialogoNotaTerminado = false;
        setPuedoMoverme(false);
    }

    public boolean consumioDestello() {
        if (destelloDisparado) {
            destelloDisparado = false;
            return true;
        }
        return false;
    }

    public void avanzarDialogo() {
        if (!mostrandoDialogo) return;

        indiceDialogo++;
        if (indiceDialogo >= dialogosNota.length) {
            mostrandoDialogo = false;
            indiceDialogo = 0;
            dialogoNotaTerminado = true;
            setPuedoMoverme(true);
        }
    }

    public void setPuedoMoverme(boolean puedoMoverme) {
        this.puedoMoverme = puedoMoverme;
    }


    public boolean isMostrandoDialogo() { return mostrandoDialogo; }
    public boolean isDialogoNotaTerminado() { return dialogoNotaTerminado; }
    public float getRotacion() { return rotacionMouse; }
    public Linterna getLinterna() { return linterna; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public void dispose() {
        if (animador != null) animador.dispose();
        if (linterna != null) linterna.dispose();
        if(luzPersonal!=null) luzPersonal.dispose();
    }


}

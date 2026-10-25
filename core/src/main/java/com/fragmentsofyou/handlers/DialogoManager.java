package com.fragmentsofyou.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.fragmentsofyou.entities.Jugador;

public class DialogoManager {

    private BitmapFont fuentePixel;
    private GlyphLayout layout = new GlyphLayout();
    private NinePatch fondoGloboTexto;

    private String[] dialogoNota;
    private int indiceDialogo = 0;
    private boolean mostrandoDialogo = false;
    private boolean dialogoTerminado = false;

    private String textoCompleto = "";
    private String textoActual = "";
    private float timerLetras = 0f;
    private float velocidadLetras = 0.03f;
    private int indiceLetra = 0;

    private float paddingX = 15f;
    private float paddingY = 25f;

    private float targetX = 0f;
    private float targetY = 0f;
    private boolean usarPosicionPersonalizada = false;

    private int framesIgnorados = 0;

    public DialogoManager(){
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

    public void iniciarDialogo(String[] lineas, Jugador jugador){
        this.dialogoNota = lineas;
        this.indiceDialogo = 0;
        this.mostrandoDialogo = true;
        this.dialogoTerminado = false;
        this.usarPosicionPersonalizada = false;
        this.framesIgnorados = 2;

        cargarLineaActual();
        jugador.setPuedoMoverme(false);
    }

    public void iniciarDialogoConPosicion(String[] lineas, Jugador jugador, float x, float y){
        this.dialogoNota = lineas;
        this.indiceDialogo = 0;
        this.mostrandoDialogo = true;
        this.dialogoTerminado = false;
        this.usarPosicionPersonalizada = true;
        this.targetX = x;
        this.targetY = y;
        this.framesIgnorados = 2;

        cargarLineaActual();
        jugador.setPuedoMoverme(false);
    }

    private void cargarLineaActual() {
        if(dialogoNota != null && indiceDialogo < dialogoNota.length){
            textoCompleto = dialogoNota[indiceDialogo];
            textoActual = "";
            indiceLetra = 0;
            timerLetras = 0f;
        }
    }

    public void update(float dt, Jugador jugador){
        if(!mostrandoDialogo) return;

        if(framesIgnorados > 0){
            framesIgnorados--;
            return;
        }

        if(Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E)){
            avanzarDialogo(jugador);
        }

        if(indiceLetra < textoCompleto.length()){
            timerLetras += dt;
            if(timerLetras >= velocidadLetras){
                timerLetras = 0f;
                indiceLetra++;
                textoActual = textoCompleto.substring(0, indiceLetra);
            }
        }
    }

    public void avanzarDialogo(Jugador jugador){
        if(!mostrandoDialogo) return;

        if(indiceLetra < textoCompleto.length()){
            indiceLetra = textoCompleto.length();
            textoActual = textoCompleto;
            return;
        }

        indiceDialogo++;
        if(indiceDialogo < dialogoNota.length){
            cargarLineaActual();
        }else{
            mostrandoDialogo = false;
            indiceDialogo = 0;
            dialogoTerminado = true;
            jugador.setPuedoMoverme(true);
        }
    }

    public void render(SpriteBatch sb, float jugadorX, float jugadorY){
        if(!mostrandoDialogo || dialogoNota == null) return;
        float anchoMaximoTexto = 60f;

        layout.setText(fuentePixel, textoActual, Color.BLACK, anchoMaximoTexto, Align.left, true);

        float anchoTexto = layout.width;
        float altoTexto = layout.height;

        float anchoGlobo = anchoTexto + (paddingX * 2f);
        float altoGlobo = altoTexto + (paddingY * 2f) + 15f;

        float posX = usarPosicionPersonalizada ? targetX : jugadorX;
        float posY = usarPosicionPersonalizada ? targetY : (jugadorY + 18f);

        float globoX = posX - (anchoGlobo / 2f);
        float globoY = posY;

        sb.setColor(1f, 1f, 1f, 1f);
        fondoGloboTexto.draw(sb, globoX, globoY, anchoGlobo, altoGlobo);

        float textoX = globoX + paddingX + 8f;
        float textoY = globoY + altoGlobo - paddingY + 4f;

        fuentePixel.setColor(Color.BLACK);
        fuentePixel.draw(sb, textoActual, textoX, textoY, anchoMaximoTexto, Align.left, true);
    }

    public boolean isMostrandoDialogo(){
        return mostrandoDialogo;
    }

    public boolean isDialogoTerminado() {
        return dialogoTerminado;
    }

    public String[] getDialogoActual() {
        return dialogoNota;
    }

    public void dispose(){
        if(fuentePixel != null) fuentePixel.dispose();
        if(fondoGloboTexto != null && fondoGloboTexto.getTexture() != null) {
            fondoGloboTexto.getTexture().dispose();
        }
    }
}

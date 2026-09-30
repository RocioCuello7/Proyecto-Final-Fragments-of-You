package com.fragmentsofyou.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class Abuela {

    private Sprite sprite;
    private Texture textura;
    private float x, y;
    private boolean visible = true;

    private BitmapFont fuentePixel;
    private GlyphLayout layout = new GlyphLayout();
    private String[] dialogos;
    private int indiceDialogo = 0;
    private boolean mostrandoDialogo = false;
    private float distanciaInteraccion = 35f;

    private NinePatch fondoGloboTexto;
    private float paddingX = 15f;
    private float paddingY = 25f;

    private boolean dialogoTerminado = false;

    public Abuela(float x, float y) {
        this.x = x;
        this.y = y;

        textura = new Texture("abuela/Abuela.png");
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
        sprite.setScale(0.5f);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixeloidSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        parameter.mono = true;
        fuentePixel = generator.generateFont(parameter);
        generator.dispose();

        fuentePixel.getData().setScale(0.58f, 0.5f);

        dialogos = new String[] {
            "Glenn... escuchaste esos ruidos afuera?",
            "La luz de la casa esta fallando...",
            "creo que esta ",
            " . . . "
        };

        Texture texBocadillo = new Texture("image-Photoroom.png");
        fondoGloboTexto = new NinePatch(texBocadillo, 4, 4, 4, 4);
    }

    public void update(float dt, float jugadorX, float jugadorY) {
        if (!visible) return;

        float distToAbuela = Vector2.dst(jugadorX, jugadorY, x, y);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E) && distToAbuela <= distanciaInteraccion) {
            if (!mostrandoDialogo) {
                mostrandoDialogo = true;
                indiceDialogo = 0;
            } else {
                indiceDialogo++;
                if (indiceDialogo >= dialogos.length) {
                    mostrandoDialogo = false;
                    indiceDialogo = 0;
                    dialogoTerminado = true;
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!visible) return;

        sprite.draw(sb);

        if (mostrandoDialogo) {
            String texto = dialogos[indiceDialogo];
            float anchoMaximoTexto = 60f;

            layout.setText(fuentePixel, texto, com.badlogic.gdx.graphics.Color.BLACK, anchoMaximoTexto, com.badlogic.gdx.utils.Align.left, true);

            float anchoTexto = layout.width;
            float altoTexto = layout.height;

            float anchoGlobo = anchoTexto + (paddingX * 2f);
            float altoGlobo = altoTexto + (paddingY * 2f) + 15f;

            float globoX = x + (sprite.getWidth() / 2f) - (anchoGlobo / 2f) + 12f;
            float globoY = y + sprite.getHeight() - 40f;

            sb.setColor(1f, 1f, 1f, 1f);
            fondoGloboTexto.draw(sb, globoX, globoY, anchoGlobo, altoGlobo);

            float textoX = globoX + paddingX + 8f;
            float textoY = globoY + altoGlobo - paddingY + 4f;

            fuentePixel.setColor(com.badlogic.gdx.graphics.Color.BLACK);
            fuentePixel.draw(sb, texto, textoX, textoY, anchoMaximoTexto, com.badlogic.gdx.utils.Align.left, true);
        }
    }

    public boolean isDialogoTerminado() {
        return dialogoTerminado;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose() {
        if (textura != null) textura.dispose();
        if (fuentePixel != null) fuentePixel.dispose();
    }
}

package com.fragmentsofyou.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BarraJefe {

    private static final int M_IZQ = 17, M_ARR = 6, M_DER = 5, M_ABA = 6;


    private static final int FILL_X = 16;
    private static final int FILL_Y = 5;
    private static final int FILL_PAD_DER =3;
    private static final int FILL_ALTO = 6;

    private final Texture texMarco, texRelleno;
    private final NinePatch marco;
    private final TextureRegion relleno;

    private final float x, y, ancho, alto;

    public BarraJefe(String rutaMarco, String rutaRelleno,
                     float x, float y, float ancho, float alto) {
        texMarco = new Texture(rutaMarco);
        texRelleno = new Texture(rutaRelleno);
        texMarco.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texRelleno.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        marco = new NinePatch(texMarco, M_IZQ, M_DER, M_ARR, M_ABA);
        relleno = recortarPixelesVisibles(rutaRelleno, texRelleno);

        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    private TextureRegion recortarPixelesVisibles(String ruta, Texture tex) {
        Pixmap pm = new Pixmap(Gdx.files.internal(ruta));
        int minX = pm.getWidth(), minY = pm.getHeight(), maxX = -1, maxY = -1;

        for (int py = 0; py < pm.getHeight(); py++) {
            for (int px = 0; px < pm.getWidth(); px++) {
                int alpha = pm.getPixel(px, py) & 0xff;
                if (alpha > 0) {
                    if (px < minX) minX = px;
                    if (px > maxX) maxX = px;
                    if (py < minY) minY = py;
                    if (py > maxY) maxY = py;
                }
            }
        }
        pm.dispose();

        if (maxX < 0) return new TextureRegion(tex);
        return new TextureRegion(tex, minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    public void render(SpriteBatch batch, float vida, float vidaMax) {
        float pct = Math.max(0f, Math.min(1f, vida / vidaMax));

        // 1) marco
        marco.draw(batch, x, y, ancho, alto);

        if (pct > 0f) {
            float rx = x + FILL_X;
            float ry = y + FILL_Y;
            float rAncho = ancho - FILL_X - FILL_PAD_DER;

            float u = relleno.getU();
            float u2 = relleno.getU2();
            relleno.setU2(u + (u2 - u) * pct);
            batch.draw(relleno, rx, ry, rAncho * pct, FILL_ALTO);
            relleno.setU2(u2);
        }
    }

    public void dispose() {
        texMarco.dispose();
        texRelleno.dispose();
    }
}

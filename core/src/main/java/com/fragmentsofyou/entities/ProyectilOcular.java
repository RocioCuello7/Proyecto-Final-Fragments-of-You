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
    private float velocidad=75f;
    private Rectangle bounds;
    private boolean activo=true;
    private static Texture texturaProyectil;

    public ProyectilOcular(float startX, float startY, float objetivoX, float objetivoY){
        this.x=startX;
        this.y=startY;
        this.bounds=new Rectangle(x, y, 6f, 6f);

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

    public void update(float dt, MapCollision mapCollision, Jugador jugador){
        if(!activo) return;

        x+=velX*dt;
        y+=velY*dt;
        bounds.setPosition(x, y);

        if(mapCollision!=null && mapCollision.isColliding(x,y,bounds.getWidth(), bounds.getHeight())){
            activo=false;
        }

        Rectangle rectJugador = new Rectangle(jugador.getX()-8f, jugador.getY()-8f,16f,16f );
        if(bounds.overlaps(rectJugador)){
            jugador.recibirImpactoOcular(10f);
            activo=false;
        }
    }

    public void render(SpriteBatch sb){
        if(activo && texturaProyectil!=null){
            sb.draw(texturaProyectil,x,y,8,8);
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

package com.fragmentsofyou.handlers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MapCollision {

    private MapObjects collisionObjects;

    private Rectangle entidadRect = new Rectangle();
    private Vector2 pInicio = new Vector2();
    private Vector2 pFin = new Vector2();

    public MapCollision(TiledMap map, String nombreCapa, World world) {
        if (map != null) {
            MapLayer capa = map.getLayers().get(nombreCapa);
            if (capa != null) {
                collisionObjects = capa.getObjects();

                if(world != null){
                    crearCuerposBox2D(world);
                }
            }
        }
    }

    private void crearCuerposBox2D(World world) {
        for(MapObject object : collisionObjects){
            if(object instanceof RectangleMapObject){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();

                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;

                bdef.position.set(rect.x + rect.width / 2f, rect.y + rect.height / 2f);

                Body body = world.createBody(bdef);

                PolygonShape shape = new PolygonShape();

                shape.setAsBox(rect.width / 2f, rect.height / 2f);

                body.createFixture(shape,0f);
                shape.dispose();
            }
        }
    }

    public boolean isColliding(float x, float y, float width, float height) {
        if (collisionObjects == null){
            return false;
        }

        entidadRect.set(x - width / 2f, y - height / 2f, width, height);

        for (MapObject object : collisionObjects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (rect.overlaps(entidadRect)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hayLineaDeVision(float x1, float y1, float x2, float y2){

        if(collisionObjects==null){
            return true;
        }

        pInicio.set(x1,y1);
        pFin.set(x2,y2);

        for(MapObject object : collisionObjects){
            if(object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                if (Intersector.intersectSegmentRectangle(pInicio, pFin, rect)) {
                    return false;
                }
            }
        }
        return true;
    }
}

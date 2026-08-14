package com.fragmentsofyou.handlers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class MapCollision {

    private MapObjects collisionObjects;

    public MapCollision(TiledMap map, String nombreCapa) {
        if (map != null) {
            MapLayer capa = map.getLayers().get(nombreCapa);
            if (capa != null) {
                collisionObjects = capa.getObjects();
            }
        }
    }

    public boolean isColliding(float x, float y, float width, float height) {
        if (collisionObjects == null){
            return false;
        }

        Rectangle playerRect = new Rectangle(x - width / 2f, y - height / 2f, width, height);

        for (MapObject object : collisionObjects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (rect.overlaps(playerRect)) {
                    return true;
                }
            }
        }

        return false;
    }
}

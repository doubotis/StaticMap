/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.maps;

import com.doubotis.mappicturegenerator.StaticMap;
import com.doubotis.mappicturegenerator.geo.Location;
import com.doubotis.mappicturegenerator.geo.MercatorProjection;
import com.doubotis.mappicturegenerator.geo.MercatorUtils;
import com.doubotis.mappicturegenerator.geo.PointF;
import com.doubotis.mappicturegenerator.geo.Tile;
import com.doubotis.mappicturegenerator.layers.Layer;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author Christophe
 */
public abstract class BaseMapType implements Layer
{
    
    private int mTileSize = 256;
    private float mOpacity = 1.0f;
    
    public void setOpacity(float opacity)
    {
        mOpacity = opacity;
    }
    
    public float getOpacity()
    {
        return mOpacity;
    }
    
    public abstract Image getTile(int tileX, int tileY, int tileZ);

    @Override
    public void draw(Graphics2D graphics, StaticMap mp)
    {
        MercatorProjection proj = mp.getProjection();
        int tileSize = proj.getTileSize();
        
        int tileX = proj.tileXFromLongitude(mp.getLocation().longitude, mp.getZoom());
        int tileY = proj.tileYFromLatitude(mp.getLocation().latitude, mp.getZoom());
        int tileZ = mp.getZoom();
        
        
        // Get the top left point.
        PointF topLeftPixels = new PointF(0 + mp.getOffset().x,
                0 + mp.getOffset().y);
        System.out.println("topLeftPixels: " + topLeftPixels.toString());
        Location topLeftLocation = proj.fromPointToLatLng(topLeftPixels, mp.getZoom());
        System.out.println("topLeftLocation: " + topLeftLocation.toString());
        Tile topLeftTile = new Tile(
                proj.tileXFromLongitude(topLeftLocation.longitude, mp.getZoom()),
                proj.tileYFromLatitude(topLeftLocation.latitude, mp.getZoom()),
                mp.getZoom());
        
        // Get the bottom right point.
        PointF bottomRightPixels = new PointF(mp.getWidth() + mp.getOffset().x,
                mp.getHeight() + mp.getOffset().y);
        System.out.println("bottomRightPixels: " + bottomRightPixels.toString());
        Location bottomRightLocation = proj.fromPointToLatLng(bottomRightPixels, mp.getZoom());
        System.out.println("bottomRightLocation: " + bottomRightLocation.toString());
        Tile bottomRightTile = new Tile(
                proj.tileXFromLongitude(bottomRightLocation.longitude, mp.getZoom()),
                proj.tileYFromLatitude(bottomRightLocation.latitude, mp.getZoom()),
                mp.getZoom());
        
        System.out.println("");
        
        // Get the top left corner or the top left tile before looping.
        double topLeftCornerLat = proj.latitudeFromTile(topLeftTile.y, mp.getZoom());
        double topLeftCornerLon = proj.longitudeFromTile(topLeftTile.x, mp.getZoom());
        PointF topLeftCorner = proj.fromLatLngToPoint(topLeftCornerLat, topLeftCornerLon, mp.getZoom());
        
        int i = 0,j = 0;
        for (int y = topLeftTile.y; y <= bottomRightTile.y; y++)
        {
            for (int x = topLeftTile.x; x <= bottomRightTile.x; x++)
            {
                // Get the tile.
                System.out.println("Get the tile " + x + "," + y + "," + tileZ);
                Image im = getTile(x, y, tileZ);
                
                // Get the "true" pos.
                PointF truePos = new PointF(topLeftCorner.x + (tileSize * i),
                        topLeftCorner.y + (tileSize * j));
                
                // Get the pos.
                PointF tilePos = new PointF(truePos.x - mp.getOffset().x ,
                    truePos.y - mp.getOffset().y);
                
                System.out.println("Draw the tile at " + tilePos.x + "," + tilePos.y);
                
                // Draw the tile.
                graphics.drawImage(im,
                        (int)tilePos.x, 
                        (int)tilePos.y, 
                        tileSize, 
                        tileSize, 
                        null);
                
                i++;
            }
            i = 0;
            j++;
        }
    }
    
}

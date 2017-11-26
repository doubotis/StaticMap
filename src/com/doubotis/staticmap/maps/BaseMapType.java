/*
 * Copyright (C) 2017 doubotis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.doubotis.staticmap.maps;

import com.doubotis.staticmap.StaticMap;
import com.doubotis.staticmap.geo.Location;
import com.doubotis.staticmap.geo.MercatorProjection;
import com.doubotis.staticmap.geo.MercatorUtils;
import com.doubotis.staticmap.geo.PointF;
import com.doubotis.staticmap.geo.Tile;
import com.doubotis.staticmap.layers.Layer;
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
        
        int tileX = proj.tileXFromLongitude(mp.getLocation().mLongitude, mp.getZoom());
        int tileY = proj.tileYFromLatitude(mp.getLocation().mLatitude, mp.getZoom());
        int tileZ = mp.getZoom();
        
        
        // Get the top left point.
        PointF topLeftPixels = new PointF(0 + mp.getOffset().x,
                0 + mp.getOffset().y);
        System.out.println("topLeftPixels: " + topLeftPixels.toString());
        Location topLeftLocation = proj.fromPointToLatLng(topLeftPixels, mp.getZoom());
        System.out.println("topLeftLocation: " + topLeftLocation.toString());
        Tile topLeftTile = new Tile(
                proj.tileXFromLongitude(topLeftLocation.mLongitude, mp.getZoom()),
                proj.tileYFromLatitude(topLeftLocation.mLatitude, mp.getZoom()),
                mp.getZoom());
        
        // Get the bottom right point.
        PointF bottomRightPixels = new PointF(mp.getWidth() + mp.getOffset().x,
                mp.getHeight() + mp.getOffset().y);
        System.out.println("bottomRightPixels: " + bottomRightPixels.toString());
        Location bottomRightLocation = proj.fromPointToLatLng(bottomRightPixels, mp.getZoom());
        System.out.println("bottomRightLocation: " + bottomRightLocation.toString());
        Tile bottomRightTile = new Tile(
                proj.tileXFromLongitude(bottomRightLocation.mLongitude, mp.getZoom()),
                proj.tileYFromLatitude(bottomRightLocation.mLatitude, mp.getZoom()),
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

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
package com.doubotis.staticmap.layers;

import com.doubotis.staticmap.StaticMap;
import com.doubotis.staticmap.geo.Location;
import com.doubotis.staticmap.geo.PointF;
import com.doubotis.staticmap.geo.Tile;
import com.doubotis.staticmap.geo.projection.MercatorProjection;
import com.doubotis.staticmap.layers.Layer;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author Christophe
 */
public abstract class TileLayer implements Layer
{
    
    private int mTileSize = 256;
    private float mOpacity = 1.0f;
    
    public void setOpacity(float opacity)
    {
        mOpacity = opacity;
    }
    
    /** Returns the opacity of the layer, between 0 and 1. */
    public float getOpacity()
    {
        return mOpacity;
    }
    
    public abstract Image getTile(int tileX, int tileY, int tileZ);

    @Override
    public void draw(Graphics2D graphics, StaticMap mp)
    {
        // Apply opacity
        float alpha = getOpacity();
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        graphics.setComposite(composite);
        
        MercatorProjection proj = mp.getProjection();
        int tileSize = proj.getTileSize();
        
        int tileX = tileXFromLongitude(mp.getLocation().getLongitude(), mp.getZoom());
        int tileY = tileYFromLatitude(mp.getLocation().getLatitude(), mp.getZoom());
        int tileZ = mp.getZoom();
        
        
        // Get the top left point.
        PointF topLeftPixels = new PointF(0 + mp.getOffset().x,
                0 + mp.getOffset().y);
        Location topLeftLocation = proj.project(topLeftPixels, mp.getZoom());
        Tile topLeftTile = new Tile(
                tileXFromLongitude(topLeftLocation.getLongitude(), mp.getZoom()),
                tileYFromLatitude(topLeftLocation.getLatitude(), mp.getZoom()),
                mp.getZoom());
        
        // Get the bottom right point.
        PointF bottomRightPixels = new PointF(mp.getWidth() + mp.getOffset().x,
                mp.getHeight() + mp.getOffset().y);
        Location bottomRightLocation = proj.project(bottomRightPixels, mp.getZoom());
        Tile bottomRightTile = new Tile(
                tileXFromLongitude(bottomRightLocation.getLongitude(), mp.getZoom()),
                tileYFromLatitude(bottomRightLocation.getLatitude(), mp.getZoom()),
                mp.getZoom());
        
        // Get the top left corner or the top left tile before looping.
        double topLeftCornerLat = latitudeFromTile(topLeftTile.y, mp.getZoom());
        double topLeftCornerLon = longitudeFromTile(topLeftTile.x, mp.getZoom());
        Location topLeftLoc = new Location(topLeftCornerLat, topLeftCornerLon);
        PointF topLeftCorner = proj.unproject(topLeftLoc, mp.getZoom());
        
        int i = 0,j = 0;
        for (int y = topLeftTile.y; y <= bottomRightTile.y; y++)
        {
            for (int x = topLeftTile.x; x <= bottomRightTile.x; x++)
            {
                // Get the tile.
                Image im = getTile(x, y, tileZ);
                
                // Get the "true" pos.
                PointF truePos = new PointF(topLeftCorner.x + (tileSize * i),
                        topLeftCorner.y + (tileSize * j));
                
                // Get the pos.
                PointF tilePos = new PointF(truePos.x - mp.getOffset().x ,
                    truePos.y - mp.getOffset().y);
                
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
        
        // Reset composite.
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        graphics.setComposite(composite);
    }
    
    // === Static methods ===
    
    public static double longitudeFromTile(int x, int z)
    {
            return (x/Math.pow(2,z)*360-180);
    }

    public static  double latitudeFromTile(int y, int z)
    {
        final double latRadians = StrictMath.PI - (2.0 * StrictMath.PI) * y / (1 << z);
        final double latitude = StrictMath.atan(StrictMath.exp(latRadians)) / StrictMath.PI * 360.0 - 90.0;
        return latitude;
    }

    public static int tileXFromLongitude(double lon, int z)
    {
        return ((int)Math.floor( (lon + 180) / 360 * (1<<z) ));
    }

    public static int tileYFromLatitude(double lat, int z)
    {
        final double alpha = Math.toRadians(lat);
        
        final int tileY = (int)StrictMath.floor( (float) ((1.0 - StrictMath.log((StrictMath.sin(alpha) + 1.0) / StrictMath.cos(alpha)) / StrictMath.PI) * 0.5 * (1 << z)));
        return tileY;
    }
}

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
import com.doubotis.staticmap.geo.LocationBounds;
import com.doubotis.staticmap.geo.MercatorProjection;
import com.doubotis.staticmap.geo.PointF;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author Christophe
 */
public class WMSMapType extends TMSMapType
{
    private boolean mIsPNG = true;
    private boolean mIsTransparent = true;
    private int mMinZoom = 4;
    protected int mMaxZoom = 11;
    protected float mOpacity = 1.0f;
    protected String mHost;
    protected String[] mLayers;
    protected String mFilter;
    private StaticMap mMapPicture;

    public WMSMapType(String host, String[] layers) {
        super(host);
        
        mHost = mPattern;
        mLayers = layers;
    }
    
    public void setLayers(String[] layers)
    {
        mLayers = layers;
    }

    @Override
    protected String buildURL(int tileX, int tileY, int tileZ) {
        
        MercatorProjection proj = mMapPicture.getProjection();
        
        String pattern = "";
        pattern += mHost;
        pattern += "?service=WMS&version=1.1.1&request=GetMap&Layers=";
        for (int i=0; i < mLayers.length; i++)
        {
            pattern += mLayers[i];
            if (i < mLayers.length - 1)
                pattern += ",";
        }
        
        // Compute locations corners.
        double lat = proj.latitudeFromTile(tileY, tileZ);
        double lon = proj.longitudeFromTile(tileX, tileZ);
        Location topLeftLocation = new Location(lat, lon);
        
        PointF topLeftCorner = proj.fromLatLngToPoint(lat, lon, tileZ);
        PointF bottomRightCorner = new PointF(topLeftCorner.x + proj.getTileSize(),
                topLeftCorner.y + proj.getTileSize());
        Location bottomRightLocation = proj.fromPointToLatLng(bottomRightCorner, tileZ);
        
        LocationBounds bounds = new LocationBounds(
                topLeftLocation.mLongitude,
                bottomRightLocation.mLongitude,
                topLeftLocation.mLatitude,
                bottomRightLocation.mLatitude);
            
        pattern += "&Styles=&SRS=EPSG:4326";
        pattern += "&BBOX=" + bounds.xmin + "," + bounds.ymax + "," + bounds.xmax + "," + bounds.ymin;
        pattern += "&width=" + proj.getTileSize();
        pattern += "&height=" + proj.getTileSize();
        pattern += (mIsPNG) ? "&format=image/png" : "&format=image/jpg";
        pattern += "&TRANSPARENT=" + ((mIsTransparent) ? "TRUE" : "FALSE");
        pattern += (mFilter == null) ? "" : "&cql_Filter=" + mFilter;
        
        return pattern;
        
    }
    
    @Override
    public void draw(Graphics2D graphics, StaticMap mp)
    {
        mMapPicture = mp;
        super.draw(graphics, mp);
        
    }
    
    
    
}

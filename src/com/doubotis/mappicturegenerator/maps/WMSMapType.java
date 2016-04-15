/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.maps;

import com.doubotis.mappicturegenerator.MapPicture;
import com.doubotis.mappicturegenerator.geo.Location;
import com.doubotis.mappicturegenerator.geo.LocationBounds;
import com.doubotis.mappicturegenerator.geo.MercatorProjection;
import com.doubotis.mappicturegenerator.geo.PointF;
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
    private MapPicture mMapPicture;

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
                topLeftLocation.longitude,
                bottomRightLocation.longitude,
                topLeftLocation.latitude,
                bottomRightLocation.latitude);
            
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
    public void draw(Graphics2D graphics, MapPicture mp)
    {
        mMapPicture = mp;
        super.draw(graphics, mp);
        
    }
    
    
    
}

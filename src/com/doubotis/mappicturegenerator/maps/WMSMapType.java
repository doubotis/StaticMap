/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.maps;

import com.doubotis.mappicturegenerator.MapPicture;
import com.doubotis.mappicturegenerator.geo.MercatorProjection;
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
        for (String layer : mLayers)
            pattern += layer + ",";
        pattern += "&Styles=&SRS=EPSG:4326&BBOX=0,61.60639637138628,11.25,66.51326044311188";
        pattern += "&width=" + 256;
        pattern += "&height=" + 256;
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

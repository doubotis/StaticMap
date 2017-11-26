/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.staticmap.layers.components;

import com.doubotis.staticmap.StaticMap;
import com.doubotis.staticmap.geo.Location;
import com.doubotis.staticmap.geo.LocationPath;
import com.doubotis.staticmap.geo.PointF;
import com.doubotis.staticmap.geo.projection.MercatorProjection;
import com.doubotis.staticmap.layers.Layer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Christophe
 */
public class LineString implements Layer
{
    private float mOpacity;
    private LocationPath mPath;
    private Color mStrokeColor = Color.RED;
    private Color mOutlineColor = Color.WHITE;
    private int mStrokeWidth = 2;
    private int mOutlineWidth = 2;
    private boolean mDrawShadows = false;
    
    public LineString(LocationPath path)
    {
        mPath = path;
    }
    
    public void opacity(float opacity)
    {
        mOpacity = opacity;
    }
    
    public float getOpacity()
    {
        return mOpacity;
    }
    
    public LocationPath getPath()
    {
        return mPath;
    }
    
    public LineString strokeColor(Color strokeColor)
    {
        mStrokeColor = strokeColor;
        return this;
    }
    
    public LineString strokeWidth(int width)
    {
        mStrokeWidth = width;
        return this;
    }
    
    public LineString outlineColor(Color outlineColor) {
        mOutlineColor = outlineColor;
        return this;
    }
    
    public LineString outlineWidth(int width)
    {
        mOutlineWidth = width;
        return this;
    }
    
    @Override
    public void draw(Graphics2D graphics, StaticMap mp)
    {
        MercatorProjection proj = mp.getProjection();
        
        int[] xPoints = new int[mPath.getSize()];
        int[] yPoints = new int[mPath.getSize()];
        
        for (int i=0; i < mPath.getSize(); i++)
        {
            Location l = mPath.getLocationAtIndex(i);
            
            PointF pixelsLocation = proj.unproject(l, mp.getZoom());
            xPoints[i] = (int)(pixelsLocation.x - mp.getOffset().x);
            yPoints[i] = (int)(pixelsLocation.y - mp.getOffset().y);
        }
        
        if (mOutlineWidth > 0)
        {
            // Draw Outline
            BasicStroke sOutline = new BasicStroke(mOutlineWidth + mStrokeWidth, 
                    BasicStroke.CAP_ROUND, 
                    BasicStroke.JOIN_ROUND);
            graphics.setColor(mOutlineColor);
            graphics.setStroke(sOutline);
            graphics.drawPolyline(xPoints, yPoints, mPath.getSize());
        }
        
        // Draw Center line
        BasicStroke sCenter = new BasicStroke(mStrokeWidth, 
                BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND);
        graphics.setColor(Color.RED);
        graphics.setStroke(sCenter);
        graphics.drawPolyline(xPoints, yPoints, mPath.getSize());
        
    }
    
}

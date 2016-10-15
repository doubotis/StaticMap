/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.layers;

import com.doubotis.mappicturegenerator.StaticMap;
import com.doubotis.mappicturegenerator.geo.Location;
import com.doubotis.mappicturegenerator.geo.LocationBounds;
import com.doubotis.mappicturegenerator.geo.LocationPath;
import com.doubotis.mappicturegenerator.geo.MercatorProjection;
import com.doubotis.mappicturegenerator.geo.PointF;
import com.doubotis.mappicturegenerator.geo.Trace;
import com.vividsolutions.jts.geom.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Christophe
 */
public class LocationPathLayer implements Layer
{
    private LocationPath mLocationPath = null;
    private Trace mTrace;
    
    public LocationPathLayer(Location[] points) {
        
        mLocationPath = new LocationPath();
        mTrace = new Trace();
        for(Location loc : points)
        {
            mTrace.getSegments().add(new Location(loc.latitude, loc.longitude));
        } 
        mLocationPath = mTrace.getPath();
    }
    
    public LocationPathLayer(Point[] points) {
        
        mLocationPath = new LocationPath();
        mTrace = new Trace();
        for(Point pt : points)
        {
            mTrace.getSegments().add(new Location(pt.getX(), pt.getY()));
        } 
        mLocationPath = mTrace.getPath();
    }
    
    public LocationBounds getBounds()
    {
        return mTrace.getBBOX();
    }
    
    public Location getCenter() {
        return mTrace.getCentroid();
    }

    @Override
    public void draw(Graphics2D graphics, StaticMap mp) {
        
        MercatorProjection proj = mp.getProjection();
        int tileSize = proj.getTileSize();
        LocationPath locationPath = getPath();
        
        int[] xPoints = new int[locationPath.getSize()];
        int[] yPoints = new int[locationPath.getSize()];
        
        for (int i=0; i < locationPath.getSize(); i++)
        {
            Location l = locationPath.getLocationAtIndex(i);
            
            PointF pixelsLocation = proj.fromLatLngToPoint(l.latitude, l.longitude, mp.getZoom());
            xPoints[i] = (int)(pixelsLocation.x - mp.getOffset().x);
            yPoints[i] = (int)(pixelsLocation.y - mp.getOffset().y);
        }
        
        // Draw Outline
        BasicStroke sOutline = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        graphics.setColor(Color.WHITE);
        graphics.setStroke(sOutline);
        graphics.drawPolyline(xPoints, yPoints, locationPath.getSize());
        
        // Draw Center line
        BasicStroke sCenter = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        graphics.setColor(Color.RED);
        graphics.setStroke(sCenter);
        graphics.drawPolyline(xPoints, yPoints, locationPath.getSize());
        
    }

    public LocationPath getPath() {
        return mLocationPath;
    }
    
}

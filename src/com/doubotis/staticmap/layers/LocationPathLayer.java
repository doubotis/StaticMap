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
import com.doubotis.staticmap.geo.LocationBounds;
import com.doubotis.staticmap.geo.LocationPath;
import com.doubotis.staticmap.geo.MercatorProjection;
import com.doubotis.staticmap.geo.PointF;
import com.doubotis.staticmap.geo.Trace;
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
            mTrace.getSegments().add(new Location(loc.mLatitude, loc.mLongitude));
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
            
            PointF pixelsLocation = proj.fromLatLngToPoint(l.mLatitude, l.mLongitude, mp.getZoom());
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

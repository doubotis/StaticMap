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
package com.doubotis.staticmap.geo;

/**
 *
 * @author Christophe
 */
public final class MercatorProjection 
{
    private int _tileSize;
    private PointF _pixelOrigin;
    private double _pixelsPerLonDegree;
    private double _pixelsPerLonRadian;
	
	public MercatorProjection()
    {
        // Default is 256x256.
        this(256);
    }

    public MercatorProjection(int tileSize)
    {
        this._tileSize = tileSize;
        this._pixelOrigin = new PointF(this._tileSize / 2.0,this._tileSize / 2.0);
        this._pixelsPerLonDegree = this._tileSize / 360.0;
        this._pixelsPerLonRadian = this._tileSize / (2 * Math.PI);
    }
    
    public int getTileSize()
    {
        return _tileSize;
    }

    double bound(double val, double valMin, double valMax)
    {
        double res;
        res = Math.max(val, valMin);
        res = Math.min(val, valMax);
        return res;
    }

    double degreesToRadians(double deg) 
    {
        return deg * (Math.PI / 180);
    }

    double radiansToDegrees(double rad) 
    {
        return rad / (Math.PI / 180);
    }

    public PointF fromLatLngToPoint(double lat, double lng, int zoom)
    {
        PointF point = new PointF(0, 0);

        point.x = _pixelOrigin.x + lng * _pixelsPerLonDegree;       

        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
        // about a third of a tile past the edge of the world tile.
        double siny = bound(Math.sin(degreesToRadians(lat)), -0.9999,0.9999);
        point.y = _pixelOrigin.y + 0.5 * Math.log((1 + siny) / (1 - siny)) *- _pixelsPerLonRadian;

        int numTiles = 1 << zoom;
        point.x = point.x * numTiles;
        point.y = point.y * numTiles;
        return point;
     }

    public Location fromPointToLatLng(PointF pt, int zoom)
    {
        PointF point = new PointF(pt.x, pt.y);
        int numTiles = 1 << zoom;
        point.x = point.x / numTiles;
        point.y = point.y / numTiles;       

        double lng = (point.x - _pixelOrigin.x) / _pixelsPerLonDegree;
        double latRadians = (point.y - _pixelOrigin.y) / - _pixelsPerLonRadian;
        double lat = radiansToDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
        return new Location(lat, lng);
    }
    
    public double longitudeFromTile(int x, int z)
    {
            return (x/Math.pow(2,z)*360-180);
    }

    public double latitudeFromTile(int y, int z)
    {
            double n=Math.PI-2*Math.PI*y/Math.pow(2,z);
            return (180/Math.PI*Math.atan(0.5*(Math.exp(n)-Math.exp(-n))));
    }

    public static int tileXFromLongitude(double lon, int z)
    {
            return ((int)Math.floor( (lon + 180) / 360 * (1<<z) ));
    }

    public static int tileYFromLatitude(double lat, int z)
    {
            return ((int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<z) ));
    }

}


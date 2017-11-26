/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.staticmap.geo.projection;

import com.doubotis.staticmap.geo.Location;
import com.doubotis.staticmap.geo.PointF;

/**
 *
 * @author cbrasseur
 */
public final class MercatorProjection implements GeographicalProjection<PointF>
{
    private static final int DEFAULT_TILE_SIZE = 256;
    
    private int _tileSize;
    private PointF _pixelOrigin;
    private double _pixelsPerLonDegree;
    private double _pixelsPerLonRadian;
    
    public MercatorProjection()
    {
        this._tileSize = DEFAULT_TILE_SIZE;
        this._pixelOrigin = new PointF(this._tileSize / 2.0,this._tileSize / 2.0);
        this._pixelsPerLonDegree = this._tileSize / 360.0;
        this._pixelsPerLonRadian = this._tileSize / (2 * Math.PI);
    }
    
    @Override
    public PointF unproject(Location location, int zoom)
    {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        
        PointF point = new PointF(0, 0);

        point.x = _pixelOrigin.x + lng * _pixelsPerLonDegree;

        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
        // about a third of a tile past the edge of the world tile.
        double siny = bound(Math.sin(Math.toRadians(lat)), -0.9999,0.9999);
        point.y = _pixelOrigin.y + 0.5 * Math.log((1 + siny) / (1 - siny)) *- _pixelsPerLonRadian;

        int numTiles = 1 << zoom;
        point.x = point.x * numTiles;
        point.y = point.y * numTiles;
        return point;
    }
    
    @Override
    public Location project(PointF pt, int zoom)
    {
        PointF point = new PointF(pt.x, pt.y);
        int numTiles = 1 << zoom;
        point.x = point.x / (double)numTiles;
        point.y = point.y / (double)numTiles;

        double lng = (point.x - _pixelOrigin.x) / _pixelsPerLonDegree;
        
        double latRadians = (point.y - _pixelOrigin.y) / - _pixelsPerLonRadian;
        double lat = Math.toDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
        return new Location(lat, lng);
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

}

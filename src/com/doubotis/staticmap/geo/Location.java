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
public class Location
{
    
    public double mLatitude;
    public double mLongitude;

    public static float distanceBetween(double lat1, double lon1, double lat2, double lon2/*, char unit*/)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(MathUtils.deg2rad(lat1)) * Math.sin(MathUtils.deg2rad(lat2)) + Math.cos(MathUtils.deg2rad(lat1)) * Math.cos(MathUtils.deg2rad(lat2)) * Math.cos(MathUtils.deg2rad(theta));
        dist = Math.acos(dist);
        dist = MathUtils.rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        /*if (unit == 'K') {
            dist = dist * 1.609344;
        }
        else if (unit == 'N') {
            dist = dist * 0.8684;
        }*/

        float floatdist = (float) dist;

        if(Float.isNaN(floatdist) || Float.isInfinite(floatdist))
        {
            //Log.i("GeoUtils", "Result of distanceBetween NaN or infinite for lat1="+lat1+ " lon1="+lon1+" lat2="+lat2 + " lon2="+lon2);
            return 0;
        }
        else
        {
            return floatdist;
        }
    }
    
    public Location(double lat, double lon)
    {
        this.mLatitude = lat;
        this.mLongitude = lon;
    }

    public Location(String geolives) {
        
    }
    
    public void setLatitude(double lat) {
        this.mLatitude = lat;
    }
    
    public void setLongitude(double lon) {
        this.mLongitude = lon;
    }
    
    public void setAltitude(double alt) {
        
    }
    
    public double getLatitude() {
        return this.mLatitude;
    }
    
    public double getLongitude() {
        return this.mLongitude;
    }
    
    public double getAltitude() {
        return 0;
    }

    @Override
    public String toString() {
        return "{" + this.mLatitude + "," + this.mLongitude + "}";
    }
    
    
    
    
    
}

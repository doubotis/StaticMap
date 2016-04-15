/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.geo;

/**
 *
 * @author Christophe
 */
public class Location
{
    
    public double latitude;
    public double longitude;

    public static float distanceBetween(double lat1, double lon1, double lat2, double lon2/*, char unit*/)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
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
    
    private static final double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }
	 
    private static final double rad2deg(double rad)
    {
        return (rad * 180 / Math.PI);
    }
    
    public Location(double lat, double lon)
    {
        this.latitude = lat;
        this.longitude = lon;
    }

    public Location(String geolives) {
        
    }
    
    public void setLatitude(double lat) {
        this.latitude = lat;
    }
    
    public void setLongitude(double lon) {
        this.longitude = lon;
    }
    
    public void setAltitude(double alt) {
        
    }
    
    public double getLatitude() {
        return this.latitude;
    }
    
    public double getLongitude() {
        return this.longitude;
    }
    
    public double getAltitude() {
        return 0;
    }

    @Override
    public String toString() {
        return "{" + this.latitude + "," + this.longitude + "}";
    }
    
    
    
    
    
}

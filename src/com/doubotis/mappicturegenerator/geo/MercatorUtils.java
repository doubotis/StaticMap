package com.doubotis.mappicturegenerator.geo;

public class MercatorUtils
{
    public static double[] googleCoordinatesToLatLng(int[] pixel)
    {
    	double originShift = 2 * Math.PI * 6378137 / 2.0;
        double lat = (pixel[1] / originShift) * 180.0;
        lat = 180 / Math.PI * (2 * Math.atan( Math.exp( lat * Math.PI / 180.0)) - Math.PI / 2.0);
        double lon = (pixel[0] / originShift) * 180.0;
        return new double[] {lat, lon};
    }
	
    private static double bound(double value, double opt_min, double opt_max)
    {
        value = Math.max(value, opt_min);
        value = Math.min(value, opt_max);
        return value;
    }

    
}

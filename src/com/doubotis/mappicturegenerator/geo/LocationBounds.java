/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.geo;

public class LocationBounds {
	
	public double xmax;
	public double xmin;
	public double ymax;
	public double ymin;
	
	public LocationBounds(double xmi, double xma, double ymi, double yma)
	{
		xmax = xma;
		xmin = xmi;
		ymax = yma;
		ymin = ymi;
	}
	
	public Location getCenter()
	{
		double xmiddle = xmin + ((xmax - xmin)/2);
		double ymiddle = ymin + ((ymax - ymin)/2);
		
		return new Location(ymiddle, xmiddle);
	}
	
	public static LocationBounds parseBBOX(String string)
	{
		try
		{
			String[] parsed = string.split(",");
			double xmin = Double.parseDouble(parsed[0]);
			double ymin = Double.parseDouble(parsed[1]);
			double xmax = Double.parseDouble(parsed[2]);
			double ymax = Double.parseDouble(parsed[3]);
			LocationBounds bbox = new LocationBounds(xmin, xmax, ymin, ymax);
			
			return bbox;
		} catch (NumberFormatException pee) { return null; }
	}

	@Override
	public String toString()
	{
		return xmin + "," + ymin + "," + xmax + "," + ymax;
	}
	
	/** Traduit ce BBOX en Well-Known-Text, afin d'?tre compris par une base Spatialite
	 * par exemple.
	 * @return WKT de retour.
	 */
	public String BBOXToWKT()
	{
            String geom = "POLYGON((";
            geom += xmin + " " + ymin + ",";		// Point haut-gauche
            geom += xmin + " " + ymax + ",";		// Point bas-gauche
            geom += xmax + " " + ymax + ",";		// Point bas-droite
            geom += xmax + " " + ymin + ",";		// Point haut-droite
            geom += xmin + " " + ymin;
            geom += "))";
            return geom;
	}
	
	/** Compute the area of the BBOX and returns a value in kilometers. */
	public double getAreaKm2()
	{
            double maxlon = xmax;
            double minlon = xmin;
            double maxlat = ymax;
            double minlat = ymin;

            double bboxwidth = Location.distanceBetween(minlat, minlon,
                                    minlat, maxlon) / 1000;
            double bboxheight = Location.distanceBetween(minlat, minlon,
                                    maxlat, minlon) / 1000;
            double bboxarea = bboxwidth * bboxheight;

            return bboxarea;
	}
	
	/** Returns true if the <code>GLatLng</code> is contained inside this BBOX. False otherwise. */
	public boolean contains(Location latLng)
	{
		double y = latLng.latitude;
		double x = latLng.longitude;
                
		if (y > ymin && y < ymax && x > xmin && x < xmax)
			return true;
		
		return false;
	}
	
	/** Returns true if the <code>BBOX</code> inside this BBOX. False otherwise.
	 * @param inclusive Flag indicates that true must be returned only if the parameter BBOX is fully contained
	 * into this BBOX.
	 */
	public boolean contains(LocationBounds bbox, boolean inclusive)
	{
		int count = 0;
		
		if (bbox.xmin > xmin && bbox.xmin < xmax)
			count++;
		
		if (bbox.xmax > xmin && bbox.xmax < xmax)
			count++;
		
		if (bbox.ymin > ymax && bbox.ymin < ymin)
			count++;
		
		if (bbox.ymax > ymax && bbox.ymax < ymin)
			count++;
		
		if (inclusive)
			return (count == 4);
		else
			return (count > 0);
	}

	/** Creates a BBOX that wraps a specified LatLng, with an area in meters around the point.
	 * @distance The distance, in meters */
	public static LocationBounds enveloppe(Location latLng, long distance) {

		LocationBounds bbox = new LocationBounds(0,0,0,0);

		double[] llcenter = new double[] {latLng.latitude, latLng.longitude};
		double latitude = llcenter[0];
		double longitude = llcenter[1];
		double[] location1 = new double[] { latitude, longitude - 0.5 };
		double[] location2 = new double[] { latitude, longitude + 0.5 };
		double mpdlon = Location.distanceBetween(location1[0], location1[1],
				location2[0], location2[1]);
		bbox.xmin = longitude - ((distance * 1f) / mpdlon);
		bbox.xmax = longitude + ((distance * 1f) / mpdlon);

		location1 = new double[] { latitude - 0.5, longitude };
		location2 = new double[] { latitude + 0.5, longitude };

		double mpdlat = Location.distanceBetween(location1[0], location1[1],
				location2[0], location2[1]);
		bbox.ymin = latitude - ((distance * 1f) / mpdlat);
		bbox.ymax = latitude + ((distance * 1f) / mpdlat);

		return bbox;
	}

	public Location getLowerLeftCorner()
	{
            return new Location(ymax, xmin);
	}

	public Location getUpperLeftCorner()
	{
            return new Location(ymin, xmin);
	}

	public Location getUpperRightCorner()
	{
            return new Location(ymin, xmax);
	}

	public Location getLowerRightCorner()
	{
            return new Location(ymax, xmax);
	}

}

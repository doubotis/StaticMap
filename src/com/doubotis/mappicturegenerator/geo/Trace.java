package com.doubotis.mappicturegenerator.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

import java.util.ArrayList;

/**
 * Created by cbrasseur on 16/02/16.
 */
public class Trace
{
    private ArrayList<Location> mLocations = new ArrayList<Location>();

    public Trace()
    {

    }

    public static Trace fromWKT(String wkt)
    {
        try {
            WKTReader reader = new WKTReader();
            Geometry geom = reader.read(wkt);
            LineString ls = (LineString) geom;
            Coordinate[] coords = ls.getCoordinates();

            Trace trace = new Trace();
            for (int i = 0; i < coords.length; i++) {
                Location l = new Location("Geolives");
                l.setLongitude(coords[i].x);
                l.setLatitude(coords[i].y);
                l.setAltitude(coords[i].z);
                trace.getSegments().add(l);
            }

            return trace;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Location> getSegments()
    {
        return mLocations;
    }

    public LocationPath getPath()
    {
        LocationPath path = new LocationPath();
        for (int i=0; i < getSegments().size(); i++)
        {
            Location l = getSegments().get(i);
            path.addLocation(new Location(l.getLatitude(), l.getLongitude()));
        }
        return path;
    }

    public LocationBounds getBBOX()
    {
        Coordinate[] coords = new Coordinate[mLocations.size()];
        for (int i=0; i < coords.length; i++)
        {
            coords[i] = new Coordinate(mLocations.get(i).getLongitude(), mLocations.get(i).getLatitude(), mLocations.get(i).getAltitude());
        }

        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(coords);

        Polygon poly = (Polygon) ls.getEnvelope();
        Coordinate topLeft = poly.getCoordinates()[0];
        Coordinate topRight = poly.getCoordinates()[1];
        Coordinate bottomRight = poly.getCoordinates()[2];
        Coordinate bottomLeft = poly.getCoordinates()[3];

        LocationBounds bbox = new LocationBounds(topLeft.x, topRight.x, topLeft.y, bottomLeft.y);
        return bbox;
    }
    
    public Location getCentroid()
    {
        Coordinate[] coords = new Coordinate[mLocations.size()];
        for (int i=0; i < coords.length; i++)
        {
            coords[i] = new Coordinate(mLocations.get(i).getLongitude(), mLocations.get(i).getLatitude(), mLocations.get(i).getAltitude());
        }

        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(coords);
        
        Point pt = ls.getCentroid();
        return new Location(pt.getY(), pt.getX());
    }

    public String toWKT() {

        Coordinate[] coords = new Coordinate[mLocations.size()];
        for (int i=0; i < coords.length; i++)
        {
            coords[i] = new Coordinate(mLocations.get(i).getLongitude(), mLocations.get(i).getLatitude(), mLocations.get(i).getAltitude());
        }

        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(coords);

        WKTWriter writer = new WKTWriter();
        return writer.write(ls);
    }
}

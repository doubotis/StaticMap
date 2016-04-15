/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import com.doubotis.mappicturegenerator.geo.LocationBounds;
import com.doubotis.mappicturegenerator.geo.Location;
import com.doubotis.mappicturegenerator.geo.LocationPath;
import com.doubotis.mappicturegenerator.geo.MercatorProjection;
import com.doubotis.mappicturegenerator.geo.MercatorUtils;
import com.doubotis.mappicturegenerator.geo.PointF;
import com.doubotis.mappicturegenerator.geo.Tile;
import com.doubotis.mappicturegenerator.layers.LocationPathLayer;
import com.doubotis.mappicturegenerator.layers.Layer;
import com.doubotis.mappicturegenerator.maps.TMSMapType;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author Christophe
 */
public class MapPicture {
    
    private static final int DEFAULT_TILE_SIZE = 256;
    
    private Location mLocation;
    private int mZoom = 3;
    
    private int mWidth;
    private int mHeight;
    private BufferedImage mImage = null;
    private MercatorProjection mProjection = new MercatorProjection(DEFAULT_TILE_SIZE);
    private ArrayList<Layer> mLayers = new ArrayList<Layer>();
    private PointF mOffset;
    
    public MapPicture(int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
    
    public void setLocation(double lat, double lon)
    {
        mLocation = new Location(lat, lon);
    }
    
    public void setLocation(Location l)
    {
        mLocation = l;
    }
    
    public Location getLocation()
    {
        return mLocation;
    }
    
    public void setZoom(int zoom)
    {
        mZoom = zoom;
    }
    
    public int getZoom()
    {
        return mZoom;
    }
    
    public void setWidth(int width)
    {
        mWidth = width;
    }
    
    public void setHeight(int height)
    {
        mHeight = height;
    }
    
    public void setSize(int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
    
    public int getWidth()
    {
        return mWidth;
    }
    
    public int getHeight()
    {
        return mHeight;
    }
    
    public void setProjection(MercatorProjection projection)
    {
        mProjection = projection;
    }
    
    public MercatorProjection getProjection()
    {
        return mProjection;
    }
    
    public PointF getOffset()
    {
        return mOffset;
    }
    
    private void prepare(Graphics2D graphics)
    {
        mOffset = computeRatioPixels(getZoom());
        
    }
    
    private void proceedDraw()
    {
        mImage = new BufferedImage(mWidth, mHeight, 
                    BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = mImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        graphics.setBackground(Color.WHITE);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, mWidth, mHeight);
        
        prepare(graphics);
        
        for (Layer layer : mLayers)
            layer.draw(graphics, this);
    }
    
    public void drawInto(File file) throws IOException
    {
        proceedDraw();
        ImageIO.write(mImage, "PNG", file);
    }
    
    public void drawInto(OutputStream os) throws IOException
    {
        proceedDraw();
        ImageIO.write(mImage, "PNG", os);
    }

    public void addLayer(Layer layer) {
        mLayers.add(layer);
    }
    
    public void insertLayer(Layer layer, int index) {
        mLayers.add(index, layer);
    }
    
    public void removeLayer(Layer layer) {
        mLayers.remove(layer);
    }

    public void fitBounds(LocationBounds bounds, int minZoom, int maxZoom) {
        
        // Find which zoom value to set.
        setLocation(bounds.getCenter());
        
        MercatorProjection mp = getProjection();
        
        System.out.println("To fit: " + bounds.toString());
        
        int baseZoom = maxZoom;
        boolean inBounds = false;
        while (inBounds == false)
        {
            baseZoom--;
            
            if (baseZoom < minZoom) {
                baseZoom = minZoom;
                break;
            }
            
            PointF rp = computeRatioPixels(baseZoom);
            
            // Compute?
            PointF topLeftPixels = new PointF(0 + rp.x,
                0 + rp.y);
            Location topLeftLocation = mp.fromPointToLatLng(topLeftPixels, baseZoom);
            
            PointF bottomRightPixels = new PointF(mWidth + rp.x,
                mHeight + rp.y);
            Location bottomRightLocation = mp.fromPointToLatLng(bottomRightPixels, baseZoom);
            
            // Test if in bounds
            LocationBounds bboxCalculation = new LocationBounds(topLeftLocation.longitude, bottomRightLocation.longitude, topLeftLocation.latitude, bottomRightLocation.latitude);
            
            System.out.println("Trying with " + baseZoom + "...");
            System.out.println(" - " + bboxCalculation.toString());
            inBounds = bboxCalculation.contains(bounds, true);
        }
        
        mZoom = baseZoom;
        
    }
    
    private PointF computeRatioPixels(int zoom)
    {
        MercatorProjection proj = getProjection();
        int tileSize = proj.getTileSize();
        
        int tileX = proj.tileXFromLongitude(getLocation().longitude, zoom);
        int tileY = proj.tileYFromLatitude(getLocation().latitude, zoom);
        int tileZ = mZoom;
        
        // Which position for this tile ?
        double tileLat = proj.latitudeFromTile(tileY, tileZ);
        double tileLon = proj.longitudeFromTile(tileX, tileZ);
        
        System.out.println("tileLat: " + tileLat);
        System.out.println("tileLon: " + tileLon);
        
        PointF tilePixels = proj.fromLatLngToPoint(tileLat, tileLon, tileZ);
        System.out.println("tilePixels" + tilePixels.toString());
        
        PointF centerPixels = proj.fromLatLngToPoint(getLocation().latitude, getLocation().longitude, zoom);
        System.out.println("centerPixels" + centerPixels.toString());
        
        // Le centre en 824, 539 est l'équivalent de 100,100 sur l'image.
        int centerImageX = mWidth / 2;
        int centerImageY = mHeight / 2;
        
        PointF pixels = new PointF(centerPixels.x - centerImageX, 
                centerPixels.y - centerImageY);
        
        return pixels;
    }

    public void fitBounds(LocationBounds bounds) {
        fitBounds(bounds, 3, 20);
    }
    
}

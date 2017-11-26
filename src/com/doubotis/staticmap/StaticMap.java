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
package com.doubotis.staticmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import com.doubotis.staticmap.geo.LocationBounds;
import com.doubotis.staticmap.geo.Location;
import com.doubotis.staticmap.geo.LocationPath;
import com.doubotis.staticmap.geo.PointF;
import com.doubotis.staticmap.geo.Tile;
import com.doubotis.staticmap.geo.projection.MercatorProjection;
import com.doubotis.staticmap.layers.Layer;
import com.doubotis.staticmap.layers.TMSLayer;
import com.doubotis.staticmap.layers.TileLayer;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/** Core class of the StaticMAp library. Serves the final results. */
public class StaticMap
{    
    private static final int DEFAULT_TILE_SIZE = 256;
    
    private Location mLocation;
    private int mZoom = 3;
    
    private int mWidth;
    private int mHeight;
    private BufferedImage mImage = null;
    private MercatorProjection mProjection = new MercatorProjection();
    private ArrayList<Layer> mLayers = new ArrayList<Layer>();
    private PointF mOffset;
    
    /** Build a static map with the specified width and height. In pixels. */
    public StaticMap(int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
    
    /** Sets the location for this map. */
    public void setLocation(double lat, double lon)
    {
        mLocation = new Location(lat, lon);
    }
    
    /** Sets the location for this map. */
    public void setLocation(Location l)
    {
        mLocation = l;
    }
    
    /** Returns the current location for this map. */
    public Location getLocation()
    {
        return mLocation;
    }
    
    /** Sets the zoom level for this map. */
    public void setZoom(int zoom)
    {
        mZoom = zoom;
    }
    
    /** Returns the current zoom level for this map. */
    public int getZoom()
    {
        return mZoom;
    }
    
    /** Sets the width of this map. In pixels. */
    public void setWidth(int width)
    {
        mWidth = width;
    }
    
    /** Sets the height of this map. In pixels. */
    public void setHeight(int height)
    {
        mHeight = height;
    }
    
    /** Sets the size of this map. In pixels. */
    public void setSize(int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
    
    /** Returns the width of this map, in pixels. */
    public int getWidth()
    {
        return mWidth;
    }
    
    /** Returns the height of this map, in pixels. */
    public int getHeight()
    {
        return mHeight;
    }
    
    /** Convert WGS84 coordinates to point on the map. */
    public PointF fromLatLngToPoint(double lat, double lng, int zoom)
    {
        final PointF offset = getOffset();
        
        Location loc = new Location(lat, lng);
        PointF pt = getProjection().unproject(loc, zoom);
        
        PointF offsetDone = new PointF(pt.x - offset.x, pt.y - offset.y);
        return offsetDone;
    }

    /** convert point on the map to WGS84 coordinates. */
    public Location fromPointToLatLng(PointF pt, int zoom)
    {
        final PointF offset = getOffset();
        
        // Offset the point for computation.
        final PointF thePoint = new PointF(pt.x + offset.x, pt.y + offset.y);
        
        Location result = getProjection().project(thePoint, zoom);
        return result;
    }
    
    /** Sets a custom projection. A projection is used to compute relations
     * between real locations and positions on the final picture.
     * See {@link MercatorProjection}.
     */
    public void setProjection(MercatorProjection projection)
    {
        mProjection = projection;
    }
    
    /** Returns the current projection. A projection is used to compute relations
     * between real locations and positions on the final picture.
     * See {@link MercatorProjection}.
     */
    public MercatorProjection getProjection()
    {
        return mProjection;
    }
    
    /** Gets the offset between the values returned by the {@link MercatorProjection}
     * and the position on the final picture, depending on the size.
     */
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
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        graphics.setBackground(Color.WHITE);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, mWidth, mHeight);
        
        prepare(graphics);
        
        
        
        for (Layer layer : mLayers) {
            layer.draw(graphics, this);
        }
    }
    
    /** Runs the procedure of drawing. Stores the result into the specified {@link File}. */
    public void drawInto(File file) throws IOException
    {
        proceedDraw();
        ImageIO.write(mImage, "PNG", file);
    }
    
    /** Runs the procedure of drawing. Stores the result into the specified {@link OutputStream}. */
    public void drawInto(OutputStream os) throws IOException
    {
        proceedDraw();
        ImageIO.write(mImage, "PNG", os);
    }

    /** Adds a {@link Layer} onto the map. The layer will be drawn from the first to the last.
     * For instance, you can add any {@link BaseMapType}, {@link TMSLayer} or {@link WMSMapType}
     * object.
     */
    public void addLayer(Layer layer) {
        mLayers.add(layer);
    }
    
    /** Inserts a {@link Layer} onto the map at the specified index. The layer will be drawn from the first to the last.
     * For instance, you can add any {@link BaseMapType}, {@link TMSLayer} or {@link WMSMapType}
     * object.
     */
    public void insertLayer(Layer layer, int index) {
        mLayers.add(index, layer);
    }
    
    /** Removes the spceified {@link Layer} from the map. The removed layer will
     * not been drawn anymore.
     */
    public void removeLayer(Layer layer) {
        mLayers.remove(layer);
    }
    
    /** Tell the map to fit the bounds of a {@link LocationBounds}. This method will
     * sets location and zoom level depending on the size of the final picture
     * and the specified bounds.
     */
    public void fitBounds(LocationBounds bounds) {
        fitBoundsPadding(bounds, 3, 20, 0);
    }

    /** Tell the map to fit the bounds of a {@link LocationBounds}. This method will
     * sets location and zoom level depending on the size of the final picture
     * and the specified bounds.<br/>
     * You can specify a minimum and maximum zoom.
     */
    public void fitBounds(LocationBounds bounds, int minZoom, int maxZoom) {
        fitBoundsPadding(bounds, minZoom, maxZoom, 0);
    }
    
    /** Tell the map to fit the bounds of a {@link LocationBounds}. This method will
     * sets location and zoom level depending on the size of the final picture
     * and the specified bounds.<br/>
     * You can specify a padding to apply to the map.
     * This doesn't means the padding will be exactly matching, but while
     * computing the right zoom, the padding will be taken in account in order to
     * not allowing space in each side, lower than this padding.
     */
    public void fitBoundsPadding(LocationBounds bounds, int padding) {
        fitBoundsPadding(bounds, 3, 20, padding);
    }

    /** Tell the map to fit the bounds of a {@link LocationBounds}. This method will
     * sets location and zoom level depending on the size of the final picture
     * and the specified bounds.<br/>
     * You can specify a padding to apply to the map.
     * This doesn't means the padding will be exactly matching, but while
     * computing the right zoom, the padding will be taken in account in order to
     * not allowing space in each side, lower than this padding.<br/>
     * You can specify a minimum and maximum zoom.
     */
    public void fitBoundsPadding(LocationBounds bounds, int minZoom, int maxZoom, int padding) {
        
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
            Location topLeftLocation = mp.project(topLeftPixels, baseZoom);
            
            PointF bottomRightPixels = new PointF(mWidth + rp.x,
                mHeight + rp.y);
            Location bottomRightLocation = mp.project(bottomRightPixels, baseZoom);
            
            // Test if in bounds
            LocationBounds bboxCalculation = new LocationBounds(topLeftLocation.mLongitude, bottomRightLocation.mLongitude, topLeftLocation.mLatitude, bottomRightLocation.mLatitude);
            
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
        
        int tileX = TileLayer.tileXFromLongitude(getLocation().mLongitude, zoom);
        int tileY = TileLayer.tileYFromLatitude(getLocation().mLatitude, zoom);
        int tileZ = mZoom;
        
        // Which position for this tile ?
        double tileLat = TileLayer.latitudeFromTile(tileY, tileZ);
        double tileLon = TileLayer.longitudeFromTile(tileX, tileZ);
        
        Location tileLoc = new Location(tileLat, tileLon);
        PointF tilePixels = proj.unproject(tileLoc, tileZ);
        
        PointF centerPixels = proj.unproject(getLocation(), zoom);
        
        // Le centre en 824, 539 est l'équivalent de 100,100 sur l'image.
        int centerImageX = mWidth / 2;
        int centerImageY = mHeight / 2;
        
        PointF pixels = new PointF(centerPixels.x - centerImageX, 
                centerPixels.y - centerImageY);
        
        return pixels;
    }
    
}

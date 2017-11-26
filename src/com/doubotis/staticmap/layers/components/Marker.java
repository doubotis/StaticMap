/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.staticmap.layers.components;

import com.doubotis.staticmap.StaticMap;
import com.doubotis.staticmap.geo.Location;
import com.doubotis.staticmap.geo.PointF;
import com.doubotis.staticmap.layers.Layer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Christophe
 */
public class Marker implements Layer
{
    private float mOpacity;
    private Image mImage;
    private Location mLocation;
    private PointF mAnchor;
    private float mRotation;
    
    public Marker(Location location) 
    {
        mLocation = location;
    }
    
    public void opacity(float opacity)
    {
        mOpacity = opacity;
    }
    
    public float getOpacity()
    {
        return mOpacity;
    }
    
    public Location getLocation()
    {
        return mLocation;
    }
    
    public Marker image(Image image)
    {
        mImage = image;
        return this;
    }
    
    public Marker rotation(float rotation)
    {
        mRotation = rotation;
        return this;
    }
    
    public Marker anchor(PointF anchor)
    {
        mAnchor = anchor;
        return this;
    }

    @Override
    public void draw(Graphics2D graphics, StaticMap mp)
    {
        int width = mImage.getWidth(null);
        int height = mImage.getHeight(null);
        
        System.out.println("width: " + width + ", height: " + height);
        
        PointF base = mp.getProjection().unproject(mLocation, mp.getZoom());
        PointF origin = new PointF(base.x - mp.getOffset().x, base.y - mp.getOffset().y);
        
        BufferedImage rotated = getRotatedImage(mRotation, mImage);
        
        int anchorX = (int)(mAnchor.x);
        int anchorY = (int)(mAnchor.y);
        
        graphics.drawImage(rotated, 
                (int)(origin.x - anchorX),
                (int)(origin.y - anchorY),
                width, 
                height,
                null);
    }
    
    private BufferedImage getRotatedImage(float angle, Image source) {
        
        // Get source image size.
        int width = source.getWidth(null);
        int height = source.getHeight(null);
        
        // Get size of rotated rectangle.
        int newWidth = width*2;
        int newHeight = height*2;
        
        AffineTransform at = new AffineTransform();
        
        int middleX = (width / 2)/* - width / 2*/;
        int middleY = (height / 2)/* - height / 2*/;

        // 4. translate it to the center of the component
        at.translate(middleX, middleY);

        // 3. do the actual rotation
        at.rotate(Math.toRadians(angle));

        // 1. translate the object so that you rotate it around the 
        //    center (easier :))
        at.translate(-middleX, -middleY);

        // draw the image
        BufferedImage rotated = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.drawImage(source, at, null);
        
        return rotated;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.maps;

import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Christophe
 */
public class TMSMapType extends BaseMapType
{
    private static final String[] SUBDOMAINS = new String[]{"a","b","c"};
    
    protected String mPattern;

    public TMSMapType(String pattern)
    {
        mPattern = pattern;
    }

    @Override
    public Image getTile(int tileX, int tileY, int tileZ) {
        
        try
        {
            String buildedUrl = buildURL(tileX, tileY, tileZ);
            System.out.println("Retreive tile from URL " + buildedUrl);
            URL url = new URL(buildedUrl);
            Image image = ImageIO.read(url);
            return image;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    protected String buildURL(int tileX, int tileY, int tileZ)
    {
        String pattern = mPattern;
        int subDomainRandom = (int)(Math.random() * SUBDOMAINS.length); 
        pattern = pattern.replace("{s}", SUBDOMAINS[subDomainRandom]);
        pattern = pattern.replace("{x}", "" + tileX);
        pattern = pattern.replace("{y}", "" + tileY);
        pattern = pattern.replace("{z}", "" + tileZ);
        
        return pattern;
    }
    
}

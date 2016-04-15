/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.geo;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Christophe
 */
public class LocationPath {
    
    private ArrayList<Location> mPath = new ArrayList<Location>();
    
    public LocationPath()
    {
        
    }
    
    public int getSize()
    {
        return mPath.size();
    }
    
    public void addLocation(Location l)
    {
        mPath.add(l);
    }
    
    public void removeLocation(Location l)
    {
        mPath.remove(l);
    }
    
    public Location getLocationAtIndex(int index)
    {
        return mPath.get(index);
    }
    
    public void reverse()
    {
        Collections.reverse(mPath);
    }
    
    
    
}

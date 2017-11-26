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

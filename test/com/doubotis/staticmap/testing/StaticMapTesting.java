package com.doubotis.staticmap.testing;

/*
 * Copyright (C) 2017 Admin
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

import com.doubotis.staticmap.StaticMap;
import com.doubotis.staticmap.layers.TMSLayer;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Admin
 */
public class StaticMapTesting {
    
    public StaticMapTesting() {
    }
    
    @Test
    public void testSmallMap() {
        
        try
        {
            
            StaticMap mp = new StaticMap(200, 200);
            mp.setLocation(50.5, 5.5);
            mp.setZoom(14);
            
            TMSLayer osmMap = new TMSLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png");
            mp.addLayer(osmMap);
            
            File f = new File(File.separator + "test.png");
            System.out.println("Printing small map in file " + f.getAbsolutePath() + "...");
            mp.drawInto(f);
            System.out.println("Printing done");
        
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMapOfCityWithOpacity() {
        
        try
        {
            
            StaticMap mp = new StaticMap(400, 400);
            mp.setLocation(48.8529009, 2.2454292);
            mp.setZoom(14);
            
            TMSLayer osmMap = new TMSLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png");
            osmMap.setOpacity(0.5f);
            mp.addLayer(osmMap);
            
            File f = new File(File.separator + "test2.png");
            System.out.println("Printing map of city with opacity in file " + f.getAbsolutePath() + "...");
            mp.drawInto(f);
            System.out.println("Printing done");
        
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

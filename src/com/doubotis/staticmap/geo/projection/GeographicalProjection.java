/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.staticmap.geo.projection;

import com.doubotis.staticmap.geo.Location;

/**
 *
 * @author cbrasseur
 */
public interface GeographicalProjection<T> {
    
    /** Gets a point from a latitude/longitude coordinate. Doesn't include the {@link StaticMap} offset. */
    public T unproject(Location location, int zoom);
    
    /** Gets a location from a point in the map. {@link StaticMap} offset must be included before requesting. */
    public Location project(T pt, int zoom);
    
}

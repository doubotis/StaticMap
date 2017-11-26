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

public class MercatorUtils
{
    public static double[] googleCoordinatesToLatLng(int[] pixel)
    {
    	double originShift = 2 * Math.PI * 6378137 / 2.0;
        double lat = (pixel[1] / originShift) * 180.0;
        lat = 180 / Math.PI * (2 * Math.atan( Math.exp( lat * Math.PI / 180.0)) - Math.PI / 2.0);
        double lon = (pixel[0] / originShift) * 180.0;
        return new double[] {lat, lon};
    }
	
    private static double bound(double value, double opt_min, double opt_max)
    {
        value = Math.max(value, opt_min);
        value = Math.min(value, opt_max);
        return value;
    }

    
}

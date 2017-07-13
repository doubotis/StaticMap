# Static Map Creator
Customizable Static Image Map Generator

This library manages the production of images for map vizualization in the Java language, using tiled maps.
This could be used for creation of small "preview" pictures, or produce very big images, for printing for instance.
**The nearest well-known example is the [Google Static Maps API](https://developers.google.com/maps/documentation/static-maps/?hl=fr). Basically, this library is able to generate the same-like images, except the tile source must be provided by yourself.**

**Additionnally, this library is _NOT_ a wrapper for Static Maps APIs (Bing, MapBox, Google, ...). The library will not request any services to make the map, he will MAKE the map itself, by requesting and assembling the map tiles from a tile service, like MapBox or OpenStreetMap tile services.**

### Features
* From 8x8 to infinite map sizes (tested until 9933x7026, A1 page at 300dpi)
* Includes TMS and WMS easy-to-use layers
* Supports user-defined custom layers
* Infinite number of layers

Here's an exemple of what you can produce with this, small pictures first:

![Sample 0](https://github.com/doubotis/MapPictureGenerator/blob/master/samples/sample-0.png)
![Sample 1](https://github.com/doubotis/MapPictureGenerator/blob/master/samples/sample-1.png)
![Sample 2](https://github.com/doubotis/MapPictureGenerator/blob/master/samples/sample-2.png)

*Tiles for picture 1 to are served from the OSM Topo Maps tile provider. Tiles for pictures 2 and 3 are served from the OSM Base Maps tile provider.*

Maybe you want bigger pictures?

![Sample 3](https://github.com/doubotis/MapPictureGenerator/blob/master/samples/sample-3.png)

*Tiles are served from the OSM Topo Maps tile provider*

Bigger and bigger. Superimpose TMs and WMS layers.

![Sample 4](https://github.com/doubotis/MapPictureGenerator/blob/master/samples/sample-4.png)

*Tiles served from the OSM Base Maps tile provider, and dots served from the WMS Geolives S.A. Server Tile, displaying all public trails of the SityTrail community.*

**Supports any tile size with a very wide range of resolutions.
The library was tested from 8x8 to 9933x7026 (A1 page, 300dpi) resolutions with good results.**

## Stability
* Used for generating small static images in production environment since 2016, without problems.
* Used for printing services in beta-test environment since 2017, without problems.

## License
[GNU Generic Public License](https://github.com/doubotis/MapPictureGenerator/blob/master/LICENSE)

## Quick Guide

### Installation

1. Build the library by yourself or use this **[distribution jar](https://github.com/doubotis/MapPictureGenerator/blob/master/dist/MapPictureGenerator.jar)**.
2. Add the .jar to your project.
3. Add the JTS library **[jts-1.8.jar](https://github.com/doubotis/MapPictureGenerator/blob/master/lib/jts-1.8.jar)**.

### Use the library

Instantiate a `StaticMap` object, and pass the width and height of the wanted final image as parameters.
From this object you can set the location and zoom.
Create a `TMSMapType` to set the basemap provider source, then add it to the list of layers of the `StaticMap`.
Finally, tell the library to draw the image into a file or into an output stream.

Here's the final example:
```
StaticMap mp = new StaticMap(pictureWidth, pictureHeight);
TMSMapType baseMap = new TMSMapType("http://{s}.tile.osm.org/{z}/{x}/{y}.png");
mp.setLocation(50.5, 5.5);
mp.setZoom(13);
mp.addLayer(baseMap);
mp.drawInto(new File(outPath));
```

### Additional features

###### fitBounds(bounds)
Instead of setting the location and zoom, you can tell the `StaticMap` object to fit bounds.
```
mp.fitBounds(new LocationBounds(xmin, xmax, ymin, ymax));
```

###### fitBounds(bounds, minZoom, maxZoom)
You can tell the fit bounds method to limit the zoom range to min-max values.
```
mp.fitBounds(new LocationBounds(xmin, xmax, ymin, ymax), minZoom, maxZoom);
```

###### Use `LocationPathLayer`
You can add a linestring by adding a `LocationPathLayer` to your `StaticMap` object.

```
Location[] path;
final LocationPathLayer layer = new LocationPathLayer(path);
staticMap.addLayer(layer);
```

###### Create your custom layer
You can add yourself a custom layer by creating a class that implements `Layer`.

```
public class YourLayer implements Layer {
  ...
  
  @Override
    public void draw(Graphics2D graphics, StaticMap mp) {
      // Get the current projection.
      MercatorProjection proj = mp.getProjection();
      
      // Get the tile size.
      int tileSize = proj.getTileSize();
      
      // Get the Offset (that means, the offset from the true position of an element, 
      // please see LocationPathLayer for examples of the use of this method).
      PointF offset = mp.getOffset();
    }
}
```

Then add it to the `StaticMap` object.
```
YourLayer layer = new YourLayer();
staticMap.addLayer(layer);
```

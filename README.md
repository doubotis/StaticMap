# MapPictureGenerator
Customizable Image Map Generator

This library manages the production of images for map vizualization in the Java language, using tiled maps.
This could be used for creation of small "preview" pictures, or produce very big images, for printing for instance.

Here's an exemple of what you can produce with this:
(https://github.com/doubotis/MapPictureGenerator/blob/master/samples/sample-0.png)

Supports any tile size with a very wide range of resolutions.
The engine is tested from 8x8 to 9933x7026 (A1 page, 300dpi) resolutions with good results.

Best part of the engine is about the customization. You can add many layers as you want.
* Add transparent TMS or WMS layers on top of a basemap
* Add some agreements, like pins, lines or polygons
* At least 1 layer.
* A basemap is not required.

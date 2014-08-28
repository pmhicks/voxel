## Notes

Voxel/height maps are something that I've wanted to play around with for a very long time but for one reason or another never got around to. The heyday of pure voxel engines for games (e.g. the Comanche series) are long gone but I still find the concept fascinating.  

Since I had to (re)learn the math involved for graphics I spent a lot of time going through old books and Googling concepts. These are my consolidated notes with additional implementation details.  

Notes are in no particular order.

### Maps

Maps are 1024 by 1024 and made up of 2 parts: the height map and the texture map stored as PNG's. The height map uses a byte for height so 0-255 possible values. Texture map is 24-bit RGB values.

Code uses (0,0) coordinates at bottom left, +Y goes up and X+ goes right. Java uses (0,0) at top left with Y+ going down. When loading from PNG's and writing to a BufferedImage the Y values need to be converted. 


### Map Creation with L3DT
This is mostly for myself:
* Create map with the following settings:
	* Width 1024
	* Height 1024
	* Horiz. Scale 50
	* Edge wrapping on
* Modify as needed
* Save Project (dont save after clipping)
* Clip height field below 0 to 0
* Export height map as PNG with following
	* Invert Y = false (default)
	* Bit depth = 8-bit
* Export texture map with following
	* Invert Y = false (default)


### Rotation and Camera
2-d rotation formula:
	
	x' = x * cosθ - y * sinθ
	y' = x * sinθ + y * cosθ

Positive angles rotate counterclockwise and negative angles rotate clockwise.

The initial camera direction is [0,1] so the formula can be simplified for rotating the camera to:

	x' = -sinθ
	y' = cosθ
	
### Perspective calculation

The perspective calculation comes from an old [flipcode article](http://www.flipcode.com/archives/Realtime_Voxel_Landscape_Engines-Part_2_Rendering_the_Landscapes_Structure.shtml)

Ys = ( Yp - Yc ) * Ks / Z + Kc

* Ys: Height on screen
* Yp: Height of point at x,y coordinates
* Yc: Height of camera
*  Z: Distance from the camera to the point
* Ks: Constant to scale the projection
* Kc: constant to center the projection

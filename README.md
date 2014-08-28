### Simple Voxel Engine

An old school voxel engine in Java. Includes 4 maps.

Maps built using L3DT [http://www.bundysoft.com/L3DT/]

See NOTES.md for implementation details

### Build

Simply use the Java compiler:

	javac *.java
	
### Keys
* `w` - move forward
* `s` - move backward
* `a` - turn left
* `d` - turn right
* `q` - move up
* `e` - move down
* `f` - toggle fog
* `1-4` - choose map

### Known Issues
If the window loses focus while one of the move keys are pressed the Key Listener wont receive the key released event. The player position will continue to move even if the window gains focus until the original move key is pressed and released. 

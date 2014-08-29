
all:
	javac *.java

jar: all
	jar cvfm voxel.jar manifest.mf *.class maps

run: all
	java Voxel

clean:
	rm -f *.class voxel.jar


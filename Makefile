
all:
	javac *.java

jar: all
	jar cvfm voxel.jar manifest.mf *.class maps

clean:
	rm -f *.class voxel.jar


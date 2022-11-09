# Based on the sample code provided in https://stackoverflow.com/questions/25735553/creating-a-makefile-for-jar-executable
JAVAC=/usr/lib/jvm/java-11/bin/javac

all : clean jar clean

clean :
	rm -f *.class
	rm -f manifest.txt

jar : XCollects.class
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: ." >> manifest.txt
	@echo "Main-Class: XCollects" >> manifest.txt
	@echo "" >> manifest.txt
	jar cmf manifest.txt xcollects.jar XCollects.class

XCollects.class : src/XCollects.java
	$(JAVAC) -d . src/XCollects.java
#!/bin/bash
WORK=~/workspace/configsys
echo making jar
rm -f configsys.war
cd WEB-INF/classes
rm *.class
javac *.java
cd $WORK
jar cf configsys.war index.html configsys.css favicon.ico WEB-INF/web.xml WEB-INF/classes/*.class WEB-INF/lib/*.jar META-INF
echo done

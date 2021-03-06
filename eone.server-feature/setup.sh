#!/bin/sh
#
echo ... Setup iDempiere Server

# Setup eone.properties and eoneEnv.properties
./idempiere --launcher.ini setup.ini -application eone.install.application

echo ... Setup Jetty
# Setup Jetty
./idempiere --launcher.ini setup.ini -application org.eclipse.ant.core.antRunner -buildfile build.xml

echo ... Make .sh executable
chmod -R a+x *.sh
find . -name '*.sh' -exec chmod a+x '{}' \;

echo ...
echo For problems, check log file in base directory

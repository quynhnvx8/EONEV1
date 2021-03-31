#!/bin/sh
#
echo Setup idempiere Server

# Setup eone.properties and eoneEnv.properties
./idempiere --launcher.ini setup.ini -application eone.install.console-application

# Setup Jetty
./idempiere --launcher.ini setup.ini -application org.eclipse.ant.core.antRunner -buildfile build.xml

echo .
echo For problems, check log file in base directory

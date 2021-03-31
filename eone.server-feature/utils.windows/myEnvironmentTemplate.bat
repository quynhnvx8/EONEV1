@Rem	myEnvironment defines the variables used for Adempiere
@Rem	Do not edit directly - use RUN_setup
@Rem	
@Rem	$Id: myEnvironmentTemplate.bat,v 1.12 2005/01/22 21:59:15 jjanke Exp $

@Echo Setting myEnvironment ....
@Rem	Clients only needs
@Rem		EONE_HOME
@Rem		JAVA_HOME 
@Rem	Server install needs to check
@Rem		EONE_DB_NAME	(for Oracle)
@Rem		passwords

@Rem 	Homes ...
@SET EONE_HOME=@EONE_HOME@
@SET JAVA_HOME=@JAVA_HOME@


@Rem	Database ...
@SET EONE_DB_SERVER=@EONE_DB_SERVER@
@SET EONE_DB_USER=@EONE_DB_USER@
@SET EONE_DB_PASSWORD=@EONE_DB_PASSWORD@
@SET EONE_DB_URL=@EONE_DB_URL@
@SET EONE_DB_PORT=@EONE_DB_PORT@

@Rem	Oracle specifics
@SET EONE_DB_PATH=@EONE_DB_PATH@
@SET EONE_DB_NAME=@EONE_DB_NAME@
@SET EONE_DB_SYSTEM=@EONE_DB_SYSTEM@

@Rem	Homes(2)
@SET EONE_DB_HOME=@EONE_HOME@\utils\@EONE_DB_TYPE@
@SET JBOSS_HOME=@EONE_HOME@\jboss

@Rem	Apps Server
@SET EONE_APPS_TYPE=@EONE_APPS_TYPE@
@SET EONE_APPS_SERVER=@EONE_APPS_SERVER@
@SET EONE_JNP_PORT=@EONE_JNP_PORT@
@SET EONE_WEB_PORT=@EONE_WEB_PORT@
@SET EONE_APPS_DEPLOY=@EONE_APPS_TYPE@
@Rem	SSL Settings
@SET EONE_SSL_PORT=@EONE_SSL_PORT@
@SET EONE_KEYSTORE=@EONE_KEYSTORE@
@SET EONE_KEYSTOREWEBALIAS=@EONE_KEYSTOREWEBALIAS@
@SET EONE_KEYSTOREPASS=@EONE_KEYSTOREPASS@

@Rem	Java
@SET EONE_JAVA=@JAVA_HOME@\bin\java
@SET EONE_JAVA_OPTIONS=@EONE_JAVA_OPTIONS@ -DEONE_HOME=@EONE_HOME@
@SET CLASSPATH="@EONE_HOME@\lib\Adempiere.jar;@EONE_HOME@\lib\AdempiereCLib.jar;"

@Rem Save Environment file
@if (%1) == () copy utils\myEnvironment.bat utils\myEnvironment_%RANDOM%.bat /Y 


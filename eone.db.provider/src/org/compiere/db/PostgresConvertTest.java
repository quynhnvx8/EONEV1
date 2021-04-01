/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.Adempiere;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Ini;


/**
 *	Order Test Example
 *	
 *  @author Jorg Janke
 *  @version $Id: PostgresConvertTest.java,v 1.2 2005/03/11 20:30:16 jjanke Exp $
 */
public class PostgresConvertTest
{
	/**
	 * 	PostgresConvertTest
	 */
	public PostgresConvertTest () 
	{
	}	//	PostgresConvertTest
	
	/**	Logger	*/
	private static final CLogger	log	= CLogger.getCLogger (PostgresConvertTest.class);
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Adempiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//
		Ini.setProperty(Ini.P_UID,"SuperUser");
		Ini.setProperty(Ini.P_PWD,"System");
		Ini.setProperty(Ini.P_ROLE,"GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG,"HQ");
		Ini.setProperty(Ini.P_WAREHOUSE,"HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE,"English");
		//Login login = new Login(Env.getCtx());
		//if (!login.batchLogin(null))
		//	System.exit(1);
		
		log.info("start test");
		StringBuilder sql = new StringBuilder (
				"SELECT 'un DATE en una constante'," +
				" 'ropa de cache para damas' FROM DUAL");
        try
        {
        	PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
        	@SuppressWarnings("unused")
			ResultSet rs = pstmt.executeQuery();
        }
        catch(SQLException e) 
        {
        	log.log(Level.SEVERE, sql.toString(), e);
        }
		
                
               
                
		sql = new StringBuilder ("UPDATE I_Order SET M_Warehouse_ID=(SELECT M_Warehouse_ID FROM M_Warehouse w WHERE ROWNUM=1 AND I_Order.AD_Client_ID=w.AD_Client_ID AND I_Order.AD_Org_ID=w.AD_Org_ID) WHERE M_Warehouse_ID IS NULL AND I_IsImported<>'Y' AND AD_Client_ID=11");
		@SuppressWarnings("unused")
		int no = DB.executeUpdate(sql.toString(), getTrxName());
               sql = new StringBuilder ("UPDATE I_Order o SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) WHERE C_BPartner_ID IS NULL AND ContactName IS NOT NULL AND EXISTS (SELECT Name FROM AD_User u WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL GROUP BY Name HAVING COUNT(*)=1) AND I_IsImported<>'Y' AND AD_Client_ID=11");                 
		no = DB.executeUpdate(sql.toString(), getTrxName());
      
                
              






                

                
		log.info("final test");

	}	//	main

	public static int getRecord_ID()
	{
		return 10000000;
	}	//	getRecord_ID

	protected static String getTrxName()
	{
		return "Test";
	}	//	getTrxName
	
}	//	PostgresConvertTest
/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package eone.base.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class SequenceCheck extends SvrProcess
{
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (SequenceCheck.class);
	
	
	String tableNameList = null;
	protected void prepare()
	{
		ProcessInfoParameter[] para = null;
		para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("tableNameList")) {
				tableNameList = (String) para[i].getParameter();
			} 
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	
	
	protected String doIt() throws java.lang.Exception
	{
		log.info("");
		if(tableNameList!=null && !tableNameList.isEmpty()){
			tableNameList = tableNameList.replaceAll(";", ",");
			tableNameList = tableNameList.replaceAll(",", "','");
			tableNameList = tableNameList.replaceAll(" ", "");
			tableNameList = " '"+tableNameList.toUpperCase() + "'";
		}
		checkTableSequences();
		
		return "Sequence Check";
	}	//	doIt
	
	private void checkTableSequences ()
	{
		String trxName = get_TrxName();
		
		String tableNotIn = "'C_Temp'"
				+ "";
		
		String sql = "SELECT TableName "
			+ "FROM AD_Table t "
			+ "WHERE IsActive='Y' AND IsView='N' and exists (select 1 from ad_column c where t.ad_table_id = c.ad_table_id and c.iskey = 'Y') "
			+ " and tableName not in ("+ tableNotIn +") ";
		if(tableNameList!=null && tableNameList.length()>0){
			sql += " and upper(TableName) in (" + tableNameList + ")  ";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sequenceName = null;
		String tableName = null;
		Integer current_val;
		int result;
		int nextVal;
		int newNextVal;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				tableName = rs.getString(1);
				sequenceName = tableName.toUpperCase() + "_SEQ";
				
				// Check xem da ton tai sequence chua
				if (DB.isOracle()) {
					sql = "SELECT " + sequenceName + ".nextVal FROM DUAL";
				} else {
					sql = "SELECT 1 FROM pg_class where upper(relname) = '" + sequenceName + "'";
				}
				
				nextVal = DB.getSQLValue(trxName, sql);
				
				// Neu chua co sequence thi tao
				if (nextVal == -1) {
					sql = "SELECT MAX(" + tableName + "_ID) FROM " + tableName;
					current_val = DB.getSQLValue(trxName, sql);
					
					if (current_val == -1) {
						addLog(0, null, null, "Error getting current max ID for " + tableName);
					}
					// Tao sequence
					else {
						current_val = current_val == 0 ? 1:current_val; 
						if (DB.isOracle()) {
							sql = "CREATE SEQUENCE " + sequenceName	+ " START WITH " + current_val	+ " INCREMENT BY 1 NOCACHE NOCYCLE";
						}else {
							sql = "CREATE SEQUENCE " + sequenceName	+ " START WITH " + current_val	+ " INCREMENT BY 1";
						}
						
						result = DB.executeUpdate(sql, trxName);
						if (result == -1) {
							addLog(0, null, null, "Error create sequence for " + tableName);
						} else {
							addLog(0, null, null, "Sequence for table " + tableName + " created with start " + current_val);
						}
					}
				}
				// Neu co roi thi check
				else {
					sql = "SELECT MAX(" + tableName + "_ID) FROM " + tableName;
					current_val = DB.getSQLValue(trxName, sql);
					
					if (current_val == -1) {
						addLog(0, null, null, "Error getting current max ID for " + tableName);
					}
					// Tao sequence
					else {
						if (nextVal < current_val) {
							sql = "ALTER SEQUENCE " + sequenceName + " INCREMENT BY " + (current_val - nextVal);
							result = DB.executeUpdate(sql, trxName);
							// Loi khong update duoc seq
							if (result == -1) {
								addLog(0, null, null, "Cannot update sequence step to " + (current_val - nextVal) + " for table" + tableName);
							} else {
								if (DB.isOracle()) {
									sql = "SELECT " + sequenceName + ".nextVal FROM DUAL";
								} else {
									sql = "SELECT nextval('" + sequenceName + "')";
								}
								newNextVal = DB.getSQLValue(trxName, sql);
								if (newNextVal == -1) {
									addLog(0, null, null, "Can increase sequence");
								} else if (newNextVal < current_val) {
									addLog(0, null, null, "Updated sequence failure");
								} else {
									addLog(0, null, null, "Sequence for table " + tableName + " updated from " + (nextVal - 1) + "to " + newNextVal);
									sql = "ALTER SEQUENCE " + sequenceName + " INCREMENT BY 1";
									result = DB.executeUpdate(sql, trxName);
									if (result == -1) {
										addLog(0, null, null, "Cannot update sequence step to 1 for table " + tableName);
									}
								}
							}
						}else{
							addLog(0, null, null, "OK: " + tableName);
						}
					}
				}
			}
			addLog(0, null, null, "SequenceName: " + sequenceName);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}
}	//	SequenceCheck

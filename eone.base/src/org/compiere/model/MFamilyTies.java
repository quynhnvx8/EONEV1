package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MFamilyTies extends X_HR_FamilyTies
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MFamilyTies (Properties ctx, int HR_FamilyTies_ID, String trxName)
	{
		super (ctx, HR_FamilyTies_ID, trxName);
		
	}	//	MAssetUse

	
	public static MFamilyTies get (Properties ctx, int HR_FamilyTies_ID, String trxName)
	{
		return (MFamilyTies) MTable.get(ctx, Table_ID).getPO(HR_FamilyTies_ID, trxName);
	}
	
	
	public static Map<Integer, BigDecimal> getAllItem (Properties ctx, int AD_Client_ID, int C_Period_ID) throws SQLException
	{
		Map<Integer, BigDecimal> listItem = new HashMap<Integer, BigDecimal>();
		MPeriod period = MPeriod.get(ctx, C_Period_ID);
		String sql = 
				" select count(1) as count, HR_Employee_ID From HR_FamilyTies \r\n" + 
				" Where IsPersonDependent='Y'\r\n" + 
				"	And ValidFrom <= ? \r\n" + 
				"	And ((ValidTo != null And ValidTo >= ?) Or ValidTo is null) And AD_Client_ID = ? \r\n" + 
				"Group by HR_Employee_ID";
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setTimestamp(1, period.getEndDate());
		ps.setTimestamp(2, period.getEndDate());
		ps.setInt(3, AD_Client_ID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			listItem.put(rs.getInt("HR_Employee_ID"), rs.getBigDecimal("count"));
		}
		
		DB.close(rs, ps);
		
		return listItem;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_Name, getName());
		dataColumn.put(COLUMNNAME_HR_ItemLine_24_ID, getHR_ItemLine_24_ID());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_FamilyTies_ID, getHR_FamilyTies_ID());
		dataColumn = null;
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_Name);
			return false;
		}
		
		return true;
	}


	public MFamilyTies (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetUse


	protected boolean afterSave (boolean newRecord,boolean success)
	{
		log.info ("afterSave");
		if (!success)
			return success;
		
		return success;
		 
		
	}	//	afterSave

	

}

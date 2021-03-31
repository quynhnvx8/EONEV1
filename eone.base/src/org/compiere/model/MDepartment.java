
package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;


public class MDepartment extends X_AD_Department
{
	
	private static final long serialVersionUID = -5604686137606338725L;


	
	public static MDepartment get (Properties ctx, int AD_Department_ID)
	{
		MDepartment retValue = s_cache.get (AD_Department_ID);
		if (retValue != null)
			return retValue;
		final String whereClause = "AD_Department_ID=?";
		retValue = new Query(ctx,I_AD_Department.Table_Name,whereClause,null)
		.setParameters(AD_Department_ID)
		.firstOnly();
		s_cache.put (AD_Department_ID, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MDepartment>	s_cache	= new CCache<Integer,MDepartment>(Table_Name, 50);
	
	
	
	public MDepartment (Properties ctx, int AD_Department_ID, String trxName)
	{
		super(ctx, AD_Department_ID, trxName);
		
	}	

	
	public MDepartment (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	

	public MDepartment (MClient client, String value, String name)
	{
		this (client.getCtx(), 0, client.get_TrxName());
		setValue (value);
		setName (name);
	}	


	private boolean createBPartner(boolean isCreateBPartner) {
		String sql = "Select C_BPartner_ID From C_BPartner Where AD_Department_ID = ? And IsAutoCreate = 'Y'";
		if (!isCreateBPartner) {
			sql = "Delete From C_BPartner Where AD_Department_ID = ? And IsAutoCreate = 'Y'";
			return DB.executeUpdate(sql, getAD_Department_ID(), get_TrxName()) > 0 ? true : false;
		}
		
		int p_C_Bpartner_ID = DB.getSQLValue(get_TrxName(), sql, getAD_Department_ID());
		int C_BP_Group_ID = DB.getSQLValue(get_TrxName(), "Select C_BP_Group_ID From C_BP_Group Where GroupType = ?", MBPGroup.GROUPTYPE_OrgDepartment);
		MBPartner bp = null;
		if (p_C_Bpartner_ID <= 0) {
			bp = new MBPartner(getCtx(), 0, get_TrxName());
			bp.setAD_Org_ID(getAD_Org_ID());
			bp.setAD_Client_ID(getAD_Client_ID());
			bp.setValue(getValue());
			bp.setName(getName());
			bp.setName2(getName());
			bp.setIsAutoCreate(true);
			bp.setIsEmployee(true);
			bp.setAD_Department_ID(getAD_Department_ID());
			bp.setC_BP_Group_ID(C_BP_Group_ID);
			
		} else {
			bp = MBPartner.get(getCtx(), p_C_Bpartner_ID);
			bp.setValue(getValue());
			bp.setName(getName());
			bp.setName2(getName());
			
		}
		if (!bp.save(get_TrxName())) {
			return false;
		}
		return true;
		
		
	}
	

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord || is_ValueChanged("Value") || isActive() || is_ValueChanged("Name")) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "AD_Department_ID != ? And (Value = ? Or Name = ?) And AD_Client_ID = ? And IsActive = 'Y' And IsSummary = 'N'", get_TrxName())
					.setParameters(getAD_Department_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "Value Or Name exists");
				return false;
			}

		}
		
		if (newRecord || is_ValueChanged("Name") || is_ValueChanged("Value") || is_ValueChanged("IsCreateBPartner")) {
			if (!createBPartner(isCreateBPartner())) {
				log.saveError("Error", "Create BPartner not success !");
			}			
		}
		return true;
	}


	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		
		return true;
	}	//	afterSave
	
	
	protected boolean afterDelete (boolean success)
	{
		
		return success;
	}	//	afterDelete


	
	
}	

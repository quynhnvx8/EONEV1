package eone.base.process;

import java.util.List;

import org.compiere.util.DB;

import eone.base.model.MOrg;
import eone.base.model.Query;
import eone.base.model.X_AD_Org;

//org.compiere.process.VerifyDepartment
public class VerifyRelatedClient extends SvrProcess
{
	protected void prepare()
	{
	
	}	//	prepare
	
	
	
	protected String doIt() throws java.lang.Exception
	{
		//Delete Partner
		String sqlDelete = "Delete From C_BPartner Where Value not in (Select Value From AD_Org Where IsActive = 'Y' And AD_Client_ID = ? And (OrgType is not null or (OrgType is null and IsSummary = 'N'))) And IsAutoCreate = 'Y' And IsEmployee != 'Y'";
		DB.executeUpdate(sqlDelete, getAD_Client_ID(), get_TrxName());
		
		//Delete warehouse
		sqlDelete = "Delete From M_Warehouse Where Value not in (Select Value From AD_Org Where IsActive = 'Y' And AD_Client_ID = ? And (OrgType is not null or (OrgType is null and IsSummary = 'N'))) And IsAutoCreate = 'Y'";
		DB.executeUpdate(sqlDelete, getAD_Client_ID(), get_TrxName());
		
		//Delete Department
		sqlDelete = "Delete From AD_Department Where Value not in (Select Value From AD_Org Where IsActive = 'Y' And AD_Client_ID = ? And (OrgType is not null or (OrgType is null and IsSummary = 'N'))) And IsAutoCreate = 'Y'";
		DB.executeUpdate(sqlDelete, getAD_Client_ID(), get_TrxName());
		String whereClause = "AD_Client_ID = ? And AD_Org_ID > 0 And (OrgType is not null or (OrgType is null and IsSummary = 'N')) And IsActive = 'Y' ";
		List<MOrg> list = new Query(getCtx(), X_AD_Org.Table_Name, whereClause, get_TrxName())
				.setParameters(getAD_Client_ID())
				.list();
		for(int i = 0; i < list.size(); i++) {
			CreateOrUpdateRelatedOrg.processData(list.get(i).getAD_Org_ID(), getCtx(), get_TrxName());
		}
		return "Check Department Success!";
	}	//	doIt
	
	
	
}	

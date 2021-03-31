package eone.base.process;

//org.compiere.process.VerifyRelatedByOrg
//Moi Org tao ra 1 Warehouse, 1 Department, 1 BPartner
public class VerifyRelatedByOrg extends SvrProcess
{
	protected void prepare()
	{
	
	}	//	prepare
	
	protected String doIt() throws java.lang.Exception
	{
		int AD_Org_ID = getRecord_ID();
		return CreateOrUpdateRelatedOrg.processData(AD_Org_ID, getCtx(), get_TrxName());
	}	//	doIt
	
}	
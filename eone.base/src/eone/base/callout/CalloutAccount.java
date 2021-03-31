package eone.base.callout;

import java.util.List;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerInfo;
import org.compiere.model.MDocType;
import org.compiere.model.X_C_Account;
import org.compiere.model.X_C_DocType;

//org.compiere.model.CalloutAccount.fillByDocType
//org.compiere.model.CalloutAccount.fillByBPartner
//org.compiere.model.CalloutAccount.fillByAsset
//org.compiere.model.CalloutAccount.fillByProduct
//org.compiere.model.CalloutAccount.fillByContract
//org.compiere.model.CalloutAccount.fillByProject
//org.compiere.model.CalloutAccount.fillByConstruc
//org.compiere.model.CalloutAccount.fillByWarehouse
//org.compiere.model.CalloutAccount.fillByTax
public class CalloutAccount extends CalloutEngine
{
	
	public String fillByDocType (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		String columnName = mField.getColumnName();
		List<MAccount> re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		if(columnName.equalsIgnoreCase("C_DocType_ID")) {
			re = MAccount.getAccountDocType(p_ID);						
		}
		
		if(columnName.equalsIgnoreCase("C_DocTypeSub_ID")) {
			re = MAccount.getAccountDocTypeSub(p_ID);			
		}
		for(int i = 0; i < re.size(); i++) {
			if (X_C_Account.TYPEACCOUNT_DebitAccount.equals(re.get(i).getTypeAccount())) {
				mTab.setValue("Account_Dr_ID", re.get(i).getAccount_ID());
			}
			if (X_C_Account.TYPEACCOUNT_CreditAccount.equals(re.get(i).getTypeAccount())) {
				mTab.setValue("Account_Cr_ID", re.get(i).getAccount_ID());
			}
			
			if (X_C_Account.TYPEACCOUNT_DebitOtherAccount.equals(re.get(i).getTypeAccount())) {
				mTab.setValue("AccountOT_Dr_ID", re.get(i).getAccount_ID());
			}
			if (X_C_Account.TYPEACCOUNT_CreditOtherAccount.equals(re.get(i).getTypeAccount())) {
				mTab.setValue("AccountOT_Cr_ID", re.get(i).getAccount_ID());
			}
			if (X_C_Account.TYPEACCOUNT_TaxInputAccount.equals(re.get(i).getTypeAccount()) 
					||X_C_Account.TYPEACCOUNT_TaxOutputAccount.equals(re.get(i).getTypeAccount())) {
				mTab.setValue("Account_Tax_ID", re.get(i).getAccount_ID());
			}
		}
		return "";
	}	


	public String fillByTax (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		String columnName = mField.getColumnName();
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		int Account_ID = 0;
		List<MAccount> re = MAccount.getAccountTax(p_ID);
		if (re == null) {
			re = MAccount.getAccountDocType(p_C_DocType_ID);
		}
		for (int i = 0; i < re.size(); i++) {
			if(columnName.equalsIgnoreCase("C_Tax_ID") && re != null && re.get(i).getAccount_ID() > 0) {
				
				if (dt != null && X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType()) 
						&& X_C_Account.TYPEACCOUNT_TaxInputAccount.equals(re.get(i).getTypeAccount())) {
					Account_ID = re.get(i).getAccount_ID();
				} else if (dt != null && X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType()) 
						&& X_C_Account.TYPEACCOUNT_TaxOutputAccount.equals(re.get(i).getTypeAccount())) {
					Account_ID = re.get(i).getAccount_ID();
				}
				
			}
			if (X_C_Account.TYPEACCOUNT_TaxInputAccount.equals(re.get(i).getTypeAccount()) 
					||X_C_Account.TYPEACCOUNT_TaxOutputAccount.equals(re.get(i).getTypeAccount())) {
				Account_ID = re.get(i).getAccount_ID();
			}
		}
		
		mTab.setValue("Account_Tax_ID", Account_ID);
		
		return "";
	}
	
	public String fillByBPartner (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountBPartner(p_ID);	
		
		//Dien them thong tin infobpartner (neu co)
		MBPartnerInfo info = MBPartner.getInfoDefaultBPartner(ctx, p_ID);
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("C_BPartner_Dr_ID") 
					|| columnName.equalsIgnoreCase("C_BPartner_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
				if (info != null && info.getC_BPartnerInfo_ID() != 0) {
					mTab.setValue("C_BPartnerInfo_Dr_ID", info.getC_BPartnerInfo_ID());
				}
			}
			if((columnName.equalsIgnoreCase("C_BPartner_Cr_ID") 
					|| columnName.equalsIgnoreCase("C_BPartner_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
				if (info != null && info.getC_BPartnerInfo_ID() != 0) {
					mTab.setValue("C_BPartnerInfo_Cr_ID", info.getC_BPartnerInfo_ID());
				}
			}
		}
		
		
		return "";
	}
	
	public String fillByProduct (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountProduct(p_ID);	
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("M_Product_Dr_ID") 
					|| columnName.equalsIgnoreCase("M_Product_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
			}
			if((columnName.equalsIgnoreCase("M_Product_Cr_ID") 
					|| columnName.equalsIgnoreCase("M_Product_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
			}
		}
		
		
		return "";
	}
	
	public String fillByAsset (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountAsset(p_ID);	
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("A_Asset_ID") 
					|| columnName.equalsIgnoreCase("A_Asset_Dr_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
			}
			if((columnName.equalsIgnoreCase("A_Asset_ID") 
					|| columnName.equalsIgnoreCase("A_Asset_Cr_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
			}
		}
		
		
		return "";
	}
	
	public String fillByWarehouse (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountWarehouse(p_ID);	
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("M_Warehouse_ID") 
					|| columnName.equalsIgnoreCase("M_Warehouse_Dr_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
			}
			if((columnName.equalsIgnoreCase("M_Warehouse_ID") 
					|| columnName.equalsIgnoreCase("M_Warehouse_Cr_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
			}
		}
		
		
		return "";
	}
	
	public String fillByContract (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountContract(p_ID);	
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("C_Contract_ID") 
					|| columnName.equalsIgnoreCase("C_Contract_Dr_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
			}
			if((columnName.equalsIgnoreCase("C_Contract_ID") 
					|| columnName.equalsIgnoreCase("C_Contract_Cr_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
			}
		}
		
		
		return "";
	}
	
	public String fillByProject (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountProject(p_ID);	
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("C_Project_ID") 
					|| columnName.equalsIgnoreCase("C_Project_Dr_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
			}
			if((columnName.equalsIgnoreCase("C_Project_ID") 
					|| columnName.equalsIgnoreCase("C_Project_Cr_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
			}
		}
		
		
		return "";
	}
	
	public String fillByConstruc (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		
			return "";
		Object objDocType = mTab.getValue("C_DocType_ID");
		int p_C_DocType_ID = 0;
		if (objDocType != null) {
			p_C_DocType_ID = Integer.parseInt(objDocType.toString());
		}
		MDocType dt = MDocType.get(ctx, p_C_DocType_ID);
		String columnName = mField.getColumnName();
		MAccount re  = null;
		int p_ID = 0;
		if (value != null) {
			p_ID = Integer.parseInt(value.toString());
		}
		re = MAccount.getAccountConstruction(p_ID);	
		if (re != null && re.getAccount_ID() > 0 && dt != null && dt.getDocType() != null) {
			if((columnName.equalsIgnoreCase("C_Construction_ID") 
					|| columnName.equalsIgnoreCase("C_Construction_Dr_ID")) 
					&& X_C_DocType.DOCTYPE_Input.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Dr_ID", re.getAccount_ID());
			}
			if((columnName.equalsIgnoreCase("C_Construction_ID") 
					|| columnName.equalsIgnoreCase("C_Construction_Cr_ID")) 
					&& X_C_DocType.DOCTYPE_Output.equalsIgnoreCase(dt.getDocType())) {
				mTab.setValue("Account_Cr_ID", re.getAccount_ID());
			}
		}
		
		
		return "";
	}
	
}	


package eone.base.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;


public class MAccount extends X_C_Account
{
	/**
	 * Muc dich bang Account nay cau hinh theo thu tu uu tien nhu sau de fill tai khoan mac dinh khi hach toan
	 * Moi thuc the luon cau hinh 1 tai khoan mac dinh co C_Account.IsDefault = Y => Khong ton tai dong thoi 2 Default cho moi thuc the.
	 * 1. Uu tien DocType 
	 * 2. Neu co SubDocType thi theo SubDocType
	 * 3. Khi chon doi tuong, san pham, tai san, ... neu co cau hinh tai khoan rieng thi sẽ lay theo tai khoan rieng.
	 * 4. Uu tien tu "Con => cha": VD doi tuong ko co tai khoan thi quay ra nhom doi tuong tim tai khoan, neu ko co moi bo qua.
	 */
	private static final long serialVersionUID = 7980515458720808532L;


	public static MAccount get (Properties ctx)
	{
		final String whereClause = "1=1";
		MAccount retValue =  new Query(ctx,I_C_Account.Table_Name,whereClause,null)
		.first();
		return retValue;
	}	//	get
	
	//private static CLogger		s_log = CLogger.getCLogger (MAccount.class);


	public MAccount (Properties ctx, int C_Account_ID, String trxName)
	{
		super (ctx, C_Account_ID, trxName);
		
	}   //  MAccount


	public MAccount (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}   //  MAccount


	public MAccount ()
	{
		this (Env.getCtx(), 0, null);
	}	//	Account


	public String toString()
	{
		return "";
	}	//	toString

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		String sql = "";
		List<Object>  params = new ArrayList<Object>();
		if (isDefault()) {
			if (getC_DocType_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_DocType_ID = ? And C_Account_ID != ? And IsDefault = 'Y' And TypeAccount = ?";
				params.add(getC_DocType_ID());
				params.add(getC_Account_ID());
				params.add(getTypeAccount());
			}
			
			if (getC_DocTypeSub_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_DocTypeSub_ID = ? And C_Account_ID != ? And IsDefault = 'Y' And TypeAccount = ?";
				params.add(getC_DocTypeSub_ID());
				params.add(getC_Account_ID());
				params.add(getTypeAccount());
			}
			//Asset
			if (getA_Asset_Group_ID() > 0) {
				sql = "Select count(1) From C_Account Where A_Asset_Group_ID = ? And C_Account_ID != ? And IsDefault = 'Y' ";
				params.add(getA_Asset_Group_ID());
				params.add(getC_Account_ID());
			}
			if (getA_Asset_ID() > 0) {
				sql = "Select count(1) From C_Account Where A_Asset_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getA_Asset_ID());
				params.add(getC_Account_ID());
			}
			//BPartner
			if (getC_BP_Group_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_BP_Group_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getC_BP_Group_ID());
				params.add(getC_Account_ID());
			}
			if (getC_BPartner_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_BPartner_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getC_BPartner_ID());
				params.add(getC_Account_ID());
			}
			
			//Product
			if (getM_Product_Category_ID() > 0) {
				sql = "Select count(1) From C_Account Where M_Product_Category_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getM_Product_Category_ID());
				params.add(getC_Account_ID());
			}
			if (getM_Product_ID() > 0) {
				sql = "Select count(1) From C_Account Where M_Product_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getM_Product_ID());
				params.add(getC_Account_ID());
			}
			if (getM_Warehouse_ID() > 0) {
				sql = "Select count(1) From C_Account Where M_Warehouse_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getM_Warehouse_ID());
				params.add(getC_Account_ID());
			}
			
			//Tax
			if (getC_Tax_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_Tax_ID = ? And C_Account_ID != ? And IsDefault = 'Y'  And TypeAccount = ?";
				params.add(getC_Tax_ID());
				params.add(getC_Account_ID());
				params.add(getTypeAccount());
			}
			
			//Contract
			if (getC_Contract_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_Contract_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getC_Contract_ID());
				params.add(getC_Account_ID());
			}
			
			//Project
			if (getC_Project_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_Project_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getC_Project_ID());
				params.add(getC_Account_ID());
			}
			
			//Construction
			if (getC_Construction_ID() > 0) {
				sql = "Select count(1) From C_Account Where C_Construction_ID = ? And C_Account_ID != ? And IsDefault = 'Y'";
				params.add(getC_Construction_ID());
				params.add(getC_Account_ID());
			}
			
			int no = DB.getSQLValue(get_TrxName(), sql, params);
			if (no >= 1) {
				log.saveError("Error", "Exists set default");
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	//private static CCache<Integer,MAccount> s_cache	= new CCache<Integer,MAccount>(Table_Name, 40, CCache.DEFAULT_TIMEOUT);

	/*
	 * Lay danh sach tai khoan cau hinh theo doctype
	 */
	public static List<MAccount> getAccountDocType(int C_DocType_ID) {
		String sqlWhere = "C_DocType_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		List<MAccount> value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_DocType_ID, C_Element_ID)
				.list();
		return value;
	}
	
	
	/*
	 * Lay danh sach tai khoan cau hinh theo doctypeSub
	 */
	public static List<MAccount> getAccountDocTypeSub(int C_DocTypeSub_ID) {
		String sqlWhere = "C_DocTypeSub_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		List<MAccount> value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_DocTypeSub_ID, C_Element_ID)
				.list();
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh cua BPartner (neu co)
	 */
	public static MAccount getAccountBPartner(int C_BPartner_ID) {
		String sqlWhere = "C_BPartner_ID = ?  And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?) And IsDefault = 'Y'";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_BPartner_ID, C_Element_ID)
				.firstOnly();
		if (value == null) {
			MBPartner bp = MBPartner.get(Env.getCtx(), C_BPartner_ID);
			if (bp == null)
				return null;
			sqlWhere = "C_BP_Group_ID = ?  And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?) And IsDefault = 'Y'";
			value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
					.setParameters(bp.getC_BP_Group_ID(), C_Element_ID)
					.firstOnly();
		}
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh cua Asset (neu co)
	 */
	public static MAccount getAccountAsset(int A_Asset_ID) {
		String sqlWhere = "A_Asset_ID = ?  And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?) And IsDefault = 'Y'";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(A_Asset_ID, C_Element_ID)
				.firstOnly();
		if (value == null) {
			MAsset asset = MAsset.get(Env.getCtx(), A_Asset_ID, null);
			if (asset == null)
				return null;
			sqlWhere = "A_Asset_Group_ID = ?  And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?) And IsDefault = 'Y'";
			value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
					.setParameters(asset.getA_Asset_Group_ID(), C_Element_ID)
					.firstOnly();
		}
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh Product (neu co)
	 */
	public static MAccount getAccountProduct(int M_Product_ID) {
		boolean check = Env.getContext(Env.getCtx(), "#IsProduct").equals(Env.YES) ? true : false;
		if (!check) {
			return null;
		}
		String sqlWhere = "M_Product_ID = ?  And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?) And IsDefault = 'Y'";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(M_Product_ID, C_Element_ID)
				.firstOnly();
		if (value == null) {
			MProduct asset = MProduct.get(Env.getCtx(), M_Product_ID);
			if (asset == null)
				return null;
			sqlWhere = "M_Product_Category_ID = ?  And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?) And IsDefault = 'Y'";
			value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
					.setParameters(asset.getM_Product_Category_ID(), C_Element_ID)
					.firstOnly();
		}
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh Thue (neu co)
	 */
	public static List<MAccount> getAccountTax(int C_Tax_ID) {
		String sqlWhere = "C_Tax_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		List<MAccount> value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_Tax_ID, C_Element_ID)
				.list();
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh cua Hop dong (neu co)
	 */
	public static MAccount getAccountContract(int C_Contract_ID) {
		boolean check = Env.getContext(Env.getCtx(), "#IsContract").equals(Env.YES) ? true : false;
		if (!check) {
			return null;
		}
		String sqlWhere = "C_Contract_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_Contract_ID, C_Element_ID)
				.firstOnly();
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh cua Du An (neu co)
	 */
	public static MAccount getAccountProject(int C_Project_ID) {
		boolean check = Env.getContext(Env.getCtx(), "#IsProject").equals(Env.YES) ? true : false;
		if (!check) {
			return null;
		}
		String sqlWhere = "C_Project_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_Project_ID, C_Element_ID)
				.firstOnly();
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh cua Cong trinh (neu co)
	 */
	public static MAccount getAccountConstruction(int C_Construction_ID) {
		boolean check = Env.getContext(Env.getCtx(), "#IsConstruction").equals(Env.YES) ? true : false;
		if (!check) {
			return null;
		}
		String sqlWhere = "C_Construction_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(C_Construction_ID, C_Element_ID)
				.firstOnly();
		return value;
	}
	
	/*
	 * Lay tai khoan mac dinh cua Kho (neu co)
	 */
	public static MAccount getAccountWarehouse(int M_Warehouse_ID) {
		boolean check = Env.getContext(Env.getCtx(), "#IsProduct").equals(Env.YES) ? true : false;
		if (!check) {
			return null;
		}
		String sqlWhere = "M_Warehouse_ID = ? And IsDefault = 'Y' And Account_ID in (Select C_ElementValue_ID From C_ElementValue Where C_Element_ID = ?)";
		int C_Element_ID = Env.getContextAsInt(Env.getCtx(), "#C_Element_ID");
		MAccount value = new Query(Env.getCtx(),  X_C_Account.Table_Name, sqlWhere, null)
				.setParameters(M_Warehouse_ID, C_Element_ID)
				.firstOnly();
		return value;
	}
}	//	Account


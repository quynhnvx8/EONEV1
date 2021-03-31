package eone.base.process;

import java.util.Properties;

import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.model.MDepartment;
import org.compiere.model.MOrg;
import org.compiere.model.MWarehouse;
import org.compiere.util.DB;

public class CreateOrUpdateRelatedOrg  {
	
	public static String processData(int AD_Org_ID, Properties ctx, String trxName) {
		MOrg org = MOrg.get(ctx, AD_Org_ID);
		MDepartment dept = MDepartment.get(ctx, AD_Org_ID);
		if (dept == null) {
			dept = new MDepartment(ctx, 0, trxName);
			dept.setAD_Department_ID(AD_Org_ID);//Lay Department_DI = AD_Org_ID luon. Da xu ly Sequence dai khac			
		}
		if (dept.getValue() != org.getValue())
			dept.setValue(org.getValue());
		if (dept.getName() != org.getName())
			dept.setName(org.getName());
		dept.setDescription("This record have been created from Org. Don't delete !");
		dept.setIsAutoCreate(true);
		//dept.setIsCreateBPartner(true);
		dept.setAD_Org_ID(AD_Org_ID);
		if(!dept.save())
			return "Update Department false !";
		
		MWarehouse wh = MWarehouse.get(ctx, AD_Org_ID);
		if (wh == null) {
			wh = new MWarehouse(ctx, 0, trxName);
			wh.setM_Warehouse_ID(AD_Org_ID);//Tuong tu nhu Department.
		}
		wh.setName(org.getName());
		wh.setDescription("This record have been created from Org. Don't delete !");
		wh.setIsAutoCreate(true);
		wh.setValue(org.getValue());
		wh.setAD_Department_ID(dept.getAD_Department_ID());
		wh.setIsDefault(true);
		wh.setAD_Org_ID(AD_Org_ID);
		if (!wh.save())
			return "Update warehouse false!";
		
		MBPartner bp = MBPartner.get(ctx, AD_Org_ID);
		if (bp == null) {
			bp = new MBPartner(ctx, 0, trxName);
			bp.setC_BPartner_ID(AD_Org_ID);
			int C_BP_Group_ID = DB.getSQLValue(trxName, "Select C_BP_Group_ID From C_BP_Group Where GroupType = ?", MBPGroup.GROUPTYPE_OrgDepartment);
			bp.setC_BP_Group_ID(C_BP_Group_ID);
		}
		bp.setValue(org.getValue());
		bp.setName(org.getName());
		bp.setName2(org.getName());
		bp.setAD_Department_ID(dept.getAD_Department_ID());
		bp.setDescription("This record have been created from Org. Don't delete !");
		bp.setIsAutoCreate(true);
		bp.setAD_Org_ID(AD_Org_ID);
		
		if (!bp.save())
			return "Update BPartner false!";
		
		return "Update Department, Warehouse, BPartner Success!";
	}

}

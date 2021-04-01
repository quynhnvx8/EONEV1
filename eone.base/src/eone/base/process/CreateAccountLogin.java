

package eone.base.process;

import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.VNCharacterUtils;

import eone.base.model.MBPGroup;
import eone.base.model.MBPartner;
import eone.base.model.MEmployee;
import eone.base.model.MUser;
import eone.base.model.MUserRoles;

public class CreateAccountLogin extends SvrProcess {

	/**
	 * Quynhnv.x8: Tien trinh tạo Account login he thong.
	 * 1. Tu dong tinh toan tao Account tranh bi trung account dang nhap da co.
	 * 2. Tao Account tren bang AD_User.
	 * 3. Phan quyen cho Account vua tao
	 */

	private int p_AD_Role_ID = -1;
	
	@Override
	protected void prepare() {
		//
		for (ProcessInfoParameter para : getParameter()) {
			String name = para.getParameterName();
			if ("AD_Role_ID".equals(name)) {
				p_AD_Role_ID  = para.getParameterAsInt();
			} else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
		MEmployee employee = MEmployee.get(getCtx(), getRecord_ID(), get_TrxName());
		String userName = createAutoAccount(employee.getName());
		employee.setUserName(userName);
		employee.saveEx(get_TrxName());
		
		MUser user = MUser.get(getCtx(), userName);
		MBPartner bpartner = null;
		if (user == null) {
			bpartner = createBPartner(employee);
			user = createAccountLogin(employee.getUserName(), employee.getName(), bpartner); 			
		}
		
		
		MUserRoles userRole = new MUserRoles(getCtx(), 0, 0, get_TrxName());
		userRole.setAD_User_ID(user.getAD_User_ID());
		userRole.setAD_Role_ID(p_AD_Role_ID);
		userRole.saveEx(get_TrxName());
		
		return "";
	}

	private MUser createAccountLogin(String userName, String name, MBPartner bpartner) {
		
		MUser user = new MUser(getCtx(), 0, get_TrxName());
		user.setValue(userName);
		user.setName(name);
		user.setPassword("1");
		user.setIsLocked(false);
		user.setIsNoExpire(false);
		user.setIsExpired(false);
		user.setIsUserAdmin(false);
		user.setIsUserSystem(false);
		user.setHR_Employee_ID(getRecord_ID());
		user.setC_BPartner_ID(bpartner.getC_BPartner_ID());
		user.save(get_TrxName());	
		return user;
	}
	
	private MBPartner createBPartner(MEmployee employee) {
		String sql = "Select C_BPartner_ID From C_BPartner Where HR_Employee_ID = ? And IsAutoCreate = 'Y' And IsEmployee_ID = 'Y'";
		int p_C_Bpartner_ID = DB.getSQLValue(get_TrxName(), sql, employee.getHR_Employee_ID());
		int C_BP_Group_ID = DB.getSQLValue(get_TrxName(), "Select C_BP_Group_ID From C_BP_Group Where GroupType = ?", MBPGroup.GROUPTYPE_Employee);
		MBPartner bpartner = null;
		if (p_C_Bpartner_ID > 0) {
			bpartner = MBPartner.get(getCtx(), p_C_Bpartner_ID);
			bpartner.setName(employee.getName());
		} else {
			bpartner = new MBPartner(getCtx(), 0, get_TrxName());
			bpartner.setAD_Org_ID(employee.getAD_Org_ID());
			bpartner.setAD_Department_ID(employee.getAD_Department_ID());
			bpartner.setValue(employee.getUserName());
			bpartner.setName(employee.getName());
			bpartner.setC_BP_Group_ID(C_BP_Group_ID);
			bpartner.setIsActive(true);
			bpartner.setIsEmployee(true);
			bpartner.setIsAutoCreate(true);
			bpartner.setHR_Employee_ID(employee.getHR_Employee_ID());
		}
		
		bpartner.saveEx(get_TrxName());
		
		return bpartner;
		
	}
	
	private String createAutoAccount(String nameEmployee) {
		String [] names = VNCharacterUtils.removeAccents(nameEmployee).split(" ");
		int n = names.length;
		String name = "";
		String prefix = "";
		for (int i = 0; i < n; i ++) {
			if (i == n - 1) {
				name = names[i];
			} else {
				prefix = prefix + names[i].substring(0,1).toLowerCase();
			}			
		}
		name = name + prefix;
		String sql = "Select max(value) From AD_User Where Value like ?";
		String oldValue = DB.getSQLValueString(get_TrxName(), sql, name + "%");
		String last = null;
		int number = 0;
		if (oldValue != null && name.length() != oldValue.length()) {
			last = oldValue.substring(name.length());
		} else if (oldValue != null) {
			number = 1;
		}
		
		
		if (last != null && !last.isEmpty()) {
			number = Integer.parseInt(last) + 1;
		}
		if (number > 0) {
			name = name + number;
		}
		return name.toLowerCase();
	}

}

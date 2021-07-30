
package eone.webui.panel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.compiere.util.Callback;
import org.compiere.util.Env;
import org.compiere.util.ItemDisplayLogic;
import org.compiere.util.ItemMandatory;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Language;
import org.compiere.util.Login;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;
import org.zkoss.zk.au.out.AuFocus;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Deferrable;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;

import eone.base.model.MBPartner;
import eone.base.model.MDepartment;
import eone.base.model.MEmployee;
import eone.base.model.MRole;
import eone.base.model.MSysConfig;
import eone.base.model.MUser;
import eone.webui.AdempiereIdGenerator;
import eone.webui.LayoutUtils;
import eone.webui.component.ComboItem;
import eone.webui.component.Combobox;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.Label;
import eone.webui.component.Window;
import eone.webui.editor.WDateEditor;
import eone.webui.session.SessionManager;
import eone.webui.theme.ITheme;
import eone.webui.util.UserPreference;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;
import eone.webui.window.LoginWindow;

/**
 *
 * @author Qunhnv.x8
 * @date 28/10/2020
 */
public class RolePanel extends Window implements EventListener<Event>, Deferrable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4486118071892173802L;

	protected LoginWindow wndLogin;
	protected Login login;

	protected Combobox lstRole, lstClient, lstOrganisation;
	protected Label lblRole, lblClient, lblOrganisation;
	protected WDateEditor lstDate;

	/** Context */
	protected Properties m_ctx;
	/** Username */
	protected String m_userName;
	/** Password */
	protected KeyNamePair[] m_clientKNPairs;

	protected UserPreference m_userpreference = null;

	protected boolean m_show = true;

	private RolePanel component;
	protected Button btnOk, btnCancel;

	private boolean isChangeRole = false;

	public boolean isChangeRole() {
		return isChangeRole;
	}

	public void setChangeRole(boolean isChangeRole) {
		this.isChangeRole = isChangeRole;
	}

	// backup old value
	private Properties ctxBeforeChangeRole = null;

	private static final String ON_DEFER_LOGOUT = "onDeferLogout";

	public RolePanel(Properties ctx, LoginWindow loginWindow, String userName, boolean show,
			KeyNamePair[] clientsKNPairs) {
		this.wndLogin = loginWindow;
		m_ctx = ctx;
		m_userName = userName;
		login = new Login(ctx);
		m_show = show;
		m_clientKNPairs = clientsKNPairs;

		if (m_clientKNPairs.length == 1 && !m_show) {
			Env.setContext(m_ctx, "#AD_Client_ID", (String) m_clientKNPairs[0].getID());
			MUser user = MUser.get(m_ctx, m_userName);
			m_userpreference = new UserPreference();
			m_userpreference.loadPreference(user.get_ID());
		}

		initComponents();
		init();
		this.setId("rolePanel");
		this.setSclass("login-box");

		if (!m_show) {
			// check if all mandatory fields are ok to not show
			if (lstRole.getSelectedItem() == null || lstRole.getSelectedItem().getValue() == null
					|| lstClient.getSelectedItem() == null || lstClient.getSelectedItem().getValue() == null
					|| lstOrganisation.getSelectedItem() == null
					|| lstOrganisation.getSelectedItem().getValue() == null) {
				m_show = true;
			}
		}
		if (m_show) {
			AuFocus auf = null;
			if (lstClient.getItemCount() > 1) {
				auf = new AuFocus(lstClient);
			} else if (lstRole.getItemCount() > 1) {
				auf = new AuFocus(lstRole);
			} else {
				auf = new AuFocus(lstOrganisation);
			}
			Clients.response(auf);
		} else {
			validateRoles();
		}
	}

	private void init() {
		Clients.response(new AuScript("zAu.cmd0.clearBusy()"));
		createUI();
	}

	protected Label lblTextBegin = new Label(ITheme.ZK_COPYRIGHT);

	protected void createUI() {
		Div divLogin = new Div();
		divLogin.setSclass("login-div");

		this.appendChild(divLogin);

		// Image
		Div divImageSologan = new Div();
		divImageSologan.setSclass("sologan-div");
		divLogin.appendChild(divImageSologan);

		// Box login
		Div divBox = new Div();
		divBox.setSclass("log-three-box");

		// Client
		Div divClient = new Div();
		divClient.setSclass("login-common");
		Div divLabelClient = new Div();
		divLabelClient.setSclass("role-label-up-div");
		lblClient.setZclass("role-text");
		divLabelClient.appendChild(lblClient);

		Div divLstClient = new Div();
		divLstClient.setSclass("login-txt");
		divLstClient.appendChild(lstClient);
		divClient.appendChild(divLabelClient);
		divClient.appendChild(divLstClient);

		divBox.appendChild(divClient);
		// Role
		Div divRole = new Div();
		divRole.setSclass("login-common");
		Div divLabelRole = new Div();
		divLabelRole.setSclass("role-label-up-div");
		lblRole.setZclass("role-text");
		divLabelRole.appendChild(lblRole);

		Div divLstRole = new Div();
		divLstRole.setSclass("login-txt");
		divLstRole.appendChild(lstRole);
		divRole.appendChild(divLabelRole);
		divRole.appendChild(divLstRole);

		divBox.appendChild(divRole);

		// Org
		Div divOrg = new Div();
		divOrg.setSclass("login-common");
		Div divLabelOrg = new Div();
		divLabelOrg.setSclass("role-label-up-div");
		lblOrganisation.setZclass("role-text");
		divLabelOrg.appendChild(lblOrganisation);

		Div divLstOrg = new Div();
		divLstOrg.setSclass("login-txt");
		divLstOrg.appendChild(lstOrganisation);
		divOrg.appendChild(divLabelOrg);
		divOrg.appendChild(divLstOrg);

		divBox.appendChild(divOrg);

		divLogin.appendChild(divBox);

		ConfirmPanel pnlButtons = new ConfirmPanel(true, false, false, false, false, false, true);
		pnlButtons.addActionListener(this);
		Button okBtn = pnlButtons.getButton(ConfirmPanel.A_OK);
		okBtn.setWidgetListener("onClick", "zAu.cmd0.showBusy(null)");

		LayoutUtils.addSclass(ITheme.LOGIN_BOX_FOOTER_PANEL_CLASS, pnlButtons);
		pnlButtons.setWidth(null);
		pnlButtons.getButton(ConfirmPanel.A_OK).setSclass(ITheme.LOGIN_BUTTON_CLASS);
		pnlButtons.getButton(ConfirmPanel.A_CANCEL).setSclass(ITheme.LOGIN_BUTTON_CLASS);

		btnOk.setSclass("login-button");
		btnCancel.setSclass("login-button");

		// Button
		Div divButton = new Div();
		divButton.setSclass("role-button");
		divButton.appendChild(btnOk);
		divButton.appendChild(btnCancel);

		divBox.appendChild(divButton);

		Div divFooter = new Div();
		divFooter.setSclass("login-footer");

		// image
		Div divImage = new Div();
		divImage.setSclass("login-image-footer");

		divFooter.appendChild(divImage);

		// Text
		Div divText = new Div();
		divText.setSclass("login-text-footer");

		lblTextBegin.setZclass("login-text-comon-login");
		divText.appendChild(lblTextBegin);

		divFooter.appendChild(divText);

		divLogin.appendChild(divFooter);
	}

	private void initComponents() {
		Language language = Env.getLanguage(m_ctx);

		lblClient = new Label();
		lblClient.setId("lblClient");
		lblClient.setValue(Msg.getMsg(language, "Client"));

		lblRole = new Label();
		lblRole.setId("lblRole");
		lblRole.setValue(Msg.getMsg(language, "Role"));
		
		lblOrganisation = new Label();
		lblOrganisation.setId("lblOrganisation");
		lblOrganisation.setValue(Msg.getMsg(language, "Organization"));
		
		lstRole = new Combobox();
		lstRole.setAutocomplete(true);
		lstRole.setAutodrop(true);
		lstRole.setId("lstRole");

		lstRole.addEventListener(Events.ON_SELECT, this);
		ZKUpdateUtil.setWidth(lstRole, "220px");

		lstClient = new Combobox();
		lstClient.setAutocomplete(true);
		lstClient.setAutodrop(true);
		lstClient.setId("lstClient");

		lstClient.addEventListener(Events.ON_SELECT, this);
		ZKUpdateUtil.setWidth(lstClient, "220px");

		lstOrganisation = new Combobox();
		lstOrganisation.setAutocomplete(true);
		lstOrganisation.setAutodrop(true);
		lstOrganisation.setId("lstOrganisation");

		lstOrganisation.addEventListener(Events.ON_SELECT, this);
		ZKUpdateUtil.setWidth(lstOrganisation, "220px");
		
		
		 btnOk = new Button();
        btnOk.setId("btnOk");
        btnOk.setLabel("Ok");
        btnOk.addEventListener("onClick", this);

        btnCancel = new Button();
        btnCancel.setId("btnCancel");
        btnCancel.setLabel("Cancel");
        btnCancel.addEventListener("onClick", this);
		
		UserPreference userPreference = SessionManager.getSessionApplication().getUserPreference();
		String initDefault = userPreference.getProperty(UserPreference.P_CLIENT);
		if (initDefault.length() == 0 && !m_show && m_userpreference != null) {
			initDefault = m_userpreference.getProperty(UserPreference.P_CLIENT);
		}
		if (m_clientKNPairs != null && m_clientKNPairs.length > 0) {
			for (int i = 0; i < m_clientKNPairs.length; i++) {
				ComboItem ci = new ComboItem(m_clientKNPairs[i].getName(), m_clientKNPairs[i].getID());
				String id = AdempiereIdGenerator.escapeId(ci.getLabel());
				if (lstClient.getFellowIfAny(id) == null)
					ci.setId(id);
				lstClient.appendChild(ci);
				if (m_clientKNPairs[i].getID().equals(initDefault))
					lstClient.setSelectedItem(ci);
			}
			if (lstClient.getSelectedIndex() == -1 && lstClient.getItemCount() > 0) {
				m_show = true; // didn't find default client
				lstClient.setSelectedIndex(0);
			}
		}
		//

		if (m_clientKNPairs != null && m_clientKNPairs.length == 1) {
			// disable client if is just one
			lstClient.setSelectedIndex(0);
			lstClient.setEnabled(false);
		} else {
			lstClient.setEnabled(true);
		}

		// Disable date combo-box at login screen
		if (!MSysConfig.getBooleanValue(MSysConfig.ALogin_ShowDate, true)) {
			lstDate.setReadWrite(false);
		}

		setUserID();
		updateRoleList();

		this.component = this;
		component.addEventListener(ON_DEFER_LOGOUT, this);
	}

	private void updateRoleList() {
		lstRole.getItems().clear();
		lstRole.setText("");
		Comboitem lstItemClient = lstClient.getSelectedItem();
		if (lstItemClient != null) {
			// initial role
			UserPreference userPreference = SessionManager.getSessionApplication().getUserPreference();
			String initDefault = userPreference.getProperty(UserPreference.P_ROLE);
			if (initDefault.length() == 0 && !m_show && m_userpreference != null) {
				initDefault = m_userpreference.getProperty(UserPreference.P_ROLE);
			}
			KeyNamePair clientKNPair = new KeyNamePair(Integer.valueOf((String) lstItemClient.getValue()),
					lstItemClient.getLabel());
			KeyNamePair roleKNPairs[] = login.getRoles(m_userName, clientKNPair, LoginPanel.ROLE_TYPES_WEBUI);
			if (roleKNPairs != null && roleKNPairs.length > 0) {
				for (int i = 0; i < roleKNPairs.length; i++) {
					ComboItem ci = new ComboItem(roleKNPairs[i].getName(), roleKNPairs[i].getID());
					String id = AdempiereIdGenerator.escapeId(ci.getLabel());
					if (lstRole.getFellowIfAny(id) == null)
						ci.setId(id);
					lstRole.appendChild(ci);
					if (roleKNPairs[i].getID().equals(initDefault))
						lstRole.setSelectedItem(ci);
				}
				if (lstRole.getSelectedIndex() == -1 && lstRole.getItemCount() > 0) {
					m_show = true; // didn't find default role
					lstRole.setSelectedIndex(0);
				}
			}
			//

			// force reload of default role
			MRole.getDefault(m_ctx, true);

			// If we have only one role, we can make readonly the combobox
			if (lstRole.getItemCount() == 1) {
				lstRole.setSelectedIndex(0);
				lstRole.setEnabled(false);
			} else {
				lstRole.setEnabled(true);
			}
		}
		setUserID();
		updateOrganisationList();
	}

	private void updateOrganisationList() {
		lstOrganisation.getItems().clear();
		lstOrganisation.setText("");
		Comboitem lstItemRole = lstRole.getSelectedItem();
		if (lstItemRole != null) {
			// initial organisation - Elaine 2009/02/06
			UserPreference userPreference = SessionManager.getSessionApplication().getUserPreference();
			String initDefault = userPreference.getProperty(UserPreference.P_ORG);
			if (initDefault.length() == 0 && !m_show && m_userpreference != null) {
				initDefault = m_userpreference.getProperty(UserPreference.P_ORG);
			}
			KeyNamePair RoleKNPair = new KeyNamePair(Integer.valueOf((String) lstItemRole.getValue()), lstItemRole.getLabel());
			
			KeyNamePair orgKNPairs[] = login.getOrgs(RoleKNPair);
			if (orgKNPairs != null && orgKNPairs.length > 0) {
				for (int i = 0; i < orgKNPairs.length; i++) {
					if (Integer.valueOf(orgKNPairs[i].getID()) == 0 && Integer.valueOf(lstClient.getSelectedItem().getValue()) != 0) {
						continue;
					}
					ComboItem ci = new ComboItem(orgKNPairs[i].getName(), orgKNPairs[i].getID());
					String id = AdempiereIdGenerator.escapeId(ci.getLabel());
					if (lstOrganisation.getFellowIfAny(id) == null)
						ci.setId(id);
					lstOrganisation.appendChild(ci);
					if (orgKNPairs[i].getID().equals(initDefault))
						lstOrganisation.setSelectedItem(ci);

				}
				if (lstOrganisation.getSelectedIndex() == -1 && lstOrganisation.getItemCount() > 0) {
					m_show = true; // didn't find default organisation
					lstOrganisation.setSelectedIndex(0);
				}
			}

			// If we have only one org, we can make readonly the combobox
			if (lstOrganisation.getItemCount() == 1) {
				lstOrganisation.setSelectedIndex(0);
				lstOrganisation.setEnabled(false);
			} else {
				lstOrganisation.setEnabled(true);
			}
			//
		}
		
	}

	

	public void onEvent(Event event) {
		String eventCompId = event.getTarget().getId();
		String eventName = event.getName();
		if (eventName.equals("onSelect")) {
			if (eventCompId.equals(lstClient.getId())) {
				updateRoleList();
			} else if (eventCompId.equals(lstRole.getId())) {
				setUserID();
				updateOrganisationList();
			} 
		}
		if (event.getTarget().getId().equals(ConfirmPanel.A_OK) || event.getTarget().getId().equals(btnOk.getId())) {
			setContextEnveronment();
			validateRoles();			
		} else if (event.getTarget().getId().equals(ConfirmPanel.A_CANCEL) || event.getTarget().getId().equals(btnCancel.getId())) {
			SessionManager.logoutSession();
            //wndLogin.loginCancelled();
		} else if (ON_DEFER_LOGOUT.equals(event.getName())) {
			SessionManager.logoutSession();
			// wndLogin.loginCancelled();
		}
		
	}

	private void setUserID() {
		if (lstClient.getSelectedItem() != null) {
			Env.setContext(m_ctx, "#AD_Client_ID", (String) lstClient.getSelectedItem().getValue());
		} else {
			Env.setContext(m_ctx, "#AD_Client_ID", (String) null);
		}
		MUser user = MUser.get(m_ctx, m_userName);
		if (user != null) {
			Env.setContext(m_ctx, "#AD_User_ID", user.getAD_User_ID());
			Env.setContext(m_ctx, "#AD_User_Name", user.getName());
			Env.setContext(m_ctx, "#SalesRep_ID", user.getAD_User_ID());
		}
	}

	/**
	 * show UI for change role
	 * 
	 * @param ctx env context
	 */
	public void changeRole(Properties ctx) {
		ctxBeforeChangeRole = new Properties();
		ctxBeforeChangeRole.putAll(ctx);
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		lstClient.setValue(AD_Client_ID);
		updateRoleList();
		int AD_Role_ID = Env.getAD_Role_ID(ctx);
		lstRole.setValue(AD_Role_ID);
		updateOrganisationList();
		int AD_Org_ID = Env.getAD_Org_ID(ctx);
		lstOrganisation.setValue(AD_Org_ID);
		
	}

	/**
	 * validate Roles
	 *
	 **/
	public void validateRoles() {
		Clients.clearBusy();
		Comboitem lstItemRole = lstRole.getSelectedItem();
		Comboitem lstItemClient = lstClient.getSelectedItem();
		Comboitem lstItemOrg = lstOrganisation.getSelectedItem();
		
		if (lstItemRole == null || lstItemRole.getValue() == null) {
			throw new WrongValueException(lstRole, Msg.getMsg(m_ctx, "FillMandatory") + lblRole.getValue());
		} else if (lstItemClient == null || lstItemClient.getValue() == null) {
			throw new WrongValueException(lstClient, Msg.getMsg(m_ctx, "FillMandatory") + lblClient.getValue());
		} else if (lstItemOrg == null || lstItemOrg.getValue() == null) {
			throw new WrongValueException(lstOrganisation,
					Msg.getMsg(m_ctx, "FillMandatory") + lblOrganisation.getValue());
		}
		int orgId = 0;
		orgId = Integer.parseInt((String) lstItemOrg.getValue());
		KeyNamePair orgKNPair = new KeyNamePair(orgId, lstItemOrg.getLabel());
		


		String msg = login.loadPreferences(orgKNPair);
		if (Util.isEmpty(msg)) {

			Session currSess = Executions.getCurrent().getDesktop().getSession();
			HttpSession httpSess = (HttpSession) currSess.getNativeSession();
			int timeout = MSysConfig.getIntValue(MSysConfig.ZK_SESSION_TIMEOUT_IN_SECONDS, -2,
					Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx()));
			if (timeout != -2) // default to -2 meaning not set
				httpSess.setMaxInactiveInterval(timeout);

			msg = login.validateLogin(orgKNPair);
		}
		if (!Util.isEmpty(msg)) {
			Env.getCtx().clear();
			FDialog.error(0, this, "Error", msg, new Callback<Integer>() {
				@Override
				public void onCallback(Integer result) {
					Events.echoEvent(new Event(ON_DEFER_LOGOUT, component));
				}
			});
			return;
		}

		int notifyDay = MSysConfig.getIntValue(MSysConfig.USER_LOCKING_PASSWORD_NOTIFY_DAY, 0);
		int pwdAgeDay = MSysConfig.getIntValue(MSysConfig.USER_LOCKING_MAX_PASSWORD_AGE_DAY, 0);
		if (notifyDay > 0 && pwdAgeDay > 0) {
			Timestamp limit = TimeUtil.addDays(MUser.get(Env.getCtx()).getDatePasswordChanged(), pwdAgeDay);
			Timestamp notifyAfter = TimeUtil.addDays(limit, -notifyDay);
			Timestamp now = TimeUtil.getDay(null);

			if (now.after(notifyAfter))
				FDialog.warn(0, null, "", Msg.getMsg(Env.getCtx(), "YourPasswordWillExpireInDays",
						new Object[] { TimeUtil.getDaysBetween(now, limit) }));
		}

		wndLogin.loginCompleted();

		// Elaine 2009/02/06 save preference to AD_Preference
		UserPreference userPreference = SessionManager.getSessionApplication().getUserPreference();
		userPreference.setProperty(UserPreference.P_LANGUAGE, Env.getContext(m_ctx, UserPreference.LANGUAGE_NAME));
		userPreference.setProperty(UserPreference.P_ROLE, (String) lstItemRole.getValue());
		userPreference.setProperty(UserPreference.P_CLIENT, (String) lstItemClient.getValue());
		userPreference.setProperty(UserPreference.P_ORG, (String) lstItemOrg.getValue());
		userPreference.savePreference();
		//
	}

	public boolean isDeferrable() {
		return false;
	}
	
	
	@SuppressWarnings("deprecation")
	public void setContextEnveronment() {
		Comboitem lstItemRole = lstRole.getSelectedItem();
		MRole role = new MRole(Env.getCtx(), new Integer((String) lstItemRole.getValue()), null);
		MUser user = MUser.get(m_ctx, m_userName);// get user login
		MBPartner bpartner = null; 
		if (user.getC_BPartner_ID() > 0)
			bpartner = new MBPartner(m_ctx, user.getC_BPartner_ID(), null);
		Comboitem lstItemOrg = lstOrganisation.getSelectedItem();
		if (lstItemOrg == null)
			throw new WrongValueException(lstRole, Msg.getMsg(m_ctx, "Role not exits Org. ") + lstRole.getValue());
		int AD_Org_ID = new Integer((String) lstItemOrg.getValue());
		
		Comboitem lstItemClient = lstClient.getSelectedItem();
		int AD_Client_ID = new Integer((String) lstItemClient.getValue());
		
		Env.setContext(m_ctx, "#IsUserAdmin", user.isUserAdmin()); 		//Duoc cau hinh 1 so chuc nang cua cong ty.
		Env.setContext(m_ctx, "#IsUserSystem", user.isUserSystem()); 	//Duoc cau hinh 1 so chuc nang cua he thong.
		//Date
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String date = simpleDateFormat.format(ts);
		Env.setContext(m_ctx, "#Date", date + " 00:00:00");
		Env.setContext(m_ctx, "#Day", new SimpleDateFormat("dd").format(ts));
		Env.setContext(m_ctx, "#Month", new SimpleDateFormat("MM").format(ts));
		Env.setContext(m_ctx, "#Year", new SimpleDateFormat("yyyy").format(ts));
		Env.setContext(m_ctx, "#FDate", TimeUtil.getDayFirstMonth(ts));
		Env.setContext(m_ctx, "#LDate", TimeUtil.getDayLastMonth(ts));
		Env.setContext(m_ctx, "#FYDate", TimeUtil.getDayFirstYear(ts));
		Env.setContext(m_ctx, "#LYDate", TimeUtil.getDayLastYear(ts));
		
		Env.setContext(m_ctx, "#IsShowPrice", role.isShowPrice());
		Env.setContext(m_ctx, "#IsConfigAcct", role.isConfigAcct());
		Env.setContext(m_ctx, "#ShowAcct", role.isShowAcct() ? Env.YES: Env.NO);
		Env.setContext(m_ctx, "#AD_Role_ID", role.getAD_Role_ID());
		Env.setContext(m_ctx, "#AD_Role_Name", role.getName());
		
		Env.setContext(m_ctx, "#RoleType", role.getRoleType());
		Env.setContext(m_ctx, "#RoleLevel", role.getRoleLevel());
		
		Env.setContext(m_ctx, "#AD_User_ID", user.getAD_User_ID());
		Env.setContext(m_ctx, "#AD_User_Name", user.getName() );
		Env.setContext(m_ctx, "#AD_User_Value", user.getValue());
		Env.setContext(m_ctx, "#AD_Org_ID", AD_Org_ID);
		Env.setContext(m_ctx, "#AD_Org_Name", lstItemOrg.getLabel());
		
		Env.setContext(m_ctx, "#AD_Client_ID", user.isUserSystem()? AD_Client_ID : user.getAD_Client_ID());
		Env.setContext(m_ctx, "#AD_Client_Name", lstItemClient.getLabel());
		if (bpartner != null)
		{
			Env.setContext(m_ctx, "#C_BPartner_ID", bpartner.getC_BPartner_ID());
			Env.setContext(m_ctx, "#HR_Employee_ID", bpartner.getHR_Employee_ID());			
		}
		MDepartment dept = null;
		if(user.getAD_Department_ID() > 0)
		{
			Env.setContext(m_ctx, "#AD_Department_ID", user.getAD_Department_ID());
			dept = MDepartment.get(Env.getCtx(), user.getAD_Department_ID());
			if (dept != null)
				Env.setContext(m_ctx, "#AD_Department_Name", dept.getName());
		} else if (bpartner != null) {
			Env.setContext(m_ctx, "#AD_Department_ID", bpartner.getAD_Department_ID());
			dept = MDepartment.get(Env.getCtx(), bpartner.getAD_Department_ID());
			if (dept != null)
				Env.setContext(m_ctx, "#AD_Department_Name", dept.getName());
		} else if (user.getHR_Employee_ID() > 0) {
			MEmployee e = MEmployee.get(m_ctx, user.getHR_Employee_ID(), null);
			Env.setContext(m_ctx, "#AD_Department_ID", e.getAD_Department_ID());
			dept = MDepartment.get(Env.getCtx(), e.getAD_Department_ID());
			if (dept != null)
				Env.setContext(m_ctx, "#AD_Department_Name", dept.getName());
		}
		
		ItemDisplayLogic 	itemDis = new ItemDisplayLogic();
		ItemMandatory 		itemMan = new ItemMandatory();
		
		login.getInfoClient(AD_Client_ID, itemDis);
		login.loadContextElementValue(AD_Client_ID, itemMan);
		
		login.loadDocType();
		
		setContextExtend(itemDis, itemMan);
		//
	}
	
	public void setContextExtend(ItemDisplayLogic itemDis, ItemMandatory itemMan) {
		//Displaylogic		
		Env.setContext(m_ctx, "#IsContract", 			itemDis.getIsContract());
		Env.setContext(m_ctx, "#IsContractLine",		itemDis.getIsContractLine());
		Env.setContext(m_ctx, "#IsProject", 			itemDis.getIsProject());
		Env.setContext(m_ctx, "#IsProjectLine", 		itemDis.getIsProjectLine());
		Env.setContext(m_ctx, "#IsConstruction", 		itemDis.getIsConstruction());
		Env.setContext(m_ctx, "#IsConstructionLine", 	itemDis.getIsConstructionLine());
		Env.setContext(m_ctx, "#IsProduct", 			itemDis.getIsProduct());
		Env.setContext(m_ctx, "#IsGroup", 				itemDis.getIsGroup());
		Env.setContext(m_ctx, "#IsMoreCurrency", 		itemDis.getIsMoreCurrency());
		Env.setContext(m_ctx, "#C_Element_ID", 			itemDis.getElement());
		Env.setContext(m_ctx, "#C_CurrencyDefault_ID", 	itemDis.getCurrenctyDefault());
		Env.setContext(m_ctx, "#MaterialPolicy", 		itemDis.getMaterialPolicy());
		
		Env.DisProduct 				= itemDis.getIsProduct()!= null 			&& itemDis.getIsProduct().equals(Env.YES) ? true : false;
		Env.DisContract 			= itemDis.getIsContract() != null 			&& itemDis.getIsContract().equals(Env.YES) ? true : false;
		Env.DisContractLine 		= itemDis.getIsContractLine() != null 		&& itemDis.getIsContractLine().equals(Env.YES) ? true : false;
		Env.DisProject 				= itemDis.getIsProject() != null 			&& itemDis.getIsProject().equals(Env.YES) ? true : false;
		Env.DisProjectLine 			= itemDis.getIsProjectLine() != null 		&& itemDis.getIsProjectLine().equals(Env.YES) ? true : false;
		Env.DisConstruction 		= itemDis.getIsConstruction() != null 		&& itemDis.getIsConstruction().equals(Env.YES) ? true : false;
		Env.DisConstructionLine 	= itemDis.getIsConstructionLine() != null 	&& itemDis.getIsConstructionLine().equals(Env.YES) ? true : false;
		
		
		
		//MandatoryLogic
		
		Env.setContext(m_ctx, "#BankAccount", 		itemMan.getBankAccount());
		Env.setContext(m_ctx, "#Asset", 			itemMan.getAsset());
		Env.setContext(m_ctx, "#BPartner", 			itemMan.getBpartner());
		Env.setContext(m_ctx, "#Product", 			itemMan.getProduct());
		Env.setContext(m_ctx, "#Project", 			itemMan.getProject());
		Env.setContext(m_ctx, "#ProjectLine", 		itemMan.getProjectLine());
		Env.setContext(m_ctx, "#Contract", 			itemMan.getContract());
		Env.setContext(m_ctx, "#ContractLine", 		itemMan.getContractLine());
		Env.setContext(m_ctx, "#Construction", 		itemMan.getConstruction());
		Env.setContext(m_ctx, "#ConstructionLine",  itemMan.getConstructionLine());
		Env.setContext(m_ctx, "#Warehouse", 		itemMan.getWarehouse());
		Env.setContext(m_ctx, "#TypeCost", 			itemMan.getTypeCost());
		Env.setContext(m_ctx, "#TypeRevenue", 		itemMan.getTypeRevenue());
		
	}
	
}

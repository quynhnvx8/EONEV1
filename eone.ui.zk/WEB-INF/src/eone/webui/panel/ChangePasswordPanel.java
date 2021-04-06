package eone.webui.panel;

import java.util.ArrayList;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;
import org.compiere.util.Callback;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

import eone.base.model.MPasswordHistory;
import eone.base.model.MPasswordRule;
import eone.base.model.MSysConfig;
import eone.base.model.MUser;
import eone.webui.LayoutUtils;
import eone.webui.apps.AEnv;
import eone.webui.component.Combobox;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.Label;
import eone.webui.component.Messagebox;
import eone.webui.component.Textbox;
import eone.webui.component.Window;
import eone.webui.session.SessionManager;
import eone.webui.theme.ITheme;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.LoginWindow;

public class ChangePasswordPanel extends Window implements EventListener<Event>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4117126419866788951L;

	private static final CLogger logger = CLogger.getCLogger(ChangePasswordPanel.class);

    protected LoginWindow wndLogin;

    /** Context					*/
    protected Properties      m_ctx;
    /** Username					*/
    protected String			m_userName;
    /** Password					*/
    protected String			m_userPassword;
    protected KeyNamePair[]	m_clientKNPairs;

    protected boolean m_show = true;
	
    protected Label lblOldPassword;
    protected Label lblNewPassword;
    protected Label lblRetypeNewPassword;
    protected Label lblSecurityQuestion;
    protected Label lblAnswer;
    protected Combobox lstSecurityQuestion;
    protected Textbox txtOldPassword;
    protected Textbox txtNewPassword;
    protected Textbox txtRetypeNewPassword;
    protected Textbox txtAnswer;

    public ChangePasswordPanel(Properties ctx, LoginWindow loginWindow, String userName, String userPassword, boolean show, KeyNamePair[] clientsKNPairs) 
    {
    	this.wndLogin = loginWindow;
    	m_ctx = ctx;
    	m_userName = userName;
    	m_userPassword = userPassword;
    	m_show = show;
        m_clientKNPairs = clientsKNPairs;
    	
        initComponents();
        init();
        this.setId("changePasswordPanel");
        this.setSclass("login-box");        
    }

    private void init()
    {
    	createUI();
    }

    protected Label lblTextBegin = new Label(ITheme.ZK_COPYRIGHT);
    protected void createUI() {
		Div divLogin = new Div();
		divLogin.setSclass("login-div");		
		
		this.appendChild(divLogin);
		
		//Image
		Div divImageSologan = new Div();
		divImageSologan.setSclass("sologan-div");		
		divLogin.appendChild(divImageSologan);
		
		//Box login	
		Div divBox = new Div();
		divBox.setSclass("log-three-box");
		
		//Old Password
		Div divOldPass = new Div();
		divOldPass.setSclass("login-common");
		Div divlblOldPass = new Div();
		divlblOldPass.setSclass("role-label-up-div");
		lblOldPassword.setZclass("role-text");
		divlblOldPass.appendChild(lblOldPassword);
		
		Div divTxtOldPass = new Div();
		divTxtOldPass.setSclass("login-txt");
		divTxtOldPass.appendChild(txtOldPassword);
		divOldPass.appendChild(divlblOldPass);
		divOldPass.appendChild(divTxtOldPass);		
		divBox.appendChild(divOldPass);
		
		//Password
		Div divPassword = new Div();
		divPassword.setSclass("login-common");
		Div divLabelPassword = new Div();
		divLabelPassword.setSclass("role-label-up-div");
		lblNewPassword.setZclass("role-text");
		divLabelPassword.appendChild(lblNewPassword);
		
		Div divTxtPassword = new Div();
		divTxtPassword.setSclass("login-txt");
		divTxtPassword.appendChild(txtNewPassword);
		divPassword.appendChild(divLabelPassword);
		divPassword.appendChild(divTxtPassword);		
		divBox.appendChild(divPassword);
			
		//Password
		Div divRePassword = new Div();
		divRePassword.setSclass("login-common");
		Div divReLabelPassword = new Div();
		divReLabelPassword.setSclass("role-label-up-div");
		lblNewPassword.setZclass("role-text");
		divReLabelPassword.appendChild(lblRetypeNewPassword);
		
		Div divReTxtPassword = new Div();
		divReTxtPassword.setSclass("login-txt");
		divReTxtPassword.appendChild(txtRetypeNewPassword);
		divRePassword.appendChild(divReLabelPassword);
		divRePassword.appendChild(divReTxtPassword);		
		divBox.appendChild(divRePassword);
		
		divLogin.appendChild(divBox);
		
    	Div divButton = new Div();
        divButton.setSclass("login-common");
        
        ConfirmPanel pnlButtons = new ConfirmPanel(true);
        pnlButtons.addActionListener(this);
        LayoutUtils.addSclass(ITheme.LOGIN_BOX_FOOTER_PANEL_CLASS, pnlButtons);
        ZKUpdateUtil.setWidth(pnlButtons, null);
        pnlButtons.getButton(ConfirmPanel.A_OK).setSclass(ITheme.LOGIN_BUTTON_CLASS);
        pnlButtons.getButton(ConfirmPanel.A_CANCEL).setSclass(ITheme.LOGIN_BUTTON_CLASS);
        divButton.appendChild(pnlButtons);
        
        divBox.appendChild(divButton);
        
        Div divFooter = new Div();
        divFooter.setSclass("login-footer");
        
    	//image
    	Div divImage = new Div();
    	divImage.setSclass("login-image-footer");
    	
    	divFooter.appendChild(divImage);
    	
    	//Text
    	Div divText = new Div();
    	divText.setSclass("login-text-footer"); 
    	
    	lblTextBegin.setZclass("login-text-comon-login");
        divText.appendChild(lblTextBegin);
    	
    	divFooter.appendChild(divText);
        
        divLogin.appendChild(divFooter);
	}
    
	

    private void initComponents()
    {
    	lblOldPassword = new Label();
    	lblOldPassword.setId("lblOldPassword");
    	lblOldPassword.setValue(Msg.getMsg(m_ctx, "Old Password"));

        lblNewPassword = new Label();
        lblNewPassword.setId("lblNewPassword");
        lblNewPassword.setValue(Msg.getMsg(m_ctx, "New Password"));
        
        lblRetypeNewPassword = new Label();
        lblRetypeNewPassword.setId("lblRetypeNewPassword");
        lblRetypeNewPassword.setValue(Msg.getMsg(m_ctx, "New Password Confirm"));
       
        txtOldPassword = new Textbox();
        txtOldPassword.setId("txtOldPassword");
        txtOldPassword.setType("password");
        txtOldPassword.setCols(25);
        ZKUpdateUtil.setWidth(txtOldPassword, "220px");

        txtNewPassword = new Textbox();
        txtNewPassword.setId("txtNewPassword");
        txtNewPassword.setType("password");
        txtNewPassword.setCols(25);
        txtNewPassword.addEventListener(Events.ON_BLUR, this);
        ZKUpdateUtil.setWidth(txtNewPassword, "220px");
        
        txtRetypeNewPassword = new Textbox();
        txtRetypeNewPassword.setId("txtRetypeNewPassword");
        txtRetypeNewPassword.setType("password");
        txtRetypeNewPassword.setCols(25);
        txtRetypeNewPassword.addEventListener(Events.ON_BLUR, this);
        ZKUpdateUtil.setWidth(txtRetypeNewPassword, "220px");
        
   }

    public void onEvent(Event event)
    {
        if (event.getTarget().getId().equals(ConfirmPanel.A_OK))
        {
			validateChangePassword();
        }
        else if (event.getTarget().getId().equals(ConfirmPanel.A_CANCEL))
        {
        	SessionManager.logoutSession();
        }
        else if (event.getTarget() == txtNewPassword) {
        	MPasswordRule pwdrule = MPasswordRule.getRules(Env.getCtx(), null);
			if (pwdrule != null) {
				try {
					pwdrule.validate(m_userName, txtNewPassword.getValue(), new ArrayList<MPasswordHistory>());
				}
				catch (Exception e) {
					throw new WrongValueException(txtNewPassword, e.getMessage());
				}
			}
        }
        else if (event.getTarget() == txtRetypeNewPassword) {
        	if (!txtNewPassword.getValue().equals(txtRetypeNewPassword.getValue()))
        		throw new WrongValueException(txtRetypeNewPassword, Msg.getMsg(m_ctx, "PasswordNotMatch"));
        }
    }
    
    public void validateChangePassword()
    {
    	Clients.clearBusy();
    	String oldPassword = txtOldPassword.getValue();
    	String newPassword = txtNewPassword.getValue();
    	String retypeNewPassword = txtRetypeNewPassword.getValue();
    	
    	if (Util.isEmpty(oldPassword))
    		throw new IllegalArgumentException(Msg.getMsg(m_ctx, "OldPasswordMandatory"));
    	
    	if (Util.isEmpty(retypeNewPassword))
    		throw new IllegalArgumentException(Msg.getMsg(m_ctx, "NewPasswordConfirmMandatory"));

    	if (!newPassword.equals(retypeNewPassword))
    		throw new IllegalArgumentException(Msg.getMsg(m_ctx, "PasswordNotMatch"));

    	if (!oldPassword.equals(m_userPassword))
    		throw new IllegalArgumentException(Msg.getMsg(m_ctx, "OldPasswordNoMatch"));
    	
    	if (MSysConfig.getBooleanValue(MSysConfig.CHANGE_PASSWORD_MUST_DIFFER, true))
    	{
    		if (oldPassword.equals(newPassword))
        		throw new IllegalArgumentException(Msg.getMsg(m_ctx, "NewPasswordMustDiffer"));
    	}

    	Trx trx = null;
    	try
    	{
        	String trxName = Trx.createTrxName("ChangePasswordTrx");
    		trx = Trx.get(trxName, true);
    		trx.setDisplayName(getClass().getName()+"_validateChangePassword");
    		
	    	for (KeyNamePair clientKNPair : m_clientKNPairs)
	    	{	    		
	    		int clientId = clientKNPair.getKey();
	    		Env.setContext(m_ctx, "#AD_Client_ID", clientId);
	    		MUser user = MUser.get(m_ctx, m_userName);
	    		if (user == null)
	    		{
	    			trx.rollback();
	    			logger.severe("Could not find user '" + m_userName + "'");
	    			throw new AdempiereException("Could not find user");
	    		}

				//user.set_ValueOfColumn("Password", newPassword);//08/03/2021: Bo phan nay set mat khau theo cach moi
	    		user.setPassword(newPassword);
	    		user.setIsExpired(false);
	    		user.saveEx(trx.getTrxName());
	    	}
	    	
	    	trx.commit();	    	
    	}
    	catch (AdempiereException e)
    	{
    		if (trx != null)
    			trx.rollback();
    		throw e;
    	}
    	finally
    	{
    		if (trx != null)
    			trx.close();
    	}
    	
		String msg = Msg.getMsg(m_ctx, "NewPasswordValidForAllTenants");
		Messagebox.showDialog(msg, AEnv.getDialogHeader(Env.getCtx(), 0), Messagebox.OK, Messagebox.INFORMATION, new Callback<Integer>() {
			@Override
			public void onCallback(Integer result) {
		    	wndLogin.loginOk(m_userName, m_show, m_clientKNPairs);
			}
			
		});
    }
}

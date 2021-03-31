/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.grid;

import java.util.logging.Level;

import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MBPartner;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Span;
import org.zkoss.zul.Vlayout;

/**
 * Business Partner : Based on VBPartner
 *
 * @author 	Niraj Sohun
 * 			Aug 15, 2007
 *
 */

@Deprecated /* use WQuickEntry instead */
public class WBPartner extends Window implements EventListener<Event>, ValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5842369060073088746L;

	private static final CLogger log = CLogger.getCLogger(WBPartner.class);
	
	private int m_WindowNo;
	
	/** The Partner				*/
	private MBPartner m_partner = null;
	
	/** The Location			*/
	
	/** The User				*/
	private MUser m_user = null;
	
	/** Read Only				*/
	private boolean m_readOnly = false;

	private Object[] m_greeting;
	
	private Textbox fValue = new Textbox();
	private Listbox fGreetingBP = new Listbox();
	private Textbox fName = new Textbox();
	private Textbox fName2 = new Textbox();
	private Textbox fContact = new Textbox();
	private Listbox fGreetingC = new Listbox();
	private Textbox fTitle = new Textbox();
	private Textbox fEMail = new Textbox();
	private Textbox fPhone = new Textbox();
	private Textbox fPhone2 = new Textbox();
	private Textbox fFax = new Textbox();
	
	
	private Vlayout centerPanel = new Vlayout();	
	
	private ConfirmPanel confirmPanel = new ConfirmPanel(true, false, false, false, false, false);
	
	/**
	 *	Constructor.
	 *	Requires call loadBPartner
	 * 	@param frame	parent
	 * 	@param WindowNo	Window No
	 */
	
	public WBPartner(int WindowNo)
	{
		super();
		
		m_WindowNo = WindowNo;
		m_readOnly = !MRole.getDefault().canUpdate(
			Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx()), 
			MBPartner.Table_ID, 0, false);
		log.info("R/O=" + m_readOnly);
		
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, ex.getMessage());
		}
		
		initBPartner();
		
	}	//	WBPartner
	
	/**
	 *	Static Init
	 * 	@throws Exception
	 */
	
	void jbInit() throws Exception
	{
		ZKUpdateUtil.setWidth(this, "350px");
		this.setBorder("normal");
		this.setClosable(true);
		this.setSizable(true);
		this.setTitle("Business Partner");		
		this.appendChild(centerPanel);
		this.appendChild(confirmPanel);
		ZKUpdateUtil.setWidth(confirmPanel, "100%");
		
		
		confirmPanel.addActionListener(Events.ON_CLICK, this);
	}
	
	/**
	 *	Dynamic Init
	 */
	private void initBPartner()
	{
		//	Get Data
		m_greeting = fillGreeting();

		//	Value
		fValue.addEventListener(Events.ON_CHANGE , this);
		createLine (fValue, "Value", true);
		
		//	Greeting Business Partner
		fGreetingBP.setMold("select");
		fGreetingBP.setRows(0);
		
		for (int i = 0; i < m_greeting.length; i++)
			fGreetingBP.appendItem(m_greeting[i].toString(), m_greeting[i]);
		createLine (fGreetingBP, "Greeting", false);
		
		//	Name
		fName.addEventListener(Events.ON_CLICK, this);
		createLine (fName, "Name", false)/*.setFontBold(true)*/;

		//	Name2
		createLine (fName2, "Name2", false);
		
		//	Contact
		createLine (fContact, "Contact", true)/*.setFontBold(true)*/;

		//	Greeting Contact
		fGreetingC.setMold("select");
		fGreetingC.setRows(0);
		
		for (int i = 0; i < m_greeting.length; i++)
			fGreetingC.appendItem(m_greeting[i].toString(), m_greeting[i]);
		
		createLine (fGreetingC, "Greeting", false);
		
		//	Title
		createLine(fTitle, "Title", false);

		//	Email
		createLine (fEMail, "EMail", false);
		
		//	Location
		
		
		
		//	Phone
		createLine (fPhone, "Phone", true);
		
		//	Phone2
		createLine (fPhone2, "Phone2", false);
		
		//	Fax
		createLine (fFax, "Fax", false);
	}	//	initBPartner

	/**
	 * 	Create Line
	 * 	@param field 	field
	 * 	@param title	label value
	 * 	@param addSpace	add more space
	 * 	@return label
	 */
	
	private Label createLine (Component field, String title, boolean addSpace)
	{
		Hlayout layout = new Hlayout(); 
		
		ZKUpdateUtil.setHflex(layout, "10");
		
		Label label = new Label(Msg.translate(Env.getCtx(), title));
		Span span = new Span();
		ZKUpdateUtil.setHflex(span, "3");
		layout.appendChild(span);
		span.appendChild(label);
		label.setSclass("field-label");

		layout.appendChild(field);
		ZKUpdateUtil.setHflex((HtmlBasedComponent)field, "7");
		
		centerPanel.appendChild(layout);
		centerPanel.appendChild(new Separator());
		
		return label;
	}	//	createLine

	/**
	 *	Fill Greeting
	 * 	@return KeyNamePair Array of Greetings
	 */
	
	private Object[] fillGreeting()
	{
		String sql = "SELECT C_Greeting_ID, Name FROM C_Greeting WHERE IsActive='Y' ORDER BY 2";
		sql = MRole.getDefault().addAccessSQL(sql, "C_Greeting", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		
		return DB.getKeyNamePairs(sql, true);
	}	//	fillGreeting

	
	
	public boolean loadBPartner (int C_BPartner_ID)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_BPartner_ID=" + C_BPartner_ID);
		
		//  New bpartner
		if (C_BPartner_ID == 0)
		{
			m_partner = null;
			m_user = null;
			return true;
		}

		m_partner = new MBPartner (Env.getCtx(), C_BPartner_ID, null);
		
		if (m_partner.get_ID() == 0)
		{
			FDialog.error(m_WindowNo, this, "BPartnerNotFound");
			return false;
		}

		//	BPartner - Load values
		fValue.setText(m_partner.getValue());
		
		
		
		fName.setText(m_partner.getName());
		fName2.setText(m_partner.getName2());

		
		//	User - Load values
		m_user = m_partner.getContact(
			Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_User_ID"));
		
		if (m_user != null)
		{
			
			fContact.setText(m_user.getName());
			fEMail.setText(m_user.getEMail());
			
		}
		return true;
	}	//	loadBPartner

	/**
	 *	Save.
	 *	Checks mandatory fields and saves Partner, Contact and Location
	 * 	@return true if saved
	 */
	
	private boolean actionSave()
	{
		log.config("");

		//	Check Mandatory fields
		if (fName.getText().equals(""))
		{
			throw new WrongValueException(fName, Msg.translate(Env.getCtx(), "FillMandatory"));
		}
		

		
		m_partner.setValue(fValue.getText());
		
		m_partner.setName(fName.getText());
		m_partner.setName2(fName2.getText());
		
		
		
		if (m_partner.save()) {
			if (log.isLoggable(Level.FINE)) log.fine("C_BPartner_ID=" + m_partner.getC_BPartner_ID());
		} else {
			FDialog.error(m_WindowNo, this, "BPartnerNotSaved");
			m_partner = null;
			return false;
		}
		
		
		//	***** Business Partner - User *****
		
		String contact = fContact.getText();
		String email = fEMail.getText();
		
		if (m_user == null && (contact.length() > 0 || email.length() > 0))
			m_user = new MUser (m_partner);
		
		if (m_user != null)
		{
			if (contact.length() == 0)
				contact = fName.getText();
		
			m_user.setName(contact);
			m_user.setEMail(email);
			
			
			if (m_user.save()) {
				if (log.isLoggable(Level.FINE)) log.fine("AD_User_ID=" + m_user.getAD_User_ID());
			} else {
				FDialog.error(m_WindowNo, this, "BPartnerNotSaved", Msg.translate(Env.getCtx(), "AD_User_ID"));
			}
		}
		return true;
	}	//	actionSave

	/**
	 *	Returns BPartner ID
	 *	@return C_BPartner_ID (0 = not saved)
	 */
	
	public int getC_BPartner_ID()
	{
		if (m_partner == null)
			return 0;
		
		return m_partner.getC_BPartner_ID();
	}	//	getBPartner_ID

	public void onEvent(Event e) throws Exception 
	{
		if (m_readOnly)
			this.detach();
		
		//	copy value
		
		else if (e.getTarget() == fValue)
		{
			if (fName.getText() == null || fName.getText().length() == 0)
				fName.setText(fValue.getText());
		}
		else if (e.getTarget() == fName)
		{
			if (fContact.getText() == null || fContact.getText().length() == 0)
				fContact.setText(fName.getText());
		}
		
		//	OK pressed
		else if ((e.getTarget() == confirmPanel.getButton("Ok")) && actionSave())
			this.detach();
		
		//	Cancel pressed
		else if (e.getTarget() == confirmPanel.getButton("Cancel"))
			this.detach();
		
	}

	public void valueChange(ValueChangeEvent evt)
	{
		
	}
}
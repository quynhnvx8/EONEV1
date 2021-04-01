/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for AD_ClientInfo
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_ClientInfo extends PO implements I_AD_ClientInfo, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200804L;

    /** Standard Constructor */
    public X_AD_ClientInfo (Properties ctx, int AD_ClientInfo_ID, String trxName)
    {
      super (ctx, AD_ClientInfo_ID, trxName);
      /** if (AD_ClientInfo_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_AD_ClientInfo (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_AD_ClientInfo[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AD_ClientInfo_UU.
		@param AD_ClientInfo_UU AD_ClientInfo_UU	  */
	public void setAD_ClientInfo_UU (String AD_ClientInfo_UU)
	{
		set_Value (COLUMNNAME_AD_ClientInfo_UU, AD_ClientInfo_UU);
	}

	/** Get AD_ClientInfo_UU.
		@return AD_ClientInfo_UU	  */
	public String getAD_ClientInfo_UU () 
	{
		return (String)get_Value(COLUMNNAME_AD_ClientInfo_UU);
	}

	public eone.base.model.I_AD_StorageProvider getAD_StorageProvider() throws RuntimeException
    {
		return (eone.base.model.I_AD_StorageProvider)MTable.get(getCtx(), eone.base.model.I_AD_StorageProvider.Table_Name)
			.getPO(getAD_StorageProvider_ID(), get_TrxName());	}

	/** Set Storage Provider.
		@param AD_StorageProvider_ID Storage Provider	  */
	public void setAD_StorageProvider_ID (int AD_StorageProvider_ID)
	{
		if (AD_StorageProvider_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_StorageProvider_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_StorageProvider_ID, Integer.valueOf(AD_StorageProvider_ID));
	}

	/** Get Storage Provider.
		@return Storage Provider	  */
	public int getAD_StorageProvider_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_StorageProvider_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_Tree getAD_Tree_BPartner() throws RuntimeException
    {
		return (eone.base.model.I_AD_Tree)MTable.get(getCtx(), eone.base.model.I_AD_Tree.Table_Name)
			.getPO(getAD_Tree_BPartner_ID(), get_TrxName());	}

	/** Set BPartner Tree.
		@param AD_Tree_BPartner_ID 
		Trees are used for (financial) reporting
	  */
	public void setAD_Tree_BPartner_ID (int AD_Tree_BPartner_ID)
	{
		if (AD_Tree_BPartner_ID < 1) 
			set_Value (COLUMNNAME_AD_Tree_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Tree_BPartner_ID, Integer.valueOf(AD_Tree_BPartner_ID));
	}

	/** Get BPartner Tree.
		@return Trees are used for (financial) reporting
	  */
	public int getAD_Tree_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Tree_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_Tree getAD_Tree_Menu() throws RuntimeException
    {
		return (eone.base.model.I_AD_Tree)MTable.get(getCtx(), eone.base.model.I_AD_Tree.Table_Name)
			.getPO(getAD_Tree_Menu_ID(), get_TrxName());	}

	/** Set Menu Tree.
		@param AD_Tree_Menu_ID 
		Tree of the menu
	  */
	public void setAD_Tree_Menu_ID (int AD_Tree_Menu_ID)
	{
		if (AD_Tree_Menu_ID < 1) 
			set_Value (COLUMNNAME_AD_Tree_Menu_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Tree_Menu_ID, Integer.valueOf(AD_Tree_Menu_ID));
	}

	/** Get Menu Tree.
		@return Tree of the menu
	  */
	public int getAD_Tree_Menu_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Tree_Menu_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_Tree getAD_Tree_Org() throws RuntimeException
    {
		return (eone.base.model.I_AD_Tree)MTable.get(getCtx(), eone.base.model.I_AD_Tree.Table_Name)
			.getPO(getAD_Tree_Org_ID(), get_TrxName());	}

	/** Set Organization Tree.
		@param AD_Tree_Org_ID 
		Trees are used for (financial) reporting and security access (via role)
	  */
	public void setAD_Tree_Org_ID (int AD_Tree_Org_ID)
	{
		if (AD_Tree_Org_ID < 1) 
			set_Value (COLUMNNAME_AD_Tree_Org_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Tree_Org_ID, Integer.valueOf(AD_Tree_Org_ID));
	}

	/** Get Organization Tree.
		@return Trees are used for (financial) reporting and security access (via role)
	  */
	public int getAD_Tree_Org_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Tree_Org_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Days to keep Log.
		@param KeepLogDays 
		Number of days to keep the log entries
	  */
	public void setKeepLogDays (int KeepLogDays)
	{
		set_Value (COLUMNNAME_KeepLogDays, Integer.valueOf(KeepLogDays));
	}

	/** Get Days to keep Log.
		@return Number of days to keep the log entries
	  */
	public int getKeepLogDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KeepLogDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Logo.
		@param Logo_ID Logo	  */
	public void setLogo_ID (int Logo_ID)
	{
		if (Logo_ID < 1) 
			set_Value (COLUMNNAME_Logo_ID, null);
		else 
			set_Value (COLUMNNAME_Logo_ID, Integer.valueOf(Logo_ID));
	}

	/** Get Logo.
		@return Logo	  */
	public int getLogo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Logo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Logo Report.
		@param LogoReport_ID Logo Report	  */
	public void setLogoReport_ID (int LogoReport_ID)
	{
		if (LogoReport_ID < 1) 
			set_Value (COLUMNNAME_LogoReport_ID, null);
		else 
			set_Value (COLUMNNAME_LogoReport_ID, Integer.valueOf(LogoReport_ID));
	}

	/** Get Logo Report.
		@return Logo Report	  */
	public int getLogoReport_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LogoReport_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Logo Web.
		@param LogoWeb_ID Logo Web	  */
	public void setLogoWeb_ID (int LogoWeb_ID)
	{
		if (LogoWeb_ID < 1) 
			set_Value (COLUMNNAME_LogoWeb_ID, null);
		else 
			set_Value (COLUMNNAME_LogoWeb_ID, Integer.valueOf(LogoWeb_ID));
	}

	/** Get Logo Web.
		@return Logo Web	  */
	public int getLogoWeb_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LogoWeb_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_StorageProvider getStorageArchive() throws RuntimeException
    {
		return (eone.base.model.I_AD_StorageProvider)MTable.get(getCtx(), eone.base.model.I_AD_StorageProvider.Table_Name)
			.getPO(getStorageArchive_ID(), get_TrxName());	}

	/** Set Archive Store.
		@param StorageArchive_ID Archive Store	  */
	public void setStorageArchive_ID (int StorageArchive_ID)
	{
		if (StorageArchive_ID < 1) 
			set_Value (COLUMNNAME_StorageArchive_ID, null);
		else 
			set_Value (COLUMNNAME_StorageArchive_ID, Integer.valueOf(StorageArchive_ID));
	}

	/** Get Archive Store.
		@return Archive Store	  */
	public int getStorageArchive_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_StorageArchive_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
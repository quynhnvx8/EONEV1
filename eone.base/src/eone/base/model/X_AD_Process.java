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
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_Process
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_Process extends PO implements I_AD_Process, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201021L;

    /** Standard Constructor */
    public X_AD_Process (Properties ctx, int AD_Process_ID, String trxName)
    {
      super (ctx, AD_Process_ID, trxName);
      /** if (AD_Process_ID == 0)
        {
			setAccessLevel (null);
			setAD_Process_ID (0);
			setEntityType (null);
// @SQL=select get_sysconfig('DEFAULT_ENTITYTYPE','U',0,0) from dual
			setIsReport (false);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AD_Process (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
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
      StringBuilder sb = new StringBuilder ("X_AD_Process[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** AccessLevel AD_Reference_ID=5 */
	public static final int ACCESSLEVEL_AD_Reference_ID=5;
	/** Organization = 1 */
	public static final String ACCESSLEVEL_Organization = "1";
	/** Client+Organization = 3 */
	public static final String ACCESSLEVEL_ClientPlusOrganization = "3";
	/** System only = 4 */
	public static final String ACCESSLEVEL_SystemOnly = "4";
	/** All = 7 */
	public static final String ACCESSLEVEL_All = "7";
	/** System+Client = 6 */
	public static final String ACCESSLEVEL_SystemPlusClient = "6";
	/** Client only = 2 */
	public static final String ACCESSLEVEL_ClientOnly = "2";
	/** Set Data Access Level.
		@param AccessLevel 
		Access Level required
	  */
	public void setAccessLevel (String AccessLevel)
	{

		set_Value (COLUMNNAME_AccessLevel, AccessLevel);
	}

	/** Get Data Access Level.
		@return Access Level required
	  */
	public String getAccessLevel () 
	{
		return (String)get_Value(COLUMNNAME_AccessLevel);
	}

	public eone.base.model.I_AD_PrintFormat getAD_PrintFormat() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintFormat)MTable.get(getCtx(), eone.base.model.I_AD_PrintFormat.Table_Name)
			.getPO(getAD_PrintFormat_ID(), get_TrxName());	}

	/** Set Print Format.
		@param AD_PrintFormat_ID 
		Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
	{
		if (AD_PrintFormat_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintFormat_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintFormat_ID, Integer.valueOf(AD_PrintFormat_ID));
	}

	/** Get Print Format.
		@return Data Print Format
	  */
	public int getAD_PrintFormat_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFormat_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Process.
		@param AD_Process_ID 
		Process or Report
	  */
	public void setAD_Process_ID (int AD_Process_ID)
	{
		if (AD_Process_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Process_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Process_ID, Integer.valueOf(AD_Process_ID));
	}

	/** Get Process.
		@return Process or Report
	  */
	public int getAD_Process_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AD_Process_UU.
		@param AD_Process_UU AD_Process_UU	  */
	public void setAD_Process_UU (String AD_Process_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AD_Process_UU, AD_Process_UU);
	}

	/** Get AD_Process_UU.
		@return AD_Process_UU	  */
	public String getAD_Process_UU () 
	{
		return (String)get_Value(COLUMNNAME_AD_Process_UU);
	}

	/** Set Classname.
		@param Classname 
		Java Classname
	  */
	public void setClassname (String Classname)
	{
		set_Value (COLUMNNAME_Classname, Classname);
	}

	/** Get Classname.
		@return Java Classname
	  */
	public String getClassname () 
	{
		return (String)get_Value(COLUMNNAME_Classname);
	}

	/** Set Copy From Report and Process.
		@param CopyFromProcess 
		Copy settings from one report and process to another.
	  */
	public void setCopyFromProcess (String CopyFromProcess)
	{
		set_Value (COLUMNNAME_CopyFromProcess, CopyFromProcess);
	}

	/** Get Copy From Report and Process.
		@return Copy settings from one report and process to another.
	  */
	public String getCopyFromProcess () 
	{
		return (String)get_Value(COLUMNNAME_CopyFromProcess);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** EntityType AD_Reference_ID=389 */
	public static final int ENTITYTYPE_AD_Reference_ID=389;
	/** Set Entity Type.
		@param EntityType 
		Dictionary Entity Type; Determines ownership and synchronization
	  */
	public void setEntityType (String EntityType)
	{

		set_Value (COLUMNNAME_EntityType, EntityType);
	}

	/** Get Entity Type.
		@return Dictionary Entity Type; Determines ownership and synchronization
	  */
	public String getEntityType () 
	{
		return (String)get_Value(COLUMNNAME_EntityType);
	}

	/** Set Beta Functionality.
		@param IsBetaFunctionality 
		This functionality is considered Beta
	  */
	public void setIsBetaFunctionality (boolean IsBetaFunctionality)
	{
		set_Value (COLUMNNAME_IsBetaFunctionality, Boolean.valueOf(IsBetaFunctionality));
	}

	/** Get Beta Functionality.
		@return This functionality is considered Beta
	  */
	public boolean isBetaFunctionality () 
	{
		Object oo = get_Value(COLUMNNAME_IsBetaFunctionality);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Report.
		@param IsReport 
		Indicates a Report record
	  */
	public void setIsReport (boolean IsReport)
	{
		set_Value (COLUMNNAME_IsReport, Boolean.valueOf(IsReport));
	}

	/** Get Report.
		@return Indicates a Report record
	  */
	public boolean isReport () 
	{
		Object oo = get_Value(COLUMNNAME_IsReport);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Server Process.
		@param IsServerProcess 
		Run this Process on Server only
	  */
	public void setIsServerProcess (boolean IsServerProcess)
	{
		set_Value (COLUMNNAME_IsServerProcess, Boolean.valueOf(IsServerProcess));
	}

	/** Get Server Process.
		@return Run this Process on Server only
	  */
	public boolean isServerProcess () 
	{
		Object oo = get_Value(COLUMNNAME_IsServerProcess);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Jasper Report.
		@param JasperReport Jasper Report	  */
	public void setJasperReport (String JasperReport)
	{
		set_Value (COLUMNNAME_JasperReport, JasperReport);
	}

	/** Get Jasper Report.
		@return Jasper Report	  */
	public String getJasperReport () 
	{
		return (String)get_Value(COLUMNNAME_JasperReport);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Procedure.
		@param ProcedureName 
		Name of the Database Procedure
	  */
	public void setProcedureName (String ProcedureName)
	{
		set_Value (COLUMNNAME_ProcedureName, ProcedureName);
	}

	/** Get Procedure.
		@return Name of the Database Procedure
	  */
	public String getProcedureName () 
	{
		return (String)get_Value(COLUMNNAME_ProcedureName);
	}

	/** Set Template Apply.
		@param TemplateApply Template Apply	  */
	public void setTemplateApply (String TemplateApply)
	{
		set_Value (COLUMNNAME_TemplateApply, TemplateApply);
	}

	/** Get Template Apply.
		@return Template Apply	  */
	public String getTemplateApply () 
	{
		return (String)get_Value(COLUMNNAME_TemplateApply);
	}

	/** Set Code.
		@param Value 
		Code
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Code.
		@return Code
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
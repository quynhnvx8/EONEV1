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
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_Role
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_Role extends PO implements I_AD_Role, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201028L;

    /** Standard Constructor */
    public X_AD_Role (Properties ctx, int AD_Role_ID, String trxName)
    {
      super (ctx, AD_Role_ID, trxName);
      /** if (AD_Role_ID == 0)
        {
			setAD_Role_ID (0);
			setConfirmQueryRecords (0);
// 0
			setIsAccessAllOrgs (false);
// N
			setIsCanExport (true);
// Y
			setIsMasterRole (false);
// N
			setIsMenuAutoExpand (false);
// N
			setIsShowAcct (false);
// N
			setIsUseUserOrgAccess (false);
// N
			setMaxQueryRecords (0);
// 0
			setName (null);
			setPreferenceType (null);
// O
			setUserLevel (null);
        } */
    }

    /** Load Constructor */
    public X_AD_Role (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AD_Role[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_Tree getAD_Tree_Menu() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Tree)MTable.get(getCtx(), org.compiere.model.I_AD_Tree.Table_Name)
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

	public org.compiere.model.I_AD_Tree getAD_Tree_Org() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Tree)MTable.get(getCtx(), org.compiere.model.I_AD_Tree.Table_Name)
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

	/** Set Confirm Query Records.
		@param ConfirmQueryRecords 
		Require Confirmation if more records will be returned by the query (If not defined 500)
	  */
	public void setConfirmQueryRecords (int ConfirmQueryRecords)
	{
		set_Value (COLUMNNAME_ConfirmQueryRecords, Integer.valueOf(ConfirmQueryRecords));
	}

	/** Get Confirm Query Records.
		@return Require Confirmation if more records will be returned by the query (If not defined 500)
	  */
	public int getConfirmQueryRecords () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ConfirmQueryRecords);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Access Advanced .
		@param IsAccessAdvanced Access Advanced 	  */
	public void setIsAccessAdvanced (boolean IsAccessAdvanced)
	{
		set_Value (COLUMNNAME_IsAccessAdvanced, Boolean.valueOf(IsAccessAdvanced));
	}

	/** Get Access Advanced .
		@return Access Advanced 	  */
	public boolean isAccessAdvanced () 
	{
		Object oo = get_Value(COLUMNNAME_IsAccessAdvanced);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Access all Orgs.
		@param IsAccessAllOrgs 
		Access all Organizations (no org access control) of the client
	  */
	public void setIsAccessAllOrgs (boolean IsAccessAllOrgs)
	{
		set_Value (COLUMNNAME_IsAccessAllOrgs, Boolean.valueOf(IsAccessAllOrgs));
	}

	/** Get Access all Orgs.
		@return Access all Organizations (no org access control) of the client
	  */
	public boolean isAccessAllOrgs () 
	{
		Object oo = get_Value(COLUMNNAME_IsAccessAllOrgs);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Can Export.
		@param IsCanExport 
		Users with this role can export data
	  */
	public void setIsCanExport (boolean IsCanExport)
	{
		set_Value (COLUMNNAME_IsCanExport, Boolean.valueOf(IsCanExport));
	}

	/** Get Can Export.
		@return Users with this role can export data
	  */
	public boolean isCanExport () 
	{
		Object oo = get_Value(COLUMNNAME_IsCanExport);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsConfigAcct.
		@param IsConfigAcct IsConfigAcct	  */
	public void setIsConfigAcct (boolean IsConfigAcct)
	{
		set_Value (COLUMNNAME_IsConfigAcct, Boolean.valueOf(IsConfigAcct));
	}

	/** Get IsConfigAcct.
		@return IsConfigAcct	  */
	public boolean isConfigAcct () 
	{
		Object oo = get_Value(COLUMNNAME_IsConfigAcct);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDragDropMenu.
		@param IsDragDropMenu IsDragDropMenu	  */
	public void setIsDragDropMenu (boolean IsDragDropMenu)
	{
		set_Value (COLUMNNAME_IsDragDropMenu, Boolean.valueOf(IsDragDropMenu));
	}

	/** Get IsDragDropMenu.
		@return IsDragDropMenu	  */
	public boolean isDragDropMenu () 
	{
		Object oo = get_Value(COLUMNNAME_IsDragDropMenu);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Master Role.
		@param IsMasterRole 
		A master role cannot be assigned to users, it is intended to define access to menu option and documents and inherit to other roles
	  */
	public void setIsMasterRole (boolean IsMasterRole)
	{
		set_Value (COLUMNNAME_IsMasterRole, Boolean.valueOf(IsMasterRole));
	}

	/** Get Master Role.
		@return A master role cannot be assigned to users, it is intended to define access to menu option and documents and inherit to other roles
	  */
	public boolean isMasterRole () 
	{
		Object oo = get_Value(COLUMNNAME_IsMasterRole);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Auto expand menu.
		@param IsMenuAutoExpand 
		If ticked, the menu is automatically expanded
	  */
	public void setIsMenuAutoExpand (boolean IsMenuAutoExpand)
	{
		set_Value (COLUMNNAME_IsMenuAutoExpand, Boolean.valueOf(IsMenuAutoExpand));
	}

	/** Get Auto expand menu.
		@return If ticked, the menu is automatically expanded
	  */
	public boolean isMenuAutoExpand () 
	{
		Object oo = get_Value(COLUMNNAME_IsMenuAutoExpand);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Show Accounting.
		@param IsShowAcct 
		Users with this role can see accounting information
	  */
	public void setIsShowAcct (boolean IsShowAcct)
	{
		set_Value (COLUMNNAME_IsShowAcct, Boolean.valueOf(IsShowAcct));
	}

	/** Get Show Accounting.
		@return Users with this role can see accounting information
	  */
	public boolean isShowAcct () 
	{
		Object oo = get_Value(COLUMNNAME_IsShowAcct);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsShowPrice.
		@param IsShowPrice IsShowPrice	  */
	public void setIsShowPrice (boolean IsShowPrice)
	{
		set_Value (COLUMNNAME_IsShowPrice, Boolean.valueOf(IsShowPrice));
	}

	/** Get IsShowPrice.
		@return IsShowPrice	  */
	public boolean isShowPrice () 
	{
		Object oo = get_Value(COLUMNNAME_IsShowPrice);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Use User Org Access.
		@param IsUseUserOrgAccess 
		Use Org Access defined by user instead of Role Org Access
	  */
	public void setIsUseUserOrgAccess (boolean IsUseUserOrgAccess)
	{
		set_Value (COLUMNNAME_IsUseUserOrgAccess, Boolean.valueOf(IsUseUserOrgAccess));
	}

	/** Get Use User Org Access.
		@return Use Org Access defined by user instead of Role Org Access
	  */
	public boolean isUseUserOrgAccess () 
	{
		Object oo = get_Value(COLUMNNAME_IsUseUserOrgAccess);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Max Query Records.
		@param MaxQueryRecords 
		If defined, you cannot query more records as defined - the query criteria needs to be changed to query less records
	  */
	public void setMaxQueryRecords (int MaxQueryRecords)
	{
		set_Value (COLUMNNAME_MaxQueryRecords, Integer.valueOf(MaxQueryRecords));
	}

	/** Get Max Query Records.
		@return If defined, you cannot query more records as defined - the query criteria needs to be changed to query less records
	  */
	public int getMaxQueryRecords () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MaxQueryRecords);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** PreferenceType AD_Reference_ID=330 */
	public static final int PREFERENCETYPE_AD_Reference_ID=330;
	/** Client = C */
	public static final String PREFERENCETYPE_Client = "C";
	/** Organization = O */
	public static final String PREFERENCETYPE_Organization = "O";
	/** User = U */
	public static final String PREFERENCETYPE_User = "U";
	/** None = N */
	public static final String PREFERENCETYPE_None = "N";
	/** Set Preference Level.
		@param PreferenceType 
		Determines what preferences the user can set
	  */
	public void setPreferenceType (String PreferenceType)
	{

		set_Value (COLUMNNAME_PreferenceType, PreferenceType);
	}

	/** Get Preference Level.
		@return Determines what preferences the user can set
	  */
	public String getPreferenceType () 
	{
		return (String)get_Value(COLUMNNAME_PreferenceType);
	}

	/** Level1 = 01 */
	public static final String ROLELEVEL_Level1 = "01";
	/** Level2 = 02 */
	public static final String ROLELEVEL_Level2 = "02";
	/** Level3 = 03 */
	public static final String ROLELEVEL_Level3 = "03";
	/** Level4 = 04 */
	public static final String ROLELEVEL_Level4 = "04";
	/** Level5 = 05 */
	public static final String ROLELEVEL_Level5 = "05";
	/** Level6 = 06 */
	public static final String ROLELEVEL_Level6 = "06";
	/** Set RoleLevel.
		@param RoleLevel RoleLevel	  */
	public void setRoleLevel (String RoleLevel)
	{

		set_Value (COLUMNNAME_RoleLevel, RoleLevel);
	}

	/** Get RoleLevel.
		@return RoleLevel	  */
	public String getRoleLevel () 
	{
		return (String)get_Value(COLUMNNAME_RoleLevel);
	}

	/** Finacial = FN */
	public static final String ROLETYPE_Finacial = "FN";
	/** Human Resource = HR */
	public static final String ROLETYPE_HumanResource = "HR";
	/** Asset = AS */
	public static final String ROLETYPE_Asset = "AS";
	/** Manufacturing = MA */
	public static final String ROLETYPE_Manufacturing = "MA";
	/** Document = DO */
	public static final String ROLETYPE_Document = "DO";
	/** System = SY */
	public static final String ROLETYPE_System = "SY";
	/** Administrator = AD */
	public static final String ROLETYPE_Administrator = "AD";
	/** Set Role Type.
		@param RoleType Role Type	  */
	public void setRoleType (String RoleType)
	{

		set_Value (COLUMNNAME_RoleType, RoleType);
	}

	/** Get Role Type.
		@return Role Type	  */
	public String getRoleType () 
	{
		return (String)get_Value(COLUMNNAME_RoleType);
	}

	/** UserLevel AD_Reference_ID=226 */
	public static final int USERLEVEL_AD_Reference_ID=226;
	/** System = S   */
	public static final String USERLEVEL_System = "S  ";
	/** Client =  C  */
	public static final String USERLEVEL_Client = " C ";
	/** Organization =   O */
	public static final String USERLEVEL_Organization = "  O";
	/** Client+Organization =  CO */
	public static final String USERLEVEL_ClientPlusOrganization = " CO";
	/** Set User Level.
		@param UserLevel 
		System Client Organization
	  */
	public void setUserLevel (String UserLevel)
	{

		set_Value (COLUMNNAME_UserLevel, UserLevel);
	}

	/** Get User Level.
		@return System Client Organization
	  */
	public String getUserLevel () 
	{
		return (String)get_Value(COLUMNNAME_UserLevel);
	}
}
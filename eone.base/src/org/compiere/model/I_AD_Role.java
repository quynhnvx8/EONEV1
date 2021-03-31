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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_Role
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_AD_Role 
{

    /** TableName=AD_Role */
    public static final String Table_Name = "AD_Role";

    /** AD_Table_ID=156 */
    public static final int Table_ID = 156;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_Role_ID */
    public static final String COLUMNNAME_AD_Role_ID = "AD_Role_ID";

	/** Set Role.
	  * Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID);

	/** Get Role.
	  * Responsibility Role
	  */
	public int getAD_Role_ID();

    /** Column name AD_Tree_Menu_ID */
    public static final String COLUMNNAME_AD_Tree_Menu_ID = "AD_Tree_Menu_ID";

	/** Set Menu Tree.
	  * Tree of the menu
	  */
	public void setAD_Tree_Menu_ID (int AD_Tree_Menu_ID);

	/** Get Menu Tree.
	  * Tree of the menu
	  */
	public int getAD_Tree_Menu_ID();

	public org.compiere.model.I_AD_Tree getAD_Tree_Menu() throws RuntimeException;

    /** Column name AD_Tree_Org_ID */
    public static final String COLUMNNAME_AD_Tree_Org_ID = "AD_Tree_Org_ID";

	/** Set Organization Tree.
	  * Trees are used for (financial) reporting and security access (via role)
	  */
	public void setAD_Tree_Org_ID (int AD_Tree_Org_ID);

	/** Get Organization Tree.
	  * Trees are used for (financial) reporting and security access (via role)
	  */
	public int getAD_Tree_Org_ID();

	public org.compiere.model.I_AD_Tree getAD_Tree_Org() throws RuntimeException;

    /** Column name ConfirmQueryRecords */
    public static final String COLUMNNAME_ConfirmQueryRecords = "ConfirmQueryRecords";

	/** Set Confirm Query Records.
	  * Require Confirmation if more records will be returned by the query (If not defined 500)
	  */
	public void setConfirmQueryRecords (int ConfirmQueryRecords);

	/** Get Confirm Query Records.
	  * Require Confirmation if more records will be returned by the query (If not defined 500)
	  */
	public int getConfirmQueryRecords();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsAccessAdvanced */
    public static final String COLUMNNAME_IsAccessAdvanced = "IsAccessAdvanced";

	/** Set Access Advanced 	  */
	public void setIsAccessAdvanced (boolean IsAccessAdvanced);

	/** Get Access Advanced 	  */
	public boolean isAccessAdvanced();

    /** Column name IsAccessAllOrgs */
    public static final String COLUMNNAME_IsAccessAllOrgs = "IsAccessAllOrgs";

	/** Set Access all Orgs.
	  * Access all Organizations (no org access control) of the client
	  */
	public void setIsAccessAllOrgs (boolean IsAccessAllOrgs);

	/** Get Access all Orgs.
	  * Access all Organizations (no org access control) of the client
	  */
	public boolean isAccessAllOrgs();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsCanExport */
    public static final String COLUMNNAME_IsCanExport = "IsCanExport";

	/** Set Can Export.
	  * Users with this role can export data
	  */
	public void setIsCanExport (boolean IsCanExport);

	/** Get Can Export.
	  * Users with this role can export data
	  */
	public boolean isCanExport();

    /** Column name IsConfigAcct */
    public static final String COLUMNNAME_IsConfigAcct = "IsConfigAcct";

	/** Set IsConfigAcct	  */
	public void setIsConfigAcct (boolean IsConfigAcct);

	/** Get IsConfigAcct	  */
	public boolean isConfigAcct();

    /** Column name IsDragDropMenu */
    public static final String COLUMNNAME_IsDragDropMenu = "IsDragDropMenu";

	/** Set IsDragDropMenu	  */
	public void setIsDragDropMenu (boolean IsDragDropMenu);

	/** Get IsDragDropMenu	  */
	public boolean isDragDropMenu();

    /** Column name IsMasterRole */
    public static final String COLUMNNAME_IsMasterRole = "IsMasterRole";

	/** Set Master Role.
	  * A master role cannot be assigned to users, it is intended to define access to menu option and documents and inherit to other roles
	  */
	public void setIsMasterRole (boolean IsMasterRole);

	/** Get Master Role.
	  * A master role cannot be assigned to users, it is intended to define access to menu option and documents and inherit to other roles
	  */
	public boolean isMasterRole();

    /** Column name IsMenuAutoExpand */
    public static final String COLUMNNAME_IsMenuAutoExpand = "IsMenuAutoExpand";

	/** Set Auto expand menu.
	  * If ticked, the menu is automatically expanded
	  */
	public void setIsMenuAutoExpand (boolean IsMenuAutoExpand);

	/** Get Auto expand menu.
	  * If ticked, the menu is automatically expanded
	  */
	public boolean isMenuAutoExpand();

    /** Column name IsShowAcct */
    public static final String COLUMNNAME_IsShowAcct = "IsShowAcct";

	/** Set Show Accounting.
	  * Users with this role can see accounting information
	  */
	public void setIsShowAcct (boolean IsShowAcct);

	/** Get Show Accounting.
	  * Users with this role can see accounting information
	  */
	public boolean isShowAcct();

    /** Column name IsShowPrice */
    public static final String COLUMNNAME_IsShowPrice = "IsShowPrice";

	/** Set IsShowPrice	  */
	public void setIsShowPrice (boolean IsShowPrice);

	/** Get IsShowPrice	  */
	public boolean isShowPrice();

    /** Column name IsUseUserOrgAccess */
    public static final String COLUMNNAME_IsUseUserOrgAccess = "IsUseUserOrgAccess";

	/** Set Use User Org Access.
	  * Use Org Access defined by user instead of Role Org Access
	  */
	public void setIsUseUserOrgAccess (boolean IsUseUserOrgAccess);

	/** Get Use User Org Access.
	  * Use Org Access defined by user instead of Role Org Access
	  */
	public boolean isUseUserOrgAccess();

    /** Column name MaxQueryRecords */
    public static final String COLUMNNAME_MaxQueryRecords = "MaxQueryRecords";

	/** Set Max Query Records.
	  * If defined, you cannot query more records as defined - the query criteria needs to be changed to query less records
	  */
	public void setMaxQueryRecords (int MaxQueryRecords);

	/** Get Max Query Records.
	  * If defined, you cannot query more records as defined - the query criteria needs to be changed to query less records
	  */
	public int getMaxQueryRecords();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name PreferenceType */
    public static final String COLUMNNAME_PreferenceType = "PreferenceType";

	/** Set Preference Level.
	  * Determines what preferences the user can set
	  */
	public void setPreferenceType (String PreferenceType);

	/** Get Preference Level.
	  * Determines what preferences the user can set
	  */
	public String getPreferenceType();

    /** Column name RoleLevel */
    public static final String COLUMNNAME_RoleLevel = "RoleLevel";

	/** Set RoleLevel	  */
	public void setRoleLevel (String RoleLevel);

	/** Get RoleLevel	  */
	public String getRoleLevel();

    /** Column name RoleType */
    public static final String COLUMNNAME_RoleType = "RoleType";

	/** Set Role Type	  */
	public void setRoleType (String RoleType);

	/** Get Role Type	  */
	public String getRoleType();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UserLevel */
    public static final String COLUMNNAME_UserLevel = "UserLevel";

	/** Set User Level.
	  * System Client Organization
	  */
	public void setUserLevel (String UserLevel);

	/** Get User Level.
	  * System Client Organization
	  */
	public String getUserLevel();
}

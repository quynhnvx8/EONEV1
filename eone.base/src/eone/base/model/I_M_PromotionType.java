/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_PromotionType
 *  @author EOne (generated) 
 *  @version Version 1.0
 */
public interface I_M_PromotionType 
{

    /** TableName=M_PromotionType */
    public static final String Table_Name = "M_PromotionType";

    /** AD_Table_ID=1200354 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

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

    /** Column name M_PromotionType_ID */
    public static final String COLUMNNAME_M_PromotionType_ID = "M_PromotionType_ID";

	/** Set Promtion Type	  */
	public void setM_PromotionType_ID (int M_PromotionType_ID);

	/** Get Promtion Type	  */
	public int getM_PromotionType_ID();

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

    /** Column name Requied_Amt */
    public static final String COLUMNNAME_Requied_Amt = "Requied_Amt";

	/** Set Requied Amount	  */
	public void setRequied_Amt (boolean Requied_Amt);

	/** Get Requied Amount	  */
	public boolean isRequied_Amt();

    /** Column name Requied_Dis */
    public static final String COLUMNNAME_Requied_Dis = "Requied_Dis";

	/** Set Requied Discount	  */
	public void setRequied_Dis (boolean Requied_Dis);

	/** Get Requied Discount	  */
	public boolean isRequied_Dis();

    /** Column name Requied_Gif */
    public static final String COLUMNNAME_Requied_Gif = "Requied_Gif";

	/** Set Requied Gif	  */
	public void setRequied_Gif (boolean Requied_Gif);

	/** Get Requied Gif	  */
	public boolean isRequied_Gif();

    /** Column name Requied_Group */
    public static final String COLUMNNAME_Requied_Group = "Requied_Group";

	/** Set Requied Group Product	  */
	public void setRequied_Group (boolean Requied_Group);

	/** Get Requied Group Product	  */
	public boolean isRequied_Group();

    /** Column name Requied_Per */
    public static final String COLUMNNAME_Requied_Per = "Requied_Per";

	/** Set Requied Percent	  */
	public void setRequied_Per (boolean Requied_Per);

	/** Get Requied Percent	  */
	public boolean isRequied_Per();

    /** Column name Requied_Qty */
    public static final String COLUMNNAME_Requied_Qty = "Requied_Qty";

	/** Set Requied Qty	  */
	public void setRequied_Qty (boolean Requied_Qty);

	/** Get Requied Qty	  */
	public boolean isRequied_Qty();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Code.
	  * Code
	  */
	public void setValue (String Value);

	/** Get Code.
	  * Code
	  */
	public String getValue();
}

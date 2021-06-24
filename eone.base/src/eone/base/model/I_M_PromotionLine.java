/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_PromotionLine
 *  @author EOne (generated) 
 *  @version Version 1.0
 */
public interface I_M_PromotionLine 
{

    /** TableName=M_PromotionLine */
    public static final String Table_Name = "M_PromotionLine";

    /** AD_Table_ID=1200346 */
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

    /** Column name AD_Department_Multi_ID */
    public static final String COLUMNNAME_AD_Department_Multi_ID = "AD_Department_Multi_ID";

	/** Set Department Multiple	  */
	public void setAD_Department_Multi_ID (String AD_Department_Multi_ID);

	/** Get Department Multiple	  */
	public String getAD_Department_Multi_ID();

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

    /** Column name Amount */
    public static final String COLUMNNAME_Amount = "Amount";

	/** Set Amount.
	  * Amount in a defined currency
	  */
	public void setAmount (String Amount);

	/** Get Amount.
	  * Amount in a defined currency
	  */
	public String getAmount();

    /** Column name C_BP_Group_ID */
    public static final String COLUMNNAME_C_BP_Group_ID = "C_BP_Group_ID";

	/** Set Business Partner Group.
	  * Business Partner Group
	  */
	public void setC_BP_Group_ID (String C_BP_Group_ID);

	/** Get Business Partner Group.
	  * Business Partner Group
	  */
	public String getC_BP_Group_ID();

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

    /** Column name DiscountAmt */
    public static final String COLUMNNAME_DiscountAmt = "DiscountAmt";

	/** Set Discount Amount.
	  * Calculated amount of discount
	  */
	public void setDiscountAmt (BigDecimal DiscountAmt);

	/** Get Discount Amount.
	  * Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt();

    /** Column name DiscountPercent */
    public static final String COLUMNNAME_DiscountPercent = "DiscountPercent";

	/** Set DiscountPercent	  */
	public void setDiscountPercent (BigDecimal DiscountPercent);

	/** Get DiscountPercent	  */
	public BigDecimal getDiscountPercent();

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

    /** Column name M_Product_Free_ID */
    public static final String COLUMNNAME_M_Product_Free_ID = "M_Product_Free_ID";

	/** Set Product Free	  */
	public void setM_Product_Free_ID (String M_Product_Free_ID);

	/** Get Product Free	  */
	public String getM_Product_Free_ID();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (String M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public String getM_Product_ID();

    /** Column name M_Promotion_ID */
    public static final String COLUMNNAME_M_Promotion_ID = "M_Promotion_ID";

	/** Set Promotion	  */
	public void setM_Promotion_ID (int M_Promotion_ID);

	/** Get Promotion	  */
	public int getM_Promotion_ID();

	public I_M_Promotion getM_Promotion() throws RuntimeException;

    /** Column name M_PromotionLine_ID */
    public static final String COLUMNNAME_M_PromotionLine_ID = "M_PromotionLine_ID";

	/** Set Promotion Line	  */
	public void setM_PromotionLine_ID (int M_PromotionLine_ID);

	/** Get Promotion Line	  */
	public int getM_PromotionLine_ID();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name QtyFree */
    public static final String COLUMNNAME_QtyFree = "QtyFree";

	/** Set QtyFree	  */
	public void setQtyFree (String QtyFree);

	/** Get QtyFree	  */
	public String getQtyFree();

    /** Column name QtyMultiplier */
    public static final String COLUMNNAME_QtyMultiplier = "QtyMultiplier";

	/** Set Multiplier Quantity.
	  * Value to multiply quantities by for generating commissions.
	  */
	public void setQtyMultiplier (String QtyMultiplier);

	/** Get Multiplier Quantity.
	  * Value to multiply quantities by for generating commissions.
	  */
	public String getQtyMultiplier();

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
}

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

/** Generated Interface for C_TransferPeriodLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_C_TransferPeriodLine 
{

    /** TableName=C_TransferPeriodLine */
    public static final String Table_Name = "C_TransferPeriodLine";

    /** AD_Table_ID=1200296 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name Account_Cr_ID */
    public static final String COLUMNNAME_Account_Cr_ID = "Account_Cr_ID";

	/** Set Account Cr.
	  * Account Cr
	  */
	public void setAccount_Cr_ID (int Account_Cr_ID);

	/** Get Account Cr.
	  * Account Cr
	  */
	public int getAccount_Cr_ID();

	public org.compiere.model.I_C_ElementValue getAccount_Cr() throws RuntimeException;

    /** Column name Account_Dr_ID */
    public static final String COLUMNNAME_Account_Dr_ID = "Account_Dr_ID";

	/** Set Account Dr.
	  * Account Dr
	  */
	public void setAccount_Dr_ID (int Account_Dr_ID);

	/** Get Account Dr.
	  * Account Dr
	  */
	public int getAccount_Dr_ID();

	public org.compiere.model.I_C_ElementValue getAccount_Dr() throws RuntimeException;

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

    /** Column name Amount */
    public static final String COLUMNNAME_Amount = "Amount";

	/** Set Amount.
	  * Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount);

	/** Get Amount.
	  * Amount in a defined currency
	  */
	public BigDecimal getAmount();

    /** Column name AmountConvert */
    public static final String COLUMNNAME_AmountConvert = "AmountConvert";

	/** Set AmountConvert	  */
	public void setAmountConvert (BigDecimal AmountConvert);

	/** Get AmountConvert	  */
	public BigDecimal getAmountConvert();

    /** Column name C_TransferPeriod_ID */
    public static final String COLUMNNAME_C_TransferPeriod_ID = "C_TransferPeriod_ID";

	/** Set Transfer Period	  */
	public void setC_TransferPeriod_ID (int C_TransferPeriod_ID);

	/** Get Transfer Period	  */
	public int getC_TransferPeriod_ID();

	public I_C_TransferPeriod getC_TransferPeriod() throws RuntimeException;

    /** Column name C_TransferPeriodLine_ID */
    public static final String COLUMNNAME_C_TransferPeriodLine_ID = "C_TransferPeriodLine_ID";

	/** Set Transfer Period Line	  */
	public void setC_TransferPeriodLine_ID (int C_TransferPeriodLine_ID);

	/** Get Transfer Period Line	  */
	public int getC_TransferPeriodLine_ID();

    /** Column name C_TypeCost_ID */
    public static final String COLUMNNAME_C_TypeCost_ID = "C_TypeCost_ID";

	/** Set TypeCost	  */
	public void setC_TypeCost_ID (int C_TypeCost_ID);

	/** Get TypeCost	  */
	public int getC_TypeCost_ID();

	public I_C_TypeCost getC_TypeCost() throws RuntimeException;

    /** Column name C_TypeRevenue_ID */
    public static final String COLUMNNAME_C_TypeRevenue_ID = "C_TypeRevenue_ID";

	/** Set Type Revenue	  */
	public void setC_TypeRevenue_ID (int C_TypeRevenue_ID);

	/** Get Type Revenue	  */
	public int getC_TypeRevenue_ID();

	public I_C_TypeRevenue getC_TypeRevenue() throws RuntimeException;

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

    /** Column name M_Product_Cr_ID */
    public static final String COLUMNNAME_M_Product_Cr_ID = "M_Product_Cr_ID";

	/** Set Product Cr	  */
	public void setM_Product_Cr_ID (int M_Product_Cr_ID);

	/** Get Product Cr	  */
	public int getM_Product_Cr_ID();

	public org.compiere.model.I_M_Product getM_Product_Cr() throws RuntimeException;

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

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

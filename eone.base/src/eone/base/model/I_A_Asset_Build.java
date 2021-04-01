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
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for A_Asset_Build
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_A_Asset_Build 
{

    /** TableName=A_Asset_Build */
    public static final String Table_Name = "A_Asset_Build";

    /** AD_Table_ID=1200282 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name A_Asset_Build_ID */
    public static final String COLUMNNAME_A_Asset_Build_ID = "A_Asset_Build_ID";

	/** Set Asset Build	  */
	public void setA_Asset_Build_ID (int A_Asset_Build_ID);

	/** Get Asset Build	  */
	public int getA_Asset_Build_ID();

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	public eone.base.model.I_A_Asset getA_Asset() throws RuntimeException;

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

	public eone.base.model.I_C_ElementValue getAccount_Cr() throws RuntimeException;

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

	public eone.base.model.I_C_ElementValue getAccount_Dr() throws RuntimeException;

    /** Column name Account_Tax_ID */
    public static final String COLUMNNAME_Account_Tax_ID = "Account_Tax_ID";

	/** Set Account Tax	  */
	public void setAccount_Tax_ID (int Account_Tax_ID);

	/** Get Account Tax	  */
	public int getAccount_Tax_ID();

	public eone.base.model.I_C_ElementValue getAccount_Tax() throws RuntimeException;

    /** Column name AccumulateAmt */
    public static final String COLUMNNAME_AccumulateAmt = "AccumulateAmt";

	/** Set AccumulateAmt	  */
	public void setAccumulateAmt (BigDecimal AccumulateAmt);

	/** Get AccumulateAmt	  */
	public BigDecimal getAccumulateAmt();

    /** Column name AccumulateUseLifes */
    public static final String COLUMNNAME_AccumulateUseLifes = "AccumulateUseLifes";

	/** Set Accumulate UseLifes	  */
	public void setAccumulateUseLifes (BigDecimal AccumulateUseLifes);

	/** Get Accumulate UseLifes	  */
	public BigDecimal getAccumulateUseLifes();

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

    /** Column name Approved */
    public static final String COLUMNNAME_Approved = "Approved";

	/** Set Approved	  */
	public void setApproved (String Approved);

	/** Get Approved	  */
	public String getApproved();

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public eone.base.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_Tax_ID */
    public static final String COLUMNNAME_C_Tax_ID = "C_Tax_ID";

	/** Set Tax.
	  * Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID);

	/** Get Tax.
	  * Tax identifier
	  */
	public int getC_Tax_ID();

	public eone.base.model.I_C_Tax getC_Tax() throws RuntimeException;

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

    /** Column name CalculateTax */
    public static final String COLUMNNAME_CalculateTax = "CalculateTax";

	/** Set CalculateTax	  */
	public void setCalculateTax (String CalculateTax);

	/** Get CalculateTax	  */
	public String getCalculateTax();

    /** Column name Canceled */
    public static final String COLUMNNAME_Canceled = "Canceled";

	/** Set Canceled	  */
	public void setCanceled (String Canceled);

	/** Get Canceled	  */
	public String getCanceled();

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

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

    /** Column name DateInvoiced */
    public static final String COLUMNNAME_DateInvoiced = "DateInvoiced";

	/** Set Date Invoiced.
	  * Date printed on Invoice
	  */
	public void setDateInvoiced (Timestamp DateInvoiced);

	/** Get Date Invoiced.
	  * Date printed on Invoice
	  */
	public Timestamp getDateInvoiced();

    /** Column name DepreciationDate */
    public static final String COLUMNNAME_DepreciationDate = "DepreciationDate";

	/** Set Depreciation Date.
	  * Date of last depreciation
	  */
	public void setDepreciationDate (Timestamp DepreciationDate);

	/** Get Depreciation Date.
	  * Date of last depreciation
	  */
	public Timestamp getDepreciationDate();

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

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name IncludeTax */
    public static final String COLUMNNAME_IncludeTax = "IncludeTax";

	/** Set IncludeTax	  */
	public void setIncludeTax (String IncludeTax);

	/** Get IncludeTax	  */
	public String getIncludeTax();

    /** Column name InvoiceNo */
    public static final String COLUMNNAME_InvoiceNo = "InvoiceNo";

	/** Set InvoiceNo	  */
	public void setInvoiceNo (String InvoiceNo);

	/** Get InvoiceNo	  */
	public String getInvoiceNo();

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

	public eone.base.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name Posted */
    public static final String COLUMNNAME_Posted = "Posted";

	/** Set Posted.
	  * Posting status
	  */
	public void setPosted (boolean Posted);

	/** Get Posted.
	  * Posting status
	  */
	public boolean isPosted();

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

    /** Column name TaxAmt */
    public static final String COLUMNNAME_TaxAmt = "TaxAmt";

	/** Set Tax Amount.
	  * Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt);

	/** Get Tax Amount.
	  * Tax Amount for a document
	  */
	public BigDecimal getTaxAmt();

    /** Column name TaxBaseAmt */
    public static final String COLUMNNAME_TaxBaseAmt = "TaxBaseAmt";

	/** Set Tax base Amount.
	  * Base for calculating the tax amount
	  */
	public void setTaxBaseAmt (BigDecimal TaxBaseAmt);

	/** Get Tax base Amount.
	  * Base for calculating the tax amount
	  */
	public BigDecimal getTaxBaseAmt();

    /** Column name TypeCalculate */
    public static final String COLUMNNAME_TypeCalculate = "TypeCalculate";

	/** Set TypeCalculate	  */
	public void setTypeCalculate (String TypeCalculate);

	/** Get TypeCalculate	  */
	public String getTypeCalculate();

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

    /** Column name UseDate */
    public static final String COLUMNNAME_UseDate = "UseDate";

	/** Set UseDate	  */
	public void setUseDate (Timestamp UseDate);

	/** Get UseDate	  */
	public Timestamp getUseDate();

    /** Column name UseLifes */
    public static final String COLUMNNAME_UseLifes = "UseLifes";

	/** Set UseLifes	  */
	public void setUseLifes (BigDecimal UseLifes);

	/** Get UseLifes	  */
	public BigDecimal getUseLifes();
}

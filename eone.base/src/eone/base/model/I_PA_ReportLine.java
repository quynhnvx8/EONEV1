/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for PA_ReportLine
 *  @author EOne (generated) 
 *  @version Version 1.0
 */
public interface I_PA_ReportLine 
{

    /** TableName=PA_ReportLine */
    public static final String Table_Name = "PA_ReportLine";

    /** AD_Table_ID=448 */
    public static final int Table_ID = 448;

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

    /** Column name C_ElementValue_ID */
    public static final String COLUMNNAME_C_ElementValue_ID = "C_ElementValue_ID";

	/** Set Account Element.
	  * Account Element
	  */
	public void setC_ElementValue_ID (String C_ElementValue_ID);

	/** Get Account Element.
	  * Account Element
	  */
	public String getC_ElementValue_ID();

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

    /** Column name FormulaSetup */
    public static final String COLUMNNAME_FormulaSetup = "FormulaSetup";

	/** Set FormulaSetup	  */
	public void setFormulaSetup (String FormulaSetup);

	/** Get FormulaSetup	  */
	public String getFormulaSetup();

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

    /** Column name IsBold */
    public static final String COLUMNNAME_IsBold = "IsBold";

	/** Set IsBold	  */
	public void setIsBold (boolean IsBold);

	/** Get IsBold	  */
	public boolean isBold();

    /** Column name IsItalic */
    public static final String COLUMNNAME_IsItalic = "IsItalic";

	/** Set IsItalic	  */
	public void setIsItalic (boolean IsItalic);

	/** Get IsItalic	  */
	public boolean isItalic();

    /** Column name IsPrinted */
    public static final String COLUMNNAME_IsPrinted = "IsPrinted";

	/** Set Printed.
	  * Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted);

	/** Get Printed.
	  * Indicates if this document / line is printed
	  */
	public boolean isPrinted();

    /** Column name IsSummary */
    public static final String COLUMNNAME_IsSummary = "IsSummary";

	/** Set Summary Level.
	  * This is a summary entity
	  */
	public void setIsSummary (boolean IsSummary);

	/** Get Summary Level.
	  * This is a summary entity
	  */
	public boolean isSummary();

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

    /** Column name OrderCalculate */
    public static final String COLUMNNAME_OrderCalculate = "OrderCalculate";

	/** Set OrderCalculate	  */
	public void setOrderCalculate (int OrderCalculate);

	/** Get OrderCalculate	  */
	public int getOrderCalculate();

    /** Column name PA_Report_ID */
    public static final String COLUMNNAME_PA_Report_ID = "PA_Report_ID";

	/** Set Financial Report.
	  * Financial Report
	  */
	public void setPA_Report_ID (int PA_Report_ID);

	/** Get Financial Report.
	  * Financial Report
	  */
	public int getPA_Report_ID();

	public eone.base.model.I_PA_Report getPA_Report() throws RuntimeException;

    /** Column name PA_ReportLine_ID */
    public static final String COLUMNNAME_PA_ReportLine_ID = "PA_ReportLine_ID";

	/** Set Report Line	  */
	public void setPA_ReportLine_ID (int PA_ReportLine_ID);

	/** Get Report Line	  */
	public int getPA_ReportLine_ID();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

    /** Column name TypeAccount */
    public static final String COLUMNNAME_TypeAccount = "TypeAccount";

	/** Set TypeAccount	  */
	public void setTypeAccount (String TypeAccount);

	/** Get TypeAccount	  */
	public String getTypeAccount();

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

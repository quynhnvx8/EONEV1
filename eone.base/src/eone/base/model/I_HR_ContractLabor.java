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

/** Generated Interface for HR_ContractLabor
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_HR_ContractLabor 
{

    /** TableName=HR_ContractLabor */
    public static final String Table_Name = "HR_ContractLabor";

    /** AD_Table_ID=1200304 */
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

    /** Column name Allowance */
    public static final String COLUMNNAME_Allowance = "Allowance";

	/** Set Allowance	  */
	public void setAllowance (BigDecimal Allowance);

	/** Get Allowance	  */
	public BigDecimal getAllowance();

    /** Column name ContentText */
    public static final String COLUMNNAME_ContentText = "ContentText";

	/** Set Content	  */
	public void setContentText (String ContentText);

	/** Get Content	  */
	public String getContentText();

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

    /** Column name DateSign */
    public static final String COLUMNNAME_DateSign = "DateSign";

	/** Set DateSign	  */
	public void setDateSign (Timestamp DateSign);

	/** Get DateSign	  */
	public Timestamp getDateSign();

    /** Column name DateStart */
    public static final String COLUMNNAME_DateStart = "DateStart";

	/** Set Date Start.
	  * Date Start for this Order
	  */
	public void setDateStart (Timestamp DateStart);

	/** Get Date Start.
	  * Date Start for this Order
	  */
	public Timestamp getDateStart();

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

    /** Column name EndTime */
    public static final String COLUMNNAME_EndTime = "EndTime";

	/** Set End Time.
	  * End of the time span
	  */
	public void setEndTime (Timestamp EndTime);

	/** Get End Time.
	  * End of the time span
	  */
	public Timestamp getEndTime();

    /** Column name HR_ContractLabor_ID */
    public static final String COLUMNNAME_HR_ContractLabor_ID = "HR_ContractLabor_ID";

	/** Set ContractLabor	  */
	public void setHR_ContractLabor_ID (int HR_ContractLabor_ID);

	/** Get ContractLabor	  */
	public int getHR_ContractLabor_ID();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee	  */
	public int getHR_Employee_ID();

	public I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_ItemLine_20_ID */
    public static final String COLUMNNAME_HR_ItemLine_20_ID = "HR_ItemLine_20_ID";

	/** Set Item Line 20	  */
	public void setHR_ItemLine_20_ID (int HR_ItemLine_20_ID);

	/** Get Item Line 20	  */
	public int getHR_ItemLine_20_ID();

	public I_HR_ItemLine getHR_ItemLine_20() throws RuntimeException;

    /** Column name HR_ItemLine_28_ID */
    public static final String COLUMNNAME_HR_ItemLine_28_ID = "HR_ItemLine_28_ID";

	/** Set Item Line 28	  */
	public void setHR_ItemLine_28_ID (int HR_ItemLine_28_ID);

	/** Get Item Line 28	  */
	public int getHR_ItemLine_28_ID();

	public I_HR_ItemLine getHR_ItemLine_28() throws RuntimeException;

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

    /** Column name NumTimes */
    public static final String COLUMNNAME_NumTimes = "NumTimes";

	/** Set NumTimes	  */
	public void setNumTimes (BigDecimal NumTimes);

	/** Get NumTimes	  */
	public BigDecimal getNumTimes();

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

    /** Column name SalaryPercent */
    public static final String COLUMNNAME_SalaryPercent = "SalaryPercent";

	/** Set SalaryPercent	  */
	public void setSalaryPercent (BigDecimal SalaryPercent);

	/** Get SalaryPercent	  */
	public BigDecimal getSalaryPercent();

    /** Column name SalaryRate */
    public static final String COLUMNNAME_SalaryRate = "SalaryRate";

	/** Set SalaryRate	  */
	public void setSalaryRate (BigDecimal SalaryRate);

	/** Get SalaryRate	  */
	public BigDecimal getSalaryRate();

    /** Column name StartTime */
    public static final String COLUMNNAME_StartTime = "StartTime";

	/** Set Start Time.
	  * Time started
	  */
	public void setStartTime (Timestamp StartTime);

	/** Get Start Time.
	  * Time started
	  */
	public Timestamp getStartTime();

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

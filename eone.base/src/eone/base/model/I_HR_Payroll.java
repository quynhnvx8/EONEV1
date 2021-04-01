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

/** Generated Interface for HR_Payroll
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_HR_Payroll 
{

    /** TableName=HR_Payroll */
    public static final String Table_Name = "HR_Payroll";

    /** AD_Table_ID=1200309 */
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

    /** Column name DateApproved */
    public static final String COLUMNNAME_DateApproved = "DateApproved";

	/** Set DateApproved	  */
	public void setDateApproved (Timestamp DateApproved);

	/** Get DateApproved	  */
	public Timestamp getDateApproved();

    /** Column name DateNext */
    public static final String COLUMNNAME_DateNext = "DateNext";

	/** Set DateNext	  */
	public void setDateNext (Timestamp DateNext);

	/** Get DateNext	  */
	public Timestamp getDateNext();

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

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee	  */
	public int getHR_Employee_ID();

	public I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_ItemLine_05_ID */
    public static final String COLUMNNAME_HR_ItemLine_05_ID = "HR_ItemLine_05_ID";

	/** Set Jobs	  */
	public void setHR_ItemLine_05_ID (int HR_ItemLine_05_ID);

	/** Get Jobs	  */
	public int getHR_ItemLine_05_ID();

	public I_HR_ItemLine getHR_ItemLine_05() throws RuntimeException;

    /** Column name HR_ItemLine_15_ID */
    public static final String COLUMNNAME_HR_ItemLine_15_ID = "HR_ItemLine_15_ID";

	/** Set Payroll Form	  */
	public void setHR_ItemLine_15_ID (int HR_ItemLine_15_ID);

	/** Get Payroll Form	  */
	public int getHR_ItemLine_15_ID();

	public I_HR_ItemLine getHR_ItemLine_15() throws RuntimeException;

    /** Column name HR_Payroll_ID */
    public static final String COLUMNNAME_HR_Payroll_ID = "HR_Payroll_ID";

	/** Set Payroll	  */
	public void setHR_Payroll_ID (int HR_Payroll_ID);

	/** Get Payroll	  */
	public int getHR_Payroll_ID();

    /** Column name HR_SalaryTable_ID */
    public static final String COLUMNNAME_HR_SalaryTable_ID = "HR_SalaryTable_ID";

	/** Set Salary Table 	  */
	public void setHR_SalaryTable_ID (int HR_SalaryTable_ID);

	/** Get Salary Table 	  */
	public int getHR_SalaryTable_ID();

	public I_HR_SalaryTable getHR_SalaryTable() throws RuntimeException;

    /** Column name HR_SalaryTableLine_ID */
    public static final String COLUMNNAME_HR_SalaryTableLine_ID = "HR_SalaryTableLine_ID";

	/** Set Table Salary Line	  */
	public void setHR_SalaryTableLine_ID (int HR_SalaryTableLine_ID);

	/** Get Table Salary Line	  */
	public int getHR_SalaryTableLine_ID();

	public I_HR_SalaryTableLine getHR_SalaryTableLine() throws RuntimeException;

    /** Column name IncentiveRate */
    public static final String COLUMNNAME_IncentiveRate = "IncentiveRate";

	/** Set IncentiveRate	  */
	public void setIncentiveRate (BigDecimal IncentiveRate);

	/** Get IncentiveRate	  */
	public BigDecimal getIncentiveRate();

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

    /** Column name IsSelected */
    public static final String COLUMNNAME_IsSelected = "IsSelected";

	/** Set Selected	  */
	public void setIsSelected (boolean IsSelected);

	/** Get Selected	  */
	public boolean isSelected();

    /** Column name LevelNo */
    public static final String COLUMNNAME_LevelNo = "LevelNo";

	/** Set Level no	  */
	public void setLevelNo (String LevelNo);

	/** Get Level no	  */
	public String getLevelNo();

    /** Column name LiabilityRate */
    public static final String COLUMNNAME_LiabilityRate = "LiabilityRate";

	/** Set LiabilityRate	  */
	public void setLiabilityRate (BigDecimal LiabilityRate);

	/** Get LiabilityRate	  */
	public BigDecimal getLiabilityRate();

    /** Column name PositionRate */
    public static final String COLUMNNAME_PositionRate = "PositionRate";

	/** Set PositionRate	  */
	public void setPositionRate (BigDecimal PositionRate);

	/** Get PositionRate	  */
	public BigDecimal getPositionRate();

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

    /** Column name SalaryBase */
    public static final String COLUMNNAME_SalaryBase = "SalaryBase";

	/** Set SalaryBase	  */
	public void setSalaryBase (BigDecimal SalaryBase);

	/** Get SalaryBase	  */
	public BigDecimal getSalaryBase();

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

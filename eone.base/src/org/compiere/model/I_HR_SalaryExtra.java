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

/** Generated Interface for HR_SalaryExtra
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_HR_SalaryExtra 
{

    /** TableName=HR_SalaryExtra */
    public static final String Table_Name = "HR_SalaryExtra";

    /** AD_Table_ID=1200344 */
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

    /** Column name FormulaSetup */
    public static final String COLUMNNAME_FormulaSetup = "FormulaSetup";

	/** Set FormulaSetup	  */
	public void setFormulaSetup (String FormulaSetup);

	/** Get FormulaSetup	  */
	public String getFormulaSetup();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee	  */
	public int getHR_Employee_ID();

	public I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_SalaryExtra_ID */
    public static final String COLUMNNAME_HR_SalaryExtra_ID = "HR_SalaryExtra_ID";

	/** Set Salary Extra	  */
	public void setHR_SalaryExtra_ID (int HR_SalaryExtra_ID);

	/** Get Salary Extra	  */
	public int getHR_SalaryExtra_ID();

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

    /** Column name Percent */
    public static final String COLUMNNAME_Percent = "Percent";

	/** Set Percent.
	  * Percentage
	  */
	public void setPercent (BigDecimal Percent);

	/** Get Percent.
	  * Percentage
	  */
	public BigDecimal getPercent();

    /** Column name TypeExtra */
    public static final String COLUMNNAME_TypeExtra = "TypeExtra";

	/** Set TypeExtra	  */
	public void setTypeExtra (String TypeExtra);

	/** Get TypeExtra	  */
	public String getTypeExtra();

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

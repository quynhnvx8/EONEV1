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

/** Generated Interface for HR_Literacy
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_HR_Literacy 
{

    /** TableName=HR_Literacy */
    public static final String Table_Name = "HR_Literacy";

    /** AD_Table_ID=1200311 */
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

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee	  */
	public int getHR_Employee_ID();

	public I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_Item_ID */
    public static final String COLUMNNAME_HR_Item_ID = "HR_Item_ID";

	/** Set Item	  */
	public void setHR_Item_ID (int HR_Item_ID);

	/** Get Item	  */
	public int getHR_Item_ID();

	public I_HR_Item getHR_Item() throws RuntimeException;

    /** Column name HR_ItemLine_10_ID */
    public static final String COLUMNNAME_HR_ItemLine_10_ID = "HR_ItemLine_10_ID";

	/** Set Address Training	  */
	public void setHR_ItemLine_10_ID (int HR_ItemLine_10_ID);

	/** Get Address Training	  */
	public int getHR_ItemLine_10_ID();

	public I_HR_ItemLine getHR_ItemLine_10() throws RuntimeException;

    /** Column name HR_ItemLine_12_ID */
    public static final String COLUMNNAME_HR_ItemLine_12_ID = "HR_ItemLine_12_ID";

	/** Set Training Form	  */
	public void setHR_ItemLine_12_ID (int HR_ItemLine_12_ID);

	/** Get Training Form	  */
	public int getHR_ItemLine_12_ID();

	public I_HR_ItemLine getHR_ItemLine_12() throws RuntimeException;

    /** Column name HR_ItemLine_38_ID */
    public static final String COLUMNNAME_HR_ItemLine_38_ID = "HR_ItemLine_38_ID";

	/** Set Ranked Graduate	  */
	public void setHR_ItemLine_38_ID (int HR_ItemLine_38_ID);

	/** Get Ranked Graduate	  */
	public int getHR_ItemLine_38_ID();

	public I_HR_ItemLine getHR_ItemLine_38() throws RuntimeException;

    /** Column name HR_ItemLine_ID */
    public static final String COLUMNNAME_HR_ItemLine_ID = "HR_ItemLine_ID";

	/** Set Item Line	  */
	public void setHR_ItemLine_ID (int HR_ItemLine_ID);

	/** Get Item Line	  */
	public int getHR_ItemLine_ID();

	public I_HR_ItemLine getHR_ItemLine() throws RuntimeException;

    /** Column name HR_Literacy_ID */
    public static final String COLUMNNAME_HR_Literacy_ID = "HR_Literacy_ID";

	/** Set Literacy	  */
	public void setHR_Literacy_ID (int HR_Literacy_ID);

	/** Get Literacy	  */
	public int getHR_Literacy_ID();

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
}

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

/** Generated Interface for HR_WorkDay
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_HR_WorkDay 
{

    /** TableName=HR_WorkDay */
    public static final String Table_Name = "HR_WorkDay";

    /** AD_Table_ID=1200341 */
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

    /** Column name EndTime1 */
    public static final String COLUMNNAME_EndTime1 = "EndTime1";

	/** Set End Time1.
	  * End of the time span
	  */
	public void setEndTime1 (Timestamp EndTime1);

	/** Get End Time1.
	  * End of the time span
	  */
	public Timestamp getEndTime1();

    /** Column name EndTime2 */
    public static final String COLUMNNAME_EndTime2 = "EndTime2";

	/** Set End Time2.
	  * End of the time span
	  */
	public void setEndTime2 (Timestamp EndTime2);

	/** Get End Time2.
	  * End of the time span
	  */
	public Timestamp getEndTime2();

    /** Column name EndTime3 */
    public static final String COLUMNNAME_EndTime3 = "EndTime3";

	/** Set End Time3.
	  * End of the time span
	  */
	public void setEndTime3 (Timestamp EndTime3);

	/** Get End Time3.
	  * End of the time span
	  */
	public Timestamp getEndTime3();

    /** Column name HR_WorkDay_ID */
    public static final String COLUMNNAME_HR_WorkDay_ID = "HR_WorkDay_ID";

	/** Set Work Day	  */
	public void setHR_WorkDay_ID (int HR_WorkDay_ID);

	/** Get Work Day	  */
	public int getHR_WorkDay_ID();

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

    /** Column name StartTime1 */
    public static final String COLUMNNAME_StartTime1 = "StartTime1";

	/** Set Start Time1.
	  * Time started
	  */
	public void setStartTime1 (Timestamp StartTime1);

	/** Get Start Time1.
	  * Time started
	  */
	public Timestamp getStartTime1();

    /** Column name StartTime2 */
    public static final String COLUMNNAME_StartTime2 = "StartTime2";

	/** Set Start Time2.
	  * Time started
	  */
	public void setStartTime2 (Timestamp StartTime2);

	/** Get Start Time2.
	  * Time started
	  */
	public Timestamp getStartTime2();

    /** Column name StartTime3 */
    public static final String COLUMNNAME_StartTime3 = "StartTime3";

	/** Set Start Time3.
	  * Time started
	  */
	public void setStartTime3 (Timestamp StartTime3);

	/** Get Start Time3.
	  * Time started
	  */
	public Timestamp getStartTime3();

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

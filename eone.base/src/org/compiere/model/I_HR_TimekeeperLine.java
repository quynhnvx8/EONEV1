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

/** Generated Interface for HR_TimekeeperLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_HR_TimekeeperLine 
{

    /** TableName=HR_TimekeeperLine */
    public static final String Table_Name = "HR_TimekeeperLine";

    /** AD_Table_ID=1200315 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

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

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException;

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

    /** Column name Day01 */
    public static final String COLUMNNAME_Day01 = "Day01";

	/** Set Day01	  */
	public void setDay01 (String Day01);

	/** Get Day01	  */
	public String getDay01();

    /** Column name Day02 */
    public static final String COLUMNNAME_Day02 = "Day02";

	/** Set Day02	  */
	public void setDay02 (String Day02);

	/** Get Day02	  */
	public String getDay02();

    /** Column name Day03 */
    public static final String COLUMNNAME_Day03 = "Day03";

	/** Set Day03	  */
	public void setDay03 (String Day03);

	/** Get Day03	  */
	public String getDay03();

    /** Column name Day04 */
    public static final String COLUMNNAME_Day04 = "Day04";

	/** Set Day04	  */
	public void setDay04 (String Day04);

	/** Get Day04	  */
	public String getDay04();

    /** Column name Day05 */
    public static final String COLUMNNAME_Day05 = "Day05";

	/** Set Day05	  */
	public void setDay05 (String Day05);

	/** Get Day05	  */
	public String getDay05();

    /** Column name Day06 */
    public static final String COLUMNNAME_Day06 = "Day06";

	/** Set Day06	  */
	public void setDay06 (String Day06);

	/** Get Day06	  */
	public String getDay06();

    /** Column name Day07 */
    public static final String COLUMNNAME_Day07 = "Day07";

	/** Set Day07	  */
	public void setDay07 (String Day07);

	/** Get Day07	  */
	public String getDay07();

    /** Column name Day08 */
    public static final String COLUMNNAME_Day08 = "Day08";

	/** Set Day08	  */
	public void setDay08 (String Day08);

	/** Get Day08	  */
	public String getDay08();

    /** Column name Day09 */
    public static final String COLUMNNAME_Day09 = "Day09";

	/** Set Day09	  */
	public void setDay09 (String Day09);

	/** Get Day09	  */
	public String getDay09();

    /** Column name Day10 */
    public static final String COLUMNNAME_Day10 = "Day10";

	/** Set Day10	  */
	public void setDay10 (String Day10);

	/** Get Day10	  */
	public String getDay10();

    /** Column name Day11 */
    public static final String COLUMNNAME_Day11 = "Day11";

	/** Set Day11	  */
	public void setDay11 (String Day11);

	/** Get Day11	  */
	public String getDay11();

    /** Column name Day12 */
    public static final String COLUMNNAME_Day12 = "Day12";

	/** Set Day12	  */
	public void setDay12 (String Day12);

	/** Get Day12	  */
	public String getDay12();

    /** Column name Day13 */
    public static final String COLUMNNAME_Day13 = "Day13";

	/** Set Day13	  */
	public void setDay13 (String Day13);

	/** Get Day13	  */
	public String getDay13();

    /** Column name Day14 */
    public static final String COLUMNNAME_Day14 = "Day14";

	/** Set Day14	  */
	public void setDay14 (String Day14);

	/** Get Day14	  */
	public String getDay14();

    /** Column name Day15 */
    public static final String COLUMNNAME_Day15 = "Day15";

	/** Set Day15	  */
	public void setDay15 (String Day15);

	/** Get Day15	  */
	public String getDay15();

    /** Column name Day16 */
    public static final String COLUMNNAME_Day16 = "Day16";

	/** Set Day16	  */
	public void setDay16 (String Day16);

	/** Get Day16	  */
	public String getDay16();

    /** Column name Day17 */
    public static final String COLUMNNAME_Day17 = "Day17";

	/** Set Day17	  */
	public void setDay17 (String Day17);

	/** Get Day17	  */
	public String getDay17();

    /** Column name Day18 */
    public static final String COLUMNNAME_Day18 = "Day18";

	/** Set Day18	  */
	public void setDay18 (String Day18);

	/** Get Day18	  */
	public String getDay18();

    /** Column name Day19 */
    public static final String COLUMNNAME_Day19 = "Day19";

	/** Set Day19	  */
	public void setDay19 (String Day19);

	/** Get Day19	  */
	public String getDay19();

    /** Column name Day20 */
    public static final String COLUMNNAME_Day20 = "Day20";

	/** Set Day20	  */
	public void setDay20 (String Day20);

	/** Get Day20	  */
	public String getDay20();

    /** Column name Day21 */
    public static final String COLUMNNAME_Day21 = "Day21";

	/** Set Day21	  */
	public void setDay21 (String Day21);

	/** Get Day21	  */
	public String getDay21();

    /** Column name Day22 */
    public static final String COLUMNNAME_Day22 = "Day22";

	/** Set Day22	  */
	public void setDay22 (String Day22);

	/** Get Day22	  */
	public String getDay22();

    /** Column name Day23 */
    public static final String COLUMNNAME_Day23 = "Day23";

	/** Set Day23	  */
	public void setDay23 (String Day23);

	/** Get Day23	  */
	public String getDay23();

    /** Column name Day24 */
    public static final String COLUMNNAME_Day24 = "Day24";

	/** Set Day24	  */
	public void setDay24 (String Day24);

	/** Get Day24	  */
	public String getDay24();

    /** Column name Day25 */
    public static final String COLUMNNAME_Day25 = "Day25";

	/** Set Day25	  */
	public void setDay25 (String Day25);

	/** Get Day25	  */
	public String getDay25();

    /** Column name Day26 */
    public static final String COLUMNNAME_Day26 = "Day26";

	/** Set Day26	  */
	public void setDay26 (String Day26);

	/** Get Day26	  */
	public String getDay26();

    /** Column name Day27 */
    public static final String COLUMNNAME_Day27 = "Day27";

	/** Set Day27	  */
	public void setDay27 (String Day27);

	/** Get Day27	  */
	public String getDay27();

    /** Column name Day28 */
    public static final String COLUMNNAME_Day28 = "Day28";

	/** Set Day28	  */
	public void setDay28 (String Day28);

	/** Get Day28	  */
	public String getDay28();

    /** Column name Day29 */
    public static final String COLUMNNAME_Day29 = "Day29";

	/** Set Day29	  */
	public void setDay29 (String Day29);

	/** Get Day29	  */
	public String getDay29();

    /** Column name Day30 */
    public static final String COLUMNNAME_Day30 = "Day30";

	/** Set Day30	  */
	public void setDay30 (String Day30);

	/** Get Day30	  */
	public String getDay30();

    /** Column name Day31 */
    public static final String COLUMNNAME_Day31 = "Day31";

	/** Set Day31	  */
	public void setDay31 (String Day31);

	/** Get Day31	  */
	public String getDay31();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee	  */
	public int getHR_Employee_ID();

	public I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_Timekeeper_ID */
    public static final String COLUMNNAME_HR_Timekeeper_ID = "HR_Timekeeper_ID";

	/** Set Timekeeper	  */
	public void setHR_Timekeeper_ID (int HR_Timekeeper_ID);

	/** Get Timekeeper	  */
	public int getHR_Timekeeper_ID();

	public I_HR_Timekeeper getHR_Timekeeper() throws RuntimeException;

    /** Column name HR_TimekeeperLine_ID */
    public static final String COLUMNNAME_HR_TimekeeperLine_ID = "HR_TimekeeperLine_ID";

	/** Set Timekeeper Line	  */
	public void setHR_TimekeeperLine_ID (int HR_TimekeeperLine_ID);

	/** Get Timekeeper Line	  */
	public int getHR_TimekeeperLine_ID();

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

    /** Column name TotalDayOff */
    public static final String COLUMNNAME_TotalDayOff = "TotalDayOff";

	/** Set TotalDayOff	  */
	public void setTotalDayOff (BigDecimal TotalDayOff);

	/** Get TotalDayOff	  */
	public BigDecimal getTotalDayOff();

    /** Column name TotalWorkDay */
    public static final String COLUMNNAME_TotalWorkDay = "TotalWorkDay";

	/** Set TotalWorkDay	  */
	public void setTotalWorkDay (BigDecimal TotalWorkDay);

	/** Get TotalWorkDay	  */
	public BigDecimal getTotalWorkDay();

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

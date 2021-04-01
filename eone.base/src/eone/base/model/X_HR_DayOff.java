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
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for HR_DayOff
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_DayOff extends PO implements I_HR_DayOff, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201225L;

    /** Standard Constructor */
    public X_HR_DayOff (Properties ctx, int HR_DayOff_ID, String trxName)
    {
      super (ctx, HR_DayOff_ID, trxName);
      /** if (HR_DayOff_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_DayOff (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_HR_DayOff[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_Department getAD_Department() throws RuntimeException
    {
		return (I_AD_Department)MTable.get(getCtx(), I_AD_Department.Table_Name)
			.getPO(getAD_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param AD_Department_ID Department	  */
	public void setAD_Department_ID (int AD_Department_ID)
	{
		if (AD_Department_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Department_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Department_ID, Integer.valueOf(AD_Department_ID));
	}

	/** Get Department.
		@return Department	  */
	public int getAD_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Address.
		@param Address Address	  */
	public void setAddress (String Address)
	{
		set_Value (COLUMNNAME_Address, Address);
	}

	/** Get Address.
		@return Address	  */
	public String getAddress () 
	{
		return (String)get_Value(COLUMNNAME_Address);
	}

	/** Set Approved.
		@param Approved Approved	  */
	public void setApproved (String Approved)
	{
		set_Value (COLUMNNAME_Approved, Approved);
	}

	/** Get Approved.
		@return Approved	  */
	public String getApproved () 
	{
		return (String)get_Value(COLUMNNAME_Approved);
	}

	/** Set Canceled.
		@param Canceled Canceled	  */
	public void setCanceled (String Canceled)
	{
		set_Value (COLUMNNAME_Canceled, Canceled);
	}

	/** Get Canceled.
		@return Canceled	  */
	public String getCanceled () 
	{
		return (String)get_Value(COLUMNNAME_Canceled);
	}

	/** Set DateDecision.
		@param DateDecision DateDecision	  */
	public void setDateDecision (Timestamp DateDecision)
	{
		set_Value (COLUMNNAME_DateDecision, DateDecision);
	}

	/** Get DateDecision.
		@return DateDecision	  */
	public Timestamp getDateDecision () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDecision);
	}

	/** Set DateEffective.
		@param DateEffective DateEffective	  */
	public void setDateEffective (Timestamp DateEffective)
	{
		set_Value (COLUMNNAME_DateEffective, DateEffective);
	}

	/** Get DateEffective.
		@return DateEffective	  */
	public Timestamp getDateEffective () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateEffective);
	}

	/** NP4 = P4 */
	public static final String DAYOFFTYPE_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAYOFFTYPE_KLCP = "KL";
	/** CO = CO */
	public static final String DAYOFFTYPE_CO = "CO";
	/** NL = NL */
	public static final String DAYOFFTYPE_NL = "NL";
	/** NO = NO */
	public static final String DAYOFFTYPE_NO = "NO";
	/** HH = HH */
	public static final String DAYOFFTYPE_HH = "HH";
	/** TS = TS */
	public static final String DAYOFFTYPE_TS = "TS";
	/** W8 = W8 */
	public static final String DAYOFFTYPE_W8 = "W8";
	/** W4 = W4 */
	public static final String DAYOFFTYPE_W4 = "W4";
	/** CT = CT */
	public static final String DAYOFFTYPE_CT = "CT";
	/** HO = HO */
	public static final String DAYOFFTYPE_HO = "HO";
	/** KLKP = KP */
	public static final String DAYOFFTYPE_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAYOFFTYPE_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAYOFFTYPE_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAYOFFTYPE_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAYOFFTYPE_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAYOFFTYPE_NP8 = "P8";
	/** Set DayOffType.
		@param DayOffType DayOffType	  */
	public void setDayOffType (String DayOffType)
	{

		set_Value (COLUMNNAME_DayOffType, DayOffType);
	}

	/** Get DayOffType.
		@return DayOffType	  */
	public String getDayOffType () 
	{
		return (String)get_Value(COLUMNNAME_DayOffType);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Reject = RE */
	public static final String DOCSTATUS_Reject = "RE";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Duration.
		@param Duration 
		Normal Duration in Duration Unit
	  */
	public void setDuration (BigDecimal Duration)
	{
		set_ValueNoCheck (COLUMNNAME_Duration, Duration);
	}

	/** Get Duration.
		@return Normal Duration in Duration Unit
	  */
	public BigDecimal getDuration () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Duration);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set End Time.
		@param EndTime 
		End of the time span
	  */
	public void setEndTime (Timestamp EndTime)
	{
		set_Value (COLUMNNAME_EndTime, EndTime);
	}

	/** Get End Time.
		@return End of the time span
	  */
	public Timestamp getEndTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime);
	}

	/** Set Day Off.
		@param HR_DayOff_ID Day Off	  */
	public void setHR_DayOff_ID (int HR_DayOff_ID)
	{
		if (HR_DayOff_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_DayOff_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_DayOff_ID, Integer.valueOf(HR_DayOff_ID));
	}

	/** Get Day Off.
		@return Day Off	  */
	public int getHR_DayOff_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_DayOff_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Employee getHR_Employee() throws RuntimeException
    {
		return (I_HR_Employee)MTable.get(getCtx(), I_HR_Employee.Table_Name)
			.getPO(getHR_Employee_ID(), get_TrxName());	}

	/** Set Employee.
		@param HR_Employee_ID Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Employee.
		@return Employee	  */
	public int getHR_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set NoDecision.
		@param NoDecision NoDecision	  */
	public void setNoDecision (String NoDecision)
	{
		set_Value (COLUMNNAME_NoDecision, NoDecision);
	}

	/** Get NoDecision.
		@return NoDecision	  */
	public String getNoDecision () 
	{
		return (String)get_Value(COLUMNNAME_NoDecision);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ReasonText.
		@param ReasonText ReasonText	  */
	public void setReasonText (String ReasonText)
	{
		set_Value (COLUMNNAME_ReasonText, ReasonText);
	}

	/** Get ReasonText.
		@return ReasonText	  */
	public String getReasonText () 
	{
		return (String)get_Value(COLUMNNAME_ReasonText);
	}

	/** Set Start Time.
		@param StartTime 
		Time started
	  */
	public void setStartTime (Timestamp StartTime)
	{
		set_Value (COLUMNNAME_StartTime, StartTime);
	}

	/** Get Start Time.
		@return Time started
	  */
	public Timestamp getStartTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime);
	}
}
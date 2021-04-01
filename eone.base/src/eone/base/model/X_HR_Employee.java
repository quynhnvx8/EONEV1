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
import org.compiere.util.KeyNamePair;

/** Generated Model for HR_Employee
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_Employee extends PO implements I_HR_Employee, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201225L;

    /** Standard Constructor */
    public X_HR_Employee (Properties ctx, int HR_Employee_ID, String trxName)
    {
      super (ctx, HR_Employee_ID, trxName);
      /** if (HR_Employee_ID == 0)
        {
			setHR_Employee_ID (0);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_HR_Employee (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
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
      StringBuilder sb = new StringBuilder ("X_HR_Employee[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
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
			set_Value (COLUMNNAME_AD_Department_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Department_ID, Integer.valueOf(AD_Department_ID));
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

	/** Set Image.
		@param AD_Image_ID 
		Image or Icon
	  */
	public void setAD_Image_ID (int AD_Image_ID)
	{
		if (AD_Image_ID < 1) 
			set_Value (COLUMNNAME_AD_Image_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Image_ID, Integer.valueOf(AD_Image_ID));
	}

	/** Get Image.
		@return Image or Icon
	  */
	public int getAD_Image_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Image_ID);
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

	/** Set Address 1.
		@param Address1 
		Address line 1 for this location
	  */
	public void setAddress1 (String Address1)
	{
		set_Value (COLUMNNAME_Address1, Address1);
	}

	/** Get Address 1.
		@return Address line 1 for this location
	  */
	public String getAddress1 () 
	{
		return (String)get_Value(COLUMNNAME_Address1);
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

	/** Set Birthday.
		@param Birthday 
		Birthday or Anniversary day
	  */
	public void setBirthday (Timestamp Birthday)
	{
		set_Value (COLUMNNAME_Birthday, Birthday);
	}

	/** Get Birthday.
		@return Birthday or Anniversary day
	  */
	public Timestamp getBirthday () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Birthday);
	}

	/** Set BirthPlace.
		@param BirthPlace BirthPlace	  */
	public void setBirthPlace (String BirthPlace)
	{
		set_Value (COLUMNNAME_BirthPlace, BirthPlace);
	}

	/** Get BirthPlace.
		@return BirthPlace	  */
	public String getBirthPlace () 
	{
		return (String)get_Value(COLUMNNAME_BirthPlace);
	}

	/** NONE = NO */
	public static final String BLOODTYPE_NONE = "NO";
	/** A = A */
	public static final String BLOODTYPE_A = "A";
	/** B = B */
	public static final String BLOODTYPE_B = "B";
	/** AB = AB */
	public static final String BLOODTYPE_AB = "AB";
	/** O = O */
	public static final String BLOODTYPE_O = "O";
	/** Set BloodType.
		@param BloodType BloodType	  */
	public void setBloodType (String BloodType)
	{

		set_Value (COLUMNNAME_BloodType, BloodType);
	}

	/** Get BloodType.
		@return BloodType	  */
	public String getBloodType () 
	{
		return (String)get_Value(COLUMNNAME_BloodType);
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

	/** Set CardID.
		@param CardID CardID	  */
	public void setCardID (String CardID)
	{
		set_Value (COLUMNNAME_CardID, CardID);
	}

	/** Get CardID.
		@return CardID	  */
	public String getCardID () 
	{
		return (String)get_Value(COLUMNNAME_CardID);
	}

	/** Set Code.
		@param Code 
		Validation Code
	  */
	public void setCode (String Code)
	{
		set_Value (COLUMNNAME_Code, Code);
	}

	/** Get Code.
		@return Validation Code
	  */
	public String getCode () 
	{
		return (String)get_Value(COLUMNNAME_Code);
	}

	/** Set DateIssue.
		@param DateIssue DateIssue	  */
	public void setDateIssue (Timestamp DateIssue)
	{
		set_Value (COLUMNNAME_DateIssue, DateIssue);
	}

	/** Get DateIssue.
		@return DateIssue	  */
	public Timestamp getDateIssue () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateIssue);
	}

	/** Set DatePayroll.
		@param DatePayroll DatePayroll	  */
	public void setDatePayroll (Timestamp DatePayroll)
	{
		set_Value (COLUMNNAME_DatePayroll, DatePayroll);
	}

	/** Get DatePayroll.
		@return DatePayroll	  */
	public Timestamp getDatePayroll () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePayroll);
	}

	/** Set DateTest.
		@param DateTest DateTest	  */
	public void setDateTest (Timestamp DateTest)
	{
		set_Value (COLUMNNAME_DateTest, DateTest);
	}

	/** Get DateTest.
		@return DateTest	  */
	public Timestamp getDateTest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTest);
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

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail () 
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
	}

	/** Female = F */
	public static final String GENDER_Female = "F";
	/** Male = M */
	public static final String GENDER_Male = "M";
	/** None = N */
	public static final String GENDER_None = "N";
	/** Set Gender.
		@param Gender Gender	  */
	public void setGender (String Gender)
	{

		set_Value (COLUMNNAME_Gender, Gender);
	}

	/** Get Gender.
		@return Gender	  */
	public String getGender () 
	{
		return (String)get_Value(COLUMNNAME_Gender);
	}

	/** Set Healthy.
		@param Healthy Healthy	  */
	public void setHealthy (String Healthy)
	{
		set_Value (COLUMNNAME_Healthy, Healthy);
	}

	/** Get Healthy.
		@return Healthy	  */
	public String getHealthy () 
	{
		return (String)get_Value(COLUMNNAME_Healthy);
	}

	/** Set Height.
		@param Height Height	  */
	public void setHeight (BigDecimal Height)
	{
		set_Value (COLUMNNAME_Height, Height);
	}

	/** Get Height.
		@return Height	  */
	public BigDecimal getHeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Height);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HomeTown.
		@param HomeTown HomeTown	  */
	public void setHomeTown (String HomeTown)
	{
		set_Value (COLUMNNAME_HomeTown, HomeTown);
	}

	/** Get HomeTown.
		@return HomeTown	  */
	public String getHomeTown () 
	{
		return (String)get_Value(COLUMNNAME_HomeTown);
	}

	/** Set Employee.
		@param HR_Employee_ID Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
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

	public I_HR_ItemLine getHR_ItemLine_05() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_05_ID(), get_TrxName());	}

	/** Set Jobs.
		@param HR_ItemLine_05_ID Jobs	  */
	public void setHR_ItemLine_05_ID (int HR_ItemLine_05_ID)
	{
		if (HR_ItemLine_05_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_05_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_05_ID, Integer.valueOf(HR_ItemLine_05_ID));
	}

	/** Get Jobs.
		@return Jobs	  */
	public int getHR_ItemLine_05_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_05_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_06() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_06_ID(), get_TrxName());	}

	/** Set Position.
		@param HR_ItemLine_06_ID Position	  */
	public void setHR_ItemLine_06_ID (int HR_ItemLine_06_ID)
	{
		if (HR_ItemLine_06_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_06_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_06_ID, Integer.valueOf(HR_ItemLine_06_ID));
	}

	/** Get Position.
		@return Position	  */
	public int getHR_ItemLine_06_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_06_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_09() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_09_ID(), get_TrxName());	}

	/** Set Item Line 09.
		@param HR_ItemLine_09_ID Item Line 09	  */
	public void setHR_ItemLine_09_ID (int HR_ItemLine_09_ID)
	{
		if (HR_ItemLine_09_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_09_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_09_ID, Integer.valueOf(HR_ItemLine_09_ID));
	}

	/** Get Item Line 09.
		@return Item Line 09	  */
	public int getHR_ItemLine_09_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_09_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_29() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_29_ID(), get_TrxName());	}

	/** Set National.
		@param HR_ItemLine_29_ID National	  */
	public void setHR_ItemLine_29_ID (int HR_ItemLine_29_ID)
	{
		if (HR_ItemLine_29_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_29_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_29_ID, Integer.valueOf(HR_ItemLine_29_ID));
	}

	/** Get National.
		@return National	  */
	public int getHR_ItemLine_29_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_29_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_30() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_30_ID(), get_TrxName());	}

	/** Set Item Line 30.
		@param HR_ItemLine_30_ID Item Line 30	  */
	public void setHR_ItemLine_30_ID (int HR_ItemLine_30_ID)
	{
		if (HR_ItemLine_30_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_30_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_30_ID, Integer.valueOf(HR_ItemLine_30_ID));
	}

	/** Get Item Line 30.
		@return Item Line 30	  */
	public int getHR_ItemLine_30_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_30_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_31() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_31_ID(), get_TrxName());	}

	/** Set Item Line 31.
		@param HR_ItemLine_31_ID Item Line 31	  */
	public void setHR_ItemLine_31_ID (int HR_ItemLine_31_ID)
	{
		if (HR_ItemLine_31_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_31_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_31_ID, Integer.valueOf(HR_ItemLine_31_ID));
	}

	/** Get Item Line 31.
		@return Item Line 31	  */
	public int getHR_ItemLine_31_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_31_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_39() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_39_ID(), get_TrxName());	}

	/** Set Item Line 39.
		@param HR_ItemLine_39_ID Item Line 39	  */
	public void setHR_ItemLine_39_ID (int HR_ItemLine_39_ID)
	{
		if (HR_ItemLine_39_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_39_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_39_ID, Integer.valueOf(HR_ItemLine_39_ID));
	}

	/** Get Item Line 39.
		@return Item Line 39	  */
	public int getHR_ItemLine_39_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_39_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Image URL.
		@param ImageURL 
		URL of  image
	  */
	public void setImageURL (String ImageURL)
	{
		set_Value (COLUMNNAME_ImageURL, ImageURL);
	}

	/** Get Image URL.
		@return URL of  image
	  */
	public String getImageURL () 
	{
		return (String)get_Value(COLUMNNAME_ImageURL);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Name 2.
		@param Name2 
		Additional Name
	  */
	public void setName2 (String Name2)
	{
		set_Value (COLUMNNAME_Name2, Name2);
	}

	/** Get Name 2.
		@return Additional Name
	  */
	public String getName2 () 
	{
		return (String)get_Value(COLUMNNAME_Name2);
	}

	/** Set Phone.
		@param Phone 
		Identifies a telephone number
	  */
	public void setPhone (String Phone)
	{
		set_Value (COLUMNNAME_Phone, Phone);
	}

	/** Get Phone.
		@return Identifies a telephone number
	  */
	public String getPhone () 
	{
		return (String)get_Value(COLUMNNAME_Phone);
	}

	/** Set 2nd Phone.
		@param Phone2 
		Identifies an alternate telephone number.
	  */
	public void setPhone2 (String Phone2)
	{
		set_Value (COLUMNNAME_Phone2, Phone2);
	}

	/** Get 2nd Phone.
		@return Identifies an alternate telephone number.
	  */
	public String getPhone2 () 
	{
		return (String)get_Value(COLUMNNAME_Phone2);
	}

	/** Set PlaceIssue.
		@param PlaceIssue PlaceIssue	  */
	public void setPlaceIssue (String PlaceIssue)
	{
		set_Value (COLUMNNAME_PlaceIssue, PlaceIssue);
	}

	/** Get PlaceIssue.
		@return PlaceIssue	  */
	public String getPlaceIssue () 
	{
		return (String)get_Value(COLUMNNAME_PlaceIssue);
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

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
	}

	/** Set User Name.
		@param UserName User Name	  */
	public void setUserName (String UserName)
	{
		set_Value (COLUMNNAME_UserName, UserName);
	}

	/** Get User Name.
		@return User Name	  */
	public String getUserName () 
	{
		return (String)get_Value(COLUMNNAME_UserName);
	}

	/** Set Weight.
		@param Weight 
		Weight of a product
	  */
	public void setWeight (BigDecimal Weight)
	{
		set_Value (COLUMNNAME_Weight, Weight);
	}

	/** Get Weight.
		@return Weight of a product
	  */
	public BigDecimal getWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
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
package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for C_BPartnerInfo
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_BPartnerInfo extends PO implements I_C_BPartnerInfo, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210308L;

    /** Standard Constructor */
    public X_C_BPartnerInfo (Properties ctx, int C_BPartnerInfo_ID, String trxName)
    {
      super (ctx, C_BPartnerInfo_ID, trxName);
      /** if (C_BPartnerInfo_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_BPartnerInfo (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_BPartnerInfo[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BPartner Info.
		@param C_BPartnerInfo_ID BPartner Info	  */
	public void setC_BPartnerInfo_ID (int C_BPartnerInfo_ID)
	{
		if (C_BPartnerInfo_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartnerInfo_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartnerInfo_ID, Integer.valueOf(C_BPartnerInfo_ID));
	}

	/** Get BPartner Info.
		@return BPartner Info	  */
	public int getC_BPartnerInfo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerInfo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Default.
		@param IsDefault 
		Default value
	  */
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	public boolean isDefault () 
	{
		Object oo = get_Value(COLUMNNAME_IsDefault);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Code.
		@param Value 
		Code
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Code.
		@return Code
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
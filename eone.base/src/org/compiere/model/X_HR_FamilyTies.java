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

/** Generated Model for HR_FamilyTies
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_FamilyTies extends PO implements I_HR_FamilyTies, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201030L;

    /** Standard Constructor */
    public X_HR_FamilyTies (Properties ctx, int HR_FamilyTies_ID, String trxName)
    {
      super (ctx, HR_FamilyTies_ID, trxName);
      /** if (HR_FamilyTies_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_FamilyTies (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_FamilyTies[")
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

	/** Set BirthYear.
		@param BirthYear BirthYear	  */
	public void setBirthYear (String BirthYear)
	{
		set_Value (COLUMNNAME_BirthYear, BirthYear);
	}

	/** Get BirthYear.
		@return BirthYear	  */
	public String getBirthYear () 
	{
		return (String)get_Value(COLUMNNAME_BirthYear);
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

	/** Set Family Ties.
		@param HR_FamilyTies_ID Family Ties	  */
	public void setHR_FamilyTies_ID (int HR_FamilyTies_ID)
	{
		if (HR_FamilyTies_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_FamilyTies_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_FamilyTies_ID, Integer.valueOf(HR_FamilyTies_ID));
	}

	/** Get Family Ties.
		@return Family Ties	  */
	public int getHR_FamilyTies_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_FamilyTies_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_24() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_24_ID(), get_TrxName());	}

	/** Set Ralative.
		@param HR_ItemLine_24_ID Ralative	  */
	public void setHR_ItemLine_24_ID (int HR_ItemLine_24_ID)
	{
		if (HR_ItemLine_24_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_24_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_24_ID, Integer.valueOf(HR_ItemLine_24_ID));
	}

	/** Get Ralative.
		@return Ralative	  */
	public int getHR_ItemLine_24_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_24_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set IsPersonDependent.
		@param IsPersonDependent IsPersonDependent	  */
	public void setIsPersonDependent (boolean IsPersonDependent)
	{
		set_Value (COLUMNNAME_IsPersonDependent, Boolean.valueOf(IsPersonDependent));
	}

	/** Get IsPersonDependent.
		@return IsPersonDependent	  */
	public boolean isPersonDependent () 
	{
		Object oo = get_Value(COLUMNNAME_IsPersonDependent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set JobName.
		@param JobName JobName	  */
	public void setJobName (String JobName)
	{
		set_Value (COLUMNNAME_JobName, JobName);
	}

	/** Get JobName.
		@return JobName	  */
	public String getJobName () 
	{
		return (String)get_Value(COLUMNNAME_JobName);
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

	/** Set TaxCodePerson.
		@param TaxCodePerson TaxCodePerson	  */
	public void setTaxCodePerson (String TaxCodePerson)
	{
		set_Value (COLUMNNAME_TaxCodePerson, TaxCodePerson);
	}

	/** Get TaxCodePerson.
		@return TaxCodePerson	  */
	public String getTaxCodePerson () 
	{
		return (String)get_Value(COLUMNNAME_TaxCodePerson);
	}

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo 
		Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
	}

	/** Set WorkPlace.
		@param WorkPlace WorkPlace	  */
	public void setWorkPlace (String WorkPlace)
	{
		set_Value (COLUMNNAME_WorkPlace, WorkPlace);
	}

	/** Get WorkPlace.
		@return WorkPlace	  */
	public String getWorkPlace () 
	{
		return (String)get_Value(COLUMNNAME_WorkPlace);
	}
}
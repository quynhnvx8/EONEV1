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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for HR_Config
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_Config extends PO implements I_HR_Config, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201030L;

    /** Standard Constructor */
    public X_HR_Config (Properties ctx, int HR_Config_ID, String trxName)
    {
      super (ctx, HR_Config_ID, trxName);
      /** if (HR_Config_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_Config (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_Config[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set HR Config.
		@param HR_Config_ID HR Config	  */
	public void setHR_Config_ID (int HR_Config_ID)
	{
		if (HR_Config_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Config_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Config_ID, Integer.valueOf(HR_Config_ID));
	}

	/** Get HR Config.
		@return HR Config	  */
	public int getHR_Config_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Config_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** BaseSalaryMin = BaseSalaryMin */
	public static final String NAME_BaseSalaryMin = "BaseSalaryMin";
	/** BaseSalaryPay = BaseSalaryPay */
	public static final String NAME_BaseSalaryPay = "BaseSalaryPay";
	/** MaleRetireAge = MaleRetireAge */
	public static final String NAME_MaleRetireAge = "MaleRetireAge";
	/** FemaleRetireAge = FemaleRetireAge */
	public static final String NAME_FemaleRetireAge = "FemaleRetireAge";
	/** WorkingTimeStandard = WorkingTimeStandard */
	public static final String NAME_WorkingTimeStandard = "WorkingTimeStandard";
	/** HealthInsurance = HealthInsurance */
	public static final String NAME_HealthInsurance = "HealthInsurance";
	/** UnemploymentInsurance = UnemploymentInsurance */
	public static final String NAME_UnemploymentInsurance = "UnemploymentInsurance";
	/** SocialInsurance = SocialInsurance */
	public static final String NAME_SocialInsurance = "SocialInsurance";
	/** PersonalDeduction = PersonalDeduction */
	public static final String NAME_PersonalDeduction = "PersonalDeduction";
	/** DependentDeduction = DependentDeduction */
	public static final String NAME_DependentDeduction = "DependentDeduction";
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

	/** Set Value.
		@param ValueNumber 
		Numeric Value
	  */
	public void setValueNumber (BigDecimal ValueNumber)
	{
		set_Value (COLUMNNAME_ValueNumber, ValueNumber);
	}

	/** Get Value.
		@return Numeric Value
	  */
	public BigDecimal getValueNumber () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ValueNumber);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
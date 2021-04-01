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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for HR_TaxPersonal
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_TaxPersonal extends PO implements I_HR_TaxPersonal, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201030L;

    /** Standard Constructor */
    public X_HR_TaxPersonal (Properties ctx, int HR_TaxPersonal_ID, String trxName)
    {
      super (ctx, HR_TaxPersonal_ID, trxName);
      /** if (HR_TaxPersonal_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_TaxPersonal (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_TaxPersonal[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set FormulaSetup.
		@param FormulaSetup FormulaSetup	  */
	public void setFormulaSetup (String FormulaSetup)
	{
		set_Value (COLUMNNAME_FormulaSetup, FormulaSetup);
	}

	/** Get FormulaSetup.
		@return FormulaSetup	  */
	public String getFormulaSetup () 
	{
		return (String)get_Value(COLUMNNAME_FormulaSetup);
	}

	/** Set Tax Personal.
		@param HR_TaxPersonal_ID Tax Personal	  */
	public void setHR_TaxPersonal_ID (int HR_TaxPersonal_ID)
	{
		if (HR_TaxPersonal_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_TaxPersonal_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_TaxPersonal_ID, Integer.valueOf(HR_TaxPersonal_ID));
	}

	/** Get Tax Personal.
		@return Tax Personal	  */
	public int getHR_TaxPersonal_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_TaxPersonal_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Max Value.
		@param MaxValue Max Value	  */
	public void setMaxValue (BigDecimal MaxValue)
	{
		set_Value (COLUMNNAME_MaxValue, MaxValue);
	}

	/** Get Max Value.
		@return Max Value	  */
	public BigDecimal getMaxValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MaxValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Min Value.
		@param MinValue Min Value	  */
	public void setMinValue (BigDecimal MinValue)
	{
		set_Value (COLUMNNAME_MinValue, MinValue);
	}

	/** Get Min Value.
		@return Min Value	  */
	public BigDecimal getMinValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
}
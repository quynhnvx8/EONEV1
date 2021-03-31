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

/** Generated Model for HR_SalaryTableLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_SalaryTableLine extends PO implements I_HR_SalaryTableLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201030L;

    /** Standard Constructor */
    public X_HR_SalaryTableLine (Properties ctx, int HR_SalaryTableLine_ID, String trxName)
    {
      super (ctx, HR_SalaryTableLine_ID, trxName);
      /** if (HR_SalaryTableLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_SalaryTableLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_SalaryTableLine[")
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

	public I_HR_SalaryTable getHR_SalaryTable() throws RuntimeException
    {
		return (I_HR_SalaryTable)MTable.get(getCtx(), I_HR_SalaryTable.Table_Name)
			.getPO(getHR_SalaryTable_ID(), get_TrxName());	}

	/** Set Salary Table .
		@param HR_SalaryTable_ID Salary Table 	  */
	public void setHR_SalaryTable_ID (int HR_SalaryTable_ID)
	{
		if (HR_SalaryTable_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_SalaryTable_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_SalaryTable_ID, Integer.valueOf(HR_SalaryTable_ID));
	}

	/** Get Salary Table .
		@return Salary Table 	  */
	public int getHR_SalaryTable_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_SalaryTable_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Table Salary Line.
		@param HR_SalaryTableLine_ID Table Salary Line	  */
	public void setHR_SalaryTableLine_ID (int HR_SalaryTableLine_ID)
	{
		if (HR_SalaryTableLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_SalaryTableLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_SalaryTableLine_ID, Integer.valueOf(HR_SalaryTableLine_ID));
	}

	/** Get Table Salary Line.
		@return Table Salary Line	  */
	public int getHR_SalaryTableLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_SalaryTableLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Percent.
		@param Percent 
		Percentage
	  */
	public void setPercent (BigDecimal Percent)
	{
		set_Value (COLUMNNAME_Percent, Percent);
	}

	/** Get Percent.
		@return Percentage
	  */
	public BigDecimal getPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** ByRate = ByRate */
	public static final String TYPEEXTRA_ByRate = "ByRate";
	/** FixExtra = FixExtra */
	public static final String TYPEEXTRA_FixExtra = "FixExtra";
	/** Set TypeExtra.
		@param TypeExtra TypeExtra	  */
	public void setTypeExtra (String TypeExtra)
	{

		set_Value (COLUMNNAME_TypeExtra, TypeExtra);
	}

	/** Get TypeExtra.
		@return TypeExtra	  */
	public String getTypeExtra () 
	{
		return (String)get_Value(COLUMNNAME_TypeExtra);
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
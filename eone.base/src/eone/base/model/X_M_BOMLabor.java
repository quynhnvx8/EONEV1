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

/** Generated Model for M_BOMLabor
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_M_BOMLabor extends PO implements I_M_BOMLabor, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200619L;

    /** Standard Constructor */
    public X_M_BOMLabor (Properties ctx, int M_BOMLabor_ID, String trxName)
    {
      super (ctx, M_BOMLabor_ID, trxName);
      /** if (M_BOMLabor_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_M_BOMLabor (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_BOMLabor[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set LaborPrice.
		@param LaborPrice LaborPrice	  */
	public void setLaborPrice (BigDecimal LaborPrice)
	{
		set_Value (COLUMNNAME_LaborPrice, LaborPrice);
	}

	/** Get LaborPrice.
		@return LaborPrice	  */
	public BigDecimal getLaborPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LaborPrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public eone.base.model.I_M_BOM getM_BOM() throws RuntimeException
    {
		return (eone.base.model.I_M_BOM)MTable.get(getCtx(), eone.base.model.I_M_BOM.Table_Name)
			.getPO(getM_BOM_ID(), get_TrxName());	}

	/** Set BOM.
		@param M_BOM_ID 
		Bill of Material
	  */
	public void setM_BOM_ID (int M_BOM_ID)
	{
		if (M_BOM_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_BOM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_BOM_ID, Integer.valueOf(M_BOM_ID));
	}

	/** Get BOM.
		@return Bill of Material
	  */
	public int getM_BOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_BOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BOM Labor.
		@param M_BOMLabor_ID BOM Labor	  */
	public void setM_BOMLabor_ID (int M_BOMLabor_ID)
	{
		if (M_BOMLabor_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_BOMLabor_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_BOMLabor_ID, Integer.valueOf(M_BOMLabor_ID));
	}

	/** Get BOM Labor.
		@return BOM Labor	  */
	public int getM_BOMLabor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_BOMLabor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll.
		@param Payroll Payroll	  */
	public void setPayroll (BigDecimal Payroll)
	{
		set_Value (COLUMNNAME_Payroll, Payroll);
	}

	/** Get Payroll.
		@return Payroll	  */
	public BigDecimal getPayroll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Payroll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set WorkHour.
		@param WorkHour WorkHour	  */
	public void setWorkHour (BigDecimal WorkHour)
	{
		set_Value (COLUMNNAME_WorkHour, WorkHour);
	}

	/** Get WorkHour.
		@return WorkHour	  */
	public BigDecimal getWorkHour () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WorkHour);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
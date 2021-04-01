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

/** Generated Model for HR_TimekeeperMap
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_TimekeeperMap extends PO implements I_HR_TimekeeperMap, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201027L;

    /** Standard Constructor */
    public X_HR_TimekeeperMap (Properties ctx, int HR_TimekeeperMap_ID, String trxName)
    {
      super (ctx, HR_TimekeeperMap_ID, trxName);
      /** if (HR_TimekeeperMap_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_TimekeeperMap (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_TimekeeperMap[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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

	/** Set Timekeeper Map.
		@param HR_TimekeeperMap_ID Timekeeper Map	  */
	public void setHR_TimekeeperMap_ID (int HR_TimekeeperMap_ID)
	{
		if (HR_TimekeeperMap_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_TimekeeperMap_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_TimekeeperMap_ID, Integer.valueOf(HR_TimekeeperMap_ID));
	}

	/** Get Timekeeper Map.
		@return Timekeeper Map	  */
	public int getHR_TimekeeperMap_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_TimekeeperMap_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** NP = NP */
	public static final String NAME_NP = "NP";
	/** KLCP = KL */
	public static final String NAME_KLCP = "KL";
	/** CO = CO */
	public static final String NAME_CO = "CO";
	/** NL = NL */
	public static final String NAME_NL = "NL";
	/** NO = NO */
	public static final String NAME_NO = "NO";
	/** HH = HH */
	public static final String NAME_HH = "HH";
	/** TS = TS */
	public static final String NAME_TS = "TS";
	/** W8 = W8 */
	public static final String NAME_W8 = "W8";
	/** W4 = W4 */
	public static final String NAME_W4 = "W4";
	/** CT = CT */
	public static final String NAME_CT = "CT";
	/** HO = HO */
	public static final String NAME_HO = "HO";
	/** KLKP = KP */
	public static final String NAME_KLKP = "KP";
	/** WT4 = T4 */
	public static final String NAME_WT4 = "T4";
	/** WT8 = T8 */
	public static final String NAME_WT8 = "T8";
	/** WL8 = L8 */
	public static final String NAME_WL8 = "L8";
	/** WL4 = L4 */
	public static final String NAME_WL4 = "L4";
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

	/** Set Percentage.
		@param Percentage 
		Percent of the entire amount
	  */
	public void setPercentage (BigDecimal Percentage)
	{
		set_Value (COLUMNNAME_Percentage, Percentage);
	}

	/** Get Percentage.
		@return Percent of the entire amount
	  */
	public BigDecimal getPercentage () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** LamMacDinh = LMD */
	public static final String TYPETIMEKEEPER_LamMacDinh = "LMD";
	/** LamThemLe = LTL */
	public static final String TYPETIMEKEEPER_LamThemLe = "LTL";
	/** LamThemThuong = LTT */
	public static final String TYPETIMEKEEPER_LamThemThuong = "LTT";
	/** NghiPhepCoLuong = NPL */
	public static final String TYPETIMEKEEPER_NghiPhepCoLuong = "NPL";
	/** NghiLe = NLL */
	public static final String TYPETIMEKEEPER_NghiLe = "NLL";
	/** NghiKhongLuongCoPhep = NPK */
	public static final String TYPETIMEKEEPER_NghiKhongLuongCoPhep = "NPK";
	/** NghiKhongLuongKhongPhep = NKK */
	public static final String TYPETIMEKEEPER_NghiKhongLuongKhongPhep = "NKK";
	/** NghiThaiSan = NTS */
	public static final String TYPETIMEKEEPER_NghiThaiSan = "NTS";
	/** Set TypeTimeKeeper.
		@param TypeTimeKeeper TypeTimeKeeper	  */
	public void setTypeTimeKeeper (String TypeTimeKeeper)
	{

		set_Value (COLUMNNAME_TypeTimeKeeper, TypeTimeKeeper);
	}

	/** Get TypeTimeKeeper.
		@return TypeTimeKeeper	  */
	public String getTypeTimeKeeper () 
	{
		return (String)get_Value(COLUMNNAME_TypeTimeKeeper);
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
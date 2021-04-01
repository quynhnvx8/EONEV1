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

/** Generated Model for HR_TimekeeperLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_TimekeeperLine extends PO implements I_HR_TimekeeperLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201029L;

    /** Standard Constructor */
    public X_HR_TimekeeperLine (Properties ctx, int HR_TimekeeperLine_ID, String trxName)
    {
      super (ctx, HR_TimekeeperLine_ID, trxName);
      /** if (HR_TimekeeperLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_TimekeeperLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_TimekeeperLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (eone.base.model.I_C_Period)MTable.get(getCtx(), eone.base.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DateApproved.
		@param DateApproved DateApproved	  */
	public void setDateApproved (Timestamp DateApproved)
	{
		set_Value (COLUMNNAME_DateApproved, DateApproved);
	}

	/** Get DateApproved.
		@return DateApproved	  */
	public Timestamp getDateApproved () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateApproved);
	}

	/** NP4 = P4 */
	public static final String DAY01_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY01_KLCP = "KL";
	/** CO = CO */
	public static final String DAY01_CO = "CO";
	/** NL = NL */
	public static final String DAY01_NL = "NL";
	/** NO = NO */
	public static final String DAY01_NO = "NO";
	/** HH = HH */
	public static final String DAY01_HH = "HH";
	/** TS = TS */
	public static final String DAY01_TS = "TS";
	/** W8 = W8 */
	public static final String DAY01_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY01_W4 = "W4";
	/** CT = CT */
	public static final String DAY01_CT = "CT";
	/** HO = HO */
	public static final String DAY01_HO = "HO";
	/** KLKP = KP */
	public static final String DAY01_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY01_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY01_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY01_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY01_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY01_NP8 = "P8";
	/** Set Day01.
		@param Day01 Day01	  */
	public void setDay01 (String Day01)
	{

		set_Value (COLUMNNAME_Day01, Day01);
	}

	/** Get Day01.
		@return Day01	  */
	public String getDay01 () 
	{
		return (String)get_Value(COLUMNNAME_Day01);
	}

	/** NP4 = P4 */
	public static final String DAY02_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY02_KLCP = "KL";
	/** CO = CO */
	public static final String DAY02_CO = "CO";
	/** NL = NL */
	public static final String DAY02_NL = "NL";
	/** NO = NO */
	public static final String DAY02_NO = "NO";
	/** HH = HH */
	public static final String DAY02_HH = "HH";
	/** TS = TS */
	public static final String DAY02_TS = "TS";
	/** W8 = W8 */
	public static final String DAY02_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY02_W4 = "W4";
	/** CT = CT */
	public static final String DAY02_CT = "CT";
	/** HO = HO */
	public static final String DAY02_HO = "HO";
	/** KLKP = KP */
	public static final String DAY02_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY02_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY02_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY02_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY02_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY02_NP8 = "P8";
	/** Set Day02.
		@param Day02 Day02	  */
	public void setDay02 (String Day02)
	{

		set_Value (COLUMNNAME_Day02, Day02);
	}

	/** Get Day02.
		@return Day02	  */
	public String getDay02 () 
	{
		return (String)get_Value(COLUMNNAME_Day02);
	}

	/** NP4 = P4 */
	public static final String DAY03_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY03_KLCP = "KL";
	/** CO = CO */
	public static final String DAY03_CO = "CO";
	/** NL = NL */
	public static final String DAY03_NL = "NL";
	/** NO = NO */
	public static final String DAY03_NO = "NO";
	/** HH = HH */
	public static final String DAY03_HH = "HH";
	/** TS = TS */
	public static final String DAY03_TS = "TS";
	/** W8 = W8 */
	public static final String DAY03_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY03_W4 = "W4";
	/** CT = CT */
	public static final String DAY03_CT = "CT";
	/** HO = HO */
	public static final String DAY03_HO = "HO";
	/** KLKP = KP */
	public static final String DAY03_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY03_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY03_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY03_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY03_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY03_NP8 = "P8";
	/** Set Day03.
		@param Day03 Day03	  */
	public void setDay03 (String Day03)
	{

		set_Value (COLUMNNAME_Day03, Day03);
	}

	/** Get Day03.
		@return Day03	  */
	public String getDay03 () 
	{
		return (String)get_Value(COLUMNNAME_Day03);
	}

	/** NP4 = P4 */
	public static final String DAY04_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY04_KLCP = "KL";
	/** CO = CO */
	public static final String DAY04_CO = "CO";
	/** NL = NL */
	public static final String DAY04_NL = "NL";
	/** NO = NO */
	public static final String DAY04_NO = "NO";
	/** HH = HH */
	public static final String DAY04_HH = "HH";
	/** TS = TS */
	public static final String DAY04_TS = "TS";
	/** W8 = W8 */
	public static final String DAY04_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY04_W4 = "W4";
	/** CT = CT */
	public static final String DAY04_CT = "CT";
	/** HO = HO */
	public static final String DAY04_HO = "HO";
	/** KLKP = KP */
	public static final String DAY04_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY04_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY04_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY04_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY04_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY04_NP8 = "P8";
	/** Set Day04.
		@param Day04 Day04	  */
	public void setDay04 (String Day04)
	{

		set_Value (COLUMNNAME_Day04, Day04);
	}

	/** Get Day04.
		@return Day04	  */
	public String getDay04 () 
	{
		return (String)get_Value(COLUMNNAME_Day04);
	}

	/** NP4 = P4 */
	public static final String DAY05_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY05_KLCP = "KL";
	/** CO = CO */
	public static final String DAY05_CO = "CO";
	/** NL = NL */
	public static final String DAY05_NL = "NL";
	/** NO = NO */
	public static final String DAY05_NO = "NO";
	/** HH = HH */
	public static final String DAY05_HH = "HH";
	/** TS = TS */
	public static final String DAY05_TS = "TS";
	/** W8 = W8 */
	public static final String DAY05_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY05_W4 = "W4";
	/** CT = CT */
	public static final String DAY05_CT = "CT";
	/** HO = HO */
	public static final String DAY05_HO = "HO";
	/** KLKP = KP */
	public static final String DAY05_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY05_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY05_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY05_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY05_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY05_NP8 = "P8";
	/** Set Day05.
		@param Day05 Day05	  */
	public void setDay05 (String Day05)
	{

		set_Value (COLUMNNAME_Day05, Day05);
	}

	/** Get Day05.
		@return Day05	  */
	public String getDay05 () 
	{
		return (String)get_Value(COLUMNNAME_Day05);
	}

	/** NP4 = P4 */
	public static final String DAY06_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY06_KLCP = "KL";
	/** CO = CO */
	public static final String DAY06_CO = "CO";
	/** NL = NL */
	public static final String DAY06_NL = "NL";
	/** NO = NO */
	public static final String DAY06_NO = "NO";
	/** HH = HH */
	public static final String DAY06_HH = "HH";
	/** TS = TS */
	public static final String DAY06_TS = "TS";
	/** W8 = W8 */
	public static final String DAY06_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY06_W4 = "W4";
	/** CT = CT */
	public static final String DAY06_CT = "CT";
	/** HO = HO */
	public static final String DAY06_HO = "HO";
	/** KLKP = KP */
	public static final String DAY06_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY06_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY06_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY06_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY06_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY06_NP8 = "P8";
	/** Set Day06.
		@param Day06 Day06	  */
	public void setDay06 (String Day06)
	{

		set_Value (COLUMNNAME_Day06, Day06);
	}

	/** Get Day06.
		@return Day06	  */
	public String getDay06 () 
	{
		return (String)get_Value(COLUMNNAME_Day06);
	}

	/** NP4 = P4 */
	public static final String DAY07_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY07_KLCP = "KL";
	/** CO = CO */
	public static final String DAY07_CO = "CO";
	/** NL = NL */
	public static final String DAY07_NL = "NL";
	/** NO = NO */
	public static final String DAY07_NO = "NO";
	/** HH = HH */
	public static final String DAY07_HH = "HH";
	/** TS = TS */
	public static final String DAY07_TS = "TS";
	/** W8 = W8 */
	public static final String DAY07_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY07_W4 = "W4";
	/** CT = CT */
	public static final String DAY07_CT = "CT";
	/** HO = HO */
	public static final String DAY07_HO = "HO";
	/** KLKP = KP */
	public static final String DAY07_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY07_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY07_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY07_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY07_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY07_NP8 = "P8";
	/** Set Day07.
		@param Day07 Day07	  */
	public void setDay07 (String Day07)
	{

		set_Value (COLUMNNAME_Day07, Day07);
	}

	/** Get Day07.
		@return Day07	  */
	public String getDay07 () 
	{
		return (String)get_Value(COLUMNNAME_Day07);
	}

	/** NP4 = P4 */
	public static final String DAY08_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY08_KLCP = "KL";
	/** CO = CO */
	public static final String DAY08_CO = "CO";
	/** NL = NL */
	public static final String DAY08_NL = "NL";
	/** NO = NO */
	public static final String DAY08_NO = "NO";
	/** HH = HH */
	public static final String DAY08_HH = "HH";
	/** TS = TS */
	public static final String DAY08_TS = "TS";
	/** W8 = W8 */
	public static final String DAY08_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY08_W4 = "W4";
	/** CT = CT */
	public static final String DAY08_CT = "CT";
	/** HO = HO */
	public static final String DAY08_HO = "HO";
	/** KLKP = KP */
	public static final String DAY08_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY08_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY08_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY08_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY08_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY08_NP8 = "P8";
	/** Set Day08.
		@param Day08 Day08	  */
	public void setDay08 (String Day08)
	{

		set_Value (COLUMNNAME_Day08, Day08);
	}

	/** Get Day08.
		@return Day08	  */
	public String getDay08 () 
	{
		return (String)get_Value(COLUMNNAME_Day08);
	}

	/** NP4 = P4 */
	public static final String DAY09_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY09_KLCP = "KL";
	/** CO = CO */
	public static final String DAY09_CO = "CO";
	/** NL = NL */
	public static final String DAY09_NL = "NL";
	/** NO = NO */
	public static final String DAY09_NO = "NO";
	/** HH = HH */
	public static final String DAY09_HH = "HH";
	/** TS = TS */
	public static final String DAY09_TS = "TS";
	/** W8 = W8 */
	public static final String DAY09_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY09_W4 = "W4";
	/** CT = CT */
	public static final String DAY09_CT = "CT";
	/** HO = HO */
	public static final String DAY09_HO = "HO";
	/** KLKP = KP */
	public static final String DAY09_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY09_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY09_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY09_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY09_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY09_NP8 = "P8";
	/** Set Day09.
		@param Day09 Day09	  */
	public void setDay09 (String Day09)
	{

		set_Value (COLUMNNAME_Day09, Day09);
	}

	/** Get Day09.
		@return Day09	  */
	public String getDay09 () 
	{
		return (String)get_Value(COLUMNNAME_Day09);
	}

	/** NP4 = P4 */
	public static final String DAY10_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY10_KLCP = "KL";
	/** CO = CO */
	public static final String DAY10_CO = "CO";
	/** NL = NL */
	public static final String DAY10_NL = "NL";
	/** NO = NO */
	public static final String DAY10_NO = "NO";
	/** HH = HH */
	public static final String DAY10_HH = "HH";
	/** TS = TS */
	public static final String DAY10_TS = "TS";
	/** W8 = W8 */
	public static final String DAY10_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY10_W4 = "W4";
	/** CT = CT */
	public static final String DAY10_CT = "CT";
	/** HO = HO */
	public static final String DAY10_HO = "HO";
	/** KLKP = KP */
	public static final String DAY10_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY10_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY10_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY10_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY10_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY10_NP8 = "P8";
	/** Set Day10.
		@param Day10 Day10	  */
	public void setDay10 (String Day10)
	{

		set_Value (COLUMNNAME_Day10, Day10);
	}

	/** Get Day10.
		@return Day10	  */
	public String getDay10 () 
	{
		return (String)get_Value(COLUMNNAME_Day10);
	}

	/** NP4 = P4 */
	public static final String DAY11_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY11_KLCP = "KL";
	/** CO = CO */
	public static final String DAY11_CO = "CO";
	/** NL = NL */
	public static final String DAY11_NL = "NL";
	/** NO = NO */
	public static final String DAY11_NO = "NO";
	/** HH = HH */
	public static final String DAY11_HH = "HH";
	/** TS = TS */
	public static final String DAY11_TS = "TS";
	/** W8 = W8 */
	public static final String DAY11_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY11_W4 = "W4";
	/** CT = CT */
	public static final String DAY11_CT = "CT";
	/** HO = HO */
	public static final String DAY11_HO = "HO";
	/** KLKP = KP */
	public static final String DAY11_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY11_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY11_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY11_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY11_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY11_NP8 = "P8";
	/** Set Day11.
		@param Day11 Day11	  */
	public void setDay11 (String Day11)
	{

		set_Value (COLUMNNAME_Day11, Day11);
	}

	/** Get Day11.
		@return Day11	  */
	public String getDay11 () 
	{
		return (String)get_Value(COLUMNNAME_Day11);
	}

	/** NP4 = P4 */
	public static final String DAY12_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY12_KLCP = "KL";
	/** CO = CO */
	public static final String DAY12_CO = "CO";
	/** NL = NL */
	public static final String DAY12_NL = "NL";
	/** NO = NO */
	public static final String DAY12_NO = "NO";
	/** HH = HH */
	public static final String DAY12_HH = "HH";
	/** TS = TS */
	public static final String DAY12_TS = "TS";
	/** W8 = W8 */
	public static final String DAY12_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY12_W4 = "W4";
	/** CT = CT */
	public static final String DAY12_CT = "CT";
	/** HO = HO */
	public static final String DAY12_HO = "HO";
	/** KLKP = KP */
	public static final String DAY12_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY12_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY12_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY12_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY12_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY12_NP8 = "P8";
	/** Set Day12.
		@param Day12 Day12	  */
	public void setDay12 (String Day12)
	{

		set_Value (COLUMNNAME_Day12, Day12);
	}

	/** Get Day12.
		@return Day12	  */
	public String getDay12 () 
	{
		return (String)get_Value(COLUMNNAME_Day12);
	}

	/** NP4 = P4 */
	public static final String DAY13_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY13_KLCP = "KL";
	/** CO = CO */
	public static final String DAY13_CO = "CO";
	/** NL = NL */
	public static final String DAY13_NL = "NL";
	/** NO = NO */
	public static final String DAY13_NO = "NO";
	/** HH = HH */
	public static final String DAY13_HH = "HH";
	/** TS = TS */
	public static final String DAY13_TS = "TS";
	/** W8 = W8 */
	public static final String DAY13_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY13_W4 = "W4";
	/** CT = CT */
	public static final String DAY13_CT = "CT";
	/** HO = HO */
	public static final String DAY13_HO = "HO";
	/** KLKP = KP */
	public static final String DAY13_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY13_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY13_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY13_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY13_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY13_NP8 = "P8";
	/** Set Day13.
		@param Day13 Day13	  */
	public void setDay13 (String Day13)
	{

		set_Value (COLUMNNAME_Day13, Day13);
	}

	/** Get Day13.
		@return Day13	  */
	public String getDay13 () 
	{
		return (String)get_Value(COLUMNNAME_Day13);
	}

	/** NP4 = P4 */
	public static final String DAY14_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY14_KLCP = "KL";
	/** CO = CO */
	public static final String DAY14_CO = "CO";
	/** NL = NL */
	public static final String DAY14_NL = "NL";
	/** NO = NO */
	public static final String DAY14_NO = "NO";
	/** HH = HH */
	public static final String DAY14_HH = "HH";
	/** TS = TS */
	public static final String DAY14_TS = "TS";
	/** W8 = W8 */
	public static final String DAY14_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY14_W4 = "W4";
	/** CT = CT */
	public static final String DAY14_CT = "CT";
	/** HO = HO */
	public static final String DAY14_HO = "HO";
	/** KLKP = KP */
	public static final String DAY14_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY14_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY14_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY14_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY14_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY14_NP8 = "P8";
	/** Set Day14.
		@param Day14 Day14	  */
	public void setDay14 (String Day14)
	{

		set_Value (COLUMNNAME_Day14, Day14);
	}

	/** Get Day14.
		@return Day14	  */
	public String getDay14 () 
	{
		return (String)get_Value(COLUMNNAME_Day14);
	}

	/** NP4 = P4 */
	public static final String DAY15_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY15_KLCP = "KL";
	/** CO = CO */
	public static final String DAY15_CO = "CO";
	/** NL = NL */
	public static final String DAY15_NL = "NL";
	/** NO = NO */
	public static final String DAY15_NO = "NO";
	/** HH = HH */
	public static final String DAY15_HH = "HH";
	/** TS = TS */
	public static final String DAY15_TS = "TS";
	/** W8 = W8 */
	public static final String DAY15_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY15_W4 = "W4";
	/** CT = CT */
	public static final String DAY15_CT = "CT";
	/** HO = HO */
	public static final String DAY15_HO = "HO";
	/** KLKP = KP */
	public static final String DAY15_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY15_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY15_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY15_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY15_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY15_NP8 = "P8";
	/** Set Day15.
		@param Day15 Day15	  */
	public void setDay15 (String Day15)
	{

		set_Value (COLUMNNAME_Day15, Day15);
	}

	/** Get Day15.
		@return Day15	  */
	public String getDay15 () 
	{
		return (String)get_Value(COLUMNNAME_Day15);
	}

	/** NP4 = P4 */
	public static final String DAY16_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY16_KLCP = "KL";
	/** CO = CO */
	public static final String DAY16_CO = "CO";
	/** NL = NL */
	public static final String DAY16_NL = "NL";
	/** NO = NO */
	public static final String DAY16_NO = "NO";
	/** HH = HH */
	public static final String DAY16_HH = "HH";
	/** TS = TS */
	public static final String DAY16_TS = "TS";
	/** W8 = W8 */
	public static final String DAY16_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY16_W4 = "W4";
	/** CT = CT */
	public static final String DAY16_CT = "CT";
	/** HO = HO */
	public static final String DAY16_HO = "HO";
	/** KLKP = KP */
	public static final String DAY16_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY16_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY16_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY16_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY16_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY16_NP8 = "P8";
	/** Set Day16.
		@param Day16 Day16	  */
	public void setDay16 (String Day16)
	{

		set_Value (COLUMNNAME_Day16, Day16);
	}

	/** Get Day16.
		@return Day16	  */
	public String getDay16 () 
	{
		return (String)get_Value(COLUMNNAME_Day16);
	}

	/** NP4 = P4 */
	public static final String DAY17_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY17_KLCP = "KL";
	/** CO = CO */
	public static final String DAY17_CO = "CO";
	/** NL = NL */
	public static final String DAY17_NL = "NL";
	/** NO = NO */
	public static final String DAY17_NO = "NO";
	/** HH = HH */
	public static final String DAY17_HH = "HH";
	/** TS = TS */
	public static final String DAY17_TS = "TS";
	/** W8 = W8 */
	public static final String DAY17_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY17_W4 = "W4";
	/** CT = CT */
	public static final String DAY17_CT = "CT";
	/** HO = HO */
	public static final String DAY17_HO = "HO";
	/** KLKP = KP */
	public static final String DAY17_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY17_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY17_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY17_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY17_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY17_NP8 = "P8";
	/** Set Day17.
		@param Day17 Day17	  */
	public void setDay17 (String Day17)
	{

		set_Value (COLUMNNAME_Day17, Day17);
	}

	/** Get Day17.
		@return Day17	  */
	public String getDay17 () 
	{
		return (String)get_Value(COLUMNNAME_Day17);
	}

	/** NP4 = P4 */
	public static final String DAY18_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY18_KLCP = "KL";
	/** CO = CO */
	public static final String DAY18_CO = "CO";
	/** NL = NL */
	public static final String DAY18_NL = "NL";
	/** NO = NO */
	public static final String DAY18_NO = "NO";
	/** HH = HH */
	public static final String DAY18_HH = "HH";
	/** TS = TS */
	public static final String DAY18_TS = "TS";
	/** W8 = W8 */
	public static final String DAY18_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY18_W4 = "W4";
	/** CT = CT */
	public static final String DAY18_CT = "CT";
	/** HO = HO */
	public static final String DAY18_HO = "HO";
	/** KLKP = KP */
	public static final String DAY18_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY18_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY18_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY18_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY18_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY18_NP8 = "P8";
	/** Set Day18.
		@param Day18 Day18	  */
	public void setDay18 (String Day18)
	{

		set_Value (COLUMNNAME_Day18, Day18);
	}

	/** Get Day18.
		@return Day18	  */
	public String getDay18 () 
	{
		return (String)get_Value(COLUMNNAME_Day18);
	}

	/** NP4 = P4 */
	public static final String DAY19_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY19_KLCP = "KL";
	/** CO = CO */
	public static final String DAY19_CO = "CO";
	/** NL = NL */
	public static final String DAY19_NL = "NL";
	/** NO = NO */
	public static final String DAY19_NO = "NO";
	/** HH = HH */
	public static final String DAY19_HH = "HH";
	/** TS = TS */
	public static final String DAY19_TS = "TS";
	/** W8 = W8 */
	public static final String DAY19_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY19_W4 = "W4";
	/** CT = CT */
	public static final String DAY19_CT = "CT";
	/** HO = HO */
	public static final String DAY19_HO = "HO";
	/** KLKP = KP */
	public static final String DAY19_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY19_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY19_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY19_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY19_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY19_NP8 = "P8";
	/** Set Day19.
		@param Day19 Day19	  */
	public void setDay19 (String Day19)
	{

		set_Value (COLUMNNAME_Day19, Day19);
	}

	/** Get Day19.
		@return Day19	  */
	public String getDay19 () 
	{
		return (String)get_Value(COLUMNNAME_Day19);
	}

	/** NP4 = P4 */
	public static final String DAY20_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY20_KLCP = "KL";
	/** CO = CO */
	public static final String DAY20_CO = "CO";
	/** NL = NL */
	public static final String DAY20_NL = "NL";
	/** NO = NO */
	public static final String DAY20_NO = "NO";
	/** HH = HH */
	public static final String DAY20_HH = "HH";
	/** TS = TS */
	public static final String DAY20_TS = "TS";
	/** W8 = W8 */
	public static final String DAY20_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY20_W4 = "W4";
	/** CT = CT */
	public static final String DAY20_CT = "CT";
	/** HO = HO */
	public static final String DAY20_HO = "HO";
	/** KLKP = KP */
	public static final String DAY20_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY20_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY20_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY20_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY20_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY20_NP8 = "P8";
	/** Set Day20.
		@param Day20 Day20	  */
	public void setDay20 (String Day20)
	{

		set_Value (COLUMNNAME_Day20, Day20);
	}

	/** Get Day20.
		@return Day20	  */
	public String getDay20 () 
	{
		return (String)get_Value(COLUMNNAME_Day20);
	}

	/** NP4 = P4 */
	public static final String DAY21_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY21_KLCP = "KL";
	/** CO = CO */
	public static final String DAY21_CO = "CO";
	/** NL = NL */
	public static final String DAY21_NL = "NL";
	/** NO = NO */
	public static final String DAY21_NO = "NO";
	/** HH = HH */
	public static final String DAY21_HH = "HH";
	/** TS = TS */
	public static final String DAY21_TS = "TS";
	/** W8 = W8 */
	public static final String DAY21_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY21_W4 = "W4";
	/** CT = CT */
	public static final String DAY21_CT = "CT";
	/** HO = HO */
	public static final String DAY21_HO = "HO";
	/** KLKP = KP */
	public static final String DAY21_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY21_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY21_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY21_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY21_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY21_NP8 = "P8";
	/** Set Day21.
		@param Day21 Day21	  */
	public void setDay21 (String Day21)
	{

		set_Value (COLUMNNAME_Day21, Day21);
	}

	/** Get Day21.
		@return Day21	  */
	public String getDay21 () 
	{
		return (String)get_Value(COLUMNNAME_Day21);
	}

	/** NP4 = P4 */
	public static final String DAY22_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY22_KLCP = "KL";
	/** CO = CO */
	public static final String DAY22_CO = "CO";
	/** NL = NL */
	public static final String DAY22_NL = "NL";
	/** NO = NO */
	public static final String DAY22_NO = "NO";
	/** HH = HH */
	public static final String DAY22_HH = "HH";
	/** TS = TS */
	public static final String DAY22_TS = "TS";
	/** W8 = W8 */
	public static final String DAY22_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY22_W4 = "W4";
	/** CT = CT */
	public static final String DAY22_CT = "CT";
	/** HO = HO */
	public static final String DAY22_HO = "HO";
	/** KLKP = KP */
	public static final String DAY22_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY22_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY22_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY22_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY22_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY22_NP8 = "P8";
	/** Set Day22.
		@param Day22 Day22	  */
	public void setDay22 (String Day22)
	{

		set_Value (COLUMNNAME_Day22, Day22);
	}

	/** Get Day22.
		@return Day22	  */
	public String getDay22 () 
	{
		return (String)get_Value(COLUMNNAME_Day22);
	}

	/** NP4 = P4 */
	public static final String DAY23_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY23_KLCP = "KL";
	/** CO = CO */
	public static final String DAY23_CO = "CO";
	/** NL = NL */
	public static final String DAY23_NL = "NL";
	/** NO = NO */
	public static final String DAY23_NO = "NO";
	/** HH = HH */
	public static final String DAY23_HH = "HH";
	/** TS = TS */
	public static final String DAY23_TS = "TS";
	/** W8 = W8 */
	public static final String DAY23_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY23_W4 = "W4";
	/** CT = CT */
	public static final String DAY23_CT = "CT";
	/** HO = HO */
	public static final String DAY23_HO = "HO";
	/** KLKP = KP */
	public static final String DAY23_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY23_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY23_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY23_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY23_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY23_NP8 = "P8";
	/** Set Day23.
		@param Day23 Day23	  */
	public void setDay23 (String Day23)
	{

		set_Value (COLUMNNAME_Day23, Day23);
	}

	/** Get Day23.
		@return Day23	  */
	public String getDay23 () 
	{
		return (String)get_Value(COLUMNNAME_Day23);
	}

	/** NP4 = P4 */
	public static final String DAY24_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY24_KLCP = "KL";
	/** CO = CO */
	public static final String DAY24_CO = "CO";
	/** NL = NL */
	public static final String DAY24_NL = "NL";
	/** NO = NO */
	public static final String DAY24_NO = "NO";
	/** HH = HH */
	public static final String DAY24_HH = "HH";
	/** TS = TS */
	public static final String DAY24_TS = "TS";
	/** W8 = W8 */
	public static final String DAY24_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY24_W4 = "W4";
	/** CT = CT */
	public static final String DAY24_CT = "CT";
	/** HO = HO */
	public static final String DAY24_HO = "HO";
	/** KLKP = KP */
	public static final String DAY24_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY24_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY24_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY24_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY24_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY24_NP8 = "P8";
	/** Set Day24.
		@param Day24 Day24	  */
	public void setDay24 (String Day24)
	{

		set_Value (COLUMNNAME_Day24, Day24);
	}

	/** Get Day24.
		@return Day24	  */
	public String getDay24 () 
	{
		return (String)get_Value(COLUMNNAME_Day24);
	}

	/** NP4 = P4 */
	public static final String DAY25_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY25_KLCP = "KL";
	/** CO = CO */
	public static final String DAY25_CO = "CO";
	/** NL = NL */
	public static final String DAY25_NL = "NL";
	/** NO = NO */
	public static final String DAY25_NO = "NO";
	/** HH = HH */
	public static final String DAY25_HH = "HH";
	/** TS = TS */
	public static final String DAY25_TS = "TS";
	/** W8 = W8 */
	public static final String DAY25_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY25_W4 = "W4";
	/** CT = CT */
	public static final String DAY25_CT = "CT";
	/** HO = HO */
	public static final String DAY25_HO = "HO";
	/** KLKP = KP */
	public static final String DAY25_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY25_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY25_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY25_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY25_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY25_NP8 = "P8";
	/** Set Day25.
		@param Day25 Day25	  */
	public void setDay25 (String Day25)
	{

		set_Value (COLUMNNAME_Day25, Day25);
	}

	/** Get Day25.
		@return Day25	  */
	public String getDay25 () 
	{
		return (String)get_Value(COLUMNNAME_Day25);
	}

	/** NP4 = P4 */
	public static final String DAY26_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY26_KLCP = "KL";
	/** CO = CO */
	public static final String DAY26_CO = "CO";
	/** NL = NL */
	public static final String DAY26_NL = "NL";
	/** NO = NO */
	public static final String DAY26_NO = "NO";
	/** HH = HH */
	public static final String DAY26_HH = "HH";
	/** TS = TS */
	public static final String DAY26_TS = "TS";
	/** W8 = W8 */
	public static final String DAY26_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY26_W4 = "W4";
	/** CT = CT */
	public static final String DAY26_CT = "CT";
	/** HO = HO */
	public static final String DAY26_HO = "HO";
	/** KLKP = KP */
	public static final String DAY26_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY26_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY26_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY26_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY26_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY26_NP8 = "P8";
	/** Set Day26.
		@param Day26 Day26	  */
	public void setDay26 (String Day26)
	{

		set_Value (COLUMNNAME_Day26, Day26);
	}

	/** Get Day26.
		@return Day26	  */
	public String getDay26 () 
	{
		return (String)get_Value(COLUMNNAME_Day26);
	}

	/** NP4 = P4 */
	public static final String DAY27_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY27_KLCP = "KL";
	/** CO = CO */
	public static final String DAY27_CO = "CO";
	/** NL = NL */
	public static final String DAY27_NL = "NL";
	/** NO = NO */
	public static final String DAY27_NO = "NO";
	/** HH = HH */
	public static final String DAY27_HH = "HH";
	/** TS = TS */
	public static final String DAY27_TS = "TS";
	/** W8 = W8 */
	public static final String DAY27_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY27_W4 = "W4";
	/** CT = CT */
	public static final String DAY27_CT = "CT";
	/** HO = HO */
	public static final String DAY27_HO = "HO";
	/** KLKP = KP */
	public static final String DAY27_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY27_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY27_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY27_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY27_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY27_NP8 = "P8";
	/** Set Day27.
		@param Day27 Day27	  */
	public void setDay27 (String Day27)
	{

		set_Value (COLUMNNAME_Day27, Day27);
	}

	/** Get Day27.
		@return Day27	  */
	public String getDay27 () 
	{
		return (String)get_Value(COLUMNNAME_Day27);
	}

	/** NP4 = P4 */
	public static final String DAY28_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY28_KLCP = "KL";
	/** CO = CO */
	public static final String DAY28_CO = "CO";
	/** NL = NL */
	public static final String DAY28_NL = "NL";
	/** NO = NO */
	public static final String DAY28_NO = "NO";
	/** HH = HH */
	public static final String DAY28_HH = "HH";
	/** TS = TS */
	public static final String DAY28_TS = "TS";
	/** W8 = W8 */
	public static final String DAY28_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY28_W4 = "W4";
	/** CT = CT */
	public static final String DAY28_CT = "CT";
	/** HO = HO */
	public static final String DAY28_HO = "HO";
	/** KLKP = KP */
	public static final String DAY28_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY28_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY28_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY28_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY28_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY28_NP8 = "P8";
	/** Set Day28.
		@param Day28 Day28	  */
	public void setDay28 (String Day28)
	{

		set_Value (COLUMNNAME_Day28, Day28);
	}

	/** Get Day28.
		@return Day28	  */
	public String getDay28 () 
	{
		return (String)get_Value(COLUMNNAME_Day28);
	}

	/** NP4 = P4 */
	public static final String DAY29_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY29_KLCP = "KL";
	/** CO = CO */
	public static final String DAY29_CO = "CO";
	/** NL = NL */
	public static final String DAY29_NL = "NL";
	/** NO = NO */
	public static final String DAY29_NO = "NO";
	/** HH = HH */
	public static final String DAY29_HH = "HH";
	/** TS = TS */
	public static final String DAY29_TS = "TS";
	/** W8 = W8 */
	public static final String DAY29_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY29_W4 = "W4";
	/** CT = CT */
	public static final String DAY29_CT = "CT";
	/** HO = HO */
	public static final String DAY29_HO = "HO";
	/** KLKP = KP */
	public static final String DAY29_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY29_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY29_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY29_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY29_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY29_NP8 = "P8";
	/** Set Day29.
		@param Day29 Day29	  */
	public void setDay29 (String Day29)
	{

		set_Value (COLUMNNAME_Day29, Day29);
	}

	/** Get Day29.
		@return Day29	  */
	public String getDay29 () 
	{
		return (String)get_Value(COLUMNNAME_Day29);
	}

	/** NP4 = P4 */
	public static final String DAY30_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY30_KLCP = "KL";
	/** CO = CO */
	public static final String DAY30_CO = "CO";
	/** NL = NL */
	public static final String DAY30_NL = "NL";
	/** NO = NO */
	public static final String DAY30_NO = "NO";
	/** HH = HH */
	public static final String DAY30_HH = "HH";
	/** TS = TS */
	public static final String DAY30_TS = "TS";
	/** W8 = W8 */
	public static final String DAY30_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY30_W4 = "W4";
	/** CT = CT */
	public static final String DAY30_CT = "CT";
	/** HO = HO */
	public static final String DAY30_HO = "HO";
	/** KLKP = KP */
	public static final String DAY30_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY30_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY30_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY30_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY30_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY30_NP8 = "P8";
	/** Set Day30.
		@param Day30 Day30	  */
	public void setDay30 (String Day30)
	{

		set_Value (COLUMNNAME_Day30, Day30);
	}

	/** Get Day30.
		@return Day30	  */
	public String getDay30 () 
	{
		return (String)get_Value(COLUMNNAME_Day30);
	}

	/** NP4 = P4 */
	public static final String DAY31_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAY31_KLCP = "KL";
	/** CO = CO */
	public static final String DAY31_CO = "CO";
	/** NL = NL */
	public static final String DAY31_NL = "NL";
	/** NO = NO */
	public static final String DAY31_NO = "NO";
	/** HH = HH */
	public static final String DAY31_HH = "HH";
	/** TS = TS */
	public static final String DAY31_TS = "TS";
	/** W8 = W8 */
	public static final String DAY31_W8 = "W8";
	/** W4 = W4 */
	public static final String DAY31_W4 = "W4";
	/** CT = CT */
	public static final String DAY31_CT = "CT";
	/** HO = HO */
	public static final String DAY31_HO = "HO";
	/** KLKP = KP */
	public static final String DAY31_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAY31_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAY31_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAY31_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAY31_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAY31_NP8 = "P8";
	/** Set Day31.
		@param Day31 Day31	  */
	public void setDay31 (String Day31)
	{

		set_Value (COLUMNNAME_Day31, Day31);
	}

	/** Get Day31.
		@return Day31	  */
	public String getDay31 () 
	{
		return (String)get_Value(COLUMNNAME_Day31);
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

	public I_HR_Timekeeper getHR_Timekeeper() throws RuntimeException
    {
		return (I_HR_Timekeeper)MTable.get(getCtx(), I_HR_Timekeeper.Table_Name)
			.getPO(getHR_Timekeeper_ID(), get_TrxName());	}

	/** Set Timekeeper.
		@param HR_Timekeeper_ID Timekeeper	  */
	public void setHR_Timekeeper_ID (int HR_Timekeeper_ID)
	{
		if (HR_Timekeeper_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Timekeeper_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Timekeeper_ID, Integer.valueOf(HR_Timekeeper_ID));
	}

	/** Get Timekeeper.
		@return Timekeeper	  */
	public int getHR_Timekeeper_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Timekeeper_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Timekeeper Line.
		@param HR_TimekeeperLine_ID Timekeeper Line	  */
	public void setHR_TimekeeperLine_ID (int HR_TimekeeperLine_ID)
	{
		if (HR_TimekeeperLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_TimekeeperLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_TimekeeperLine_ID, Integer.valueOf(HR_TimekeeperLine_ID));
	}

	/** Get Timekeeper Line.
		@return Timekeeper Line	  */
	public int getHR_TimekeeperLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_TimekeeperLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set TotalDayOff.
		@param TotalDayOff TotalDayOff	  */
	public void setTotalDayOff (BigDecimal TotalDayOff)
	{
		set_Value (COLUMNNAME_TotalDayOff, TotalDayOff);
	}

	/** Get TotalDayOff.
		@return TotalDayOff	  */
	public BigDecimal getTotalDayOff () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalDayOff);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalWorkDay.
		@param TotalWorkDay TotalWorkDay	  */
	public void setTotalWorkDay (BigDecimal TotalWorkDay)
	{
		set_Value (COLUMNNAME_TotalWorkDay, TotalWorkDay);
	}

	/** Get TotalWorkDay.
		@return TotalWorkDay	  */
	public BigDecimal getTotalWorkDay () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalWorkDay);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
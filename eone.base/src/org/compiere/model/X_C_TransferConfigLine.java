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
import java.util.Properties;

/** Generated Model for C_TransferConfigLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_TransferConfigLine extends PO implements I_C_TransferConfigLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200921L;

    /** Standard Constructor */
    public X_C_TransferConfigLine (Properties ctx, int C_TransferConfigLine_ID, String trxName)
    {
      super (ctx, C_TransferConfigLine_ID, trxName);
      /** if (C_TransferConfigLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_TransferConfigLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_TransferConfigLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Account Cr.
		@param Account_Cr_ID 
		Account Cr
	  */
	public void setAccount_Cr_ID (String Account_Cr_ID)
	{

		set_Value (COLUMNNAME_Account_Cr_ID, Account_Cr_ID);
	}

	/** Get Account Cr.
		@return Account Cr
	  */
	public String getAccount_Cr_ID () 
	{
		return (String)get_Value(COLUMNNAME_Account_Cr_ID);
	}

	public org.compiere.model.I_C_ElementValue getAccount_Dr() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Dr_ID(), get_TrxName());	}

	/** Set Account Dr.
		@param Account_Dr_ID 
		Account Dr
	  */
	public void setAccount_Dr_ID (int Account_Dr_ID)
	{
		if (Account_Dr_ID < 1) 
			set_Value (COLUMNNAME_Account_Dr_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Dr_ID, Integer.valueOf(Account_Dr_ID));
	}

	/** Get Account Dr.
		@return Account Dr
	  */
	public int getAccount_Dr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Dr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_TransferConfig getC_TransferConfig() throws RuntimeException
    {
		return (I_C_TransferConfig)MTable.get(getCtx(), I_C_TransferConfig.Table_Name)
			.getPO(getC_TransferConfig_ID(), get_TrxName());	}

	/** Set Transfer Config.
		@param C_TransferConfig_ID Transfer Config	  */
	public void setC_TransferConfig_ID (int C_TransferConfig_ID)
	{
		if (C_TransferConfig_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_TransferConfig_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TransferConfig_ID, Integer.valueOf(C_TransferConfig_ID));
	}

	/** Get Transfer Config.
		@return Transfer Config	  */
	public int getC_TransferConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TransferConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Transfer Config Line.
		@param C_TransferConfigLine_ID Transfer Config Line	  */
	public void setC_TransferConfigLine_ID (int C_TransferConfigLine_ID)
	{
		if (C_TransferConfigLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_TransferConfigLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TransferConfigLine_ID, Integer.valueOf(C_TransferConfigLine_ID));
	}

	/** Get Transfer Config Line.
		@return Transfer Config Line	  */
	public int getC_TransferConfigLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TransferConfigLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Credit = CRED */
	public static final String METHODTRANSFER_Credit = "CRED";
	/** Debit = DEDT */
	public static final String METHODTRANSFER_Debit = "DEDT";
	/** Balance Debit = BADR */
	public static final String METHODTRANSFER_BalanceDebit = "BADR";
	/** Balance Credit = BACR */
	public static final String METHODTRANSFER_BalanceCredit = "BACR";
	/** ABSValue = ABSV */
	public static final String METHODTRANSFER_ABSValue = "ABSV";
	/** Set MethodTransfer.
		@param MethodTransfer MethodTransfer	  */
	public void setMethodTransfer (String MethodTransfer)
	{

		set_Value (COLUMNNAME_MethodTransfer, MethodTransfer);
	}

	/** Get MethodTransfer.
		@return MethodTransfer	  */
	public String getMethodTransfer () 
	{
		return (String)get_Value(COLUMNNAME_MethodTransfer);
	}

	/** Set OrderCalculate.
		@param OrderCalculate OrderCalculate	  */
	public void setOrderCalculate (int OrderCalculate)
	{
		set_Value (COLUMNNAME_OrderCalculate, Integer.valueOf(OrderCalculate));
	}

	/** Get OrderCalculate.
		@return OrderCalculate	  */
	public int getOrderCalculate () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OrderCalculate);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
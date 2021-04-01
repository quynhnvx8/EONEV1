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

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for C_TransferConfig
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_TransferConfig extends PO implements I_C_TransferConfig, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200921L;

    /** Standard Constructor */
    public X_C_TransferConfig (Properties ctx, int C_TransferConfig_ID, String trxName)
    {
      super (ctx, C_TransferConfig_ID, trxName);
      /** if (C_TransferConfig_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_TransferConfig (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_TransferConfig[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_C_ElementValue getAccount_Dr() throws RuntimeException
    {
		return (eone.base.model.I_C_ElementValue)MTable.get(getCtx(), eone.base.model.I_C_ElementValue.Table_Name)
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

	/** Transfer = TRAN */
	public static final String TRANSFERTYPE_Transfer = "TRAN";
	/** Allocation = ALLO */
	public static final String TRANSFERTYPE_Allocation = "ALLO";
	/** Set TransferType.
		@param TransferType TransferType	  */
	public void setTransferType (String TransferType)
	{

		set_Value (COLUMNNAME_TransferType, TransferType);
	}

	/** Get TransferType.
		@return TransferType	  */
	public String getTransferType () 
	{
		return (String)get_Value(COLUMNNAME_TransferType);
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
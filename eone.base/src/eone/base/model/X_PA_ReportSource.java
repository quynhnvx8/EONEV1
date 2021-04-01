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

/** Generated Model for PA_ReportSource
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_PA_ReportSource extends PO implements I_PA_ReportSource, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200807L;

    /** Standard Constructor */
    public X_PA_ReportSource (Properties ctx, int PA_ReportSource_ID, String trxName)
    {
      super (ctx, PA_ReportSource_ID, trxName);
      /** if (PA_ReportSource_ID == 0)
        {
			setPA_ReportLine_ID (0);
			setPA_ReportSource_ID (0);
        } */
    }

    /** Load Constructor */
    public X_PA_ReportSource (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_PA_ReportSource[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Validation code.
		@param Code 
		Validation Code
	  */
	public void setCode (String Code)
	{
		set_Value (COLUMNNAME_Code, Code);
	}

	/** Get Validation code.
		@return Validation Code
	  */
	public String getCode () 
	{
		return (String)get_Value(COLUMNNAME_Code);
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

	/** Set IsHermaphrodite.
		@param IsHermaphrodite IsHermaphrodite	  */
	public void setIsHermaphrodite (boolean IsHermaphrodite)
	{
		set_Value (COLUMNNAME_IsHermaphrodite, Boolean.valueOf(IsHermaphrodite));
	}

	/** Get IsHermaphrodite.
		@return IsHermaphrodite	  */
	public boolean isHermaphrodite () 
	{
		Object oo = get_Value(COLUMNNAME_IsHermaphrodite);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public eone.base.model.I_PA_ReportLine getPA_ReportLine() throws RuntimeException
    {
		return (eone.base.model.I_PA_ReportLine)MTable.get(getCtx(), eone.base.model.I_PA_ReportLine.Table_Name)
			.getPO(getPA_ReportLine_ID(), get_TrxName());	}

	/** Set Report Line.
		@param PA_ReportLine_ID Report Line	  */
	public void setPA_ReportLine_ID (int PA_ReportLine_ID)
	{
		if (PA_ReportLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PA_ReportLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PA_ReportLine_ID, Integer.valueOf(PA_ReportLine_ID));
	}

	/** Get Report Line.
		@return Report Line	  */
	public int getPA_ReportLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ReportLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Report Source.
		@param PA_ReportSource_ID 
		Restriction of what will be shown in Report Line
	  */
	public void setPA_ReportSource_ID (int PA_ReportSource_ID)
	{
		if (PA_ReportSource_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PA_ReportSource_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PA_ReportSource_ID, Integer.valueOf(PA_ReportSource_ID));
	}

	/** Get Report Source.
		@return Restriction of what will be shown in Report Line
	  */
	public int getPA_ReportSource_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ReportSource_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PA_ReportSource_UU.
		@param PA_ReportSource_UU PA_ReportSource_UU	  */
	public void setPA_ReportSource_UU (String PA_ReportSource_UU)
	{
		set_Value (COLUMNNAME_PA_ReportSource_UU, PA_ReportSource_UU);
	}

	/** Get PA_ReportSource_UU.
		@return PA_ReportSource_UU	  */
	public String getPA_ReportSource_UU () 
	{
		return (String)get_Value(COLUMNNAME_PA_ReportSource_UU);
	}

	/** TypeAccount AD_Reference_ID=53280 */
	public static final int TYPEACCOUNT_AD_Reference_ID=53280;
	/** BalanceCreditAcct = BCR_ACCT */
	public static final String TYPEACCOUNT_BalanceCreditAcct = "BCR_ACCT";
	/** BalanceDebitAcct = BDR_ACCT */
	public static final String TYPEACCOUNT_BalanceDebitAcct = "BDR_ACCT";
	/** BalanceCreditAcctFinal = BCR_ACCT_F */
	public static final String TYPEACCOUNT_BalanceCreditAcctFinal = "BCR_ACCT_F";
	/** BalanceDebitAcctFinal = BDR_ACCT_F */
	public static final String TYPEACCOUNT_BalanceDebitAcctFinal = "BDR_ACCT_F";
	/** AccumulateDebitAcct = ACCDR_ACCT */
	public static final String TYPEACCOUNT_AccumulateDebitAcct = "ACCDR_ACCT";
	/** AccumulateCreditAcct = ACCCR_ACCT */
	public static final String TYPEACCOUNT_AccumulateCreditAcct = "ACCCR_ACCT";
	/** IncuredCreditAcct = INCCR_ACCT */
	public static final String TYPEACCOUNT_IncuredCreditAcct = "INCCR_ACCT";
	/** IncuredDebitAcct = INCDR_ACCT */
	public static final String TYPEACCOUNT_IncuredDebitAcct = "INCDR_ACCT";
	/** Set TypeAccount.
		@param TypeAccount TypeAccount	  */
	public void setTypeAccount (String TypeAccount)
	{

		set_Value (COLUMNNAME_TypeAccount, TypeAccount);
	}

	/** Get TypeAccount.
		@return TypeAccount	  */
	public String getTypeAccount () 
	{
		return (String)get_Value(COLUMNNAME_TypeAccount);
	}
}
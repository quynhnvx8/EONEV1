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

/** Generated Model for C_Contract
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_Contract extends PO implements I_C_Contract, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200820L;

    /** Standard Constructor */
    public X_C_Contract (Properties ctx, int C_Contract_ID, String trxName)
    {
      super (ctx, C_Contract_ID, trxName);
      /** if (C_Contract_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_Contract (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_Contract[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Approved.
		@param Approved Approved	  */
	public void setApproved (String Approved)
	{
		set_Value (COLUMNNAME_Approved, Approved);
	}

	/** Get Approved.
		@return Approved	  */
	public String getApproved () 
	{
		return (String)get_Value(COLUMNNAME_Approved);
	}

	/** Set BaseAmtCurrent.
		@param BaseAmtCurrent BaseAmtCurrent	  */
	public void setBaseAmtCurrent (BigDecimal BaseAmtCurrent)
	{
		set_Value (COLUMNNAME_BaseAmtCurrent, BaseAmtCurrent);
	}

	/** Get BaseAmtCurrent.
		@return BaseAmtCurrent	  */
	public BigDecimal getBaseAmtCurrent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BaseAmtCurrent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set BaseAmtOriginal.
		@param BaseAmtOriginal BaseAmtOriginal	  */
	public void setBaseAmtOriginal (BigDecimal BaseAmtOriginal)
	{
		set_Value (COLUMNNAME_BaseAmtOriginal, BaseAmtOriginal);
	}

	/** Get BaseAmtOriginal.
		@return BaseAmtOriginal	  */
	public BigDecimal getBaseAmtOriginal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BaseAmtOriginal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public eone.base.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (eone.base.model.I_C_BPartner)MTable.get(getCtx(), eone.base.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Contract.
		@param C_Contract_ID Contract	  */
	public void setC_Contract_ID (int C_Contract_ID)
	{
		if (C_Contract_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Contract_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Contract_ID, Integer.valueOf(C_Contract_ID));
	}

	/** Get Contract.
		@return Contract	  */
	public int getC_Contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ContractType getC_ContractType() throws RuntimeException
    {
		return (I_C_ContractType)MTable.get(getCtx(), I_C_ContractType.Table_Name)
			.getPO(getC_ContractType_ID(), get_TrxName());	}

	/** Set ContractType.
		@param C_ContractType_ID ContractType	  */
	public void setC_ContractType_ID (int C_ContractType_ID)
	{
		if (C_ContractType_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ContractType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ContractType_ID, Integer.valueOf(C_ContractType_ID));
	}

	/** Get ContractType.
		@return ContractType	  */
	public int getC_ContractType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ContractType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Canceled.
		@param Canceled Canceled	  */
	public void setCanceled (String Canceled)
	{
		set_Value (COLUMNNAME_Canceled, Canceled);
	}

	/** Get Canceled.
		@return Canceled	  */
	public String getCanceled () 
	{
		return (String)get_Value(COLUMNNAME_Canceled);
	}

	/** Set Content.
		@param ContentText Content	  */
	public void setContentText (String ContentText)
	{
		set_Value (COLUMNNAME_ContentText, ContentText);
	}

	/** Get Content.
		@return Content	  */
	public String getContentText () 
	{
		return (String)get_Value(COLUMNNAME_ContentText);
	}

	/** Set DateEffective.
		@param DateEffective DateEffective	  */
	public void setDateEffective (Timestamp DateEffective)
	{
		set_Value (COLUMNNAME_DateEffective, DateEffective);
	}

	/** Get DateEffective.
		@return DateEffective	  */
	public Timestamp getDateEffective () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateEffective);
	}

	/** Set Finish Date.
		@param DateFinish 
		Finish or (planned) completion date
	  */
	public void setDateFinish (Timestamp DateFinish)
	{
		set_Value (COLUMNNAME_DateFinish, DateFinish);
	}

	/** Get Finish Date.
		@return Finish or (planned) completion date
	  */
	public Timestamp getDateFinish () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFinish);
	}

	/** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set DateSign.
		@param DateSign DateSign	  */
	public void setDateSign (Timestamp DateSign)
	{
		set_Value (COLUMNNAME_DateSign, DateSign);
	}

	/** Get DateSign.
		@return DateSign	  */
	public Timestamp getDateSign () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateSign);
	}

	/** Set Date To.
		@param DateTo 
		End date of a date range
	  */
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
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

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Reject = RE */
	public static final String DOCSTATUS_Reject = "RE";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set IsMoreValue.
		@param IsMoreValue IsMoreValue	  */
	public void setIsMoreValue (boolean IsMoreValue)
	{
		set_Value (COLUMNNAME_IsMoreValue, Boolean.valueOf(IsMoreValue));
	}

	/** Get IsMoreValue.
		@return IsMoreValue	  */
	public boolean isMoreValue () 
	{
		Object oo = get_Value(COLUMNNAME_IsMoreValue);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set NumberChange.
		@param NumberChange NumberChange	  */
	public void setNumberChange (int NumberChange)
	{
		set_Value (COLUMNNAME_NumberChange, Integer.valueOf(NumberChange));
	}

	/** Get NumberChange.
		@return NumberChange	  */
	public int getNumberChange () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NumberChange);
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

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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
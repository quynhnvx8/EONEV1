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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_Cash
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_Cash extends PO implements I_C_Cash, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200911L;

    /** Standard Constructor */
    public X_C_Cash (Properties ctx, int C_Cash_ID, String trxName)
    {
      super (ctx, C_Cash_ID, trxName);
      /** if (C_Cash_ID == 0)
        {
			setAccount_Cr_ID (0);
			setAccount_Dr_ID (0);
			setAmount (Env.ZERO);
			setC_Cash_ID (0);
			setC_Currency_ID (0);
// @#C_CurrencyDefault_ID@
			setC_DocType_ID (0);
			setC_DocTypeSub_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDescription (null);
			setDocStatus (null);
// DR
			setPosted (false);
// N
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_C_Cash (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_Cash[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_ElementValue getAccount_Cr() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Cr_ID(), get_TrxName());	}

	/** Set Account Cr.
		@param Account_Cr_ID 
		Account Cr
	  */
	public void setAccount_Cr_ID (int Account_Cr_ID)
	{
		if (Account_Cr_ID < 1) 
			set_Value (COLUMNNAME_Account_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Cr_ID, Integer.valueOf(Account_Cr_ID));
	}

	/** Get Account Cr.
		@return Account Cr
	  */
	public int getAccount_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public org.compiere.model.I_C_ElementValue getAccount_Tax() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Tax_ID(), get_TrxName());	}

	/** Set Account Tax.
		@param Account_Tax_ID Account Tax	  */
	public void setAccount_Tax_ID (int Account_Tax_ID)
	{
		if (Account_Tax_ID < 1) 
			set_Value (COLUMNNAME_Account_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Tax_ID, Integer.valueOf(Account_Tax_ID));
	}

	/** Get Account Tax.
		@return Account Tax	  */
	public int getAccount_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Address.
		@param Address Address	  */
	public void setAddress (String Address)
	{
		set_Value (COLUMNNAME_Address, Address);
	}

	/** Get Address.
		@return Address	  */
	public String getAddress () 
	{
		return (String)get_Value(COLUMNNAME_Address);
	}

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmountConvert.
		@param AmountConvert AmountConvert	  */
	public void setAmountConvert (BigDecimal AmountConvert)
	{
		set_Value (COLUMNNAME_AmountConvert, AmountConvert);
	}

	/** Get AmountConvert.
		@return AmountConvert	  */
	public BigDecimal getAmountConvert () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountConvert);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public org.compiere.model.I_C_BPartner getC_BPartner_Cr() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_Cr_ID(), get_TrxName());	}

	/** Set Business Partner Cr.
		@param C_BPartner_Cr_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_Cr_ID (int C_BPartner_Cr_ID)
	{
		if (C_BPartner_Cr_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Cr_ID, Integer.valueOf(C_BPartner_Cr_ID));
	}

	/** Get Business Partner Cr.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner_Dr() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_Dr_ID(), get_TrxName());	}

	/** Set Business Partner Dr.
		@param C_BPartner_Dr_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_Dr_ID (int C_BPartner_Dr_ID)
	{
		if (C_BPartner_Dr_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Dr_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Dr_ID, Integer.valueOf(C_BPartner_Dr_ID));
	}

	/** Get Business Partner Dr.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_Dr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Dr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Cash Journal.
		@param C_Cash_ID 
		Cash Journal
	  */
	public void setC_Cash_ID (int C_Cash_ID)
	{
		if (C_Cash_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Cash_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Cash_ID, Integer.valueOf(C_Cash_ID));
	}

	/** Get Cash Journal.
		@return Cash Journal
	  */
	public int getC_Cash_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Cash_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Cash_UU.
		@param C_Cash_UU C_Cash_UU	  */
	public void setC_Cash_UU (String C_Cash_UU)
	{
		set_Value (COLUMNNAME_C_Cash_UU, C_Cash_UU);
	}

	/** Get C_Cash_UU.
		@return C_Cash_UU	  */
	public String getC_Cash_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_Cash_UU);
	}

	public I_C_Construction getC_Construction() throws RuntimeException
    {
		return (I_C_Construction)MTable.get(getCtx(), I_C_Construction.Table_Name)
			.getPO(getC_Construction_ID(), get_TrxName());	}

	/** Set Construction.
		@param C_Construction_ID Construction	  */
	public void setC_Construction_ID (int C_Construction_ID)
	{
		if (C_Construction_ID < 1) 
			set_Value (COLUMNNAME_C_Construction_ID, null);
		else 
			set_Value (COLUMNNAME_C_Construction_ID, Integer.valueOf(C_Construction_ID));
	}

	/** Get Construction.
		@return Construction	  */
	public int getC_Construction_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Construction_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ConstructionLine getC_ConstructionLine() throws RuntimeException
    {
		return (I_C_ConstructionLine)MTable.get(getCtx(), I_C_ConstructionLine.Table_Name)
			.getPO(getC_ConstructionLine_ID(), get_TrxName());	}

	/** Set Construction Line.
		@param C_ConstructionLine_ID Construction Line	  */
	public void setC_ConstructionLine_ID (int C_ConstructionLine_ID)
	{
		if (C_ConstructionLine_ID < 1) 
			set_Value (COLUMNNAME_C_ConstructionLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConstructionLine_ID, Integer.valueOf(C_ConstructionLine_ID));
	}

	/** Get Construction Line.
		@return Construction Line	  */
	public int getC_ConstructionLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConstructionLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Contract getC_Contract() throws RuntimeException
    {
		return (I_C_Contract)MTable.get(getCtx(), I_C_Contract.Table_Name)
			.getPO(getC_Contract_ID(), get_TrxName());	}

	/** Set Contract.
		@param C_Contract_ID Contract	  */
	public void setC_Contract_ID (int C_Contract_ID)
	{
		if (C_Contract_ID < 1) 
			set_Value (COLUMNNAME_C_Contract_ID, null);
		else 
			set_Value (COLUMNNAME_C_Contract_ID, Integer.valueOf(C_Contract_ID));
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

	public I_C_ContractAnnex getC_ContractAnnex() throws RuntimeException
    {
		return (I_C_ContractAnnex)MTable.get(getCtx(), I_C_ContractAnnex.Table_Name)
			.getPO(getC_ContractAnnex_ID(), get_TrxName());	}

	/** Set C_ContractAnnex.
		@param C_ContractAnnex_ID C_ContractAnnex	  */
	public void setC_ContractAnnex_ID (int C_ContractAnnex_ID)
	{
		if (C_ContractAnnex_ID < 1) 
			set_Value (COLUMNNAME_C_ContractAnnex_ID, null);
		else 
			set_Value (COLUMNNAME_C_ContractAnnex_ID, Integer.valueOf(C_ContractAnnex_ID));
	}

	/** Get C_ContractAnnex.
		@return C_ContractAnnex	  */
	public int getC_ContractAnnex_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ContractAnnex_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ContractLine getC_ContractLine() throws RuntimeException
    {
		return (I_C_ContractLine)MTable.get(getCtx(), I_C_ContractLine.Table_Name)
			.getPO(getC_ContractLine_ID(), get_TrxName());	}

	/** Set Contract Line.
		@param C_ContractLine_ID Contract Line	  */
	public void setC_ContractLine_ID (int C_ContractLine_ID)
	{
		if (C_ContractLine_ID < 1) 
			set_Value (COLUMNNAME_C_ContractLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_ContractLine_ID, Integer.valueOf(C_ContractLine_ID));
	}

	/** Get Contract Line.
		@return Contract Line	  */
	public int getC_ContractLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ContractLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_DocTypeSub getC_DocTypeSub() throws RuntimeException
    {
		return (I_C_DocTypeSub)MTable.get(getCtx(), I_C_DocTypeSub.Table_Name)
			.getPO(getC_DocTypeSub_ID(), get_TrxName());	}

	/** Set Sub Document.
		@param C_DocTypeSub_ID 
		Document type for generating in dispute Shipments
	  */
	public void setC_DocTypeSub_ID (int C_DocTypeSub_ID)
	{
		if (C_DocTypeSub_ID < 1) 
			set_Value (COLUMNNAME_C_DocTypeSub_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocTypeSub_ID, Integer.valueOf(C_DocTypeSub_ID));
	}

	/** Get Sub Document.
		@return Document type for generating in dispute Shipments
	  */
	public int getC_DocTypeSub_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeSub_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
    {
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ProjectLine getC_ProjectLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_ProjectLine)MTable.get(getCtx(), org.compiere.model.I_C_ProjectLine.Table_Name)
			.getPO(getC_ProjectLine_ID(), get_TrxName());	}

	/** Set Project Line.
		@param C_ProjectLine_ID 
		Task or step in a project
	  */
	public void setC_ProjectLine_ID (int C_ProjectLine_ID)
	{
		if (C_ProjectLine_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectLine_ID, Integer.valueOf(C_ProjectLine_ID));
	}

	/** Get Project Line.
		@return Task or step in a project
	  */
	public int getC_ProjectLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Tax getC_Tax() throws RuntimeException
    {
		return (org.compiere.model.I_C_Tax)MTable.get(getCtx(), org.compiere.model.I_C_Tax.Table_Name)
			.getPO(getC_Tax_ID(), get_TrxName());	}

	/** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID)
	{
		if (C_Tax_ID < 1) 
			set_Value (COLUMNNAME_C_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
	}

	/** Get Tax.
		@return Tax identifier
	  */
	public int getC_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_TypeCost getC_TypeCost() throws RuntimeException
    {
		return (I_C_TypeCost)MTable.get(getCtx(), I_C_TypeCost.Table_Name)
			.getPO(getC_TypeCost_ID(), get_TrxName());	}

	/** Set TypeCost.
		@param C_TypeCost_ID TypeCost	  */
	public void setC_TypeCost_ID (int C_TypeCost_ID)
	{
		if (C_TypeCost_ID < 1) 
			set_Value (COLUMNNAME_C_TypeCost_ID, null);
		else 
			set_Value (COLUMNNAME_C_TypeCost_ID, Integer.valueOf(C_TypeCost_ID));
	}

	/** Get TypeCost.
		@return TypeCost	  */
	public int getC_TypeCost_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TypeCost_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_TypeRevenue getC_TypeRevenue() throws RuntimeException
    {
		return (I_C_TypeRevenue)MTable.get(getCtx(), I_C_TypeRevenue.Table_Name)
			.getPO(getC_TypeRevenue_ID(), get_TrxName());	}

	/** Set Type Revenue.
		@param C_TypeRevenue_ID Type Revenue	  */
	public void setC_TypeRevenue_ID (int C_TypeRevenue_ID)
	{
		if (C_TypeRevenue_ID < 1) 
			set_Value (COLUMNNAME_C_TypeRevenue_ID, null);
		else 
			set_Value (COLUMNNAME_C_TypeRevenue_ID, Integer.valueOf(C_TypeRevenue_ID));
	}

	/** Get Type Revenue.
		@return Type Revenue	  */
	public int getC_TypeRevenue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TypeRevenue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** NET = NET */
	public static final String CALCULATETAX_NET = "NET";
	/** GROSS = GRO */
	public static final String CALCULATETAX_GROSS = "GRO";
	/** Set CalculateTax.
		@param CalculateTax CalculateTax	  */
	public void setCalculateTax (String CalculateTax)
	{

		set_Value (COLUMNNAME_CalculateTax, CalculateTax);
	}

	/** Get CalculateTax.
		@return CalculateTax	  */
	public String getCalculateTax () 
	{
		return (String)get_Value(COLUMNNAME_CalculateTax);
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

	/** Set Rate.
		@param CurrencyRate 
		Currency Conversion Rate
	  */
	public void setCurrencyRate (BigDecimal CurrencyRate)
	{
		set_Value (COLUMNNAME_CurrencyRate, CurrencyRate);
	}

	/** Get Rate.
		@return Currency Conversion Rate
	  */
	public BigDecimal getCurrencyRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CurrencyRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set Date Invoiced.
		@param DateInvoiced 
		Date printed on Invoice
	  */
	public void setDateInvoiced (Timestamp DateInvoiced)
	{
		set_Value (COLUMNNAME_DateInvoiced, DateInvoiced);
	}

	/** Get Date Invoiced.
		@return Date printed on Invoice
	  */
	public Timestamp getDateInvoiced () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateInvoiced);
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

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
    }

	/** NoneTax = NONE */
	public static final String INCLUDETAX_NoneTax = "NONE";
	/** Included = INCL */
	public static final String INCLUDETAX_Included = "INCL";
	/** Set IncludeTax.
		@param IncludeTax IncludeTax	  */
	public void setIncludeTax (String IncludeTax)
	{

		set_Value (COLUMNNAME_IncludeTax, IncludeTax);
	}

	/** Get IncludeTax.
		@return IncludeTax	  */
	public String getIncludeTax () 
	{
		return (String)get_Value(COLUMNNAME_IncludeTax);
	}

	/** Set InvoiceNo.
		@param InvoiceNo InvoiceNo	  */
	public void setInvoiceNo (String InvoiceNo)
	{
		set_Value (COLUMNNAME_InvoiceNo, InvoiceNo);
	}

	/** Get InvoiceNo.
		@return InvoiceNo	  */
	public String getInvoiceNo () 
	{
		return (String)get_Value(COLUMNNAME_InvoiceNo);
	}

	/** Set ObjectName.
		@param ObjectName ObjectName	  */
	public void setObjectName (String ObjectName)
	{
		set_Value (COLUMNNAME_ObjectName, ObjectName);
	}

	/** Get ObjectName.
		@return ObjectName	  */
	public String getObjectName () 
	{
		return (String)get_Value(COLUMNNAME_ObjectName);
	}

	public org.compiere.model.I_PA_ReportLine getPA_ReportLine() throws RuntimeException
    {
		return (org.compiere.model.I_PA_ReportLine)MTable.get(getCtx(), org.compiere.model.I_PA_ReportLine.Table_Name)
			.getPO(getPA_ReportLine_ID(), get_TrxName());	}

	/** Set Report Line.
		@param PA_ReportLine_ID Report Line	  */
	public void setPA_ReportLine_ID (int PA_ReportLine_ID)
	{
		if (PA_ReportLine_ID < 1) 
			set_Value (COLUMNNAME_PA_ReportLine_ID, null);
		else 
			set_Value (COLUMNNAME_PA_ReportLine_ID, Integer.valueOf(PA_ReportLine_ID));
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

	/** Set Posted.
		@param Posted 
		Posting status
	  */
	public void setPosted (boolean Posted)
	{
		set_Value (COLUMNNAME_Posted, Boolean.valueOf(Posted));
	}

	/** Get Posted.
		@return Posting status
	  */
	public boolean isPosted () 
	{
		Object oo = get_Value(COLUMNNAME_Posted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Tax Amount.
		@param TaxAmt 
		Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_ValueNoCheck (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	public BigDecimal getTaxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax base Amount.
		@param TaxBaseAmt 
		Base for calculating the tax amount
	  */
	public void setTaxBaseAmt (BigDecimal TaxBaseAmt)
	{
		set_Value (COLUMNNAME_TaxBaseAmt, TaxBaseAmt);
	}

	/** Get Tax base Amount.
		@return Base for calculating the tax amount
	  */
	public BigDecimal getTaxBaseAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxBaseAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
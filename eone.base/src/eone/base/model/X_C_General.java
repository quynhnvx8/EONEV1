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
import org.compiere.util.KeyNamePair;

/** Generated Model for C_General
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_General extends PO implements I_C_General, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200821L;

    /** Standard Constructor */
    public X_C_General (Properties ctx, int C_General_ID, String trxName)
    {
      super (ctx, C_General_ID, trxName);
      /** if (C_General_ID == 0)
        {
			setAccount_Cr_ID (0);
			setAccount_Dr_ID (0);
			setAmount (Env.ZERO);
			setC_Currency_ID (0);
// @#C_CurrencyDefault_ID@
			setC_DocType_ID (0);
			setC_General_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDescription (null);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setPosted (false);
// N
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_C_General (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_General[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_C_ElementValue getAccount_Cr() throws RuntimeException
    {
		return (eone.base.model.I_C_ElementValue)MTable.get(getCtx(), eone.base.model.I_C_ElementValue.Table_Name)
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

	public eone.base.model.I_C_BPartner getC_BPartner_Cr() throws RuntimeException
    {
		return (eone.base.model.I_C_BPartner)MTable.get(getCtx(), eone.base.model.I_C_BPartner.Table_Name)
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

	public eone.base.model.I_C_BPartner getC_BPartner_Dr() throws RuntimeException
    {
		return (eone.base.model.I_C_BPartner)MTable.get(getCtx(), eone.base.model.I_C_BPartner.Table_Name)
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

	public I_C_BPartnerInfo getC_BPartnerInfo_Cr() throws RuntimeException
    {
		return (I_C_BPartnerInfo)MTable.get(getCtx(), I_C_BPartnerInfo.Table_Name)
			.getPO(getC_BPartnerInfo_Cr_ID(), get_TrxName());	}

	/** Set BPartner Info Cr.
		@param C_BPartnerInfo_Cr_ID BPartner Info Cr	  */
	public void setC_BPartnerInfo_Cr_ID (int C_BPartnerInfo_Cr_ID)
	{
		if (C_BPartnerInfo_Cr_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerInfo_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerInfo_Cr_ID, Integer.valueOf(C_BPartnerInfo_Cr_ID));
	}

	/** Get BPartner Info Cr.
		@return BPartner Info Cr	  */
	public int getC_BPartnerInfo_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerInfo_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartnerInfo getC_BPartnerInfo_Dr() throws RuntimeException
    {
		return (I_C_BPartnerInfo)MTable.get(getCtx(), I_C_BPartnerInfo.Table_Name)
			.getPO(getC_BPartnerInfo_Dr_ID(), get_TrxName());	}

	/** Set BPartner Info Dr.
		@param C_BPartnerInfo_Dr_ID BPartner Info Dr	  */
	public void setC_BPartnerInfo_Dr_ID (int C_BPartnerInfo_Dr_ID)
	{
		if (C_BPartnerInfo_Dr_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerInfo_Dr_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerInfo_Dr_ID, Integer.valueOf(C_BPartnerInfo_Dr_ID));
	}

	/** Get BPartner Info Dr.
		@return BPartner Info Dr	  */
	public int getC_BPartnerInfo_Dr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerInfo_Dr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_CashActivity getC_CashActivity() throws RuntimeException
    {
		return (I_C_CashActivity)MTable.get(getCtx(), I_C_CashActivity.Table_Name)
			.getPO(getC_CashActivity_ID(), get_TrxName());	}

	/** Set C_CashActivity.
		@param C_CashActivity_ID C_CashActivity	  */
	public void setC_CashActivity_ID (int C_CashActivity_ID)
	{
		if (C_CashActivity_ID < 1) 
			set_Value (COLUMNNAME_C_CashActivity_ID, null);
		else 
			set_Value (COLUMNNAME_C_CashActivity_ID, Integer.valueOf(C_CashActivity_ID));
	}

	/** Get C_CashActivity.
		@return C_CashActivity	  */
	public int getC_CashActivity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CashActivity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public eone.base.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (eone.base.model.I_C_Currency)MTable.get(getCtx(), eone.base.model.I_C_Currency.Table_Name)
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

	public eone.base.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (eone.base.model.I_C_DocType)MTable.get(getCtx(), eone.base.model.I_C_DocType.Table_Name)
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

	/** Set Gereral.
		@param C_General_ID Gereral	  */
	public void setC_General_ID (int C_General_ID)
	{
		if (C_General_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_General_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_General_ID, Integer.valueOf(C_General_ID));
	}

	/** Get Gereral.
		@return Gereral	  */
	public int getC_General_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_General_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_General_UU.
		@param C_General_UU C_General_UU	  */
	public void setC_General_UU (String C_General_UU)
	{
		set_Value (COLUMNNAME_C_General_UU, C_General_UU);
	}

	/** Get C_General_UU.
		@return C_General_UU	  */
	public String getC_General_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_General_UU);
	}

	public eone.base.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (eone.base.model.I_C_Invoice)MTable.get(getCtx(), eone.base.model.I_C_Invoice.Table_Name)
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

	public eone.base.model.I_C_Project getC_Project() throws RuntimeException
    {
		return (eone.base.model.I_C_Project)MTable.get(getCtx(), eone.base.model.I_C_Project.Table_Name)
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

	public eone.base.model.I_C_ProjectLine getC_ProjectLine() throws RuntimeException
    {
		return (eone.base.model.I_C_ProjectLine)MTable.get(getCtx(), eone.base.model.I_C_ProjectLine.Table_Name)
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

	public eone.base.model.I_C_UOM getC_UOM() throws RuntimeException
    {
		return (eone.base.model.I_C_UOM)MTable.get(getCtx(), eone.base.model.I_C_UOM.Table_Name)
			.getPO(getC_UOM_ID(), get_TrxName());	}

	/** Set UOM.
		@param C_UOM_ID 
		Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_UOM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
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
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
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

	public eone.base.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (eone.base.model.I_M_Product)MTable.get(getCtx(), eone.base.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_M_Warehouse getM_Warehouse_Cr() throws RuntimeException
    {
		return (eone.base.model.I_M_Warehouse)MTable.get(getCtx(), eone.base.model.I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_Cr_ID(), get_TrxName());	}

	/** Set Warehouse Cr.
		@param M_Warehouse_Cr_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_Cr_ID (int M_Warehouse_Cr_ID)
	{
		if (M_Warehouse_Cr_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_Cr_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Warehouse_Cr_ID, Integer.valueOf(M_Warehouse_Cr_ID));
	}

	/** Get Warehouse Cr.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_M_Warehouse getM_Warehouse_Dr() throws RuntimeException
    {
		return (eone.base.model.I_M_Warehouse)MTable.get(getCtx(), eone.base.model.I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_Dr_ID(), get_TrxName());	}

	/** Set Warehouse Dr.
		@param M_Warehouse_Dr_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_Dr_ID (int M_Warehouse_Dr_ID)
	{
		if (M_Warehouse_Dr_ID < 1) 
			set_Value (COLUMNNAME_M_Warehouse_Dr_ID, null);
		else 
			set_Value (COLUMNNAME_M_Warehouse_Dr_ID, Integer.valueOf(M_Warehouse_Dr_ID));
	}

	/** Get Warehouse Dr.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_Dr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_Dr_ID);
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

	/** Set Price.
		@param Price 
		Price
	  */
	public void setPrice (BigDecimal Price)
	{
		set_ValueNoCheck (COLUMNNAME_Price, Price);
	}

	/** Get Price.
		@return Price
	  */
	public BigDecimal getPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Price);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
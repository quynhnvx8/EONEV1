/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for Fact_Acct
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_Fact_Acct extends PO implements I_Fact_Acct, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210810L;

    /** Standard Constructor */
    public X_Fact_Acct (Properties ctx, int Fact_Acct_ID, String trxName)
    {
      super (ctx, Fact_Acct_ID, trxName);
      /** if (Fact_Acct_ID == 0)
        {
			setAD_Table_ID (0);
			setC_Currency_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
			setFact_Acct_ID (0);
			setPostingType (null);
			setRecord_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Fact_Acct (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_Fact_Acct[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_A_Asset getA_Asset() throws RuntimeException
    {
		return (eone.base.model.I_A_Asset)MTable.get(getCtx(), eone.base.model.I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_AD_Department getAD_Department() throws RuntimeException
    {
		return (I_AD_Department)MTable.get(getCtx(), I_AD_Department.Table_Name)
			.getPO(getAD_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param AD_Department_ID Department	  */
	public void setAD_Department_ID (int AD_Department_ID)
	{
		if (AD_Department_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Department_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Department_ID, Integer.valueOf(AD_Department_ID));
	}

	/** Get Department.
		@return Department	  */
	public int getAD_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_Table getAD_Table() throws RuntimeException
    {
		return (eone.base.model.I_AD_Table)MTable.get(getCtx(), eone.base.model.I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_Window getAD_Window() throws RuntimeException
    {
		return (eone.base.model.I_AD_Window)MTable.get(getCtx(), eone.base.model.I_AD_Window.Table_Name)
			.getPO(getAD_Window_ID(), get_TrxName());	}

	/** Set Window.
		@param AD_Window_ID 
		Data entry or display window
	  */
	public void setAD_Window_ID (int AD_Window_ID)
	{
		if (AD_Window_ID < 1) 
			set_Value (COLUMNNAME_AD_Window_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Window_ID, Integer.valueOf(AD_Window_ID));
	}

	/** Get Window.
		@return Data entry or display window
	  */
	public int getAD_Window_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Window_ID);
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
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
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
			set_ValueNoCheck (COLUMNNAME_C_DocTypeSub_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeSub_ID, Integer.valueOf(C_DocTypeSub_ID));
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

	public eone.base.model.I_C_Tax getC_Tax() throws RuntimeException
    {
		return (eone.base.model.I_C_Tax)MTable.get(getCtx(), eone.base.model.I_C_Tax.Table_Name)
			.getPO(getC_Tax_ID(), get_TrxName());	}

	/** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID)
	{
		if (C_Tax_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Tax_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
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
			set_ValueNoCheck (COLUMNNAME_C_TypeCost_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TypeCost_ID, Integer.valueOf(C_TypeCost_ID));
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
			set_ValueNoCheck (COLUMNNAME_C_TypeRevenue_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TypeRevenue_ID, Integer.valueOf(C_TypeRevenue_ID));
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

	/** Set Accounting Fact.
		@param Fact_Acct_ID Accounting Fact	  */
	public void setFact_Acct_ID (int Fact_Acct_ID)
	{
		if (Fact_Acct_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Fact_Acct_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Fact_Acct_ID, Integer.valueOf(Fact_Acct_ID));
	}

	/** Get Accounting Fact.
		@return Accounting Fact	  */
	public int getFact_Acct_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Fact_Acct_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Line ID.
		@param Line_ID 
		Transaction line ID (internal)
	  */
	public void setLine_ID (int Line_ID)
	{
		if (Line_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Line_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Line_ID, Integer.valueOf(Line_ID));
	}

	/** Get Line ID.
		@return Transaction line ID (internal)
	  */
	public int getLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** LessThan3 = 03 */
	public static final String LISTDURATION_LessThan3 = "03";
	/** LessThan6 = 06 */
	public static final String LISTDURATION_LessThan6 = "06";
	/** LessThan12 = 12 */
	public static final String LISTDURATION_LessThan12 = "12";
	/** GreatThan12 = 13 */
	public static final String LISTDURATION_GreatThan12 = "13";
	/** Set ListDuration.
		@param ListDuration ListDuration	  */
	public void setListDuration (String ListDuration)
	{

		set_Value (COLUMNNAME_ListDuration, ListDuration);
	}

	/** Get ListDuration.
		@return ListDuration	  */
	public String getListDuration () 
	{
		return (String)get_Value(COLUMNNAME_ListDuration);
	}

	public eone.base.model.I_M_Product getM_Product_Cr() throws RuntimeException
    {
		return (eone.base.model.I_M_Product)MTable.get(getCtx(), eone.base.model.I_M_Product.Table_Name)
			.getPO(getM_Product_Cr_ID(), get_TrxName());	}

	/** Set Product Cr.
		@param M_Product_Cr_ID Product Cr	  */
	public void setM_Product_Cr_ID (int M_Product_Cr_ID)
	{
		if (M_Product_Cr_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Cr_ID, Integer.valueOf(M_Product_Cr_ID));
	}

	/** Get Product Cr.
		@return Product Cr	  */
	public int getM_Product_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_M_Workshop getM_Workshop_Cr() throws RuntimeException
    {
		return (I_M_Workshop)MTable.get(getCtx(), I_M_Workshop.Table_Name)
			.getPO(getM_Workshop_Cr_ID(), get_TrxName());	}

	/** Set Workshop Cr.
		@param M_Workshop_Cr_ID Workshop Cr	  */
	public void setM_Workshop_Cr_ID (int M_Workshop_Cr_ID)
	{
		if (M_Workshop_Cr_ID < 1) 
			set_Value (COLUMNNAME_M_Workshop_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_M_Workshop_Cr_ID, Integer.valueOf(M_Workshop_Cr_ID));
	}

	/** Get Workshop Cr.
		@return Workshop Cr	  */
	public int getM_Workshop_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Workshop_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Workshop getM_Workshop() throws RuntimeException
    {
		return (I_M_Workshop)MTable.get(getCtx(), I_M_Workshop.Table_Name)
			.getPO(getM_Workshop_ID(), get_TrxName());	}

	/** Set Workshop.
		@param M_Workshop_ID Workshop	  */
	public void setM_Workshop_ID (int M_Workshop_ID)
	{
		if (M_Workshop_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Workshop_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Workshop_ID, Integer.valueOf(M_Workshop_ID));
	}

	/** Get Workshop.
		@return Workshop	  */
	public int getM_Workshop_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Workshop_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** PostingType AD_Reference_ID=125 */
	public static final int POSTINGTYPE_AD_Reference_ID=125;
	/** Actual = A */
	public static final String POSTINGTYPE_Actual = "A";
	/** Wait = W */
	public static final String POSTINGTYPE_Wait = "W";
	/** Set PostingType.
		@param PostingType 
		The type of posted amount for the transaction
	  */
	public void setPostingType (String PostingType)
	{

		set_ValueNoCheck (COLUMNNAME_PostingType, PostingType);
	}

	/** Get PostingType.
		@return The type of posted amount for the transaction
	  */
	public String getPostingType () 
	{
		return (String)get_Value(COLUMNNAME_PostingType);
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

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_Record_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
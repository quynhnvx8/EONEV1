/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for PA_ReportLine
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_PA_ReportLine extends PO implements I_PA_ReportLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210806L;

    /** Standard Constructor */
    public X_PA_ReportLine (Properties ctx, int PA_ReportLine_ID, String trxName)
    {
      super (ctx, PA_ReportLine_ID, trxName);
      /** if (PA_ReportLine_ID == 0)
        {
			setIsSummary (false);
			setName (null);
			setPA_ReportLine_ID (0);
			setSeqNo (0);
// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM PA_ReportLine WHERE PA_ReportLine_ID=@PA_ReportLine_ID@
        } */
    }

    /** Load Constructor */
    public X_PA_ReportLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_PA_ReportLine[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Account Element.
		@param C_ElementValue_ID 
		Account Element
	  */
	public void setC_ElementValue_ID (String C_ElementValue_ID)
	{

		set_Value (COLUMNNAME_C_ElementValue_ID, C_ElementValue_ID);
	}

	/** Get Account Element.
		@return Account Element
	  */
	public String getC_ElementValue_ID () 
	{
		return (String)get_Value(COLUMNNAME_C_ElementValue_ID);
	}

	/** Set Account Element Other.
		@param C_ElementValueOT_ID 
		Account Element
	  */
	public void setC_ElementValueOT_ID (String C_ElementValueOT_ID)
	{

		set_ValueNoCheck (COLUMNNAME_C_ElementValueOT_ID, C_ElementValueOT_ID);
	}

	/** Get Account Element Other.
		@return Account Element
	  */
	public String getC_ElementValueOT_ID () 
	{
		return (String)get_Value(COLUMNNAME_C_ElementValueOT_ID);
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

	/** Set FixZero.
		@param FixZero FixZero	  */
	public void setFixZero (int FixZero)
	{
		set_Value (COLUMNNAME_FixZero, Integer.valueOf(FixZero));
	}

	/** Get FixZero.
		@return FixZero	  */
	public int getFixZero () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FixZero);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FormulaSetup.
		@param FormulaSetup FormulaSetup	  */
	public void setFormulaSetup (String FormulaSetup)
	{
		set_Value (COLUMNNAME_FormulaSetup, FormulaSetup);
	}

	/** Get FormulaSetup.
		@return FormulaSetup	  */
	public String getFormulaSetup () 
	{
		return (String)get_Value(COLUMNNAME_FormulaSetup);
	}

	/** Set IsBisexual .
		@param IsBisexual IsBisexual 	  */
	public void setIsBisexual (boolean IsBisexual)
	{
		set_Value (COLUMNNAME_IsBisexual, Boolean.valueOf(IsBisexual));
	}

	/** Get IsBisexual .
		@return IsBisexual 	  */
	public boolean isBisexual () 
	{
		Object oo = get_Value(COLUMNNAME_IsBisexual);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsBold.
		@param IsBold IsBold	  */
	public void setIsBold (boolean IsBold)
	{
		set_Value (COLUMNNAME_IsBold, Boolean.valueOf(IsBold));
	}

	/** Get IsBold.
		@return IsBold	  */
	public boolean isBold () 
	{
		Object oo = get_Value(COLUMNNAME_IsBold);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsItalic.
		@param IsItalic IsItalic	  */
	public void setIsItalic (boolean IsItalic)
	{
		set_Value (COLUMNNAME_IsItalic, Boolean.valueOf(IsItalic));
	}

	/** Get IsItalic.
		@return IsItalic	  */
	public boolean isItalic () 
	{
		Object oo = get_Value(COLUMNNAME_IsItalic);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Printed.
		@param IsPrinted 
		Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted)
	{
		set_Value (COLUMNNAME_IsPrinted, Boolean.valueOf(IsPrinted));
	}

	/** Get Printed.
		@return Indicates if this document / line is printed
	  */
	public boolean isPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Summary Level.
		@param IsSummary 
		This is a summary entity
	  */
	public void setIsSummary (boolean IsSummary)
	{
		set_Value (COLUMNNAME_IsSummary, Boolean.valueOf(IsSummary));
	}

	/** Get Summary Level.
		@return This is a summary entity
	  */
	public boolean isSummary () 
	{
		Object oo = get_Value(COLUMNNAME_IsSummary);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Multiply Value.
		@param MultiplyValue Multiply Value	  */
	public void setMultiplyValue (int MultiplyValue)
	{
		set_Value (COLUMNNAME_MultiplyValue, Integer.valueOf(MultiplyValue));
	}

	/** Get Multiply Value.
		@return Multiply Value	  */
	public int getMultiplyValue () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MultiplyValue);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
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

	public eone.base.model.I_PA_Report getPA_Report() throws RuntimeException
    {
		return (eone.base.model.I_PA_Report)MTable.get(getCtx(), eone.base.model.I_PA_Report.Table_Name)
			.getPO(getPA_Report_ID(), get_TrxName());	}

	/** Set Financial Report.
		@param PA_Report_ID 
		Financial Report
	  */
	public void setPA_Report_ID (int PA_Report_ID)
	{
		if (PA_Report_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PA_Report_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PA_Report_ID, Integer.valueOf(PA_Report_ID));
	}

	/** Get Financial Report.
		@return Financial Report
	  */
	public int getPA_Report_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_Report_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TypeAccount AD_Reference_ID=53280 */
	public static final int TYPEACCOUNT_AD_Reference_ID=53280;
	/** BalanceCreditAcct = BCR_ACCT */
	public static final String TYPEACCOUNT_BalanceCreditAcct = "BCR_ACCT";
	/** BalanceDebitAcct = BDR_ACCT */
	public static final String TYPEACCOUNT_BalanceDebitAcct = "BDR_ACCT";
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

	/** TypeAccountOT AD_Reference_ID=53280 */
	public static final int TYPEACCOUNTOT_AD_Reference_ID=53280;
	/** BalanceCreditAcct = BCR_ACCT */
	public static final String TYPEACCOUNTOT_BalanceCreditAcct = "BCR_ACCT";
	/** BalanceDebitAcct = BDR_ACCT */
	public static final String TYPEACCOUNTOT_BalanceDebitAcct = "BDR_ACCT";
	/** AccumulateDebitAcct = ACCDR_ACCT */
	public static final String TYPEACCOUNTOT_AccumulateDebitAcct = "ACCDR_ACCT";
	/** AccumulateCreditAcct = ACCCR_ACCT */
	public static final String TYPEACCOUNTOT_AccumulateCreditAcct = "ACCCR_ACCT";
	/** IncuredCreditAcct = INCCR_ACCT */
	public static final String TYPEACCOUNTOT_IncuredCreditAcct = "INCCR_ACCT";
	/** IncuredDebitAcct = INCDR_ACCT */
	public static final String TYPEACCOUNTOT_IncuredDebitAcct = "INCDR_ACCT";
	/** Set TypeAccount Other.
		@param TypeAccountOT TypeAccount Other	  */
	public void setTypeAccountOT (String TypeAccountOT)
	{

		set_Value (COLUMNNAME_TypeAccountOT, TypeAccountOT);
	}

	/** Get TypeAccount Other.
		@return TypeAccount Other	  */
	public String getTypeAccountOT () 
	{
		return (String)get_Value(COLUMNNAME_TypeAccountOT);
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
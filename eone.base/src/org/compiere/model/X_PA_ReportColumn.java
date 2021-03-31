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
import org.compiere.util.KeyNamePair;

/** Generated Model for PA_ReportColumn
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_PA_ReportColumn extends PO implements I_PA_ReportColumn, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200807L;

    /** Standard Constructor */
    public X_PA_ReportColumn (Properties ctx, int PA_ReportColumn_ID, String trxName)
    {
      super (ctx, PA_ReportColumn_ID, trxName);
      /** if (PA_ReportColumn_ID == 0)
        {
			setIsPrinted (true);
// Y
			setName (null);
			setPA_ReportColumn_ID (0);
			setSeqNo (0);
// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM PA_ReportColumn WHERE PA_Report_ID=@PA_Report_ID@
        } */
    }

    /** Load Constructor */
    public X_PA_ReportColumn (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_PA_ReportColumn[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** AmountType AD_Reference_ID=53328 */
	public static final int AMOUNTTYPE_AD_Reference_ID=53328;
	/** Balance First = BF */
	public static final String AMOUNTTYPE_BalanceFirst = "BF";
	/** Balance Last = BL */
	public static final String AMOUNTTYPE_BalanceLast = "BL";
	/** Arising Period = AP */
	public static final String AMOUNTTYPE_ArisingPeriod = "AP";
	/** Accumulated = AC */
	public static final String AMOUNTTYPE_Accumulated = "AC";
	/** Set Amount Type.
		@param AmountType 
		Type of amount to report
	  */
	public void setAmountType (String AmountType)
	{

		set_Value (COLUMNNAME_AmountType, AmountType);
	}

	/** Get Amount Type.
		@return Type of amount to report
	  */
	public String getAmountType () 
	{
		return (String)get_Value(COLUMNNAME_AmountType);
	}

	/** O1 Add O2 = ADD */
	public static final String CALCULATIONCOLUMN_O1AddO2 = "ADD";
	/** O1 Sub O2 = SUB */
	public static final String CALCULATIONCOLUMN_O1SubO2 = "SUB";
	/** O1 Muilti O2 = MUL */
	public static final String CALCULATIONCOLUMN_O1MuiltiO2 = "MUL";
	/** O1 DIV O2 = DIV */
	public static final String CALCULATIONCOLUMN_O1DIVO2 = "DIV";
	/** O1 Percent O2 = PER */
	public static final String CALCULATIONCOLUMN_O1PercentO2 = "PER";
	/** O1 Average O2 = AVE */
	public static final String CALCULATIONCOLUMN_O1AverageO2 = "AVE";
	/** Max O1 O2 = MAX */
	public static final String CALCULATIONCOLUMN_MaxO1O2 = "MAX";
	/** Min O1 O2 = MIN */
	public static final String CALCULATIONCOLUMN_MinO1O2 = "MIN";
	/** Set CalculationColumn.
		@param CalculationColumn CalculationColumn	  */
	public void setCalculationColumn (String CalculationColumn)
	{

		set_Value (COLUMNNAME_CalculationColumn, CalculationColumn);
	}

	/** Get CalculationColumn.
		@return CalculationColumn	  */
	public String getCalculationColumn () 
	{
		return (String)get_Value(COLUMNNAME_CalculationColumn);
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

	/** Factor AD_Reference_ID=53285 */
	public static final int FACTOR_AD_Reference_ID=53285;
	/** Thousand = k */
	public static final String FACTOR_Thousand = "k";
	/** Million = M */
	public static final String FACTOR_Million = "M";
	/** None = N */
	public static final String FACTOR_None = "N";
	/** Set Factor.
		@param Factor 
		Scaling factor.
	  */
	public void setFactor (String Factor)
	{

		set_Value (COLUMNNAME_Factor, Factor);
	}

	/** Get Factor.
		@return Scaling factor.
	  */
	public String getFactor () 
	{
		return (String)get_Value(COLUMNNAME_Factor);
	}

	/** Set IsFormula.
		@param IsFormula IsFormula	  */
	public void setIsFormula (boolean IsFormula)
	{
		set_Value (COLUMNNAME_IsFormula, Boolean.valueOf(IsFormula));
	}

	/** Get IsFormula.
		@return IsFormula	  */
	public boolean isFormula () 
	{
		Object oo = get_Value(COLUMNNAME_IsFormula);
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

	public org.compiere.model.I_PA_ReportColumn getOper_1() throws RuntimeException
    {
		return (org.compiere.model.I_PA_ReportColumn)MTable.get(getCtx(), org.compiere.model.I_PA_ReportColumn.Table_Name)
			.getPO(getOper_1_ID(), get_TrxName());	}

	/** Set Operand 1.
		@param Oper_1_ID 
		First operand for calculation
	  */
	public void setOper_1_ID (int Oper_1_ID)
	{
		if (Oper_1_ID < 1) 
			set_Value (COLUMNNAME_Oper_1_ID, null);
		else 
			set_Value (COLUMNNAME_Oper_1_ID, Integer.valueOf(Oper_1_ID));
	}

	/** Get Operand 1.
		@return First operand for calculation
	  */
	public int getOper_1_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Oper_1_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_PA_ReportColumn getOper_2() throws RuntimeException
    {
		return (org.compiere.model.I_PA_ReportColumn)MTable.get(getCtx(), org.compiere.model.I_PA_ReportColumn.Table_Name)
			.getPO(getOper_2_ID(), get_TrxName());	}

	/** Set Operand 2.
		@param Oper_2_ID 
		Second operand for calculation
	  */
	public void setOper_2_ID (int Oper_2_ID)
	{
		if (Oper_2_ID < 1) 
			set_Value (COLUMNNAME_Oper_2_ID, null);
		else 
			set_Value (COLUMNNAME_Oper_2_ID, Integer.valueOf(Oper_2_ID));
	}

	/** Get Operand 2.
		@return Second operand for calculation
	  */
	public int getOper_2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Oper_2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public org.compiere.model.I_PA_Report getPA_Report() throws RuntimeException
    {
		return (org.compiere.model.I_PA_Report)MTable.get(getCtx(), org.compiere.model.I_PA_Report.Table_Name)
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

	/** Set Report Column.
		@param PA_ReportColumn_ID 
		Column in Report
	  */
	public void setPA_ReportColumn_ID (int PA_ReportColumn_ID)
	{
		if (PA_ReportColumn_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PA_ReportColumn_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PA_ReportColumn_ID, Integer.valueOf(PA_ReportColumn_ID));
	}

	/** Get Report Column.
		@return Column in Report
	  */
	public int getPA_ReportColumn_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_ReportColumn_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PA_ReportColumn_UU.
		@param PA_ReportColumn_UU PA_ReportColumn_UU	  */
	public void setPA_ReportColumn_UU (String PA_ReportColumn_UU)
	{
		set_Value (COLUMNNAME_PA_ReportColumn_UU, PA_ReportColumn_UU);
	}

	/** Get PA_ReportColumn_UU.
		@return PA_ReportColumn_UU	  */
	public String getPA_ReportColumn_UU () 
	{
		return (String)get_Value(COLUMNNAME_PA_ReportColumn_UU);
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
}
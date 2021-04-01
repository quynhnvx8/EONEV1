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
import org.compiere.util.KeyNamePair;

/** Generated Model for C_DocType
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_DocType extends PO implements I_C_DocType, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200904L;

    /** Standard Constructor */
    public X_C_DocType (Properties ctx, int C_DocType_ID, String trxName)
    {
      super (ctx, C_DocType_ID, trxName);
      /** if (C_DocType_ID == 0)
        {
			setC_DocType_ID (0);
			setDocBaseType (null);
			setIsDefault (false);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_C_DocType (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
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
      StringBuilder sb = new StringBuilder ("X_C_DocType[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

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

	/** Set C_DocType_UU.
		@param C_DocType_UU C_DocType_UU	  */
	public void setC_DocType_UU (String C_DocType_UU)
	{
		set_Value (COLUMNNAME_C_DocType_UU, C_DocType_UU);
	}

	/** Get C_DocType_UU.
		@return C_DocType_UU	  */
	public String getC_DocType_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_DocType_UU);
	}

	/** Set Date Column.
		@param DateColumn 
		Fully qualified date column
	  */
	public void setDateColumn (String DateColumn)
	{
		set_Value (COLUMNNAME_DateColumn, DateColumn);
	}

	/** Get Date Column.
		@return Fully qualified date column
	  */
	public String getDateColumn () 
	{
		return (String)get_Value(COLUMNNAME_DateColumn);
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

	/** DocBaseType AD_Reference_ID=183 */
	public static final int DOCBASETYPE_AD_Reference_ID=183;
	/** 111 Debit = 111DR */
	public static final String DOCBASETYPE_111Debit = "111DR";
	/** 112 Debit = 112DR */
	public static final String DOCBASETYPE_112Debit = "112DR";
	/** Debit_113 = DR113 */
	public static final String DOCBASETYPE_Debit_113 = "DR113";
	/** Debit_121 = DR121 */
	public static final String DOCBASETYPE_Debit_121 = "DR121";
	/** Debit_128 = DR128 */
	public static final String DOCBASETYPE_Debit_128 = "DR128";
	/** Debit_131 = DR131 */
	public static final String DOCBASETYPE_Debit_131 = "DR131";
	/** Debit_133 = DR133 */
	public static final String DOCBASETYPE_Debit_133 = "DR133";
	/** Debit_136 = DR136 */
	public static final String DOCBASETYPE_Debit_136 = "DR136";
	/** Debit_138 = DR138 */
	public static final String DOCBASETYPE_Debit_138 = "DR138";
	/** Debit_141 = DR141 */
	public static final String DOCBASETYPE_Debit_141 = "DR141";
	/** Debit_151 = DR151 */
	public static final String DOCBASETYPE_Debit_151 = "DR151";
	/** 152 Debit = 152DR */
	public static final String DOCBASETYPE_152Debit = "152DR";
	/** 153 Debit = 153DR */
	public static final String DOCBASETYPE_153Debit = "153DR";
	/** 154 Debit = 154DR */
	public static final String DOCBASETYPE_154Debit = "154DR";
	/** 155 Debit = 155DR */
	public static final String DOCBASETYPE_155Debit = "155DR";
	/** 156 Debit = 156DR */
	public static final String DOCBASETYPE_156Debit = "156DR";
	/** Debit_157 = DR157 */
	public static final String DOCBASETYPE_Debit_157 = "DR157";
	/** Debit_158 = DR158 */
	public static final String DOCBASETYPE_Debit_158 = "DR158";
	/** Debit_161 = DR161 */
	public static final String DOCBASETYPE_Debit_161 = "DR161";
	/** Debit_171 = DR171 */
	public static final String DOCBASETYPE_Debit_171 = "DR171";
	/** Debit_212 = DR212 */
	public static final String DOCBASETYPE_Debit_212 = "DR212";
	/** Debit_213 = DR213 */
	public static final String DOCBASETYPE_Debit_213 = "DR213";
	/** Debit_214 = DR214 */
	public static final String DOCBASETYPE_Debit_214 = "DR214";
	/** Debit_217 = DR217 */
	public static final String DOCBASETYPE_Debit_217 = "DR217";
	/** Debit_221 = DR221 */
	public static final String DOCBASETYPE_Debit_221 = "DR221";
	/** Debit_222 = DR222 */
	public static final String DOCBASETYPE_Debit_222 = "DR222";
	/** Debit_228 = DR228 */
	public static final String DOCBASETYPE_Debit_228 = "DR228";
	/** Debit_229 = DR229 */
	public static final String DOCBASETYPE_Debit_229 = "DR229";
	/** Debit_241 = DR241 */
	public static final String DOCBASETYPE_Debit_241 = "DR241";
	/** Debit_242 = DR242 */
	public static final String DOCBASETYPE_Debit_242 = "DR242";
	/** Debit_243 = DR243 */
	public static final String DOCBASETYPE_Debit_243 = "DR243";
	/** Debit_244 = DR244 */
	public static final String DOCBASETYPE_Debit_244 = "DR244";
	/** Debit_331 = DR331 */
	public static final String DOCBASETYPE_Debit_331 = "DR331";
	/** Debit_333 = DR333 */
	public static final String DOCBASETYPE_Debit_333 = "DR333";
	/** Debit_334 = DR334 */
	public static final String DOCBASETYPE_Debit_334 = "DR334";
	/** Debit_335 = DR335 */
	public static final String DOCBASETYPE_Debit_335 = "DR335";
	/** Debit_336 = DR336 */
	public static final String DOCBASETYPE_Debit_336 = "DR336";
	/** Debit_337 = DR337 */
	public static final String DOCBASETYPE_Debit_337 = "DR337";
	/** Debit_338 = DR338 */
	public static final String DOCBASETYPE_Debit_338 = "DR338";
	/** Debit_341 = DR341 */
	public static final String DOCBASETYPE_Debit_341 = "DR341";
	/** Debit_343 = DR343 */
	public static final String DOCBASETYPE_Debit_343 = "DR343";
	/** Debit_344 = DR344 */
	public static final String DOCBASETYPE_Debit_344 = "DR344";
	/** Debit_347 = DR347 */
	public static final String DOCBASETYPE_Debit_347 = "DR347";
	/** Debit_352 = DR352 */
	public static final String DOCBASETYPE_Debit_352 = "DR352";
	/** Debit_353 = DR353 */
	public static final String DOCBASETYPE_Debit_353 = "DR353";
	/** Debit_356 = DR356 */
	public static final String DOCBASETYPE_Debit_356 = "DR356";
	/** Debit_357 = DR357 */
	public static final String DOCBASETYPE_Debit_357 = "DR357";
	/** Debit_411 = DR411 */
	public static final String DOCBASETYPE_Debit_411 = "DR411";
	/** Debit_412 = DR412 */
	public static final String DOCBASETYPE_Debit_412 = "DR412";
	/** Debit_413 = DR413 */
	public static final String DOCBASETYPE_Debit_413 = "DR413";
	/** Debit_414 = DR414 */
	public static final String DOCBASETYPE_Debit_414 = "DR414";
	/** Debit_417 = DR417 */
	public static final String DOCBASETYPE_Debit_417 = "DR417";
	/** Debit_418 = DR418 */
	public static final String DOCBASETYPE_Debit_418 = "DR418";
	/** Debit_419 = DR419 */
	public static final String DOCBASETYPE_Debit_419 = "DR419";
	/** Debit_421 = DR421 */
	public static final String DOCBASETYPE_Debit_421 = "DR421";
	/** Debit_441 = DR441 */
	public static final String DOCBASETYPE_Debit_441 = "DR441";
	/** Debit_461 = DR461 */
	public static final String DOCBASETYPE_Debit_461 = "DR461";
	/** Debit_466 = DR466 */
	public static final String DOCBASETYPE_Debit_466 = "DR466";
	/** 111 Credit = 111CR */
	public static final String DOCBASETYPE_111Credit = "111CR";
	/** 112 Credit = 112CR */
	public static final String DOCBASETYPE_112Credit = "112CR";
	/** Credit_113 = CR113 */
	public static final String DOCBASETYPE_Credit_113 = "CR113";
	/** Credit_121 = CR121 */
	public static final String DOCBASETYPE_Credit_121 = "CR121";
	/** Credit_128 = CR128 */
	public static final String DOCBASETYPE_Credit_128 = "CR128";
	/** Credit_131 = CR131 */
	public static final String DOCBASETYPE_Credit_131 = "CR131";
	/** Credit_133 = CR133 */
	public static final String DOCBASETYPE_Credit_133 = "CR133";
	/** Credit_136 = CR136 */
	public static final String DOCBASETYPE_Credit_136 = "CR136";
	/** Credit_138 = CR138 */
	public static final String DOCBASETYPE_Credit_138 = "CR138";
	/** Credit_141 = CR141 */
	public static final String DOCBASETYPE_Credit_141 = "CR141";
	/** Credit_151 = CR151 */
	public static final String DOCBASETYPE_Credit_151 = "CR151";
	/** 152 Credit = 152CR */
	public static final String DOCBASETYPE_152Credit = "152CR";
	/** 153 Credit = 153CR */
	public static final String DOCBASETYPE_153Credit = "153CR";
	/** 154 Credit = 154CR */
	public static final String DOCBASETYPE_154Credit = "154CR";
	/** 155 Credit = 155CR */
	public static final String DOCBASETYPE_155Credit = "155CR";
	/** 156 Credit = 156CR */
	public static final String DOCBASETYPE_156Credit = "156CR";
	/** Credit_157 = CR157 */
	public static final String DOCBASETYPE_Credit_157 = "CR157";
	/** Credit_158 = CR158 */
	public static final String DOCBASETYPE_Credit_158 = "CR158";
	/** Credit_161 = CR161 */
	public static final String DOCBASETYPE_Credit_161 = "CR161";
	/** Credit_171 = CR171 */
	public static final String DOCBASETYPE_Credit_171 = "CR171";
	/** 211 Upgrade = 211UP */
	public static final String DOCBASETYPE_211Upgrade = "211UP";
	/** Credit_212 = CR212 */
	public static final String DOCBASETYPE_Credit_212 = "CR212";
	/** Credit_213 = CR213 */
	public static final String DOCBASETYPE_Credit_213 = "CR213";
	/** Credit_214 = CR214 */
	public static final String DOCBASETYPE_Credit_214 = "CR214";
	/** Credit_217 = CR217 */
	public static final String DOCBASETYPE_Credit_217 = "CR217";
	/** Credit_221 = CR221 */
	public static final String DOCBASETYPE_Credit_221 = "CR221";
	/** Credit_222 = CR222 */
	public static final String DOCBASETYPE_Credit_222 = "CR222";
	/** Credit_228 = CR228 */
	public static final String DOCBASETYPE_Credit_228 = "CR228";
	/** Credit_229 = CR229 */
	public static final String DOCBASETYPE_Credit_229 = "CR229";
	/** Credit_241 = CR241 */
	public static final String DOCBASETYPE_Credit_241 = "CR241";
	/** 242 Credit = 242CR */
	public static final String DOCBASETYPE_242Credit = "242CR";
	/** Credit_243 = CR243 */
	public static final String DOCBASETYPE_Credit_243 = "CR243";
	/** Credit_244 = CR244 */
	public static final String DOCBASETYPE_Credit_244 = "CR244";
	/** Credit_331 = CR331 */
	public static final String DOCBASETYPE_Credit_331 = "CR331";
	/** Credit_333 = CR333 */
	public static final String DOCBASETYPE_Credit_333 = "CR333";
	/** Credit_334 = CR334 */
	public static final String DOCBASETYPE_Credit_334 = "CR334";
	/** Credit_335 = CR335 */
	public static final String DOCBASETYPE_Credit_335 = "CR335";
	/** Credit_336 = CR336 */
	public static final String DOCBASETYPE_Credit_336 = "CR336";
	/** Credit_337 = CR337 */
	public static final String DOCBASETYPE_Credit_337 = "CR337";
	/** Credit_338 = CR338 */
	public static final String DOCBASETYPE_Credit_338 = "CR338";
	/** Credit_341 = CR341 */
	public static final String DOCBASETYPE_Credit_341 = "CR341";
	/** Credit_343 = CR343 */
	public static final String DOCBASETYPE_Credit_343 = "CR343";
	/** Credit_344 = CR344 */
	public static final String DOCBASETYPE_Credit_344 = "CR344";
	/** Credit_347 = CR347 */
	public static final String DOCBASETYPE_Credit_347 = "CR347";
	/** Credit_352 = CR352 */
	public static final String DOCBASETYPE_Credit_352 = "CR352";
	/** Credit_353 = CR353 */
	public static final String DOCBASETYPE_Credit_353 = "CR353";
	/** Credit_356 = CR356 */
	public static final String DOCBASETYPE_Credit_356 = "CR356";
	/** Credit_357 = CR357 */
	public static final String DOCBASETYPE_Credit_357 = "CR357";
	/** Credit_411 = CR411 */
	public static final String DOCBASETYPE_Credit_411 = "CR411";
	/** Credit_412 = CR412 */
	public static final String DOCBASETYPE_Credit_412 = "CR412";
	/** Credit_413 = CR413 */
	public static final String DOCBASETYPE_Credit_413 = "CR413";
	/** Credit_414 = CR414 */
	public static final String DOCBASETYPE_Credit_414 = "CR414";
	/** Credit_417 = CR417 */
	public static final String DOCBASETYPE_Credit_417 = "CR417";
	/** Credit_418 = CR418 */
	public static final String DOCBASETYPE_Credit_418 = "CR418";
	/** Credit_419 = CR419 */
	public static final String DOCBASETYPE_Credit_419 = "CR419";
	/** Credit_421 = CR421 */
	public static final String DOCBASETYPE_Credit_421 = "CR421";
	/** Credit_441 = CR441 */
	public static final String DOCBASETYPE_Credit_441 = "CR441";
	/** Credit_461 = CR461 */
	public static final String DOCBASETYPE_Credit_461 = "CR461";
	/** Credit_466 = CR466 */
	public static final String DOCBASETYPE_Credit_466 = "CR466";
	/** Debit_511 = DR511 */
	public static final String DOCBASETYPE_Debit_511 = "DR511";
	/** Debit_515 = DR515 */
	public static final String DOCBASETYPE_Debit_515 = "DR515";
	/** Debit_521 = DR521 */
	public static final String DOCBASETYPE_Debit_521 = "DR521";
	/** Debit_611 = DR611 */
	public static final String DOCBASETYPE_Debit_611 = "DR611";
	/** 621 Debit = 621DR */
	public static final String DOCBASETYPE_621Debit = "621DR";
	/** 622 Debit = 622DR */
	public static final String DOCBASETYPE_622Debit = "622DR";
	/** Debit_623 = DR623 */
	public static final String DOCBASETYPE_Debit_623 = "DR623";
	/** 627 Debit = 627DR */
	public static final String DOCBASETYPE_627Debit = "627DR";
	/** Debit_631 = DR631 */
	public static final String DOCBASETYPE_Debit_631 = "DR631";
	/** Debit_632 = DR632 */
	public static final String DOCBASETYPE_Debit_632 = "DR632";
	/** Debit_635 = DR635 */
	public static final String DOCBASETYPE_Debit_635 = "DR635";
	/** Debit_641 = DR641 */
	public static final String DOCBASETYPE_Debit_641 = "DR641";
	/** Debit_642 = DR642 */
	public static final String DOCBASETYPE_Debit_642 = "DR642";
	/** Debit_711 = DR711 */
	public static final String DOCBASETYPE_Debit_711 = "DR711";
	/** Debit_811 = DR811 */
	public static final String DOCBASETYPE_Debit_811 = "DR811";
	/** Debit_821 = DR821 */
	public static final String DOCBASETYPE_Debit_821 = "DR821";
	/** 911 Debit = 911DR */
	public static final String DOCBASETYPE_911Debit = "911DR";
	/** Credit_511 = CR511 */
	public static final String DOCBASETYPE_Credit_511 = "CR511";
	/** Credit_515 = CR515 */
	public static final String DOCBASETYPE_Credit_515 = "CR515";
	/** Credit_521 = CR521 */
	public static final String DOCBASETYPE_Credit_521 = "CR521";
	/** Credit_611 = CR611 */
	public static final String DOCBASETYPE_Credit_611 = "CR611";
	/** Credit_621 = CR621 */
	public static final String DOCBASETYPE_Credit_621 = "CR621";
	/** Credit_622 = CR622 */
	public static final String DOCBASETYPE_Credit_622 = "CR622";
	/** Credit_623 = CR623 */
	public static final String DOCBASETYPE_Credit_623 = "CR623";
	/** Credit_627 = CR627 */
	public static final String DOCBASETYPE_Credit_627 = "CR627";
	/** Credit_631 = CR631 */
	public static final String DOCBASETYPE_Credit_631 = "CR631";
	/** Credit_632 = CR632 */
	public static final String DOCBASETYPE_Credit_632 = "CR632";
	/** Credit_635 = CR635 */
	public static final String DOCBASETYPE_Credit_635 = "CR635";
	/** Credit_641 = CR641 */
	public static final String DOCBASETYPE_Credit_641 = "CR641";
	/** Credit_642 = CR642 */
	public static final String DOCBASETYPE_Credit_642 = "CR642";
	/** Credit_711 = CR711 */
	public static final String DOCBASETYPE_Credit_711 = "CR711";
	/** Credit_811 = CR811 */
	public static final String DOCBASETYPE_Credit_811 = "CR811";
	/** Credit_821 = CR821 */
	public static final String DOCBASETYPE_Credit_821 = "CR821";
	/** Credit_911 = CR911 */
	public static final String DOCBASETYPE_Credit_911 = "CR911";
	/** Other = OTHER */
	public static final String DOCBASETYPE_Other = "OTHER";
	/** 211 Down = 211DO */
	public static final String DOCBASETYPE_211Down = "211DO";
	/** 211 Openbalance = 211OP */
	public static final String DOCBASETYPE_211Openbalance = "211OP";
	/** 211 Add New = 211NE */
	public static final String DOCBASETYPE_211AddNew = "211NE";
	/** 211 Deposal = 211DE */
	public static final String DOCBASETYPE_211Deposal = "211DE";
	/** 211 Transfer  = 211TR */
	public static final String DOCBASETYPE_211Transfer = "211TR";
	/** 211 OTher  = 211OT */
	public static final String DOCBASETYPE_211OTher = "211OT";
	/** 152 New = 152NE */
	public static final String DOCBASETYPE_152New = "152NE";
	/** 155 New = 155NE */
	public static final String DOCBASETYPE_155New = "155NE";
	/** 156 New = 156NE */
	public static final String DOCBASETYPE_156New = "156NE";
	/** 155 Return = 155RE */
	public static final String DOCBASETYPE_155Return = "155RE";
	/** 152 Return = 152RE */
	public static final String DOCBASETYPE_152Return = "152RE";
	/** 156 Return = 156RE */
	public static final String DOCBASETYPE_156Return = "156RE";
	/** 156 Return = 156RE */
	public static final String DOCBASETYPE_156Sell = "156SE";
	/** 15 Inventory miss = 15IVM */
	public static final String DOCBASETYPE_15InventoryMiss = "15IVM";
	/** 15 Inventory redundant = 15IVR */
	public static final String DOCBASETYPE_15InventoryRedundant = "15IVR";
	/** 133 Input = 133IN */
	public static final String DOCBASETYPE_133Input = "133IN";
	/** 333 Onput = 333ON */
	public static final String DOCBASETYPE_333Onput = "333ON";
	/** 153 New = 153NE */
	public static final String DOCBASETYPE_153New = "153NE";
	/** 153 Return = 153RE */
	public static final String DOCBASETYPE_153Return = "153RE";
	/** 242 New = 242NE */
	public static final String DOCBASETYPE_242New = "242NE";
	/** 242 Openbalance = 242OP */
	public static final String DOCBASETYPE_242Openbalance = "242OP";
	/** 153 Expense = 153EX */
	public static final String DOCBASETYPE_153Expense = "153EX";
	/** 153 ONE = 153ON */
	public static final String DOCBASETYPE_153ONE = "153ON";
	/** Set Document BaseType.
		@param DocBaseType 
		Logical type of document
	  */
	public void setDocBaseType (String DocBaseType)
	{

		set_Value (COLUMNNAME_DocBaseType, DocBaseType);
	}

	/** Get Document BaseType.
		@return Logical type of document
	  */
	public String getDocBaseType () 
	{
		return (String)get_Value(COLUMNNAME_DocBaseType);
	}

	/** Set DocNoFormat.
		@param DocNoFormat DocNoFormat	  */
	public void setDocNoFormat (String DocNoFormat)
	{
		set_Value (COLUMNNAME_DocNoFormat, DocNoFormat);
	}

	/** Get DocNoFormat.
		@return DocNoFormat	  */
	public String getDocNoFormat () 
	{
		return (String)get_Value(COLUMNNAME_DocNoFormat);
	}

	/** Input = INP */
	public static final String DOCTYPE_Input = "INP";
	/** Output = OUT */
	public static final String DOCTYPE_Output = "OUT";
	/** None = NON */
	public static final String DOCTYPE_None = "NON";
	/** Balance = BAL */
	public static final String DOCTYPE_Balance = "BAL";
	/** Set DocType.
		@param DocType DocType	  */
	public void setDocType (String DocType)
	{

		set_Value (COLUMNNAME_DocType, DocType);
	}

	/** Get DocType.
		@return DocType	  */
	public String getDocType () 
	{
		return (String)get_Value(COLUMNNAME_DocType);
	}

	/** Set IsConfirmPosting.
		@param IsConfirmPosting 
		IsConfirmPosting
	  */
	public void setIsConfirmPosting (boolean IsConfirmPosting)
	{
		set_Value (COLUMNNAME_IsConfirmPosting, Boolean.valueOf(IsConfirmPosting));
	}

	/** Get IsConfirmPosting.
		@return IsConfirmPosting
	  */
	public boolean isConfirmPosting () 
	{
		Object oo = get_Value(COLUMNNAME_IsConfirmPosting);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Default.
		@param IsDefault 
		Default value
	  */
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	public boolean isDefault () 
	{
		Object oo = get_Value(COLUMNNAME_IsDefault);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsShowSub.
		@param IsShowSub IsShowSub	  */
	public void setIsShowSub (boolean IsShowSub)
	{
		set_Value (COLUMNNAME_IsShowSub, Boolean.valueOf(IsShowSub));
	}

	/** Get IsShowSub.
		@return IsShowSub	  */
	public boolean isShowSub () 
	{
		Object oo = get_Value(COLUMNNAME_IsShowSub);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsShowTab.
		@param IsShowTab IsShowTab	  */
	public void setIsShowTab (boolean IsShowTab)
	{
		set_Value (COLUMNNAME_IsShowTab, Boolean.valueOf(IsShowTab));
	}

	/** Get IsShowTab.
		@return IsShowTab	  */
	public boolean isShowTab () 
	{
		Object oo = get_Value(COLUMNNAME_IsShowTab);
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
}
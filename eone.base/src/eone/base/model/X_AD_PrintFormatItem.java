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

/** Generated Model for AD_PrintFormatItem
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_PrintFormatItem extends PO implements I_AD_PrintFormatItem, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201021L;

    /** Standard Constructor */
    public X_AD_PrintFormatItem (Properties ctx, int AD_PrintFormatItem_ID, String trxName)
    {
      super (ctx, AD_PrintFormatItem_ID, trxName);
      /** if (AD_PrintFormatItem_ID == 0)
        {
			setAD_PrintFormat_ID (0);
			setAD_PrintFormatItem_ID (0);
			setFieldAlignmentType (null);
// D
			setIsAveraged (false);
			setIsCounted (false);
			setIsCountedGroup (false);
// N
			setIsMaxCalc (false);
			setIsMinCalc (false);
			setIsPrintBarcodeText (true);
// Y
			setIsPrinted (true);
// Y
			setIsSummarized (false);
			setMaxHeight (0);
			setMaxWidth (0);
			setName (null);
			setPrintAreaType (null);
// C
			setPrintFormatType (null);
// F
			setSeqNo (0);
// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_PrintFormatItem WHERE AD_PrintFormat_ID=@AD_PrintFormat_ID@
        } */
    }

    /** Load Constructor */
    public X_AD_PrintFormatItem (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AD_PrintFormatItem[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_AD_PrintColor getAD_PrintColor() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintColor)MTable.get(getCtx(), eone.base.model.I_AD_PrintColor.Table_Name)
			.getPO(getAD_PrintColor_ID(), get_TrxName());	}

	/** Set Print Color.
		@param AD_PrintColor_ID 
		Color used for printing and display
	  */
	public void setAD_PrintColor_ID (int AD_PrintColor_ID)
	{
		if (AD_PrintColor_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintColor_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintColor_ID, Integer.valueOf(AD_PrintColor_ID));
	}

	/** Get Print Color.
		@return Color used for printing and display
	  */
	public int getAD_PrintColor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintColor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_PrintFont getAD_PrintFont() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintFont)MTable.get(getCtx(), eone.base.model.I_AD_PrintFont.Table_Name)
			.getPO(getAD_PrintFont_ID(), get_TrxName());	}

	/** Set Print Font.
		@param AD_PrintFont_ID 
		Maintain Print Font
	  */
	public void setAD_PrintFont_ID (int AD_PrintFont_ID)
	{
		if (AD_PrintFont_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintFont_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintFont_ID, Integer.valueOf(AD_PrintFont_ID));
	}

	/** Get Print Font.
		@return Maintain Print Font
	  */
	public int getAD_PrintFont_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFont_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_PrintFormat getAD_PrintFormat() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintFormat)MTable.get(getCtx(), eone.base.model.I_AD_PrintFormat.Table_Name)
			.getPO(getAD_PrintFormat_ID(), get_TrxName());	}

	/** Set Print Format.
		@param AD_PrintFormat_ID 
		Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
	{
		if (AD_PrintFormat_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PrintFormat_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PrintFormat_ID, Integer.valueOf(AD_PrintFormat_ID));
	}

	/** Get Print Format.
		@return Data Print Format
	  */
	public int getAD_PrintFormat_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFormat_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Print Format Item.
		@param AD_PrintFormatItem_ID 
		Item/Column in the Print format
	  */
	public void setAD_PrintFormatItem_ID (int AD_PrintFormatItem_ID)
	{
		if (AD_PrintFormatItem_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PrintFormatItem_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PrintFormatItem_ID, Integer.valueOf(AD_PrintFormatItem_ID));
	}

	/** Get Print Format Item.
		@return Item/Column in the Print format
	  */
	public int getAD_PrintFormatItem_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFormatItem_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_Reference getAD_Reference() throws RuntimeException
    {
		return (eone.base.model.I_AD_Reference)MTable.get(getCtx(), eone.base.model.I_AD_Reference.Table_Name)
			.getPO(getAD_Reference_ID(), get_TrxName());	}

	/** Set Reference.
		@param AD_Reference_ID 
		System Reference and Validation
	  */
	public void setAD_Reference_ID (int AD_Reference_ID)
	{
		if (AD_Reference_ID < 1) 
			set_Value (COLUMNNAME_AD_Reference_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Reference_ID, Integer.valueOf(AD_Reference_ID));
	}

	/** Get Reference.
		@return System Reference and Validation
	  */
	public int getAD_Reference_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Reference_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** BarcodeType AD_Reference_ID=377 */
	public static final int BARCODETYPE_AD_Reference_ID=377;
	/** Codabar 2 of 7 linear = 2o9 */
	public static final String BARCODETYPE_Codabar2Of7Linear = "2o9";
	/** Code 39  3 of 9 linear w/o Checksum = 3o9 */
	public static final String BARCODETYPE_Code393Of9LinearWOChecksum = "3o9";
	/** Codeabar linear = COD */
	public static final String BARCODETYPE_CodeabarLinear = "COD";
	/** Code 128 dynamically switching = C28 */
	public static final String BARCODETYPE_Code128DynamicallySwitching = "C28";
	/** Code 128 A character set = 28A */
	public static final String BARCODETYPE_Code128ACharacterSet = "28A";
	/** Code 128 B character set = 28B */
	public static final String BARCODETYPE_Code128BCharacterSet = "28B";
	/** Code 128 C character set = 28C */
	public static final String BARCODETYPE_Code128CCharacterSet = "28C";
	/** Code 39 linear with Checksum = C39 */
	public static final String BARCODETYPE_Code39LinearWithChecksum = "C39";
	/** EAN 128 = E28 */
	public static final String BARCODETYPE_EAN128 = "E28";
	/** Global Trade Item No GTIN UCC/EAN 128 = GTN */
	public static final String BARCODETYPE_GlobalTradeItemNoGTINUCCEAN128 = "GTN";
	/** Codabar Monarch linear = MON */
	public static final String BARCODETYPE_CodabarMonarchLinear = "MON";
	/** Codabar NW-7 linear = NW7 */
	public static final String BARCODETYPE_CodabarNW_7Linear = "NW7";
	/** PDF417 two dimensional = 417 */
	public static final String BARCODETYPE_PDF417TwoDimensional = "417";
	/** SCC-14 shipping code UCC/EAN 128 = C14 */
	public static final String BARCODETYPE_SCC_14ShippingCodeUCCEAN128 = "C14";
	/** Shipment ID number UCC/EAN 128 = SID */
	public static final String BARCODETYPE_ShipmentIDNumberUCCEAN128 = "SID";
	/** UCC 128 = U28 */
	public static final String BARCODETYPE_UCC128 = "U28";
	/** Code 39 USD3 with Checksum = US3 */
	public static final String BARCODETYPE_Code39USD3WithChecksum = "US3";
	/** Codabar USD-4 linear = US4 */
	public static final String BARCODETYPE_CodabarUSD_4Linear = "US4";
	/** US Postal Service UCC/EAN 128 = USP */
	public static final String BARCODETYPE_USPostalServiceUCCEAN128 = "USP";
	/** SSCC-18 number UCC/EAN 128 = C18 */
	public static final String BARCODETYPE_SSCC_18NumberUCCEAN128 = "C18";
	/** Code 39 USD3 w/o Checksum = us3 */
	public static final String BARCODETYPE_Code39USD3WOChecksum = "us3";
	/** Code 39  3 of 9 linear with Checksum = 3O9 */
	public static final String BARCODETYPE_Code393Of9LinearWithChecksum = "3O9";
	/** Code 39 linear w/o Checksum = c39 */
	public static final String BARCODETYPE_Code39LinearWOChecksum = "c39";
	/** EAN 13 = E13 */
	public static final String BARCODETYPE_EAN13 = "E13";
	/** UPC-A = UPA */
	public static final String BARCODETYPE_UPC_A = "UPA";
	/** Code 39 with Checksum = 39C */
	public static final String BARCODETYPE_Code39WithChecksum = "39C";
	/** Code 39 w/o Checksum = 39c */
	public static final String BARCODETYPE_Code39WOChecksum = "39c";
	/** QR Code = QRC */
	public static final String BARCODETYPE_QRCode = "QRC";
	/** Set Barcode Type.
		@param BarcodeType 
		Type of barcode
	  */
	public void setBarcodeType (String BarcodeType)
	{

		set_Value (COLUMNNAME_BarcodeType, BarcodeType);
	}

	/** Get Barcode Type.
		@return Type of barcode
	  */
	public String getBarcodeType () 
	{
		return (String)get_Value(COLUMNNAME_BarcodeType);
	}

	/** Set Column Span.
		@param ColumnSpan 
		Number of column for a box of field
	  */
	public void setColumnSpan (int ColumnSpan)
	{
		set_Value (COLUMNNAME_ColumnSpan, Integer.valueOf(ColumnSpan));
	}

	/** Get Column Span.
		@return Number of column for a box of field
	  */
	public int getColumnSpan () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ColumnSpan);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** FieldAlignmentType AD_Reference_ID=253 */
	public static final int FIELDALIGNMENTTYPE_AD_Reference_ID=253;
	/** Default = D */
	public static final String FIELDALIGNMENTTYPE_Default = "D";
	/** Leading (left) = L */
	public static final String FIELDALIGNMENTTYPE_LeadingLeft = "L";
	/** Trailing (right) = T */
	public static final String FIELDALIGNMENTTYPE_TrailingRight = "T";
	/** Block = B */
	public static final String FIELDALIGNMENTTYPE_Block = "B";
	/** Center = C */
	public static final String FIELDALIGNMENTTYPE_Center = "C";
	/** Set Field Alignment.
		@param FieldAlignmentType 
		Field Text Alignment
	  */
	public void setFieldAlignmentType (String FieldAlignmentType)
	{

		set_Value (COLUMNNAME_FieldAlignmentType, FieldAlignmentType);
	}

	/** Get Field Alignment.
		@return Field Text Alignment
	  */
	public String getFieldAlignmentType () 
	{
		return (String)get_Value(COLUMNNAME_FieldAlignmentType);
	}

	/** Set Format Pattern.
		@param FormatPattern 
		The pattern used to format a number or date.
	  */
	public void setFormatPattern (String FormatPattern)
	{
		set_Value (COLUMNNAME_FormatPattern, FormatPattern);
	}

	/** Get Format Pattern.
		@return The pattern used to format a number or date.
	  */
	public String getFormatPattern () 
	{
		return (String)get_Value(COLUMNNAME_FormatPattern);
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

	/** Set Calculate Accumulate.
		@param IsAccumulateCal Calculate Accumulate	  */
	public void setIsAccumulateCal (boolean IsAccumulateCal)
	{
		set_Value (COLUMNNAME_IsAccumulateCal, Boolean.valueOf(IsAccumulateCal));
	}

	/** Get Calculate Accumulate.
		@return Calculate Accumulate	  */
	public boolean isAccumulateCal () 
	{
		Object oo = get_Value(COLUMNNAME_IsAccumulateCal);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Calculate Mean (μ).
		@param IsAveraged 
		Calculate Average of numeric content or length
	  */
	public void setIsAveraged (boolean IsAveraged)
	{
		set_Value (COLUMNNAME_IsAveraged, Boolean.valueOf(IsAveraged));
	}

	/** Get Calculate Mean (μ).
		@return Calculate Average of numeric content or length
	  */
	public boolean isAveraged () 
	{
		Object oo = get_Value(COLUMNNAME_IsAveraged);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Balance Final.
		@param IsBalanceFinal Balance Final	  */
	public void setIsBalanceFinal (boolean IsBalanceFinal)
	{
		set_Value (COLUMNNAME_IsBalanceFinal, Boolean.valueOf(IsBalanceFinal));
	}

	/** Get Balance Final.
		@return Balance Final	  */
	public boolean isBalanceFinal () 
	{
		Object oo = get_Value(COLUMNNAME_IsBalanceFinal);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Calculate Count (№).
		@param IsCounted 
		Count number of not empty elements
	  */
	public void setIsCounted (boolean IsCounted)
	{
		set_Value (COLUMNNAME_IsCounted, Boolean.valueOf(IsCounted));
	}

	/** Get Calculate Count (№).
		@return Count number of not empty elements
	  */
	public boolean isCounted () 
	{
		Object oo = get_Value(COLUMNNAME_IsCounted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Counted Group.
		@param IsCountedGroup 
		Counted Group
	  */
	public void setIsCountedGroup (boolean IsCountedGroup)
	{
		set_Value (COLUMNNAME_IsCountedGroup, Boolean.valueOf(IsCountedGroup));
	}

	/** Get Counted Group.
		@return Counted Group
	  */
	public boolean isCountedGroup () 
	{
		Object oo = get_Value(COLUMNNAME_IsCountedGroup);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Group by.
		@param IsGroupBy 
		After a group change, totals, etc. are printed
	  */
	public void setIsGroupBy (boolean IsGroupBy)
	{
		set_Value (COLUMNNAME_IsGroupBy, Boolean.valueOf(IsGroupBy));
	}

	/** Get Group by.
		@return After a group change, totals, etc. are printed
	  */
	public boolean isGroupBy () 
	{
		Object oo = get_Value(COLUMNNAME_IsGroupBy);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsMapColumnSelectSQL.
		@param IsMapColumnSelectSQL IsMapColumnSelectSQL	  */
	public void setIsMapColumnSelectSQL (boolean IsMapColumnSelectSQL)
	{
		set_Value (COLUMNNAME_IsMapColumnSelectSQL, Boolean.valueOf(IsMapColumnSelectSQL));
	}

	/** Get IsMapColumnSelectSQL.
		@return IsMapColumnSelectSQL	  */
	public boolean isMapColumnSelectSQL () 
	{
		Object oo = get_Value(COLUMNNAME_IsMapColumnSelectSQL);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Calculate Maximum (↑).
		@param IsMaxCalc 
		Calculate the maximum amount
	  */
	public void setIsMaxCalc (boolean IsMaxCalc)
	{
		set_Value (COLUMNNAME_IsMaxCalc, Boolean.valueOf(IsMaxCalc));
	}

	/** Get Calculate Maximum (↑).
		@return Calculate the maximum amount
	  */
	public boolean isMaxCalc () 
	{
		Object oo = get_Value(COLUMNNAME_IsMaxCalc);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Calculate Minimum (↓).
		@param IsMinCalc 
		Calculate the minimum amount
	  */
	public void setIsMinCalc (boolean IsMinCalc)
	{
		set_Value (COLUMNNAME_IsMinCalc, Boolean.valueOf(IsMinCalc));
	}

	/** Get Calculate Minimum (↓).
		@return Calculate the minimum amount
	  */
	public boolean isMinCalc () 
	{
		Object oo = get_Value(COLUMNNAME_IsMinCalc);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Print Barcode Text.
		@param IsPrintBarcodeText 
		Print barcode text at the bottom of barcode
	  */
	public void setIsPrintBarcodeText (boolean IsPrintBarcodeText)
	{
		set_Value (COLUMNNAME_IsPrintBarcodeText, Boolean.valueOf(IsPrintBarcodeText));
	}

	/** Get Print Barcode Text.
		@return Print barcode text at the bottom of barcode
	  */
	public boolean isPrintBarcodeText () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrintBarcodeText);
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

	/** Set Calculate Sum (Σ).
		@param IsSummarized 
		Calculate the Sum of numeric content or length
	  */
	public void setIsSummarized (boolean IsSummarized)
	{
		set_Value (COLUMNNAME_IsSummarized, Boolean.valueOf(IsSummarized));
	}

	/** Get Calculate Sum (Σ).
		@return Calculate the Sum of numeric content or length
	  */
	public boolean isSummarized () 
	{
		Object oo = get_Value(COLUMNNAME_IsSummarized);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsZoom.
		@param IsZoom IsZoom	  */
	public void setIsZoom (boolean IsZoom)
	{
		set_Value (COLUMNNAME_IsZoom, Boolean.valueOf(IsZoom));
	}

	/** Get IsZoom.
		@return IsZoom	  */
	public boolean isZoom () 
	{
		Object oo = get_Value(COLUMNNAME_IsZoom);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Max Height.
		@param MaxHeight 
		Maximum Height in 1/72 if an inch - 0 = no restriction
	  */
	public void setMaxHeight (int MaxHeight)
	{
		set_Value (COLUMNNAME_MaxHeight, Integer.valueOf(MaxHeight));
	}

	/** Get Max Height.
		@return Maximum Height in 1/72 if an inch - 0 = no restriction
	  */
	public int getMaxHeight () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MaxHeight);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Max Width.
		@param MaxWidth 
		Maximum Width in 1/72 if an inch - 0 = no restriction
	  */
	public void setMaxWidth (int MaxWidth)
	{
		set_Value (COLUMNNAME_MaxWidth, Integer.valueOf(MaxWidth));
	}

	/** Get Max Width.
		@return Maximum Width in 1/72 if an inch - 0 = no restriction
	  */
	public int getMaxWidth () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MaxWidth);
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

	/** Set Number of Lines.
		@param NumLines 
		Number of lines for a field
	  */
	public void setNumLines (int NumLines)
	{
		set_Value (COLUMNNAME_NumLines, Integer.valueOf(NumLines));
	}

	/** Get Number of Lines.
		@return Number of lines for a field
	  */
	public int getNumLines () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NumLines);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Row 1 = 1 */
	public static final String ORDERROWHEADER_Row1 = "1";
	/** Row 2 = 2 */
	public static final String ORDERROWHEADER_Row2 = "2";
	/** Row 3 = 3 */
	public static final String ORDERROWHEADER_Row3 = "3";
	/** Row 4 = 4 */
	public static final String ORDERROWHEADER_Row4 = "4";
	/** Set OrderRowHeader.
		@param OrderRowHeader OrderRowHeader	  */
	public void setOrderRowHeader (String OrderRowHeader)
	{

		set_Value (COLUMNNAME_OrderRowHeader, OrderRowHeader);
	}

	/** Get OrderRowHeader.
		@return OrderRowHeader	  */
	public String getOrderRowHeader () 
	{
		return (String)get_Value(COLUMNNAME_OrderRowHeader);
	}

	/** PrintAreaType AD_Reference_ID=256 */
	public static final int PRINTAREATYPE_AD_Reference_ID=256;
	/** Content = C */
	public static final String PRINTAREATYPE_Content = "C";
	/** Header = H */
	public static final String PRINTAREATYPE_Header = "H";
	/** Footer = F */
	public static final String PRINTAREATYPE_Footer = "F";
	/** Set Area.
		@param PrintAreaType 
		Print Area
	  */
	public void setPrintAreaType (String PrintAreaType)
	{

		set_Value (COLUMNNAME_PrintAreaType, PrintAreaType);
	}

	/** Get Area.
		@return Print Area
	  */
	public String getPrintAreaType () 
	{
		return (String)get_Value(COLUMNNAME_PrintAreaType);
	}

	/** PrintFormatType AD_Reference_ID=255 */
	public static final int PRINTFORMATTYPE_AD_Reference_ID=255;
	/** Text = T */
	public static final String PRINTFORMATTYPE_Text = "T";
	/** Image = I */
	public static final String PRINTFORMATTYPE_Image = "I";
	/** Set Format Type.
		@param PrintFormatType 
		Print Format Type
	  */
	public void setPrintFormatType (String PrintFormatType)
	{

		set_Value (COLUMNNAME_PrintFormatType, PrintFormatType);
	}

	/** Get Format Type.
		@return Print Format Type
	  */
	public String getPrintFormatType () 
	{
		return (String)get_Value(COLUMNNAME_PrintFormatType);
	}

	/** Set Print Name.
		@param PrintName 
		The label text to be printed on a document or correspondence.
	  */
	public void setPrintName (String PrintName)
	{
		set_Value (COLUMNNAME_PrintName, PrintName);
	}

	/** Get Print Name.
		@return The label text to be printed on a document or correspondence.
	  */
	public String getPrintName () 
	{
		return (String)get_Value(COLUMNNAME_PrintName);
	}

	/** Set Rotation Text.
		@param RotationText Rotation Text	  */
	public void setRotationText (int RotationText)
	{
		set_Value (COLUMNNAME_RotationText, Integer.valueOf(RotationText));
	}

	/** Get Rotation Text.
		@return Rotation Text	  */
	public int getRotationText () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RotationText);
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

	/** Set DB Table Name.
		@param TableName 
		Name of the table in the database
	  */
	public void setTableName (String TableName)
	{
		set_Value (COLUMNNAME_TableName, TableName);
	}

	/** Get DB Table Name.
		@return Name of the table in the database
	  */
	public String getTableName () 
	{
		return (String)get_Value(COLUMNNAME_TableName);
	}

	/** Set Zoom Logic.
		@param ZoomLogic 
		the result determines if the zoom condition is applied
	  */
	public void setZoomLogic (String ZoomLogic)
	{
		set_Value (COLUMNNAME_ZoomLogic, ZoomLogic);
	}

	/** Get Zoom Logic.
		@return the result determines if the zoom condition is applied
	  */
	public String getZoomLogic () 
	{
		return (String)get_Value(COLUMNNAME_ZoomLogic);
	}
}
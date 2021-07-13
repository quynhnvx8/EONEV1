/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_PrintFormatItem
 *  @author EOne (generated) 
 *  @version Version 1.0
 */
public interface I_AD_PrintFormatItem 
{

    /** TableName=AD_PrintFormatItem */
    public static final String Table_Name = "AD_PrintFormatItem";

    /** AD_Table_ID=489 */
    public static final int Table_ID = 489;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_PrintColor_ID */
    public static final String COLUMNNAME_AD_PrintColor_ID = "AD_PrintColor_ID";

	/** Set Print Color.
	  * Color used for printing and display
	  */
	public void setAD_PrintColor_ID (int AD_PrintColor_ID);

	/** Get Print Color.
	  * Color used for printing and display
	  */
	public int getAD_PrintColor_ID();

	public eone.base.model.I_AD_PrintColor getAD_PrintColor() throws RuntimeException;

    /** Column name AD_PrintFont_ID */
    public static final String COLUMNNAME_AD_PrintFont_ID = "AD_PrintFont_ID";

	/** Set Print Font.
	  * Maintain Print Font
	  */
	public void setAD_PrintFont_ID (int AD_PrintFont_ID);

	/** Get Print Font.
	  * Maintain Print Font
	  */
	public int getAD_PrintFont_ID();

	public eone.base.model.I_AD_PrintFont getAD_PrintFont() throws RuntimeException;

    /** Column name AD_PrintFormat_ID */
    public static final String COLUMNNAME_AD_PrintFormat_ID = "AD_PrintFormat_ID";

	/** Set Print Format.
	  * Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID);

	/** Get Print Format.
	  * Data Print Format
	  */
	public int getAD_PrintFormat_ID();

	public eone.base.model.I_AD_PrintFormat getAD_PrintFormat() throws RuntimeException;

    /** Column name AD_PrintFormatItem_ID */
    public static final String COLUMNNAME_AD_PrintFormatItem_ID = "AD_PrintFormatItem_ID";

	/** Set Print Format Item.
	  * Item/Column in the Print format
	  */
	public void setAD_PrintFormatItem_ID (int AD_PrintFormatItem_ID);

	/** Get Print Format Item.
	  * Item/Column in the Print format
	  */
	public int getAD_PrintFormatItem_ID();

    /** Column name AD_Reference_ID */
    public static final String COLUMNNAME_AD_Reference_ID = "AD_Reference_ID";

	/** Set Reference.
	  * System Reference and Validation
	  */
	public void setAD_Reference_ID (int AD_Reference_ID);

	/** Get Reference.
	  * System Reference and Validation
	  */
	public int getAD_Reference_ID();

	public eone.base.model.I_AD_Reference getAD_Reference() throws RuntimeException;

    /** Column name BarcodeType */
    public static final String COLUMNNAME_BarcodeType = "BarcodeType";

	/** Set Barcode Type.
	  * Type of barcode
	  */
	public void setBarcodeType (String BarcodeType);

	/** Get Barcode Type.
	  * Type of barcode
	  */
	public String getBarcodeType();

    /** Column name ColumnSpan */
    public static final String COLUMNNAME_ColumnSpan = "ColumnSpan";

	/** Set Column Span.
	  * Number of column for a box of field
	  */
	public void setColumnSpan (int ColumnSpan);

	/** Get Column Span.
	  * Number of column for a box of field
	  */
	public int getColumnSpan();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name FieldAlignmentType */
    public static final String COLUMNNAME_FieldAlignmentType = "FieldAlignmentType";

	/** Set Field Alignment.
	  * Field Text Alignment
	  */
	public void setFieldAlignmentType (String FieldAlignmentType);

	/** Get Field Alignment.
	  * Field Text Alignment
	  */
	public String getFieldAlignmentType();

    /** Column name FormatPattern */
    public static final String COLUMNNAME_FormatPattern = "FormatPattern";

	/** Set Format Pattern.
	  * The pattern used to format a number or date.
	  */
	public void setFormatPattern (String FormatPattern);

	/** Get Format Pattern.
	  * The pattern used to format a number or date.
	  */
	public String getFormatPattern();

    /** Column name FormulaSetup */
    public static final String COLUMNNAME_FormulaSetup = "FormulaSetup";

	/** Set FormulaSetup	  */
	public void setFormulaSetup (String FormulaSetup);

	/** Get FormulaSetup	  */
	public String getFormulaSetup();
	
	public static final String COLUMNNAME_FieldSumGroup = "FieldSumGroup";

	/** Set FormulaSetup	  */
	public void setFieldSumGroup (String FieldSumGroup);

	/** Get FormulaSetup	  */
	public String getFieldSumGroup();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsCountedGroup */
    public static final String COLUMNNAME_IsCountedGroup = "IsCountedGroup";

	/** Set Counted Group.
	  * Counted Group
	  */
	public void setIsCountedGroup (boolean IsCountedGroup);

	/** Get Counted Group.
	  * Counted Group
	  */
	public boolean isCountedGroup();

    /** Column name IsGroupBy */
    public static final String COLUMNNAME_IsGroupBy = "IsGroupBy";

	/** Set Group by.
	  * After a group change, totals, etc. are printed
	  */
	public void setIsGroupBy (boolean IsGroupBy);

	/** Get Group by.
	  * After a group change, totals, etc. are printed
	  */
	public boolean isGroupBy();

    /** Column name IsMapColumnSelectSQL */
    public static final String COLUMNNAME_IsMapColumnSelectSQL = "IsMapColumnSelectSQL";

	/** Set IsMapColumnSelectSQL	  */
	public void setIsMapColumnSelectSQL (boolean IsMapColumnSelectSQL);

	/** Get IsMapColumnSelectSQL	  */
	public boolean isMapColumnSelectSQL();

    /** Column name IsPrintBarcodeText */
    public static final String COLUMNNAME_IsPrintBarcodeText = "IsPrintBarcodeText";

	/** Set Print Barcode Text.
	  * Print barcode text at the bottom of barcode
	  */
	public void setIsPrintBarcodeText (boolean IsPrintBarcodeText);

	/** Get Print Barcode Text.
	  * Print barcode text at the bottom of barcode
	  */
	public boolean isPrintBarcodeText();

    /** Column name IsPrinted */
    public static final String COLUMNNAME_IsPrinted = "IsPrinted";

	/** Set Printed.
	  * Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted);

	/** Get Printed.
	  * Indicates if this document / line is printed
	  */
	public boolean isPrinted();

    /** Column name IsZoom */
    public static final String COLUMNNAME_IsZoom = "IsZoom";

	/** Set IsZoom	  */
	public void setIsZoom (boolean IsZoom);

	/** Get IsZoom	  */
	public boolean isZoom();

    /** Column name MaxHeight */
    public static final String COLUMNNAME_MaxHeight = "MaxHeight";

	/** Set Max Height.
	  * Maximum Height in 1/72 if an inch - 0 = no restriction
	  */
	public void setMaxHeight (int MaxHeight);

	/** Get Max Height.
	  * Maximum Height in 1/72 if an inch - 0 = no restriction
	  */
	public int getMaxHeight();

    /** Column name MaxWidth */
    public static final String COLUMNNAME_MaxWidth = "MaxWidth";

	/** Set Max Width.
	  * Maximum Width in 1/72 if an inch - 0 = no restriction
	  */
	public void setMaxWidth (int MaxWidth);

	/** Get Max Width.
	  * Maximum Width in 1/72 if an inch - 0 = no restriction
	  */
	public int getMaxWidth();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name NumLines */
    public static final String COLUMNNAME_NumLines = "NumLines";

	/** Set Number of Lines.
	  * Number of lines for a field
	  */
	public void setNumLines (int NumLines);

	/** Get Number of Lines.
	  * Number of lines for a field
	  */
	public int getNumLines();

    /** Column name OrderRowHeader */
    public static final String COLUMNNAME_OrderRowHeader = "OrderRowHeader";

	/** Set OrderRowHeader	  */
	public void setOrderRowHeader (String OrderRowHeader);

	/** Get OrderRowHeader	  */
	public String getOrderRowHeader();

    /** Column name PrintAreaType */
    public static final String COLUMNNAME_PrintAreaType = "PrintAreaType";

	/** Set Area.
	  * Print Area
	  */
	public void setPrintAreaType (String PrintAreaType);

	/** Get Area.
	  * Print Area
	  */
	public String getPrintAreaType();

    /** Column name PrintFormatType */
    public static final String COLUMNNAME_PrintFormatType = "PrintFormatType";

	/** Set Format Type.
	  * Print Format Type
	  */
	public void setPrintFormatType (String PrintFormatType);

	/** Get Format Type.
	  * Print Format Type
	  */
	public String getPrintFormatType();

    /** Column name PrintName */
    public static final String COLUMNNAME_PrintName = "PrintName";

	/** Set Print Name.
	  * The label text to be printed on a document or correspondence.
	  */
	public void setPrintName (String PrintName);

	/** Get Print Name.
	  * The label text to be printed on a document or correspondence.
	  */
	public String getPrintName();

    /** Column name RotationText */
    public static final String COLUMNNAME_RotationText = "RotationText";

	/** Set Rotation Text	  */
	public void setRotationText (int RotationText);

	/** Get Rotation Text	  */
	public int getRotationText();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

    /** Column name TableName */
    public static final String COLUMNNAME_TableName = "TableName";

	/** Set DB Table Name.
	  * Name of the table in the database
	  */
	public void setTableName (String TableName);

	/** Get DB Table Name.
	  * Name of the table in the database
	  */
	public String getTableName();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name ZoomLogic */
    public static final String COLUMNNAME_ZoomLogic = "ZoomLogic";

	/** Set Zoom Logic.
	  * the result determines if the zoom condition is applied
	  */
	public void setZoomLogic (String ZoomLogic);

	/** Get Zoom Logic.
	  * the result determines if the zoom condition is applied
	  */
	public String getZoomLogic();
}

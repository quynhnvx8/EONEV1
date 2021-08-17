/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_PrintFormat
 *  @author EOne (generated) 
 *  @version Version 1.0
 */
public interface I_AD_PrintFormat 
{

    /** TableName=AD_PrintFormat */
    public static final String Table_Name = "AD_PrintFormat";

    /** AD_Table_ID=493 */
    public static final int Table_ID = 493;

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

    /** Column name AD_PrintPaper_ID */
    public static final String COLUMNNAME_AD_PrintPaper_ID = "AD_PrintPaper_ID";

	/** Set Print Paper.
	  * Printer paper definition
	  */
	public void setAD_PrintPaper_ID (int AD_PrintPaper_ID);

	/** Get Print Paper.
	  * Printer paper definition
	  */
	public int getAD_PrintPaper_ID();

	public eone.base.model.I_AD_PrintPaper getAD_PrintPaper() throws RuntimeException;

    /** Column name AxisX */
    public static final String COLUMNNAME_AxisX = "AxisX";

	/** Set AxisX	  */
	public void setAxisX (String AxisX);

	/** Get AxisX	  */
	public String getAxisX();

    /** Column name AxisY */
    public static final String COLUMNNAME_AxisY = "AxisY";

	/** Set AxisY	  */
	public void setAxisY (String AxisY);

	/** Get AxisY	  */
	public String getAxisY();

    /** Column name ChartTitle */
    public static final String COLUMNNAME_ChartTitle = "ChartTitle";

	/** Set ChartTitle	  */
	public void setChartTitle (String ChartTitle);

	/** Get ChartTitle	  */
	public String getChartTitle();

    /** Column name ChartType */
    public static final String COLUMNNAME_ChartType = "ChartType";

	/** Set Chart Type.
	  * Type of chart to render
	  */
	public void setChartType (String ChartType);

	/** Get Chart Type.
	  * Type of chart to render
	  */
	public String getChartType();

    /** Column name CreateCopy */
    public static final String COLUMNNAME_CreateCopy = "CreateCopy";

	/** Set Create Copy	  */
	public void setCreateCopy (String CreateCopy);

	/** Get Create Copy	  */
	public String getCreateCopy();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name FooterMargin */
    public static final String COLUMNNAME_FooterMargin = "FooterMargin";

	/** Set Footer Margin.
	  * Margin of the Footer in 1/72 of an inch
	  */
	public void setFooterMargin (int FooterMargin);

	/** Get Footer Margin.
	  * Margin of the Footer in 1/72 of an inch
	  */
	public int getFooterMargin();

    /** Column name HeaderMargin */
    public static final String COLUMNNAME_HeaderMargin = "HeaderMargin";

	/** Set Header Margin.
	  * Margin of the Header in 1/72 of an inch
	  */
	public void setHeaderMargin (int HeaderMargin);

	/** Get Header Margin.
	  * Margin of the Header in 1/72 of an inch
	  */
	public int getHeaderMargin();

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

    /** Column name IsShowChart */
    public static final String COLUMNNAME_IsShowChart = "IsShowChart";

	/** Set Show Chart	  */
	public void setIsShowChart (boolean IsShowChart);

	/** Get Show Chart	  */
	public boolean isShowChart();

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
}

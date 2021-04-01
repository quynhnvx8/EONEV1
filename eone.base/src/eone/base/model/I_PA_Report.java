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
package eone.base.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for PA_Report
 *  @author iDempiere (generated) 
 *  @version Version 1.0
 */
public interface I_PA_Report 
{

    /** TableName=PA_Report */
    public static final String Table_Name = "PA_Report";

    /** AD_Table_ID=445 */
    public static final int Table_ID = 445;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

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

    /** Column name IsDetailBPartner */
    public static final String COLUMNNAME_IsDetailBPartner = "IsDetailBPartner";

	/** Set Manage Business Partners	  */
	public void setIsDetailBPartner (boolean IsDetailBPartner);

	/** Get Manage Business Partners	  */
	public boolean isDetailBPartner();

    /** Column name IsDetailCashFlow */
    public static final String COLUMNNAME_IsDetailCashFlow = "IsDetailCashFlow";

	/** Set IsDetailCashFlow	  */
	public void setIsDetailCashFlow (boolean IsDetailCashFlow);

	/** Get IsDetailCashFlow	  */
	public boolean isDetailCashFlow();

    /** Column name IsDetailContract */
    public static final String COLUMNNAME_IsDetailContract = "IsDetailContract";

	/** Set IsDetailContract	  */
	public void setIsDetailContract (boolean IsDetailContract);

	/** Get IsDetailContract	  */
	public boolean isDetailContract();

    /** Column name IsDetailProduct */
    public static final String COLUMNNAME_IsDetailProduct = "IsDetailProduct";

	/** Set Manage Products	  */
	public void setIsDetailProduct (boolean IsDetailProduct);

	/** Get Manage Products	  */
	public boolean isDetailProduct();

    /** Column name IsDetailReportLine */
    public static final String COLUMNNAME_IsDetailReportLine = "IsDetailReportLine";

	/** Set IsDetailReportLine	  */
	public void setIsDetailReportLine (boolean IsDetailReportLine);

	/** Get IsDetailReportLine	  */
	public boolean isDetailReportLine();

    /** Column name IsDetailTypeCost */
    public static final String COLUMNNAME_IsDetailTypeCost = "IsDetailTypeCost";

	/** Set IsDetailTypeCost	  */
	public void setIsDetailTypeCost (boolean IsDetailTypeCost);

	/** Get IsDetailTypeCost	  */
	public boolean isDetailTypeCost();

    /** Column name IsDetailTypeRevenue */
    public static final String COLUMNNAME_IsDetailTypeRevenue = "IsDetailTypeRevenue";

	/** Set IsDetailTypeRevenue	  */
	public void setIsDetailTypeRevenue (boolean IsDetailTypeRevenue);

	/** Get IsDetailTypeRevenue	  */
	public boolean isDetailTypeRevenue();

    /** Column name JasperProcess_ID */
    public static final String COLUMNNAME_JasperProcess_ID = "JasperProcess_ID";

	/** Set Jasper Process.
	  * The Jasper Process used by the printengine if any process defined
	  */
	public void setJasperProcess_ID (int JasperProcess_ID);

	/** Get Jasper Process.
	  * The Jasper Process used by the printengine if any process defined
	  */
	public int getJasperProcess_ID();

	public eone.base.model.I_AD_Process getJasperProcess() throws RuntimeException;

    /** Column name JasperProcessing */
    public static final String COLUMNNAME_JasperProcessing = "JasperProcessing";

	/** Set Jasper Process Now	  */
	public void setJasperProcessing (String JasperProcessing);

	/** Get Jasper Process Now	  */
	public String getJasperProcessing();

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

    /** Column name PA_Report_ID */
    public static final String COLUMNNAME_PA_Report_ID = "PA_Report_ID";

	/** Set Financial Report.
	  * Financial Report
	  */
	public void setPA_Report_ID (int PA_Report_ID);

	/** Get Financial Report.
	  * Financial Report
	  */
	public int getPA_Report_ID();

    /** Column name PA_Report_UU */
    public static final String COLUMNNAME_PA_Report_UU = "PA_Report_UU";

	/** Set PA_Report_UU	  */
	public void setPA_Report_UU (String PA_Report_UU);

	/** Get PA_Report_UU	  */
	public String getPA_Report_UU();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

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

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

/** Generated Model for PA_Report
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_PA_Report extends PO implements I_PA_Report, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200807L;

    /** Standard Constructor */
    public X_PA_Report (Properties ctx, int PA_Report_ID, String trxName)
    {
      super (ctx, PA_Report_ID, trxName);
      /** if (PA_Report_ID == 0)
        {
			setName (null);
			setPA_Report_ID (0);
			setProcessing (false);
        } */
    }

    /** Load Constructor */
    public X_PA_Report (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_PA_Report[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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
			set_Value (COLUMNNAME_AD_PrintFormat_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintFormat_ID, Integer.valueOf(AD_PrintFormat_ID));
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

	/** Set Manage Business Partners.
		@param IsDetailBPartner Manage Business Partners	  */
	public void setIsDetailBPartner (boolean IsDetailBPartner)
	{
		set_Value (COLUMNNAME_IsDetailBPartner, Boolean.valueOf(IsDetailBPartner));
	}

	/** Get Manage Business Partners.
		@return Manage Business Partners	  */
	public boolean isDetailBPartner () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailBPartner);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDetailCashFlow.
		@param IsDetailCashFlow IsDetailCashFlow	  */
	public void setIsDetailCashFlow (boolean IsDetailCashFlow)
	{
		set_Value (COLUMNNAME_IsDetailCashFlow, Boolean.valueOf(IsDetailCashFlow));
	}

	/** Get IsDetailCashFlow.
		@return IsDetailCashFlow	  */
	public boolean isDetailCashFlow () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailCashFlow);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDetailContract.
		@param IsDetailContract IsDetailContract	  */
	public void setIsDetailContract (boolean IsDetailContract)
	{
		set_Value (COLUMNNAME_IsDetailContract, Boolean.valueOf(IsDetailContract));
	}

	/** Get IsDetailContract.
		@return IsDetailContract	  */
	public boolean isDetailContract () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailContract);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Manage Products.
		@param IsDetailProduct Manage Products	  */
	public void setIsDetailProduct (boolean IsDetailProduct)
	{
		set_Value (COLUMNNAME_IsDetailProduct, Boolean.valueOf(IsDetailProduct));
	}

	/** Get Manage Products.
		@return Manage Products	  */
	public boolean isDetailProduct () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailProduct);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDetailReportLine.
		@param IsDetailReportLine IsDetailReportLine	  */
	public void setIsDetailReportLine (boolean IsDetailReportLine)
	{
		set_Value (COLUMNNAME_IsDetailReportLine, Boolean.valueOf(IsDetailReportLine));
	}

	/** Get IsDetailReportLine.
		@return IsDetailReportLine	  */
	public boolean isDetailReportLine () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailReportLine);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDetailTypeCost.
		@param IsDetailTypeCost IsDetailTypeCost	  */
	public void setIsDetailTypeCost (boolean IsDetailTypeCost)
	{
		set_Value (COLUMNNAME_IsDetailTypeCost, Boolean.valueOf(IsDetailTypeCost));
	}

	/** Get IsDetailTypeCost.
		@return IsDetailTypeCost	  */
	public boolean isDetailTypeCost () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailTypeCost);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsDetailTypeRevenue.
		@param IsDetailTypeRevenue IsDetailTypeRevenue	  */
	public void setIsDetailTypeRevenue (boolean IsDetailTypeRevenue)
	{
		set_Value (COLUMNNAME_IsDetailTypeRevenue, Boolean.valueOf(IsDetailTypeRevenue));
	}

	/** Get IsDetailTypeRevenue.
		@return IsDetailTypeRevenue	  */
	public boolean isDetailTypeRevenue () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailTypeRevenue);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public eone.base.model.I_AD_Process getJasperProcess() throws RuntimeException
    {
		return (eone.base.model.I_AD_Process)MTable.get(getCtx(), eone.base.model.I_AD_Process.Table_Name)
			.getPO(getJasperProcess_ID(), get_TrxName());	}

	/** Set Jasper Process.
		@param JasperProcess_ID 
		The Jasper Process used by the printengine if any process defined
	  */
	public void setJasperProcess_ID (int JasperProcess_ID)
	{
		if (JasperProcess_ID < 1) 
			set_Value (COLUMNNAME_JasperProcess_ID, null);
		else 
			set_Value (COLUMNNAME_JasperProcess_ID, Integer.valueOf(JasperProcess_ID));
	}

	/** Get Jasper Process.
		@return The Jasper Process used by the printengine if any process defined
	  */
	public int getJasperProcess_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_JasperProcess_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Jasper Process Now.
		@param JasperProcessing Jasper Process Now	  */
	public void setJasperProcessing (String JasperProcessing)
	{
		set_Value (COLUMNNAME_JasperProcessing, JasperProcessing);
	}

	/** Get Jasper Process Now.
		@return Jasper Process Now	  */
	public String getJasperProcessing () 
	{
		return (String)get_Value(COLUMNNAME_JasperProcessing);
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

	/** Set PA_Report_UU.
		@param PA_Report_UU PA_Report_UU	  */
	public void setPA_Report_UU (String PA_Report_UU)
	{
		set_Value (COLUMNNAME_PA_Report_UU, PA_Report_UU);
	}

	/** Get PA_Report_UU.
		@return PA_Report_UU	  */
	public String getPA_Report_UU () 
	{
		return (String)get_Value(COLUMNNAME_PA_Report_UU);
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}
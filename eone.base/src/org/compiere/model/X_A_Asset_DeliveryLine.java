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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for A_Asset_DeliveryLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_A_Asset_DeliveryLine extends PO implements I_A_Asset_DeliveryLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200906L;

    /** Standard Constructor */
    public X_A_Asset_DeliveryLine (Properties ctx, int A_Asset_DeliveryLine_ID, String trxName)
    {
      super (ctx, A_Asset_DeliveryLine_ID, trxName);
      /** if (A_Asset_DeliveryLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_A_Asset_DeliveryLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_A_Asset_DeliveryLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_A_Asset_Delivery getA_Asset_Delivery() throws RuntimeException
    {
		return (I_A_Asset_Delivery)MTable.get(getCtx(), I_A_Asset_Delivery.Table_Name)
			.getPO(getA_Asset_Delivery_ID(), get_TrxName());	}

	/** Set Asset Delivery.
		@param A_Asset_Delivery_ID 
		Delivery of Asset
	  */
	public void setA_Asset_Delivery_ID (int A_Asset_Delivery_ID)
	{
		if (A_Asset_Delivery_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_Delivery_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_Delivery_ID, Integer.valueOf(A_Asset_Delivery_ID));
	}

	/** Get Asset Delivery.
		@return Delivery of Asset
	  */
	public int getA_Asset_Delivery_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_Delivery_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Asset Delivery Line.
		@param A_Asset_DeliveryLine_ID Asset Delivery Line	  */
	public void setA_Asset_DeliveryLine_ID (int A_Asset_DeliveryLine_ID)
	{
		if (A_Asset_DeliveryLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_DeliveryLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_DeliveryLine_ID, Integer.valueOf(A_Asset_DeliveryLine_ID));
	}

	/** Get Asset Delivery Line.
		@return Asset Delivery Line	  */
	public int getA_Asset_DeliveryLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_DeliveryLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException
    {
		return (org.compiere.model.I_A_Asset)MTable.get(getCtx(), org.compiere.model.I_A_Asset.Table_Name)
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

	/** Set Comments.
		@param Comments 
		Comments or additional information
	  */
	public void setComments (String Comments)
	{
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments () 
	{
		return (String)get_Value(COLUMNNAME_Comments);
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

	/** Approved = AP */
	public static final String RECEIVECONFIRM_Approved = "AP";
	/** Confirm = CF */
	public static final String RECEIVECONFIRM_Confirm = "CF";
	/** None = NO */
	public static final String RECEIVECONFIRM_None = "NO";
	/** Set ReceiveConfirm.
		@param ReceiveConfirm ReceiveConfirm	  */
	public void setReceiveConfirm (String ReceiveConfirm)
	{

		set_Value (COLUMNNAME_ReceiveConfirm, ReceiveConfirm);
	}

	/** Get ReceiveConfirm.
		@return ReceiveConfirm	  */
	public String getReceiveConfirm () 
	{
		return (String)get_Value(COLUMNNAME_ReceiveConfirm);
	}
}
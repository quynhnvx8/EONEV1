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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_ConstructionLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_ConstructionLine extends PO implements I_C_ConstructionLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200820L;

    /** Standard Constructor */
    public X_C_ConstructionLine (Properties ctx, int C_ConstructionLine_ID, String trxName)
    {
      super (ctx, C_ConstructionLine_ID, trxName);
      /** if (C_ConstructionLine_ID == 0)
        {
			setC_Construction_ID (0);
			setC_ConstructionLine_ID (0);
			setLine (0);
// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_ProjectLine WHERE C_Project_ID=@C_Project_ID@
			setProcessed (false);
// N
        } */
    }

    /** Load Constructor */
    public X_C_ConstructionLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_ConstructionLine[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set ActualValue.
		@param ActualValue 
		ActualValue
	  */
	public void setActualValue (BigDecimal ActualValue)
	{
		set_Value (COLUMNNAME_ActualValue, ActualValue);
	}

	/** Get ActualValue.
		@return ActualValue
	  */
	public BigDecimal getActualValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ActualValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
			set_ValueNoCheck (COLUMNNAME_C_Construction_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Construction_ID, Integer.valueOf(C_Construction_ID));
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

	/** Set Construction Line.
		@param C_ConstructionLine_ID Construction Line	  */
	public void setC_ConstructionLine_ID (int C_ConstructionLine_ID)
	{
		if (C_ConstructionLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ConstructionLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ConstructionLine_ID, Integer.valueOf(C_ConstructionLine_ID));
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

	/** Set C_ConstructionLine_UU.
		@param C_ConstructionLine_UU C_ConstructionLine_UU	  */
	public void setC_ConstructionLine_UU (String C_ConstructionLine_UU)
	{
		set_Value (COLUMNNAME_C_ConstructionLine_UU, C_ConstructionLine_UU);
	}

	/** Get C_ConstructionLine_UU.
		@return C_ConstructionLine_UU	  */
	public String getC_ConstructionLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_ConstructionLine_UU);
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

	/** Set End Time.
		@param EndTime 
		End of the time span
	  */
	public void setEndTime (Timestamp EndTime)
	{
		set_Value (COLUMNNAME_EndTime, EndTime);
	}

	/** Get End Time.
		@return End of the time span
	  */
	public Timestamp getEndTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime);
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
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

	/** Set Planner Key.
		@param PlannerValue 
		Search Key of the Planning
	  */
	public void setPlannerValue (BigDecimal PlannerValue)
	{
		set_Value (COLUMNNAME_PlannerValue, PlannerValue);
	}

	/** Get Planner Key.
		@return Search Key of the Planning
	  */
	public BigDecimal getPlannerValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PlannerValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Start Time.
		@param StartTime 
		Time started
	  */
	public void setStartTime (Timestamp StartTime)
	{
		set_Value (COLUMNNAME_StartTime, StartTime);
	}

	/** Get Start Time.
		@return Time started
	  */
	public Timestamp getStartTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime);
	}
}
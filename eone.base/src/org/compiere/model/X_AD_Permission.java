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

/** Generated Model for AD_Permission
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_Permission extends PO implements I_AD_Permission, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210110L;

    /** Standard Constructor */
    public X_AD_Permission (Properties ctx, int AD_Permission_ID, String trxName)
    {
      super (ctx, AD_Permission_ID, trxName);
      /** if (AD_Permission_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_AD_Permission (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AD_Permission[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	
	/** Set Form View.
		@param AD_Form_Sale_ID Form View	  */
	public void setAD_Form_Sale_ID (int AD_Form_Sale_ID)
	{
		if (AD_Form_Sale_ID < 1) 
			set_Value (COLUMNNAME_AD_Form_Sale_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Form_Sale_ID, Integer.valueOf(AD_Form_Sale_ID));
	}

	/** Get Form View.
		@return Form View	  */
	public int getAD_Form_Sale_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Form_Sale_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Permission.
		@param AD_Permission_ID Permission	  */
	public void setAD_Permission_ID (int AD_Permission_ID)
	{
		if (AD_Permission_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Permission_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Permission_ID, Integer.valueOf(AD_Permission_ID));
	}

	/** Get Permission.
		@return Permission	  */
	public int getAD_Permission_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Permission_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_Name)
			.getPO(getAD_Role_ID(), get_TrxName());	}

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Records deletable.
		@param IsDeleteable 
		Indicates if records can be deleted from the database
	  */
	public void setIsDeleteable (boolean IsDeleteable)
	{
		set_Value (COLUMNNAME_IsDeleteable, Boolean.valueOf(IsDeleteable));
	}

	/** Get Records deletable.
		@return Indicates if records can be deleted from the database
	  */
	public boolean isDeleteable () 
	{
		Object oo = get_Value(COLUMNNAME_IsDeleteable);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Insert Record.
		@param IsInsertRecord 
		The user can insert a new Record
	  */
	public void setIsInsertRecord (boolean IsInsertRecord)
	{
		set_Value (COLUMNNAME_IsInsertRecord, Boolean.valueOf(IsInsertRecord));
	}

	/** Get Insert Record.
		@return The user can insert a new Record
	  */
	public boolean isInsertRecord () 
	{
		Object oo = get_Value(COLUMNNAME_IsInsertRecord);
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

	/** Set Updatable.
		@param IsUpdateable 
		Determines, if the field can be updated
	  */
	public void setIsUpdateable (boolean IsUpdateable)
	{
		set_Value (COLUMNNAME_IsUpdateable, Boolean.valueOf(IsUpdateable));
	}

	/** Get Updatable.
		@return Determines, if the field can be updated
	  */
	public boolean isUpdateable () 
	{
		Object oo = get_Value(COLUMNNAME_IsUpdateable);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}
/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Document Type Model
 *	
 *  @author Jorg Janke
 *  @author Karsten Thiemann FR [ 1782412 ]
 *  @author Teo Sarca, www.arhipac.ro
 *  		<li>BF [ 2476824 ] MDocType.getOfDocBaseType should return ONLY active records
 *  		<li>BF [ -       ] MDocType.getOfClient should return ONLY active records.
 *  							See https://sourceforge.net/forum/message.php?msg_id=6499893
 *  @version $Id: MDocType.java,v 1.3 2006/07/30 00:54:54 jjanke Exp $
 */
public class MDocType extends X_C_DocType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6556521509479670059L;

	/**
	 * Return the first Doc Type for this BaseType
	 * @param DocBaseType
	 * @return
	 */
	static public int getDocType(String DocBaseType)
	{
		MDocType[] doc = MDocType.getOfDocBaseType(Env.getCtx(), DocBaseType);
		return doc.length > 0 ? doc[0].get_ID() : 0;
	}
	
	/**
	 * 	Get Client Document Type with DocBaseType
	 *	@param ctx context
	 *	@param DocBaseType base document type
	 *	@return array of doc types
	 */
	static public MDocType[] getOfDocBaseType (Properties ctx, String DocBaseType)
	{
		final String whereClause  = "AD_Client_ID=? AND DocBaseType=?";
		List<MDocType> list = new Query(ctx, Table_Name, whereClause, null)
									.setParameters(Env.getAD_Client_ID(ctx), DocBaseType)
									.setOnlyActiveRecords(true)
									.setOrderBy("IsDefault DESC, C_DocType_ID")
									.list();
		return list.toArray(new MDocType[list.size()]);
	}	//	getOfDocBaseType
	
	/**
	 * 	Get Client Document Types
	 *	@param ctx context
	 *	@return array of doc types
	 */
	static public MDocType[] getOfClient (Properties ctx)
	{
		List<MDocType> list = new Query(ctx, Table_Name, null, null)
									.setClient_ID()
									.setOnlyActiveRecords(true)
									.list();
		return list.toArray(new MDocType[list.size()]);
	}	//	getOfClient
	
	/**
	 * 	Get Document Type (cached)
	 *	@param ctx context
	 *	@param C_DocType_ID id
	 *	@return document type
	 */
	static public MDocType get (Properties ctx, int C_DocType_ID)
	{
		MDocType retValue = (MDocType)s_cache.get(C_DocType_ID);
		if (retValue == null)
		{
			retValue = new MDocType (ctx, C_DocType_ID, null);
			s_cache.put(C_DocType_ID, retValue);
		}
		return retValue; 
	} 	//	get
	
	/**	Cache					*/
	static private CCache<Integer,MDocType>	s_cache = new CCache<Integer,MDocType>(Table_Name, 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DocType_ID id
	 *	@param trxName transaction
	 */
	public MDocType(Properties ctx, int C_DocType_ID, String trxName)
	{
		super(ctx, C_DocType_ID, trxName);
		if (C_DocType_ID == 0)
		{
			setIsDefault (false);
		}
	}	//	MDocType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MDocType(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDocType

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param DocBaseType document base type
	 *	@param Name name
	 *	@param trxName transaction
	 */
	public MDocType (Properties ctx, String DocBaseType, String Name, String trxName)
	{
		this (ctx, 0, trxName);
		setAD_Org_ID(0);
		setDocBaseType (DocBaseType);
		setName (Name);
		
	}	//	MDocType

	
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("MDocType[");
		sb.append(get_ID()).append("-").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

	
	
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		String listvalue = "";
		String sql = "";
		if (is_ValueChanged("IsShowSub")) {
			sql = "select COALESCE(string_agg(C_DocType_ID::text,','),'') from C_DocType where IsShowSub = ?";
			listvalue = DB.getSQLValueString(get_TrxName(), sql, Env.YES);
			Env.setContext(getCtx(), "#ShowDocTypeSub", listvalue);
		}
		if (is_ValueChanged("IsShowTab")) {
			sql = "select COALESCE(string_agg(C_DocType_ID::text,','),'') from C_DocType where IsShowTab = ?";
			listvalue = DB.getSQLValueString(get_TrxName(), sql, Env.YES);
			Env.setContext(getCtx(), "#ShowTabDetail", listvalue);
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return success;
	}	//	afterSave
	
	/**
	 * 	Executed before Delete operation.
	 *
	 *	@return true if delete is a success
	 */
	protected boolean beforeDelete ()
	{
		return true;
	}   //  beforeDelete

	

}	//	MDocType

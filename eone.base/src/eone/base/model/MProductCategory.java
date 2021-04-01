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
package eone.base.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Product Category Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductCategory.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MProductCategory extends X_M_Product_Category
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1239249591584452179L;

	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Product_Category_ID id
	 *	@return category
	 */
	public static MProductCategory get (Properties ctx, int M_Product_Category_ID)
	{
		Integer ii = Integer.valueOf(M_Product_Category_ID);
		MProductCategory retValue = (MProductCategory)s_cache.get(ii);
		if (retValue != null)
			return retValue;
		retValue = new MProductCategory (ctx, M_Product_Category_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (M_Product_Category_ID, retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Is Product in Category
	 *	@param M_Product_Category_ID category
	 *	@param M_Product_ID product
	 *	@return true if product has category
	 */
	public static boolean isCategory (int M_Product_Category_ID, int M_Product_ID)
	{
		if (M_Product_ID == 0 || M_Product_Category_ID == 0)
			return false;
		//	Look up
		Integer product = Integer.valueOf(M_Product_ID);
		Integer category = (Integer)s_products.get(product);
		if (category != null)
			return category.intValue() == M_Product_Category_ID;
		
		String sql = "SELECT M_Product_Category_ID FROM M_Product WHERE M_Product_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				category = Integer.valueOf(rs.getInt(1));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (category != null)
		{
			//	TODO: LRU logic  
			s_products.put(product, category);
			//
			if (s_log.isLoggable(Level.FINE)) s_log.fine("M_Product_ID=" + M_Product_ID + "(" + category
				+ ") in M_Product_Category_ID=" + M_Product_Category_ID
				+ " - " + (category.intValue() == M_Product_Category_ID));
			return category.intValue() == M_Product_Category_ID;
		}
		s_log.log(Level.SEVERE, "Not found M_Product_ID=" + M_Product_ID);
		return false;
	}	//	isCategory
	
	/**	Categopry Cache				*/
	private static CCache<Integer,MProductCategory>	s_cache = new CCache<Integer,MProductCategory>(Table_Name, 20);
	/**	Product Cache				*/
	private static CCache<Integer,Integer> s_products = new CCache<Integer,Integer>(I_M_Product.Table_Name, Table_Name + "|M_Product", 100);
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MProductCategory.class);

	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Category_ID id
	 *	@param trxName transaction
	 */
	public MProductCategory (Properties ctx, int M_Product_Category_ID, String trxName)
	{
		super(ctx, M_Product_Category_ID, trxName);
		if (M_Product_Category_ID == 0)
		{
			setIsDefault (false);
		}
	}	//	MProductCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MProductCategory(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductCategory

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
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

	
		
}	//	MProductCategory
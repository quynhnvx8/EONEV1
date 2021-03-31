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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *	Unit Of Measure Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MUOM.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MUOM extends X_C_UOM
{
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord || is_ValueChanged("UOMSymbol") || isActive()) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_UOM_ID != ? And UOMSymbol = ? And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_UOM_ID(), getUOMSymbol(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "UOMSymbol exists");
				return false;
			}

		}
		return true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	UOM Cache				*/
	protected static CCache<Integer,MUOM>	s_cache = new CCache<Integer,MUOM>(Table_Name, 30);

	/**
	 * 	Get UOM from Cache
	 * 	@param ctx context
	 *	@param C_UOM_ID ID
	 * 	@return UOM
	 */
	public static MUOM get (Properties ctx, int C_UOM_ID)
	{
		if (s_cache.size() == 0)
			loadUOMs (ctx);
		//
		MUOM uom = s_cache.get(C_UOM_ID);
		if (uom != null)
			return uom;
		//
		uom = new MUOM (ctx, C_UOM_ID, null);
		s_cache.put(C_UOM_ID, uom);
		return uom;
	}	//	get
	
	/**
	 * Get UOM by name
	 * @param ctx
	 * @param name
	 * @param trxName
	 * @return MUOM if found, null if not found
	 */
	public static MUOM get(Properties ctx, String name, String trxName)
	{
		MTable table = MTable.get(Env.getCtx(), Table_ID);
		MUOM uom = (MUOM)table.getPO("Name = ?", new Object[]{name}, trxName);
		
		return uom;
	}

	/**
	 * 	Get Precision
	 * 	@param ctx context
	 *	@param C_UOM_ID ID
	 * 	@return Precision
	 */
	public static int getPrecision (Properties ctx, int C_UOM_ID)
	{
		MUOM uom = get(ctx, C_UOM_ID);
		return uom.getStdPrecision();
	}	//	getPrecision

	/**
	 * 	Load All UOMs
	 * 	@param ctx context
	 */
	protected static void loadUOMs (Properties ctx)
	{
		List<MUOM> list = new Query(ctx, Table_Name, "IsActive='Y'", null)
								.setApplyAccessFilter(MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
								.list();
		//
		for (MUOM uom : list) {
			s_cache.put(uom.get_ID(), uom);
		}
	}	//	loadUOMs
	
	
	/**************************************************************************
	 *	Constructor.
	 *	@param ctx context
	 *  @param C_UOM_ID UOM ID
	 *  @param trxName transaction
	 */
	public MUOM (Properties ctx, int C_UOM_ID, String trxName)
	{
		super (ctx, C_UOM_ID, trxName);
		if (C_UOM_ID == 0)
		{
			setIsDefault (false);
			setStdPrecision (2);
		}
	}	//	UOM

	/**
	 *	Load Constructor.
	 *	@param ctx context
	 *  @param rs result set
	 *  @param trxName transaction
	 */
	public MUOM (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	UOM

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("UOM[");
		sb.append("ID=").append(get_ID())
			.append(", Name=").append(getName());
		return sb.toString();
	}	//	toString

	/**
	 * 	Round qty
	 *	@param qty quantity
	 *	@param stdPrecision true if std precisison
	 *	@return rounded quantity
	 */
	public BigDecimal round (BigDecimal qty, boolean stdPrecision)
	{
		int precision = getStdPrecision();
		
		if (qty.scale() > precision)
			return qty.setScale(getStdPrecision(), RoundingMode.HALF_UP);
		return qty;
	}	//	round

	

}	//	MUOM

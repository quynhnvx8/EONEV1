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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  Tax Model
 *
 *	@author Jorg Janke
 *	@version $Id: MTax.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 * 	red1 - FR: [ 2214883 ] Remove SQL code and Replace for Query
 *  trifonnt - BF [2913276] - Allow only one Default Tax Rate per Tax Category
 *  mjmckay - BF [2948632] - Allow edits to the Default Tax Rate 
 */
public class MTax extends X_C_Tax
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5871827364071851846L;

	/**	Cache						*/
	private static CCache<Integer,MTax>		s_cache	= new CCache<Integer,MTax>(Table_Name, 5);
	/**	Cache of Client						*/
	private static CCache<Integer,MTax[]>	s_cacheAll = new CCache<Integer,MTax[]>(Table_Name, Table_Name+"_Of_Client", 5);
	
	

	/**
	 * 	Get All Tax codes (for AD_Client)
	 *	@param ctx context
	 *	@return MTax
	 */
	public static MTax[] getAll (Properties ctx)
	{
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		MTax[] retValue = (MTax[])s_cacheAll.get(AD_Client_ID);
		if (retValue != null)
			return retValue;

		//	Create it
		//FR: [ 2214883 ] Remove SQL code and Replace for Query - red1
		List<MTax> list = new Query(ctx, I_C_Tax.Table_Name, null, null)
								.setClient_ID()
								.setOrderBy(" Value DESC")
								.setOnlyActiveRecords(true)
								.list();
		for (MTax tax : list)
		{
			s_cache.put(tax.get_ID(), tax);
		}
		retValue = list.toArray(new MTax[list.size()]);
		s_cacheAll.put(AD_Client_ID, retValue);
		return retValue;
	}	//	getAll

	
	/**
	 * 	Get Tax from Cache
	 *	@param ctx context
	 *	@param C_Tax_ID id
	 *	@return MTax
	 */
	public static MTax get (Properties ctx, int C_Tax_ID)
	{
		Integer key = Integer.valueOf(C_Tax_ID);
		MTax retValue = (MTax) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MTax (ctx, C_Tax_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Tax_ID id
	 *	@param trxName transaction
	 */
	public MTax (Properties ctx, int C_Tax_ID, String trxName)
	{
		super (ctx, C_Tax_ID, trxName);
		if (C_Tax_ID == 0)
		{
			setIsDefault (false);
			setRate (Env.ZERO);
		}
	}	//	MTax

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MTax (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTax

	/**
	 * 	New Constructor
	 *	@param ctx
	 *	@param Name
	 *	@param Rate
	 *	@param C_TaxCategory_ID
	 *	@param trxName transaction
	 */
	public MTax (Properties ctx, String Name, BigDecimal Rate, int C_TaxCategory_ID, String trxName)
	{
		this (ctx, 0, trxName);
		setName (Name);
		setRate (Rate == null ? Env.ZERO : Rate);
	}	//	MTax

	
	public boolean isZeroTax()
	{
		return getRate().signum() == 0;
	}	//	isZeroTax
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("MTax[")
			.append(get_ID())
			.append(", Name = ").append(getName())
			.append(", Rate=").append(getRate())
			.append("]");
		return sb.toString();
	}	//	toString

	
	/**
	 * 	Calculate Tax - no rounding
	 *	@param amount amount
	 *	@param taxIncluded if true tax is calculated from gross otherwise from net 
	 *	@param scale scale 
	 *	@return  tax amount
	 */
	public BigDecimal calculateTax (BigDecimal amount, boolean taxIncluded, int scale)
	{
		//	Null Tax
		if (isZeroTax())
			return Env.ZERO;

		MTax[] taxarray = new MTax[] {this};

		BigDecimal tax = Env.ZERO;		
		for (MTax taxc : taxarray) {
			BigDecimal multiplier = taxc.getRate().divide(Env.ONEHUNDRED, 12, RoundingMode.HALF_UP);		
			if (!taxIncluded)	//	$100 * 6 / 100 == $6 == $100 * 0.06
			{
				BigDecimal itax = amount.multiply(multiplier).setScale(scale, RoundingMode.HALF_UP);
				tax = tax.add(itax);
			}
			else			//	$106 - ($106 / (100+6)/100) == $6 == $106 - ($106/1.06)
			{
				multiplier = multiplier.add(Env.ONE);
				BigDecimal base = amount.divide(multiplier, 12, RoundingMode.HALF_UP); 
				BigDecimal itax = amount.subtract(base).setScale(scale, RoundingMode.HALF_UP);
				tax = tax.add(itax);
			}
		}
		if (log.isLoggable(Level.FINE)) log.fine("calculateTax " + amount 
			+ " (incl=" + taxIncluded + ",scale=" + scale 
			+ ") = " + tax + " [" + tax + "]");
		return tax;
	}	//	calculateTax

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord || is_ValueChanged("Value") || isActive() || is_ValueChanged("Name")) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_Tax_ID != ? And (Value = ? Or Name = ?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_Tax_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", Msg.getMsg(getCtx(), "ValueOrNameExists"));//ValueExists, NameExists
				return false;
			}

		}
		
		if (isDefault()) {
			String whereClause = MTax.COLUMNNAME_C_Tax_ID+"<>? AND IsDefault='Y'";
			List<MTax> list = new Query(getCtx(), I_C_Tax.Table_Name, whereClause,  get_TrxName())
				.setParameters(getC_Tax_ID())
				.setOnlyActiveRecords(true)
				.list();
			if (list.size() >= 1) {
				log.saveError("Error", Msg.parseTranslation(getCtx(), Msg.getMsg(Env.getCtx(),"OnlyOneTaxPerCategoryMarkedDefault")));
				return false;
			}
		}
		

		return super.beforeSave(newRecord);
	}
	
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

}	//	MTax

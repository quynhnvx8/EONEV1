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

import java.awt.Point;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Msg;

import eone.exceptions.DBException;

/**
 *	Unit of Measure Conversion Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUOMConversion.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MUOMConvert extends X_C_UOM_Convert
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3555218774291122619L;


	/**
	 *	Convert qty to target UOM and round.
	 *  @param ctx context
	 *  @param C_UOM_ID from UOM
	 *  @param C_UOM_To_ID to UOM
	 *  @param qty qty
	 *  @return converted qty (std precision)
	 */
	static public BigDecimal convert (Properties ctx,
		int C_UOM_ID, int C_UOM_To_ID, BigDecimal qty)
	{
		if (qty == null || qty.compareTo(Env.ZERO)==0 || C_UOM_ID == C_UOM_To_ID)
			return qty;
		BigDecimal retValue = getRate (ctx, C_UOM_ID, C_UOM_To_ID);
		if (retValue != null)
		{
			MUOM uom = MUOM.get (ctx, C_UOM_To_ID);
			if (uom != null)
				return uom.round(retValue.multiply(qty), true);
			return retValue.multiply(qty);
		}
		return null;
	}	//	convert

	/**
	 *	Get Multiplier Rate to target UOM
	 *  @param ctx context
	 *  @param C_UOM_ID from UOM
	 *  @param C_UOM_To_ID to UOM
	 *  @return multiplier
	 */
	static public BigDecimal getRate (Properties ctx,
		int C_UOM_ID, int C_UOM_To_ID)
	{
		//	nothing to do
		if (C_UOM_ID == C_UOM_To_ID)
			return Env.ONE;
		//
		Point p = new Point(C_UOM_ID, C_UOM_To_ID);
		//	get conversion
		BigDecimal retValue = getRate (ctx, p);
		return retValue;
	}	//	convert

	
	/**
	 *	Convert qty to target UOM and round.
	 *  @param ctx context
	 *  @param C_UOM_ID from UOM
	 *  @param qty qty
	 *  @return minutes - 0 if not found
	 */
	static public int convertToMinutes (Properties ctx,
		int C_UOM_ID, BigDecimal qty)
	{
		return 0;
		
	}	//	convert
	
	/**
	 * 	Calculate End Date based on start date and qty
	 *  @param ctx context
	 * 	@param startDate date
	 *  @param C_UOM_ID UOM
	 * 	@param qty qty
	 * 	@return end date
	 */
	static public Timestamp getEndDate (Properties ctx, Timestamp startDate, int C_UOM_ID, BigDecimal qty)
	{
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.setTime(startDate);
		//
		int minutes = MUOMConvert.convertToMinutes (ctx, C_UOM_ID, qty);
		endDate.add(Calendar.MINUTE, minutes);
		//
		Timestamp retValue = new Timestamp(endDate.getTimeInMillis());
	//	log.config( "TimeUtil.getEndDate", "Start=" + startDate
	//		+ ", Qty=" + qty + ", End=" + retValue);
		return retValue;
	}	//	startDate
	
	/**************************************************************************
	 * 	Get Conversion Multiplier Rate, try to derive it if not found directly
	 * 	@param ctx context
	 * 	@param p Point with from(x) - to(y) C_UOM_ID
	 * 	@return conversion multiplier or null
	 */
	static protected BigDecimal getRate (Properties ctx, Point p)
	{
		BigDecimal retValue = null;
		if (Ini.isClient())
		{
			if (s_conversions == null)
				createRates(ctx);
			retValue = (BigDecimal)s_conversions.get(p);
		}
		else
			retValue = getRate (p.x, p.y);
		if (retValue != null)
			return retValue;
		//	try to derive
		return deriveRate (ctx, p.x, p.y);
	}	//	getConversion

	/**
	 * 	Create Conversion Matrix (Client)
	 * 	@param ctx context
	 */
	protected static void createRates (Properties ctx)
	{
		s_conversions = new CCache<Point,BigDecimal>(Table_Name, 20);
		//
		String sql = MRole.getDefault(ctx, false).addAccessSQL (
			"SELECT C_UOM_ID, C_UOM_To_ID, MultiplyRate, DivideRate "
			+ "FROM C_UOM_Convert "
			+ "WHERE IsActive='Y' AND M_Product_ID IS NULL",
			"C_UOM_Convert", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Point p = new Point (rs.getInt(1), rs.getInt(2));
				BigDecimal mr = rs.getBigDecimal(3);
				BigDecimal dr = rs.getBigDecimal(4);
				if (mr != null)
					s_conversions.put(p, mr);
				//	reverse
				if (dr == null && mr != null)
					dr = Env.ONE.divide(mr, RoundingMode.HALF_UP);
				if (dr != null)
					s_conversions.put(new Point(p.y,p.x), dr);
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}	//	createRatess

	/**
	 * 	Derive Standard Conversions
	 * 	@param ctx context
	 *  @param C_UOM_ID from UOM
	 *  @param C_UOM_To_ID to UOM
	 * 	@return Conversion or null
	 */
	public static BigDecimal deriveRate (Properties ctx,
		int C_UOM_ID, int C_UOM_To_ID)
	{
		if (C_UOM_ID == C_UOM_To_ID)
			return Env.ONE;
		//	get Info
		MUOM from = MUOM.get (ctx, C_UOM_ID);
		MUOM to = MUOM.get (ctx, C_UOM_To_ID);
		if (from == null || to == null)
			return null;
		
		return null;
	}	//	deriveRate

	/**************************************************************************
	 * 	Get Conversion Multiplier Rate from Server
	 *  @param C_UOM_ID from UOM
	 *  @param C_UOM_To_ID to UOM
	 * 	@return conversion multiplier or null
	 */
	public static BigDecimal getRate (int C_UOM_ID, int C_UOM_To_ID)
	{
		return convert (C_UOM_ID, C_UOM_To_ID, GETRATE, false);
	}	//	getConversion

	/**
	 *  Get Converted Qty from Server (no cache)
	 *  @param  qty             The quantity to be converted
	 *  @param  C_UOM_From_ID   The C_UOM_ID of the qty
	 *  @param  C_UOM_To_ID     The targeted UOM
	 *  @param  StdPrecision    if true, standard precision, if false costing precision
	 *  @return amount
	 *  @depreciated should not be used
	 */
	public static BigDecimal convert (int C_UOM_From_ID, int C_UOM_To_ID, 
		BigDecimal qty, boolean StdPrecision)
	{
		//  Nothing to do
		if (qty == null || qty.compareTo(Env.ZERO)==0
				|| C_UOM_From_ID == C_UOM_To_ID)
			return qty;
		//
		BigDecimal retValue = null;
		int precision = 2;
		String sql = "SELECT c.MultiplyRate, uomTo.StdPrecision "
			+ "FROM	C_UOM_Convert c"
			+ " INNER JOIN C_UOM uomTo ON (c.C_UOM_TO_ID=uomTo.C_UOM_ID) "
			+ "WHERE c.IsActive='Y' AND c.C_UOM_ID=? AND c.C_UOM_TO_ID=? "		//	#1/2
			+ " AND c.M_Product_ID IS NULL"
			+ " ORDER BY c.AD_Client_ID DESC, c.AD_Org_ID DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_UOM_From_ID);
			pstmt.setInt(2, C_UOM_To_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = rs.getBigDecimal(1);
				precision = rs.getInt(2);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (retValue == null)
		{
			if (s_log.isLoggable(Level.INFO)) s_log.info ("NOT found - FromUOM=" + C_UOM_From_ID + ", ToUOM=" + C_UOM_To_ID);
			return null;
		}
			
		//	Just get Rate
		if (GETRATE.equals(qty))
			return retValue;
		
		//	Calculate & Scale
		retValue = retValue.multiply(qty);
		if (retValue.scale() > precision)
			retValue = retValue.setScale(precision, RoundingMode.HALF_UP);
		return retValue;
	}   //  convert

	
	/**************************************************************************
	 *	Convert PRICE expressed in entered UoM to equivalent price in product UoM and round. <br/>
	 *  OR Convert QTY in product UOM to qty in entered UoM and round. <br/>
	 *  
	 *   eg: $6/6pk => $1/ea <br/>
	 *   OR 6 X ea => 1 X 6pk
	 *   
	 *  @param ctx context
	 *  @param M_Product_ID product
	 *  @param C_UOM_To_ID entered UOM
	 *  @param qtyPrice quantity or price
	 *  @return Product: Qty/Price (precision rounded)
	 */
	static public BigDecimal convertProductTo (Properties ctx,
		int M_Product_ID, int C_UOM_To_ID, BigDecimal qtyPrice)
	{
		if (qtyPrice == null || qtyPrice.signum() == 0 
			|| M_Product_ID == 0 || C_UOM_To_ID == 0)
			return qtyPrice;
		
		BigDecimal retValue = getProductRateTo (ctx, M_Product_ID, C_UOM_To_ID);
		if (retValue != null)
		{
			if (Env.ONE.compareTo(retValue) == 0)
				return qtyPrice;
			MUOM uom = MUOM.get (ctx, C_UOM_To_ID);
			if (uom != null)
				return uom.round(retValue.multiply(qtyPrice), true);
			return retValue.multiply(qtyPrice);
		}
		return null;
	}	//	convertProductTo

	/**
	 *	Get multiply rate to convert PRICE from price in entered UOM to price in product UOM <br/>
	 *  OR multiply rate to convert QTY from product UOM to entered UOM
	 *  @param ctx context
	 *  @param M_Product_ID product
	 *  @param C_UOM_To_ID entered UOM
	 *  @return multiplier or null
	 */
	static public BigDecimal getProductRateTo (Properties ctx,
		int M_Product_ID, int C_UOM_To_ID)
	{
		if (M_Product_ID == 0)
			return null;
		MUOMConvert[] rates = getProductConversions(ctx, M_Product_ID);
		
		for (int i = 0; i < rates.length; i++)
		{
			MUOMConvert rate = rates[i];
			if (rate.getC_UOM_To_ID() == C_UOM_To_ID)
				return rate.getMultiplyRate();
		}
		
		List<MUOMConvert> conversions = new Query(ctx, Table_Name, "C_UOM_ID=? AND C_UOM_TO_ID=?", null)
				.setParameters(MProduct.get(ctx, M_Product_ID).getC_UOM_ID(), C_UOM_To_ID)
				.setOnlyActiveRecords(true)
				.list();
		for (int i = 0; i < conversions.size(); i++)
		{
			MUOMConvert rate = conversions.get(i);
			if (rate.getC_UOM_To_ID() == C_UOM_To_ID)
				return rate.getMultiplyRate();
		}
		return null;
	}	//	getProductRateTo

	/**************************************************************************
	 *	Convert PRICE expressed in product UoM to equivalent price in entered UoM and round. <br/>
	 *  OR Convert QTY in entered UOM to qty in product UoM and round.  <br/>
	 *  
	 *   eg: $1/ea => $6/6pk <br/>
	 *   OR 1 X 6pk => 6 X ea
	 *   
	 *  @param ctx context
	 *  @param M_Product_ID product
	 *  @param C_UOM_To_ID entered UOM
	 *  @param qtyPrice quantity or price
	 *  @return Product: Qty/Price (precision rounded)
	 */
	static public BigDecimal convertProductFrom (Properties ctx,
		int M_Product_ID, int C_UOM_To_ID, BigDecimal qtyPrice)
	{
		//	No conversion
		if (qtyPrice == null || qtyPrice.compareTo(Env.ZERO)==0 
			|| C_UOM_To_ID == 0|| M_Product_ID == 0)
		{
			if (s_log.isLoggable(Level.FINE)) s_log.fine("No Conversion - QtyPrice=" + qtyPrice);
			return qtyPrice;
		}
		
		BigDecimal retValue = getProductRateFrom (ctx, M_Product_ID, C_UOM_To_ID);
		if (retValue != null)
		{
			if (Env.ONE.compareTo(retValue) == 0)
				return qtyPrice;
			MUOM uom = MUOM.get (ctx, C_UOM_To_ID);
			if (uom != null)
				return uom.round(retValue.multiply(qtyPrice), true);
			return retValue.multiply(qtyPrice);
		}
		if (s_log.isLoggable(Level.FINE)) s_log.fine("No Rate M_Product_ID=" + M_Product_ID);
		return null;
	}	//	convertProductFrom

	/**
	 *	Get multiply rate to convert PRICE from price in entered UOM to price in product UOM <br/>
	 *  OR multiply rate to convert QTY from product UOM to entered UOM
	 *  @param ctx context
	 *  @param M_Product_ID product
	 *  @param C_UOM_To_ID entered UOM
	 *  @return multiplier or null
	 */
	static public BigDecimal getProductRateFrom (Properties ctx,
		int M_Product_ID, int C_UOM_To_ID)
	{
		MUOMConvert[] rates = getProductConversions(ctx, M_Product_ID);
		
		for (int i = 0; i < rates.length; i++)
		{
			MUOMConvert rate = rates[i];
			if (rate.getC_UOM_To_ID() == C_UOM_To_ID)
				return rate.getDivideRate();
		}
	
		List<MUOMConvert> conversions = new Query(ctx, Table_Name, "C_UOM_ID=? AND C_UOM_TO_ID=?", null)
				.setParameters(MProduct.get(ctx, M_Product_ID).getC_UOM_ID(), C_UOM_To_ID)
				.setOnlyActiveRecords(true)
				.list();
		for (int i = 0; i < conversions.size(); i++)
		{
			MUOMConvert rate = conversions.get(i);
			if (rate.getC_UOM_To_ID() == C_UOM_To_ID)
				return rate.getDivideRate();
		}
		
		return null;
	}	//	getProductRateFrom


	/**
	 * 	Get Product Conversions (cached)
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@return array of conversions
	 */
	static public MUOMConvert[] getProductConversions (Properties ctx, int M_Product_ID)
	{
		if (M_Product_ID == 0)
			return new MUOMConvert[0];
		Integer key = Integer.valueOf(M_Product_ID);
		MUOMConvert[] result = (MUOMConvert[])s_conversionProduct.get(key);
		if (result != null)
			return result;
		
		ArrayList<MUOMConvert> list = new ArrayList<MUOMConvert>();
		//	Add default conversion
		MUOMConvert defRate = new MUOMConvert (MProduct.get(ctx, M_Product_ID));
		list.add(defRate);
		//
		final String whereClause = "M_Product_ID=?"
			+ " AND EXISTS (SELECT 1 FROM M_Product p "
				+ "WHERE C_UOM_Convert.M_Product_ID=p.M_Product_ID AND C_UOM_Convert.C_UOM_ID=p.C_UOM_ID)";
		List<MUOMConvert> conversions = new Query(ctx, Table_Name, whereClause, null)
		.setParameters(M_Product_ID)
		.setOnlyActiveRecords(true)
		.list();
		list.addAll(conversions);
		
		//	Convert & save
		result = new MUOMConvert[list.size ()];
		list.toArray (result);
		s_conversionProduct.put(key, result);
		if (s_log.isLoggable(Level.FINE)) s_log.fine("getProductConversions - M_Product_ID=" + M_Product_ID + " #" + result.length);
		return result;
	}	//	getProductConversions

	/** Static Logger					*/
	private static final CLogger s_log = CLogger.getCLogger(MUOMConvert.class);
	/**	Indicator for Rate					*/
	protected static final BigDecimal GETRATE = BigDecimal.valueOf(123.456);
	/**	Conversion Map: Key=Point(from,to) Value=BigDecimal	*/
	protected static CCache<Point,BigDecimal>	s_conversions = null;
	/** Product Conversion Map					*/
	protected static final CCache<Integer,MUOMConvert[]>	s_conversionProduct 
		= new CCache<Integer,MUOMConvert[]>(Table_Name, Table_Name+"_Of_Product", 20); 
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param C_UOM_Conversion_ID id
	 *	@param trxName transaction
	 */
	public MUOMConvert (Properties ctx, int C_UOM_Conversion_ID, String trxName)
	{
		super(ctx, C_UOM_Conversion_ID, trxName);
	}	//	MUOMConversion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MUOMConvert(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MUOMConversion

	/**
	 * 	Parent Constructor
	 *	@param parent uom parent
	 */
	public MUOMConvert (MUOM parent)
	{
		this(parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg (parent);
		setC_UOM_ID (parent.getC_UOM_ID());
		setM_Product_ID(0);
		//
		setC_UOM_To_ID (parent.getC_UOM_ID());
		setMultiplyRate(Env.ONE);
		setDivideRate(Env.ONE);
	}	//	MUOMConversion

	/**
	 * 	Parent Constructor
	 *	@param parent product parent
	 */
	public MUOMConvert (MProduct parent)
	{
		this(parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg (parent);
		setC_UOM_ID (parent.getC_UOM_ID());
		setM_Product_ID(parent.getM_Product_ID());
		//
		setC_UOM_To_ID (parent.getC_UOM_ID());
		setMultiplyRate(Env.ONE);
		setDivideRate(Env.ONE);
	}	//	MUOMConversion
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	From - To is the same
		if (getC_UOM_ID() == getC_UOM_To_ID())
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@C_UOM_ID@ = @C_UOM_To_ID@"));
			return false;
		}
		//	Nothing to convert
		if (getMultiplyRate().compareTo(Env.ZERO) <= 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@MultiplyRate@ <= 0"));
			return false;
		}
		//	Enforce Product UOM
		if (MSysConfig.getBooleanValue(MSysConfig.ProductUOMConversionUOMValidate, true, getAD_Client_ID()))
		{
			if (getM_Product_ID() != 0 
				&& (newRecord || is_ValueChanged("M_Product_ID")))
			{
				// Check of product must be in the same transaction as the conversion being saved
				MProduct product = new MProduct(getCtx(), getM_Product_ID(), get_TrxName());
				if (product.getC_UOM_ID() != getC_UOM_ID())
				{
					MUOM uom = MUOM.get(getCtx(), product.getC_UOM_ID());
					log.saveError("ProductUOMConversionUOMError", uom.getName());
					return false;
				}
			}
		}

		//	The Product UoM needs to be the smallest UoM - Multiplier must be < 0; Divider must be > 0
		if (MSysConfig.getBooleanValue(MSysConfig.ProductUOMConversionRateValidate, true, getAD_Client_ID()))
		{
			if (getM_Product_ID() != 0 && getDivideRate().compareTo(Env.ONE) < 0)
			{
				log.saveError("ProductUOMConversionRateError", "");
				return false;
			}
		}
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MUOMConversion[");
		sb.append(get_ID()).append("-C_UOM_ID=").append(getC_UOM_ID())
			.append(",C_UOM_To_ID=").append(getC_UOM_To_ID())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append("-Multiply=").append(getMultiplyRate())
			.append("/Divide=").append(getDivideRate())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	UOMConversion

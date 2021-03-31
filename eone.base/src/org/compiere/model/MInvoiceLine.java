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
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.adempiere.base.IProductPricing;
import org.compiere.util.CLogger;


/**
 *	Invoice Line Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInvoiceLine.java,v 1.5 2006/07/30 00:51:03 jjanke Exp $
 * 
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>BF [ 2804142 ] MInvoice.setRMALine should work only for CreditMemo invoices
 * 				https://sourceforge.net/tracker/?func=detail&aid=2804142&group_id=176962&atid=879332
 * @author red1 FR: [ 2214883 ] Remove SQL code and Replace for Query
 */
public class MInvoiceLine extends X_C_InvoiceLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1590896898028805978L;

	/**
	 * 	Get Invoice Line referencing InOut Line
	 *	@param sLine shipment line
	 *	@return (first) invoice line
	 */
	public static MInvoiceLine getOfInOutLine (MInOutLine sLine)
	{
		if (sLine == null)
			return null;
		final String whereClause = I_M_InOutLine.COLUMNNAME_M_InOutLine_ID+"=?";
		List<MInvoiceLine> list = new Query(sLine.getCtx(),I_C_InvoiceLine.Table_Name,whereClause,sLine.get_TrxName())
		.setParameters(sLine.getM_InOutLine_ID())
		.list();
		
		MInvoiceLine retValue = null;
		if (list.size() > 0) {
			retValue = list.get(0);
			if (list.size() > 1)
				s_log.warning("More than one C_InvoiceLine of " + sLine);
		}

		return retValue;
	}	//	getOfInOutLine

	/**
	 * 	Get Invoice Line referencing InOut Line - from MatchInv
	 *	@param sLine shipment line
	 *	@return (first) invoice line
	 */
	public static MInvoiceLine getOfInOutLineFromMatchInv(MInOutLine sLine) {
		if (sLine == null)
			return null;
		final String whereClause = "C_InvoiceLine_ID IN (SELECT C_InvoiceLine_ID FROM M_MatchInv WHERE M_InOutLine_ID=?)";
		List<MInvoiceLine> list = new Query(sLine.getCtx(),I_C_InvoiceLine.Table_Name,whereClause,sLine.get_TrxName())
		.setParameters(sLine.getM_InOutLine_ID())
		.list();
		
		MInvoiceLine retValue = null;
		if (list.size() > 0) {
			retValue = list.get(0);
			if (list.size() > 1)
				s_log.warning("More than one C_InvoiceLine of " + sLine);
		}

		return retValue;
	}

	/**	Static Logger	*/
	protected static CLogger	s_log	= CLogger.getCLogger (MInvoiceLine.class);

	/** Tax							*/
	protected MTax 		m_tax = null;
	
	
	/**************************************************************************
	 * 	Invoice Line Constructor
	 * 	@param ctx context
	 * 	@param C_InvoiceLine_ID invoice line or 0
	 * 	@param trxName transaction name
	 */
	public MInvoiceLine (Properties ctx, int C_InvoiceLine_ID, String trxName)
	{
		super (ctx, C_InvoiceLine_ID, trxName);
		
	}	//	MInvoiceLine

	/**
	 * 	Parent Constructor
	 * 	@param invoice parent
	 */
	public MInvoiceLine (MInvoice invoice)
	{
		this (invoice.getCtx(), 0, invoice.get_TrxName());
		if (invoice.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
		setC_Invoice_ID (invoice.getC_Invoice_ID());
	}	//	MInvoiceLine


	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MInvoiceLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoiceLine

	protected int			m_M_PriceList_ID = 0;
	protected Timestamp	m_DateInvoiced = null;
	protected int			m_C_BPartner_ID = 0;
	protected int			m_C_BPartner_Location_ID = 0;
	protected boolean		m_IsSOTrx = true;
	protected boolean		m_priceSet = false;
	protected MProduct	m_product = null;
	
	/**	Cached Name of the line		*/
	protected String		m_name = null;
	/** Cached Precision			*/
	protected Integer		m_precision = null;
	/** Product Pricing				*/
	protected IProductPricing	m_productPricing = null;
	/** Parent						*/
	protected MInvoice	m_parent = null;

	
	public MInvoice getParent()
	{
		if (m_parent == null)
			m_parent = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
		return m_parent;
	}	//	getParent

	
	
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MInvoiceLine[")
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	protected boolean beforeSave (boolean newRecord)
	{
		
		
		return true;
	}	//	beforeSave

	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
    	return true;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;

		
		return true;
	}	//	afterDelete

	

	public void clearParent()
	{
		this.m_parent = null;
	}

}	//	MInvoiceLine

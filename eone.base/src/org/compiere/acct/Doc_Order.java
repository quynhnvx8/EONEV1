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
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MCurrency;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MTax;

/**
 *  Post Order Documents.
 *  <pre>
 *  Table:              C_Order (259)
 *  Document Types:     SOO, POO
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Order.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Order extends Doc
{
	/**
	 *  Constructor
	 * 	@param as accounting schema
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Order (ResultSet rs, String trxName)
	{
		super (MOrder.class, rs, trxName);
	}	//	Doc_Order

	/** Contained Optional Tax Lines    */
	private DocTax[]        m_taxes = null;
	
	private int				m_precision = -1;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MOrder order = (MOrder)getPO();
		setIsTaxIncluded(order.isTaxIncluded());
		//	Amounts
		setAmount(AMTTYPE_Gross, order.getGrandTotal());
		setAmount(AMTTYPE_Net, order.getTotalLines());
		setAmount(AMTTYPE_Charge, order.getChargeAmt());

		
		p_lines = loadLines(order);
	//	log.fine( "Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}   //  loadDocumentDetails


	/**
	 *	Load Invoice Line
	 *	@param order order
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MOrder order)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MOrderLine[] lines = order.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MOrderLine line = lines[i];
			DocLine docLine = new DocLine (line, this);
			BigDecimal LineNetAmt = null;
			LineNetAmt = line.getLineNetAmt();
			docLine.setAmount (LineNetAmt);	//	DR
			BigDecimal PriceList = line.getPriceList();
			int C_Tax_ID = docLine.getC_Tax_ID();
			//	Correct included Tax
			if (isTaxIncluded() && C_Tax_ID != 0)
			{
				MTax tax = MTax.get(getCtx(), C_Tax_ID);
				if (!tax.isZeroTax())
				{
					BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, getStdPrecision());
					if (log.isLoggable(Level.FINE)) log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
					LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
					for (int t = 0; t < m_taxes.length; t++)
					{
						if (m_taxes[t].getC_Tax_ID() == C_Tax_ID)
						{
							m_taxes[t].addIncludedTax(LineNetAmtTax);
							break;
						}
					}
					BigDecimal PriceListTax = tax.calculateTax(PriceList, true, getStdPrecision());
					PriceList = PriceList.subtract(PriceListTax);
				}
			}	//	correct included Tax

			docLine.setAmount (LineNetAmt);
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dl = new DocLine[list.size()];
		list.toArray(dl);
		return dl;
	}	//	loadLines


	
	private int getStdPrecision()
	{
		if (m_precision == -1)
			m_precision = MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
		return m_precision;
	}	//	getPrecision

	
	public ArrayList<Fact> createFacts ()
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		
		return facts;
	}   //  createFact


	

}   //  Doc_Order
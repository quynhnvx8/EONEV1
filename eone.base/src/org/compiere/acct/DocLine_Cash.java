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

import org.compiere.model.MCashLine;
import org.compiere.util.Env;

/**
 *  Cash Journal Line
 *
 *  @author Jorg Janke
 *  @version  $Id: DocLine_Cash.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class DocLine_Cash extends DocLine
{
	/**
	 *  Constructor
	 *  @param line cash line
	 *  @param doc header
	 */
	public DocLine_Cash (MCashLine line, Doc_Cash doc)
	{
		super (line, doc);
		
		//
		m_Amount = line.getAmount();
		m_AmountConvert = line.getAmountConvert();
		Account_Dr_ID = line.getAccount_Dr_ID();
		Account_Cr_ID = line.getAccount_Cr_ID();
		setAmount(m_Amount);
		setAmountConvert(m_AmountConvert);


	}   //  DocLine_Cash

	/** Cash Type               */

	//  AD_Reference_ID=217
	/** Charge - C		*/
	public static final String  CASHTYPE_CHARGE = "C";
	/** Difference - D	*/
	public static final String  CASHTYPE_DIFFERENCE = "D";
	/** Expense - E		*/
	public static final String  CASHTYPE_EXPENSE = "E";
	/** Onvoice - I 	*/
	public static final String  CASHTYPE_INVOICE = "I";
	/** Receipt - R		*/
	public static final String  CASHTYPE_RECEIPT = "R";
	/** Transfer - T	*/
	public static final String  CASHTYPE_TRANSFER = "T";

	//  References
	private int     m_C_BankAccount_ID = 0;
	private int     m_C_Invoice_ID = 0;

	//  Amounts
	private BigDecimal      m_Amount = Env.ZERO;
	private BigDecimal      m_AmountConvert = Env.ZERO;

	private int Account_Dr_ID = 0;
	private int Account_Cr_ID = 0;
	
	public int getC_BankAccount_ID()
	{
		return m_C_BankAccount_ID;
	}   //  getC_BankAccount_ID

	/**
	 *  Get Invoice
	 *  @return C_Invoice_ID
	 */
	public int getC_Invoice_ID()
	{
		return m_C_Invoice_ID;
	}   //  getC_Invoice_ID

	/**
	 *  Get Amount
	 *  @return Payment Amount
	 */
	public BigDecimal getAmount()
	{
		return m_Amount;
	}
	/**
	 *  Get Discount
	 *  @return Discount Amount
	 */
	public BigDecimal getAmountConvert()
	{
		return m_AmountConvert;
	}
	

	public int getAccount_Dr_ID() {
		return Account_Dr_ID;
	}
	
	public int getAccount_Cr_ID() {
		return Account_Cr_ID;
	}
}   //  DocLine_Cash

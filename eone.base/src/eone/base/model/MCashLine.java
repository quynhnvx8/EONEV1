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
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	Cash Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCashLine.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 *  
 *  @author Teo Sarca, SC ARHIPAC SERVICE SRL
 *  			<li>BF [ 1760240 ] CashLine bank account is filled even if is not bank transfer
 *  			<li>BF [ 1918266 ] MCashLine.updateHeader should ignore not active lines
 * 				<li>BF [ 1918290 ] MCashLine.createReversal should inactivate if not processed
 */
public class MCashLine extends X_C_CashLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5023249596033465923L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CashLine_ID id
	 *	@param trxName transaction
	 */
	public MCashLine (Properties ctx, int C_CashLine_ID, String trxName)
	{
		super (ctx, C_CashLine_ID, trxName);
		if (C_CashLine_ID == 0)
		{
			setAmount (Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setIsGenerated(false);
		}
	}	//	MCashLine

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MCashLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCashLine
	
	/**
	 * 	Parent Cosntructor
	 *	@param cash parent
	 */
	public MCashLine (MCash cash)
	{
		this (cash.getCtx(), 0, cash.get_TrxName());
		setClientOrg(cash);
		setC_Cash_ID(cash.getC_Cash_ID());
		m_parent = cash;
	}	//	MCashLine

	/** Parent					*/
	protected MCash		m_parent = null;
	protected MInvoice		m_invoice = null;
	

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	
	public MCash getParent()
	{
		if (m_parent == null)
			m_parent = new MCash (getCtx(), getC_Cash_ID(), get_TrxName());
		return m_parent;
	}	//	getCash
	
	
	protected boolean beforeDelete ()
	{
		//	Cannot Delete generated Invoices
		Boolean generated = (Boolean)get_ValueOld("IsGenerated");
		if (generated != null && generated.booleanValue())
		{
			if (get_ValueOld("C_Invoice_ID") != null)
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "CannotDeleteCashGenInvoice"));
				return false;
			}
		}
		return true;
	}	//	beforeDelete

	/**
	 * 	After Delete
	 *	@param success
	 *	@return true/false
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterDelete

	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true/false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord && getParent().isComplete()) {
			log.saveError("ParentComplete", Msg.translate(getCtx(), "C_CashLine"));
			return false;
		}
		//	Cannot change generated Invoices
		if (is_ValueChanged(COLUMNNAME_C_Invoice_ID))
		{
			Object generated = get_ValueOld(COLUMNNAME_IsGenerated);
			if (generated != null && ((Boolean)generated).booleanValue())
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "CannotChangeCashGenInvoice"));
				return false;
			}
		}
		
		BigDecimal rate = getParent().getCurrencyRate();
		setAmountConvert(getAmount().multiply(rate));
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterSave
	
	/**
	 * 	Update Cash Header.
	 * 	Statement Difference, Ending Balance
	 *	@return true if success
	 */
	protected boolean updateHeader()
	{
		String sql = "UPDATE C_Cash c"
			+ " SET (Amount, AmountConvert)="
				+ "(SELECT Sum(Amount), Sum(AmountConvert) "
				+ "FROM C_CashLine cl "
				+ "WHERE cl.C_Cash_ID=c.C_Cash_ID"
				+ " AND cl.IsActive='Y'"
				+") "
			+ "WHERE C_Cash_ID=" + getC_Cash_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.warning("Amount, AmountConvert #" + no);
		return no == 1;
	}	//	updateHeader
	
}	//	MCashLine

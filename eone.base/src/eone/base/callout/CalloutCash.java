

package eone.base.callout;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;

/**
 *	Cash Book Journal Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutCashJournal.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class CalloutCash extends CalloutEngine
{
	/**
	 *  Cash Journal Line Invoice.
	 *  when Invoice selected
	 *  - set C_Currency, DiscountAnt, Amount, WriteOffAmt
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String invoice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";

		
		return "";
	}	//	CashJournal_Invoice

	
	/**
	 *  Cash Journal Line Invoice Amounts.
	 *  when DiscountAnt, Amount, WriteOffAmt change
	 *  making sure that add up to InvTotalAmt (created by CashJournal_Invoice)
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String amounts (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//  Needs to be Invoice
		if (isCalloutActive() || !"I".equals(mTab.getValue("CashType")))
			return "";
		//  Check, if InvTotalAmt exists
		String total = Env.getContext(ctx, WindowNo, "InvTotalAmt");
		if (total == null || total.length() == 0)
			return "";
		BigDecimal InvTotalAmt = new BigDecimal(total);

		BigDecimal PayAmt = (BigDecimal)mTab.getValue("Amount");
		BigDecimal DiscountAmt = (BigDecimal)mTab.getValue("DiscountAmt");
		BigDecimal WriteOffAmt = (BigDecimal)mTab.getValue("WriteOffAmt");
		String colName = mField.getColumnName();
		if (log.isLoggable(Level.FINE)) log.fine(colName + " - Invoice=" + InvTotalAmt
			+ " - Amount=" + PayAmt + ", Discount=" + DiscountAmt + ", WriteOff=" + WriteOffAmt);

		//  Amount - calculate write off
		if (colName.equals("Amount"))
		{
			WriteOffAmt = InvTotalAmt.subtract(PayAmt).subtract(DiscountAmt);
			mTab.setValue("WriteOffAmt", WriteOffAmt);
		}
		else    //  calculate PayAmt
		{
			PayAmt = InvTotalAmt.subtract(DiscountAmt).subtract(WriteOffAmt);
			mTab.setValue("Amount", PayAmt);
		}

		return "";
	}	//	amounts

}	//	CalloutCashJournal

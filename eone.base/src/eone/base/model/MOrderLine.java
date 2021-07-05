
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MOrderLine extends X_C_OrderLine
{
	private static final long serialVersionUID = -7152360636393521683L;

	protected static CLogger s_log = CLogger.getCLogger (MOrderLine.class);
	
	public MOrderLine (Properties ctx, int C_OrderLine_ID, String trxName)
	{
		super (ctx, C_OrderLine_ID, trxName);
		if (C_OrderLine_ID == 0)
		{
			setAmount (Env.ZERO);
			setPrice (Env.ZERO);
			setQty (Env.ZERO);	// 1
			setProcessed (false);
			setLine (0);
		}
	}	//	MOrderLine
	
	
	public MOrderLine (MOrder order)
	{
		this (order.getCtx(), 0, order.get_TrxName());
		if (order.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setC_Order_ID (order.getC_Order_ID());	//	parent
		setOrder(order);
	}	//	MOrderLine

	public MOrderLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MOrderLine

	
	protected MProduct 		m_product = null;
	/** Parent					*/
	protected MOrder			m_parent = null;
	
	public void setOrder (MOrder order)
	{
		//
		setHeaderInfo(order);	//	sets m_order
	}	//	setOrder

	public void setHeaderInfo (MOrder order)
	{
		m_parent = order;
	}	//	setHeaderInfo
	
	public MOrder getParent()
	{
		if (m_parent == null)
			m_parent = new MOrder(getCtx(), getC_Order_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	
	
	
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MOrderLine[")
			.append(get_ID())
			.append(", Line=").append(getLine())
			.append(", Ordered=").append(getQty())
			.append(", Amount=").append(getAmount())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	
	public String getDescriptionText()
	{
		return super.getDescription();
	}	//	getDescriptionText
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord && getParent().isComplete()) {
			log.saveError("ParentComplete", Msg.translate(getCtx(), "C_OrderLine"));
			return false;
		}
		
		
		return true;
	}	//	beforeSave

	protected boolean beforeDelete ()
	{
		
		return true;
	}	//	beforeDelete
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (getParent().isProcessed())
			return success;
		
		updateHeader();
		
		return success;
	}	//	afterSave
	
	protected void updateHeader() {
		String sql = "Update C_Order o Set (Amount, TaxAmt, TaxBaseAmt) = " +
				" (Select Sum(Amount) Amount, Sum(TaxAmt) TaxAmt, Sum(TaxBaseAmt) TaxBaseAmt From C_OrderLine l where l.C_Order_ID = o.C_Order_ID )" +
				" Where C_Order_ID = ?";
		DB.executeUpdate(sql, getC_Order_ID(), true, get_TrxName(), 10);
	}

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
}	//	MOrderLine

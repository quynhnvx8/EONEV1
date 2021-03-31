
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MAccount;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class DocLine
{	
	
	public DocLine (PO po, Doc doc)
	{
		if (po == null)
			throw new IllegalArgumentException("PO is null");
		p_po = po;
		m_doc = doc;
		//
		//  Document Consistency
		if (p_po.getAD_Org_ID() == 0)
			p_po.setAD_Org_ID(m_doc.getAD_Org_ID());
	}	//	DocLine

	/** Persistent Object		*/
	protected PO				p_po = null;
	/** Parent					*/
	private Doc					m_doc = null;
	/**	 Log					*/
	protected transient CLogger	log = CLogger.getCLogger(getClass());

	/** Qty                     */
	private BigDecimal	 		m_qty = null;

	//  -- GL Amounts
	/** Debit Journal Amt   	*/
	private BigDecimal      	m_Amount = Env.ZERO;
	/** Credit Journal Amt		*/
	private BigDecimal      	m_AmountConvert = Env.ZERO;
	
	
	/** Account used only for GL Journal    */
	private MAccount 			m_account = null;

	/** Accounting Date				*/
	private Timestamp			m_DateAcct = null;
	
	private int					m_C_BPartner_ID = -1;
	
	/** Currency					*/
	private int					m_C_Currency_ID = -1;
	/** Period						*/
	private int					m_C_Period_ID = -1;

	/**
	 *  Get Currency
	 *  @return c_Currency_ID
	 */
	public int getC_Currency_ID ()
	{
		if (m_C_Currency_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_Currency_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_Currency_ID = ii.intValue();
			}
			if (m_C_Currency_ID <= 0)
				m_C_Currency_ID = m_doc.getC_Currency_ID();
		}
		return m_C_Currency_ID;
	}   //  getC_Currency_ID

	
	public void setAmount (BigDecimal sourceAmt)
	{
		m_Amount = sourceAmt == null ? Env.ZERO : sourceAmt;
	}   //  setAmounts

	
	public void setAmountConvert (BigDecimal AmountConvert)
	{
		m_AmountConvert = AmountConvert == null ? Env.ZERO : AmountConvert;
	}   //  setConvertedAmt

	/**
	 *  Line Net Amount or Dr-Cr
	 *  @return balance
	 */
	public BigDecimal getAmount()
	{
		return m_Amount;
	}   //  getAmount

	public BigDecimal getAmountConvert()
	{
		return m_AmountConvert;
	}   //  getAmount

	
	
	public void setDateAcct (Timestamp dateAcct)
	{
		m_DateAcct = dateAcct;
	}   //  setDateAcct

	/**
	 *  Get Accounting Date
	 *  @return accounting date
	 */
	public Timestamp getDateAcct ()
	{
		if (m_DateAcct != null)
			return m_DateAcct;
		int index = p_po.get_ColumnIndex("DateAcct");
		if (index != -1)
		{
			m_DateAcct = (Timestamp)p_po.get_Value(index);
			if (m_DateAcct != null)
				return m_DateAcct;
		}
		m_DateAcct = m_doc.getDateAcct();
		return m_DateAcct;
	}   //  getDateAcct

	
	public void setAccount (MAccount acct)
	{
		m_account = acct;
	}   //  setAccount

	/**
	 *  Get GL Journal Account
	 *  @return account
	 */
	public MAccount getAccount()
	{
		return m_account;
	}   //  getAccount

	
	protected int getC_Period_ID()
	{
		if (m_C_Period_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_Period_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_Period_ID = ii.intValue();
			}
			if (m_C_Period_ID == -1)
				m_C_Period_ID = 0;
		}
		return m_C_Period_ID;
	}	//	getC_Period_ID
	
	/**
	 * 	Set C_Period_ID
	 *	@param C_Period_ID id
	 */
	protected void setC_Period_ID (int C_Period_ID)
	{
		m_C_Period_ID = C_Period_ID;
	}	//	setC_Period_ID
	
	
	public int get_ID()
	{
		return p_po.get_ID();
	}	//	get_ID
	
	/**
	 * 	Get AD_Org_ID
	 *	@return org
	 */
	public int getAD_Org_ID()
	{
		return p_po.getAD_Org_ID();
	}	//	getAD_Org_ID
	
	
	/**
	 *  Product
	 *  @return M_Product_ID
	 */
	public int getM_Product_ID()
	{
		int index = p_po.get_ColumnIndex("M_Product_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   //  getM_Product_ID

	

	
	public int getM_Warehouse_Dr_ID()
	{
		int index = p_po.get_ColumnIndex("M_Warehouse_Dr_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   

	
	public int getM_Warehouse_Cr_ID()
	{
		int index = p_po.get_ColumnIndex("M_Warehouse_Cr_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   

	
	public int getC_UOM_ID()
	{
		//	Trx UOM
		int index = p_po.get_ColumnIndex("C_UOM_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		
		return 0;
	}   //  getC_UOM

	/**
	 *  Quantity
	 *  @param qty transaction Qty
	 * 	@param isSOTrx SL order trx (i.e. negative qty)
	 */
	public void setQty (BigDecimal qty, boolean isSOTrx)
	{
		if (qty == null)
			m_qty = Env.ZERO;
		else if (isSOTrx)
			m_qty = qty.negate();
		else
			m_qty = qty;
	}   //  setQty

	/**
	 *  Quantity
	 *  @return transaction Qty
	 */
	public BigDecimal getQty()
	{
		return m_qty;
	}   //  getQty

	
	
	/**
	 *  Description
	 *  @return doc line description
	 */
	public String getDescription()
	{
		int index = p_po.get_ColumnIndex("Description");
		if (index != -1)
			return (String)p_po.get_Value(index);
		return null;
	}	//	getDescription

	/**
	 *  Line Tax
	 *  @return C_Tax_ID
	 */
	public int getC_Tax_ID()
	{
		int index = p_po.get_ColumnIndex("C_Tax_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}	//	getC_Tax_ID

	/**
	 *  Get Line Number
	 *  @return line no
	 */
	public int getLine()
	{
		int index = p_po.get_ColumnIndex("Line");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   //  getLine

	/**
	 *  Get BPartner
	 *  @return C_BPartner_ID
	 */
	public int getC_BPartner_ID()
	{
		if (m_C_BPartner_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_BPartner_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_BPartner_ID = ii.intValue();
			}
			if (m_C_BPartner_ID <= 0)
				m_C_BPartner_ID = m_doc.getC_BPartner_ID();
		}
		return m_C_BPartner_ID;
	}   //  getC_BPartner_ID

	/**
	 * 	Set C_BPartner_ID
	 *	@param C_BPartner_ID id
	 */
	protected void setC_BPartner_ID (int C_BPartner_ID)
	{
		m_C_BPartner_ID = C_BPartner_ID;
	}	//	setC_BPartner_ID
	
	
	

	
	/**
	 *  Get Project
	 *  @return C_Project_ID
	 */
	public int getC_Project_ID()
	{
		int index = p_po.get_ColumnIndex("C_Project_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   //  getC_Project_ID

	

	public int getC_TypeCost_ID()
	{
		int index = p_po.get_ColumnIndex("C_TypeCost_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   

	public int getC_TypeRevenue_ID()
	{
		int index = p_po.get_ColumnIndex("C_TypeRevenue_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   
        
        	/**
	 *  Get User Defined Column
	 *  @param ColumnName column name
	 *  @return user defined column value
	 */
	public int getValue(String ColumnName)
	{
		int index = p_po.get_ColumnIndex(ColumnName);
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}   //  getValue

	//AZ Goodwill
	private int         		m_ReversalLine_ID = 0;
	/**
	 *  Set ReversalLine_ID
	 *  store original (voided/reversed) document line
	 *  @param ReversalLine_ID
	 */
	public void setReversalLine_ID (int ReversalLine_ID)
	{
		m_ReversalLine_ID = ReversalLine_ID;
	}   //  setReversalLine_ID

	/**
	 *  Get ReversalLine_ID
	 *  get original (voided/reversed) document line
	 *  @return ReversalLine_ID
	 */
	public int getReversalLine_ID()
	{
		return m_ReversalLine_ID;
	}   //  getReversalLine_ID
	//end AZ Goodwill
	
	public PO getPO() 
	{
		return p_po;
	}
	
	/**
	 *  String representation
	 *  @return String
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("DocLine=[");
		sb.append(p_po.get_ID());
		if (getDescription() != null)
			sb.append(",").append(getDescription());
		if (getM_Product_ID() != 0)
			sb.append(",M_Product_ID=").append(getM_Product_ID());
		sb.append(",Qty=").append(m_qty)
			.append(",Amt=").append(getAmount())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	DocumentLine

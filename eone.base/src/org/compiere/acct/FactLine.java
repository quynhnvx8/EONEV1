
package org.compiere.acct;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.MElementValue;
import org.compiere.model.X_Fact_Acct;
import org.compiere.util.Env;


public final class FactLine extends X_Fact_Acct
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6141312459030795891L;


	public FactLine (Properties ctx, int AD_Table_ID, int Record_ID, int Line_ID, String trxName)
	{
		super(ctx, 0, trxName);
		setAD_Client_ID(0);							//	do not derive
		setAD_Org_ID(0);							//	do not derive
		//
		setAmount (Env.ZERO);
		setAmountConvert (Env.ZERO);
		setAD_Table_ID (AD_Table_ID);
		setRecord_ID (Record_ID);
		setLine_ID (Line_ID);
	}   //  FactLine

	private MElementValue	m_acct = null;
	private MElementValue m_acctCr = null;
	
	private Doc			m_doc = null;
	private DocLine 	m_docLine = null;
	
	public void setAccount (MElementValue dr, MElementValue cr)
	{
		m_acct = dr;
		m_acctCr = cr;
		if (dr != null)
			setAccount_Dr_ID(dr.getC_ElementValue_ID());
		if (cr != null)
			setAccount_Cr_ID(cr.getC_ElementValue_ID());
	} 
	
	public void setAmount(int C_Currency_ID, BigDecimal rate, BigDecimal Amount , BigDecimal AmountConvert) {
		setC_Currency_ID(C_Currency_ID);
		setCurrencyRate(rate);
		setAmount(Amount);
		setAmountConvert(AmountConvert);
	}
	
	public void setDocumentInfo(Doc doc, DocLine docLine)
	{
		m_doc = doc;
		m_docLine = docLine;
		//	reset
		setAD_Org_ID(m_doc.getAD_Org_ID());
		setAD_Department_ID(m_doc.getAD_Department_ID());
		setAD_Client_ID (m_doc.getAD_Client_ID());
		setC_DocType_ID(m_doc.getC_DocType_ID());
		setC_DocTypeSub_ID(m_doc.getC_DocTypeSub_ID());
		setDateAcct (m_doc.getDateAcct());
		setDocumentNo(m_doc.getDocumentNo());
		setAD_Window_ID(m_doc.getAD_Window_ID());
		if (m_docLine != null &&  m_docLine.getC_Period_ID() != 0)
			setC_Period_ID(m_docLine.getC_Period_ID());
		else
			setC_Period_ID (m_doc.getC_Period_ID());
		if (m_docLine != null)
			setC_Tax_ID (m_docLine.getC_Tax_ID());
		//	Description
		setDescription(m_doc.getDescription());
		
		
	}   //  setDocumentInfo
	
	public void setDocumentInfoHead(Doc doc)
	{
		m_doc = doc;
		//	reset
		setAD_Org_ID(m_doc.getAD_Org_ID());
		setAD_Client_ID (m_doc.getAD_Client_ID());
		
		setDateAcct (m_doc.getDateAcct());
		setDocumentNo(m_doc.getDocumentNo());
		
		if (m_doc != null &&  m_doc.getC_Period_ID() != 0)
			setC_Period_ID(m_doc.getC_Period_ID());
		else
			setC_Period_ID (m_doc.getC_Period_ID());
		if (m_doc != null)
			setC_Tax_ID (m_doc.getC_Tax_ID());
		//	Description
		setDescription(m_doc.getDescription());
		setAD_Window_ID(m_doc.getAD_Window_ID());
		
		
	}

	/**
	 * 	Get Document Line
	 *	@return doc line
	 */
	public DocLine getDocLine()
	{
		return m_docLine;
	}	//	getDocLine
	
	
	public void setM_Warehouse_Dr_ID (int M_Warehouse_ID)
	{
		super.setM_Warehouse_Dr_ID (M_Warehouse_ID);
		
	}   
	
	public void setM_Warehouse_Cr_ID (int M_Warehouse_ID)
	{
		super.setM_Warehouse_Cr_ID (M_Warehouse_ID);
		
	}   

	
	public MElementValue getAccountDr()
	{
		return m_acct;
	}	//	getAccount
	
	/**
	 * 	Get Account
	 *	@return account
	 */
	public MElementValue getAccountCr()
	{
		return m_acctCr;
	}
	/**
	 *	To String
	 *  @return String
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("FactLine=[");
		sb.append(getAD_Table_ID()).append(":").append(getRecord_ID())
			.append(",").append(m_acct)
			.append(",Cur=").append(getC_Currency_ID())
			.append(", DR=").append(getAmount()).append("|").append(getAmountConvert())
			.append("]");
		return sb.toString();
	}	//	toString

	
}	//	FactLine
